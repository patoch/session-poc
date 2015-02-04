import com.datastax.poc.AppSession;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by patrick on 04/02/15.
 */
public class AppSessionTest {

    static final String SOME_JSON = "{\"session_id\":\"1234\",\"user\":{\"name\":\"Patrick\",\"age\":40,\"a_number\":992,\"last_name\":\"Guillebert\"},\"created_on\":\"02/02/2015\",\"history\":{\"event\":{\"type\":\"creation\"}}}";

    @Test
    public void buildJson() {
        AppSession appSession = new AppSession();
        appSession.addString("session_id", "1234")
                .addObject("user")
                .addString("name", "Patrick")
                .addInt("age", 40)
                .select()
                .addString("created_on", "02/02/2015")
                .select("user")
                .addInt("a_number", 992)
                .select("history", "event")
                .addString("type", "creation")
                .mergeJson("{\"user\":{\"last_name\":\"Guillebert\"}}")
                .mergeChanges();
        
        assertEquals(SOME_JSON, appSession.toString());
    }

    @Test
    public void buildJsonAndChangeIt() {
        AppSession appSession = new AppSession();
        appSession.addString("session_id", "1234")
                .addObject("user")
                .addString("name", "Patrick")
                .addInt("age", 40)
                .select()
                .addString("created_on", "02/02/2015")
                .select("user")
                .addInt("a_number", 992)
                .select("history", "event")
                .addString("type", "creation")
                .mergeJson("{\"user\":{\"last_name\":\"Guillebert\"}}")
                .mergeChanges();

        appSession.select("user")
                .addString("City", "Paris");

        assertEquals(SOME_JSON, appSession.toString());
        assertEquals("{\"user\":{\"City\":\"Paris\"}}", appSession.getChangeJson());

    }

}
