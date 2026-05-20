package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest2 {

    @Mock
    private Repository<Integer, Product> mockRepo;

    @Mock
    private ProductValidator mockValidator;

    private ProductService productService;

    private Product validProduct;
    private Product invalidProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(mockRepo, mockValidator);
        validProduct = new Product(1, "Espresso", 7.5, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        invalidProduct = new Product(-1, "", -10.0, null, null);
    }

    @Test
    void testAddProduct_Valid_ShouldCallSave() {
        doNothing().when(mockValidator).validate(validProduct);
        productService.addProduct(validProduct);
        verify(mockValidator, times(1)).validate(validProduct);
        verify(mockRepo, times(1)).save(validProduct);
    }

    @Test
    void testAddProduct_Invalid_ShouldThrowAndNotCallSave() {
        doThrow(new ValidationException("Invalid product"))
                .when(mockValidator).validate(invalidProduct);
        assertThrows(ValidationException.class, () -> productService.addProduct(invalidProduct));
        verify(mockValidator, times(1)).validate(invalidProduct);
        verify(mockRepo, never()).save(any());
    }
}