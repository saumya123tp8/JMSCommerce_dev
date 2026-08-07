package com.example.JMSCommerce.services;

import com.example.JMSCommerce.Adapters.CustomizationAdapter;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CustomizationResponseDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.CustomizationGroup;
import com.example.JMSCommerce.Model.CustomizationValidator;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Services.CustomizationServiceImpl;
import com.example.JMSCommerce.Utility.ProductHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomizationServiceImplTest {

    @Mock
    private ProductRepo productRepository;

    @Mock
    private CustomizationAdapter customizationAdapter;

    @Mock
    private CustomizationValidator customizationValidator;

    @Mock
    private ProductHelper productHelper;

    @InjectMocks
    private CustomizationServiceImpl customizationService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void shouldCreateCustomizationSuccessfully() {

        Long productId = 1L;

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder().build();

        List<CustomizationGroup> groups =
                List.of(new CustomizationGroup());

        CustomizationResponseDTO response =
                CustomizationResponseDTO.builder().build();

        try (MockedStatic<ProductHelper> mocked =
                     mockStatic(ProductHelper.class)) {

            mocked.when(() ->
                    productHelper.getActiveProductOrThrow(
                            productRepository,
                            productId
                    )
            ).thenReturn(product);

            when(customizationAdapter.toEntities(request))
                    .thenReturn(groups);

            when(productRepository.save(product))
                    .thenReturn(product);

            when(customizationAdapter.toCustomizationResponse(product))
                    .thenReturn(response);

            CustomizationResponseDTO result =
                    customizationService.createCustomizations(
                            productId,
                            request
                    );

            assertSame(response, result);

            verify(customizationValidator).validateDefinition(request);

            verify(customizationAdapter)
                    .toEntities(request);

            verify(productRepository)
                    .save(product);

            verify(customizationAdapter)
                    .toCustomizationResponse(product);
        }
    }

    @Test
    void shouldThrowWhenCustomizationAlreadyExists() {

        Long productId = 1L;

        product.addCustomizationGroup(
                new CustomizationGroup()
        );

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder().build();

        try (MockedStatic<ProductHelper> mocked =
                     mockStatic(ProductHelper.class)) {

            mocked.when(() ->
                    productHelper.getActiveProductOrThrow(
                            productRepository,
                            productId
                    )
            ).thenReturn(product);

            BadRequestException exception =
                    assertThrows(
                            BadRequestException.class,
                            () -> customizationService.createCustomizations(
                                    productId,
                                    request
                            )
                    );

            assertEquals(
                    "Customizations already exist for this product.",
                    exception.getMessage()
            );

            verify(productRepository, never())
                    .save(any());
        }
    }

}
