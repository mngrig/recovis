package com.example.recovis;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.recovis.databinding.FragmentDailyBinding;
import com.example.recovis.model.Comment;
import com.example.recovis.model.Eav;
import com.example.recovis.model.EavID;
import com.example.recovis.model.PatientProfile;
import com.example.recovis.retrofit.PatientAPI;
import com.example.recovis.retrofit.RetrofitService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyFieldsFragment extends Fragment {

    private FragmentDailyBinding binding;

    ArrayList<PatientProfile> optionalFields;
    ArrayList<PatientProfile> requiredFields;

    String patient_id;

    RetrofitService retrofitService;
    PatientAPI patientAPI;

    FieldAdapter requiredFieldsAdapter;

    FieldAdapter optionalFieldsAdapter;


    public DailyFieldsFragment(ArrayList<PatientProfile> requiredFields,
                               ArrayList<PatientProfile> optionalFields){
        this.requiredFields = requiredFields;
        this.optionalFields = optionalFields;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitService = new RetrofitService();
        patientAPI = retrofitService.getRetrofit().create(PatientAPI.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDailyBinding.inflate(inflater,container,false);
        patient_id = getArguments().getString("patient_id");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StaggeredGridLayoutManager requiredFieldsManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        StaggeredGridLayoutManager optionalFieldsManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        binding.requiredFields.setLayoutManager(requiredFieldsManager);
        requiredFieldsAdapter = new FieldAdapter(requiredFields);
        binding.requiredFields.setAdapter(requiredFieldsAdapter);

        binding.optionalFields.setLayoutManager(optionalFieldsManager);
        optionalFieldsAdapter = new FieldAdapter(optionalFields);
        binding.optionalFields.setAdapter(optionalFieldsAdapter);


        //when save button is clicked
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Eav> eavs = populateEAV();

                //Daily comment
                String comment_str = binding.dailyCommentText.getText().toString();
                // if it's not empty we have to post EAVS inside call as it is an asynchronous call
                if(!TextUtils.isEmpty(comment_str)){
                    Comment dailyComment = new Comment(patient_id,null,null,comment_str);
                    patientAPI.postDailyComment(dailyComment).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            // we get the auto-incremented comment_id primary key and save its value in the EAV
                            Integer comment_id = response.body();
                            EavID eavID = new EavID(patient_id, new Date(), 13); // field_id = 13 - daily comment
                            Eav eav = new Eav(eavID, comment_id.toString());
                            eavs.add(eav);
                            postEAVs(eavs);
                        }
                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(getContext(), "Save successful!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // if comment is empty just post the all other fields
                else {
                    postEAVs(eavs);
                }
            }
        });
    }

    public ArrayList<Eav> populateEAV(){

        ArrayList<Eav> eavs = new ArrayList<>();

        // required fields
        for(int i = 0; i < requiredFieldsAdapter.getItemCount(); i++){
            FieldAdapter.ViewHolder viewHolder = (FieldAdapter.ViewHolder) binding.requiredFields.findViewHolderForAdapterPosition(i);
            if(viewHolder != null){
                String value = viewHolder.getTextInputEditTextValue();
                if(!Objects.equals(value, "")) {
                    EavID eavID = new EavID(patient_id, new Date(), requiredFields.get(i).getField().getField_id());
                    Eav eav = new Eav(eavID, value);
                    eavs.add(eav);
                }
            }
        }
        //optional fields
        for(int i = 0; i < optionalFieldsAdapter.getItemCount(); i++){
            FieldAdapter.ViewHolder viewHolder = (FieldAdapter.ViewHolder) binding.optionalFields.findViewHolderForAdapterPosition(i);
            if(viewHolder != null){
                String value = viewHolder.getTextInputEditTextValue();
                if(!Objects.equals(value, "")) {
                    EavID eavID = new EavID(patient_id, new Date(), optionalFields.get(i).getField().getField_id());
                    Eav eav = new Eav(eavID, value);
                    eavs.add(eav);
                }
            }
        }
        return eavs;
    }

    public void postEAVs(ArrayList<Eav> eavs){

        if (!eavs.isEmpty()){
            patientAPI.postEav(eavs).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Save successful!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
