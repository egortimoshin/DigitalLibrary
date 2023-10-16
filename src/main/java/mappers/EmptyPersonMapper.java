package mappers;

import models.Person;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmptyPersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("person_id");
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
