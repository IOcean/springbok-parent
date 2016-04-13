package fr.iocean.framework.core.resource.json;

import fr.iocean.framework.core.resource.mock.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JsonStringTest {
    
    @Test
    public void newJsonString_resource_returnsResourceAsJsonString() {
        User user = new User().withId(1L).withName("John");
        String expectedUserJsonString = "{\"id\":1,\"name\":\"John\"}";
        String jsonString = new JsonString(user).value();
        
        assertEquals(jsonString, expectedUserJsonString);
    }
    
    @Test
    public void newJsonString_null_returnsNullString() {
        String expectedUserJsonString = "null";
        String jsonString = new JsonString(null).value();
        
        assertEquals(jsonString, expectedUserJsonString);
    }
}
