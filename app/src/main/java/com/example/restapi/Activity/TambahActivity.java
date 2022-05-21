package com.example.restapi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restapi.API.APIRequestData;
import com.example.restapi.API.RetroServer;
import com.example.restapi.Model.ResponseModel;
import com.example.restapi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText etNama, etNim, etJurusan;
    private Button saveDataMahasiswa;
    private String nama, nim, jurusan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.editTextNama);
        etNim = findViewById(R.id.editTextNim);
        etJurusan = findViewById(R.id.editTextJurusan);
        saveDataMahasiswa = findViewById(R.id.button);

        saveDataMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                nim = etNim.getText().toString();
                jurusan = etJurusan.getText().toString();

                if(nama.trim().equals("")){
                    etNama.setError("Nama Harus Diisi");
                }else if(nim.trim().equals("")){
                    etNim.setError("NIM Harus Diisi");
                }else if(jurusan.trim().equals("")){
                    etJurusan.setError("Jurusan Harus Diisi");
                }else{
                    createData();
                }
            }
        });
    }

    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(nama, nim, jurusan);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode :"  + kode +  " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}