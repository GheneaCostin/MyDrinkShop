package drinkshop.service.validator;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private OrderValidator orderValidator;
    private Validator<OrderItem> itemValidator;

    @BeforeEach
    void setUp() {
        itemValidator = item -> {
            if (item.getProduct().getNume().equals("Bad")) {
                throw new ValidationException("Eroare de la item!");
            }
        };
        orderValidator = new OrderValidator(itemValidator);
    }

    @Test
    void testValidate_P1_ValidOrder() {
        List<OrderItem> items = List.of(new OrderItem(new Product(1, "Cafea", 10, CategorieBautura.ALL, TipBautura.ALL), 1));
        Order order = new Order(1, items, 150.0);
        assertDoesNotThrow(() -> orderValidator.validate(order));
    }

    @Test
    void testValidate_P2_InvalidID() {
        Order order = new Order(0, List.of(new OrderItem(new Product(1, "Apa", 5, CategorieBautura.ALL, TipBautura.ALL), 1)), 5.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> orderValidator.validate(order));
        assertTrue(ex.getMessage().contains("ID comanda invalid"));
    }

    @Test
    void testValidate_P3_EmptyItems() {
        Order order = new Order(1, new ArrayList<>(), 50.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> orderValidator.validate(order));
        assertTrue(ex.getMessage().contains("Comanda fara produse"));
    }

    @Test
    void testValidate_P4_MultipleItems() {
        // Testăm bucla (Loop Coverage) cu 2 elemente
        List<OrderItem> items = List.of(
                new OrderItem(new Product(1, "Suc", 10, CategorieBautura.ALL, TipBautura.ALL), 1),
                new OrderItem(new Product(2, "Ceai", 8, CategorieBautura.ALL, TipBautura.ALL), 2)
        );
        Order order = new Order(10, items, 26.0);
        assertDoesNotThrow(() -> orderValidator.validate(order));
    }

    @Test
    void testValidate_P5_ItemValidationError() {
        List<OrderItem> items = List.of(new OrderItem(new Product(1, "Bad", 10, CategorieBautura.ALL, TipBautura.ALL), 1));
        Order order = new Order(1, items, 10.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> orderValidator.validate(order));
        assertTrue(ex.getMessage().contains("Eroare de la item"));
    }

    @Test
    void testValidate_P6_NegativeTotal() {
        List<OrderItem> items = List.of(new OrderItem(new Product(1, "Bere", 12, CategorieBautura.ALL, TipBautura.ALL), 1));
        Order order = new Order(1, items, -10.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> orderValidator.validate(order));
        assertTrue(ex.getMessage().contains("Total invalid"));
    }

    @Test
    void testValidate_P7_MultipleErrors() {
        // Testăm calea unde ID e invalid ȘI lista e null (cumulăm erori)
        Order order = new Order(-5, null, 100.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> orderValidator.validate(order));

        String msg = ex.getMessage();
        assertTrue(msg.contains("ID comanda invalid") && msg.contains("Comanda fara produse"));
    }
}