package uwosh.titan_alumni_card_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static String id;
    private static String email;
    private static String firstName;
    private static String lastName;
    private static String userName;
    private static String collegeAttended;
    private static String graduationYear;
    //private static String qrCode;
    private static String alumnPhoto;
    private static int backgroundImage;
    private ViewStub cardBackground;

    //UPLOAD IMAGE STUFF
    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    //Include the IP of the computer XAMPP is running on
    //private static String URL = "http://192.168.0.7/AlumniCardAndroid/upload.php";
    //Include the url of where the db is being hosted
    private static String UPLOAD_URL = "http://uwoshalumnicardextra.000webhostapp.com/upload.php";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     *
     * This is the activity which the fragments are based off of
     * This is the cobcardbackground, fragment_main is the forground
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardBackground = (ViewStub) findViewById(R.id.cardBackground);

        //UPLOAD IMAGE STUFF
        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        editText = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        //Set user data received from database
        String userData = getIntent().getStringExtra("USER_DATA");
        setUserVariables(userData);
        displayUserVariables();
    }

    @Override
    protected void onStart() {
        super.onStart();
        imageView = (ImageView) findViewById(R.id.profilePic);
    }

    /*
        * This is the method responsible for image upload
        * We need the full image path and the name for the image in this method
        * */
    public void uploadMultipart() {
        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        //imageView.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
    }

    /**
     * Displays user data depending on the length of their
     * name and college graduated from
     */
    private void displayUserVariables(){

        switch (backgroundImage){
            case 1:
                cardBackground.setLayoutResource(R.layout.clashcardbackground);
                break;
            case 2:
                cardBackground.setLayoutResource(R.layout.cobcardbackground);
                break;
            case 3:
                cardBackground.setLayoutResource(R.layout.scapecardbackground);
                break;
        }
        cardBackground.inflate();

        boolean longName = false;
        boolean longCollege = false;
        TextView[] alumniCardTextFields = getTextViews();
        alumniCardTextFields[5].setText(id);

        int nameLength = firstName.length() + lastName.length();
        //display name on two lines
        if(nameLength > 15){
            longName = true;
        }

        if(collegeAttended.length() > 20){
            longCollege = true;
        }

        if(longName && longCollege){
            longNameAndCollege(alumniCardTextFields);
        }else if(longName || longCollege){
            longNameOrCollege(alumniCardTextFields, longName);
        }else{
            basicDisplay(alumniCardTextFields);
        }
        setMargins(alumniCardTextFields,longName, longCollege);
    }

    /**
     * Sets margin of each text field based on the length of the user's information
     * @param alumniCardTextFields TextViews displayed over the alumni card
     * @param longName Is the user's full name longer than 15 characters
     * @param longCollege Is the user's graduated college longer than 20 characters
     */
    private void setMargins(TextView[] alumniCardTextFields, boolean longName, boolean longCollege){
        //Convert pd to pixels. The number in applyDimension is the dp value
        float leftPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, getResources().getDisplayMetrics());

        float bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                30, getResources().getDisplayMetrics());
        if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    33, getResources().getDisplayMetrics());
        }
        //Get the first TextView, @+id/nameLine1
        ViewGroup.MarginLayoutParams textView = (ViewGroup.MarginLayoutParams)
                                            alumniCardTextFields[0].getLayoutParams();

        //Make separate helper functions. Looks like crap with the specifications for the scape background
        if(!longName && !longCollege){
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        31, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        28, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        11, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        8, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            //Hide the unused TextViews
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
            alumniCardTextFields[3].setVisibility(View.INVISIBLE);
        }else if(longName){
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[1].getLayoutParams();
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        21, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        18, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        9, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        6, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
        }else if(longCollege){
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        21, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        18, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[3].getLayoutParams();
            if(backgroundImage == 3){//scapealumnicard needs +3 to all bottom paddings
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        9, getResources().getDisplayMetrics());
            }else {
                bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        6, getResources().getDisplayMetrics());
            }
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
        }
    }

    //TODO: Replace array with private class TextView variables
    private TextView[] getTextViews(){
        TextView[] alumniCardTextFields = new TextView[7];
        alumniCardTextFields[0] = (TextView) findViewById(R.id.nameLine1);
        alumniCardTextFields[1] = (TextView) findViewById(R.id.nameLine2);
        alumniCardTextFields[2] = (TextView) findViewById(R.id.collegeLine1);
        alumniCardTextFields[3] = (TextView) findViewById(R.id.collegeLine2);
        alumniCardTextFields[4] = (TextView) findViewById(R.id.gradYear);
        alumniCardTextFields[5] = (TextView) findViewById(R.id.alumnNum);
        alumniCardTextFields[6] = (TextView) findViewById(R.id.qrCode);
        return alumniCardTextFields;
    }

    private void longNameAndCollege(TextView[] alumniCardTextFields){
        String[] college = collegeAttended.split("AND");

        alumniCardTextFields[0].setText(firstName);
        alumniCardTextFields[1].setText(lastName);
        alumniCardTextFields[2].setText(college[0]);
        alumniCardTextFields[3].setText(college[1]);
    }

    private void longNameOrCollege(TextView[] alumniCardTextFields, boolean longName){
        String[] college = collegeAttended.split("AND");

        if(longName){
            alumniCardTextFields[0].setText(firstName);
            alumniCardTextFields[1].setText(lastName);
            alumniCardTextFields[2].setText(collegeAttended);
        }else{//longCollege
            alumniCardTextFields[0].setText(firstName + " " + lastName);
            alumniCardTextFields[2].setText(college[0]);
            alumniCardTextFields[3].setText("AND" + college[1]);
        }
    }

    private void basicDisplay(TextView[] alumniCardTextFields){
        alumniCardTextFields[0].setText(firstName + " " + lastName);
        alumniCardTextFields[2].setText(collegeAttended);
    }

    /**
     * Sets variables to the corresponding user data. SHould set these as text fields
     * @param userData data pulled from database
     */
    private void setUserVariables(String userData){
        String[] userVariables = userData.split(",");
        id = userVariables[0];
        email = userVariables[1];
        userName = userVariables[2];
        firstName = userVariables[3];
        lastName = userVariables[4];
        collegeAttended = userVariables[5];
        graduationYear = userVariables[6];
        alumnPhoto = userVariables[7];
        backgroundImage = Integer.valueOf(userVariables[8]);
        //qrCode = userVariables[9];
    }

}
