package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_floor extends AppCompatActivity {
    private LinearLayout user_floor_layoyt;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_floor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_floor_layoyt=(LinearLayout) findViewById(R.id.user_floor_linearlayout);
        previously_inserted();

    }

    private void previously_inserted() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds :task.getResult().getChildren()){
                    if(ds.child("name").exists()){
                        String str=ds.child("name").getValue().toString();
                        View floor_cardview_view=getLayoutInflater().inflate(R.layout.user_cardview,null);
                        TextView cardview_text=(TextView) floor_cardview_view.findViewById(R.id.user_settext);
                        cardview_text.setText(str);
                        floor_cardview_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent1=new Intent(User_floor.this,User_rack.class);
                                intent1.putExtra("floorname",str);
                                startActivity(intent1);
                            }
                        });
                        user_floor_layoyt.addView(floor_cardview_view);
                       }
                }
            }
        });
    }

}