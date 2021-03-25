package common;

import java.util.Locale;
import java.util.ResourceBundle;

public final class Tools {
    public static final Locale currentLocale = Locale.getDefault();

    public static String getLocalizedString(String key) {
        return ResourceBundle.getBundle("translation", currentLocale).getString(key);
    }
}
