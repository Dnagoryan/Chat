package com.loggin.and.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;


class ArrayClassTest {
    private static ArrayClass arrayClass;

    @BeforeAll
    static void init() {
        arrayClass = new ArrayClass();
    }


    @Test
    void checkArrayTest() {
        Assertions.assertTrue(arrayClass.checkArray(new int[]{1, 1, 1, 4, 4, 1, 4, 4}));
        Assertions.assertThrows(RuntimeException.class, () -> arrayClass.checkArray(new int[]{}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{1, 1, 1, 1, 1, 1}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{4, 4, 4, 4}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{1, 4, 1, 1, 4, 3}));
    }


    @ParameterizedTest
    @MethodSource("dataForWriteElements")
    public void takeLastTest(int[] input, int[] output) {
        Assertions.assertArrayEquals(output, arrayClass.takeLasts(input));
    }

    private static Stream<Arguments> dataForWriteElements() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[]{1, 2, 3, 4, 5, 6, 7}, new int[]{5, 6, 7}));
        out.add(Arguments.arguments(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, new int[]{9, 10}));
        return out.stream();
    }

    @Test
    public void takeLastTestException() {
        Assertions.assertThrows(RuntimeException.class, () -> arrayClass.takeLasts(new int[]{}));
        Assertions.assertThrows(RuntimeException.class, () -> arrayClass.takeLasts(new int[]{1, 2, 3}));
        Assertions.assertThrows(RuntimeException.class, () -> arrayClass.takeLasts(new int[]{1, 2, 3, 4}));
    }
}