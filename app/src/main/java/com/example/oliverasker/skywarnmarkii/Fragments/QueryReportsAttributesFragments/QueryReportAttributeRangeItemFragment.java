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
    private EditText minRangeTV;
    private EditText maxRangeTV;
    private TextView attributeLabel;
    private StringBuilder attrLabelSB = new StringBuilder();

    private String maxRangeString = "10";
    private String minRangeString = "1";


    public QueryReportAttributeRangeItemFragment() {
        // Required empty public constructor
    }

    //      @return A new instance of fragment QueryReportAttributeRangeItemFragment.
    public static QueryReportAttributeRangeItemFragment newInstance() {
        QueryReportAttributeRangeItemFragment fragment = new QueryReportAttributeRangeItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
