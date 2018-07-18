package com.example.palak.eddvissintegratedproject;

public class ChapterDetail {
    private Chapter responseObject;
    private String errorCode;
    private String errorMessage;

    public Chapter getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Chapter responseObject) {
        this.responseObject = responseObject;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
