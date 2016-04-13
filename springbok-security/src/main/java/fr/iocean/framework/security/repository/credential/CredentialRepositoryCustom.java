package fr.iocean.framework.security.repository.credential;

import fr.iocean.framework.security.model.credential.Credential;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface CredentialRepositoryCustom {

    PageImpl<Credential> search(Pageable pageable, Long accountId, String username, Long profileId);
    
}
