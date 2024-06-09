package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Admin_floor extends AppCompatActivity {
    private LinearLayout floor_container_layout;
    private Button add_btn;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_floor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floor_container_layout=(LinearLayout) findViewById(R.id.linearlayout_floor);
        previously_inserted();
        add_btn=(Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardview();
            }
        });
    }

    private void previously_inserted() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
               for (DataSnapshot ds :task.getResult().getChildren()){
                   if(ds.child("name").exists()){
                   String str=ds.child("name").getValue().toString();
                   View floor_cardview_view=getLayoutInflater().inflate(R.layout.floor_cardview,null);
                   TextView cardview_text=(TextView) floor_cardview_view.findViewById(R.id.settext);
                   Button delete_button=(Button) floor_cardview_view.findViewById(R.id.cancel_layout_btn);
                   cardview_text.setText(str);
                   floor_cardview_view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent1=new Intent(Admin_floor.this,Admin_rack.class);
                           intent1.putExtra("floorname",str);
                           startActivity(intent1);
                       }
                   });
                   floor_container_layout.addView(floor_cardview_view);
                   delete_button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           AlertDialog.Builder floor_delete_builder=new AlertDialog.Builder(Admin_floor.this);
                           floor_delete_builder.setTitle("Alert Warning!").setMessage("Do you want to Delete "+str+" file?").setCancelable(true)
                                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                   floor_container_layout.removeView(floor_cardview_view);
                                                   DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Floors");
                                                   ref.child(str).removeValue();
                                                   dialogInterface.dismiss();
                                               }
                                           }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           dialogInterface.dismiss();
                                       }
                                   }).show();
                       }
                   });}
               }
            }
        });
    }

    private void addcardview() {
        AlertDialog.Builder floor_builder=new AlertDialog.Builder(Admin_floor.this);
        View alert_view=getLayoutInflater().inflate(R.layout.floor_alert,null);
        floor_builder.setView(alert_view);
        EditText floor_alert_ed=(EditText) alert_view.findViewById(R.id.et_name);
        Button floor_ok_button=(Button) alert_view.findViewById(R.id.bt_ok);
        Button floor_cancel_button=(Button) alert_view.findViewById(R.id.bt_cancel);
        AlertDialog final_floor_builder=floor_builder.create();
        final_floor_builder.show();
        floor_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_floor_builder.dismiss();
            }
        });
        floor_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View floor_cardview_view = getLayoutInflater().inflate(R.layout.floor_cardview, null);
                TextView cardview_text = (TextView) floor_cardview_view.findViewById(R.id.settext);
                Button delete_button = (Button) floor_cardview_view.findViewById(R.id.cancel_layout_btn);
                String name_et = floor_alert_ed.getText().toString();
                if (!name_et.isEmpty()){
                    cardview_text.setText(name_et);
                floor_cardview_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(Admin_floor.this, Admin_rack.class);
                        intent1.putExtra("floorname", name_et);
                        startActivity(intent1);
                    }
                });
                floor_container_layout.addView(floor_cardview_view);

                final_floor_builder.dismiss();
                HashMap<String, Object> floor_map = new HashMap<>();
                floor_map.put("name", name_et);
                reference = FirebaseDatabase.getInstance().getReference("Floors");
                reference.child(name_et).setValue(floor_map);
                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder floor_delete_builder = new AlertDialog.Builder(Admin_floor.this);
                        floor_delete_builder.setTitle("Alert Warning!").setMessage("Do you want to Delete " + name_et + " file?").setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        floor_container_layout.removeView(floor_cardview_view);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Floors");
                                        ref.child(name_et).removeValue();
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                });
            }else{
                    Toast.makeText(Admin_floor.this, "Empty name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}