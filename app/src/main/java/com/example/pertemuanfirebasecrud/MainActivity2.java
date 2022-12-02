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

public class MainActivity2 extends AppCompatActivity {

    EditText name, nameUpdate, nim, nimUpdate, prodi, prodiUpdate;
    Button update, insert, showData, delete;

    DatabaseReference mDatabaseReference;

    Mahasiswa mahasiswa;

    String keyMahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name = findViewById(R.id.et_name_mhs);
        nameUpdate = findViewById(R.id.et_update_name_mhs);
        nim = findViewById(R.id.et_nim);
        nimUpdate = findViewById(R.id.et_update_nim);
        prodi = findViewById(R.id.et_prodi);
        prodiUpdate = findViewById(R.id.et_update_prodi);
        update = findViewById(R.id.btn_update_mhs);
        insert = findViewById(R.id.btn_insert_mhs);
        showData = findViewById(R.id.btn_read_mhs);
        delete = findViewById(R.id.btn_delete_mhs);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Mahasiswa.class.getSimpleName());

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
                mDatabaseReference.child(keyMahasiswa).removeValue();
                Toast.makeText(MainActivity2.this, "Delete Data Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertData() {

        Mahasiswa newMahasiswa = new Mahasiswa();

        String valueName = name.getText().toString();
        String valueNIM = nim.getText().toString();
        String valueProdi = prodi.getText().toString();


        if (!valueNIM.equals("") && !valueName.equals("") && !valueProdi.equals("")) {
            newMahasiswa.setNama(valueName);
            newMahasiswa.setNim(valueNIM);
            newMahasiswa.setProdi(valueProdi);

            mDatabaseReference.push().setValue(newMahasiswa);
            Toast.makeText(MainActivity2.this, "Saving Data is Sucessfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateData() {
        Mahasiswa updatedData = new Mahasiswa();

        String valueName = nameUpdate.getText().toString();
        String valueNIM = nimUpdate.getText().toString();
        String valueProdi = prodiUpdate.getText().toString();

        updatedData.setNama(valueName);
        updatedData.setNim(valueNIM);
        updatedData.setProdi(valueProdi);

        // After update lalu di add di db Firebase
        mDatabaseReference.child(keyMahasiswa).setValue(updatedData);
    }

    public void readData() {

        mahasiswa = new Mahasiswa();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        keyMahasiswa = currentData.getKey();
                        mahasiswa.setNama(currentData.child("nama").getValue().toString());
                        mahasiswa.setNim(currentData.child("nim").getValue().toString());
                        mahasiswa.setProdi(currentData.child("prodi").getValue().toString());
                    }
                }

                nameUpdate.setText(mahasiswa.getNama());
                nimUpdate.setText(mahasiswa.getNim());
                prodiUpdate.setText(mahasiswa.getProdi());


                Toast.makeText(MainActivity2.this, "Data has been shown!", Toast.LENGTH_SHORT).show();

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