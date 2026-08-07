package com.example.JMSCommerce.Adapter;

import com.example.JMSCommerce.Adapters.CustomizationAdapter;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationGroupRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationOptionRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CustomizationResponseDTO;
import com.example.JMSCommerce.Model.CustomizationGroup;
import com.example.JMSCommerce.Model.CustomizationOption;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Utility.enums.AdjustmentType;
import com.example.JMSCommerce.Utility.enums.SelectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomizationAdapterTest {

    private CustomizationAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CustomizationAdapter();
    }

    @Test
    void shouldConvertRequestToEntities() {

        CreateCustomizationOptionRequestDTO optionDTO =
                CreateCustomizationOptionRequestDTO.builder()
                        .name("Extra Cheese")
                        .adjustmentType(AdjustmentType.FIXED)
                        .adjustmentValue(BigDecimal.valueOf(50))
                        .displayOrder(1)
                        .build();

        CreateCustomizationGroupRequestDTO groupDTO =
                CreateCustomizationGroupRequestDTO.builder()
                        .name("Toppings")
                        .selectionType(SelectionType.MULTIPLE)
                        .required(true)
                        .minSelection(1)
                        .maxSelection(3)
                        .displayOrder(1)
                        .options(List.of(optionDTO))
                        .build();

        CreateCustomizationRequestDTO request =
                CreateCustomizationRequestDTO.builder()
                        .groups(List.of(groupDTO))
                        .build();

        List<CustomizationGroup> groups = adapter.toEntities(request);

        assertEquals(1, groups.size());

        CustomizationGroup group = groups.get(0);

        assertEquals("Toppings", group.getName());
        assertEquals(SelectionType.MULTIPLE, group.getSelectionType());
        assertTrue(group.getRequired());
        assertEquals(1, group.getMinSelection());
        assertEquals(3, group.getMaxSelection());
        assertEquals(1, group.getDisplayOrder());

        assertEquals(1, group.getOptions().size());

        CustomizationOption option = group.getOptions().get(0);

        assertEquals("Extra Cheese", option.getName());
        assertEquals(AdjustmentType.FIXED, option.getAdjustmentType());
        assertEquals(BigDecimal.valueOf(50), option.getAdjustmentValue());
        assertEquals(1, option.getDisplayOrder());

        // Verify bidirectional relationship
        assertSame(group, option.getCustomizationGroup());
    }

    @Test
    void shouldConvertProductToCustomizationResponse() {

        Product product = new Product();
        product.setId(1L);

        CustomizationGroup group = CustomizationGroup.builder()
                .name("Toppings")
                .selectionType(SelectionType.MULTIPLE)
                .required(true)
                .minSelection(1)
                .maxSelection(3)
                .displayOrder(1)
                .active(true)
                .build();
        group.setId(10L);
        CustomizationOption option = CustomizationOption.builder()
//                .id(100L)
                .name("Extra Cheese")
                .adjustmentType(AdjustmentType.FIXED)
                .adjustmentValue(BigDecimal.valueOf(50))
                .displayOrder(1)
                .active(true)
                .build();
        option.setId(100L);
        group.addOption(option);
        product.addCustomizationGroup(group);

        CustomizationResponseDTO response =
                adapter.toCustomizationResponse(product);

        assertEquals(1L, response.getProductId());
        assertEquals(1, response.getGroups().size());

        var responseGroup = response.getGroups().get(0);

        assertEquals(10L, responseGroup.getId());
        assertEquals("Toppings", responseGroup.getName());
        assertEquals(SelectionType.MULTIPLE, responseGroup.getSelectionType());
        assertTrue(responseGroup.getRequired());
        assertEquals(1, responseGroup.getMinSelection());
        assertEquals(3, responseGroup.getMaxSelection());
        assertEquals(1, responseGroup.getDisplayOrder());
        assertTrue(responseGroup.getActive());

        assertEquals(1, responseGroup.getOptions().size());

        var responseOption = responseGroup.getOptions().get(0);

        assertEquals(100L, responseOption.getId());
        assertEquals("Extra Cheese", responseOption.getName());
        assertEquals(AdjustmentType.FIXED, responseOption.getAdjustmentType());
        assertEquals(BigDecimal.valueOf(50), responseOption.getAdjustmentValue());
        assertEquals(1, responseOption.getDisplayOrder());
        assertTrue(responseOption.getActive());
    }
}