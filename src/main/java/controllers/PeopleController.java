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
import util.PersonValidator;

@Controller
@RequestMapping("/people")
@ComponentScan("dao")
@ComponentScan("util")
public class PeopleController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showAllPeople(Model model) {
        model.addAttribute("people", personDAO.showAllPeople());
        return "/people/showAll";
    }

    @GetMapping("/{id}")
    public String showPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.showPersonById(id));
        model.addAttribute("books", bookDAO.getBooksReadByPerson(id));
        return "/people/showPerson";
    }

    @GetMapping("/new")
    public String showNewPersonView(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") @Valid Person person, BindingResult br) {
        personValidator.validate(person, br);
        if (br.hasErrors()) return "/people/new";
        personDAO.insertPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String showPersonEditionView(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.showPersonById(id));
        return "/people/edit";
    }

    @PostMapping("/{id}")
    public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult br) {
        personValidator.validate(person, br);
        if (br.hasErrors()) return "/people/edit";
        personDAO.editPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        personDAO.removePerson(id);
        return "redirect:/people";
    }

    @PostMapping("/{person_id}/release/{book_id}")
    public String takeBookFromPerson(@PathVariable("book_id") int book_id, @PathVariable("person_id") int person_id) {
        Person person = personDAO.showPersonById(person_id);
        Book book = bookDAO.showBook(book_id);
        bookDAO.takeBookFromPerson(person, book);
        return "redirect:/people/{person_id}";
    }
}
