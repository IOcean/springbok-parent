package fr.iocean.framework.security.controller;

import fr.iocean.framework.core.resource.controller.ResourceController;
import fr.iocean.framework.security.model.profile.ProfileCredential;
import fr.iocean.framework.security.service.ProfileCredentialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.iocean.framework.security.SpringbokSecurityApplication.API_ROOT_PATH;

@RestController
@RequestMapping(API_ROOT_PATH + "/profilecredentials")
public class ProfileCredentialController extends ResourceController<ProfileCredential, Long, ProfileCredentialService> {
}
