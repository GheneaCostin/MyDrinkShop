package drinkshop.service.validator;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {



    @Test
    @DisplayName("TC1 ECP: Produsul valid trebuie să treacă validarea")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_ValidData_ECP() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(1, "Cafea", 50, CategorieBautura.ALL, TipBautura.ALL);


        assertDoesNotThrow(() -> {
            validator.validate(product);
        }, "Validarea unui produs corect nu ar trebui să arunce excepții.");
    }

    @Test
    @DisplayName("TC2 ECP: Prețul negativ trebuie să genereze ValidationException")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_InvalidPriceNegative_ECP() {
        ProductValidator validator = new ProductValidator();
        Product product = new Product(2, "Latte", -10, CategorieBautura.ALL, TipBautura.ALL);


        assertThrows(ValidationException.class, () -> {
            validator.validate(product);
        }, "Un preț mai mic sau egal cu 0 ar trebui să pice validarea.");
    }

    @Test
    @DisplayName("TC3 ECP: Prețul prea mare trebuie să genereze ValidationException")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_InvalidPriceTooHigh_ECP() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(3, "Cola", 1500, CategorieBautura.ALL, TipBautura.ALL);


        assertThrows(ValidationException.class, () -> {
            validator.validate(product);
        }, "Un preț peste 1000 ar trebui să pice validarea.");
    }

    @Test
    @DisplayName("TC4 ECP: Numele prea scurt trebuie să genereze ValidationException")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_InvalidNameTooShort_ECP() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(4, "Ap", 25, CategorieBautura.ALL, TipBautura.ALL);

        assertThrows(ValidationException.class, () -> {
            validator.validate(product);
        }, "Un nume cu lungimea sub 3 caractere ar trebui respins.");
    }


    @Test
    @DisplayName("TC5 BVA: Limita inferioară validă a prețului (1)")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_ValidPriceLowerBound_BVA() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(5, "Ceai", 1, CategorieBautura.ALL, TipBautura.ALL);


        assertDoesNotThrow(() -> {
            validator.validate(product);
        });
    }

    @Test
    @DisplayName("TC6 BVA: Limita superioară invalidă a prețului (1001)")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_InvalidPriceUpperBound_BVA() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(6, "Cafea", 1001, CategorieBautura.ALL, TipBautura.ALL);


        assertThrows(ValidationException.class, () -> {
            validator.validate(product);
        });
    }

    @Test
    @DisplayName("TC7 BVA: Limita inferioară invalidă a numelui (2 caractere)")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_InvalidNameLowerBound_BVA() {

        ProductValidator validator = new ProductValidator();
        Product product = new Product(7, "Ce", 50, CategorieBautura.ALL, TipBautura.ALL);


        assertThrows(ValidationException.class, () -> {
            validator.validate(product);
        });
    }

    @Test
    @DisplayName("TC8 BVA: Limita inferioară validă a numelui (3 caractere)")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testValidateProduct_ValidNameLowerBound_BVA() {
        ProductValidator validator = new ProductValidator();
        Product product = new Product(8, "Apa", 50, CategorieBautura.ALL, TipBautura.ALL);


        assertDoesNotThrow(() -> {
            validator.validate(product);
        });
    }
}