package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public static String currentFormatedTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        return LocalDateTime.now().format(dtf);
    }

}
