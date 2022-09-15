package com.provider.entity.user.hashing;

import com.provider.entity.user.UserPassword;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingTest {
    @ParameterizedTest
    @MethodSource("getAllHashMethods")
    public void testGetInstanceForEachHashMethod(PasswordHashing.HashMethod hashMethod) {
        final PasswordHashing hashing = PasswordHashing.getInstance(hashMethod);
        assertInstanceOf(PasswordHashing.class, hashing);
    }

    private static Stream<PasswordHashing.HashMethod> getAllHashMethods() {
        return Arrays.stream(PasswordHashing.HashMethod.values());
    }

    @ParameterizedTest
    @MethodSource({"getRegularPasswords", "getRandomNotBlankPasswords"})
    public void testHash(String password) {
        getAllPasswordHashingImplementations().forEach(hashing -> {
            final UserPassword first = hashing.hash(password);
            final UserPassword second = hashing.hash(password, first.getSalt());
            assertEquals(first, second);
        });
    }

    private static Stream<String> getRegularPasswords() {
        return Stream.of("12345678", "qwerty", "password", "pass1234", "123pas4s", "pass_-WORD.11");
    }

    private static Stream<String> getRandomNotBlankPasswords() {
        final var random = new Random();
        final int MAX_LENGTH = 32;
        final int MAX_COUNT = 32;
        return Stream.generate(() -> random.nextInt(MAX_LENGTH))
                .map(byte[]::new)
                .peek(random::nextBytes)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .filter(s -> !s.isBlank())
                .limit(MAX_COUNT);
    }

    private static Stream<PasswordHashing> getAllPasswordHashingImplementations() {
        return Arrays.stream(PasswordHashing.HashMethod.values())
                .map(PasswordHashing::getInstance);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\n", "\r", " \n\r",})
    public void testThrowsWhenPasswordIsBlank(String password) {
        getAllPasswordHashingImplementations()
                .forEach(hashing -> assertThrows(IllegalArgumentException.class, () -> hashing.hash(password)));
    }
}
