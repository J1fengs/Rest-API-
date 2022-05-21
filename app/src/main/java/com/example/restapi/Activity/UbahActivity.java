package com.example.restapi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNama, xNim, xJurusan;
    private EditText etNama, etNim, etJurusan;
    private Button btnUbah;
    private String yNama, yNim, yJurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNama = terima.getStringExtra("xNama");
        xNim = terima.getStringExtra("xNim");
        xJurusan = terima.getStringExtra("xJurusan");

        etNama = findViewById(R.id.etUbahNama);
        etNim = findViewById(R.id.etUbahNim);
        etJurusan = findViewById(R.id.etUbahJurusan);
        btnUbah = findViewById(R.id.btSaveEdit);

        etNama.setText(xNama);
        etNim.setText(xNim);
        etJurusan.setText(xJurusan);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNama = etNama.getText().toString();
                yNim = etNim.getText().toString();
                yJurusan = etJurusan.getText().toString();

                updateData();
            }
        });


    }

    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yNama, yNim, yJurusan);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode :"  + kode +  " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}