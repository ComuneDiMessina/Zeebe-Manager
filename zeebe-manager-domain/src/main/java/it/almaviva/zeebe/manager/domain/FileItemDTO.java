package it.almaviva.zeebe.manager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileItemDTO{
    private String filename;
    private String base64EncodedFile;
    private String authorEmail;
    private String authorName;
    private String saveComment;
}