package com.blog.demo.model;

import com.blog.demo.model.validators.Age;
import com.blog.demo.model.validators.NewEmployee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Age(min = 18L, max = 65L)
    private LocalDate dateOfBirth;

    @NotNull(groups = NewEmployee.class)
    private String department;

}
