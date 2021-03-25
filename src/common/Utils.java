package common;

import at.favre.lib.crypto.bcrypt.BCrypt;

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
     * Used to cipher a string
     * @param message the string
     * @return the ciphered string
     */
    public static String getHashedString(String message) {
        return BCrypt.withDefaults().hashToString(6, message.toCharArray());
    }
}
