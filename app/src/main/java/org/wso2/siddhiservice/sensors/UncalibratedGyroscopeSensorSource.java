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

@Extension(
        name = "uncalibrated_gyroscope",
        namespace="source",
        description = "Get events from the light sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class UncalibratedGyroscopeSensorSource extends AbstractSensorSource {

    private float previousValueX=-1;
    private float previousValueY=-1;
    private float previousValueZ=-1;
    private float previousDriftX=-1;
    private float previousDriftY=-1;
    private float previousDriftZ=-1;


    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);

        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        if(sensor==null)
            Log.e("Siddhi Source Error","Uncalibrated Gyroscope Sensor is not supported in the device. Stream "+sourceEventListener.getStreamDefinition().getId());
    }

    @Override
    public void destroy() {
        super.destroy();
        sensor=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==previousValueX && event.values[1]==previousValueY && event.values[2]==previousValueZ
                && event.values[3]==previousDriftX && event.values[4]==previousDriftY && event.values[5]==previousDriftZ)
            return;
        previousValueX=event.values[0];
        previousValueY=event.values[1];
        previousValueZ=event.values[2];
        previousDriftX=event.values[3];
        previousDriftY=event.values[4];
        previousDriftZ=event.values[5];

        Object eventOutput[] ={event.sensor.getName(),event.timestamp,event.accuracy,event.values[0],event.values[1],event.values[2],
        event.values[3],event.values[4],event.values[5]};


        this.sourceEventListener.onEvent(eventOutput,null);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
