package com.example.pertemuanfirebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText name, nameUpdate, address, addressUpdate;
    Button update, insert, showData, delete;

    DatabaseReference mDatabaseReference;

    Student mStudent;

    String keyStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.et_name);
        nameUpdate = findViewById(R.id.et_update_name);
        address = findViewById(R.id.et_address);
        addressUpdate = findViewById(R.id.et_update_address);
        insert = findViewById(R.id.btn_insert);
        update = findViewById(R.id.btn_update);
        showData = findViewById(R.id.btn_read);
        delete = findViewById(R.id.btn_delete);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(keyStudent).removeValue();
                Toast.makeText(MainActivity.this, "Delete Data Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertData() {

        Student newStudent = new Student();

        String valueName = name.getText().toString();
        String valueAddress = address.getText().toString();

        if (!valueAddress.equals("") && !valueName.equals("")) {
            newStudent.setName(valueName);
            newStudent.setAddress(valueAddress);

            mDatabaseReference.push().setValue(newStudent);
            Toast.makeText(MainActivity.this, "Saving Data is Sucessfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateData() {
        Student updatedData = new Student();

        String valueName = nameUpdate.getText().toString();
        String valueAddress = addressUpdate.getText().toString();

        updatedData.setName(valueName);
        updatedData.setAddress(valueAddress);

        // After update lalu di add di db Firebase
        mDatabaseReference.child(keyStudent).setValue(updatedData);
    }

    public void readData() {

        mStudent = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        keyStudent = currentData.getKey();
                        mStudent.setName(currentData.child("name").getValue().toString());
                        mStudent.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                nameUpdate.setText(mStudent.getName());
                addressUpdate.setText(mStudent.getAddress());

                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.crud_citizen){
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.crud_mahasiswa) {
            startActivity(new Intent(this, MainActivity2.class));
        }

        return true;
    }
}