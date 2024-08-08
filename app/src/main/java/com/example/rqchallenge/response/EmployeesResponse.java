package com.example.rqchallenge.response;

import com.example.rqchallenge.employees.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeesResponse {
    private String status;
    private List<Employee> data;

    private String message;

    public EmployeesResponse() {

    }

    public String getStatus() {
        return status;
    }

    public List<Employee> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
