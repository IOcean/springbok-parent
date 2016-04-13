package fr.iocean.framework.security.credential;


import fr.iocean.framework.security.Application;
import fr.iocean.framework.security.controller.CredentialController;
import fr.iocean.framework.security.model.credential.Credential;
import fr.iocean.framework.security.repository.credential.CredentialRepository;
import fr.iocean.framework.test.integration.SecuredIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = Application.class)
    public class CredentialIntegrationTest extends SecuredIntegrationTest {

    @Autowired
    private CredentialRepository repository;

    @Autowired
    private CredentialController credentialController;

    @Test
    @WithMockUser
    public void testSearchPaginated() throws Exception {
        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .param("profileId", "1")
                        .param("pageSize", "1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.totalElements").value(4));

        // using sort
        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .param("properties", "label")
                        .param("direction", "asc")
                        .param("profileId", "1")
        )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.content[0].label").value("CREATE"))
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.totalElements").value(4));
    }

    @Test
    @WithMockUser
    public void testSearch() throws Exception {
        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .param("accountId", "1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.totalElements").value(4));

        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .param("username", "Admin")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.totalElements").value(4));

        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .param("profileId", "2")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    public void testSearchAll() throws Exception {
        int count = repository.findAll().size();

        this.mockMvc.perform(get("/api/credentials/search/").contentType(
                        MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(count));
    }

    @Test(expectedExceptions = {AccessDeniedException.class})
    @WithMockUser
    public void testDeniedAccessCreate() throws Exception {
        credentialController.create(new Credential());
    }

    @Test(expectedExceptions = {AccessDeniedException.class})
    @WithMockUser
    public void testDeniedAccessUpdate() throws Exception {
        credentialController.update(-1l, new Credential());
    }

}
