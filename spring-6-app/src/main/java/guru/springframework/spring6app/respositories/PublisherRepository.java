package guru.springframework.spring6app.respositories;

import guru.springframework.spring6app.domain.Book;
import guru.springframework.spring6app.domain.Publisher;
import org.springframework.data.repository.CrudRepository;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

}
