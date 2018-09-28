package com.enigmarobolox.arm_avi.enigmarobolox;

import android.os.AsyncTask;

/**
 * Created by Arm_AVI on 3/22/2018.
 */

public class SelectTask extends AsyncTask<Void, Void, String> {
    private String mUrl;

    public SelectTask(String url){
        super();
        mUrl = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        String jsonString = JsonHttp.makeHttpRequest(mUrl);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
