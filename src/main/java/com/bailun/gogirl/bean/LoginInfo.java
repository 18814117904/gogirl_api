package com.bailun.gogirl.bean;

public class LoginInfo extends LoginInfoKey {
    private Byte type;

    private String redirectUri;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri == null ? null : redirectUri.trim();
    }
}