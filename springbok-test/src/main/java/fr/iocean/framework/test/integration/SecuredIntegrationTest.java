package fr.iocean.framework.test.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;

/**
 * Provides a set up for a secured integration testing of a resource endpoint.
 */
@TestExecutionListeners(listeners = {WithSecurityContextTestExecutionListener.class})
@WebAppConfiguration
public abstract class SecuredIntegrationTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired 
    protected WebApplicationContext webApplicationContext;
    
    protected MockMvc mockMvc;
    
    @BeforeMethod
    public void setUpMethod() {
        mockMvc = 
            MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
