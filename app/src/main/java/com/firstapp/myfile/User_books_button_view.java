package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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

public class User_books_button_view extends AppCompatActivity {
    private LinearLayout inside_layout,button_layout;
    private String floor_intended,rack_intended,side_visit,domain_name;
    private String row_length_str,column_length_str;
    private static int row_length,column_length,width,height,margin;
    private int[] pos_arr=new int[56];
    private static int size=0;
    private TextView tv;
    private DatabaseReference reference;
    private static String[] str_arr=new String[56];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books_button_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floor_intended=getIntent().getStringExtra("floorname");
        rack_intended=getIntent().getStringExtra("rackname");
        side_visit=getIntent().getStringExtra("side_visit");
        domain_name=getIntent().getStringExtra("domain_name");
        tv=(TextView)findViewById(R.id.heading_view_user_tv);
        button_layout=(LinearLayout)findViewById(R.id.button_user_linearlayout);
        tv.setText("Books are available in below red highlighted cells at "+side_visit+" side of Rack");
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
                                str_arr=new String[row_length*column_length];
                                reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("Floors").child(floor_intended).child(rack_intended).child(side_visit).child(domain_name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        for (DataSnapshot ds : task.getResult().getChildren()) {
                                            if (ds.child("name").exists()) {

                                                String str=(String) ds.child("name").getValue().toString();
                                                str_arr[size]=str;
                                                size++;
                                            }
                                        }
                                        domain_button_view(size,row_length,column_length,width,height,margin);
                                        size=0;
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void domain_button_view(int size,int row_length,int column_length,int width,int height,int margin) {
        for(int i=0;i<row_length;i++) {
            View subrowView = getLayoutInflater().inflate(R.layout.button_layout_construct, null);
            inside_layout = (LinearLayout) subrowView.findViewById(R.id.inside_layout);
            for (int j = 0; j < column_length; j++) {
                Button single_btn = new Button(User_books_button_view.this);
                single_btn.setId(column_length * i + j);
                single_btn.setBackgroundColor(Color.rgb(0, 0, 150));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
                lp.setMargins(margin, 10, margin, 10);
                single_btn.setLayoutParams(lp);
                int a = (i + 1), b = (j + 1);
                single_btn.setText(a + ":" + b);
                single_btn.setTextColor(Color.rgb(255,255,255));
                String str_cmp1=(column_length*i+j)+"";
                for(int k=0;k<size;k++)
                {

                    if(str_cmp1.equals(str_arr[k])){
                        single_btn.setBackgroundColor(Color.rgb(150, 0, 10));
                        break;
                    }
                }
                inside_layout.addView(single_btn);
            }
            button_layout.addView(subrowView);
        }
    }
}