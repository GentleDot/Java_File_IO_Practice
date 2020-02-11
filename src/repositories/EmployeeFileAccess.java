package repositories;

import domain.Employee;
import domain.EmployeeStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmployeeFileAccess implements EmployeeRepository {
    private static final int WRITE_FAILED = 0;
    private static final int WRITE_SUCCESS = 1;

    private final File file;

    public EmployeeFileAccess(File file) {
        this.file = file;
    }

    @Override
    public List<Employee> readAll() {
        BufferedReader bufferedReader = null;
        List<Employee> employeeList = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.mark(8192);

            if (bufferedReader.lines().count() == 0) {
                return employeeList;
            }

            bufferedReader.reset();
            bufferedReader.lines().forEach(s -> {
                String[] employeeInfo = s.split(",");
                Employee employee = new Employee.Builder(employeeInfo[0])
                        .name(employeeInfo[1])
                        .phoneNumber(employeeInfo[2])
                        .ranks(employeeInfo[3])
                        .email(employeeInfo[4])
                        .status(employeeInfo[5].equals("직원") ? EmployeeStatus.MEMBER : EmployeeStatus.TERMINATED)
                        .build();
                employeeList.add(employee);
            });

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        return employeeList;
    }

    @Override
    public Optional<Employee> readOne(String employeeNo) {
        Optional<String> getEmployee = Optional.empty();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.mark(8192);

            getEmployee = bufferedReader.lines()
                    .filter(s -> s.split(",")[0].equals(employeeNo))
                    .findFirst();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        return getEmployee.map(s -> {
            String[] employeeInfo = s.split(",");
            return new Employee.Builder(employeeInfo[0])
                    .name(employeeInfo[1])
                    .phoneNumber(employeeInfo[2])
                    .ranks(employeeInfo[3])
                    .email(employeeInfo[4])
                    .status(employeeInfo[5].equals("직원") ? EmployeeStatus.MEMBER : EmployeeStatus.TERMINATED)
                    .build();
        });
    }

    @Override
    public int writeAll(List<Employee> employees) {
        int result = WRITE_FAILED;
        BufferedWriter bufferedWriter = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, false));

            employees.forEach(employee -> stringBuffer.append(employee.toString()));

            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
            result = WRITE_SUCCESS;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        return result;
    }

    @Override
    public int writeOne(Employee employee) {
        int result = WRITE_FAILED;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));

            bufferedWriter.write(employee.toString());
            bufferedWriter.flush();

            result = WRITE_SUCCESS;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        return result;
    }
}
