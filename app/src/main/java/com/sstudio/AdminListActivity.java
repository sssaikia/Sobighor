package com.sstudio;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
import com.sstudio.sobighor.R;
import com.sstudio.sobighor.SobighorVote;

public class AdminListActivity extends AppCompatActivity {
    Firebase firebase;
    FirebaseListAdapter adaptar;
    ListView listView;
    String deleteEntryNo;
    Button clearVotes;
    private Firebase vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
        listView = (ListView) findViewById(R.id.adminlist);
        clearVotes = (Button) findViewById(R.id.clearvotes);
        firebase = new Firebase("https://sobighor-689f4.firebaseio.com/sobighor");
        vote = new Firebase("https://sobighor-689f4.firebaseio.com/voteUrl/");
        adaptar = new FirebaseListAdapter<SobighorVote>(this, SobighorVote.class, R.layout.adminparticipantlayout, firebase) {
            @Override
            protected void populateView(View view, SobighorVote sobighor, int i) {
                ((TextView) view.findViewById(R.id.perticipant_nameadmin)).setText("Participant Name : " + sobighor.getName());
                ((TextView) view.findViewById(R.id.perticipant_noadmin)).setText("Participant No : " + sobighor.getNo());
                ((TextView) view.findViewById(R.id.perticipant_ageadmin)).setText("Age : " + sobighor.getAge());
            }
        };
        listView.setAdapter(adaptar);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteEntryNo = ((TextView) view.findViewById(R.id.perticipant_noadmin)).getText().toString();
                deleteEntryNo = deleteEntryNo.replaceAll("Participant No : ", "");
                //Toast.makeText(AdminListActivity.this, ""+deleteEntryNo, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(AdminListActivity.this, R.style.DialogTheme)
                        .setTitle("Delete this Entry?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebase.child(deleteEntryNo).removeValue();
                                Log.d("reference  ::  ", firebase.child(deleteEntryNo).toString());
                            }
                        }).show();
            }
        });
        clearVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AdminListActivity.this, R.style.DialogTheme)
                        .setTitle("Clear all votes?")
                        .setMessage("Clicking yes will erase all votes. Are you sure?")
                        .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                vote.removeValue(new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError == null) {
                                            Toast.makeText(AdminListActivity.this, "All votes deleted.!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}
