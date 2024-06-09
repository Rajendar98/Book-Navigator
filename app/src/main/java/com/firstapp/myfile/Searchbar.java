package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Searchbar extends AppCompatActivity {
    private SearchView search;
    private ListView listview;
    private ArrayAdapter<String> adapt;
    private ArrayList<String> array=new ArrayList<String>();
    private String prev="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbar);
        search=findViewById(R.id.search);
        listview=findViewById(R.id.list);

        adapt=new ArrayAdapter<>(Searchbar.this, android.R.layout.simple_list_item_1,array);
        listview.setAdapter(adapt);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Books");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty())
                {
                    String firstchar=s.charAt(0)+"";
                    array.clear();
                    reference.child(firstchar).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren())
                            {
                                array.add(ds.child("en").getValue().toString());
                            }
                            adapt.getFilter().filter(s);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

                return false;
            }
        });



    }
}