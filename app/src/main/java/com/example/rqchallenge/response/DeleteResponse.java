package com.example.rqchallenge.response;

public class DeleteResponse {
    private String status;
    private String message;

    @Override
    public String toString() {
        return "DeleteResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
