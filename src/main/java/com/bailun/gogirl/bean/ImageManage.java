package com.bailun.gogirl.bean;

import java.io.Serializable;


public class ImageManage implements Serializable {

    private Integer id;
    private String name;
    private String imgGroup;
    private String url;
    private String typeName;

    public Integer getId() {
        return id;
    }

    public ImageManage setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageManage setName(String name) {
        this.name = name;
        return this;
    }

    public String getImgGroup() {
        return imgGroup;
    }

    public ImageManage setImgGroup(String imgGroup) {
        this.imgGroup = imgGroup;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ImageManage setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "ImageManage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgGroup='" + imgGroup + '\'' +
                ", url='" + url + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
