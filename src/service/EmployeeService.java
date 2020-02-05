package service;

import domain.Employee;
import domain.EmployeeStatus;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;

import static java.util.regex.Pattern.matches;

/*
 * 직원 관리 프로그램
 * 지정한 파일(employee_list.txt)로 생성된 BufferedReader reader, BufferedWriter writer를 통해
 * file 읽기, 쓰기 기능 구현
 * Scanner scanner 는 Main에서 생성된 instance를 그대로 사용.
 * 수정 기능은 FILE을 통해 새로운 Writer를 작성하여 덮어쓰기로 구현.
 * */

public class EmployeeService {
    private final Scanner scanner;

    public EmployeeService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void insertEmployee(BufferedReader reader, BufferedWriter writer) {
        try {
            reader.reset();
            int employeeCount = (int) reader.lines().count() - 1;
            System.out.println("====== 직원 정보 입력 (exit 입력 시 종료) ======");
            Optional<Employee> newEmployee = getEmployeeFromInput(String.format("%03d", (employeeCount + 1)));
            if (newEmployee.isEmpty()) {
                return;
            }

            writer.write(newEmployee.toString());
            writer.flush();
            System.out.println("입력 완료.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printEmployeeList(BufferedReader reader) {
        System.out.println("직원번호        이름");
        System.out.println("====================");

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[5].equals("직원")) {
                    System.out.println(new StringBuffer()
                            .append(line[0])
                            .append("            ")
                            .append(line[1])
                            .toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printEmployeeDetailList(BufferedReader reader) {
        System.out.println("직원번호        이름            전화번호            직급            이메일");
        System.out.println("==========================================================================");

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[5].equals("직원")) {
                    System.out.println(new StringBuffer()
                            .append(line[0])
                            .append("            ")
                            .append(line[1])
                            .append("        ")
                            .append(line[2])
                            .append("        ")
                            .append(line[3])
                            .append("        ")
                            .append(line[4])
                            .toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(File file, BufferedReader reader) {
        final String targetEmployeeNo = getInputValueWithCheckRegex("직원번호", "[\\d]{3}", "001");
        if (targetEmployeeNo.equals("exit")) {
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String name;
                String phoneNumber;
                String ranks;
                String email;

                String[] line = s.split(",");
                if (line[0].equals(targetEmployeeNo) && line[5].equals("직원")) {

                    System.out.println("====== 직원 정보 수정 ======");
                    System.out.println("현재 데이터 : " + s);

                    name = getInputValue("이름");
                    if (name.equals("exit")){
                        return;
                    }

                    phoneNumber = getInputValueWithCheckRegex("전화번호",
                            "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", "010-1234-5678");
                    if (phoneNumber.equals("exit")){
                        return;
                    }

                    ranks = getInputValue("직급");
                    if (ranks.equals("exit")){
                        return;
                    }

                    email = getInputValueWithCheckRegex("이메일",
                            "^[\\d\\w-_.]+@[\\d\\w]+[.][\\w]{2,4}$", "email@email.com");
                    if (email.equals("exit")){
                        return;
                    }

                    Employee targetEmployee = new Employee.Builder(targetEmployeeNo)
                            .name(line[1])
                            .phoneNumber(line[2])
                            .ranks(line[3])
                            .email(line[4])
                            .build();

                    stringBuffer.append(new Employee.Builder(targetEmployee)
                            .name(name.isEmpty() ? targetEmployee.getName() : name)
                            .phoneNumber(phoneNumber.isEmpty() ? targetEmployee.getPhoneNumber() : phoneNumber)
                            .ranks(ranks.isEmpty() ? targetEmployee.getRanks() : ranks)
                            .email(email.isEmpty() ? targetEmployee.getEmail() : email)
                            .build().toString());

                } else {
                    stringBuffer.append(s).append("\r\n");
                }
            });

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(stringBuffer.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(File file, BufferedReader reader) {
        final String targetEmployeeNo = getInputValueWithCheckRegex("직원번호", "[\\d]{3}", "001");

        StringBuffer stringBuffer = new StringBuffer();
        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[0].equals(targetEmployeeNo) && line[5].equals("직원")) {
                    Employee targetEmployee = new Employee.Builder(targetEmployeeNo)
                            .name(line[1])
                            .phoneNumber(line[2])
                            .ranks(line[3])
                            .email(line[4])
                            .status(EmployeeStatus.TERMINATED)
                            .build();

                    stringBuffer.append(targetEmployee.toString());
                } else {
                    stringBuffer.append(s).append("\r\n");
                }
            });

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(stringBuffer.toString());
            writer.flush();
            writer.close();

            System.out.println("====== 입력한 직원 (직원번호_" + targetEmployeeNo + ")이 삭제되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getInputValue(String categoryName) {
        System.out.print(categoryName + " : ");
        return scanner.nextLine();
    }

    private String getInputValueWithCheckRegex(String categoryName, String regex, String exampleValue) {
        boolean retryFlag = true;
        String inputValue = "";
        while (retryFlag) {
            System.out.print(categoryName + " : ");
            inputValue = scanner.nextLine();

            if (inputValue.isEmpty()) {
                return "";
            } else if (inputValue.equals("exit")) {
                inputValue = "exit";
            } else if (!matches(regex, inputValue)) {
                System.out.println("유효하지 않은 " + categoryName + "입력! 다시 입력해주세요. ex) " + exampleValue);
                continue;
            }
            retryFlag = false;
        }

        return inputValue;
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
