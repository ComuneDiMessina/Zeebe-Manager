package it.almaviva.zeebe.manager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiagramItemDTO {
	
    private String diagramName;
    private String base64EncodedDiagram;

}
