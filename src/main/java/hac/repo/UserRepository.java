package hac.repo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * User Repository for querying users from the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long Id);
    boolean existsByUserName(String userName);
    User findByUserName(String userName);

}
