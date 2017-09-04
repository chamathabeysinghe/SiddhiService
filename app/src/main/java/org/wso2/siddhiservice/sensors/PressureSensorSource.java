package org.wso2.siddhiservice.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;

import java.util.HashMap;
import java.util.Map;

@Extension(
        name = "pressure",
        namespace="source",
        description = "Get events from the light sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class PressureSensorSource extends AbstractSensorSource {

    private float previousValue=-1;
    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);

        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(sensor==null)
            Log.e("Siddhi Source Error","Pressure Sensor is not supported in the device. Stream "+sourceEventListener.getStreamDefinition().getId());
    }

    @Override
    public void destroy() {
        super.destroy();
        sensor=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==previousValue)
            return;
        previousValue=event.values[0];

//        Object eventOutput[] ={event.sensor.getName(),event.timestamp,event.accuracy,event.values[0]};


        Map<String,Object> output = new HashMap<>();
        output.put("sensor",event.sensor.getName());
        output.put("timestamp",event.timestamp);
        output.put("accuracy",event.accuracy);
        output.put("value",event.values[0]);

        this.sourceEventListener.onEvent(output,null);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
