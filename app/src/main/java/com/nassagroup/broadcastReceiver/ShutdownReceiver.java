package com.nassagroup.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nassagroup.tools.UserInfo;


/**
 * Created by hollyn.derisse on 21/05/2018.
 */

public class ShutdownReceiver extends BroadcastReceiver {
    public ShutdownReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();

        Log.i("power_off", "it's power off");
        Log.i("Main", "Logout");
        UserInfo userInfo = new UserInfo(context);
        userInfo.setLoggedin(false);
        userInfo.clear();

//        //redirect user to login screen
//        Intent i = new Intent(this.context, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(i);
//        ((Activity)context).finish();
    }

}
