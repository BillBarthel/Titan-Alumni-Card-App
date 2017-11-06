package uwosh.titan_alumni_card_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bill on 10/16/2017.
 */

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/register.php";
    //Include the url of where the db is being hosted
    private static String URL = "http://uwoshalumnicard.000webhostapp.com/app/register.php";

    // UI references.
    private EditText mFirstName;
    private EditText mMiddleName;
    private EditText mLastName;
    private Spinner mCollegeAttended;
    private Spinner mGraduationYear;
    private AutoCompleteTextView mEmailView;
    private EditText mMailingAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZipCode;
    private EditText mPhoneNumber;

    public LoginActivity LoginActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);

        mCollegeAttended = (Spinner) view.findViewById(R.id.college_attended);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(LoginActivity,
                R.array.colleges, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCollegeAttended.setAdapter(adapter);

        String[] lastOneHundredYears = new String[100];
        Calendar now = Calendar.getInstance();
        for(int i = 0; i < lastOneHundredYears.length; i++){
            lastOneHundredYears[i] = String.valueOf(now.get(Calendar.YEAR) - i);
        }

        mGraduationYear = (Spinner) view.findViewById(R.id.year_graduated);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> gameKindArray= new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item, lastOneHundredYears);
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGraduationYear.setAdapter(gameKindArray);

        // Set up the register form.
        // Credential checks required
        mFirstName = (AutoCompleteTextView) view.findViewById(R.id.first_name);
        mMiddleName = (AutoCompleteTextView) view.findViewById(R.id.middle_name);
        mLastName = (EditText) view.findViewById(R.id.last_name);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mMailingAddress = (AutoCompleteTextView) view.findViewById(R.id.mailing_address);
        mCity = (AutoCompleteTextView) view.findViewById(R.id.city);
        mState = (AutoCompleteTextView) view.findViewById(R.id.state);
        mZipCode = (AutoCompleteTextView) view.findViewById(R.id.zip_code);
        mPhoneNumber = (AutoCompleteTextView) view.findViewById(R.id.phone_number);

        Button mEmailSignInButton = (Button) view.findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
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
    private void attemptRegistration() {
        // Reset errors.
        mEmailView.setError(null);
        mFirstName.setError(null);
        mLastName.setError(null);

        // Store values at the time of the login attempt.
        final String firstName = mFirstName.getText().toString();
        final String middleName = mMiddleName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String collegeAttended = mCollegeAttended.getSelectedItem().toString();
        final String graduationYear = mGraduationYear.getSelectedItem().toString();
        final String email = mEmailView.getText().toString();
        final String mailingAddress = mMailingAddress.getText().toString();
        final String city = mCity.getText().toString();
        final String state = mState.getText().toString();
        final String zipCode = mZipCode.getText().toString();
        final String phoneNumber = mPhoneNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(firstName)) {
            focusView = mFirstName;
            cancel = true;
        } else if(TextUtils.isEmpty(middleName)){
            focusView = mMiddleName;
            cancel = true;
        } else if(TextUtils.isEmpty(lastName)){
            focusView = mLastName;
            cancel = true;
        } else if(TextUtils.isEmpty(email) || !isEmailValid(email)){
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


            String URLVariables = "?firstName=" + firstName + "&middleName=" + middleName +
                                  "&lastName=" + lastName +"&collegeAttended=" + collegeAttended +
                                  "&graduationYear=" + graduationYear + "&email=" + email +
                                  "&mailingAddress=" + mailingAddress + "&city=" + city +
                                  "&state=" + state + "&zipCode=" + zipCode +
                                  "&phoneNumber=" + phoneNumber;

            String login = URL.concat(URLVariables);
            //URL = URL.concat(URLVariables);
            //Request a string response from the provided URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //check the response from the server
                            String[] result = response.split("-");
                            if(result[0].equals("success")){
                                //Registration authenticated. Start the next activity
                                Intent i = new Intent(getActivity().getApplicationContext(), AlumniCardBackgroundSelectActivity.class);
                                i.putExtra("USER_DATA",result[1]);
                                startActivity(i);
                                getActivity().finish();
                            }else{
                                //login failed. prompt to re-enter credentials
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                                //Toast.makeText(getContext(),"Properly complete the required fields",Toast.LENGTH_LONG).show();
                                //mPasswordView.setError(response);
                                //mPasswordView.setError("Email is already registered");
                                //mPasswordView.requestFocus();
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
        return email.contains("@");
    }
}
