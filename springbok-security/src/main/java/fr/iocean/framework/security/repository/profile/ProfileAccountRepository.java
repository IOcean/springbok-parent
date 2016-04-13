package fr.iocean.framework.security.repository.profile;


import fr.iocean.framework.core.resource.repository.ResourceRepository;
import fr.iocean.framework.security.model.profile.ProfileAccount;
import fr.iocean.framework.security.model.account.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileAccountRepository extends ResourceRepository<ProfileAccount, Long> {

    List<ProfileAccount> findByAccount(final Account account);

}
