package com.example.springreact.ems.utils;

import com.example.springreact.ems.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {

    Employees findByEmail(String email);
}