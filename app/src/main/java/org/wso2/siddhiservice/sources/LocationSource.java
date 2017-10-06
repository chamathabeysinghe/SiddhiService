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
package org.wso2.siddhiservice.sources;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Extension(
        name = "android-location",
        namespace = "source",
        description = "Location Source gets events from location sensor of android device. ",
        parameters = {
                @Parameter(
                        name = "polling.interval",
                        description = " polling.interval is the time between two events in milliseconds. " +
                                "If a polling interval is specified events are generated only at " +
                                "that frequency even if the sensor value changes.",
                        defaultValue = "0L",
                        optional = true,
                        type = {DataType.LONG}
                )
        },
        examples = {
                @Example(
                        syntax = "@source(type = 'android-location' ,@map(type='keyvalue'))\n" +
                                "define stream locationStream(sensor string, location float, accuracy int)",
                        description = "This will consume events from Location sensor transport " +
                                "when the sensor value is changed.\n"
                ),
                @Example(
                        syntax = "@source(type = 'android-location' ,polling.interval = 100," +
                                "@map(type='keyvalue'))\n" +
                                "define stream locationStream(sensor string, location float, accuracy int)",
                        description = "This will consume events from Location sensor transport " +
                                "periodically with a interval of 100 milliseconds.\n"
                )
        }
)
public class LocationSource extends Source implements LocationListener{

    private SourceEventListener sourceEventListener;
    protected Long pollingInterval = 0L;
    protected Timer timer;
    protected TimerTask timerTask;
    private LocationManager locationManager;
    protected Map<String, Object> latestInput;
    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {

        this.sourceEventListener=sourceEventListener;
        this.locationManager = (LocationManager) SiddhiAppService.instance.getSystemService(Context.LOCATION_SERVICE);
        this.pollingInterval = Long.valueOf(optionHolder.validateAndGetStaticValue("polling.interval", "0"));

        if (this.pollingInterval != 0) {
            this.timer = new Timer();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    postUpdates();
                }
            };
        }
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[0];
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        if (ActivityCompat.checkSelfPermission(SiddhiAppService.instance, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SiddhiAppService.instance, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission","Requires Permission");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, this);
        if (pollingInterval != 0) {
            this.timer.schedule(timerTask, 0, pollingInterval);
        }
    }

    @Override
    public void disconnect() {
        locationManager.removeUpdates(this);
        timer.cancel();
    }

    @Override
    public void destroy() {
        locationManager = null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

    @Override
    public void onLocationChanged(Location location) {
        HashMap<String,Object> output=new HashMap<>();
        output.put("latitude",location.getLatitude());
        output.put("longitude",location.getLongitude());
        output.put("altitude",location.getAltitude());
        output.put("bearing",location.getBearing());
        output.put("speed",location.getSpeed());
        output.put("accuracy",location.getAccuracy());

        if (this.pollingInterval == 0L && (output == latestInput)) {
            this.sourceEventListener.onEvent(output, null);
        }
        this.latestInput = output;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void postUpdates() {
        if (latestInput == null) {
            Log.d("Sensor Source", "No  sensor input at the moment.Polling chance is missed. ");
            return;
        }
        this.sourceEventListener.onEvent(this.latestInput, null);
    }
}
