package uwosh.titan_alumni_card_app;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/signIn.php";
    //Include the url of where the db is being hosted
    private static String URL = "http://uwoshalumnicard.000webhostapp.com/app/signIn.php";
    //private static String URL = "http://uwoalumnicard.xyz/app/signIn.php";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    public LoginActivity LoginActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            LoginActivity = (LoginActivity)context;
        }
    }

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
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            String URLVariables = "?email=" + email;
            String login = URL.concat(URLVariables);
            ((LoginActivity)getActivity()).login(login, 0);
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
