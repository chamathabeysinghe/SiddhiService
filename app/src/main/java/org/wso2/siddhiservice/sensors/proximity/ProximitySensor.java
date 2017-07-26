package org.wso2.siddhiservice.sensors.proximity;


/**
 * Created by chamath on 7/21/17.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhiservice.SiddhiAppService;

public class ProximitySensor implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;

    private SourceEventListener sourceEventListener;

    private float previousValue=-1;
    public ProximitySensor() throws Exception {
        this.context=SiddhiAppService.instance;
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensor==null)
            throw new Exception("Proximity Device is not supported in the device");
    }


    public void connectSensor(SourceEventListener sourceEventListener){
        this.sourceEventListener=sourceEventListener;

        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void disconnectSensor(){
        sensorManager.unregisterListener(this);
        this.sourceEventListener=null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==previousValue)
            return;
        previousValue=event.values[0];
        Object eventOutput[] ={event.sensor.getName(),event.timestamp,event.accuracy,event.values[0]};
        this.sourceEventListener.onEvent(eventOutput,null);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
