package fr.iocean.framework.security.service;

import fr.iocean.framework.core.resource.service.ResourceService;
import fr.iocean.framework.security.model.profile.Profile;
import fr.iocean.framework.security.repository.profile.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService extends ResourceService<Profile, Long, ProfileRepository> {
}
