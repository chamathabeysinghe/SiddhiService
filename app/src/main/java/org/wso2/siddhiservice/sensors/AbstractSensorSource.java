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
import android.util.Log;

import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;

import java.util.Map;


public abstract class AbstractSensorSource extends Source implements SensorEventListener{

    protected String CONTEXT="context";
    protected SourceEventListener sourceEventListener;
    protected StreamDefinition streamDefinition;
    protected OptionHolder optionHolder;
    protected String context;

    protected SensorManager sensorManager;
    protected Sensor sensor;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.sourceEventListener=sourceEventListener;
        context=optionHolder.validateAndGetStaticValue(CONTEXT,
                siddhiAppContext.getName()+"/"+sourceEventListener.getStreamDefinition().getId());
        streamDefinition=StreamDefinition.id(context);
        streamDefinition.getAttributeList().addAll(sourceEventListener.getStreamDefinition().getAttributeList());
        this.optionHolder=optionHolder;
        sensorManager= (SensorManager) (SiddhiAppService.instance.getSystemService(Context.SENSOR_SERVICE));

    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{Event[].class, Event.class};
    }

    @Override
    public void connect(Source.ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        if(sensor==null){
            Log.e("Sensor Error","Android sensor is not initialized in the Source.Sensor might be not supported in the device. Stream : "+sourceEventListener.getStreamDefinition().getId());
            throw new ConnectionUnavailableException("Android sensor is not initialized in the Source.Sensor might be not supported in the device. Stream : "+sourceEventListener.getStreamDefinition().getId());
        }
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void disconnect() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void destroy() {
        sensorManager=null;
    }

    @Override
    public void pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void resume() {
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

}
