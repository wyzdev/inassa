package com.nassagroup.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nassagroup.activities.LoginActivity;
import com.nassagroup.activities.UserGuide;

import java.util.TimerTask;

/**
 * Created by hollyn.derisse on 31/10/2017.
 */

public class LogOutTimerTask extends TimerTask {

    Context context;

    public LogOutTimerTask(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        Log.i("Main", "Logout");
        UserInfo userInfo = new  UserInfo(context);
        userInfo.setLoggedin(false);
        userInfo.clear();

        //redirect user to login screen
        Intent i = new Intent(this.context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((Activity)context).finish();
    }
}