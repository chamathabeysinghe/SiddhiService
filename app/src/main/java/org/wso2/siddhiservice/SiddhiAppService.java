package org.wso2.siddhiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class SiddhiAppService extends Service {
    private RequestController requestController=new RequestController();
    public static SiddhiAppService instance;
    private AppManager appManager;
    public SiddhiAppService() {
        SiddhiAppService.instance=this;
        appManager=new AppManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return requestController;
    }


    private class RequestController extends IRequestController.Stub{

        @Override
        public String startSiddhiApp(String definition, String identifier) throws RemoteException {
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
