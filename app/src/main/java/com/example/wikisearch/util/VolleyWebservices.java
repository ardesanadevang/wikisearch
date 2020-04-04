package com.example.wikisearch.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devang on 05/03/2017.
 */

public class VolleyWebservices {

    ProgressDialog dialog;
    ConnectionDetector cd;

    public void callWSmethodGET(final Activity activity, String url, final Callback callback, final String TAG) {
        Util.putInLog("refund_url :- " + url);

        cd = new ConnectionDetector(activity);
        if (!cd.isConnectingToInternet()) {
            callback.onFailure("Please check internet connection and try again", TAG);
            return;
        }

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        // Add the request to the queue
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Util.putInLog(TAG + "_responce:->" + response);
                callback.onSuccess(response, TAG);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                callback.onFailure("", TAG);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(activity).add(stringRequest);
    }

    public interface Callback {
        public void onSuccess(String responce, String TAG);

        public void onFailure(String message, String TAG);
    }
}
