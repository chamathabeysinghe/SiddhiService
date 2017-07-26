package org.wso2.siddhiservice.sensors.temperature;


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
import org.wso2.siddhiservice.sensors.proximity.ProximitySensor;

import java.util.Map;

@Extension(
        name = "temperature",
        namespace="source",
        description = "Get events from the ambient temperature sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class TemperatureSensorSource extends Source {

    private String CONTEXT="context";
    private SourceEventListener sourceEventListener;
    private StreamDefinition streamDefinition;
    private OptionHolder optionHolder;
    private String context;
    private TemperatureSensor temperatureSensor;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.sourceEventListener=sourceEventListener;
        context=optionHolder.validateAndGetStaticValue(CONTEXT,
                siddhiAppContext.getName()+"/"+sourceEventListener.getStreamDefinition().getId());
        streamDefinition=StreamDefinition.id(context);
        streamDefinition.getAttributeList().addAll(sourceEventListener.getStreamDefinition().getAttributeList());
        this.optionHolder=optionHolder;
        try {
            temperatureSensor=new TemperatureSensor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{Event[].class, Event.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        temperatureSensor.connectSensor(this.sourceEventListener);
    }

    @Override
    public void disconnect() {
        temperatureSensor.disconnectSensor();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void pause() {
        temperatureSensor.disconnectSensor();
    }

    @Override
    public void resume() {
        temperatureSensor.connectSensor(this.sourceEventListener);
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }
}
