package org.wso2.siddhiservice.sensors;

/*
Not tested. Not supported in the device
 */
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
        name = "rotation_vector",
        namespace="source",
        description = "Get events from the light sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class RotationVectorSensorSource extends AbstractSensorSource {

    private float previousValueX=-1;
    private float previousValueY=-1;
    private float previousValueZ=-1;
    private float previousValueScalar=-1;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);

        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(sensor==null)
            Log.e("Siddhi Source Error","Rotation Vector Sensor is not supported in the device. Stream "+sourceEventListener.getStreamDefinition().getId());
    }

    @Override
    public void destroy() {
        super.destroy();
        sensor=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==previousValueX && event.values[1]==previousValueY && event.values[2]==previousValueZ && event.values[3]==previousValueScalar)
            return;
        previousValueX=event.values[0];
        previousValueY=event.values[1];
        previousValueZ=event.values[2];
        previousValueScalar=event.values[3];

        //        Object eventOutput[] ={event.sensor.getName(),event.timestamp,event.accuracy,event.values[0],event.values[1],event.values[2]};

        Map<String,Object> output = new HashMap<>();
        output.put("sensor",event.sensor.getName());
        output.put("timestamp",event.timestamp);
        output.put("accuracy",event.accuracy);
        output.put("valueX",event.values[0]);
        output.put("valueY",event.values[1]);
        output.put("valueZ",event.values[2]);
        output.put("valueScalar",event.values[3]);

        this.sourceEventListener.onEvent(output,null);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
