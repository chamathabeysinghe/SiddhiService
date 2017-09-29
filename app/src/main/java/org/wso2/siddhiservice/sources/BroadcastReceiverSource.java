package org.wso2.siddhiservice.sources;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhiandroidlibrary.SiddhiAppService;

import java.util.HashMap;
import java.util.Map;

@Extension(
        name = "broadcast-receiver",
        namespace="source",
        description = "Get events from a intent broadcast",
        examples = @Example(description = "TBD",syntax = "TBD")
)
public class BroadcastReceiverSource extends Source {

    private String CONTEXT="context";
    private SourceEventListener sourceEventListener;
    private StreamDefinition streamDefinition;
    private OptionHolder optionHolder;
    private String context;

    private static final String BROADCAST_FILTER_IDENTIFIER="identifier";
    private String intentIdentifier;
    private DataUpdateReceiver dataUpdateReceiver;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder,
                     String[] strings, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
        this.sourceEventListener=sourceEventListener;
        context=optionHolder.validateAndGetStaticValue(CONTEXT,
                siddhiAppContext.getName()+"/"+sourceEventListener.getStreamDefinition().getId());
        streamDefinition=StreamDefinition.id(context);
        streamDefinition.getAttributeList().addAll(sourceEventListener.getStreamDefinition().getAttributeList());
        this.optionHolder=optionHolder;
        intentIdentifier=this.optionHolder.validateAndGetStaticValue(BROADCAST_FILTER_IDENTIFIER);
        dataUpdateReceiver=new DataUpdateReceiver();
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[0];
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(intentIdentifier);
        SiddhiAppService.instance.registerReceiver(dataUpdateReceiver,intentFilter);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //keys
            //state
            if(intent.getAction().equals(intentIdentifier)){
                Bundle bundle=intent.getExtras();
                Map<String,Object> results=new HashMap<>();
                if(bundle!=null){
                    for(String key: bundle.keySet()){
                        Object value=bundle.get(key);
                        results.put(key,value);
                    }
                }
                sourceEventListener.onEvent(results,null);
            }
        }
    }
}
