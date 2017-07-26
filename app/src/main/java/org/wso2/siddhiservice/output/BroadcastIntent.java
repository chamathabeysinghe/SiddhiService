package org.wso2.siddhiservice.output;

import android.content.Context;
import android.content.Intent;
import org.wso2.siddhiservice.SiddhiAppService;

public class BroadcastIntent {
    private static BroadcastIntent broadcastIntent;
    private Context context;
    private BroadcastIntent(){

    }

    public static BroadcastIntent init(){
        if(broadcastIntent==null){
            broadcastIntent=new BroadcastIntent();
            broadcastIntent.context= SiddhiAppService.instance;
        }
        return broadcastIntent;
    }

    public static BroadcastIntent getInstance() {
        if(broadcastIntent==null)
            init();
        return broadcastIntent;
    }

    public void sendIntent(Object event,String identifier){
        Intent in = new Intent(identifier);
        in.putExtra("events",event.toString());
        context.sendBroadcast(in);
    }
}
