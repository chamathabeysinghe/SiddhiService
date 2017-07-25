package org.wso2.siddhiservice.output;

import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.util.Map;

/**
 * Created by chamath on 7/24/17.
 */

@Extension(
    name = "broadcast",
    namespace = "sink",
    description = "no description"

)
public class BroadcastIntentSink extends Sink{

    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[]{Event[].class, Event.class};
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        BroadcastIntent.init();
    }

    @Override
    public void publish(Object o, DynamicOptions dynamicOptions) throws ConnectionUnavailableException {
        BroadcastIntent.getInstance().sendIntent(o);
    }

    @Override
    public void connect() throws ConnectionUnavailableException {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }
}
