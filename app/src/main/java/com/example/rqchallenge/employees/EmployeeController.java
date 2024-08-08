package com.example.rqchallenge.employees;

import com.example.rqchallenge.client.ReliaQuestClient;
import com.example.rqchallenge.response.DeleteResponse;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.response.EmployeesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeController implements IEmployeeController {

    public static final Logger LOGGER = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private ReliaQuestClient reliaQuestClient;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        LOGGER.info("Fetching all employees...");
        List<Employee> employees;
        EmployeesResponse employeesResponse;
        try {
            employeesResponse = reliaQuestClient.webClient().get().uri("/employees").retrieve().bodyToMono(EmployeesResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Fetching all employees failed: {}", String.valueOf(e));
            throw new IOException(String.valueOf(e));
        }
        employees = employeesResponse != null ? employeesResponse.getData() : new ArrayList<>();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        LOGGER.info("Searching employees by name...");
        List<Employee> employees;
        EmployeesResponse employeesResponse = new EmployeesResponse();
        try {
            employeesResponse = reliaQuestClient.webClient().get().uri("/employees").retrieve().bodyToMono(EmployeesResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Error searching employees by name failed: {}", String.valueOf(e));
        }
        if (employeesResponse != null && employeesResponse.getData() != null) {
            employees = employeesResponse.getData().stream()
                    .filter(employee -> employee.getEmployee_name().contains(searchString)).collect(Collectors.toList());
        } else {
            employees = new ArrayList<>();
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        LOGGER.info("Fetching employee by Id...");
        EmployeeResponse employeeResponse;
        try {
            employeeResponse = reliaQuestClient.webClient().get().uri("/employee/{id}", id )
                    .retrieve().bodyToMono(EmployeeResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Fetching employee by Id failed: {}", String.valueOf(e));
            return ResponseEntity.internalServerError().build();
        }
        if (employeeResponse != null) {
            return ResponseEntity.ok(employeeResponse.getData());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        LOGGER.info("Fetching highest salary of employees...");
        EmployeesResponse employeesResponse = null;
        try {
            employeesResponse = reliaQuestClient.webClient().get().uri("/employees").retrieve().bodyToMono(EmployeesResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Fetching of highest salary employee failed: {}", String.valueOf(e));
        }
        if (employeesResponse != null) {
            Employee highestSalaryEmployee = employeesResponse.getData().stream().max(Comparator.comparing(Employee::getEmployee_salary)).orElseThrow();
            return ResponseEntity.ok(highestSalaryEmployee.getEmployee_salary());
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        LOGGER.info("Fetching top ten highest earning employees names...");
        List<Employee> employees;
        EmployeesResponse employeesResponse = null;
        try {
            employeesResponse = reliaQuestClient.webClient().get().uri("/employees").retrieve().bodyToMono(EmployeesResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Fetching top ten highest earning employees names failed: {}", String.valueOf(e));
        }
        employees = employeesResponse != null ? employeesResponse.getData() : new ArrayList<>();
        List<String> topTenHighestSalaries = employees.stream()
                .sorted(Comparator.comparing(Employee::getEmployee_salary)
                        .reversed())
                .limit(10)
                // .map(e -> e.getEmployeeName() + ": $" + e.getEmployeeSalary()) testing purposes
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topTenHighestSalaries);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        LOGGER.info("Creating employee...");
        Employee employee = null;
        try {
            String jsonResponse = reliaQuestClient.webClient().post()
                    .uri("/create")
                    .bodyValue(employeeInput).retrieve().bodyToMono(String.class).block();
            LOGGER.info("Response from API: {}", jsonResponse);
            // Manually parsing because of conflicting names: employee_name, employee_salary & name, salary.
            ObjectMapper objectMapper = new ObjectMapper();
            // TODO: Fix CVE later
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
            Map<String, Object> employeeMap = (Map<String, Object>) responseMap.get("data");
            employee = new Employee((Integer) employeeMap.get("id"),
                    (String) employeeMap.get("name"), Integer.parseInt((String) employeeMap.get("salary")),
                    Integer.parseInt((String) employeeMap.get("age")), null );
        } catch (RuntimeException e) {
            LOGGER.error("Creating employee failed: {}", String.valueOf(e));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (employee != null) {
            LOGGER.info("Create employee succeeded!");
            return ResponseEntity.ok(employee);
        } else {
            LOGGER.info("Create employee failed!");
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        LOGGER.info("Deleting employee by Id...");
        DeleteResponse response = null;
        try {
            response = reliaQuestClient.webClient().delete().uri("/delete/{id}", id).retrieve().bodyToMono(DeleteResponse.class).block();
        } catch (RuntimeException e) {
            LOGGER.error("Deleting employee by Id failed: {}", String.valueOf(e));
        }
        if (response != null) {
            LOGGER.info("Delete employee succeeded!");
            return ResponseEntity.ok(response.toString());
        } else {
            LOGGER.info("Delete employee failed!");
            return ResponseEntity.internalServerError().build();
        }
    }
}
