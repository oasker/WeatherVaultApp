package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.SearchDBFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.UserInfoHomeFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.ViewReportsFromSingleDayFragment;
import com.example.oliverasker.skywarnmarkii.Managers.CognitoManager;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserCognitoAttributesTask;

import java.util.Map;


public class TabbedUserHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UserAttributesCallback {
    private static final String TAG= "TabbedUserHomeActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_user_home);
        Log.d(TAG, "onCreate");
        //Setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbars);
        setSupportActionBar(myToolbar);
        //mViewPager=new ViewPager(this);
//         Create the adapter that will return a fragment for each of the three
//         primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(TabbedUserHomeActivity.this);
        // showUserInfoFragment();

        //Get User details
        attributesTask.initUserPool(this);
        attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
        attributesTask.execute();

    }

    //Retreive User Attributes to be displayed on home page
    public void getUserDetails(){
        Intent i = new Intent(this, CognitoManager.class);
        i.putExtra("event", "get_attributes");
        startActivityForResult(i, Constants.GET_USER_ATTRIBUTES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult()");
        if(resultCode == RESULT_OK && requestCode==Constants.GET_USER_ATTRIBUTES) {
            Log.d(TAG, "onActivityResult() get_attributes: RESULT_OK");
//            //Wait until all data received before setting adapter
//            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//            // Set up the ViewPager with the sections adapter.
//            mViewPager = (ViewPager) findViewById(R.id.container);
//            mViewPager.setAdapter(mSectionsPagerAdapter);

            Bundle bundle = data.getExtras();
            String[]attributes=bundle.getStringArray("attributeArray");
            Log.d(TAG, "onActivityResult(); attributes=null? " + (attributes == null));

            attributes =data.getStringArrayExtra("attributeArray");
            Log.d(TAG, "onActivityResult(); attributes=null? " + (attributes == null));
            Log.d(TAG, "onACtivityResult : UserInformationModel.email: " + UserInformationModel.getInstance().getPhone());
        }

        if(resultCode == RESULT_CANCELED){
            if(data.getStringExtra("errorCode")!=null){
                Log.d(TAG, "onActivityResult() REQUEST_CANCELLED ErrorCode: " + data.getStringExtra("errorCode"));
                launchConfirmUserActivity();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_user_home_activity2, menu);
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
            Toast.makeText(this, "User Settings Functionality is Still under Development",Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.submit_report) {
            launchSubmitReportActivity();
            return true;
        }
        if(id == R.id.action_logout){
            Log.d(TAG, "Logout button in actionbar pressed");


            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG, "Logout Confirm button in actionbar pressed");
                            launchLoginActivity();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG, "Cancel Logout button in actionbar pressed");
                        }
                    });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void launchConfirmUserActivity(){
        Intent i = new Intent(this,ConfirmNewUserActivity.class);
        startActivity(i);
    }
    public void launchLoginActivity(){
        UserInformationModel.getInstance().clearAllUserInformation();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void launchSubmitReportActivity(){
        Intent i = new Intent(this, SubmitReportActivity.class);
        startActivity(i);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProcessFinished(Map<String, String> vals) {

    }

    @Override
    public void onProcessFinished(String[] vals) {

    }


    public static class PlaceholderFragment extends Fragment {

//          The fragment argument representing the section number for this
//          fragment.
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

//          Returns a new instance of this fragment for the given section
//          number.
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
//            View rootView = inflater.inflate(R.layout.fragment_tabbed_user_home_activity2, container, false);
            View rootView = inflater.inflate(R.layout.fragment_placeholder_layout, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
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
            switch (position){
                case 0:
                    Log.d(TAG, "UserInfoHomeFragment");
                    return new UserInfoHomeFragment();
                //Query Report
                case 1:
                    Log.d(TAG, "Query Report frag");
                    return new SearchDBFragment();

                //Relevant reports
                case 2:
                    Log.d(TAG, "Relevant report frag");

                    Intent i = new Intent();
                   // i.putExtra("date", "02/12/2017");
                    Bundle b = i.getExtras();

                    ViewReportsFromSingleDayFragment viewRep = new ViewReportsFromSingleDayFragment();
                    viewRep.setArguments(b);
                    return  viewRep;

//                case 3:
//                    Log.d(TAG, "Submit Report TAB");
//                    return new SubmitReportActivity();
                default:
                    Log.d(TAG, "Default placeholder fragment");
                    return new UserInfoHomeFragment();
                    //return new ReportListViewFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "User Profile";
                case 1:
                    return "Search Reports";
                case 2:
                    return "Relevent Reports";
            }
            return null;
        }
    }

}
