/*
 * Copyright (C) 2016 Dmitry Mozgin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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