package org.wso2.siddhiservice.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhiservice.SiddhiAppService;

/**
 * Created by chamath on 7/26/17.
 */

public abstract class AbstractSensor implements SensorEventListener {

    protected Context context;
    protected SensorManager sensorManager;
    protected Sensor sensor;

    protected SourceEventListener sourceEventListener;

    public AbstractSensor() throws Exception {
        this.context= SiddhiAppService.instance;
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    public void connectSensor(SourceEventListener sourceEventListener){
        this.sourceEventListener=sourceEventListener;
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void disconnectSensor(){
        sensorManager.unregisterListener(this);
        this.sourceEventListener=null;
    }



}
