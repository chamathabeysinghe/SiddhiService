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
        name = "proximity",
        namespace="source",
        description = "Get events from the ambient temperature sensor",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class ProximitySensorSource extends AbstractSensorSource {

    private float previousValue=-275;
    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        super.init(sourceEventListener,optionHolder,strings,configReader,siddhiAppContext);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensor==null)
            Log.e("Siddhi Source Error","Light Sensor is not supported in the device. Stream "+sourceEventListener.getStreamDefinition().getId());

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
        Log.e("Proximity","Value (@Siddhi) :  "+event.values[0]);
        this.sourceEventListener.onEvent(output,null);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
