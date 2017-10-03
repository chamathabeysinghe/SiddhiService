package org.wso2.siddhiservice.sensors;
/*
Not tested, device is not supporting
 */
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;

import java.util.HashMap;
import java.util.Map;

@Extension(
        name = "steps",
        namespace="source",
        description = "Proximity Source gets events from step detector sensor of android device which is a software based sensor.",
        parameters = {
                @Parameter(
                        name = "polling.interval",
                        description = " polling.interval is the time between two events in milliseconds. " +
                                "If a polling interval is specified events are generated only at " +
                                "that frequency even if the sensor value changes. If this is not specified 1 will be given as event for each step taken. Otherwise count of steps taken in this interval",
                        defaultValue = "0L",
                        optional = true,
                        type = {DataType.LONG}
                )
        },
        examples = {
                @Example(
                        syntax = "@source(type = 'android-steps' ,polling.interval = 100 ,@map(type='keyvalue'))\n" +
                                "define stream fooStream(sensor string, steps int, accuracy float)",
                        description = "This will consume events from Step Detector sensor transport " +
                                "It will insert number of steps taken within the time frame of polling interval.\n"
                ),
                @Example(
                        syntax = "@source(type = 'android-steps' ,@map(type='keyvalue'))\n" +
                                "define stream fooStream(sensor string, steps int, accuracy float)",
                        description = "This will consume events from Proximity sensor transport " +
                                "When ever user took a step it will input an event into the stream which has steps count as 1\n"
                )
        }
)
public class StepCounterSensoorSource extends AbstractSensorSource {
    private int stepCount = 0;
    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensor == null) {
            Log.e("Siddhi Source Error", "Proximity Sensor is not supported in the device. Stream " + sourceEventListener.getStreamDefinition().getId());
            throw new SiddhiAppCreationException("Proximity Sensor is not supported in the device. Stream " + sourceEventListener.getStreamDefinition().getId());
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepCount++;
        Map<String, Object> output = new HashMap<>();
        output.put("sensor", event.sensor.getName());
        output.put("timestamp", event.timestamp);
        output.put("accuracy", event.accuracy);
        output.put("steps", stepCount);

        if (this.pollingInterval == 0L) {
            this.sourceEventListener.onEvent(output, null);
            stepCount=0;
        }
        this.latestInput = output;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void postUpdates() {
        if (latestInput == null) {
            Log.d("Sensor Source", "No  sensor input at the moment.Polling chance is missed. ");
            return;
        }
        this.sourceEventListener.onEvent(this.latestInput, null);
        this.stepCount = 0;
    }
}
