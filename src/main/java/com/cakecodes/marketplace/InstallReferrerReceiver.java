package com.cakecodes.marketplace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String LOG_TAG = "CakeCodes Referrer";
        String referrer = intent.getStringExtra("referrer");

        Log.d(LOG_TAG, "Install referrer: "+ referrer);

        // Parse the referrer string
        // referrer=utm_source%3Dbitmaker&id=<accountId>&task=<taskId>
        String accountId = referrer.substring(referrer.indexOf("id=") + 3, referrer.indexOf("&task"));
        String taskId = referrer.substring(referrer.indexOf("task=") + 5);

        Log.d(LOG_TAG, "Account ID: " + accountId);
        Log.d(LOG_TAG, "Task ID: " + taskId);

        // Save the info locally
        SharedPreferences ccSharedPref = context.getSharedPreferences(CakeCodes.CAKECODES_SHARED_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ccSharedPref.edit();
        editor.putString("accountId", accountId);
        editor.putString("taskId", taskId);
        editor.apply();
    }
}
