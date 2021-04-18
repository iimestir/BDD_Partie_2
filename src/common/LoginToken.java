package common;

import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;

/**
 * Used to store the current logged in user
 */
public final class LoginToken {
    public static UserDTO CURRENT_USER;

    /**
     * Checks if the user is logged in
     * @return boolean
     */
    public static boolean isLoggedIn() {
        return CURRENT_USER != null;
    }

    /**
     * Checks if the current user is an epidemiologist
     *
     * @return boolean
     */
    public static boolean isEpidemiologist() {
        return CURRENT_USER instanceof EpidemiologistDTO;
    }

    /**
     * Used to disconnect the current user
     */
    public static void logOff() {
        if(!isLoggedIn())
            return;

        CURRENT_USER = null;
    }
}
