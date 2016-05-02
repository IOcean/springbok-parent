package fr.iocean.framework.security.service;

import fr.iocean.framework.core.exception.ResourceNotFoundException;
import fr.iocean.framework.core.resource.service.ResourceService;
import fr.iocean.framework.security.model.credential.Credential;
import fr.iocean.framework.security.model.profile.Profile;
import fr.iocean.framework.security.model.profile.ProfileCredential;
import fr.iocean.framework.security.model.profile.ProfileAccount;
import fr.iocean.framework.security.model.account.Account;
import fr.iocean.framework.security.repository.profile.ProfileCredentialRepository;
import fr.iocean.framework.security.repository.profile.ProfileAccountRepository;
import fr.iocean.framework.security.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService extends ResourceService<Account, Long, AccountRepository> implements UserDetailsService  {

    @Autowired
    private ProfileAccountRepository profileAccountRepository;
    
    @Autowired
    private ProfileCredentialRepository profileCredentialRepository;

    @Override
    public Account update(Long id, Account resource) {
        Account accountToUpdate = findOne(id);
        
        if (accountToUpdate != null) {
            resource.setPassword(accountToUpdate.getPassword());
        }
        
        return super.update(id, resource);
    }
    
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        Account account = repository.findByUsernameIgnoreCaseAndEnabled(username, true);
        
        if (account == null) {
            log.error(String.format("Username not found : %s", username));
            throw new UsernameNotFoundException("Username not found");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        findCredentialsOfUser(account).forEach(credential -> authorities.add(new SimpleGrantedAuthority(credential.getLabel())));
        
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), authorities);
    }
    
    private List<Credential> findCredentialsOfUser(final Account account) {
        List<Credential> accountCredentials = new LinkedList<>();
        
        List<Profile> accountProfiles = 
            profileAccountRepository.findByAccount(account)
                .stream()
                .map(ProfileAccount::getProfile)
                .collect(Collectors.toList());
        
        List<ProfileCredential> profileCredentials = profileCredentialRepository.findByProfileIn(accountProfiles);
        
        profileCredentials.forEach(profileCredential -> accountCredentials.add(profileCredential.getCredential()));
        
        return accountCredentials;
    }
}