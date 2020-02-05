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

        try {
            /*
             * https://qkrrudtjr954.github.io/java/2017/11/13/file-write.html
             * BufferedWriter : 한 줄씩 처리, 8192 bytes 이하 크기의 쓰기, 여러 곳에서 쓰기가 이뤄지는 경우라면 효과적일 것임.
             * */
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedReader = new BufferedReader(new FileReader(file));

            bufferedReader.mark(8192);
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
                        insertEmployee(scanner, bufferedReader, bufferedWriter);
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
                        updateEmployee(file, bufferedReader, scanner);
                        break;
                    case "5":
                        System.out.println("직원 정보 삭제");
                        deleteEmployee(file, bufferedReader, scanner);
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
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void insertEmployee(Scanner scanner, BufferedReader reader, BufferedWriter writer) {
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

        try {
            reader.reset();
            int employeeCount = (int) reader.lines().count() - 1;
            String newEmployee = new StringBuffer()
                    .append(String.format("%03d", (employeeCount + 1)))
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
                if (line[5].equals("직원")) {
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

    public static void updateEmployee(File file, BufferedReader reader, Scanner scanner) {
        boolean inputEmployeeNoRetryFlag = true;
        String employeeNo = "";
        StringBuffer stringBuffer = new StringBuffer();

        System.out.print("직원번호 ? : ");
        while (inputEmployeeNoRetryFlag) {
            employeeNo = scanner.nextLine();
            if (employeeNo.equals("exit")) {
                return;
            } else if (matches("[\\d]{3}", employeeNo)) {
                inputEmployeeNoRetryFlag = false;
            }
        }

        final String targetEmployeeNo = employeeNo;

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String name;
                String phoneNumber;
                String ranks;
                String email;
                boolean inputPhoneRetryFlag = true;
                boolean inputEmailRetryFlag = true;

                String[] line = s.split(",");
                if (line[0].equals(targetEmployeeNo) && line[5].equals("직원")) {
                    System.out.println("====== 직원 정보 수정 ====== (입력 없이 enter를 누르면 현재 값을 수정하지 않습니다.)");
                    System.out.println("현재 데이터 : " + s);
                    stringBuffer.append(line[0]).append(",");

                    System.out.print("이름 : ");
                    name = scanner.nextLine();
                    if (name.isBlank()) {
                        stringBuffer.append(line[1]).append(",");
                    } else {
                        stringBuffer.append(name).append(",");
                    }

                    while (inputPhoneRetryFlag) {
                        System.out.print("전화번호 : ");
                        phoneNumber = scanner.nextLine();

                        if (phoneNumber.isBlank()) {
                            stringBuffer.append(line[2]).append(",");
                        } else if (!matches("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$", phoneNumber)) {
                            System.out.println("유효하지 않은 전화번호 입력! 다시 입력해주세요. ex) 010-1234-5678");
                            continue;
                        } else {
                            stringBuffer.append(phoneNumber).append(",");
                            inputPhoneRetryFlag = false;
                        }
                    }

                    System.out.print("직급 : ");
                    ranks = scanner.nextLine();
                    if (ranks.isBlank()) {
                        stringBuffer.append(line[3]).append(",");
                    } else {
                        stringBuffer.append(ranks).append(",");
                    }

                    while (inputEmailRetryFlag) {
                        System.out.print("이메일 : ");
                        email = scanner.nextLine();

                        if (email.isBlank()) {
                            stringBuffer.append(line[4]).append(",");
                        } else if (!matches("^[\\d\\w-_.]+@[\\d\\w]+[.][\\w]{2,4}$", email)) {
                            System.out.println("유효하지 않은 이메일 입력! 다시 입력해주세요. ex) email@email.com");
                            continue;
                        } else {
                            stringBuffer.append(email).append(",");
                            inputEmailRetryFlag = false;
                        }
                    }

                    stringBuffer.append("직원\r\n");
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

    public static void deleteEmployee(File file, BufferedReader reader, Scanner scanner) {
        boolean inputEmployeeNoRetryFlag = true;
        String employeeNo = "";
        StringBuffer stringBuffer = new StringBuffer();

        System.out.print("직원번호 ? : ");
        while (inputEmployeeNoRetryFlag) {
            employeeNo = scanner.nextLine();
            if (employeeNo.equals("exit")) {
                return;
            } else if (matches("[\\d]{3}", employeeNo)) {
                inputEmployeeNoRetryFlag = false;
            }
        }

        final String targetEmployeeNo = employeeNo;

        try {
            reader.reset();
            reader.lines().forEach(s -> {
                String[] line = s.split(",");
                if (line[0].equals(targetEmployeeNo) && line[5].equals("직원")) {
                    stringBuffer
                            .append(line[0]).append(",")
                            .append(line[1]).append(",")
                            .append(line[2]).append(",")
                            .append(line[3]).append(",")
                            .append(line[4]).append(",")
                            .append("퇴사\r\n");
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
}
