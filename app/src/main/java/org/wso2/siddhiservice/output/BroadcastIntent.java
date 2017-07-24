package org.wso2.siddhiservice.output;

/**
 * Created by chamath on 7/24/17.
 */

public class BroadcastIntent {
    private static BroadcastIntent broadcastIntent;

    private BroadcastIntent(){

    }

    public static BroadcastIntent getBroadcastIntent(){
        if(broadcastIntent==null){
            broadcastIntent=new BroadcastIntent();
        }
        return broadcastIntent;
    }
}
