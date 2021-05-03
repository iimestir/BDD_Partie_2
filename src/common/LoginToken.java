package common;

import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Used to store the current logged in user
 */
public final class LoginToken {
    public static SimpleObjectProperty<UserDTO> CURRENT_LOGIN = new SimpleObjectProperty<>();

    /**
     * Checks if the user is logged in
     * @return boolean
     */
    public static boolean isLoggedIn() {
        return CURRENT_LOGIN.get() != null;
    }

    /**
     * Checks if the current user is an epidemiologist
     *
     * @return boolean
     */
    public static boolean isEpidemiologist() {
        return CURRENT_LOGIN.get() instanceof EpidemiologistDTO;
    }

    /**
     * Used to disconnect the current user
     */
    public static void logOff() {
        if(!isLoggedIn())
            return;

        CURRENT_LOGIN.set(null);
    }
}
