package uwosh.titan_alumni_card_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bill on 10/16/2017.
 */

public class RegisterFragment extends Fragment {
    private static final String TAG = "SignInFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);

        return view;
    }
}
