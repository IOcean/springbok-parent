package fr.iocean.framework.core.resource.service;

import fr.iocean.framework.core.SpringbokCoreApplication;
import fr.iocean.framework.core.exception.ResourceNotFoundException;
import fr.iocean.framework.core.resource.mock.User;
import fr.iocean.framework.core.resource.mock.UserRepository;
import fr.iocean.framework.core.resource.mock.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(classes = SpringbokCoreApplication.class)
public class ResourceServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void create_validResource_callsRepositorySaveAndReturnsResource() {
        User validUser = new User().withName("validUserName");
        User savedValidUser = new User().withId(1L).withName("validUserName");
        
        when(repository.save(validUser)).thenReturn(savedValidUser);
        
        User createdUser = service.create(validUser);
        
        verify(repository).save(validUser);
        assertEquals(createdUser, savedValidUser);
    }
    
    @Test
    public void create_invalidResource_callsRepositorySaveAndThrowsConstraintViolationException() {
        User invalidUser = new User().withName("");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        when(repository.save(invalidUser))
            .thenThrow(
                new ConstraintViolationException(
                    validator.validateProperty(invalidUser, "name")));
        
        try {
            service.create(invalidUser);
        } catch (ConstraintViolationException e) {
            verify(repository).save(invalidUser);
            return;
        }
        
        fail();
    }
    
    @Test
    public void create_validResources_callsRepositorySaveAndReturnsResources() {
        List<User> validUsers = 
            Arrays.asList(
                new User().withName("validUserName1"),
                new User().withName("validUserName2"));
    
        
        List<User> savedValidUsers = Arrays.asList(
                new User().withId(1L).withName("validUserName1"),
                new User().withId(2L).withName("validUserName2"));
        
        when(repository.save(validUsers)).thenReturn(savedValidUsers);
        
        List<User> createdUsers = (List<User>)service.create(validUsers);
        
        verify(repository).save(validUsers);
        
        assertEquals(createdUsers, savedValidUsers);
    }
    
    @Test
    public void create_invalidResources_callsRepositorySaveAndThrowsConstraintViolationException() {
        List<User> invalidUsers = 
            Arrays.asList(
                new User().withName(""),
                new User().withName(""));
        
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<? extends ConstraintViolation<User>> constraintViolations = new HashSet<>();
        invalidUsers.forEach((User invalidUser) -> constraintViolations.addAll((Set)validator.validateProperty(invalidUser, "name")));
        
        when(repository.save(invalidUsers))
            .thenThrow(
                new ConstraintViolationException(constraintViolations));
        
        try {
            service.create(invalidUsers);
        } catch (ConstraintViolationException e) {
            verify(repository).save(invalidUsers);
            return;
        }
        
        fail();
    }
    
    @Test
    public void exists_nullResourceId_returnsFalse() {
        assertFalse(service.exists(null));
    }
    
    @Test
    public void exists_existingResourceId_callsRepositoryExistsAndReturnsTrue() {
        when(repository.exists(1L)).thenReturn(true);
        
        assertTrue(service.exists(1L));
        verify(repository).exists(1L);
    }
    
    @Test
    public void exists_nonExistingResourceId_callsRepositoryExistsAndReturnsFalse() {
        when(repository.exists(-1L)).thenReturn(false);
        
        assertFalse(service.exists(-1L));
        verify(repository).exists(-1L);
    }
    
    @Test
    public void count_callsRepositoryCountAndReturnsSameResult() {
        when(repository.count()).thenReturn(22L);
        
        assertEquals(service.count(), 22);
        verify(repository).count();
    }
    
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void findOne_nullId_throwsResourceNotFoundException() {
        service.findOne(null);
    }
    
    @Test
    public void findOne_nonExistingResourceId_throwsResourceNotFoundException() {
        when(repository.findOne(-1L)).thenThrow(new ResourceNotFoundException(""));
        
        try {
             service.findOne(-1L);
        } catch (ResourceNotFoundException e) {
            verify(repository).findOne(-1L);
            return;
        }
        
        fail();
    }
    
    @Test
    public void findOne_existingResourceId_callsRepositoryFindOneAndReturnsResource() {
        User existingUser = new User().withId(1L).withName("existingUserName");
        
        when(repository.findOne(existingUser.getId())).thenReturn(existingUser);
        
        User foundUser = service.findOne(existingUser.getId());
        
        verify(repository).findOne(existingUser.getId());
        assertEquals(foundUser, existingUser);
    }
    
    @Test
    public void findAll_callsRepositoryFindAllAndReturnsAllResources() {
        List<User> allUsers = 
            Arrays.asList(
                new User().withId(1L).withName("userName1"),
                new User().withId(2L).withName("userName2"),
                new User().withId(3L).withName("userName3"));
        
        when(repository.findAll()).thenReturn(allUsers);
        
        List<User> users = (List<User>)service.findAll();
        
        verify(repository).findAll();
        assertEquals(users, allUsers);
    }
    
    @Test
    public void findAll_sort_callsRepositoryFindAllWithSort() {
        Sort sort = new Sort(Sort.Direction.ASC, "id", "name");
        
        service.findAll(sort);
        verify(repository).findAll(sort);
    }
    
    @Test
    public void findAll_pageRequest_callsRepositoryFindAllWithPageRequest() {
        PageRequest userPageRequest = new PageRequest(1, 10, Sort.Direction.ASC, "id", "name");
        
        service.findAll(userPageRequest);
        verify(repository).findAll(userPageRequest);
    }
    
    @Test
    public void findAll_multipleResourcesIds_callsRepositoryFindAllWithMultipleResourcesIds() {
        List<Long> usersIds = Arrays.asList(1L, 2L);
        
        service.findAll(usersIds);
        verify(repository).findAll(usersIds);
    }
    
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void update_nullResourceId_throwsResourceNotFoundException() {
        service.update(null, null);
    }
    
    @Test
    public void update_nonExistingResourceId_callsRepositoryExistsAndThrowsResourceNotFoundException() {
        when(repository.exists(-1L)).thenReturn(false);
        
        try {
            service.update(-1L, null);
        } catch (ResourceNotFoundException e) {
            verify(repository).exists(-1L);
            return;
        }
        
        fail();
    }
    
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void update_nullResource_throwsResourceNotFoundException() {
        when(repository.exists(1L)).thenReturn(true);
        
        service.update(1L, null);
    }
    
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void update_existingResourceIdButDiffersFromActualResourceId_throwsResourceNotFoundException() {
        when(repository.exists(1L)).thenReturn(true);
        
        service.update(1L, new User().withId(2L).withName("userName"));
    }
    
    @Test
    public void update_existingResourceIdAndValidResource_callsRepositorySaveWithResourceAndReturnsResource() {
        User user = new User().withId(1L).withName("userName");
        when(repository.exists(user.getId())).thenReturn(true);
        when(repository.save(user)).thenReturn(user);
        
        User updatedUser = service.update(user.getId(), user);
        
        verify(repository).save(user);
        assertEquals(updatedUser, user);
    }
    
    @Test
    public void update_multipleResources_callsReposiotrySaveWithMultipleResources() {
        List<User> users = 
            Arrays.asList(
                new User().withId(1L).withName("userName1"),
                new User().withId(2L).withName("userName2"));
        
        service.update(users);
        verify(repository).save(users);
    }
    
    @Test
    public void delete_nonExistingResourceId_callsRepositoryExistsAndThrowsResourceNotFoundException() {
        when(repository.exists(-1L)).thenThrow(new ResourceNotFoundException(""));
        
        try {
             service.delete(-1L);
        } catch (ResourceNotFoundException e) {
            verify(repository).exists(-1L);
            return;
        }
        
        fail();
    }
    
    @Test
    public void delete_existingResourceId_callsRepositoryExistsAndDelete() {
        when(repository.exists(1L)).thenReturn(true);
        
        service.delete(1L);
        verify(repository).exists(1L);
        verify(repository).delete(1L);
    }

    @Test
    public void delete_resource_callsRepositoryDeleteWithResource() {
        User user = new User().withId(1L).withName("userName");
        
        service.delete(user);
        verify(repository).delete(user);
    }
    
    @Test
    public void delete_existingResourceIds_deletesResources() {
        User existingUser1 = new User().withId(1L).withName("userName1");
        User existingUser2 = new User().withId(2L).withName("userName2");
        
        when(repository.findAll(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(existingUser1, existingUser2));
        service.delete(Arrays.asList(1L, 2L));
        
        verify(repository).delete(existingUser1);
        verify(repository).delete(existingUser2);
    }
    
    @Test
    public void delete_nonExistingResourceIds_doesntCallRepositoryDeleteWithResourceIds() {
        User nonExistingUser1 = new User().withId(-1L).withName("userName1");
        User nonExistingUser2 = new User().withId(-2L).withName("userName2");
        
        when(repository.findAll(Arrays.asList(-1L, -2L))).thenReturn(new ArrayList<>());
        service.delete(Arrays.asList(-1L, -2L));
        
        verify(repository, never()).delete(nonExistingUser1);
        verify(repository, never()).delete(nonExistingUser2);
    }
    
    @Test
    public void delete_nonExistingAndExistingResourceIds_deletesOnlyExistingResources() {
        User existingUser1 = new User().withId(1L).withName("userName1");
        User existingUser2 = new User().withId(2L).withName("userName2");
        User nonExistingUser = new User().withId(-3L).withName("userName3");
        
        when(repository.findAll(Arrays.asList(1L, 2L, -3L))).thenReturn(Arrays.asList(existingUser1, existingUser2));
        service.delete(Arrays.asList(1L, 2L, -3L));
        
        verify(repository).delete(existingUser1);
        verify(repository).delete(existingUser2);
        verify(repository, never()).delete(nonExistingUser);
    }
    
    @Test
    public void delete_multipleResources_callsRepositoryDeleteWithResources() {
        List<User> users = 
            Arrays.asList(
                new User().withId(1L).withName("userName1"),
                new User().withId(2L).withName("userName2"));
        
        service.delete(users);
        verify(repository).delete(users);
    }
    
    @Test
    public void deleteAll_callsRepositoryDeleteAll() {
        service.deleteAll();
        verify(repository).deleteAll();
    }
}
