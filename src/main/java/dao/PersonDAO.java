package dao;

import mappers.EmptyPersonMapper;
import models.Book;
import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import mappers.BookMapper;
import mappers.EmptyBookMapper;
import mappers.PersonMapper;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> showAllPeople() {
        return jdbcTemplate.query("SELECT * FROM PERSON ORDER BY id", new PersonMapper());
    }

    public Person showPersonById(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                new PersonMapper()).stream().findAny().orElse(null);
    }

    public Person showPersonByName(String name) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new Object[]{name},
                new PersonMapper()).stream().findAny().orElse(null);
    }

    public Person showPersonByTg(String tg) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE tg=?", new Object[]{tg},
                new PersonMapper()).stream().findAny().orElse(null);
    }

    public void insertPerson(Person person) {
        jdbcTemplate.update("INSERT INTO PERSON(name, tg) values(?, ?)",
                person.getName(), person.getTg());
    }

    public void removePerson(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }

    public void editPerson(Person newOne) {
        Person oldOne =  showPersonById(newOne.getId());
        oldOne.setName(newOne.getName());
        oldOne.setTg(newOne.getTg());
        jdbcTemplate.update("UPDATE Person SET name = ?, tg = ? WHERE id = ?",
                oldOne.getName(), oldOne.getTg(), oldOne.getId());
    }

    public List<Person> getPeopleReadingBook(int book_id) {
        List<Person> list =  jdbcTemplate.query("SELECT * FROM PERSON_BOOK WHERE book_id = ?",
                new Object[]{book_id}, new EmptyPersonMapper());
        for (int i = 0; i < list.size(); i++) {
            Person person = list.get(i);
            person = jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{person.getId()}, new PersonMapper())
                    .stream().findAny().orElse(null);
            list.set(i, person);
        }
        return list;
    }
}
