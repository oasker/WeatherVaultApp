package com.example.oliverasker.skywarnmarkii.Callbacks;

/**
 * Created by oliverasker on 5/25/17.
 */
//  Callback interface for GetReportsRatedByUserTask

public interface GetReportsRatedByUserTaskCallback {
    //    This method of callback returns S3 text file containing
//    all reports user has rated
    void setRatedReportString(String ratedReportString);
}
