package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Subrack_user_books extends AppCompatActivity {
    private SearchView searchview_bar;
    private ArrayAdapter adapt_subrack;
    private ArrayList<String> array_subrack=new ArrayList<String>();
    private ListView book_listview;
    private String rack_str,floor_str;
    private DatabaseReference reference;
    int  count1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subrack_user_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rack_str=getIntent().getStringExtra("rackname");
        floor_str=getIntent().getStringExtra("floorname");
        searchview_bar=(SearchView) findViewById(R.id.user_searchview);
        book_listview=(ListView) findViewById(R.id.user_listview);
       // count1=0;
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").child(floor_str).child(rack_str).child("Left").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    if (ds.child("name").exists()) {
                        String str = ds.child("name").getValue().toString();
                        array_subrack.add(str);
                        count1++;
                    }
                }
                adapt_subrack= new ArrayAdapter(Subrack_user_books.this, android.R.layout.simple_list_item_1,array_subrack);
                book_listview.setAdapter(adapt_subrack);
            }
        });
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").child(floor_str).child(rack_str).child("Right").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    if (ds.child("name").exists()) {
                        String str = ds.child("name").getValue().toString();
                        array_subrack.add(str);
                    }
                }
                adapt_subrack= new ArrayAdapter(Subrack_user_books.this, android.R.layout.simple_list_item_1,array_subrack);
                book_listview.setAdapter(adapt_subrack);
            }
        });
        book_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String domain_name=adapt_subrack.getItem(i).toString();
                int count2=0;
                for(String list_name:array_subrack){
                    count2++;
                    if(list_name.equals(domain_name))
                        break;
                }
                if(count1<count2)
                {
                    String side="Right";
                    Intent intent=new Intent(Subrack_user_books.this,User_books_button_view.class);
                    intent.putExtra("side_visit",side);
                    intent.putExtra("rackname",rack_str);
                    intent.putExtra("floorname",floor_str);
                    intent.putExtra("domain_name",domain_name);
                    startActivity(intent);
                }
                else
                {
                    String side="Left";
                    Intent intent=new Intent(Subrack_user_books.this,User_books_button_view.class);
                    intent.putExtra("side_visit",side);
                    intent.putExtra("rackname",rack_str);
                    intent.putExtra("floorname",floor_str);
                    intent.putExtra("domain_name",domain_name);
                    startActivity(intent);
                }
            }

        });
        searchview_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if(adapt_subrack.getCount() == 0)
                    Toast.makeText(Subrack_user_books.this, "No results found", Toast.LENGTH_SHORT).show();return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()) {
                    adapt_subrack= new ArrayAdapter(Subrack_user_books.this, android.R.layout.simple_list_item_1,array_subrack);
                    adapt_subrack.getFilter().filter(s);
                    book_listview.setAdapter(adapt_subrack);
                }
                else {
                    adapt_subrack= new ArrayAdapter(Subrack_user_books.this, android.R.layout.simple_list_item_1,array_subrack);
                    book_listview.setAdapter(adapt_subrack);
                }
                return false;
            }
        });
    }


}