package fr.iocean.framework.core.resource.mock;

import fr.iocean.framework.core.resource.repository.ResourceRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ResourceRepository<User, Long>{
    
    public User findFirstByName(final String name);
}
