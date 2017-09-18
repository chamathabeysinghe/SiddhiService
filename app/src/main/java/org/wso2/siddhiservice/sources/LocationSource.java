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
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;

import java.util.HashMap;
import java.util.Map;

@Extension(
        name = "location",
        namespace="source",
        description = "Get events from a intent broadcast",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class LocationSource extends Source implements LocationListener{

    private String CONTEXT="context";
    private SourceEventListener sourceEventListener;
    private StreamDefinition streamDefinition;
    private OptionHolder optionHolder;
    private String context;

    private LocationManager locationManager;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder, String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {

        this.sourceEventListener=sourceEventListener;
        context=optionHolder.validateAndGetStaticValue(CONTEXT,
                siddhiAppContext.getName()+"/"+sourceEventListener.getStreamDefinition().getId());
        streamDefinition=StreamDefinition.id(context);
        streamDefinition.getAttributeList().addAll(sourceEventListener.getStreamDefinition().getAttributeList());
        this.optionHolder=optionHolder;

        locationManager = (LocationManager) SiddhiAppService.instance.getSystemService(Context.LOCATION_SERVICE);

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

    }

    @Override
    public void disconnect() {
        locationManager.removeUpdates(this);

    }

    @Override
    public void destroy() {

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
        output.put("accuracy",location.getAccuracy());
        sourceEventListener.onEvent(output,null);
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
}
