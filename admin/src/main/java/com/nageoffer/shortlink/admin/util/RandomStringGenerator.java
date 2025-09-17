package com.nageoffer.shortlink.admin.util;

import java.security.SecureRandom;

/**
 * 随机字符串生成工具类
 */
public class RandomStringGenerator {

    // 定义包含的字符集：大写字母、小写字母和数字
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    // 生成的字符串长度
    private static final int LENGTH = 6;

    /**
     * 生成6位随机字母数字组合
     *
     * @return 6位长度的随机字符串，包含字母和数字
     */
    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            // 随机从字符集中选择一个字符
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
}
