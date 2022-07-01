package com.coffeecat.springbootcourse.status;

public class ImageUploadStatus {
    private String message;

    public ImageUploadStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
