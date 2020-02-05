import service.EmployeeService;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static URL PATH = Main.class.getResource("");
    private static String FILE_NAME = "employee_list.txt";

    public static void main(String[] args) {
        boolean bolRunProgram = true;
        final Scanner scanner = new Scanner(System.in);
        final File file = new File(PATH.getPath(), FILE_NAME);
        final BufferedReader bufferedReader;
        final BufferedWriter bufferedWriter;

        try {
            /*
             * https://qkrrudtjr954.github.io/java/2017/11/13/file-write.html
             * BufferedWriter : 한 줄씩 처리, 8192 bytes 이하 크기의 쓰기, 여러 곳에서 쓰기가 이뤄지는 경우라면 효과적일 것임.
             * */
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedReader = new BufferedReader(new FileReader(file));

            bufferedReader.mark(8192);

            EmployeeService employeeService = new EmployeeService(scanner);


            while (bolRunProgram) {
                System.out.println("0. 종료");
                System.out.println("1. 직원 정보 입력");
                System.out.println("2. 직원 리스트");
                System.out.println("3. 직원 상세 정보 출력");
                System.out.println("4. 직원 정보 수정");
                System.out.println("5. 직원 정보 삭제");

                System.out.print("        메뉴 선택 : ");
                String menuSelect = scanner.nextLine();

                switch (menuSelect) {
                    case "0":
                        bolRunProgram = false;
                        break;
                    case "1":
                        System.out.println("직원 정보 입력");
                        employeeService.insertEmployee(bufferedReader, bufferedWriter);
                        break;
                    case "2":
                        System.out.println("직원 리스트");
                        employeeService.printEmployeeList(bufferedReader);
                        break;
                    case "3":
                        System.out.println("직원 상세 정보");
                        employeeService.printEmployeeDetailList(bufferedReader);
                        break;
                    case "4":
                        System.out.println("직원 정보 수정");
                        employeeService.updateEmployee(file, bufferedReader);
                        break;
                    case "5":
                        System.out.println("직원 정보 삭제");
                        employeeService.deleteEmployee(file, bufferedReader);
                        break;
                }
            }

            bufferedReader.close();
            bufferedWriter.close();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
