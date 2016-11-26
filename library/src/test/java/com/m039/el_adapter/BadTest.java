package com.m039.el_adapter;

import org.assertj.core.api.Java6Assertions;
import org.junit.Test;

/**
 * Created by m039 on 6/9/16.
 */
public class BadTest {

    @Test
    public void bad() {
        Java6Assertions.assertThat(true).isTrue();
    }

}
