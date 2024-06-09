package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Updatemyfile extends AppCompatActivity {

    private DatabaseReference db;
    private EditText rac,fr;
    private Button sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemyfile);
        rac=findViewById(R.id.rack);
        fr=findViewById(R.id.floor);
        sub=findViewById(R.id.submit);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fl,ra;
                fl=fr.getText().toString();
                ra=rac.getText().toString();
                updatedata(fl,ra);
            }
        });
    }
    private Void updatedata(String fl,String ra)
    {


        HashMap<String,Object> hm=new HashMap<>();
        hm.put("er",ra);
        db= FirebaseDatabase.getInstance().getReference("Books");
        db.child(fl).updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    fr.setText("");
                    rac.setText("");
                    Toast.makeText(Updatemyfile.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Updatemyfile.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return null;
    }
}