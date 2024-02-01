package guru.springframework.spring6app.services;

import guru.springframework.spring6app.domain.Author;

public interface AuthorService {
    public Iterable<Author> findAll();
}
