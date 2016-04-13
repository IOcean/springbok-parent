package fr.iocean.framework.security.repository.profile;

import fr.iocean.framework.core.resource.repository.ResourceRepository;
import fr.iocean.framework.security.model.profile.Profile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends ResourceRepository<Profile, Long> {
}