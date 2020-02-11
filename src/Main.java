import domain.Employee;
import repositories.EmployeeFileAccess;
import repositories.EmployeeRepository;
import service.EmployeeService;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static common.PromptUtil.getInputValueWithCheckRegex;

public class Main {

    public static void main(String[] args) {
        // 기본 설정
        boolean bolRunProgram = true;
        final Scanner scanner = new Scanner(System.in);
        final URL path = Main.class.getResource("");
        final File file = new File(path.getPath(), "employee_list.txt");

        // repository 생성
        final EmployeeRepository employeeRepository = new EmployeeFileAccess(file);
        Comparator<Employee> compare = Comparator.comparing(Employee::getSeq);

        List<Employee> employeeList = employeeRepository.readAll();
        employeeList.sort(compare);

        // service 생성
        final EmployeeService employeeService = new EmployeeService(scanner, employeeList);

        while (bolRunProgram) {
            System.out.println("====== 직원 정보 관리 (종료 시 파일에 저장됩니다.) ======");
            System.out.println("0. 종료");
            System.out.println("1. 직원 정보 입력");
            System.out.println("2. 직원 리스트");
            System.out.println("3. 직원 상세 정보 출력");
            System.out.println("4. 직원 정보 수정");
            System.out.println("5. 직원 정보 삭제");

            System.out.print("        메뉴 선택 : ");
            String menuSelect = scanner.nextLine();

            String employeeNo;

            switch (menuSelect) {
                case "0":
                    employeeRepository.writeAll(employeeList);
                    bolRunProgram = false;
                    break;
                case "1":
                    System.out.println("직원 정보 입력");
                    employeeService.insertEmployee(String.format("%03d", (employeeList.size())));
                    break;
                case "2":
                    System.out.println("직원 리스트");
                    employeeService.printEmployeeList();
                    break;
                case "3":
                    System.out.println("직원 상세 정보");
                    employeeService.printEmployeeDetailList();
                    break;
                case "4":
                    System.out.println("직원 정보 수정");
                    employeeNo = getInputValueWithCheckRegex("직원 번호", "[\\d]{3}", "001");
                    if (employeeNo.equals("exit")) {
                        System.out.println("수정 작업이 취소되었습니다.");
                    } else {
                        employeeService.updateEmployee(employeeNo);
                    }
                    break;
                case "5":
                    System.out.println("직원 정보 삭제");
                    employeeNo = getInputValueWithCheckRegex("직원 번호", "[\\d]{3}", "001");
                    if (employeeNo.equals("exit")) {
                        System.out.println("삭제 작업이 취소되었습니다.");
                    } else {
                        employeeService.deleteEmployee(employeeNo);
                    }
                    break;
            }
        }

        scanner.close();
    }
}
