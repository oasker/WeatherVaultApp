package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Callbacks.GetReportsRatedByUserTaskCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments.ChatMessageParent;
import com.example.oliverasker.skywarnmarkii.Fragments.QueryReportsAttributesFragments.QueryReportAttributesFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.UserHomeFragments.UserInfoHomeFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.ViewReportsFromSingleDayFragment;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetReportsRatedByUserTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserCognitoAttributesTask;

import java.util.Map;


public class TabbedUserHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UserAttributesCallback, GetReportsRatedByUserTaskCallback {
    private static final String TAG= "TabbedUserHomeActivity";
    private Fragment mostRecentSelectedFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout topTabLayout;
    private TabLayout bottomTabLayout;
    private ImageButton newReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tabbed_user_home);
        setContentView(R.layout.activity_user_home_test);

        Log.d(TAG, "onCreate");

//      Setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbars);
        setSupportActionBar(myToolbar);


//      Create the adapter that will manage the fragments for each of the
//      primary sections of the user home
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

//      Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);


//      Setup top and bottom tab layout
        bottomTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        bottomTabLayout.setupWithViewPager(mViewPager);
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 3:
                        launchMultipleOrSingleReportDialog();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 3:
//                        launchMultipleOrSingleReportDialog();
                        break;
                }
            }
        });


//        bottomTabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        bottomTabLayout.setupWithViewPager(mViewPager);
//        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch (tab.getPosition()) {
//                    case 3:
//                        launchMultipleOrSingleReportDialog();
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                switch (tab.getPosition()) {
//                    case 3:
////                        launchMultipleOrSingleReportDialog();
//                        break;
//                }
//            }
//        });


//        newReportButton = (ImageButton) findViewById(R.id.open_write_new_report_button);
//        newReportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////              launchMultipleOrSingleReportDialog();
//            }
//        });



        //Use this to launch
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.submit_reports_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        Get User attribtes task

//      Start task that retreives the users attributes from Cognito
        GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(TabbedUserHomeActivity.this);
        attributesTask.initUserPool(this);
        attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
        attributesTask.execute();

//      Start task that retreives text file from S3 that holds all the reports
//      The user has rated.
        GetReportsRatedByUserTask getRatedReportsTask = new GetReportsRatedByUserTask();
        getRatedReportsTask.setmContext(this);
        getRatedReportsTask.setFilePath(getCacheDir() + "/");
        getRatedReportsTask.setUsername(UserInformationModel.getInstance().getUsername());
        getRatedReportsTask.setCallback(this);
        getRatedReportsTask.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult()");
        if(resultCode == RESULT_OK && requestCode==Constants.GET_USER_ATTRIBUTES) {
            Log.d(TAG, "onActivityResult() get_attributes: RESULT_OK");

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


    //  Menu Setup Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        if(menu.equals(R.menu.tabbed_user_home_activity_single_or_multiple_report_submit_menu)){
//            getMenuInflater().inflate(R.menu.tabbed_user_home_activity_single_or_multiple_report_submit_menu,menu);
//        }
//        if(menu.equals(R.menu.menu_tabbed_user_home_activity2))
            getMenuInflater().inflate(R.menu.menu_tabbed_user_home_activity2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.action_settings:
//                Toast.makeText(this, "User Settings Functionality is Still under Development", Toast.LENGTH_LONG).show();
                launchSettingsActivity();
                return true;
            case R.id.submit_report:
                launchSubmitReportActivity();
                return true;

            case R.id.action_logout:
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
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //  Callback Methods
//  Callbacks for UserAttributesCallback
    @Override
    public void onProcessFinished(Map<String, String> vals) {
        Log.d(TAG, "onProcessFinished(Map<String, String>)");
    }
    @Override
    public void onProcessFinished(String[] vals) {

    }

    //   Callback for GetReportRatedByUserTask
    @Override
    public void setRatedReportString(String ratedReportString) {
        Log.d(TAG, "setRatedReportString() ratedReportString: " + ratedReportString);
        UserInformationModel.getInstance().setRatedReportsTextFileContents(ratedReportString);
    }


    //  Methods that launch activities or dialog
    public void launchMultipleOrSingleReportDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Submit Single or Multiple Reports?")
                .setPositiveButton("Single Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Launch Single report submit activity");
                        launchSubmitReportActivity();
                    }
                })
                .setNeutralButton("Multiple Reports", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Launch Multiple reports activity");
                        launchSubmitMultipleReportsActivtiy();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Cancel Submit reports button pressed");
                    }
                });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
    //  Methods that launch new activities
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

    public void launchSubmitMultipleReportsActivtiy(){
        Intent i = new Intent(this, SubmitMultipleReportsInfoActivity.class);
        startActivity(i);
    }

    public void launchSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }


    //
    public static class PlaceholderFragment extends Fragment {

//      The fragment argument representing the section number for this
//      fragment.
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
//      Returns a new instance of this fragment for the given section
//      number.
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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


        //        Todo: Find new way to launch submit report
//
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            mostRecentSelectedFragment = null;//new UserInfoHomeFragment();
            switch (position){
                case 0:
                    Log.d(TAG, "UserInfoHomeFragment");
                    return new UserInfoHomeFragment();

                //Query Report
                case 1:
                    Log.d(TAG, "QueryReportAttributesFragment");
                    return new QueryReportAttributesFragment();

                //Relevant reports
                case 2:
                    Log.d(TAG, "ViewReportsFromSingleDayFragment");
                    ViewReportsFromSingleDayFragment viewRep = new ViewReportsFromSingleDayFragment();
                    Intent i = new Intent();
                    Bundle b = i.getExtras();
                    viewRep.setArguments(b);
                    return  viewRep;


                case 4:
                    Log.d(TAG, "Chat Frag Selected");
//                    ChatMessageFragment chatFrag = new ChatMessageFragment();
//                    return chatFrag;
                    ChatMessageParent chatParent = new ChatMessageParent();
                    return chatParent;


                default:
                    Log.d(TAG, "Default: UsereInfoHomeFragment");
                    return new UserInfoHomeFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "User";
                case 1:
                    return "Search";
                case 2:
                    return "Reports";
                case 3:
                    return "Submit";
                case 4:
                    return "Chat";
            }
            return null;
        }
    }
}
