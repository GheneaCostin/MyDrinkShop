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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryProductRepository implements Repository<Integer, Product> {
    private final Map<Integer, Product> storage = new HashMap<>();

    @Override
    public Product findOne(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Product save(Product entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Product delete(Integer id) {
        return storage.remove(id);
    }

    @Override
    public Product update(Product entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }
}

public class ProductServiceIntegrationTest {

    @Mock
    private Repository<Integer, Product> mockRepo;

    private ProductValidator realValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        realValidator = new ProductValidator();
    }

    // Step 2: Integrare S + V (mock pentru R)
    @Test
    void step2IntegrationSAndVValidProduct() {
        ProductService service = new ProductService(mockRepo, realValidator);
        Product validProduct = new Product(10, "Latte", 12.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);

        service.addProduct(validProduct);

        verify(mockRepo, times(1)).save(validProduct);
    }

    @Test
    void step2IntegrationSAndVInvalidProductThrows() {
        ProductService service = new ProductService(mockRepo, realValidator);
        Product invalidProduct = new Product(-1, "", -5.0, null, null);

        assertThrows(ValidationException.class, () -> service.addProduct(invalidProduct));
        verify(mockRepo, never()).save(any());
    }

    // Step 3: Integrare S + V + R (repository real in-memory)
    @Test
    void step3IntegrationSVRValidProduct() {
        InMemoryProductRepository realRepo = new InMemoryProductRepository();
        ProductService service = new ProductService(realRepo, realValidator);
        Product p = new Product(20, "Apă plată", 3.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        service.addProduct(p);

        assertEquals(1, service.getAllProducts().size());
        assertEquals(p, service.findById(20));
    }

    @Test
    void step3IntegrationSVRInvalidProductNotSaved() {
        InMemoryProductRepository realRepo = new InMemoryProductRepository();
        ProductService service = new ProductService(realRepo, realValidator);
        Product invalid = new Product(-5, "X", -2.0, null, null);

        assertThrows(ValidationException.class, () -> service.addProduct(invalid));
        assertTrue(service.getAllProducts().isEmpty());
    }
}