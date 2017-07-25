package org.wso2.siddhiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhiservice.output.BroadcastIntentSink;
import org.wso2.siddhiservice.sensors.proximity.ProximitySensor;
import org.wso2.siddhiservice.sensors.proximity.ProximitySensorSource;

public class SiddhiAppService extends Service {
    private RequestController requestController=new RequestController();
    public static SiddhiAppService instance;
    public SiddhiAppService() {
        SiddhiAppService.instance=this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service","Start the service");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Service","Bind a client");
        return requestController;
    }


    private void runSiddhiApp(){

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.setExtension("source:proximity",ProximitySensorSource.class);
        siddhiManager.setExtension("sink:broadcast", BroadcastIntentSink.class);
        String inStreamDefinition = "" +
                "@app:name('foo')" +
                "@source(type='proximity',classname='org.wso2.ceptest.MainActivity', @map(type='passThrough'))" +
                "@sink(type='broadcast',@map(type='passThrough'))" +
                "define stream streamProximity ( sensorName string, timestamp long, accuracy int,distance float);";


        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition);

        siddhiAppRuntime.start();

    }

    private class RequestController extends IRequestController.Stub{

        @Override
        public String startSiddhiApp(String definition, String identifier) throws RemoteException {
            Log.e("App","Start Siddhi App New TWO");
            Runnable r=new Runnable() {
                @Override
                public void run() {
                    runSiddhiApp();
                }
            };

            Thread t=new Thread(r);
            t.setContextClassLoader(this.getClass().getClassLoader());
            t.start();
            return "Hello World";
        }
    }
}
