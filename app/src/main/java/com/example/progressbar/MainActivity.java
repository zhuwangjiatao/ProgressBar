package com.example.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private int progress = 0;
    private Message message;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int p = msg.what;

            uploadingDialog.setProgress(p);
        }

    };
    private UploadingDialog uploadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadingDialog = new UploadingDialog(MainActivity.this);
                uploadingDialog.setCancelable(true);
                uploadingDialog.setMaxProgress(100);
                new Thread(runnable).start();
                uploadingDialog.show();
            }
        });
    }
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            message = handler.obtainMessage();
            // TODO Auto-generated method stub
            try {
                for (int i = 1; i <= 103; i++) {
                    int x = progress++;
                    message.what = x;
                    handler.sendEmptyMessage(message.what);
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
