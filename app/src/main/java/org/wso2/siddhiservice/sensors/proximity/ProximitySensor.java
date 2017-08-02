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
package org.wso2.siddhiservice.sensors.proximity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import org.wso2.siddhiservice.sensors.AbstractSensor;

public class ProximitySensor extends AbstractSensor {

    private float previousValue = -1;

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
        Object eventOutput[] = {event.sensor.getName(), event.timestamp, event.accuracy, event.values[0]}; //use hashmap
        this.sourceEventListener.onEvent(eventOutput, null);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}