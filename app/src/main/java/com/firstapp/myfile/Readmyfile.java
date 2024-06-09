package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Readmyfile extends AppCompatActivity {
    private EditText bid;
    private Button st;
    TextView text;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmyfile);
        bid=findViewById(R.id.id);
        text=findViewById(R.id.tv1);
        st=findViewById(R.id.go);
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = bid.getText().toString();
                if(!id.isEmpty())
                {
                    readData(id);
                }else
                {
                    Toast.makeText(Readmyfile.this, "enter id" , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private  void readData(String id)
    {
        ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child("A").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Toast.makeText(Readmyfile.this, "successfully read", Toast.LENGTH_SHORT).show();
                        DataSnapshot ds =task.getResult();
                      //  String book_name = String.valueOf(ds.child("en").getValue());
                      //  String author_name = String.valueOf(ds.child("ea").getValue());
                        String rack_no = String.valueOf(ds.child("er").getValue());
                        //String subrack = String.valueOf(ds.child("es").getValue());
                        text.setText(rack_no);




                    } else {
                        Toast.makeText(Readmyfile.this, "not exits", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(Readmyfile.this, "enter id" , Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}