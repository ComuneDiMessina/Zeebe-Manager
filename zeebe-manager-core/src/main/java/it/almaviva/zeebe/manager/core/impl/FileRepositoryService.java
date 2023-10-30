package it.almaviva.zeebe.manager.core.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.almaviva.zeebe.manager.core.IFileRepositoryService;
import it.almaviva.zeebe.manager.core.exceptions.GitTransactionException;
import it.almaviva.zeebe.manager.core.exceptions.RepositoryNotInitializedException;
import it.almaviva.zeebe.manager.domain.FileItemDTO;
import it.almaviva.zeebe.manager.domain.HistoryItemDTO;
import it.almaviva.zeebe.manager.integration.jgitclient.JGitClient;
import it.almaviva.zeebe.manager.integration.jgitclient.domain.LogResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileRepositoryService implements IFileRepositoryService {

    @Value("${repo.path}")
    private String repoPath;
    
    @Autowired
    public JGitClient jGitClient;

    @PostConstruct
    public void init() throws Exception{
        try{
            initRepo();
        }catch(Exception e){
            log.error("Errore nell'inizializzazione del repository!", e);
        }
        
    }

    @Override
    public boolean initRepo() throws RepositoryNotInitializedException {
        
        boolean isCreated = false;

        try{
            Path path = Paths.get(repoPath + File.separator + ".git");

            if (Files.notExists(path))
                jGitClient.initialize(repoPath, true);
            else
                jGitClient.initialize(repoPath, false);

            isCreated = true;
        }catch(Exception e){
            log.error("Inizializzazione repository non riuscita", e);
            throw new RepositoryNotInitializedException(e.getMessage(), e.getCause());
        }

        return isCreated;

    }

    @Override
    public FileItemDTO readFile(String filePath) throws Exception {
        FileItemDTO dto = new FileItemDTO();
        Path path = Paths.get(repoPath, File.separator, filePath);
        byte[] bytes = Files.readAllBytes(path);
        String base64Encode =  Base64.getEncoder().encodeToString(bytes);
        dto.setBase64EncodedFile(base64Encode);
        dto.setFilename(filePath);
        return dto;
    }  

    @Override
    public void saveFile(FileItemDTO dto) throws GitTransactionException {
        try{
            byte[] decodedFile = Base64.getDecoder()
                    .decode(
                        dto.getBase64EncodedFile()
                        .getBytes(StandardCharsets.UTF_8)
                        );

            Path destinationFile = Paths.get(repoPath + File.separator + dto.getFilename());
            Files.write(destinationFile, decodedFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            jGitClient.add(dto.getFilename());
            jGitClient.commit(dto.getAuthorName(), dto.getAuthorEmail(), dto.getSaveComment());

        }catch(Exception e){
            log.error("Salvataggio file non riuscito", e);
            throw new GitTransactionException(e.getMessage(), e.getCause());
        }

    }
    
    @Override
    public void revertFile(HistoryItemDTO dto) throws GitTransactionException {
        try{
        	
        	jGitClient.checkout(dto.getCommitSha(),dto.getFilename());
        	jGitClient.commit(dto.getAuthor(), dto.getEmail(), dto.getMessage());

        }catch(Exception e){
            log.error("Revert file non riuscito", e);
            throw new GitTransactionException(e.getMessage(), e.getCause());
        }

    }

    @Override
	public List<LogResult> obtainMetaInfo(String filePath) throws GitTransactionException {
        
        List<LogResult> logResults = null;

        try{
            if (filePath != null)
                logResults = jGitClient.getFileLog(filePath);
            else
                logResults = jGitClient.getFileLog(null);
        }catch(Exception e){
            log.error("Nessun log trovato per il filepath {}", filePath, e);
            throw new GitTransactionException(e.getMessage(), e.getCause());
        }

        return logResults;
	}

    @Override
    public List<FileItemDTO> listFiles() throws Exception {
        
        List<FileItemDTO> dtos = new ArrayList<FileItemDTO>();
        
        Stream<Path> pathStream =  Files.list(Paths.get(repoPath))
        .filter(Files::isRegularFile);

        pathStream.forEach(pathItem -> {
            FileItemDTO dto = new FileItemDTO();
            dto.setFilename(pathItem.getFileName().toString());
            dtos.add(dto); 
        } );

        return dtos;
    }
}