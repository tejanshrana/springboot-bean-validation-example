package com.blog.demo.service;

import com.blog.demo.model.Employee;
import com.blog.demo.model.validators.NewEmployee;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class EmployeeService {

    @Validated(NewEmployee.class)
    public void addEmployee(@Valid Employee employee) {
        //do something

    }

    public void amendEmployee(@Valid Employee employee) {
        //do something
    }

}
