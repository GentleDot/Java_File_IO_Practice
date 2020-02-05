package common;

import static java.util.regex.Pattern.matches;

public class ParamCheckUtil {

    public static void patternCheck(String regex, String target, String exceptionMessage) {
        if (!matches(regex, target)) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public static void notNullCheck(String target, String exceptionMessage) {
        if (target.isEmpty() || target.isBlank() || target == null){
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
