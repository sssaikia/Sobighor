package com.sstudio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.sstudio.sobighor.R;
import com.sstudio.sobighor.SobighorVote;
import com.sstudio.sobighor.UserDetails;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.Random;

public class VoteActivity extends AppCompatActivity {

    JazzyListView listView;
    Firebase firebase, vote, session, eventName;
    FirebaseListAdapter<SobighorVote> adaptar;
    String userEmail, userId;
    UserDetails user;
    TextView eventName1, eventName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        Intent i = getIntent();
        userEmail = i.getStringExtra("Email_ID");
        userId = i.getStringExtra("user_name");
        Log.d("email :: ", "" + userEmail);
        listView = (JazzyListView) findViewById(R.id.listview);
        eventName1 = (TextView) findViewById(R.id.event_nameTv);
        eventName2 = (TextView) findViewById(R.id.event_nameTv2);
        eventName = new Firebase("https://sobighor-689f4.firebaseio.com/EventName");
        eventName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventName1.setText(dataSnapshot.child("eventname").getValue().toString());
                eventName2.setText(dataSnapshot.child("eventround").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase = new Firebase("https://sobighor-689f4.firebaseio.com/sobighor");
        vote = new Firebase("https://sobighor-689f4.firebaseio.com/voteUrl/");
        adaptar = new FirebaseListAdapter<SobighorVote>(this, SobighorVote.class, R.layout.listrview_layout, firebase) {
            @Override
            protected void populateView(View view, SobighorVote sobighor, int i) {
                //((TextView) view.findViewById(R.id.perticipant_name)).setText("Participant Name : " + sobighor.getName());
                ((TextView) view.findViewById(R.id.perticipant_no)).setText("Participant No : " + sobighor.getNo());
                //((TextView) view.findViewById(R.id.perticipant_age)).setText("Age : " + sobighor.getAge());
            }
        };

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Random random = new Random();
                int ran = random.nextInt(15);
                listView.setTransitionEffect(ran);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        session = new Firebase("https://sobighor-689f4.firebaseio.com/votingsession");
        session.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("dataSnapshot", "" + (dataSnapshot.child("votingsession").getValue(boolean.class)));
                if (dataSnapshot.child("votingsession").getValue(boolean.class)) {
                    listView.setAdapter(adaptar);
                } else {
                    listView.setAdapter(null);
                    try {
                        new AlertDialog.Builder(VoteActivity.this, R.style.DialogTheme)
                                .setTitle("Voting is not available at the moment.")
                                .setNeutralButton("Okay", null)
                                .show();
                    } catch (Exception e) {
                        Log.e("VoteActivity  ::  ", "" + e);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                String name = String.valueOf(((TextView) view.findViewById(R.id.perticipant_no)).getText());
                int name1 = Integer.parseInt(name.replaceAll("Participant No : ", ""));
                vote = new Firebase("https://sobighor-689f4.firebaseio.com/voteUrl/" + name1);
                user = new UserDetails(name1, userId, userEmail);

                new AlertDialog.Builder(VoteActivity.this, R.style.DialogTheme)
                        .setTitle("Vote for participant No " + name1 + " ?")
                        .setMessage("\nNote:\nVote once registered cannot be removed.\n" +
                                "Please cast your vote carefully.")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                vote.child(userId).setValue(user, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError == null) {
                                            Toast.makeText(VoteActivity.this, "Your vote has been successfully registered. " +
                                                    "Thankyou. ", Toast.LENGTH_SHORT).show();
                                            ((ImageView) view.findViewById(R.id.buttonIm)).setImageResource(R.drawable.done);
                                        } else {
                                            Toast.makeText(VoteActivity.this, "" + firebaseError, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Are you sure you want to leave?\n")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(" No", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
