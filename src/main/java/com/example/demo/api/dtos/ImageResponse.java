package com.example.demo.api.dtos;

import lombok.Data;

@Data
public class ImageResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public ImageResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
