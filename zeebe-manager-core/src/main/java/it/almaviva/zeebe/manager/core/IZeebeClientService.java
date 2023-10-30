package it.almaviva.zeebe.manager.core;

import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.Topology;
import it.almaviva.zeebe.manager.domain.DiagramItemDTO;

public interface IZeebeClientService {
	
	public DeploymentEvent deployDiagram(DiagramItemDTO dto) throws Exception;
	public Topology monitorZeebe() throws Exception;

}
