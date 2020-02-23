package com.iths.mock.untils;

import java.util.Random;

/**
 * @author sen.huang
 * @description
 * @date 2020/2/23.
 */
public class MockUtils {
    private static final Random random = new Random();

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
