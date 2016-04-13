package fr.iocean.framework.core.resource.integration;

import fr.iocean.framework.core.SpringbokCoreApplication;
import fr.iocean.framework.core.resource.json.JsonObject;
import fr.iocean.framework.core.resource.json.JsonString;
import fr.iocean.framework.core.resource.mock.User;
import fr.iocean.framework.core.resource.mock.UserRepository;
import fr.iocean.framework.test.integration.IntegrationTest;
import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import org.testng.annotations.Test;

@SpringApplicationConfiguration(classes = SpringbokCoreApplication.class)
public class ResourceIntegrationTest extends IntegrationTest {
    
    @Autowired
    private UserRepository repository;
    
    @Test
    public void create_validResource_returnsHttpCreatedAndCreatedResource() throws Exception {
        User validUser = new User().withName("validUserName");
        
        MvcResult result = 
            mockMvc
                .perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonString(validUser).value()))
                .andExpect(status().isCreated())
                .andReturn();
        
        User createdUser = new JsonObject<>(result.getResponse().getContentAsString(), User.class).value().get();
        assertNotNull(createdUser.getId());
        assertEquals(createdUser.getName(), validUser.getName());
    }
    
    @Test
    public void create_validResource_persistsResource() throws Exception {
        User validUser = new User().withName("validUserName");
        long userCountBeforeCreate = repository.count();
        
        mockMvc
            .perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(validUser).value()));
        
        User persistedUser = repository.findFirstByName(validUser.getName());
        long userCountAfterCreate = repository.count();
        
        assertNotNull(persistedUser);
        assertEquals(persistedUser.getName(), validUser.getName());
        assertEquals(userCountAfterCreate, userCountBeforeCreate + 1);
    }
    
    @Test
    public void create_invalidResource_returnsHttpBadREquest() throws Exception {
        User invalidUser = new User().withName("");
        
        mockMvc
            .perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(invalidUser).value()))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void create_invalidResource_doesntPersistResource() throws Exception {
        User invalidUser = new User().withName("");
        long userCountBeforeCreate = repository.count();
        
        mockMvc
            .perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(invalidUser).value()));
        
        User persistedUser = repository.findFirstByName(invalidUser.getName());
        long userCountAfterCreate = repository.count();
        
        assertNull(persistedUser);
        assertEquals(userCountAfterCreate, userCountBeforeCreate);
    }
    
    @Test
    public void getOne_existingResource_returnsHttpOkAndResource() throws Exception {
        User existingUser = repository.findOne(1L);
        
        MvcResult result = 
            mockMvc
                .perform(get("/user/" + existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        User retrievedUser = new JsonObject<>(result.getResponse().getContentAsString(), User.class).value().get();
        assertEquals(retrievedUser, existingUser);
    }
    
    @Test
    public void getOne_nonExistingResource_returnsHttpNotfound() throws Exception {
        User nonExistingUser = new User().withId(-1L).withName("nameOfNonExistingUser");
                
        mockMvc
            .perform(get("/user/" + nonExistingUser.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void getAll_noParameters_returnsHttpOkAndPage0WithSize20SortedByIdAsc() throws Exception {
        mockMvc
            .perform(get("/user")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(20)))
            .andExpect(jsonPath("$.totalPages").value(2))
            .andExpect(jsonPath("$.totalElements").value(22))
            .andExpect(jsonPath("$.sort[0].direction").value("ASC"))
            .andExpect(jsonPath("$.sort[0].property").value("id"))
            .andExpect(jsonPath("$.numberOfElements").value(20))
            .andExpect(jsonPath("$.size").value(20))
            .andExpect(jsonPath("$.number").value(0));
    }
    
    @Test
    public void getAll_page1WithSize2SortedByNameDesc_returnsHttpOkAndPage1WithSize2SortedByNameDesc() throws Exception {
        mockMvc
            .perform(get("/user?pageNumber=1&pageSize=2&direction=DESC&properties=name")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.totalPages").value(11))
            .andExpect(jsonPath("$.totalElements").value(22))
            .andExpect(jsonPath("$.sort[0].direction").value("DESC"))
            .andExpect(jsonPath("$.sort[0].property").value("name"))
            .andExpect(jsonPath("$.numberOfElements").value(2))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.number").value(1));
    }
    
    @Test
    public void getAll_sortedByNameDescThenByIdDesc_returnsPageSortedByNameDescThenByIdDesc() throws Exception {
        mockMvc
            .perform(get("/user?direction=DESC&properties=name,id")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sort[0].direction").value("DESC"))
            .andExpect(jsonPath("$.sort[0].property").value("name"))
            .andExpect(jsonPath("$.sort[1].direction").value("DESC"))
            .andExpect(jsonPath("$.sort[1].property").value("id"));
    }

    @Test
    public void getAll_invalidPageNumber_returnsHttpBadRequest() throws Exception {
        mockMvc
            .perform(get("/user?pageNumber=-1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        
        mockMvc
            .perform(get("/user?pageNumber=invalidPageNumber")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void getAll_invalidPageSize_returnsHttpBadRequest() throws Exception {
        mockMvc
            .perform(get("/user?pageSize=-1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        
        mockMvc
            .perform(get("/user?pageSize=invalidPageSize")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void getAll_invalidDirection_returnsHttpBadRequest() throws Exception {
        mockMvc
            .perform(get("/user?direction=invalidDirection")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void update_validUpdatedResource_returnsHttpOkAndUpdatedResource() throws Exception {
        User validUpdatedUser = repository.findOne(1L);
        validUpdatedUser.setName("updatedName");
        
        MvcResult result = 
            mockMvc
                .perform(put("/user/" + validUpdatedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonString(validUpdatedUser).value()))
                .andExpect(status().isOk())
                .andReturn();
        
        User retrievedUser = new JsonObject<>(result.getResponse().getContentAsString(), User.class).value().get();
        assertEquals(retrievedUser, validUpdatedUser);
    }
    
    @Test
    public void update_validUpdatedResource_updatesResource() throws Exception {
        User originalUser = repository.findOne(1L);
        User validUpdatedUser = new User().withId(originalUser.getId()).withName("updatedName");
        
        mockMvc
            .perform(put("/user/" + validUpdatedUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(validUpdatedUser).value()));
        
        assertEquals(repository.findOne(1L).getName(), validUpdatedUser.getName());
    }
    
    @Test
    public void update_invalidUpdatedResource_returnsHttpBadRequest() throws Exception {
        User invalidUpdatedUser = repository.findOne(1L);
        invalidUpdatedUser.setName("");
        
        mockMvc
            .perform(put("/user/" + invalidUpdatedUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(invalidUpdatedUser).value()))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void update_invalidUpdatedResource_doesntUpdateResource() throws Exception {
        User originalUser = repository.findOne(1L);
        User invalidUpdatedUser = new User().withId(originalUser.getId()).withName("");
        
        mockMvc
            .perform(put("/user/" + invalidUpdatedUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(invalidUpdatedUser).value()));
        
        assertEquals(repository.findOne(1L).getName(), originalUser.getName());
    }
    
    @Test
    public void update_nonExistingResourceId_returnsHttpNotFound() throws Exception {
        User user = new User().withId(-1L).withName("name");
        
        mockMvc
            .perform(put("/user/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(user).value()))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void update_existingResourceIdButDiffersFromActualResourceId_returnsHttpNotFound() throws Exception {
        User user = repository.findOne(1L);
        
        mockMvc
            .perform(put("/user/" + -1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(user).value()))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void update_existingResourceIdButDiffersFromActualResourceId_doesntUpdateResource() throws Exception {
        User originalUser = repository.findOne(1L);
        User updatedUser = new User().withId(originalUser.getId()).withName("updatedName");
        
        mockMvc
            .perform(put("/user/" + -1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(updatedUser).value()));
        
        assertEquals(repository.findOne(1L).getName(), originalUser.getName());
    }
    
    @Test
    public void delete_existingResource_returnsHttpGone() throws Exception {
        mockMvc
            .perform(delete("/user/1"))
            .andExpect(status().isGone());
    }
    
    @Test
    public void delete_existingResource_deletesResource() throws Exception {
        mockMvc
            .perform(delete("/user/1"));
        
        assertFalse(repository.exists(1L));
    }
    
    @Test
    public void delete_nonExistingResource_returnsHttpNotFound() throws Exception {
        mockMvc
            .perform(delete("/user/-1"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void delete_existingResourceIds_returnsHttpGone() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(Arrays.asList(1L, 2L)).value()))
            .andExpect(status().isGone());
    }
    
    @Test
    public void delete_existingResourceIds_deletesResources() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(Arrays.asList(1L, 2L)).value()));
        
        assertFalse(repository.exists(1L));
        assertFalse(repository.exists(2L));
    }
    
    @Test
    public void delete_nonExistingResourceIds_returnsHttpGone() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(Arrays.asList(-1L, -2L)).value()))
            .andExpect(status().isGone());
    }
    
    @Test
    public void delete_nonExistingAndExistingResourceIds_returnsHttpGone() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(Arrays.asList(-1L, -2L)).value()))
            .andExpect(status().isGone());
    }
    
    @Test
    public void delete_nonExistingAndExistingResourceIds_deletesExistingResources() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(Arrays.asList(1L, 2L, -3L)).value()));
        
        assertFalse(repository.exists(1L));
        assertFalse(repository.exists(2L));
    }
    
    @Test
    public void delete_null_returnsHttpBadRequest() throws Exception {
        mockMvc
            .perform(post("/user/deleteSeveral")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JsonString(null).value()))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void deleteAll_returnsHttpGone() throws Exception {
        mockMvc
            .perform(delete("/user"))
            .andExpect(status().isGone());
    }
    
    @Test
    public void deleteAll_deletesAllResources() throws Exception {
        mockMvc
            .perform(delete("/user"));
        
        assertEquals(repository.count(), 0);
    }
}
