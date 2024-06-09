package com.firstapp.myfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Admin_rack extends AppCompatActivity {
    private String floorname_intented;
    private TextView heading_text;
    private LinearLayout rack_container_layout;
    private Button add_rack;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        heading_text=(TextView) findViewById(R.id.rac_in_lib);
        add_rack=(Button)findViewById(R.id.add_btn_rack);
        rack_container_layout=(LinearLayout) findViewById(R.id.linearlayout_rack);
        floorname_intented=getIntent().getStringExtra("floorname");
        heading_text.setText("Racks in "+floorname_intented);
        previously_inserted_racks();
        add_rack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_rack_method();
            }
        });
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
                    View rack_cardview_view=getLayoutInflater().inflate(R.layout.floor_cardview,null);
                    TextView cardview_text=(TextView) rack_cardview_view.findViewById(R.id.settext);
                    Button delete_button=(Button) rack_cardview_view.findViewById(R.id.cancel_layout_btn);
                    cardview_text.setText(str);
                        rack_cardview_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertdialog_method(str,"Left","Right");
                            }
                        });
                    rack_container_layout.addView(rack_cardview_view);
                        delete_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder rack_delete_builder=new AlertDialog.Builder(Admin_rack.this);
                                rack_delete_builder.setTitle("Alert Warning!").setMessage("Do you want to Delete "+str+" file?").setCancelable(true)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                rack_container_layout.removeView(rack_cardview_view);
                                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Floors");
                                                ref.child(floorname_intented).child(str).removeValue();
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
                    }
                }
            }
        });
    }

    private void alertdialog_method(String str,String pos,String neg) {
        AlertDialog.Builder rack_delete_builder=new AlertDialog.Builder(Admin_rack.this);
        rack_delete_builder.setTitle("Choose").setMessage("Select which side of rack that you want to visit").setCancelable(true)
                .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1=new Intent(Admin_rack.this,subrack_books.class);
                        intent1.putExtra("rackname",str);
                        intent1.putExtra("floorname",floorname_intented);
                        intent1.putExtra("side_of_rack","Left");
                        startActivity(intent1);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1=new Intent(Admin_rack.this,subrack_books.class);
                        intent1.putExtra("rackname",str);
                        intent1.putExtra("floorname",floorname_intented);
                        intent1.putExtra("side_of_rack","Right");
                        startActivity(intent1);
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void add_rack_method() {
        AlertDialog.Builder rack_builder=new AlertDialog.Builder(Admin_rack.this);
        View alert_view=getLayoutInflater().inflate(R.layout.rack_r_c_spinnerlayout,null);
        rack_builder.setView(alert_view);
        EditText rack_alert_ed=(EditText) alert_view.findViewById(R.id.et_name);
        Spinner row_spinner=(Spinner)alert_view.findViewById(R.id.row_spinner);
        Spinner column_spinner=(Spinner)alert_view.findViewById(R.id.column_spinner);
        String[] row_arr={"6","7"};
        String[]col_arr={"4","8","10"};
        ArrayAdapter<String> adapter_row=new ArrayAdapter<String>(Admin_rack.this, android.R.layout.simple_spinner_item,row_arr);
        adapter_row.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        row_spinner.setAdapter(adapter_row);
        ArrayAdapter<String> adapter_col=new ArrayAdapter<String>(Admin_rack.this, android.R.layout.simple_spinner_item,col_arr);
        adapter_row.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        column_spinner.setAdapter(adapter_col);
        Button rack_ok_button=(Button) alert_view.findViewById(R.id.bt_ok);
        Button rack_cancel_button=(Button) alert_view.findViewById(R.id.bt_cancel);
        AlertDialog final_rack_builder=rack_builder.create();
        final_rack_builder.show();
        rack_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_rack_builder.dismiss();
            }
        });
        rack_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View rack_cardview_view = getLayoutInflater().inflate(R.layout.floor_cardview, null);
                TextView cardview_text = (TextView) rack_cardview_view.findViewById(R.id.settext);
                Button delete_button = (Button) rack_cardview_view.findViewById(R.id.cancel_layout_btn);
                String name_et = rack_alert_ed.getText().toString();
                if (!name_et.isEmpty()){
                    cardview_text.setText(name_et);
                rack_cardview_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertdialog_method(name_et, "Left", "Right");
                    }
                });
                rack_container_layout.addView(rack_cardview_view);
                final_rack_builder.dismiss();
                HashMap<String, Object> rack_map = new HashMap<>();
                String row = row_spinner.getSelectedItem().toString();
                String column = column_spinner.getSelectedItem().toString();
                rack_map.put("name", name_et);
                rack_map.put("row", row);
                rack_map.put("column", column);
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Floors").child(floorname_intented).child(name_et).setValue(rack_map);
                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder rack_delete_builder = new AlertDialog.Builder(Admin_rack.this);
                        rack_delete_builder.setTitle("Alert Warning!").setMessage("Do you want to Delete " + name_et + " file?").setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rack_container_layout.removeView(rack_cardview_view);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Floors");
                                        ref.child(floorname_intented).child(name_et).removeValue();
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
                    Toast.makeText(Admin_rack.this, "Empty name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}