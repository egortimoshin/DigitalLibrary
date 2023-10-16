package dao;

import mappers.EmptyBookMapper;
import models.Book;
import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import mappers.BookMapper;
import mappers.EmptyPersonMapper;
import mappers.PersonMapper;

import java.util.List;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> showAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY id", new BookMapper());
    }

    public Book showBook(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new Object[]{id}, new BookMapper())
                .stream().findAny().orElse(null);
    }

    public void deleteBook(int id) {
       jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    public void updateBook(Book newBook) {
        Book oldBook = showBook(newBook.getId());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setTitle(newBook.getTitle());
        oldBook.setYear(newBook.getYear());
        oldBook.setQuantity(newBook.getQuantity());
        jdbcTemplate.update("UPDATE BOOK SET author = ?, title = ?, year = ?, quantity = ? WHERE id = ?",
                oldBook.getAuthor(), oldBook.getTitle(), oldBook.getYear(), oldBook.getQuantity(), oldBook.getId());
    }

    public void insertBook(Book book) {
        jdbcTemplate.update("INSERT INTO BOOK(title, author, year, quantity) VALUES(?, ?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear(), book.getQuantity());
    }

    public void giveBookToPerson(Book book, Person person) {
        jdbcTemplate.update("UPDATE BOOK SET quantity = ? WHERE id = ?",
                book.getQuantity() - 1, book.getId());
        jdbcTemplate.update("INSERT INTO PERSON_BOOK(person_id, book_id) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM PERSON_BOOK WHERE person_id = ? AND book_id = ?)",
                person.getId(), book.getId(), person.getId(), book.getId());
    }

    public void takeBookFromPerson(Person person, Book book) {
        jdbcTemplate.update("DELETE FROM PERSON_BOOK WHERE person_id = ? AND book_id = ?",
                person.getId(), book.getId());
        jdbcTemplate.update("UPDATE BOOK SET quantity = ? WHERE id = ?",
                book.getQuantity() + 1, book.getId());
    }

    public List<Book> getBooksReadByPerson(int person_id) {
        List<Book> list =  jdbcTemplate.query("SELECT * FROM PERSON_BOOK WHERE person_id = ?",
                new Object[]{person_id}, new EmptyBookMapper());
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            book = jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new Object[]{book.getId()}, new BookMapper())
                    .stream().findAny().orElse(null);
            list.set(i, book);
        }
        return list;
    }
}
