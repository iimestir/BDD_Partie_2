package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Used for every miscellaneous things
 */
public final class Utils {
    public static final Locale CURRENT_LOCALE = Locale.getDefault();

    /**
     * Used to get the translated string corresponding to a key from the resource bundle
     * @param key String key
     * @return the translated string
     */
    public static String getTranslatedString(String key) {
        return ResourceBundle.getBundle("translation", CURRENT_LOCALE).getString(key);
    }

    /**
     * Checks if the password has a special character
     * @param password the password
     * @return boolean
     */
    private static boolean hasSpecial(String password) {
        return password.contains("+") || password.contains("~") || password.contains("!")
                || password.contains("@") || password.contains("#") || password.contains("$")
                || password.contains("^") || password.contains("%") || password.contains("&")
                || password.contains("(") || password.contains(")") || password.contains("_")
                || password.contains("-") || password.contains("=") || password.contains("?")
                || password.contains(".") || password.contains(";") || password.contains(",")
                || password.contains("[") || password.contains("]") || password.contains("ù")
                || password.contains("µ") || password.contains("{") || password.contains("}")
                || password.contains("€") || password.contains("/") || password.contains("\\")
                || password.contains("<") || password.contains(">") || password.contains("é")
                || password.contains("ç") || password.contains("à") || password.contains("'");
    }

    /**
     * Used to score the input password
     * @param password the password
     * @return the score
     */
    public static int scorePassword(String password) {
        int score = 0;

        if(password.length() < 8)
            return 0;
        else if(password.length() >= 10)
            score += 2;
        else
            score += 1;

        if(password.matches("(?=.*[0-9]).*"))
            score += 2;
        if(password.matches("(?=.*[a-z]).*"))
            score += 2;
        if(password.matches("(?=.*[A-Z]).*"))
            score += 2;

        if(hasSpecial(password))
            score += 2;

        return score;
    }

    /**
     * Returns the current time formatted to a specified manner
     * @return the current time
     */
    public static String currentFormattedTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        return LocalDateTime.now().format(dtf);
    }

    /**
     * Fills the SQL request with the sub requests
     *
     * @param request the request string builder
     * @param subRequest the sub requests list
     */
    public static void fillSQLSelect(StringBuilder request, List<String> subRequest) {
        boolean where = false;
        boolean and = false;
        for(String sub : subRequest) {
            if(!where) {
                request.append(" WHERE");
                where = true;
            }

            if(and) {
                request.append(" AND ").append(sub);
            } else {
                request.append(" ").append(sub);
                and = true;
            }
        }
    }

    /**
     * Fills the SQL request with the sub requests
     *
     * @param request the request string builder
     * @param subRequest the sub requests list
     */
    public static void fillSQLUpdate(StringBuilder request, List<String> subRequest) {
        boolean set = false;
        boolean comma = false;
        for(String sub : subRequest) {
            if(!set) {
                request.append(" SET");
                set = true;
            }

            if(comma) {
                request.append(", ").append(sub);
            } else {
                request.append(" ").append(sub);
                comma = true;
            }
        }
    }
}
