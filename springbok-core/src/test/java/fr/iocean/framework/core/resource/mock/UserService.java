package fr.iocean.framework.core.resource.mock;

import fr.iocean.framework.core.resource.service.ResourceService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ResourceService<User, Long, UserRepository> {
}
