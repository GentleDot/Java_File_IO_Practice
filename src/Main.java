import repositories.EmployeeFileAccess;
import repositories.EmployeeRepository;
import service.EmployeeService;

import java.io.File;
import java.net.URL;

import static common.PromptUtil.*;

public class Main {

    public static void main(String[] args) {
        // 기본 설정
        boolean bolRunProgram = true;
        final URL path = Main.class.getResource("");
        final File file = new File(path.getPath(), "employee_list.txt");

        // repository 생성
        final EmployeeRepository employeeRepository = new EmployeeFileAccess(file);

        // service 생성
        final EmployeeService employeeService = new EmployeeService(employeeRepository);

        while (bolRunProgram) {
            String menuSelect = employeeManagementPrompt();
            String employeeNo;

            switch (menuSelect) {
                case "0":
                    bolRunProgram = false;
                    break;
                case "1":
                    System.out.println("직원 정보 입력");
                    employeeService.insertEmployee();
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
        closePromptScanner();
    }
}
