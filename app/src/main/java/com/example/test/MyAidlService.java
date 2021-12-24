package com.example.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyAidlService extends Service {
    private static final String TAG = "mytest";

    public MyAidlService() {
    }

    public IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String saySomething(String s) throws RemoteException {
            Log.d(TAG, "MyAidlService saySomething: s = " + s + "; Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
            return tell();
        }

        @Override
        public String tell() throws RemoteException {
            return "I'm 18 years old.(from MyAidlService)";
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
