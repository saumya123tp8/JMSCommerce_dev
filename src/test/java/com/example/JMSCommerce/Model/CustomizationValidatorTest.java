package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationGroupRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomizationValidatorTest {

    private CustomizationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CustomizationValidator();
    }

    @Test
    void shouldAcceptValidRequest() {

        CreateCustomizationGroupRequestDTO group =
                CreateCustomizationGroupRequestDTO.builder()
                        .build();

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder()
                        .groups(List.of(group))
                        .build();

        assertDoesNotThrow(() -> validator.validateDefinition(request));
    }

    @Test
    void shouldThrowWhenRequestIsNull() {

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validator.validateDefinition(null)
        );

        assertEquals(
                "At least one customization group is required.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenGroupsAreNull() {

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder()
                        .groups(null)
                        .build();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validator.validateDefinition(request)
        );

        assertEquals(
                "At least one customization group is required.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenGroupsAreEmpty() {

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder()
                        .groups(List.of())
                        .build();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> validator.validateDefinition(request)
        );

        assertEquals(
                "At least one customization group is required.",
                exception.getMessage()
        );
    }

}