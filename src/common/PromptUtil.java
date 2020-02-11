package common;

import java.util.Scanner;

import static java.util.regex.Pattern.matches;

public class PromptUtil {
    private static Scanner scanner = new Scanner(System.in);;

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
}
