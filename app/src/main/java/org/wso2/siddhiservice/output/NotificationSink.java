package org.wso2.siddhiservice.output;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
import org.wso2.siddhiservice.R;


import java.util.Map;


@Extension(
        name = "notification",
        namespace = "sink",
        description = "Show android notifications",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class NotificationSink extends Sink{
    private String identifier;

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

    }

    @Override
    public void publish(Object o, DynamicOptions dynamicOptions) throws ConnectionUnavailableException {
        sendNotification();
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

    private void sendNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SiddhiAppService.instance)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        NotificationManager mNotificationManager =
                (NotificationManager) SiddhiAppService.instance.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
