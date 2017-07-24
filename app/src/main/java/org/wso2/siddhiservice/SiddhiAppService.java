package org.wso2.siddhiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhiservice.sensors.proximity.ProximitySensor;
import org.wso2.siddhiservice.sensors.proximity.ProximitySensorSource;

public class SiddhiAppService extends Service {
    private RequestController requestController=new RequestController();

    public SiddhiAppService() {
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
        Log.e("Working","Working.............");
        try {
            ProximitySensor.getInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.setExtension("source:proximity",ProximitySensorSource.class);
        String inStreamDefinition = "" +
                "@app:name('foo')" +
                "@source(type='proximity',classname='org.wso2.ceptest.MainActivity', @map(type='passThrough'))" +
                "define stream streamProximity ( sensorName string, timestamp long, accuracy int,distance float);";

        String query = ("@info(name = 'query1') " +
                "from streamProximity " +
                "select *  " +
                "insert into outputStream;");

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition +
                query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    Log.e("Event changed source :",event.toString());
                    Intent in = new Intent("EVENT_DETAILS");
                    in.putExtra("events",event.toString());
                    sendBroadcast(in);
                }
            }
        });

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
