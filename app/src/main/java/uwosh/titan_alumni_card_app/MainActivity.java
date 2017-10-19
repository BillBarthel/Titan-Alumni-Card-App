package uwosh.titan_alumni_card_app;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStub;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        //Set user data received from database
        String userData = getIntent().getStringExtra("USER_DATA");
        setUserVariables(userData);
        displayUserVariables();

/*
        //Trying to add textviews to a scrollview
        LinearLayout view = (LinearLayout) findViewById(R.id.newContentFeed);
        for (int i = 0; i < 30; i++){
            TextView t = new TextView(this);
            t.setText("hello scroll view");
            t.setId(i);
            t.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) view).addView(t);
        }
*/
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
