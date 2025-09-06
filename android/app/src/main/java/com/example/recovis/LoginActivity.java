package com.example.recovis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recovis.databinding.ActivityLoginBinding;
import com.example.recovis.model.PatientProfile;
import com.example.recovis.retrofit.PatientAPI;
import com.example.recovis.retrofit.RetrofitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        java.lang.String loginStatus = sharedPreferences.getString(getResources().getString(R.string.loggedInStatus), "");
        if (loginStatus.equals("loggedin")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("getProfileCalled", false);
            editor.apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        RetrofitService retrofitService = new RetrofitService();
        PatientAPI patientAPI = retrofitService.getRetrofit().create(PatientAPI.class);


        binding.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();

                if (username.isEmpty()) {
                    binding.username.setBackgroundColor(Color.RED);
                }
                if (password.isEmpty()) {
                    binding.password.setBackgroundColor(Color.RED);
                }
                if (!username.isEmpty() && !password.isEmpty()) {

                    // search for patient's id
                    patientAPI.searchPatientID(username,password).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String patient_id = response.body();
                            if(!(patient_id == null))
                            {
                                //if it exists then get his profile and load main activity
                                patientAPI.getPatientProfile(patient_id).enqueue(new Callback<ArrayList<PatientProfile>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<PatientProfile>> call, Response<ArrayList<PatientProfile>> response) {
                                        ArrayList<PatientProfile> patientFields = response.body();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        i.putExtra("fields",patientFields);
                                        i.putExtra("patient_id",patient_id);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        if (binding.checkbox.isChecked()) {
                                            editor.putString(getResources().getString(R.string.loggedInStatus), "loggedin");
                                            editor.putString(getResources().getString(R.string.patient_id), patient_id);
                                            editor.putBoolean("getProfileCalled", true);
                                        } else {
                                            editor.putString(getResources().getString(R.string.loggedInStatus), "loggedout");
                                        }
                                        editor.apply();
                                        startActivity(i);
                                    }
                                    @Override
                                    public void onFailure(Call<ArrayList<PatientProfile>> call, Throwable t) {
                                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, t.toString());
                                    }
                                });
                            }
                            else
                            {
                                Toast toast = Toast.makeText(LoginActivity.this, "Wrong credentials!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, t.toString());
                        }
                    });
                }
            }
        });
    }
}