package com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by oliverasker on 4/9/17.
 */

public class SubmitMultipleReportsDate extends Fragment implements OnDateSetListener {

    private static final String TAG = "SubmitMultReportDate";
    private Button setDateButton;
    //    private CheckBox setAsDefault;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView dateTV;

    private String dateSubmittedString;
    private double dateSubmittedEpoch;
    private long dateOfEvent;


    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_multiple_report_date_select,container,false);

        Log.d(TAG, "onCreate()");
        dateTV = (TextView)v.findViewById(R.id.multiple_reports_dateTV);
//        setAsDefault = (CheckBox)v.findViewById(R.id.use_as_default_location_checkbox);

        setDateButton = (Button)v.findViewById(R.id.set_date_multiple_reports_button);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setDateButton onClick() ");
                final Calendar c;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                }
//                Set datepicker start date
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            new OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Log.d(TAG, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Calendar c = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        c = Calendar.getInstance();
                                        c.set(year, monthOfYear, dayOfMonth);
                                        dateOfEvent = c.getTimeInMillis();
                            }
                                    dateTV.setText((monthOfYear + 1) + "/" + (dayOfMonth + "/" + year));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
            }
            }
        });
        return v;
    }

    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues");

        if(dateOfEvent!=0)
            vals.put("DateOfEvent", new AttributeValue().withN(String.valueOf(dateOfEvent)));
        if (dateOfEvent == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar c = Calendar.getInstance();
                vals.put("DateOfEvent", new AttributeValue().withN(String.valueOf(c.getTimeInMillis())));
            }
        }

        vals.put("DateSubmittedEpoch", new AttributeValue().withN(String.valueOf(System.currentTimeMillis())));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(new Date()).toString();
        vals.put("DateSubmittedString", new AttributeValue().withS(date));

        return vals;
    }

    public boolean areAllRequiredFieldsEntered() {
        return dateOfEvent != 0;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
