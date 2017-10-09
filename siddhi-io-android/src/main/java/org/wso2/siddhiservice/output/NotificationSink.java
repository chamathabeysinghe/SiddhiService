package org.wso2.siddhiservice.output;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;

import java.util.HashMap;
import java.util.Map;

@Extension(
        name = "android-notification",
        namespace = "sink",
        description = "This will publish events arriving to the stream through android " +
                "notifications",
        parameters = {
                @Parameter(
                        name = "multiple.notifications",
                        description = "If multipleNotifications is set as true new notification " +
                                "will be published for every new event arriving at the stream. " +
                                "Otherwise it will override the previously published " +
                                "notification if it is not cleared.",
                        optional = true,
                        type = {DataType.BOOL},
                        defaultValue = "false"
                ),
                @Parameter(
                        name = "title",
                        description = "Title of the notification message",
                        optional = true,
                        type = {DataType.STRING},
                        defaultValue = "Siddhi Platform"
                ),
                @Parameter(
                        name = "icon",
                        description = "Android id of notification icon image resource",
                        optional = true,
                        type = {DataType.INT},
                        defaultValue = "As given by Platform"
                )
        },
        examples = {
                @Example(
                        syntax = "@sink(type = 'android-notification',title = ‘Example’, " +
                                "icon = ‘R.drawable.notificationIcon’, " +
                                "multiple.notifications = ‘true’,@map(type='keyvalue'," +
                                "@payload(message = 'Value is {{value}} taken from {{sensor}}')))\n"
                                +"define stream fooStream(sensor string, value float," +
                                " accuracy float)",
                        description = "This will publish events arrive to fooStream through " +
                                "android notifications which has title 'Example'. For each " +
                                "event it will send a new notification instead of updating " +
                                "the previous one"
                )
        }
)

public class NotificationSink extends Sink{


    private NotificationManager mManager;
    public static final String SIDDHI_CHANNEL_ID = "org.wso2.extension.io.android.NOTIFICATION";
    public static final String SIDDHI_CHANNEL_NAME = "SIDDHI_NOTIFICATION_CHANNEL";

    private String MULTIPLE_NOTIFICATION_STRING = "multiple.notifications";
    private String TITLE_STRING = "title";
    private String NOTIFICATION_ICON_STRING = "icon";

    private int notificationId = 101;

    private boolean multipleNotifications = false;
    private String title = "Siddhi Platform";
    private int icon = SiddhiAppService.getAppIcon();

    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder,
                        ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        multipleNotifications=Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(
                MULTIPLE_NOTIFICATION_STRING,"false"));
        title = optionHolder.validateAndGetStaticValue(TITLE_STRING,"Siddhi Platform");
        icon = Integer.parseInt(optionHolder.validateAndGetStaticValue(NOTIFICATION_ICON_STRING,
                String.valueOf(SiddhiAppService.getAppIcon())));
        mManager = (NotificationManager) SiddhiAppService.instance.getSystemService(
                Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel androidChannel = new NotificationChannel(SIDDHI_CHANNEL_ID,
                    SIDDHI_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.GREEN);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(androidChannel);
        }
    }

    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[]{Map.class};
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    public void publish(Object o, DynamicOptions dynamicOptions)
            throws ConnectionUnavailableException {
        HashMap<String,Object> mapInput = (HashMap<String, Object>) o;

        StringBuilder sb = new StringBuilder();
        for(String key:mapInput.keySet()){
            sb.append(key).append(" : ").append(mapInput.get(key)).append("\n");
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            Notification.Builder nb = new Notification
                    .Builder(SiddhiAppService.instance, SIDDHI_CHANNEL_ID)
                    .setContentTitle(title)
                    .setSmallIcon(icon)
                    .setStyle(new Notification.BigTextStyle().bigText(sb.toString()))
                    .setAutoCancel(true);
            mManager.notify(this.notificationId,nb.build());
        }
        else{
            Notification n  = new NotificationCompat.Builder(SiddhiAppService.instance)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(sb.toString()))
                    .setSmallIcon(icon)
                    .build();
            mManager.notify(this.notificationId, n);
        }
        if(multipleNotifications){
            this.notificationId+=1;
        }
    }

    @Override
    public void connect() throws ConnectionUnavailableException {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {
        this.mManager = null;
    }

    @Override
    public Map<String, Object> currentState()
    {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

}

