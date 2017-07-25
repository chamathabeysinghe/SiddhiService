package org.wso2.siddhiservice.output;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.wso2.siddhiservice.SiddhiAppService;

/**
 * Created by chamath on 7/24/17.
 */

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
    public void sendIntent(Object event){
        Log.e("In Sink",event.toString());
        Intent in = new Intent("EVENT_DETAILS");
        in.putExtra("events",event.toString());
        context.sendBroadcast(in);
    }
}
