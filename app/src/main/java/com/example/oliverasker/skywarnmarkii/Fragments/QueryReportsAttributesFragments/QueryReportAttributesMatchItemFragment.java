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
public class QueryReportAttributesMatchItemFragment extends Fragment {
    private EditText inputQueryItem;

    private TextView attributeLabel;
    private String attrLabelString;


    public QueryReportAttributesMatchItemFragment() {
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
    public static QueryReportAttributesMatchItemFragment newInstance(String param1, String param2) {
        QueryReportAttributesMatchItemFragment fragment = new QueryReportAttributesMatchItemFragment();
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
        attrLabelString = getArguments().getString("attributeString");


//         Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_query_report_attribute_match_item, container, false);

        inputQueryItem = (EditText) v.findViewById(R.id.query_match_attr_input);

        attributeLabel = (TextView) v.findViewById(R.id.query_match_attr_label_TV);
        attributeLabel.setText(attrLabelString);
        return v;
    }


    public String getQueryInput() {
        return inputQueryItem.getText().toString();
    }
}
