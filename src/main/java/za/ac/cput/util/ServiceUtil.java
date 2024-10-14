package za.ac.cput.util;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class ServiceUtil {
    public static <T> T copyProperties(Object source, T target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            throw new IllegalArgumentException("Password and hashed password cannot be null");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
