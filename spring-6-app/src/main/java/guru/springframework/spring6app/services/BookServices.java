package guru.springframework.spring6app.services;

import guru.springframework.spring6app.domain.Book;

public interface BookServices {
    Iterable<Book> findAll();
}
