package fr.iocean.framework.security.repository;

import fr.iocean.framework.core.resource.repository.ResourceRepository;
import fr.iocean.framework.security.model.account.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ResourceRepository<Account, Long> {

    Account findByUsernameIgnoreCaseAndEnabled(final String username, final boolean enabled);
    
}