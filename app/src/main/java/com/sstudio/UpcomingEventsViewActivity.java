package com.sstudio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;
import com.sstudio.sobighor.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class UpcomingEventsViewActivity extends AppCompatActivity {
    ListView listView;
    FirebaseListAdapter adapter;
    Firebase images, venue;
    ImageView imageView;
    Button skip;
    String email, id;
    boolean ifRegistered = false;
    private Firebase user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events_view);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email_ID");
        id = intent.getStringExtra("user_name");
        listView = (ListView) findViewById(R.id.comingEvents);
        skip = (Button) findViewById(R.id.skip);

        venue = new Firebase("https://sobighor-689f4.firebaseio.com/venue");
        user = new Firebase("https://sobighor-689f4.firebaseio.com/RegUserEmail/users");

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child(id).getValue(String.class).equals(id)) {
                        ifRegistered = true;
                        //skip.callOnClick();
                    }
                } catch (NullPointerException e) {
                    //Toast.makeText(UpcomingEventsViewActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ifRegistered) {
                    ImageView imageView = new ImageView(UpcomingEventsViewActivity.this);
                    QRGEncoder qrgEncoder = new QRGEncoder(id, null, QRGContents.Type.TEXT, 400);
                    try {
                        Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                        imageView.setImageBitmap(bitmap);
                        //imageView.setBackgroundResource(R.drawable.sobighor);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(UpcomingEventsViewActivity.this, R.style.DialogTheme)
                            .setTitle("Not registered ! \n")
                            .setMessage(id)
                            .setView(imageView)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    Intent intent = new Intent(UpcomingEventsViewActivity.this, VoteActivity.class);
                    intent.putExtra("Email_ID", email);
                    intent.putExtra("user_name", id);
                    startActivity(intent);
                }


            }
        });
        images = new Firebase("https://sobighor-689f4.firebaseio.com/images");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        int height = size.y;
        adapter = new FirebaseListAdapter<ImageUrlC>(this, ImageUrlC.class,
                R.layout.imageviewlay, images.orderByChild("timestamp")) {
            @Override
            protected void populateView(View view, ImageUrlC s, int i) {
                imageView = view.findViewById(R.id.upcomingImageview);
                ((TextView)view.findViewById(R.id.priceTag)).setText(s.getPrice());
                Log.d("String value ::  ", String.valueOf(s));
                Picasso.with(UpcomingEventsViewActivity.this)
                        .load(String.valueOf(s.getUrl()))
                        .resize(width, (width - 100))
                        .into(imageView);
            }


        };
        listView.setAdapter(adapter);
        ((Button)findViewById(R.id.show_upcoming))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(UpcomingEventsViewActivity.this,Second_layout.class));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
