package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText eid,en,ea,ef,er,es;
    Button bt;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase=FirebaseDatabase.getInstance();

        eid=findViewById(R.id.bkid_et);
        en=findViewById(R.id.bkname_et);
        ea=findViewById(R.id.bkauthor_et);
        ef=findViewById(R.id.bkfloor_et);
        er=findViewById(R.id.bkrack_et);
        es=findViewById(R.id.bksubrack_et);
        bt=findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model map = new model();
                map.setEid(eid.getText().toString());
                map.setEn(en.getText().toString());
                map.setEa(ea.getText().toString());
                map.setEf(ef.getText().toString());
                map.setEr(er.getText().toString());
                map.setEs(es.getText().toString());
               /* HashMap<String,Object> map = new HashMap<>();
                map.put("id",eid.getText().toString());
                map.put("name",en.getText().toString());
                map.put("authorname",ea.getText().toString());
                map.put("floor",ef.getText().toString());
                map.put("rack",er.getText().toString());
                map.put("subrack",es.getText().toString());*/
                 char ch=map.en.charAt(0);
                firebaseDatabase.getReference("Books").child(ch+"").child(map.en).setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this,"upload sucessfully",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }
}