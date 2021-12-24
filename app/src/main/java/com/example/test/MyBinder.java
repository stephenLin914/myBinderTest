package com.example.test;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyBinder extends Binder {
    private static final String TAG = "mytest";

    public void saySomething(String s) {
        Log.d(TAG, "saySomething: s = " + s + ". Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        Log.d(TAG, "onTransact: Pid = " + android.os.Process.myPid() + "; thread = " + Thread.currentThread().getName());
        saySomething(data.readString());
        reply.writeString("I'm 18 years old!");
        return true;
    }
}
