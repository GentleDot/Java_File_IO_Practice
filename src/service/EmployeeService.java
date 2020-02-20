package service;

import domain.Employee;
import domain.EmployeeStatus;
import repositories.EmployeeCache;
import repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static common.PromptUtil.getInputValue;
import static common.PromptUtil.getInputValueWithCheckRegex;

/*
 * 직원 관리 프로그램
 * employeeList 에 정보를 관리하는 클래스
 * */

public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeCache employeeCache;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        employeeCache = new EmployeeCache(employeeRepository);
    }

    public void insertEmployee() {
        int employeesCount = employeeCache.getManagedEmployees().size();
        String employeeNo = String.format("%03d", employeesCount);
        Optional<Employee> newEmployee = getEmployeeFromInput(employeeNo);

        if (newEmployee.isEmpty()) {
            System.out.println("입력 작업이 취소되었습니다.");
            return;
        }

        employeeRepository.writeOne(newEmployee.get());
        employeeCache.clearCache();
        System.out.println("직원 정보 입력 완료!");
    }

    public void printEmployeeList() {
        System.out.println("직원번호        이름");
        System.out.println("====================");

        employeeCache.getManagedEmployees().values().forEach(employee -> {
            if (employee.getStatus().equals(EmployeeStatus.MEMBER)) {
                System.out.println(new StringBuffer()
                        .append(employee.getSeq())
                        .append("        ")
                        .append(employee.getName())
                        .toString());
            }
        });
    }

    public void printEmployeeDetailList() {
        System.out.println("직원번호        이름            전화번호            직급            이메일");
        System.out.println("==========================================================================");

        employeeCache.getManagedEmployees().values().forEach(employee -> {
            if (employee.getStatus().equals(EmployeeStatus.MEMBER)) {
                System.out.println(new StringBuffer()
                        .append(employee.getSeq())
                        .append("        ")
                        .append(employee.getName())
                        .append("        ")
                        .append(employee.getPhoneNumber())
                        .append("        ")
                        .append(employee.getRanks())
                        .append("        ")
                        .append(employee.getEmail())
                        .toString());
            }
        });
    }

    public void updateEmployee(String employeeNo) {
        Map<String, Employee> managedEmployees = employeeCache.getManagedEmployees();
        if (managedEmployees.containsKey(employeeNo)) {
            Employee getEmployee = managedEmployees.get(employeeNo);
            if (getEmployee.getStatus() != EmployeeStatus.MEMBER) {
                return;
            }

            Optional<Employee> employeeFromInput = getEmployeeFromInput(employeeNo);
            if (employeeFromInput.isEmpty()) {
                System.out.println("직원 정보 수정 작업을 취소하였습니다.");
                return;
            }

            Employee updatedEmployee = new Employee.Builder(getEmployee)
                    .name(employeeFromInput.get().getName().isBlank() ?
                            getEmployee.getName() : employeeFromInput.get().getName())
                    .phoneNumber(employeeFromInput.get().getPhoneNumber().isBlank() ?
                            getEmployee.getPhoneNumber() : employeeFromInput.get().getPhoneNumber())
                    .ranks(employeeFromInput.get().getRanks().isBlank() ?
                            getEmployee.getRanks() : employeeFromInput.get().getRanks())
                    .email(employeeFromInput.get().getEmail().isBlank() ?
                            getEmployee.getEmail() : employeeFromInput.get().getEmail())
                    .build();

            managedEmployees.put(employeeNo, updatedEmployee);

            employeeRepository.writeAll(new ArrayList<>(managedEmployees.values()));
            employeeCache.clearCache();
            System.out.println("직원 정보 수정 완료!");
        }
    }

    public void deleteEmployee(String employeeNo) {
        Map<String, Employee> managedEmployees = employeeCache.getManagedEmployees();
        if (managedEmployees.containsKey(employeeNo)) {
            Employee getEmployee = managedEmployees.get(employeeNo);
            if (getEmployee.getStatus() != EmployeeStatus.MEMBER) {
                return;
            }

            getEmployee.setStatus(EmployeeStatus.TERMINATED);
            managedEmployees.put(employeeNo, getEmployee);

            employeeRepository.writeAll(new ArrayList<>(managedEmployees.values()));
            employeeCache.clearCache();
            System.out.println("직원 정보 삭제 완료!");
        }
    }

    private Optional<Employee> getEmployeeFromInput(String seq) {
        String name;
        String phoneNumber;
        String ranks;
        String email;

        name = getInputValue("이름");
        if (name.equals("exit")) {
            return Optional.empty();
        }

        phoneNumber = getInputValueWithCheckRegex("전화번호",
                "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", "010-1234-5678");
        if (phoneNumber.equals("exit")) {
            return Optional.empty();
        }

        ranks = getInputValue("직급");
        if (ranks.equals("exit")) {
            return Optional.empty();
        }

        email = getInputValueWithCheckRegex("이메일",
                "^[\\d\\w-_.]+@[\\d\\w]+[.][\\w]{2,4}$", "email@email.com");
        if (email.equals("exit")) {
            return Optional.empty();
        }

        return Optional.of(
                new Employee.Builder(seq)
                        .name(name)
                        .phoneNumber(phoneNumber)
                        .ranks(ranks)
                        .email(email)
                        .build());
    }
}
