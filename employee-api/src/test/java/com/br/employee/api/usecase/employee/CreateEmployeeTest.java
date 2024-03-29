package com.br.employee.api.usecase.employee;

import com.br.employee.api.common.exception.EmployeeAlreadyExistsException;
import com.br.employee.api.common.exception.InvalidEmailException;
import com.br.employee.api.usecase.employee.entities.Departament;
import com.br.employee.api.usecase.employee.entities.Employee;
import com.br.employee.api.gateway.client.EmailValidatorGateway;
import com.br.employee.api.gateway.repository.EmployeeRepoGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateEmployeeTest {

    @Mock
    private EmailValidatorGateway emailValidatorGateway;

    @Mock
    private EmployeeRepoGateway employeeRepoGateway;

    private CreateEmployee createEmployee;

    @Before
    public void setUp() {

        createEmployee = new CreateEmployee(employeeRepoGateway, emailValidatorGateway);
    }

    @Test
    public void shouldCreateEmployee() {

        Employee employeeInput = buildEmployee();

        createEmployee.execute(employeeInput);

        verify(employeeRepoGateway, times(1)).saveNewEmployee(employeeInput);
        verify(emailValidatorGateway, times(1)).isInvalidEmail(employeeInput.getEmail());
    }

    @Test(expected = EmployeeAlreadyExistsException.class)
    public void shouldFailOnCreatingEmployeeWhenTryInsertWithExistingEmail() {

        Employee employeeInput = buildEmployee();

        when(employeeRepoGateway.findByEmail("meuemail@email.com")).thenReturn(Optional.of(buildEmployee()));

        createEmployee.execute(employeeInput);
    }

    @Test(expected = InvalidEmailException.class)
    public void shouldFailOnCreateingEmployeeWhenTryInsertWithInvalidEmail() {

        Employee employeeInput = buildEmployee();

        when(emailValidatorGateway.isInvalidEmail("meuemail@email.com")).thenReturn(Boolean.TRUE);

        createEmployee.execute(employeeInput);
    }

    private Employee buildEmployee() {

        return Employee.builder()
            .name("Meu nome")
            .email("meuemail@email.com")
            .departament(Departament.ARCHITECTURE)
            .build();
    }
}
