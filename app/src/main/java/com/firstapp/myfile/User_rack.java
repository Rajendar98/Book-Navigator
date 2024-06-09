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

public class User_rack extends AppCompatActivity {
    private String floorname_intented;
    private TextView heading_text;
    private LinearLayout rack_container_layout;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        heading_text=(TextView) findViewById(R.id.heading_ur_tv);
        rack_container_layout=(LinearLayout) findViewById(R.id.User_rack_linearlayout);
        floorname_intented=getIntent().getStringExtra("floorname");
        heading_text.setText("Racks in "+floorname_intented);
        previously_inserted_racks();
    }

    private void previously_inserted_racks() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").child(floorname_intented).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren())
                {
                    if(ds.child("name").exists()){
                        String str=ds.child("name").getValue().toString();
                        View rack_cardview_view=getLayoutInflater().inflate(R.layout.user_cardview,null);
                        TextView cardview_text=(TextView) rack_cardview_view.findViewById(R.id.user_settext);
                        cardview_text.setText(str);
                        rack_cardview_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(User_rack.this,Subrack_user_books.class);
                                intent.putExtra("rackname",str);
                                intent.putExtra("floorname",floorname_intented);
                                startActivity(intent);

                            }
                        });
                        rack_container_layout.addView(rack_cardview_view);
                    }
                }
            }
        });
    }
}