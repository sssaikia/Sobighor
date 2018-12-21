package com.sstudio.sobighor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sstudio.AdminActivity;
import com.sstudio.UpcomingEventsViewActivity;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Firebase firebaseAuth, dynmicTexts;
    String id1, id2;
    ImageButton info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = new Firebase("https://sobighor-689f4.firebaseio.com/auth");
        dynmicTexts = new Firebase("https://sobighor-689f4.firebaseio.com/DynamicTexts/");
        info = (ImageButton) findViewById(R.id.infoButton);
        firebaseAuth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id1 = dataSnapshot.child("email").getValue().toString();
                id2 = dataSnapshot.child("email1").getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        ((Button) findViewById(R.id.start_voting_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isNetworkAvailable()) {
                            new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme)
                                    .setTitle("No Internet connection detected")
                                    .setMessage("Internet connection is needed. If mobile data is not working than try connecting to a WiFi.")
                                    .setNeutralButton("Okay", null)
                                    .show();
                        } else {
                            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                            startActivityForResult(signInIntent, 112);
                        }
                    }
                });
        ((Button) findViewById(R.id.admin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 113);


            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View layoutInflater = getLayoutInflater().inflate(R.layout.aboutdialog_layout, null);
                layoutInflater.findViewById(R.id.aboutUs)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogMethod("aboutUs");
                            }
                        });
                layoutInflater.findViewById(R.id.ourServices)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogMethod("ourServices");
                            }
                        });
                layoutInflater.findViewById(R.id.contactUs)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogMethod("contactUs");
                            }
                        });
                layoutInflater.findViewById(R.id.aboutTheApp)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogMethod("aboutTheApp");
                            }
                        });
                layoutInflater.findViewById(R.id.share)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent=new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sobighor");
                                    String sAux = "\nDownload sobighor, print your life application. \n\n";
                                    sAux = sAux + "Link to be updated shortly \n\n";
                                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(intent, "Share using "));
                                }catch (Exception e){
                                    Log.e("Exception e  :: ",e.toString());
                                }

                            }
                        });
                layoutInflater.findViewById(R.id.facebookLink)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri = Uri.parse("http://facebook.com/sobighar.printyourlife");
                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                likeIng.setPackage("com.facebook.android");

                                try {
                                    startActivity(likeIng);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://facebook.com/sobighar.printyourlife")));
                                }
                            }
                        });
                layoutInflater.findViewById(R.id.instaLink)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri = Uri.parse("http://instagram.com/_u/Sobighar");
                                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                likeIng.setPackage("com.instagram.android");

                                try {
                                    startActivity(likeIng);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://instagram.com/Sobighar")));
                                }
                            }
                        });
                new AlertDialog.Builder(MainActivity.this)
                        .setView(layoutInflater)
                        .show();
            }
        });
    }


    String texts;

    public void DialogMethod(String link) {
        final TextView textView = new TextView(this);
        textView.setMinimumHeight(300);
        textView.setTextSize(15);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(Color.BLUE);
        textView.setBackgroundResource(R.drawable.boardershape);
        dynmicTexts.child(link).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                texts = dataSnapshot.getValue(String.class);
                textView.setText(texts);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        new AlertDialog.Builder(this)
                .setTitle(link)
                .setView(textView)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 112) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Intent intent = new Intent(MainActivity.this, UpcomingEventsViewActivity.class);
                intent.putExtra("Email_ID", result.getSignInAccount().getEmail());
                intent.putExtra("user_name", result.getSignInAccount().getId());
                startActivity(intent);
                Log.d("Main activity ::", "" + result.getSignInAccount().getEmail());
            }
        }
        if (requestCode == 113) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                if (result.getSignInAccount().getEmail().equals(id1)
                        || result.getSignInAccount().getEmail().equals(id2)) {
                    Intent in = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(this, "Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed!! try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .setNeutralButton("Rate us", null)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
