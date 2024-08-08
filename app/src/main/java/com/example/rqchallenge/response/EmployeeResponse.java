package com.example.rqchallenge.response;

import com.example.rqchallenge.employees.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {
    private String status;
    private String message;
    private Employee data;

    public EmployeeResponse() {

    }

    public EmployeeResponse(String status, Employee data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Employee getData() {
        return data;
    }
}
