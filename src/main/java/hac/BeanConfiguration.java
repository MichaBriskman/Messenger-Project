package hac;

import hac.repo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Bean configuration class sessionScope Bean.
 */
@Configuration
public class BeanConfiguration {
    @Bean(name="loggedUser")
    @SessionScope
    public User sessionUser () {
        return new User();
    }
}
