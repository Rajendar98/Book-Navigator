package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class button_visual extends AppCompatActivity {
    private String floor_intended,rack_intended,side_visit,row_length_str,column_length_str;
    private int row_length,column_length;
    private LinearLayout button_layout,inside_layout;
    private Button set_btn;
    private static int width,height,margin;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_visual);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button_layout=(LinearLayout)findViewById(R.id.button_row_layont);
        floor_intended=getIntent().getStringExtra("floorname");
        rack_intended=getIntent().getStringExtra("rackname");
        side_visit=getIntent().getStringExtra("side_visit");
        set_btn=(Button)findViewById(R.id.set_btn);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").child(floor_intended).child(rack_intended).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                DataSnapshot ds= task.getResult();
                                column_length_str=String.valueOf(ds.child("column").getValue());
                                row_length_str=String.valueOf(ds.child("row").getValue());
                                if(row_length_str.equals("7") && column_length_str.equals("8")) {
                                    width = 110;
                                    height=130;
                                    margin=10;
                                }
                                else if(row_length_str.equals("6") && column_length_str.equals("4")) {
                                    width = 235;
                                    height=150;
                                    margin=10;
                                }else if(row_length_str.equals("7") && column_length_str.equals("10")){
                                width=98;
                                height=120;
                                margin=2;
                                }else if(row_length_str.equals("6") && column_length_str.equals("8")){
                                    width=110;
                                    height=130;
                                    margin=10;
                                }
                                row_length=Integer.parseInt(row_length_str);
                                column_length=Integer.parseInt(column_length_str);
                                insert_method(row_length,column_length,width,height,margin);
                            }
                        }
                    }
                });


    }

    private void insert_method(int row_length,int column_length,int width,int height,int margin) {
        int[] int_arr=new int[row_length*column_length];
        for(int i=0;i<(row_length*column_length);i++)
        {
                int_arr[i]=0;
        }
        for(int i=0;i<row_length;i++)
        {
            View subrowView=getLayoutInflater().inflate(R.layout.button_layout_construct,null);
            inside_layout = (LinearLayout) subrowView.findViewById(R.id.inside_layout);
            for(int j=0;j<column_length;j++)
            {
                Button single_btn=new Button(button_visual.this);
                single_btn.setId((column_length)*i+j);
                single_btn.setBackgroundColor(Color.rgb(0,0,150));
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width, height);
                lp.setMargins(margin,10,margin,10);
                single_btn.setLayoutParams(lp);
                int a=(i+1),b=(j+1);
                single_btn.setText(a+":"+b);
                single_btn.setTextColor(Color.rgb(255,255,255));
                single_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id =single_btn.getId();
                        if(int_arr[id]==1)
                        {
                            single_btn.setBackgroundColor(Color.rgb(0,0,150));
                            int_arr[id]=0;
                        }
                        else {
                            single_btn.setBackgroundColor(Color.rgb(150,30,30));
                            int_arr[id]=1;
                        }
                    }
                });
                inside_layout.addView(single_btn);
            }
            button_layout.addView(subrowView);
        }
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder visual_builder=new AlertDialog.Builder(button_visual.this);
                View alert_view=getLayoutInflater().inflate(R.layout.floor_alert,null);
                visual_builder.setView(alert_view);
                EditText visual_alert_ed=(EditText) alert_view.findViewById(R.id.et_name);
                Button visual_ok_button=(Button) alert_view.findViewById(R.id.bt_ok);
                Button visual_cancel_button=(Button) alert_view.findViewById(R.id.bt_cancel);
                AlertDialog final_visual_builder=visual_builder.create();
                final_visual_builder.show();
                visual_cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final_visual_builder.dismiss();
                    }
                });
                visual_ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str = visual_alert_ed.getText().toString();
                        if (!str.isEmpty()){
                            HashMap<String, String> rack_map = new HashMap<String, String>();
                        rack_map.put("name", str);
                        reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Floors").child(floor_intended).child(rack_intended).child(side_visit)
                                .child(str).setValue(rack_map);
                        for (int k = 0; k < (column_length * row_length); k++) {
                            if (int_arr[k] == 1) {
                                reference = FirebaseDatabase.getInstance().getReference();
                                HashMap<String, String> cell_map = new HashMap<String, String>();
                                cell_map.put("name", k + "");
                                reference.child("Floors").child(floor_intended).child(rack_intended).child(side_visit)
                                        .child(str).child(k + "").setValue(cell_map);
                            }
                        }
                        Toast.makeText(button_visual.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        final_visual_builder.dismiss();
                    }
                        else{
                            Toast.makeText(button_visual.this, "Empty Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}