package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.cognitoidentityprovider.model.UpdateUserAttributesResult;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.MyImageModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.BitmapUtility;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

interface UpdateUserAttributesCallback {
    void onSuccess();

    void onFailure();
}

public class SettingsActivity extends AppCompatActivity implements UpdateUserAttributesCallback {
    private static final String TAG = "SettingsActivity";

    //    Profile picture imageview
    private ImageView profileImageView;


    private Button cancelButton;
    private Button saveChangesButton;
    private Button changeProfilePictureButton;

    private EditText callSignInput;
    private EditText spotterIdInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneInput;


    //    Spinner and selected value
    private Spinner affitiationSpinner;
    private String affiliationString;
    private ArrayList<MyImageModel> imageModelArrayList;

    //  Holds path to new user profile image
    private String path;

    private int PICK_IMAGE_REQUEST = 0;
    private int TAKE_NEW_IMAGE_REQUEST = 1;


    //    Callbacks for updating cognito attributes task
    @Override
    public void onSuccess() {
        Log.d(TAG, "callback onSuccess()");
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "callback onFailure()");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);

        imageModelArrayList = new ArrayList<MyImageModel>();


//        Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Setup widgets
        profileImageView = (ImageView) findViewById(R.id.settings_profile__picture_IV);

        callSignInput = (EditText) findViewById(R.id.settings_callsign_input);
        callSignInput.setHint(UserInformationModel.getInstance().getCallsign());

        spotterIdInput = (EditText) findViewById(R.id.settings_spotter_id_input);
        spotterIdInput.setHint(UserInformationModel.getInstance().getSpotterID());

        firstNameInput = (EditText) findViewById(R.id.settings_given_name_input);
        firstNameInput.setHint(UserInformationModel.getFirstName());

        lastNameInput = (EditText) findViewById(R.id.settings_family_name_input);
        lastNameInput.setHint(UserInformationModel.getLastName());

        phoneInput = (EditText) findViewById(R.id.settings_phone_number_input);
        phoneInput.setText(UserInformationModel.getInstance().getPhone());


        affitiationSpinner = (Spinner) findViewById(R.id.settings_affiliation_spinner);
        int position = getIndexOfAffiliationOnSpinner(UserInformationModel.getInstance().getAffiliation());
        if (position != -1) {
            affitiationSpinner.setSelection(position);
        }
//      Setup affiliation spinner and set listeners
        affitiationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "StateSpinnerInput: onItemSelected(): " + parent.getItemAtPosition(position).toString());
                affiliationString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                affiliationString = null;
            }

        });

//      Add listeners to buttons
        changeProfilePictureButton = (Button) findViewById(R.id.settings_update_user_profile_picture);
        changeProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "changeProfilePictureButton onClick()");
                launchUpdateUserPhotoDialog();
            }
        });

        saveChangesButton = (Button) findViewById(R.id.settings_save_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "saveChangedButton onClick()");
//                getAttributeToUpdate();
//                updateAttribtes();
                UpdateCognitoAttributesTask updateTask = new UpdateCognitoAttributesTask();
                updateTask.setmContext(getApplicationContext());
                updateTask.setUserID(UserInformationModel.getInstance().getUserID());
                updateTask.setAttributesToUpdate(getAttributeToUpdate());
                updateTask.setCognitoUser(UserInformationModel.getInstance().getCognitoUser());
                updateTask.execute();

