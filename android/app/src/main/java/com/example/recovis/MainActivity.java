package com.example.recovis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.recovis.databinding.ActivityMainBinding;
import com.example.recovis.model.PatientProfile;
import com.example.recovis.retrofit.PatientAPI;
import com.example.recovis.retrofit.RetrofitService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    ArrayList<PatientProfile> requiredFields = new ArrayList<>();
    ArrayList<PatientProfile> optionalFields = new ArrayList<>();

    SharedPreferences sharedPreferences;

    ArrayList<PatientProfile> fields;

    String patient_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fields = (ArrayList<PatientProfile>) getIntent().getSerializableExtra("fields");
        patient_id = (String) getIntent().getSerializableExtra("patient_id");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RetrofitService retrofitService = new RetrofitService();
        PatientAPI patientAPI = retrofitService.getRetrofit().create(PatientAPI.class);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        // if the user is already logged in, get his profile
        if(!sharedPreferences.getBoolean("getProfileCalled",false))
        {
            patient_id = sharedPreferences.getString(getString(R.string.patient_id), "");
            patientAPI.getPatientProfile(patient_id).enqueue(new Callback<ArrayList<PatientProfile>>() {
                @Override
                public void onResponse(Call<ArrayList<PatientProfile>> call, Response<ArrayList<PatientProfile>> response) {
                    fields = response.body();

                    if(fields != null) {
                        for (PatientProfile field : fields) {
                            if (field.getRequired() == 1) {
                                requiredFields.add(field);
                            }
                            else if(field.getRequired() == 0)
                                optionalFields.add(field);
                        }
                    }

                }
                @Override
                public void onFailure(Call<ArrayList<PatientProfile>> call, Throwable t) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, t.toString());
                }
            });
        }
        // if he's not we have the fields from loginactivity already
        else{
            if(fields != null) {
                for (PatientProfile field : fields) {
                    if (field.getRequired() == 1) {
                        requiredFields.add(field);
                    }
                    else if(field.getRequired() == 0)
                        optionalFields.add(field);
                }
            }
        }

        DailyFieldsFragment dailyFieldsFragment = new DailyFieldsFragment(requiredFields,optionalFields);
        DateCommentFragment dateCommentFragment = new DateCommentFragment();
        YearlyFieldsFragment yearlyFieldsFragment = new YearlyFieldsFragment();


        Bundle bundle = new Bundle();
        bundle.putString("patient_id", patient_id);
        dailyFieldsFragment.setArguments(bundle);
        loadFragment(dailyFieldsFragment);

        binding.topBar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //logout option
                if(item.getItemId() == R.id.logout){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getResources().getString(R.string.loggedInStatus),"logout");
                    editor.apply();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                return false;
            }
        });
        binding.categories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("patient_id", patient_id);
                    dailyFieldsFragment.setArguments(bundle);
                    loadFragment(dailyFieldsFragment);
                }
                else if(pos == 1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("patient_id", patient_id);
                    dateCommentFragment.setArguments(bundle);
                    loadFragment(dateCommentFragment);
                }
                else if(pos == 2)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("patient_id", patient_id);
                    yearlyFieldsFragment.setArguments(bundle);
                    loadFragment(yearlyFieldsFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void loadFragment(Fragment fragment) {
        //attach fragment
        getSupportFragmentManager().beginTransaction().replace(binding.flFragment.getId(), fragment).commit();
    }
}
