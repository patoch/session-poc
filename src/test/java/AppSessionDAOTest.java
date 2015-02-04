import com.datastax.poc.AppSession;
import com.datastax.poc.dao.AppSessionDAO;
import com.datastax.poc.dao.Cassandra;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.assertEquals;


/**
 * Created by patrick on 04/02/15.
 */
public class AppSessionDAOTest {

    static final String SOME_JSON = "{\"session_id\":\"1234\",\"user\":{\"name\":\"Patrick\",\"age\":40,\"a_number\":992,\"last_name\":\"Guillebert\"},\"created_on\":\"02/02/2015\",\"history\":{\"event\":{\"type\":\"creation\"}}}";
    static AppSessionDAO dao;

    @BeforeClass
    public static void setup() {
        dao = AppSessionDAO.getInstance();
    }

    @Test
    public void setAndGetAppSession() {
        AppSession appSession = new AppSession();
        UUID sessionId = appSession.getId();

        appSession.mergeJson(SOME_JSON);
        dao.setAppSession(appSession);

        appSession = dao.getAppSession(sessionId);

        assertEquals(SOME_JSON, appSession.toString());
        assertEquals("{}", appSession.getChangeJson());
    }

    @Test
    public void setChangeAndGetAppSession() {
        AppSession appSession = new AppSession();
        UUID sessionId = appSession.getId();

        appSession.mergeJson(SOME_JSON);
        dao.setAppSession(appSession);

        appSession.select("user")
                .addString("City", "Paris");
        dao.setAppSession(appSession);

        appSession = dao.getAppSession(sessionId);

        assertEquals("{\"session_id\":\"1234\",\"user\":{\"name\":\"Patrick\",\"age\":40,\"a_number\":992,\"last_name\":\"Guillebert\",\"City\":\"Paris\"},\"created_on\":\"02/02/2015\",\"history\":{\"event\":{\"type\":\"creation\"}}}", appSession.toString());
    }

    @AfterClass
    public static void shutdown() {
        Cassandra.getSession().close();
        Cassandra.getCluster().close();
    }


}
