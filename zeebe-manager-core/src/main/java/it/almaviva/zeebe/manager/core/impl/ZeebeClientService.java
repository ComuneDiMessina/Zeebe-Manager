package it.almaviva.zeebe.manager.core.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.Topology;
import it.almaviva.zeebe.manager.core.IZeebeClientService;
import it.almaviva.zeebe.manager.domain.DiagramItemDTO;
import it.almaviva.zeebe.manager.integration.zeebe.ZeebeClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ZeebeClientService implements IZeebeClientService {


	@Autowired
	public ZeebeClient zeebeClient;

	@Override
	public DeploymentEvent deployDiagram(DiagramItemDTO dto) throws Exception {

		try {

			byte[] decodedFile = Base64.getDecoder()
					.decode(
							dto.getBase64EncodedDiagram()
							.getBytes(StandardCharsets.UTF_8)
							);

			DeploymentEvent event = zeebeClient.deployBpmnDiagram(decodedFile, dto.getDiagramName());
			return event;


		} catch (Exception e) {
			log.error("Deploy del diagramma non riuscito", e);
			throw new Exception(e.getMessage(), e.getCause());
		}

	}

	@Override
	public Topology monitorZeebe() throws Exception {
	
		try {
			
			Topology item = zeebeClient.monitorZeebe();
			return item;
			
		} catch (Exception e) {
			log.error("Funzione di montiraggio non riuscita", e);
			throw new Exception(e.getMessage(), e.getCause());
		}
		
		
	}

}
