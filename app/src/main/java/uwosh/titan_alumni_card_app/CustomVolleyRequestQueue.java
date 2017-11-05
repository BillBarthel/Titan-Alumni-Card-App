package uwosh.titan_alumni_card_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.Policy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bill on 11/4/2017.
 */

public class CustomVolleyRequestQueue {
    //Define these in resources/strings
    private static final String SIGN_IN = "signIn.php?";
    private static final String REGISTER = "register.php?";
    private static final String SET_BACKGROUND = "setbackground.php?";

    //private static CustomVolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private String parameters;
    private String type;

    private CustomVolleyRequestQueue(Context context, String parameters, String type) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        parameters = parameters;
        type = type;
    }

    public void execute(){

        String URL = concatURL();

        //Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        anilizeResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error in sending request
                        Toast.makeText(mCtx,error.toString() + " onErrorResponse",Toast.LENGTH_LONG).show();
                    }
                }){
            //add parameters to the request
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                //params.put(EMAIL,email);
                //params.put(PASSWORD, password);
                return params;
            }
        };
        //add the request to the RequestQueue
        mRequestQueue.add(stringRequest);
    }

    public String concatURL(){
        if(type.equals("signin")){
            return SIGN_IN.concat(parameters);
        }else if (type.equals("register")){
            return REGISTER.concat(parameters);
        }else if (type.equals("setbackground")){
            return SET_BACKGROUND.concat(parameters);
        }else{
            Log.d("VOLLEY", "INVALID PARAMETER 'type' FOR CustomVolleyRequest");
        }
        return "error";
    }

    private void anilizeResponse(String response){

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}
