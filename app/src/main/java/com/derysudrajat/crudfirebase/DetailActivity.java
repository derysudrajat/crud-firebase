package com.derysudrajat.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_EXTRA = "detail_extra";
    public static final int REQ_CODE_DETAIL = 1;

    @BindView(R.id.et_nomor)
    EditText etNomor;
    @BindView(R.id.et_name)
    EditText etNama;
    @BindView(R.id.et_date)
    EditText etDate;
    @BindView(R.id.et_jk)
    EditText etJk;
    @BindView(R.id.et_alamat)
    EditText etAlamat;
    @BindView(R.id.btn_save_change)
    CardView btnSaveChange;
    @BindView(R.id.tv_btn)
    TextView tvbtn;

    private Crew crew;
    private boolean isUpdate;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (getIntent().getParcelableExtra(DETAIL_EXTRA) != null) {
                isUpdate = true;
                getSupportActionBar().setTitle("Edit Crew");
                crew = getIntent().getParcelableExtra(DETAIL_EXTRA);
                assert crew != null;
                setData(crew);
                tvbtn.setText(R.string.update_crew);
                Log.d("Detail", "data crew: " + crew.getId());
            } else {
                isUpdate = false;
                getSupportActionBar().setTitle("Add Crew");
                tvbtn.setText(R.string.add_crew);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUpdate) getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder;
        if (item.getItemId() == android.R.id.home) {
            if (isValid()){
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Apakah Anda yakin akan membuang data ini?")
                        .setPositiveButton("Buang",(dialog, which) -> finish())
                        .setNegativeButton("Batal",(dialog, which) -> {});
                builder.create().show();
            }else{
                finish();
            }
        } else if (item.getItemId() == R.id.action_delete) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah Anda yakin akan menghapus "+ crew.getNama() +" dari Crew?")
                    .setPositiveButton("Hapus",(dialog, which) -> {
                        Toast.makeText(this, crew.getNama() + " Telah dihapus", Toast.LENGTH_SHORT).show();
                        //db.crewDao().deleteCrew(crew);
                        myRef.child("crew").child(crew.getId()).removeValue();
                        startActivityForResult(new Intent(this, MainActivity.class), REQ_CODE_DETAIL);
                    })
                    .setNegativeButton("Batal",(dialog, which) -> {});
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save_change)
    void onClickButtonSaveChange() {
        if (isUpdate) {
            if (isValid()) {
                /*
                 * Get Data Form Edit Text
                 */
                String id = crew.getId();
                Log.d("Detail", "data crew 2: " + id);
                Crew mCrew = new Crew();
                mCrew.setId(crew.getId());
                mCrew.setNama(etNama.getText().toString());
                mCrew.setNomor(Integer.parseInt(etNomor.getText().toString()));
                mCrew.setDate(etDate.getText().toString());
                mCrew.setJk(etJk.getText().toString());
                mCrew.setAlamat(etAlamat.getText().toString());
                /*
                 * Set Firebase Reference
                 */
                myRef.child("crew").child(id).setValue(mCrew);
                Toast.makeText(this, "Data Was Update", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(this, MainActivity.class), REQ_CODE_DETAIL);
            }

        } else {
            if (isValid()) {
                Crew crew = new Crew();
                String id = myRef.child("crew").push().getKey();
                crew.setId(id);
                crew.setNama(etNama.getText().toString());
                crew.setNomor(Integer.parseInt(etNomor.getText().toString()));
                crew.setDate(etDate.getText().toString());
                crew.setJk(etJk.getText().toString());
                crew.setAlamat(etAlamat.getText().toString());
                //db.crewDao().insertCrew(crew);
                myRef.child("crew").child(id).setValue(crew);
                Toast.makeText(this, "Data Was Inserted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please Fill Form", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setData(Crew data) {
        etNomor.setText(String.valueOf(data.getNomor()));
        etNama.setText(data.getNama());
        etDate.setText(data.getDate());
        etJk.setText(data.getJk());
        etAlamat.setText(data.getAlamat());
    }

    private boolean isValid() {
        return !TextUtils.isEmpty(etNomor.getText().toString()) && !TextUtils.isEmpty(etNama.getText().toString()) &&
                !TextUtils.isEmpty(etDate.getText().toString()) && !TextUtils.isEmpty(etJk.getText().toString()) &&
                !TextUtils.isEmpty(etAlamat.getText().toString());
    }
}
