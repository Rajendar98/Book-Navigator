package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class All_search_activity extends AppCompatActivity {
    private ListView allbk_listview;
    private DatabaseReference reference,reference1,reference2,reference3,reference4;
    private static ArrayList<Object> array_allbks=new ArrayList<Object>();
    private ArrayAdapter adapt_allbks;
    private static book_info_class[] bookclass_ref=new book_info_class[2000];
    private static HashMap<String, Object> cell_map = new HashMap<String, Object>();
    private static int ind=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_search);
        allbk_listview=(ListView)findViewById(R.id.all_search_listview);
        array_allbks.add("abc0");
        array_allbks.add("adf");
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot ds:task.getResult().getChildren()){
                    if(ds.child("name").exists()){
                        String floor_str=ds.child("name").getValue().toString();
                        reference1=FirebaseDatabase.getInstance().getReference().child("Floors");
                        reference1.child(floor_str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task1) {
                                for (DataSnapshot ds1:task1.getResult().getChildren()){
                                    if(ds1.child("name").exists()){
                                    String rack_str=ds1.child("name").getValue().toString();
                                        reference2=FirebaseDatabase.getInstance().getReference().child("Floors").child(floor_str);
                                    reference2.child(rack_str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task3) {
                                            reference3=FirebaseDatabase.getInstance().getReference().child("Floors").child(floor_str).child(rack_str);
                                            reference3.child("Left").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task4) {
                                                    for(DataSnapshot ds2:task4.getResult().getChildren()){
                                                        if(ds2.child("name").exists()){
                                                            String book_name=ds2.child("name").getValue().toString();
                                                            array_allbks.add(book_name);
                                                            book_info_class bk_in=new book_info_class();
                                                            bk_in.floor_str=floor_str;
                                                            bk_in.rack_str=rack_str;
                                                            bk_in.side_str="Left";
                                                            cell_map.put(book_name,bk_in);
                                                        }
                                                    }
                                                }
                                            });
                                            reference4=FirebaseDatabase.getInstance().getReference().child("Floors").child(floor_str).child(rack_str);
                                            reference4.child("Right").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task4) {
                                                    for(DataSnapshot ds2:task4.getResult().getChildren()){
                                                        if(ds2.child("name").exists()){
                                                            String book_name=ds2.child("name").getValue().toString();
                                                            array_allbks.add(book_name);
                                                            book_info_class bk_in=new book_info_class();
                                                            bk_in.floor_str=floor_str;
                                                            bk_in.rack_str=rack_str;
                                                            bk_in.side_str="Right";
                                                            cell_map.put(book_name,bk_in);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });}
                                }
                            }
                        });
                    }
                }
                adapt_allbks=new ArrayAdapter(All_search_activity.this, android.R.layout.simple_expandable_list_item_1,array_allbks);
                allbk_listview.setAdapter(adapt_allbks);

            }
        });

       allbk_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String book_name=adapt_allbks.getItem(i).toString();
                book_info_class book_in=new book_info_class();
                book_in= (book_info_class) cell_map.get(book_name);
                Toast.makeText(All_search_activity.this, book_in.rack_str+book_in.side_str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}