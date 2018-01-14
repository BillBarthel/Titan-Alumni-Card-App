package uwosh.titan_alumni_card_app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlumniCardBackgroundSelectActivity extends AppCompatActivity {
    private RadioGroup cardBackgrounds;
    private RadioButton selectedBackground;
    private Button getSelectedBackground;
    private ArrayList<String> userData;
    private static String email;
    private int alumniCardBackground;
    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/setbackground.php";
    //Include the url of where the db is being hosted
    private static String URL = "http://uwoshalumnicard.000webhostapp.com/app/setbackground.php";
    //private static String URL = "http://uwoalumnicard.xyz/app/setbackground.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_card_background_select);

        userData = getIntent().getStringArrayListExtra("USER_DATA");
        email = userData.get(1);
        addListenerOnButton();

        ImageView a = new ImageView(this);
        ImageView b = new ImageView(this);
        ImageView c = new ImageView(this);

        a.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.a, 100, 100));
        b.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.b, 100, 100));
        c.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.c, 100, 100));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void addListenerOnButton() {

        cardBackgrounds = (RadioGroup) findViewById(R.id.radioSex);
        getSelectedBackground = (Button) findViewById(R.id.done);

        getSelectedBackground.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = cardBackgrounds.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                selectedBackground = (RadioButton) findViewById(selectedId);

                switch (selectedId){
                    case R.id.clash:
                        alumniCardBackground = 1;
                        break;
                    case R.id.cob:
                        alumniCardBackground = 2;
                        break;
                    case R.id.scape:
                        alumniCardBackground = 3;
                        break;
                }

                sendVolleyRequest();
            }
        });
    }

    private void sendVolleyRequest(){
        @SuppressWarnings("ConstantConditions") RequestQueue requestQueue =
                Volley.newRequestQueue(this.getApplicationContext());

        String URLVariables = "?background=" + alumniCardBackground + "&email=" + email;
        URL = URL.concat(URLVariables);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //check the response from the server
                        if(!response.equals("success")){
                            //Database wasn't updated
                            Toast.makeText(getApplicationContext(),"Your selection could not be saved at this time.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error in sending request
                        Toast.makeText(getApplicationContext(),error.toString() + " onErrorResponse",Toast.LENGTH_LONG).show();
                    }
                }){
            //add parameters to the request
            @Override
            protected Map<String,String> getParams(){
                //Map<String,String> params = new HashMap<>();
                return null;
            }
        };
        //add the request to the RequestQueue
        requestQueue.add(stringRequest);
        //Start the main activity with the user's selected background
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        userData.add(Integer.toString(alumniCardBackground));
        i.putExtra("USER_DATA",userData);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        //Prevents calling finish() when pressing the Back button
        moveTaskToBack(true);
    }
}
