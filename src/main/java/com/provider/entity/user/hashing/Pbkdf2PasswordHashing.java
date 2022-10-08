package com.provider.entity.user.hashing;

import com.provider.entity.user.UserPassword;
import com.provider.entity.user.impl.UserPasswordImpl;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class Pbkdf2PasswordHashing implements PasswordHashing {
    private static Pbkdf2PasswordHashing instance;

    public static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private static final int SALT_BYTES_COUNT = 16;

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    private final Random random = new SecureRandom();

    private Pbkdf2PasswordHashing() {}

    public static Pbkdf2PasswordHashing getInstance() {
        if (instance == null) {
            instance = new Pbkdf2PasswordHashing();
        }
        return instance;
    }

    @Override
    public @NotNull UserPassword hash(@NotNull String password) {
        try {
            final byte[] saltBytes = new byte[SALT_BYTES_COUNT];
            random.nextBytes(saltBytes);
            return hash(password, saltBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public @NotNull UserPassword hash(@NotNull String password, @NotNull String salt) {
        try {
            final byte[] saltBytes = decoder.decode(salt);
            return hash(password, saltBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static @NotNull UserPassword hash(@NotNull String password, byte[] saltBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Blank password");
        }
        final KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        final byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();

        return UserPasswordImpl.of(encoder.encodeToString(hash), encoder.encodeToString(saltBytes),
                HashMethod.PBKDF2_1);
    }
}
