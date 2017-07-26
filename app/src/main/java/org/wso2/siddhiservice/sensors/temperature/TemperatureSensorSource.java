package org.wso2.siddhiservice.sensors.temperature;


import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhiservice.sensors.AbstractSensorSource;

@Extension(
        name = "temperature",
        namespace="source",
        description = "Get events from the ambient temperature sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class TemperatureSensorSource extends AbstractSensorSource {


    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);
        try {
            androidSensor=new TemperatureSensor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
