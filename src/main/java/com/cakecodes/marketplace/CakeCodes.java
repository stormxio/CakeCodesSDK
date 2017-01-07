package com.cakecodes.marketplace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Calvin on 1/7/2017.
 */

public class CakeCodes {
    protected static final String CAKECODES_SHARED_PREF = "com.cakecodes.marketplace";

    private static SharedPreferences ccSharedPref;

    public static void init(Context c) {
        ccSharedPref = c.getSharedPreferences(CAKECODES_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public static void sendPostback(final String signature, int goalId) {
        new AsyncTask<Void, Void, String>() {
            private static final String LOG_TAG = "Send Postback";

            @Override
            protected void onPreExecute() {}

            protected String doInBackground(Void... voids) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String url = "https://api.cakecodes.com/marketplace/task?id=" + getAccountId() +
                            "&task=" + getTaskId() + "&signature=" + signature;
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "BG error: " + e.toString());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    Log.d(LOG_TAG, "Response: " + result);
                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "Error: " + e.toString());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static String generateSignature(String privateKey) {
        return md5(getAccountId() + getTaskId() + privateKey);
    }

    private static String getAccountId() {
        return ccSharedPref.getString("accountId", "");
    }

    private static String getTaskId() {
        return ccSharedPref.getString("taskId", "");
    }

    private static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
