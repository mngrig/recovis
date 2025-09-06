package com.example.recovis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recovis.databinding.FragmentDateCommentBinding;
import com.example.recovis.databinding.FragmentYearlyFieldsBinding;
import com.example.recovis.model.YearlyFields;
import com.example.recovis.model.YearlyFieldsID;
import com.example.recovis.retrofit.PatientAPI;
import com.example.recovis.retrofit.RetrofitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YearlyFieldsFragment extends Fragment {

    private FragmentYearlyFieldsBinding binding;

    RetrofitService retrofitService;
    PatientAPI patientAPI;

    YearlyFields yearlyFields;

    public YearlyFieldsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentYearlyFieldsBinding.inflate(getLayoutInflater());

        retrofitService = new RetrofitService();
        patientAPI = retrofitService.getRetrofit().create(PatientAPI.class);

        String patient_id = getArguments().getString("patient_id");
        patientAPI.getYearlyFields(patient_id).enqueue(new Callback<YearlyFields>() {
            @Override
            public void onResponse(Call<YearlyFields> call, Response<YearlyFields> response) {
                yearlyFields = response.body();
                if (yearlyFields.toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Empty profile!", Toast.LENGTH_SHORT).show();

                }
                binding.year.setText("Εξετάσεις έτους: " + yearlyFields.getYearlyFieldsID().getYeardate());
                if (yearlyFields.heart_triplex == 1) {
                    binding.checkboxTriplex.setChecked(true);
                }
                if (yearlyFields.abdominal_us == 1) {
                    binding.checkboxUs.setChecked(true);
                }
                if (yearlyFields.dxa == 1) {
                    binding.checkboxDxa.setChecked(true);
                }
            }
            @Override
            public void onFailure(Call<YearlyFields> call, Throwable t) {
                Logger.getLogger(getContext().getClass().getName()).log(Level.SEVERE, "Error occurred", t);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.checkboxTriplex.isChecked()){
                    yearlyFields.heart_triplex = 1;
                }
                else{
                    yearlyFields.heart_triplex = 0;
                }
                if(binding.checkboxUs.isChecked()){
                    yearlyFields.abdominal_us = 1;
                }
                else{
                    yearlyFields.abdominal_us = 0;

                }
                if(binding.checkboxDxa.isChecked()){
                    yearlyFields.dxa = 1;
                }
                else{
                    yearlyFields.dxa = 0;
                }
                patientAPI.postYearlyFields(yearlyFields).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Save successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Logger.getLogger(getContext().getClass().getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
            }
        });
    }
}
