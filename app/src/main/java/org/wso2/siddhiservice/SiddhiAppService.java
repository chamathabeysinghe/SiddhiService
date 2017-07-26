package org.wso2.siddhiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SiddhiAppService extends Service {
    private RequestController requestController=new RequestController();
    public static SiddhiAppService instance;
    private AppManager appManager;
    int count =0;
    public SiddhiAppService() {
        SiddhiAppService.instance=this;
        appManager=new AppManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SiddhiService","Starting");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return requestController;
    }


    private class RequestController extends IRequestController.Stub{

        @Override
        public String startSiddhiApp(String definition, String identifier) throws RemoteException {
            count+=1;
            Log.e("SiddhiService","Binding  "+count);
            Log.e("Siddhi","Creating the siddhi app");
            Runnable r= () -> {
                appManager.startApp(definition,identifier);
            };
            Thread t=new Thread(r);
            t.setContextClassLoader(this.getClass().getClassLoader());
            t.start();
            return "App Launched";
        }
    }
}
