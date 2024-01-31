package guru.springframework.spring6app.respositories;

import guru.springframework.spring6app.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
