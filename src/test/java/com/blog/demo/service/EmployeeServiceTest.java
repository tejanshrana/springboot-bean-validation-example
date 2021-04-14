package com.blog.demo.service;

import com.blog.demo.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    protected static Validator validator;

    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Should pass without errors")
    void shouldAddEmployee() {

        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .department("Engineering")
                .build();

        employeeService.addEmployee(employee);
    }

    @ParameterizedTest
    @MethodSource("invalidTestData")
    @DisplayName("Add employee - Should fail validation")
    void addEmployee_shouldFailValidation(Employee employee, String invalidProperty, String message) {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        assertThat(constraintViolations).noneMatch(violation -> violation.getPropertyPath().toString().equals(invalidProperty) && violation.getMessage().equals(message));

    }


    private static Stream<Arguments> invalidTestData() {
        return Stream.of(Arguments.of(Employee.builder()
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .department("Engineering")
                .build(), "firstName", "must not be null"));
    }

}
