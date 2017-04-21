package com.example.oliverasker.skywarnmarkii.Fragments.QueryReportsAttributesFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QueryReportAttributeRangeItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryReportAttributeRangeItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private EditText minRangeTV;
    private EditText maxRangeTV;
    private TextView attributeLabel;
    private StringBuilder attrLabelSB = new StringBuilder();

    private String maxRangeString = "10";
    private String minRangeString = "1";


    public QueryReportAttributeRangeItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryReportAttributeRangeItemFragment.
     */
    public static QueryReportAttributeRangeItemFragment newInstance(String param1, String param2) {
        QueryReportAttributeRangeItemFragment fragment = new QueryReportAttributeRangeItemFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//          Get data passed to fragment
        attrLabelSB.append(getArguments().getString("attributeString"));

//         Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_query_report_attribute_range_item, container, false);
        minRangeTV = (EditText) v.findViewById(R.id.query_range_attr_min);
        minRangeTV.setText(minRangeString);

        maxRangeTV = (EditText) v.findViewById(R.id.query_range_attr_max);
        maxRangeTV.setText(maxRangeString);

        attributeLabel = (TextView) v.findViewById(R.id.attribute_label_TV);
        attributeLabel.setText(attrLabelSB.toString());
        return v;
    }


    //////////////////   Setters/Getters     ///////////////////////

    public String getMaxRangeString() {
        return maxRangeTV.getText().toString();
    }

    public void setMaxRangeString(String maxRangeString) {
        this.maxRangeString = maxRangeString;
    }

    public String getMinRangeString() {
        return minRangeTV.getText().toString();
    }

    public void setMinRangeString(String minRangeString) {
        this.minRangeString = minRangeString;
    }

    public TextView getAttributeLabel() {
        return attributeLabel;
    }
}
