package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 3/15/17.
 */

public class ViewReportAttributeTableRowFragment extends Fragment{
    TextView label;
    TextView val;
    TableRow row;
    String labelString;
    String valString;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstance ){

        View view = inflator.inflate(R.layout.fragment_view_report_attribute_tablerow, container, false);
        row= (TableRow)view.findViewById(R.id.attribute_tablerow);
        label = (TextView)view.findViewById(R.id.attribute_label_TV);
        val = (TextView)view.findViewById(R.id.attribute_value_TV);
        return view;
    }

    public String getLabelString() {
        return labelString;
    }

    public void setLabelString(String labelString) {
        this.labelString = labelString;
    }

    public String getValString() {
        return valString;
    }

    public void setValString(String valString) {
        this.valString = valString;
    }

    public TableRow getRow() {
        return row;
    }

    public void setRow(TableRow row) {
        this.row = row;
    }
}
