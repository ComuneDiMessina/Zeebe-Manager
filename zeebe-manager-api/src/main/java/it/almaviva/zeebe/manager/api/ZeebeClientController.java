package it.almaviva.zeebe.manager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.zeebe.client.api.response.Topology;
import it.almaviva.zeebe.manager.core.IZeebeClientService;
import it.almaviva.zeebe.manager.domain.DiagramItemDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/zeebeclient")
public class ZeebeClientController {
	
	@Autowired
	IZeebeClientService zeebeClientService;
	
	 @PostMapping(value="/deploy", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseEntity<Void> deployDiagram(@RequestBody DiagramItemDTO dto) {

	        ResponseEntity<Void> responseEntity;

	        if(log.isDebugEnabled())
	            log.debug("API POST: /zeebeclient/deploy called");
	            
	        try{
	        	zeebeClientService.deployDiagram(dto);
	            responseEntity = new ResponseEntity<>(HttpStatus.OK);
	        } catch(Exception e) {
	            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        return responseEntity;
	        
	    }
	 
	 @GetMapping(value = "/monitoring", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public Topology monitorZeebe() {
		 
		   Topology item;

	        if(log.isDebugEnabled())
	            log.debug("API GET: /zeebeclient/monitoring called.");

	        try{
	        	item = zeebeClientService.monitorZeebe();
	        } catch(Exception e) {
	            throw new ResponseStatusException(
	                HttpStatus.NOT_FOUND, "API GET: /zeebeclient/monitoring fallita.");
	        }

	        return item;
	    }

}
