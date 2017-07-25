package org.wso2.siddhiservice;

import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhiservice.output.BroadcastIntentSink;
import org.wso2.siddhiservice.sensors.proximity.ProximitySensorSource;

import java.util.HashMap;

/**
 * Created by chamath on 7/25/17.
 */


public class AppManager {

    private SiddhiManager siddhiManager;
    private HashMap<String,SiddhiAppRuntime> siddhiAppList;

    public AppManager(){
        siddhiAppList=new HashMap<>();
        this.siddhiManager=new SiddhiManager();
        siddhiManager.setExtension("source:proximity",ProximitySensorSource.class);
        siddhiManager.setExtension("sink:broadcast", BroadcastIntentSink.class);
    }

    public void startApp(String inStream,String identifier){
        SiddhiAppRuntime siddhiAppRuntime=siddhiManager.createSiddhiAppRuntime(inStream);
        siddhiAppList.put(identifier,siddhiAppRuntime);
        siddhiAppRuntime.start();
    }
}
