package common;

import java.util.Scanner;

import static java.util.regex.Pattern.matches;

public class PromptUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static String getInputValue(String categoryName) {
        System.out.print(categoryName + " : ");
        return scanner.nextLine();
    }

    public static String getInputValueWithCheckRegex(String categoryName, String regex, String exampleValue) {
        boolean retryFlag = true;
        String inputValue = "";
        while (retryFlag) {
            System.out.print(categoryName + " : ");
            inputValue = scanner.nextLine();

            if (inputValue.isBlank()) {
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

    public static String employeeManagementPrompt() {
        System.out.println("====== 직원 정보 관리 ======");
        System.out.println("0. 종료");
        System.out.println("1. 직원 정보 입력");
        System.out.println("2. 직원 리스트");
        System.out.println("3. 직원 상세 정보 출력");
        System.out.println("4. 직원 정보 수정");
        System.out.println("5. 직원 정보 삭제");

        System.out.print("        메뉴 선택 : ");
        return scanner.nextLine();
    }

    public static void closePromptScanner() {
        scanner.close();
    }

}
