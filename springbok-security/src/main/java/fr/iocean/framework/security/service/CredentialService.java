package fr.iocean.framework.security.service;

import fr.iocean.framework.core.resource.service.ResourceService;
import fr.iocean.framework.security.model.credential.Credential;
import fr.iocean.framework.security.repository.credential.CredentialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CredentialService extends ResourceService<Credential, Long, CredentialRepository> {

    public Page<Credential> search(Pageable pageRequest, Long accountId, String username, Long profileId) {
        return repository.search(pageRequest, accountId, username, profileId);
    }

}
