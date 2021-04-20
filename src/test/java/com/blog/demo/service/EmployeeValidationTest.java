package com.blog.demo.service;

import com.blog.demo.model.Employee;
import com.blog.demo.model.validators.AllEmployees;
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
public class EmployeeValidationTest {


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


    @ParameterizedTest
    @MethodSource("invalidTestData_addEmployee")
    @DisplayName("Add employee - Should fail validation")
    void addEmployee_shouldFailValidation(Employee employee, String invalidProperty, String message) {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee, AllEmployees.class);
        assertThat(constraintViolations).anyMatch(violation -> violation.getPropertyPath().toString().equals(invalidProperty) && violation.getMessage().equals(message));
    }

    @Test
    @DisplayName("Add employee - Should pass validation")
    void addEmployee_shouldPassValidation() {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .department("Engineering")
                .build();
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee, AllEmployees.class);
        assertThat(constraintViolations).isEmpty();

    }

    @ParameterizedTest
    @MethodSource("validTestData_amendEmployee")
    @DisplayName("Amend employee - should pass validation")
    void amendEmployee_shouldPassValidation(Employee employee) {

        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        assertThat(constraintViolations).isEmpty();

    }


    @ParameterizedTest
    @MethodSource("invalidTestData_amendEmployee")
    @DisplayName("Amend employee - should fail validation")
    void amendEmployee_shouldFailValidation(Employee employee, String invalidProperty, String message) {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        assertThat(constraintViolations).anyMatch(violation -> violation.getPropertyPath().toString().equals(invalidProperty) && violation.getMessage().equals(message));
    }

    private static Stream<Arguments> invalidTestData_addEmployee() {
        return Stream.of(
                //Pass first name, last name, and department as blank strings:
                Arguments.of(Employee.builder()
                        .firstName(" ")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department("Engineering")
                        .build(), "firstName", "must not be blank"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName(" ")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department("Engineering")
                        .build(), "lastName", "must not be blank"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department(" ")
                        .build(), "department", "must not be blank"),

                //Pass first name, last name, and department as null:
                Arguments.of(Employee.builder()
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department("Engineering")
                        .build(), "firstName", "must not be blank"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department("Engineering")
                        .build(), "lastName", "must not be blank"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .build(), "department", "must not be blank"),

                //Pass invalid date of birth
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(17))
                        .department("Engineering")
                        .build(), "dateOfBirth", "Age must be between 18 and 65"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(66))
                        .department("Engineering")
                        .build(), "dateOfBirth", "Age must be between 18 and 65"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .department("Engineering")
                        .build(), "dateOfBirth", "Age must be between 18 and 65")

        );
    }

    private static Stream<Arguments> invalidTestData_amendEmployee() {
        return Stream.of(Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .build(), "department", "must not be blank"),
                Arguments.of(Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.now().minusYears(20))
                        .department("               ")
                        .build(), "department", "must not be blank"));
    }

    private static Stream<Arguments> validTestData_amendEmployee() {
        return Stream.of(
                Arguments.of(Employee.builder()
                        .department("Engineering")
                        .build()),
                Arguments.of(Employee.builder()
                        .department("Engineering")
                        .dateOfBirth(LocalDate.now().minusYears(15))
                        .build()),
                Arguments.of(Employee.builder()
                        .firstName("       ")
                        .department("Engineering")
                        .build()),
                Arguments.of(Employee.builder()
                        .lastName("       ")
                        .department("Engineering")
                        .dateOfBirth(LocalDate.now().minusYears(15))
                        .build()),
                Arguments.of(Employee.builder()
                        .firstName("       ")
                        .lastName("        ")
                        .department("Engineering")
                        .build())
        );
    }

}
