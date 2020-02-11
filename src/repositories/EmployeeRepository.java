package repositories;

import domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> readAll();
    Optional<Employee> readOne(String employeeNo);
    int writeAll(List<Employee> employees);
    int writeOne(Employee employee);
}
