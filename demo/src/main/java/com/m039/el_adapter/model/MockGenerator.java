package com.m039.el_adapter.model;

import java.util.ArrayList;
import java.util.List;

public class MockGenerator {
    public static Page getPage(int num, int offset, int count) {
        Page page = new Page();
        page.num = num;
        page.items = numbers(offset, count);
        return page;
    }

    private static List<String> numbers(int offset, int count) {
        List<String> numbers = new ArrayList<>(count);
        for (int i = offset; i < (offset + count); i++) {
            numbers.add(String.valueOf(i));
        }
        return numbers;
    }
}