package com.simbirsoft.helpers;

import io.qameta.allure.Step;

import java.util.List;

/**
 * StringHelper.java
 * <p>
 * Вспомогательный класс для операций со строками
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.06.2026
 */
public final class StringHelper {

    /**
     * Находит самое длинное слово в списке строк
     * @param words список слов
     * @return самое длинное слово, или пустую строку если список пуст
     */
    @Step("Поиск самого длинного слова из списка: {words}")
    public static String findLongestWord(List<String> words) {
        if (words == null || words.isEmpty()) {
            return "";
        }

        String longestWord = "";
        for (String word : words) {
            String trimmed = word.trim();
            if (trimmed.length() > longestWord.length()) {
                longestWord = trimmed;
            }
        }
        return longestWord;
    }
}
