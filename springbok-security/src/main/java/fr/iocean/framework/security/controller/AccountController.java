package fr.iocean.framework.security.controller;

import fr.iocean.framework.core.resource.controller.DeletableResourceController;
import fr.iocean.framework.core.resource.exception.ResourceNotFoundException;
import fr.iocean.framework.security.model.account.Account;
import fr.iocean.framework.security.service.SecurityService;
import fr.iocean.framework.security.service.AccountService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static fr.iocean.framework.security.SpringbokSecurityApplication.API_ROOT_PATH;

@RestController
@RequestMapping(API_ROOT_PATH + "/accounts")
public class AccountController extends DeletableResourceController<Account, Long, AccountService> {
    
    @Autowired
    protected SecurityService securityService;

    @RequestMapping(value = "authenticated", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Account getAuthenticated() throws NotFoundException {
        Account authenticatedAccount = securityService.getConnectedAccount();
        
        if (authenticatedAccount == null) {
            throw new ResourceNotFoundException("There is no connected account");
        }

        return authenticatedAccount;
    }
    
    @PreAuthorize("hasRole('DELETE')")
    @Override
    public void deleteAll() {
        super.deleteAll();
    }

    @PreAuthorize("hasRole('DELETE')")
    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        super.delete(id);
    }
}
