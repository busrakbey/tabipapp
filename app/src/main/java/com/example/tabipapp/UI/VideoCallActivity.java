package com.example.tabipapp.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.tabipapp.R;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class VideoCallActivity extends AppCompatActivity {

    String gelenRoomName;
    EditText editText;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call);
        gelenRoomName = getIntent().getStringExtra("roomName");

        editText = findViewById(R.id.conferenceName);
        editText.setText(gelenRoomName);
        // store the string input by user in editText in
        // an local variable named text of string type
         text= editText.getText().toString();

        // using try catch block to handle exceptions
        try {
            // object creation of JitsiMeetConferenceOptions
            // class by the name of options
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // we have declared the name of onButtonClick() method
    // in our xml file now we are going to define it.
    public void onButtonClick(View v) {
        // initialize editText with method findViewById()
        // here editText will hold the name of room which is given by user


        // if user has typed some text in
        // EditText then only room will create
        if (text.length() > 0) {
            // creating a room using JitsiMeetConferenceOptions class
            // here .setRoom() method will set the text in room name
            // here launch method with launch a new room to user where
            // they can invite others too.
            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .build();
            JitsiMeetActivity.launch(this, options);
        }
    }
}