package guru.springframework.spring6app.respositories;

import guru.springframework.spring6app.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
