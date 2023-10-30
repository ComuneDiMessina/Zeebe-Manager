package it.almaviva.zeebe.manager.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.almaviva.zeebe.manager.core.IFileRepositoryService;
import it.almaviva.zeebe.manager.domain.FileItemDTO;
import it.almaviva.zeebe.manager.domain.HistoryItemDTO;
import it.almaviva.zeebe.manager.integration.jgitclient.domain.LogResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/repo")
public class GitRepositoryController{

    @Autowired
    IFileRepositoryService fileRepositoryService;

    @GetMapping(value = "/file/{fileName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<LogResult>> fileLog(@PathVariable String fileName) throws Exception {
        
        ResponseEntity<List<LogResult>> responseEntityResults;

        if(log.isDebugEnabled())
            log.debug("API GET: /repo/file/"+ fileName +" called");
        
        try{
            List<LogResult> results = fileRepositoryService.obtainMetaInfo(fileName);
            
            if (results.size() == 0)
                responseEntityResults = new ResponseEntity<List<LogResult>>(results, HttpStatus.NO_CONTENT);
            else
                responseEntityResults = new ResponseEntity<List<LogResult>>(results, HttpStatus.OK);

        } catch (Exception e){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Log di commit non presente per il file: " + fileName);
        }

        return responseEntityResults;
        
    }

    @GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<LogResult> fileLog() throws Exception {
        
        List<LogResult> results = null;

        if(log.isDebugEnabled())
            log.debug("API GET: /repo/file called");

        try{
            results = fileRepositoryService.obtainMetaInfo(null);

        } catch (Exception e){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Log di commit non presente per il repository. E' stato fatto almeno un commit?");
        }

        return results;
        
    }


    @PostMapping(value="/file", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> addFile(@RequestBody FileItemDTO dto) {

        ResponseEntity<Void> responseEntity;

        if(log.isDebugEnabled())
            log.debug("API POST: /repo/file called");
            
        try{
            fileRepositoryService.saveFile(dto);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
        
    }
    
    @PutMapping(value="/file/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> checkoutFile(@RequestBody HistoryItemDTO dto) {

        ResponseEntity<Void> responseEntity;

        if(log.isDebugEnabled())
            log.debug("API PUT: /repo/file/checkout called");
            
        try{
            fileRepositoryService.revertFile(dto);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
        
    }

}