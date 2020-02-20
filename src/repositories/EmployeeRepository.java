package repositories;

import domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> readAll();
    Optional<Employee> readOne(String employeeNo);
    void writeAll(List<Employee> employees);
    void writeOne(Employee employee);
}
