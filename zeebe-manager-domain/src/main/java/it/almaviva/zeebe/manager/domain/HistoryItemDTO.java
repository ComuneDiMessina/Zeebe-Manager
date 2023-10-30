package it.almaviva.zeebe.manager.domain;

import lombok.Data;

@Data
public class HistoryItemDTO {
	
	private String commitSha;
	private String filename;
	private String author;
	private String email;
	private String message;

}
