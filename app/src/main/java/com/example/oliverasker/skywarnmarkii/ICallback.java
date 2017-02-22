package com.example.oliverasker.skywarnmarkii;

import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;

import java.util.ArrayList;

/**
 * Created by student on 10/12/16.
 */

public interface ICallback {
    void processFinish(ArrayList<SkywarnWSDBMapper> result);
}
