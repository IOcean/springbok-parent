package fr.iocean.framework.security.authentication.http;

import fr.iocean.framework.security.Application;
import fr.iocean.framework.test.integration.SecuredIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import static fr.iocean.framework.security.Application.API_ROOT_PATH;
import org.apache.commons.codec.binary.Base64;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = Application.class)
public class HttpBasicAuthenticationIntegrationTest extends SecuredIntegrationTest {
    
    @Test
    public void publicRequest_unauthenticated_returnsHttpOk() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/public/constants")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser
    public void publicRequest_authenticated_returnsHttpOk() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/public/constants")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void request_unauthenticated_returnsHttpUnauthorized() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void request_invalidCredentials_returnsHttpUnauthorized() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts")
            .header("Authorization", generateAuthorizationHeader("invalidUser", "invalidPassword"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void request_validCredentials_returnsHttpOk() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts")
            .header("Authorization", generateAuthorizationHeader("John", "password"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void request_credentialsCheckIsCaseInsensitive() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts")
            .header("Authorization", generateAuthorizationHeader("john", "password"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @WithMockUser
    @Test
    public void authenticated_canRequestSecuredResources() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @WithMockUser
    @Test
    public void userWithoutDeleteCredential_deleteResourcesProtectedByDeleteCredential_returnsHttpForbidden() throws Exception {
        mockMvc
            .perform(delete(API_ROOT_PATH + "/accounts")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
    
    @WithMockUser(roles = "DELETE")
    @Test
    public void userWithDeleteCredential_deleteResourcesProtectedByDeleteCredential_returnsCHttpGone() throws Exception {
        mockMvc
            .perform(delete(API_ROOT_PATH + "/accounts")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isGone());
    } 
    
    @Test
    public void getAuthenticatedAccount_notAuthenticated_returnsHttpUnauthorized() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts/authenticated")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
    
    @WithMockUser(username = "John")
    @Test
    public void getAuthenticatedAccount_authenticated_returnsHttpOk() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts/authenticated")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @WithMockUser(username = "John")
    @Test
    public void getAuthenticatedAccount_authenticated_returnsAuthenticatedUserDetails() throws Exception {
        mockMvc
            .perform(get(API_ROOT_PATH + "/accounts/authenticated")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value("John"));
    }
    
    private String generateAuthorizationHeader(final String username, final String password) throws Exception {
        String authorizationHeader = "Basic ";
        
        authorizationHeader += Base64.encodeBase64String((username + ":" + password).getBytes());
        
        return authorizationHeader;
    }
}
