package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "ID invalid!\n";


        if (product.getNume() == null || product.getNume().isBlank() || product.getNume().length() < 3 || product.getNume().length() > 50)
            errors += "Numele trebuie sa aiba intre 3 si 50 caractere!\n";


        if (product.getPret() <= 0 || product.getPret() > 1000)
            errors += "Pret invalid (trebuie sa fie intre 1 si 1000)!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
