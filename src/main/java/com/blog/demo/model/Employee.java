package com.blog.demo.model;

import com.blog.demo.model.validators.Age;
import com.blog.demo.model.validators.AllEmployees;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @NotBlank(groups = AllEmployees.class)
    private String firstName;

    @NotBlank(groups = AllEmployees.class)
    private String lastName;

    @Age(min = 18L, max = 65L, groups = AllEmployees.class)
    private LocalDate dateOfBirth;

    @NotBlank
    private String department;

}
