package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.ProductAdapter;
import com.example.JMSCommerce.DTOs.product.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDetailsDTO;
import com.example.JMSCommerce.DTOs.productSpecification.ProductSpecificationValueDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductSpecificationValue;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.ProductSpecificationRepository;
import com.example.JMSCommerce.Repositories.SpecificationDefinitionRepository;
import com.example.JMSCommerce.Utility.ProductHelper;
import com.example.JMSCommerce.Utility.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryService categoryService;
    private final ProductAdapter productAdapter;
    private final ProductHelper productHelper;
    private final ProductSpecificationRepository productSpecificationRepository;
    private final SpecificationDefinitionRepository specificationDefinitionRepository;
    //    public List<Product> getAllProducts() {
//        return productRepo.findAll();
//    }
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductResponseDTO> listProducts = products.stream().map(product -> ProductResponseDTO.builder()
                .name(product.getName())
                .id(product.getId())
                .mrp(product.getMrp())
                .rating(product.getRating())
                .description(product.getDescription())
                .primaryImage(product.getPrimaryImage())
                .build()
        ).collect(Collectors.toList());
        return listProducts;
    }

//    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
//        Category category = categoryService.getCategoryById(productCreateDTO.getCategoryId());
//        Product product = productAdapter.mapProductCreateDTOToProduct(productCreateDTO);
//        product.setCategory(category);
//        productRepo.save(product);
//        return productAdapter.mapProductToResponseDTO(product);
//    }

    @Transactional
    public ProductResponseDTO createProduct(
            ProductCreateDTO productCreateDTO
    ) {

        Category category = categoryService.getCategoryById(
                productCreateDTO.getCategoryId()
        );

        productHelper.validateDuplicateProductName(
                productCreateDTO.getName()
        );

        productHelper.validateSellingPrice(
                productCreateDTO.getMrp(),
                productCreateDTO.getSellingPrice()
        );

        productHelper.validateSku(
                productCreateDTO.getSku()
        );

        productHelper.validateBarcode(
                productCreateDTO.getBarcode()
        );


        Product product = productAdapter
                .mapProductCreateDTOToProduct(productCreateDTO);

        String normalizedName = productHelper.normalizeName(productCreateDTO.getName());
        product.setName(normalizedName);
        product.setCategory(category);

        product.setSlug(
                SlugUtil.generateUniqueSlug(
                        productCreateDTO.getName(),
                        productRepo::existsBySlug
                )
        );
        product = productRepo.save(product);
        if(productCreateDTO.getSpecifications()!=null){
            //all possible - parent hirarchy
            List<SpecificationDefinition> allowedSpecifications = productHelper.fetchAllSpecificationsThroughParent(productCreateDTO.getCategoryId());
            Map<Long, SpecificationDefinition> allowedSpecificationMap =
                    allowedSpecifications.stream()
                            .collect(Collectors.toMap(
                                    SpecificationDefinition::getId,
                                    Function.identity()
                            ));
//            if(allowedSpecificationIds.size()>0){
                List<Long> submittedSpecificationIds = productCreateDTO.getSpecifications().stream().map(
                        productSpecificationValueDTO-> productSpecificationValueDTO.getSpecificationId()
                ).collect(Collectors.toList());
            Set<Long> uniqueIds = Set.copyOf(submittedSpecificationIds);

            if (uniqueIds.size() != submittedSpecificationIds.size()) {
                throw new BadRequestException(
                        "Duplicate specifications submitted."
                );
            }
                List<SpecificationDefinition> specificationDefinitions =
                        specificationDefinitionRepository.findAllById(submittedSpecificationIds);

            for (Long id : submittedSpecificationIds) {

                SpecificationDefinition definition = allowedSpecificationMap.get(id);

                if (definition == null) {
                    throw new BadRequestException(
                            "Specification with id " + id +
                                    " does not belong to the selected category."
                    );
                }

                // Later:
                // definition.getDataType();
                // definition.getRequired();
                // definition.getDisplayName();
            }

                if(specificationDefinitions.size() != submittedSpecificationIds.size()){
                    throw new ResourceNotFoundException(
                            "One or more specifications not found."
                    );
                }

//            }

            List<ProductSpecificationValue> values = new ArrayList<>();
            for (ProductSpecificationValueDTO dto : productCreateDTO.getSpecifications()) {

                SpecificationDefinition definition =
                        allowedSpecificationMap.get(dto.getSpecificationId());

                ProductSpecificationValue value = ProductSpecificationValue.builder()
                        .product(product)
                        .specificationDefinition(definition)
                        .value(dto.getValue())
                        .build();

                values.add(value);
            }
            productSpecificationRepository.saveAll(values);
        }

        return productAdapter.mapProductToResponseDTO(product);
    }


    public Void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product with id " + id + " not found"));

        productRepo.delete(product);
        return null;
    }
    //    public List<Product> getProductByCategory(String category) {
//
//        return productRepo.findByCategory(category);
//    }
    public List<ProductResponseDetailsDTO> getProductByCategory(Long category_id) {
        List<ProductResponseDetailsDTO> productListWithGivenCategory =
                productRepo.findByCategory_Id(category_id).stream().map(
                      product->  productAdapter.mapProductToResponseDetailsDTO(product)
                ).collect(Collectors.toList());
        if (productListWithGivenCategory.isEmpty()) {
            throw new ResourceNotFoundException("Product with Category" + category_id + " not found");
        }
        return productListWithGivenCategory;
//        return productRepo.findByCategory(category_id);
    }

//    public Product getProductByID(Long id) {
//       return productRepo.findById(id).orElseThrow(
//               ()->new RuntimeException("Product with given Id not found")
//       );
//    }

    public ProductResponseDTO getProductByID(Long id) {
        ProductResponseDTO response = productRepo.findById(id).map(
                        product -> productAdapter.mapProductToResponseDTO(product)
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product with given Id not found")
                );
        return response;
    }

    public ProductResponseDetailsDTO findProductDetailById(Long id)
    {
        ProductResponseDetailsDTO productResponseDetailsDTO = productRepo.findProductDetailById(id).map(
                product -> productAdapter.mapProductToResponseDetailsDTO(product)
        ).orElseThrow(
                () -> new ResourceNotFoundException("Product with id " + id + " not found")
        );

        return productResponseDetailsDTO;
    }
}
