package org.wso2.siddhiservice.sensors;

import android.hardware.Sensor;

import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.util.Map;

/**
 * Created by chamath on 7/26/17.
 */

public abstract class AbstractSensorSource extends Source{

    protected String CONTEXT="context";
    protected SourceEventListener sourceEventListener;
    protected StreamDefinition streamDefinition;
    protected OptionHolder optionHolder;
    protected String context;
    protected AbstractSensor androidSensor;

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
    public void connect(Source.ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        androidSensor.connectSensor(this.sourceEventListener);
    }

    @Override
    public void disconnect() {
        androidSensor.disconnectSensor();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void pause() {
        androidSensor.disconnectSensor();
    }

    @Override
    public void resume() {
        androidSensor.connectSensor(this.sourceEventListener);
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

}
