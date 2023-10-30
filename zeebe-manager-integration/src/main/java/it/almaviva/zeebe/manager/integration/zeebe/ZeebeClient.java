package it.almaviva.zeebe.manager.integration.zeebe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.Topology;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;

@Component
@EnableZeebeClient
public class ZeebeClient {

    @Autowired
    ZeebeClientLifecycle clientLifecycle;

    public DeploymentEvent deployBpmnDiagram(byte[] diagramBytes, String fileName) throws Exception{
        
        DeploymentEvent event = clientLifecycle.
            newDeployCommand()
            .addResourceBytes(diagramBytes, fileName)
            .send()
            .join();

        return event;
    }
    
    public Topology monitorZeebe() throws Exception{
    	
    	Topology item = clientLifecycle.newTopologyRequest().send().join();
    	return item;
    	
    }

    
    
}