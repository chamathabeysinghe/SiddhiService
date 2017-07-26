package org.wso2.siddhiservice.sensors.proximity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import org.wso2.siddhiservice.sensors.AbstractSensor;

public class ProximitySensor extends AbstractSensor {

    protected float previousValue = -1;

    public ProximitySensor() throws Exception {
        super();
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensor == null)
            throw new Exception("Proximity Sensor is not supported in the device");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == previousValue)
            return;
        previousValue = event.values[0];
        Object eventOutput[] = {event.sensor.getName(), event.timestamp, event.accuracy, event.values[0]};
        this.sourceEventListener.onEvent(eventOutput, null);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}