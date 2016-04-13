package fr.iocean.framework.security.controller;

import fr.iocean.framework.core.resource.controller.ResourceController;
import fr.iocean.framework.security.model.profile.Profile;
import fr.iocean.framework.security.service.ProfileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.iocean.framework.security.SpringbokSecurityApplication.API_ROOT_PATH;

@Transactional
@RestController
@RequestMapping(value = API_ROOT_PATH + "/profiles")
public class ProfileController extends ResourceController<Profile, Long, ProfileService> {
}
