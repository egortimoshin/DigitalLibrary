package util;

import models.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if (book.getYear() < 0) {
            errors.rejectValue("year", "", "Не правильно введен год издания");
        }

        if (book.getQuantity() < 0) {
            errors.rejectValue("quantity", "", "Недопустимое количество книг");
        }
    }
}
