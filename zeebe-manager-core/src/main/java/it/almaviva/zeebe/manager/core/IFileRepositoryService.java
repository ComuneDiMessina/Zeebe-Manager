package it.almaviva.zeebe.manager.core;

import java.util.List;

import it.almaviva.zeebe.manager.core.exceptions.GitTransactionException;
import it.almaviva.zeebe.manager.core.exceptions.RepositoryNotInitializedException;
import it.almaviva.zeebe.manager.domain.FileItemDTO;
import it.almaviva.zeebe.manager.domain.HistoryItemDTO;
import it.almaviva.zeebe.manager.integration.jgitclient.domain.LogResult;


public interface IFileRepositoryService{

    public boolean initRepo() throws RepositoryNotInitializedException;
    public void saveFile(FileItemDTO dto) throws GitTransactionException;
    public void revertFile(HistoryItemDTO dto) throws GitTransactionException;
    public FileItemDTO readFile(String filePath) throws Exception;
    public List<LogResult> obtainMetaInfo(String filePath) throws GitTransactionException;
    public List<FileItemDTO> listFiles() throws Exception;
}