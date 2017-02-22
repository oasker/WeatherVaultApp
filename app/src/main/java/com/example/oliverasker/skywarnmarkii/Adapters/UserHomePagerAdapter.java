package com.example.oliverasker.skywarnmarkii.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.oliverasker.skywarnmarkii.Constants;

//import android.app.FragmentManager;


/**
 * Created by oliverasker on 1/15/17.
 * https://developer.android.com/reference/android/app/Fragment.html
 */

public class UserHomePagerAdapter extends FragmentPagerAdapter{

    public UserHomePagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            //based on which page return fragment
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;

        }

        return ArrayListFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return Constants.NUM_TAB_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return "Page " + position;
    }














    ///////////////////////////////////////////////////////////////
    //              ArrayListFragment                           //
    ///////////////////////////////////////////////////////////////

    public static class ArrayListFragment extends ListFragment {
        private final static String TAG = "ArrayListFragment";
        int mNum;
     static ArrayListFragment newInstance(int num){
         ArrayListFragment f = new ArrayListFragment();

         Bundle  args = new Bundle();
         args.putInt("num",num);
         f.setArguments(args);

         return  f;
     }

        @Override
        public void onCreate(Bundle savedInstance){
            super.onCreate(savedInstance);
            mNum = getArguments() !=null? getArguments().getInt("num") :1;
        }

        /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

            View v = inflater.inflate(R.layout.fragment_pager_list,container,false);

            View tv = v.findViewById(R.id.text);
            ((TextView)tv).setText("Fragment #: " + mNum);
            return v;
        }
        */

        @Override
        public void onActivityCreated(Bundle savedInstance){

            super.onActivityCreated(savedInstance);
            /*
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android:R.layout.user_home_tabs,
                    )
                    */
        }


        @Override
        public void onListItemClick(ListView l, View v, int position, long id){
            Log.i("fragmentList", "ItemClicked(): " + id);
        }

    }
}