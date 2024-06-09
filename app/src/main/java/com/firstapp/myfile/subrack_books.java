package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class subrack_books extends AppCompatActivity {
    private SearchView searchview_bar;
    private Button add_button_subrack;
    private ArrayAdapter adapt_subrack;
    private ArrayList<String> array_subrack=new ArrayList<String>();
    private ListView book_listview;
    private String rack_str,floor_str,side_of_rack;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subrack_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rack_str=getIntent().getStringExtra("rackname");
        floor_str=getIntent().getStringExtra("floorname");
        side_of_rack=getIntent().getStringExtra("side_of_rack");
        searchview_bar=(SearchView) findViewById(R.id.search_bar);
        book_listview=(ListView) findViewById(R.id.books_listview);
        add_button_subrack=(Button)findViewById(R.id.add_btn);
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Floors").child(floor_str).child(rack_str).child(side_of_rack).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    if (ds.child("name").exists()) {
                        String str = ds.child("name").getValue().toString();
                        array_subrack.add(str);
                    }
                }
                adapt_subrack= new ArrayAdapter(subrack_books.this, android.R.layout.simple_list_item_1,array_subrack);
                book_listview.setAdapter(adapt_subrack);
            }
        });
        add_button_subrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_domainname();
            }
        });
        book_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str="Select mode";
                String domain_name=adapt_subrack.getItem(i).toString();
                alertdialog_method("View","Modify",str, domain_name);
            }
        });
        searchview_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(adapt_subrack.getCount() == 0)
                    Toast.makeText(subrack_books.this, "No results found", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.isEmpty()) {
                    adapt_subrack= new ArrayAdapter(subrack_books.this, android.R.layout.simple_list_item_1,array_subrack);
                    adapt_subrack.getFilter().filter(s);


                    book_listview.setAdapter(adapt_subrack);
                }
                else {
                    adapt_subrack= new ArrayAdapter(subrack_books.this, android.R.layout.simple_list_item_1,array_subrack);
                    book_listview.setAdapter(adapt_subrack);
                }
                return false;
            }
        });
    }


    private void insert_domainname() {
        Intent intent1=new Intent(subrack_books.this,button_visual.class);
        intent1.putExtra("side_visit",side_of_rack);
        intent1.putExtra("floorname",floor_str);
        intent1.putExtra("rackname",rack_str);
        startActivity(intent1);
    }

    private void alertdialog_method(String pos,String neg,String message,String domain_name) {
        AlertDialog.Builder subrack_delete_builder=new AlertDialog.Builder(subrack_books.this);
        subrack_delete_builder.setTitle("Choose").setMessage(message).setCancelable(true)
                .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1=new Intent(subrack_books.this,books_view.class);
                        intent1.putExtra("floorname",floor_str);
                        intent1.putExtra("rackname",rack_str);
                        intent1.putExtra("side_visit",side_of_rack);
                        intent1.putExtra("domain_name",domain_name);
                        startActivity(intent1);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent intent1=new Intent(subrack_books.this,button_visual.class);
                        intent1.putExtra("side_visit",side_of_rack);
                        startActivity(intent1);
                        dialogInterface.dismiss();*/
                        modify_method(domain_name);
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void modify_method(String domain_name) {
        AlertDialog.Builder floor_builder=new AlertDialog.Builder(subrack_books.this);
        View alert_view=getLayoutInflater().inflate(R.layout.modify_layout,null);
        floor_builder.setView(alert_view);
        Button update_btn=(Button)alert_view.findViewById(R.id.update_btn);
        Button delete_btn=(Button)alert_view.findViewById(R.id.delete_btn);
        AlertDialog final_subrack_builder=floor_builder.create();
        final_subrack_builder.show();
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder subrack_delete_builder=new AlertDialog.Builder(subrack_books.this);
                subrack_delete_builder.setTitle("Warning!").setMessage("Do you want to delete "+domain_name+" domain?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference= FirebaseDatabase.getInstance().getReference();
                                reference.child("Floors").child(floor_str).child(rack_str).child(side_of_rack).child(domain_name).removeValue();
                                dialogInterface.dismiss();
                                final_subrack_builder.dismiss();
                                Toast.makeText(subrack_books.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(subrack_books.this,update_domain.class);
                intent1.putExtra("floorname",floor_str);
                intent1.putExtra("rackname",rack_str);
                intent1.putExtra("side_visit",side_of_rack);
                intent1.putExtra("domain_name",domain_name);
                final_subrack_builder.dismiss();
                startActivity(intent1);
            }
        });
    }
}