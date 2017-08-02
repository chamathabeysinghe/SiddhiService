/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhiservice.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhiservice.SiddhiAppService;


public abstract class AbstractSensor implements SensorEventListener {

    protected Context context;
    protected SensorManager sensorManager;
    protected Sensor sensor;

    protected SourceEventListener sourceEventListener;

    public AbstractSensor() throws Exception {
        this.context= SiddhiAppService.instance;
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //get service dynamically

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
