package com.example.oliverasker.skywarnmarkii;

import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by student on 10/27/16.
 */

public class Utility {

    /*
        This method retreives all EditText objects in a layout.
            1. Checks for default vals
            2. If no default vals, adds to arraylist
            3. returns list
     */

    public static ArrayList<EditText> getTextFields(LinearLayout myLayout){
        ArrayList<EditText> myEditTextList = new ArrayList<EditText>();
        String[] editTextVals = new String[myEditTextList.size()-1];
        int arraySize = myLayout.getChildCount();


        for( int i = 0; i < myLayout.getChildCount(); i++ )
            if( myLayout.getChildAt( i ) instanceof EditText ) {
                if(    !( ((EditText) myLayout.getChildAt(i)).getText().equals( "|") )
                        |  !( ((EditText) myLayout.getChildAt(i)).getText().equals( "") )
                        |   !( ((EditText) myLayout.getChildAt(i)).getText().equals( "999") ) ) {

                    myEditTextList.add((EditText) myLayout.getChildAt(i));
                    System.out.println("Utility/ EditTextVals: " + editTextVals[i]);
                }
             }
        return myEditTextList;
    }
}
