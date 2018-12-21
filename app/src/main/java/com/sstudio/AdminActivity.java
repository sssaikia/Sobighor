package com.sstudio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sstudio.sobighor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {
    Button button, button2, buttonEvent, participantlist, vote_round, setLoc, urlButton,
            dynamicTextSetButton;
    ToggleButton button3;
    Firebase session, eventname, setvenue, setDynamictextUrl;
    Firebase regUser;
    private Firebase imagesUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        button = (Button) findViewById(R.id.add);
        button2 = (Button) findViewById(R.id.vote);
        button3 = (ToggleButton) findViewById(R.id.session);
        buttonEvent = (Button) findViewById(R.id.event_name);
        vote_round = (Button) findViewById(R.id.voting_round);
        participantlist = (Button) findViewById(R.id.participant_list);
        setLoc = (Button) findViewById(R.id.setlocationbutton);
        urlButton = (Button) findViewById(R.id.urlButton);
        dynamicTextSetButton = (Button) findViewById(R.id.setDynamicText);
        session = new Firebase("https://sobighor-689f4.firebaseio.com/votingsession");
        eventname = new Firebase("https://sobighor-689f4.firebaseio.com/EventName");
        setvenue = new Firebase("https://sobighor-689f4.firebaseio.com/venue");
        imagesUrl = new Firebase("https://sobighor-689f4.firebaseio.com/images");
        setDynamictextUrl = new Firebase("https://sobighor-689f4.firebaseio.com/");
        regUser = new Firebase("https://sobighor-689f4.firebaseio.com/RegUserEmail/users");

        ((Button) findViewById(R.id.deleteAllUsers))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(AdminActivity.this, R.style.DialogTheme)
                                .setTitle("Are you sure?")
                                .setMessage("Clicking OK will delete all the users " +
                                        "that are currently registered.\nThis action cannot be undone. " +
                                        "Please proceed with coution.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        regUser.removeValue(new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                new AlertDialog.Builder(AdminActivity.this, R.style.DialogTheme)
                                                        .setTitle("Done")
                                                        .setMessage("All users have been deleted.")
                                                        .setPositiveButton("OK", null)
                                                        .show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("cancel", null)
                                .show();

                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, AddActivity.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, VoteCountActivity.class);
                startActivity(i);
            }
        });
        session.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("votingsession").getValue(boolean.class)) {
                    button3.setChecked(true);
                } else {
                    button3.setChecked(false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    session.child("votingsession").setValue(true, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                Toast.makeText(AdminActivity.this, "Voting ON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    session.child("votingsession").setValue(false, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                Toast.makeText(AdminActivity.this, "Voting OFF", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        buttonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText e = new EditText(AdminActivity.this);
                new AlertDialog.Builder(AdminActivity.this, R.style.DialogTheme)
                        .setTitle("Enter the name of the Event.")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventname.child("eventname").setValue(e.getText().toString(), new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        Toast.makeText(AdminActivity.this, "Saved successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setView(e)
                        .show();

            }
        });
        participantlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, AdminListActivity.class);
                startActivity(i);
            }
        });
        vote_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText e = new EditText(AdminActivity.this);
                new AlertDialog.Builder(AdminActivity.this, R.style.DialogTheme)
                        .setTitle("Enter the round.")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventname.child("eventround").setValue(e.getText().toString(), new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError == null) {
                                            Toast.makeText(AdminActivity.this, "Saved successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setView(e)
                        .show();
            }
        });

        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPermission();
            }
        });


        urlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                View view1=getLayoutInflater().inflate(R.layout.setimageandprice,null);
                final String date = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                final EditText editText = view1.findViewById(R.id.seturl);
                final EditText price = view1.findViewById(R.id.setprice);
                editText.setBackgroundResource(R.drawable.boardershape);
                editText.setTextColor(Color.WHITE);
                new AlertDialog.Builder(AdminActivity.this, R.style.DialogTheme)
                        .setTitle("Add new image url.")
                        .setMessage("Copy the image link from somewhere on the web and paste here.")
                        .setView(view1)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ImageUrlC imageUrl1 = new ImageUrlC(editText.getText().toString(), (Long.parseLong("-" + date))
                                        ,price.getText().toString());
                                imagesUrl.child("-" + date).setValue(imageUrl1, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError == null) {
                                            Toast.makeText(AdminActivity.this, "url added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        dynamicTextSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View layoutInflater = getLayoutInflater().inflate(R.layout.aboutdialog_layout, null);
                layoutInflater.findViewById(R.id.aboutUs)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetTextMethod("aboutUs");
                            }
                        });
                layoutInflater.findViewById(R.id.ourServices)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetTextMethod("ourServices");
                            }
                        });
                layoutInflater.findViewById(R.id.contactUs)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetTextMethod("contactUs");
                            }
                        });
                layoutInflater.findViewById(R.id.aboutTheApp)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetTextMethod("aboutTheApp");
                            }
                        });
                new AlertDialog.Builder(AdminActivity.this)
                        .setView(layoutInflater)
                        .show();
            }
        });

    }

    String text;

    public void SetTextMethod(final String link) {

        final EditText textView = new EditText(this);
        textView.setTextSize(15);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.boardershape);
        setDynamictextUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    textView.setText(dataSnapshot.child("DynamicTexts").child(link).getValue(String.class));
                }catch (Exception e){
                    Toast.makeText(AdminActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        new AlertDialog.Builder(this,R.style.DialogTheme)
                .setTitle("Write your text here carefully.")
                .setView(textView)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        text = textView.getText().toString();
                        setDynamictextUrl.child("DynamicTexts").child(link).setValue(text, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    Toast.makeText(AdminActivity.this, "Text set successfully.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .show();
    }


    public void cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 111);
        } else {
            Intent i = new Intent(AdminActivity.this, QRScanActivity.class);
            startActivity(i);
        }
    }


}
