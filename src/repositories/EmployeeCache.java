package repositories;

import domain.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeCache {
    private static final long CACHE_DURATION = 600 * 1000L;

    private final EmployeeRepository employeeRepository;
    private final Map<String, Employee> managedEmployees;
    private long loadTime;

    public EmployeeCache(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        managedEmployees = new HashMap<>();
    }

    public Map<String, Employee> getManagedEmployees() {
        long now = System.currentTimeMillis();
        initCache(now);
        return managedEmployees;
    }

    public void clearCache() {
        managedEmployees.clear();
    }

    private void initCache(long now) {
        if (managedEmployees.isEmpty() || isOldCache(now)) {
            managedEmployees.clear();
            List<Employee> employees = employeeRepository.readAll();
            employees.forEach(employee -> managedEmployees.put(employee.getSeq(), employee));
            loadTime = now;
        }
    }

    private boolean isOldCache(long now) {
        return (now - loadTime) > CACHE_DURATION;
    }
}
