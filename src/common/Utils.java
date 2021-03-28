package common;

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
        if(password.matches("(?=.*[\\+~!@#$%^&*()_-]).*"))
            score += 2;

        return score;

    }

}
