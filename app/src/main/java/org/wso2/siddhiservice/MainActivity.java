/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhiservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private IRequestController requestController;

    private ServiceConnection serviceCon=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            requestController =IRequestController.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnBind(View view){
        Log.e("Bind","Bind Clicked");
        Intent intent=new Intent(this.getBaseContext(),SiddhiAppService.class);
        startService(intent);
//        Intent intent=new Intent("org.wso2.siddhiappservice.AIDL");
////        this.startService(convertIntent(intent));
//        bindService(convertIntent(intent),serviceCon,BIND_AUTO_CREATE);
    }

    public void btnCallService(View view){
        try {
            requestController.startSiddhiApp("1000","1000");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /*
    private Intent convertIntent(Intent implicitIntent){
        PackageManager pm=getPackageManager();
        List<ResolveInfo> resolveInfoList=pm.queryIntentServices(implicitIntent,0);

        if(resolveInfoList==null){
            return null;
        }
        ResolveInfo serviceInfo=resolveInfoList.get(0);
        ComponentName component=new ComponentName(serviceInfo.serviceInfo.packageName,serviceInfo.serviceInfo.name);
        Intent explicitIntent=new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
    */
}
