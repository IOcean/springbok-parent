package fr.iocean.framework.core.resource.mock;

import fr.iocean.framework.core.resource.controller.DeletableResourceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends DeletableResourceController<User, Long, UserService> {
}
