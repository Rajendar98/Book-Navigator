package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Deletemyfile extends AppCompatActivity {
    private EditText ed1;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletemyfile);
        ed1=findViewById(R.id.del_id);
        button=findViewById(R.id.del_sub);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=ed1.getText().toString();
                if(!id.isEmpty())
                {
                    DatabaseReference ref;
                    ref=FirebaseDatabase.getInstance().getReference("Books").child(id);
                    ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Deletemyfile.this, "success", Toast.LENGTH_SHORT).show();
                            ed1.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Deletemyfile.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}