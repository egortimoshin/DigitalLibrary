package util;

import dao.PersonDAO;
import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Person repeatedPerson = personDAO.showPersonByName(person.getName());
        if (repeatedPerson != null && person.getId() != repeatedPerson.getId()) {
            errors.rejectValue("name", "", "This name is already used");
        }

        repeatedPerson = personDAO.showPersonByTg(person.getTg());
        if (repeatedPerson != null && person.getId() != repeatedPerson.getId()) {
            errors.rejectValue("tg", "", "This tg is already used");
        }
    }
}
