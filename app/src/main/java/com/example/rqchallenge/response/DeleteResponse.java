package com.example.rqchallenge.response;

public class DeleteResponse {
    private String status;
    private String message;

    public DeleteResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public DeleteResponse() {
    }

    @Override
    public String toString() {
        return "DeleteResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
