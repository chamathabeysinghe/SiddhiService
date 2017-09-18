package org.wso2.siddhiservice.output;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;
import java.util.Map;

@Extension(
        name = "notification",
        namespace = "sink",
        description = "Show android notifications",
        examples = @Example(description = "TBD",syntax = "TBD")
)

public class NotificationSink extends Sink{
    private String identifier;
    private NotificationUtils mNotificationUtils;
    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[]{Event[].class, Event.class};
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        mNotificationUtils = new NotificationUtils(SiddhiAppService.instance);
    }

    @Override
    public void publish(Object o, DynamicOptions dynamicOptions) throws ConnectionUnavailableException {
        sendNotification(o);
    }

    @Override
    public void connect() throws ConnectionUnavailableException {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

    private void sendNotification(Object event) {


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            Notification.Builder nb = mNotificationUtils.getSiddhiChannelNotification("Event",event.toString());
            mNotificationUtils.getManager().notify(101,nb.build());
        }
        else{
            Notification n  = new NotificationCompat.Builder(SiddhiAppService.instance)
                    .setContentTitle("Event")
                    .setContentText(event.toString())
                    .setSmallIcon(SiddhiAppService.getAppIcon())
                    .build();
            mNotificationUtils.getManager().notify(102, n);
        }

    }
}

class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String SIDDHI_CHANNEL_ID = "org.wso2.SIDDHI";
    public static final String SIDDHI_CHANNEL_NAME = "SIDDHI CHANNEL";

    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {

        NotificationChannel androidChannel = new NotificationChannel(SIDDHI_CHANNEL_ID,SIDDHI_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.GREEN);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(androidChannel);

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getSiddhiChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), SIDDHI_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

}
