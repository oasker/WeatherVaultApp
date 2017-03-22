package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Adapters.ExpandableListAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.QueryReportAttributesTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryReportAttributesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryReportAttributesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryReportAttributesFragment extends Fragment implements ICallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button submitSearchButton;
    private ExpandableListView winterExpandableListView;
    private ExpandableListView severeExpandableListView;
    private ExpandableListView rainExpandableListView;
    private ExpandableListView coastalExpandableListView;

    private HashMap<String, List<String>> listDataChild;

    private String mParam1;
    private String mParam2;
    private HashMap<String,String> attributesToQuery;

    private List<String> expandableHeaders;
    private ExpandableListAdapter expandableListAdapter;

    private OnFragmentInteractionListener mListener;

    public QueryReportAttributesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryReportAttributesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueryReportAttributesFragment newInstance(String param1, String param2) {
        QueryReportAttributesFragment fragment = new QueryReportAttributesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_query_report_attributes,container,false );


        // Begin query button
        submitSearchButton = (Button)v.findViewById(R.id.submit_query_attr_button);
        submitSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQueryReportAttributesTask();
            }
        });

        initData();
        expandableListAdapter = new ExpandableListAdapter(getContext(), expandableHeaders, listDataChild);

        
        ////////////WINTER EXPANDABLE SCROLLVIEW//////////
        winterExpandableListView = (ExpandableListView)v.findViewById(R.id.winter_expandable_listview);
        setListViewHeightBasedOnChildren(winterExpandableListView);
        //Init data before attaching adapter
        winterExpandableListView.setAdapter(expandableListAdapter);
        winterExpandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        winterExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand() winter");
            }
        });
        winterExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollpase() winter");
            }
        });
        winterExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), listDataChild.get(expandableHeaders.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        ////////////SEVERE EXPANDABLE SCROLLVIEW//////////
        severeExpandableListView = (ExpandableListView)v.findViewById(R.id.severe_expandable_listview);
        setListViewHeightBasedOnChildren(severeExpandableListView);
        severeExpandableListView.setAdapter(expandableListAdapter);
        severeExpandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        severeExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand() severe");
            }
        });
        severeExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollpase() severe");
            }
        });
        severeExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), listDataChild.get(expandableHeaders.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        ////////////RAIN/FLOOD EXPANDABLE SCROLLVIEW//////////
        rainExpandableListView = (ExpandableListView)v.findViewById(R.id.rain_expandable_listview);
        setListViewHeightBasedOnChildren(rainExpandableListView);
        rainExpandableListView.setAdapter(expandableListAdapter);
        rainExpandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        rainExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand() rain");
            }
        });
        rainExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollpase() rain");
            }
        });
        rainExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), listDataChild.get(expandableHeaders.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        ////////////COASTAL EXPANDABLE SCROLLVIEW//////////
        coastalExpandableListView = (ExpandableListView)v.findViewById(R.id.coastal_expandable_listview);
        setListViewHeightBasedOnChildren(coastalExpandableListView);
        coastalExpandableListView.setAdapter(expandableListAdapter);
        coastalExpandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        coastalExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand() coastal");
            }
        });
        coastalExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollpase() coastal");
            }
        });
        coastalExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), listDataChild.get(expandableHeaders.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return v;
    }

    private void initData(){
        //Todo: Figure out why the number of expandable list views doesnt match expectation

        expandableHeaders = new ArrayList<>();
        listDataChild = new HashMap<>();

        List winterAttributesList = new ArrayList<String>();
        winterAttributesList.add("Snowfall Depth");
        winterAttributesList.add("Snowfall Rate");

        List severeAttributesList = new ArrayList<String>();
        severeAttributesList.add("Wind Gust");
        severeAttributesList.add("Wind Speed");

        List rainAttributesList = new ArrayList<String>();
        rainAttributesList.add("Water Equivalent");
        rainAttributesList.add("Snowfall Rate");
//
        List coastalAttributesList = new ArrayList<String>();
        coastalAttributesList.add("Storm Surge");


        //Set Headers
        expandableHeaders.add("Winter Attributes");
        expandableHeaders.add("Severe Attributes");
        expandableHeaders.add("Coastal Attributes");
        expandableHeaders.add("Rain/Flood Attributes");

        Log.d(TAG, "expandableHeaders size: " + expandableHeaders.size());

        listDataChild.put(expandableHeaders.get(0), winterAttributesList);
        listDataChild.put(expandableHeaders.get(1), severeAttributesList);
        listDataChild.put(expandableHeaders.get(2),coastalAttributesList);
        listDataChild.put(expandableHeaders.get(3), rainAttributesList);

    }

    private void launchQueryReportAttributesTask(){
        QueryReportAttributesTask queryTask = new QueryReportAttributesTask();
        queryTask.setContext(getContext());
        queryTask.setDelegate(this);


//        attributesToQuery.put("CallSign", "KD1CY");
//        attributesToQuery.put("City","Peabody");
        queryTask.setAttributesToQuery(attributesToQuery);
        queryTask.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
        Log.i(TAG, "processFinish()");
        Log.i(TAG, "result null?: " + (result == null) + " size(): " + result.size());

        for(SkywarnWSDBMapper item:result){
            Log.i(TAG, "date OF: " + item.getDateOfEvent());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
