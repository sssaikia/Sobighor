package com.sstudio;

import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.sstudio.sobighor.R;

public class QRScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private Firebase regUser;
    MediaPlayer mediaPlayer;
    QRCodeReaderView q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        q = (QRCodeReaderView) findViewById(R.id.qrview);
        q.setOnQRCodeReadListener(this);
        regUser = new Firebase("https://sobighor-689f4.firebaseio.com/RegUserEmail/users");
        mediaPlayer = MediaPlayer.create(QRScanActivity.this, R.raw.beep);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    String lastText = "abc";

    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        if (!lastText.equals(text)) {
            regUser.child(text).setValue(text, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError == null) {
                        lastText = text;
                        ((ImageView) findViewById(R.id.scanFeedback))
                                .setImageResource(R.drawable.done);

                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //mediaPlayer.stop();
                                //mediaPlayer.release();
                                ((ImageView) findViewById(R.id.scanFeedback))
                                        .setImageResource(R.drawable.wait);
                            }
                        });


                    }
                }

            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        q.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        q.startCamera();
    }
}
