import java.io.*;
import java.net.URL;
import java.util.Scanner;

import static java.util.regex.Pattern.matches;

public class Main {
    private static URL PATH = Main.class.getResource("");
    private static String FILE_NAME = "employee_list.txt";

    public static void main(String[] args) {
        boolean bolRunProgram = true;
        Scanner scanner = new Scanner(System.in);
        File file = new File(PATH.getPath(), FILE_NAME);
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        FileReader fileReader = null;
        FileWriter fileWriter = null;

        try {
            /*
             * https://qkrrudtjr954.github.io/java/2017/11/13/file-write.html
             * BufferedWriter : 한 줄씩 처리, 8192 bytes 이하 크기의 쓰기, 여러 곳에서 쓰기가 이뤄지는 경우라면 효과적일 것임.
             * */
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            bufferedReader.mark(8192);
            while (bolRunProgram) {
                int currentEmployeeCount = (int) bufferedReader.lines().count();
                System.out.println(currentEmployeeCount);

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
                        insertEmployee(scanner, bufferedWriter, (currentEmployeeCount - 1));
                        break;
                    case "2":
                        System.out.println("직원 리스트");
                        printEmployeeList(bufferedReader);
                        break;
                    case "3":
                        System.out.println("직원 상세 정보");
                        printEmployeeDetailList(bufferedReader);
                        break;
                    case "4":
                        System.out.println("직원 정보 수정");
                        break;
                    case "5":
                        System.out.println("직원 정보 삭제");
                        break;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    fileReader.close();
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    fileWriter.close();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void insertEmployee(Scanner scanner, BufferedWriter writer, int employeeCount) {
        String name;
        String phoneNumber = "";
        String ranks;
        String email = "";
        boolean inputPhoneRetryFlag = true;
        boolean inputEmailRetryFlag = true;

        System.out.println("====== 직원 정보 입력 (exit 입력 시 종료) ======");

        System.out.print("이름 : ");
        name = scanner.nextLine();
        if (name.equals("exit")) {
            return;
        }

        while (inputPhoneRetryFlag) {
            System.out.print("전화번호 : ");
            phoneNumber = scanner.nextLine();

            if (phoneNumber.equals("exit")) {
                return;
            } else if (!matches("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", phoneNumber)) {
                System.out.println("유효하지 않은 전화번호 입력! 다시 입력해주세요. ex) 010-1234-5678");
                continue;
            }
            inputPhoneRetryFlag = false;
        }

        System.out.print("직급 : ");
        ranks = scanner.nextLine();
        if (ranks.equals("exit")) {
            return;
        }

        while (inputEmailRetryFlag) {
            System.out.print("이메일 : ");
            email = scanner.nextLine();

            if (email.equals("exit")) {
                return;
            } else if (!matches("^[\\d\\w-_.]+@[\\d\\w]+[.][\\w]{2,4}$", email)) {
                System.out.println("유효하지 않은 이메일 입력! 다시 입력해주세요. ex) email@email.com");
                continue;
            }
            inputEmailRetryFlag = false;
        }

        String newEmployee = new StringBuffer()
                .append(String.format("%03d", employeeCount + 1))
                .append(",")
                .append(name)
                .append(",")
                .append(phoneNumber)
                .append(",")
                .append(ranks)
                .append(",")
                .append(email)
                .append(",")
                .append("직원")
                .append("\r\n")
                .toString();

        try {
            writer.write(newEmployee);
            writer.flush();
            System.out.println("입력 완료.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printEmployeeList(BufferedReader reader) {
        System.out.println("직원번호        이름");
        System.out.println("====================");

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[5] == "직원") {
                    System.out.println(line[0] + "      " + line[1]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printEmployeeDetailList(BufferedReader reader) {
        System.out.println("직원번호        이름            전화번호            직급            이메일");
        System.out.println("==========================================================================");

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[5] == "직원") {
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
}
