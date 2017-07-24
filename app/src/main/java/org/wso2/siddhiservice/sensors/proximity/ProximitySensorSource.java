package org.wso2.siddhiservice.sensors.proximity;


import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import java.util.Map;

@Extension(
        name = "proximity",
        namespace="source",
        description = "Get events from the proximity sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class ProximitySensorSource extends Source {

    private String CONTEXT="context";
    private SourceEventListener sourceEventListener;
    private StreamDefinition streamDefinition;
    private OptionHolder optionHolder;
    private String context;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.sourceEventListener=sourceEventListener;
        context=optionHolder.validateAndGetStaticValue(CONTEXT,
                siddhiAppContext.getName()+"/"+sourceEventListener.getStreamDefinition().getId());
        streamDefinition=StreamDefinition.id(context);
        streamDefinition.getAttributeList().addAll(sourceEventListener.getStreamDefinition().getAttributeList());
        this.optionHolder=optionHolder;
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{Event[].class, Event.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {

        ProximitySensor.getInstance().connectSensor(this.sourceEventListener);
    }

    @Override
    public void disconnect() {
        ProximitySensor.getInstance().disconnectSensor();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void pause() {
        ProximitySensor.getInstance().disconnectSensor();
    }

    @Override
    public void resume() {
        ProximitySensor.getInstance().connectSensor(this.sourceEventListener);
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }
}
