package fr.iocean.framework.security.service;

import fr.iocean.framework.core.resource.service.ResourceService;
import fr.iocean.framework.security.model.profile.ProfileCredential;
import fr.iocean.framework.security.repository.profile.ProfileCredentialRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileCredentialService extends ResourceService<ProfileCredential, Long, ProfileCredentialRepository> {
}
