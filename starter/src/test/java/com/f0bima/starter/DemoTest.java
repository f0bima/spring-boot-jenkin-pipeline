package com.f0bima.starter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DemoTest {

    @Test
    @DisplayName("Method health() harus mengembalikan string OK")
    void testHealthReturnsOk() {
        // Arrange
        Demo demo = new Demo();

        // Act
        String response = demo.health();

        // Assert
        assertNotNull(response);
        assertEquals("OK", response);
    }
}
