package uwosh.titan_alumni_card_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Bill on 10/16/2017.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/signIn.php";
    //Include the url of where the db is being hosted
    private static String URL = "http://uwoshalumnicard.000webhostapp.com/app/signIn.php";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    public LoginActivity LoginActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment,container,false);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);

        Button mEmailSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            LoginActivity = (LoginActivity)context;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     *
     * Break this into separate functions
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        //final String email = "a@alumni.uwosh.edu";
        //final String password = "admin";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            @SuppressWarnings("ConstantConditions") RequestQueue requestQueue =
                                                    Volley.newRequestQueue(getView().getContext());

            String URLVariables = "?email=" + email;
            String login = URL.concat(URLVariables);

            //URL = URL.concat(URLVariables);

            //Toast.makeText(getContext(),"URL: " + URL,Toast.LENGTH_LONG).show();
            //Request a string response from the provided URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //check the response from the server
                            //Toast.makeText(getContext(),"TOAST 1: " + response,Toast.LENGTH_LONG).show();
                            String[] result = response.split("-");
                            if(result[0].equals("success")){
                                //login authenticated. Start the next activity
                                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                //Intent i = new Intent(getActivity().getApplicationContext(), AlumniCardBackgroundSelectActivity.class);
                                i.putExtra("USER_DATA",result[1]);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                getActivity().finish();
                            }else{
                                //login failed. prompt to re-enter credentials
                                Toast.makeText(getContext(),"Invalid email.",Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //error in sending request
                            Toast.makeText(getContext(),error.toString() + " onErrorResponse",Toast.LENGTH_LONG).show();
                        }
                    });
            //add the request to the RequestQueue
            requestQueue.add(stringRequest);
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
