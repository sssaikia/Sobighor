package com.sstudio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.sstudio.sobighor.R;
import com.sstudio.sobighor.UserDetails;

public class VoteCountActivity extends AppCompatActivity {
    Firebase firebase;
    FirebaseListAdapter adapter;
    ListView listView;
    long pcount;
    EditText editText;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_count);
        editText=(EditText)findViewById(R.id.pNo);
        button=(Button)findViewById(R.id.srch_button);
        textView=(TextView)findViewById(R.id.resultTV);
        firebase=new Firebase("https://sobighor-689f4.firebaseio.com/voteUrl");
        listView=(ListView)findViewById(R.id.votecountlist);
        /*firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pcount=dataSnapshot.getChildrenCount();
                Log.d("child ::  "+dataSnapshot.getChildren().toString(),"    count "+dataSnapshot.getChildrenCount());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Firebase firebasevote=new Firebase("https://sobighor-689f4.firebaseio.com/voteUrl/"+editText.getText().toString());
                firebasevote.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textView.setText(dataSnapshot.getChildrenCount()+" Votes.");
                        Log.d("edit text  :  ",editText.getText().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }

                });
                 adapter=new FirebaseListAdapter<UserDetails>(VoteCountActivity.this,UserDetails.class,R.layout.votecountlayout,firebasevote) {
            @Override
            protected void populateView(View view, UserDetails s, int i) {
                Log.d(" Log ::::  ",""+s);
                int n=1;
                TextView textView=(TextView)view.findViewById(R.id.displayName);
                textView.setText(""+s.getUserEmail());
            }
        };
        listView.setAdapter(adapter);
            }
        });

    }
}
