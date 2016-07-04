package fr.iocean.framework.core.constants;

import fr.iocean.framework.core.SpringbokCoreApplication;
import fr.iocean.framework.test.integration.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringApplicationConfiguration(classes = SpringbokCoreApplication.class)
public class EnumControllerTest extends IntegrationTest {

    @Test
    public void testEnums() throws Exception {
        this.mockMvc.perform(get("/api/public/constants")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.TestEnum", hasSize(2)))
                .andExpect(content().string(containsString("VALEUR_1")))
                .andExpect(content().string(containsString("VALEUR_2")))
                .andExpect(content().string(containsString("clé 1")))
                .andExpect(content().string(containsString("clé 2")));
    }

    @Test
    public void test_Enums_en_locale() throws Exception {
        this.mockMvc.perform(get("/api/public/constants")
                .header("Accept-Language", "en")
                .characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.TestEnum", hasSize(2)))
                .andExpect(content().string(containsString("VALEUR_1")))
                .andExpect(content().string(containsString("VALEUR_2")))
                .andExpect(content().string(containsString("key 1")))
                .andExpect(content().string(containsString("key 2")));
    }
    
    enum TestEnum implements RestEnum {
        VALEUR_1("my.key.1"),
        VALEUR_2("my.key.2");

        private final String i18nKey;

        TestEnum(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String getI18nKey() {
            return i18nKey;
        }
    }


}
