package controllers;

import dao.BookDAO;
import dao.PersonDAO;
import jakarta.validation.Valid;
import models.Book;
import models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import util.BookValidator;

@Controller
@ComponentScan("dao")
@ComponentScan("util")
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String showAllBooks(Model model) {
        model.addAttribute("books", bookDAO.showAllBooks());
        return "/books/showAll";
    }

    @GetMapping("/{id}")
    public String showBook(Model model, @PathVariable("id") int id,
                           @ModelAttribute("person") Person peron) {
        model.addAttribute("book", bookDAO.showBook(id));
        model.addAttribute("people", personDAO.getPeopleReadingBook(id));
        model.addAttribute("allPeople", personDAO.showAllPeople());
        return "/books/showBook";
    }

    @GetMapping("/new")
    public String showNewPersonView(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult br) {
        bookValidator.validate(book, br);
        if (br.hasErrors()) return "/books/new";
        bookDAO.insertBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showBookEditionView(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.showBook(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String editBook(@ModelAttribute("book") @Valid Book book, BindingResult br) {
        bookValidator.validate(book, br);
        if (br.hasErrors()) return "/books/edit";
        bookDAO.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/{book_id}/connect")
    public String giveBookToPerson(@PathVariable("book_id") int book_id,
                              @ModelAttribute("person") Person person,
                              Model model) {
        Book book = bookDAO.showBook(book_id);
        if (book.getQuantity() != 0) bookDAO.giveBookToPerson(book, person);
        return "redirect:/books/{book_id}";
    }
}