//                Upload user photo to S3
                beginUpload(path);
            }
        });

        cancelButton = (Button) findViewById(R.id.settings_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cancelButton onClick()");
                onBackPressed();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //    Adds fields that are not empty to the list of attribtues to update
    private HashMap<String, String> getAttributeToUpdate() {
        HashMap<String, String> userAttributesToUpdate = new HashMap<>();
        if (affiliationString != null)
            userAttributesToUpdate.put("custom:Affiliation", affiliationString);
        if (callSignInput.getText().toString().trim().length() > 0)
            userAttributesToUpdate.put("custom:CallSign", callSignInput.getText().toString());
        if (spotterIdInput.getText().toString().trim().length() > 0)
            userAttributesToUpdate.put("custom:SpotterID", spotterIdInput.getText().toString());
        if (firstNameInput.getText().toString().trim().length() > 0)
            userAttributesToUpdate.put("given_name", firstNameInput.getText().toString());
        if (lastNameInput.getText().toString().trim().length() > 0)
            userAttributesToUpdate.put("family_name", lastNameInput.getText().toString());
        return userAttributesToUpdate;
    }

    private int getIndexOfAffiliationOnSpinner(String affiliation) {
        int index = -1;
        String[] affiliationsStringArray = getResources().getStringArray(R.array.affiliation_spinner_values);
        if (Arrays.asList(affiliationsStringArray).contains(affiliation)) {
            index = Arrays.asList(affiliationsStringArray).indexOf(affiliation);
            Log.d(TAG, "index of affiliation: " + index);
        }
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap existingBitmap = null;
        if (data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Log.d(TAG, "PICK_IMAGE_REQUEST");
                try {
                    existingBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    Uri uri = BitmapUtility.getImageUri(this, existingBitmap);
                    path = BitmapUtility.getRealPathFromURI(uri, this);

                    MyImageModel image = new MyImageModel();
                    image.setTitle("Test");
                    image.setDescription(
                            "test take a photo and add it to list view");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(path);
                    profileImageView.setImageBitmap(existingBitmap);
                    imageModelArrayList.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == TAKE_NEW_IMAGE_REQUEST) {
                Log.d(TAG, "TAKE_NEW_IMAGE_REQUEST");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profileImageView.setImageBitmap(photo);
                Uri uri = BitmapUtility.getImageUri(this, photo);
                path = BitmapUtility.getRealPathFromURI(uri, this);
//                beginUpload(path);
            }
        }
        Log.d(TAG, "data == null");
    }


    //  Note the actual names of the buttons (negative, positive) are irrelevant
    public void launchUpdateUserPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Take a new photo or use existing?")

                .setTitle("Update User Photo");
        builder.setPositiveButton("Use Existing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, " positive button  EXISTING PHOTO clicked");
                Intent intent = new Intent();

//              Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

//              Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        builder.setNegativeButton("Take New", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, " netural button  TAKE NEW PHOTO clicked");

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    String fileName = "temp.jpg";
//                    ContentValues values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, fileName);
//                    mCapturedImageURI = getContentResolver()
//                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//                    startActivityForResult(takePictureIntent, TAKE_NEW_IMAGE_REQUEST);
//                }
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_NEW_IMAGE_REQUEST);
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, " negative button  CANCEL clicked");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void beginUpload(String filePath) {
        Log.d(TAG, "beginUpload()");
        Log.d(TAG, "beginUpload(): filepath: " + filePath);
        //File file = new File (mCurrentPhotoPath);
        File file = new File(filePath);

        Log.d(TAG, "fileName: " + file.getName() + "   filePath: " + filePath);
        String filename = UserInformationModel.getInstance().getUsername() + ".jpg";
        Log.d(TAG, "beginUpload(): filename: " + filename);
        TransferObserver observer = Utility.getTransferUtility(this).upload(Constants.BUCKET_NAME, filename, file);
        observer.setTransferListener(new SettingsActivity.UploadListener());
    }

    private class UploadListener implements TransferListener {
        //Updates UI when notified
        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG, "onStateChanged: " + id + state);

        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, format("onProgressChanged:  %d, total %d, current: %d", id, bytesTotal, bytesCurrent));

        }

        @Override
        public void onError(int id, Exception ex) {
            Log.d(TAG, "Error during upload: " + ex);
        }

    }


    private class UpdateCognitoAttributesTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "UpdateCogAttrTask";
        private Context mContext;
        private HashMap<String, String> attributesToUpdate;
        private String userID;
        private CognitoUser cognitoUser;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute()");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground()");


            com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler handler
                    = new com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler() {

                @Override
                public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                    Log.d(TAG, "onSuccess");

                }

                @Override
                public void onFailure(Exception exception) {
                    // Change failed, probe exception for details
                    Log.e(TAG, "onFailure", exception);
                }
            };


            CognitoUserPool userPool = new CognitoUserPool(mContext, Constants.cognitoPoolID, Constants.cognitoAppClientId, null);

            cognitoUser = userPool.getUser(userID);
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();

            Iterator it = attributesToUpdate.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pair = (Map.Entry) it.next();
                //System.out.println("AttrMap contains: "+pair.getKey() + " = " + pair.getValue());
                Log.d(TAG, "AttrMap contains: " + pair.getKey() + " = " + pair.getValue());
                userAttributes.addAttribute(pair.getKey().toString(), pair.getValue().toString());
            }


            //        userAttributes.addAttribute("custom:Affiliation", "test AFfiliation 11");
            //        Log.d(TAG, "userAttributes=null? "+(userAttributes==null) + " handler==null? " + (handler==null));
            //        Log.d(TAG,"user == null? " + (cognitoUser == null));

            try {
                cognitoUser.updateAttributesInBackground(userAttributes, handler);
            } catch (NullPointerException exc) {
                Log.e(TAG, "NPE caught trying to update user attributes");
            }

            UpdateUserAttributesResult result = new UpdateUserAttributesResult();
            //        Log.d(TAG, "RESULT: " + result.getCodeDeliveryDetailsList().toString());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute()");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate()");
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        public void setAttributesToUpdate(HashMap<String, String> attributesToUpdate) {
            this.attributesToUpdate = attributesToUpdate;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setCognitoUser(CognitoUser cognitoUser) {
            this.cognitoUser = cognitoUser;
        }
    }
}