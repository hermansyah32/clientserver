package com.sharinproject.clientserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TambahActivity extends BaseActivity {

    @BindView(R.id.etNama)
    EditText editTextNama;
    @BindView(R.id.etEmail)
    EditText editTextEmail;
    @BindView(R.id.etPassword)
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSimpan)
    public void onSimpanCLicked(View view){
        showDialog();
        UserModel userModel = new UserModel();
        userModel.setUser_name(editTextNama.getText().toString());
        userModel.setUser_mail(editTextEmail.getText().toString());
        userModel.setUser_password(editTextPassword.getText().toString());

        String jsondata = JSON.toJSONString(userModel);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("user",jsondata)
                .build();
        Request request = new Request.Builder()
                .url(urlBase + "?addMember=ya")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissDialog();
                Log.e("Error Tambah", e.getMessage());
                backgroundThreadShortToast(TambahActivity.this, "" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dismissDialog();
                if (response.isSuccessful()){
                    try {
                        String respon = response.body().string();
                        Log.i("Respon Tambah", "" + respon);
                        if (respon.contains("gagal")){
                            //Respon gagal
                            backgroundThreadShortToast(TambahActivity.this, "Gagal menambahkan data");
                        }else {
                            //Respon berhasil
                            backgroundThreadShortToast(TambahActivity.this, "Berhasil menambahkan data");
                        }
                    }catch (Exception e){
                        Log.e("Respon Tambah", "" + e.getMessage());
                        backgroundThreadShortToast(TambahActivity.this, "" + e.getMessage());
                    }
                }else {
                    Log.w("Respon Tambah", "Respon Gagal");
                    backgroundThreadShortToast(TambahActivity.this, "Respon Gagal");
                }
            }
        });
    }
}
