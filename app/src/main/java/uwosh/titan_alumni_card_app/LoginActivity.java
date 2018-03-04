package uwosh.titan_alumni_card_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 * The login and register screen are fragments resting on top of the ViewPager.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private SharedPreferences sp;
    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/signIn.php";
    //Include the url of where the db is being hosted
    private static String URL = "http://uwoshalumnicard.000webhostapp.com/app/signIn.php";
    //private static String URL = "http://uwoalumnicard.xyz/app/signIn.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("login",MODE_PRIVATE);

        //sp.edit().putBoolean("loggedIn", false).apply();//Keep the app from auto logging in
        //if it's true the user has already logged in
        if(sp.getBoolean("loggedIn", false)){//example says make b=false
            //start activity. Login with stored email
            String storedEmail = sp.getString("email","");//Will return last logged in email
            String URLVariables = "?email=" + storedEmail;
            String login = URL.concat(URLVariables);
            login(login, 0);
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        //Set up the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.login_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Logs in the user
     * @param login URL string to log in or register the user
     * @param type 0 if the user is logging in, 1 if registering
     */
    public void login(String login, final int type){
        @SuppressWarnings("ConstantConditions") RequestQueue requestQueue =
                Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(getContext(),"SUCCESS: " + obj,Toast.LENGTH_LONG).show();
                            ArrayList<String> userData = parseJSONObject(obj);
                            Intent i;
                            if(type == 0){
                                i = new Intent(getApplicationContext(), MainActivity.class);
                            }else{
                                i = new Intent(getApplicationContext(), AlumniCardBackgroundSelectActivity.class);
                            }
                            i.putExtra("USER_DATA",userData);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            sp.edit().putBoolean("loggedIn", true).apply();
                            sp.edit().putString("email", userData.get(1)).apply();
                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"EXCEPTION",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error in sending request
                        Toast.makeText(getApplicationContext(),"Unable to connect to server. Try again later.",Toast.LENGTH_LONG).show();
                    }
                });
        //add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    private ArrayList<String> parseJSONObject(JSONObject obj){
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            arrayList.add(obj.getString("paddedId"));
            arrayList.add(obj.getString("email"));
            arrayList.add(obj.getString("username"));
            arrayList.add(obj.getString("firstname"));
            arrayList.add(obj.getString("lastname"));
            arrayList.add(obj.getString("collegeattended"));
            arrayList.add(obj.getString("graduationyear"));
            arrayList.add(obj.getString("alumnphoto"));
            arrayList.add(obj.getString("background"));
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Error parsing JSON",Toast.LENGTH_LONG).show();
        }

        return arrayList;
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "SIGN IN");
        adapter.addFragment(new RegisterFragment(), "FIRST TIME SIGN IN");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //Prevents calling finish() when pressing the Back button
        moveTaskToBack(true);
    }
}

