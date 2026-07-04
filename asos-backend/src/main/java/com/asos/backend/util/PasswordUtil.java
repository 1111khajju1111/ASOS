package com.asos.backend.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * Manual password hashing using jBCrypt. Deliberately not using
 * Spring Security's PasswordEncoder.
 */
@Component
public class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
