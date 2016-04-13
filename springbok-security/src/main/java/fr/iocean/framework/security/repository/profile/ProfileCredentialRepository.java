package fr.iocean.framework.security.repository.profile;


import fr.iocean.framework.core.resource.repository.ResourceRepository;
import fr.iocean.framework.security.model.profile.Profile;
import fr.iocean.framework.security.model.profile.ProfileCredential;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProfileCredentialRepository extends ResourceRepository<ProfileCredential, Long> {

    List<ProfileCredential> findByProfileIn(final Collection<Profile> profile);
}   
