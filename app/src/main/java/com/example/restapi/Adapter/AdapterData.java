package com.example.restapi.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restapi.API.APIRequestData;
import com.example.restapi.API.RetroServer;
import com.example.restapi.Activity.MainActivity;
import com.example.restapi.Activity.UbahActivity;
import com.example.restapi.Model.DataModel;
import com.example.restapi.Model.ResponseModel;
import com.example.restapi.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context context;
    private List<DataModel> listData;
    private List<DataModel> listMahasiswa;
    private int idMahasiswa;

    public AdapterData(Context context, List<DataModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dataModel = listData.get(position);

        holder.tvId.setText(String.valueOf(dataModel.getId()));
        holder.tvNama.setText(dataModel.getNama());
        holder.tvNim.setText(dataModel.getNim());
        holder.tvJurusan.setText(dataModel.getJurusan());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvNama, tvNim, tvJurusan;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(context);
                    dialogPesan.setMessage("Pilih Menu : ");
                    dialogPesan.setCancelable(true);

                    idMahasiswa = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ((MainActivity) context).retrieveData();
                        }
                    });

                    dialogPesan.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();

                        }
                    });

                    dialogPesan.show();


                    return false;
                }
            });
        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idMahasiswa);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(context, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idMahasiswa);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listMahasiswa = response.body().getData();

                    int varIdMahasiswa = listMahasiswa.get(0).getId();
                    String varNamaMahasiswa = listMahasiswa.get(0).getNama();
                    String varNimMahasiswa = listMahasiswa.get(0).getNim();
                    String varJurusanMahasiswa = listMahasiswa.get(0).getJurusan();

                    Toast.makeText(context, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();

                    Intent kirim = new Intent(context, UbahActivity.class);
                    kirim.putExtra("xId", varIdMahasiswa);
                    kirim.putExtra("xNama", varNamaMahasiswa);
                    kirim.putExtra("xNim", varNimMahasiswa);
                    kirim.putExtra("xJurusan", varJurusanMahasiswa);
                    context.startActivity(kirim);

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
}
