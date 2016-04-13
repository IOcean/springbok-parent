package fr.iocean.framework.security.service;

import fr.iocean.framework.security.model.account.Account;
import fr.iocean.framework.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class SecurityService {
    
    private final AccountRepository accountRepository;

    @Autowired
    public SecurityService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Account getConnectedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        
        return accountRepository.findByUsernameIgnoreCaseAndEnabled(u.getUsername(), true);
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities();
    }
    
    public boolean hasCredential(String credential) {
        GrantedAuthority authority = new SimpleGrantedAuthority(credential);
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesConnectedUser();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }

        return authorities.contains(authority);
    }
}
