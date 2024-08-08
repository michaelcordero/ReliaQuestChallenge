package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String employee_name;
    @JsonProperty
    private Integer employee_salary;
    @JsonProperty
    private Integer employee_age;
    @JsonProperty
    private String profile_image;

    public Integer getId() {
        return id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public Integer getEmployee_salary() {
        return employee_salary;
    }

    public Integer getEmployee_age() {
        return employee_age;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public Employee() {}

    public Employee(Integer id, String employee_name, Integer employee_salary, Integer employee_age, String profile_image) {
        this.id = id;
        this.employee_name = employee_name;
        this.employee_salary = employee_salary;
        this.employee_age = employee_age;
        this.profile_image = profile_image;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employee_name='" + employee_name + '\'' +
                ", employee_salary=" + employee_salary +
                ", employee_age=" + employee_age +
                ", profile_image='" + profile_image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(employee_name, employee.employee_name) && Objects.equals(employee_salary, employee.employee_salary) && Objects.equals(employee_age, employee.employee_age) && Objects.equals(profile_image, employee.profile_image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee_name, employee_salary, employee_age, profile_image);
    }
}
