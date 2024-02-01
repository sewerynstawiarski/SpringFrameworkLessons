package guru.springframework.spring6app.bootstrap;

import guru.springframework.spring6app.domain.Author;
import guru.springframework.spring6app.domain.Book;
import guru.springframework.spring6app.domain.Publisher;
import guru.springframework.spring6app.respositories.AuthorRepository;
import guru.springframework.spring6app.respositories.BookRepository;
import guru.springframework.spring6app.respositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BootstrapData(AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Author eric  = new Author();
        eric.setFirstName("Eric");
        eric.setLastName("Evans");

        Book ddd = new Book();
        ddd.setTitle("Domain Driven Design");
        ddd.setIsbn("654321");

        Author ericSaved = authorRepository.save(eric);
        Book dddSaved = bookRepository.save(ddd);

        Author rod  = new Author();
        rod.setFirstName("Rod");
        rod.setLastName("Johnson");

        Book noEJB = new Book();
        noEJB.setTitle("J2EE Development without EJB");
        noEJB.setIsbn("9876545");

        Publisher newPublisher = new Publisher();
        newPublisher.setPublisherName("WindAndWater");
        newPublisher.setCity("Wrocław");
        newPublisher.setAddress("Parkowa");
        newPublisher.setState("Colorado");
        newPublisher.setZip("66-109");

        Publisher newPublisherSaved = publisherRepository.save(newPublisher);

        Author rodSaved = authorRepository.save(rod);
        Book noEJBSaved = bookRepository.save(noEJB);

        ericSaved.getBooks().add(dddSaved);
        rodSaved.getBooks().add(noEJBSaved);
        dddSaved.getAuthors().add(ericSaved);
        noEJBSaved.getAuthors().add(rodSaved);

        dddSaved.setPublisher(newPublisherSaved);
        noEJBSaved.setPublisher(newPublisherSaved);

        authorRepository.save(ericSaved);
        authorRepository.save(rodSaved);
        bookRepository.save(dddSaved);
        bookRepository.save(noEJBSaved);

        System.out.println("In Bootstrap");
        System.out.println("Author count: " + authorRepository.count());
        System.out.println("Books count: " + bookRepository.count());
        System.out.println("Publisher count: " +  publisherRepository.count());

    }
}
