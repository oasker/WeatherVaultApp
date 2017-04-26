package com.example.oliverasker.skywarnmarkii.Fragments.QueryReportsAttributesFragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Callbacks.DateCallBack;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.DateUtility;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryReportAttributesSingleDayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryReportAttributesSingleDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryReportAttributesSingleDayFragment extends Fragment {
    private static final String TAG = "QueryReprtAttrSingleDay";
    int mMonth;
    int mDay;
    int mYear;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private Button setSingleDayButton;
    private long dateToQueryEpoch;
    private StringBuilder dateToQueryString;
    private TextView dateToQueryTV;
    private String month;
    private String day;
    private Date dateToQuery;

    private DateCallBack callBack;

    //    private String mParam1;
//    private String mParam2;
    private TextView dateTV;


    private OnFragmentInteractionListener mListener;

    public QueryReportAttributesSingleDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryReportAttributesSingleDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueryReportAttributesSingleDayFragment newInstance(String param1, String param2) {
        QueryReportAttributesSingleDayFragment fragment = new QueryReportAttributesSingleDayFragment();
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
        dateToQuery = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_query_report_attributes_single_day_date_select, container, false);
        dateToQuery = java.util.Calendar.getInstance().getTime();
        dateToQueryString = new StringBuilder();
        //  Setup widgets and listeners
        dateToQueryTV = (TextView) v.findViewById(R.id.single_day_to_query_TV);
        dateToQueryTV.setText(DateUtility.getTodaysDateString());

        setSingleDayButton = (Button) v.findViewById(R.id.set_single_date_button);
        setSingleDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
//                Set datepicker start date
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Log.d(TAG, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    if (monthOfYear < 10)
                                        month = 0 + String.valueOf(monthOfYear);
                                    Calendar c = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        c = Calendar.getInstance();
                                        c.set(year, monthOfYear, dayOfMonth);
                                        dateToQueryEpoch = c.getTimeInMillis();
                                        dateToQuery = c.getTime();

                                        callBack.setSingleDateToQuery(c);
                                    }

                                    if (monthOfYear + 1 < 10)
                                        month = "0" + String.valueOf(monthOfYear + 1);
                                    else
                                        month = String.valueOf(monthOfYear + 1);

                                    if (dayOfMonth < 10)
                                        day = "0" + String.valueOf(dayOfMonth);
                                    else
                                        day = String.valueOf(dayOfMonth);

//                                    dateToQueryString.append((monthOfYear + 1)+ "/" + (dayOfMonth + "/"  + year));
                                    dateToQueryString.setLength(0);
                                    dateToQueryString.append(month + "/" + day + "/" + year);

                                    dateToQueryTV.setText(dateToQueryString);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMaxDate(DateUtility.getTodaysEpoch());
                    datePickerDialog.show();
                }
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getDateToQueryString() {
        Log.d(TAG, "dateToQueryTV.getText(): " + dateToQueryTV.getText().toString());
        return dateToQueryTV.getText().toString();
    }

    public Date getDateToQuery() {
        return dateToQuery;
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
        void onFragmentInteraction(Uri uri);
    }

    public void setCallBack(DateCallBack callBack) {
        this.callBack = callBack;
    }
}

