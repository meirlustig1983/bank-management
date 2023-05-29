package com.ml.bank_management.utils;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomDisplayNameGenerator implements DisplayNameGenerator {
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return String.format("Run a tests for class: %s", testClass.getSimpleName());
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return "Run standard test2: " + nestedClass.getName();
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        String[] args = testMethod.getName().split("_");
        if (args.length == 1) {
            return String.format("Test method '%s' ", args[0]);
        } else if (args.length == 2) {
            return String.format("Test method '%s', action: %s", args[0], generateString(args[1]));
        } else {
            return String.format("Test method '%s', action: %s, result: %s", args[0],
                    generateString(args[1]), generateString(args[2]));
        }
    }

    private static String generateString(String str) {
        return Pattern.compile("(?=\\p{Upper})")
                .splitAsStream(str)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}