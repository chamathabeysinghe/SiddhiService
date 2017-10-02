package org.wso2.siddhiservice.sensors;

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
        name = "android-gyroscope",
        namespace = "source",
        description = "Gyroscope Source gets events from gyroscope sensor of android device. The events " +
                "are related to device’s rate of rotation. ",
        parameters = {
                @Parameter(
                        name = "polling.interval",
                        description = "polling.interval is the time between two events in milliseconds. " +
                                "If a polling interval is specified events are generated only at " +
                                "that frequency even if the sensor value changes.",
                        defaultValue = "0L",
                        optional = true,
                        type = {DataType.LONG}
                )
        },
        examples = {
                @Example(
                        syntax = "@source(type = 'android-gyroscope' ,@map(type='keyvalue'))\n" +
                                "define stream gyroscopeStream(sensor string, rotationX float, accuracy int)",
                        description = "This will consume events from Gyroscope sensor transport " +
                                "when the sensor value is changed.\n"
                ),
                @Example(
                        syntax = "@source(type = 'android-gyroscope' ,polling.interval = 100," +
                                "@map(type='keyvalue'))\n" +
                                "define stream gyroscopeStream(sensor string, rotationX float, accuracy int)",
                        description = "This will consume events from Gyroscope sensor transport " +
                                "periodically with a interval of 100 milliseconds.\n"
                )
        }
)
public class GyroscopeSensorSource extends AbstractSensorSource {

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);

        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (sensor == null) {
            Log.e("Siddhi Source Error", "Gyroscope Sensor is not supported in the device. Stream " + sourceEventListener.getStreamDefinition().getId());
            throw new SiddhiAppCreationException("Gyroscope Sensor is not supported in the device. Stream " + sourceEventListener.getStreamDefinition().getId());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Map<String, Object> output = new HashMap<>();
        output.put("sensor", event.sensor.getName());
        output.put("timestamp", event.timestamp);
        output.put("accuracy", event.accuracy);
        output.put("rotationX", event.values[0]);
        output.put("rotationY", event.values[1]);
        output.put("rotationZ", event.values[2]);

        if (this.pollingInterval == 0L && (this.latestInput == null
                || (float)this.latestInput.get("rotationX") != (float)output.get("rotationX")
                ||(float)this.latestInput.get("rotationY") != (float)output.get("rotationY")
                ||(float)this.latestInput.get("rotationZ") != (float)output.get("rotationZ"))) {
            this.sourceEventListener.onEvent(output, null);
        }
        this.latestInput = output;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
