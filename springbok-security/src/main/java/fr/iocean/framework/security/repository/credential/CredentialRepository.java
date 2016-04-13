package fr.iocean.framework.security.repository.credential;

import fr.iocean.framework.core.resource.repository.ResourceRepository;
import fr.iocean.framework.security.model.credential.Credential;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends ResourceRepository<Credential, Long>, CredentialRepositoryCustom {
}