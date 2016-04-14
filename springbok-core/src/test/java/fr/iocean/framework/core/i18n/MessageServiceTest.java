package fr.iocean.framework.core.i18n;

import fr.iocean.framework.core.SpringbokCoreApplication;
import fr.iocean.framework.test.integration.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringApplicationConfiguration(classes = SpringbokCoreApplication.class)
public class MessageServiceTest extends IntegrationTest {

    @Test
    public void test_get_message_default_locale() throws Exception {
        this.mockMvc.perform(get("/msg")
                .param("code", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(content().string(equalTo("Bonjour")));
    }

    @Test
    public void test_get_message_en_locale() throws Exception {
        this.mockMvc.perform(get("/msg")
                .header("Accept-Language", "en")
                .param("code", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(content().string(equalTo("Hello")));
    }

    @Test
    public void test_get_message_fr_locale() throws Exception {
        this.mockMvc.perform(get("/msg")
                .header("Accept-Language", "fr")
                .param("code", "test")
                .param("lang", "fr")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(content().string(equalTo("Bonjour")));
    }

    @Test
    public void test_get_message_unknown_locale() throws Exception {
        this.mockMvc.perform(get("/msg")
                .header("Accept-Language", "es")
                .param("code", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(content().string(equalTo("Bonjour")));
    }

    @Test
    public void test_global_exception_message_default_locale() throws Exception {
        this.mockMvc.perform(get("/msg/global")
                .param("code", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.message").value("test n'est pas valide"));
    }

    @Test
    public void test_global_exception_message_en_locale() throws Exception {
        this.mockMvc.perform(get("/msg/global")
                .header("Accept-Language", "en")
                .param("code", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("test is not valid"));
    }
}
