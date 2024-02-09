package com.wafflestudio.babble.common.utils;

@FunctionalInterface
public interface StringGenerator {

    String generateRandomAlphanumericString(int length);
}
