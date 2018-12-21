package com.sstudio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.sstudio.sobighor.R;

public class Second_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_layout);
        Firebase events = new Firebase("https://sobighor-689f4.firebaseio.com/upcomingevents/");
        FirebaseListAdapter<String> adaptar = new FirebaseListAdapter<String>(this, String.class, R.layout.eventlist, events) {
            @Override
            protected void populateView(View view, String sobighor, int i) {
                ((TextView) view.findViewById(R.id.eventlist_tv)).setText(sobighor);
            }
        };
        ((ListView)findViewById(R.id.event_listview)).setAdapter(adaptar);
    }
}
