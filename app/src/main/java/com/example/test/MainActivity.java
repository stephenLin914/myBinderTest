package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "mytest";

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
            /**
             *  1.
             *  当Service和activity不在同一进程时（android:process=":myService"），
             *  肯定是不能将代理类BinderProxy进行强制类型转换的，
             *  正确方式应该是 .Stub.asInterface()，AIDL的方式
             *  java.lang.ClassCastException: android.os.BinderProxy cannot be cast to com.example.test.MyBinder
             */
//            MyBinder myBinder = (MyBinder) service;
//            myBinder.saySomething("how old are you?");

            /**
             *  2.
             *  AIDL的底层方式
             */
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeString("how old are you?");
            try {
                service.transact(1, data, reply, 0);

                String respondse = reply.readString();
                Log.d(TAG, "respondse = " + respondse);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection aidlServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /**
             *  3.
             *  AIDL
             *  对方法 2. 的封装
             */
            IMyAidlInterface myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                String respondse = myAidlInterface.saySomething("how old are you?");
                Log.d(TAG, "onAidlServiceConnection: resondse = " + respondse + "; Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void bindService(View view) {
        Log.d(TAG, "bindService: Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void unBindService(View view) {
        Log.d(TAG, "unbindService: Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
        unbindService(serviceConnection);
    }

    public void bindAidlService(View view) {
        Log.d(TAG, "bindService: Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
        Intent intent = new Intent(this, MyAidlService.class);
        bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE);
    }

    public void unBindAidlService(View view) {
        unbindService(aidlServiceConnection);
    }
}