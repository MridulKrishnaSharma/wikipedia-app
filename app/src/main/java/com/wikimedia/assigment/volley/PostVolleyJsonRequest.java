package com.wikimedia.assigment.volley;

import android.app.Activity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PostVolleyJsonRequest {

    private String type;
    private Activity act;
    private IVolleyJsonRespondsListener volleyJsonRespondsListener;
    private String networkurl;
    private JSONObject jsonObject = null;
    private JSONObject params;


    public PostVolleyJsonRequest(Activity act, IVolleyJsonRespondsListener volleyJsonRespondsListener, String type, String netnetworkUrl, JSONObject params) {
        this.act = act;
        this.volleyJsonRespondsListener = volleyJsonRespondsListener;
        this.type = type;
        this.networkurl = netnetworkUrl;
        this.params = params;
        sendRequest();
    }

    private void sendRequest() {

        Log.d("url", "url" + networkurl);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, networkurl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "response " + response);
                        volleyJsonRespondsListener.onSuccessJson(response, type);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            NetworkResponse response = error.networkResponse;
                            Log.e("response", "response " + response);
                            if (response != null) {
                                int code = response.statusCode;

                                String errorMsg = new String(response.data);
                                Log.e("response", "response" + errorMsg);
                                try {
                                    jsonObject = new JSONObject(errorMsg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String msg = jsonObject.optString("message");
                                volleyJsonRespondsListener.onFailureJson(code, msg);
                            } else {
                                String errorMsg = error.getMessage();
                                volleyJsonRespondsListener.onFailureJson(0, errorMsg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestqueue = Volley.newRequestQueue(act);
        requestqueue.add(jsObjRequest);


    }
}
/*
call from main
try {
        //parameters
        //Context,Interface,Type(to indentify your responds),URL,parameter for your request

        //request 1
        new PostVolleyJsonRequest(TestVolley.this, TestVolley.this, "Submit", url, params);

        //request 2
        new PostVolleyJsonRequest(TestVolley.this, TestVolley.this, "AccessData", url_2, params_2);




 } catch (Exception e) {

 e.printStackTrace()
 }

 //Methods from Interface

  @Override
public void onSuccessJson(JSONObject result, String type) {

   //Based on the Type you send get the responds and parse it
    switch (type) {
        case "Submit":
            try {
                parseSubmit(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;

        case "AccessData":
            try {
                parseAccessData(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
           break;
    }
Share
Improve this answer
Follow
edited Jul 20 '17 at 12:03
*/