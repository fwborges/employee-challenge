package com.br.employee.api.usecase.employee;

import com.br.employee.api.common.config.UseCase;
import com.br.employee.api.common.exception.EmployeeNotFoundException;
import com.br.employee.api.usecase.employee.entities.Employee;
import com.br.employee.api.gateway.repository.EmployeeRepoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@UseCase
public class RemoveEmployee {

    private static Logger LOGGER = LoggerFactory.getLogger(RemoveEmployee.class);

    private EmployeeRepoGateway employeeRepoGateway;

    @Autowired
    public RemoveEmployee(EmployeeRepoGateway employeeRepoGateway) {
        this.employeeRepoGateway = employeeRepoGateway;
    }

    public void execute(String email) {

        validateBeforeRemove(email);

        employeeRepoGateway.deleteByEmail(email);
    }

    private void validateBeforeRemove(String email) {

        Optional<Employee> employee = employeeRepoGateway.findByEmail(email);

        if(!employee.isPresent()) {
            LOGGER.error("Cannot delete user with email {} because it not exists", email);
            throw new EmployeeNotFoundException("Cannot delete this employee");
        }
    }
}
