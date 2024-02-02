package org.swiggy.model;

import org.swiggy.exception.HashAlgorithmNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Provides hashed password for security purposes.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class PasswordGenerator {

    private static PasswordGenerator passwordGenerator;

    private PasswordGenerator() {
    }

    /**
     * <p>
     * Gets the password generator class object.
     * </p>
     *
     * @return The password generator object
     */
    public static PasswordGenerator getInstance() {
        if (null == passwordGenerator) {
            passwordGenerator = new PasswordGenerator();
        }

        return passwordGenerator;
    }

    /**
     * <p>
     * Hashes the user password.
     * </p>
     *
     * @param password password of the current user
     * @return The hashed password
     */
    public String hashPassword(final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            final byte[] encodedHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            final StringBuilder hashString = new StringBuilder();
            for (final byte hashByte : encodedHash) {
                hashString.append(String.format("%02x", hashByte));
            }

            return hashString.substring(0, 25);
        } catch (NoSuchAlgorithmException message) {
            throw new HashAlgorithmNotFoundException(message.getMessage());
        }
    }
}