import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {
    private static URL PATH = Main.class.getResource("");
    private static String FILE_NAME = "employee_list.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File file = new File(PATH.getPath(), FILE_NAME);
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedReader = new BufferedReader(new FileReader(file));

            int currentEmployeeCount = init(file, bufferedReader);
            System.out.println(currentEmployeeCount);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*System.out.println("0. 종료");
        System.out.println("1. 직원 정보 입력");
        System.out.println("2. 직원 리스트");
        System.out.println("3. 직원 상세 정보 출력");
        System.out.println("4. 직원 정보 수정");
        System.out.println("5. 직원 정보 삭제");

        System.out.println("        메뉴 선택 : ");
        String menuSelect = scanner.nextLine();*/
//        insertEmployee();
    }

    public static int init(File file, BufferedReader bufferedReader) {
        int employeeCount = 0;
        try {
            employeeCount = Optional.ofNullable(bufferedReader.readLine())
                    .map(s -> {
                        int result = 0;
                        try {
                            result = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            System.out.println("인원 수가 입력되어있지 않음.");
                            return null;
                        }
                        return result;
                    })
                    .orElseGet(() -> {
                        try {
                            FileWriter fileWriter = new FileWriter(file);
                            fileWriter.write("0\r\n");
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employeeCount;
    }

    public static void insertEmployee() {
        BufferedOutputStream outputStream = null;
        List<String> list = new ArrayList();
        /*try {


//
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
//            System.out.println("이름 : ");
//            String name = scanner.nextLine();
//            System.out.println("전화번호 : ");
//            String phoneNumber = scanner.nextLine();
//            System.out.println("직급 : ");
//            String ranks = scanner.nextLine();
//            System.out.println("이메일 : ");
//            String email = scanner.nextLine();
//
//
//            System.out.println("작업 완료!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }*/
    }
}
