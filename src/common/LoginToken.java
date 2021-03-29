package common;

import database.transfer.UserDTO;

/**
 * Used to store the current logged in user
 */
public final class LoginToken {
    public static UserDTO CURRENT_USER;

    public static boolean isLoggedIn() {
        return CURRENT_USER != null;
    }
}
