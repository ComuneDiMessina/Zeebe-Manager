package it.almaviva.zeebe.manager.integration.jgitclient.domain;

import lombok.Data;

@Data
public class LogResult{

    private String name;
    private String email;
    private String fileName;
    private String commitSha;
    private String comment;

    
    
    
    
}