
package com.example.oliverasker.skywarnmarkii.Fragments;
/**
 * Created by oliverasker on 1/12/17.
 */

public class Handlers {

    /**
     * Callback used for calls which do not return data and only indicate success or failure.
     */
    public interface GenericHandler {
        /**
         * This callback method is invoked when the call has successfully completed.
         */
        void onSuccess();

        /**
         * This callback method is invoked when call has failed. Probe {@code exception} for cause.
         *
         * @param exception
         */
        void onFailure(Exception exception);
    }
}
