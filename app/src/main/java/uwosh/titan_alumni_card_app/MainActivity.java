package uwosh.titan_alumni_card_app;

import android.app.ActionBar;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
     * This is the background, fragment_main is the forground
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Set user data received from database
        String userData = getIntent().getStringExtra("USER_DATA");
        setUserVariables(userData);
        displayUserVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
/*
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
        boolean longName = false;
        boolean longCollege = false;
        TextView[] alumniCardTextFields = getTextViews();

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
        //Get the first TextView, @+id/nameLine1
        ViewGroup.MarginLayoutParams textView = (ViewGroup.MarginLayoutParams)
                                            alumniCardTextFields[0].getLayoutParams();

        if(!longName && !longCollege){
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    28, getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                    getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            //Hide the unused TextViews
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
            alumniCardTextFields[3].setVisibility(View.INVISIBLE);
        }else if(longName){
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[1].getLayoutParams();
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18,
                    getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                    getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
        }else if(longCollege){
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[2].getLayoutParams();
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18,
                                                                getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            textView = (ViewGroup.MarginLayoutParams) alumniCardTextFields[3].getLayoutParams();
            bottomPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                                                                getResources().getDisplayMetrics());
            textView.setMargins((int)leftPadding, 0, 0, (int)bottomPadding);
            alumniCardTextFields[1].setVisibility(View.INVISIBLE);
        }
    }

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
     * Sets variables to the corresponding user data
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
        //qrCode = userVariables[];
        alumnPhoto = userVariables[7];
        backgroundImage = Integer.valueOf(userName = userVariables[0]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
