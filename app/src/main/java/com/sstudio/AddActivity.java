package com.sstudio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.sstudio.sobighor.R;
import com.sstudio.sobighor.SobighorVote;

public class AddActivity extends AppCompatActivity {
    Firebase firebase;
    EditText name, no, age;
    Button button;
    String nameVal;
    int noVal,ageVal;
    ImageView imageView;
    SobighorVote sobighorVote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        firebase= new Firebase("https://sobighor-689f4.firebaseio.com/sobighor");
        name=(EditText)findViewById(R.id.name);
        age=(EditText)findViewById(R.id.age);
        no=(EditText)findViewById(R.id.no);
        button=(Button)findViewById(R.id.button_save);
        imageView=(ImageView)findViewById(R.id.feedbackicon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nameVal=name.getText().toString();
                    noVal=Integer.parseInt( no.getText().toString());
                    ageVal=Integer.parseInt(age.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                sobighorVote=new SobighorVote(noVal,nameVal,ageVal);
                firebase.child(String.valueOf(noVal)).setValue(sobighorVote, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError==null){
                            imageView.setImageResource(R.drawable.done);
                        }else {
                            Toast.makeText(AddActivity.this, ""+firebaseError, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageView.setImageResource(R.drawable.wait);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageView.setImageResource(R.drawable.wait);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            imageView.setImageResource(R.drawable.wait);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /*firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child(String.valueOf(noVal)).getValue(SobighorVote.class).getNo()==noVal){
                        imageView.setImageResource(R.drawable.done);
                    }else{
                        Toast.makeText(AddActivity.this, "Failed"+dataSnapshot.child(String.valueOf(noVal)).getValue(Sobighor.class), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("Addactivity  ::  ",e+"");
                    //Toast.makeText(AddActivity.this, "failed"+e, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }
}
