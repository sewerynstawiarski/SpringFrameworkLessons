package guru.springframework.spring6app.services;

import guru.springframework.spring6app.domain.Book;
import guru.springframework.spring6app.respositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServicesImpl implements BookServices {
    private final BookRepository bookRepository;

    public BookServicesImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }
}
