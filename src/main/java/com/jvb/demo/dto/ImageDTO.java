package com.jvb.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.jvb.demo.entity.Image;

public class ImageDTO {
    private Long id;
    private String url;
    private MultipartFile file;

    public ImageDTO() {
    }

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
