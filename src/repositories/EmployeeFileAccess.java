package repositories;

import domain.Employee;
import domain.EmployeeStatus;

import java.io.*;
import java.util.*;

public class EmployeeFileAccess implements EmployeeRepository {
    private final File file;

    public EmployeeFileAccess(File file) {
        this.file = file;
    }

    @Override
    public List<Employee> readAll() {
        BufferedReader bufferedReader = null;
        ArrayList<Employee> employeeList = new ArrayList<>();
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

            Comparator<Employee> compare = Comparator.comparing(Employee::getSeq);
            employeeList.sort(compare);

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
    public void writeAll(List<Employee> employees) {
        BufferedWriter bufferedWriter = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, false));

            employees.forEach(employee -> stringBuffer.append(employee.toString()));

            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
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
    }

    @Override
    public void writeOne(Employee employee) {
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));

            bufferedWriter.write(employee.toString());
            bufferedWriter.flush();

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
    }
}
