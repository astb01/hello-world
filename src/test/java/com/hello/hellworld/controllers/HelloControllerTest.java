package com.hello.hellworld.controllers;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class HelloControllerTest {
    @Test
    public void saysHello() {
        HelloController controller = new HelloController();

        var result = controller.sayHello("John");
        Assertions.assertThat(result).isEqualTo("Hello John");
    }
}