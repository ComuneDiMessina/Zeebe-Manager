package it.almaviva.zeebe.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.almaviva.zeebe.manager.core.IFileRepositoryService;
import it.almaviva.zeebe.manager.domain.FileItemDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/fs")
public class FileSystemRepositoryController{

    @Autowired
    IFileRepositoryService fileRepositoryService;

    @GetMapping(value = "/file/{fileName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FileItemDTO readFile(@PathVariable String fileName) {

        FileItemDTO dto;

        if(log.isDebugEnabled())
            log.debug("API GET: /fs/file called with param: {}", fileName);

        try{
            dto = fileRepositoryService.readFile(fileName);
        } catch(Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "File: " + fileName + " non trovato.");
        }

        return dto;
    }

    @GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<FileItemDTO>> listFiles() {
        ResponseEntity<List<FileItemDTO>> fileItemsEntity;

        if(log.isDebugEnabled())
            log.debug("API GET: /fs/file called");      
        try{
            List<FileItemDTO> fileItems = fileRepositoryService.listFiles();
            if (fileItems.size() == 0)
                fileItemsEntity = new ResponseEntity<>(fileItems, HttpStatus.NO_CONTENT);
            else
                fileItemsEntity = new ResponseEntity<>(fileItems, HttpStatus.OK);
        } catch(Exception e) {
            throw new ResponseStatusException(
                 HttpStatus.INTERNAL_SERVER_ERROR, "Impossibile visualizzare il contenuto della directory"
            );
        }

        return fileItemsEntity;
    }
    
    
}