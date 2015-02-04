import com.datastax.poc.AppSession;
import com.datastax.poc.dao.AppSessionDAO;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;

/**
 * Created by patrick on 04/02/15.
 */
public class JMeterSampler extends AbstractJavaSamplerClient implements Serializable {


    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {


        SampleResult result = new SampleResult();
        result.sampleStart();
        try {
            AppSession appSession = new AppSession();
            appSession.addString("attribute1", "XXXXXXXXXXX");
            AppSessionDAO.getInstance().setAppSession(appSession);
            result.sampleEnd();
            result.setSuccessful(true);
            result.setResponseMessage("Session updated.");
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage(e.toString());
        }

        return result;
    }

}
