package models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class Book {
    private int id;
    @NotEmpty(message = "Название не может быть пустым")
    private String title;

    @Pattern(regexp = "[A-Za-zА-Яа-я ]{3,}", message = "Не правильное имя автора")
    private String author;

    @Min(value = 0, message = "Год издания должен быть >= 0")
    private int year;

    @Min(value = 0, message = "Количество книг должно быть >= 0")
    private int quantity;

    public Book() {
    }

    public Book(int id, String title, String author, int year, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
