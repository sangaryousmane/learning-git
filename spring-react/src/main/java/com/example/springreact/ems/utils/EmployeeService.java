package com.example.springreact.ems.utils;

import com.example.springreact.ems.entities.Employees;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeesRepository repository;


    public List<Employees> listAllEmployees(){
        return repository.findAll();
    }

    public void saveEmployee(Employees employees){
        repository.save(employees);
    }

    public Employees getById(Long id){
        return repository.findAll()
                .stream()
                .filter(employees -> id.equals(employees.getId()))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Id "+ id+ " not found."));
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
