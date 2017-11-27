package com.example.pasha.lab3_service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /**
     * Key of message to EchoService
     */
    final static String KEY_MESSAGE_SERVICE = "MESSAGE_SERVICE";

    /**
     * Action to send message to EchoService
     */
    final static String ACTION_SEND_MESSAGE_SERVICE = "SEND_MESSAGE_SERVICE";

    @BindView(R.id.button_start)
    Button startButton;

    @BindView(R.id.button_stop)
    Button stopButton;

    @BindView(R.id.button_send)
    Button sendButton;

    @BindView(R.id.edit_text_message)
    EditText messageTextEdit;

    @BindView(R.id.text_view_message)
    TextView messageTextView;

    MainActivityReceiver receiver;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_start)
    void startService() {
        intent = new Intent(MainActivity.this, EchoService.class);
        startService(intent);
        makeToast("Starting service");
    }

    @OnClick(R.id.button_stop)
    void stopService() {

        if (intent != null) {
            stopService(intent);
        }

        intent = null;
        makeToast("Stopping service");
    }

    /** Sends message to Service using broadcast */
    @OnClick(R.id.button_send)
    void onClickSend() {
        String message = messageTextEdit.getText().toString();

        Intent intent = new Intent();
        intent.setAction(ACTION_SEND_MESSAGE_SERVICE);
        intent.putExtra(KEY_MESSAGE_SERVICE, message);
        sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    /**
     * Receives messages from service and changes text view
     */
    private class MainActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (EchoService.ACTION_SEND_MESSAGE_ACTIVITY.equals(action)) {
                String message = intent.getStringExtra(EchoService.KEY_MESSAGE_ACTIVITY);
                messageTextView.setText(String.valueOf(message));
            }
        }
    }

    /**
     * Creates and registers new receiver of messages from Service
     */
    private void createReceiver() {
        receiver = new MainActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EchoService.ACTION_SEND_MESSAGE_ACTIVITY);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * Makes simple toast
     *
     * @param msg message to display
     */
    private void makeToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}
