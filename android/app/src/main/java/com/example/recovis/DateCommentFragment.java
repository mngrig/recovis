package com.example.recovis;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recovis.databinding.FragmentDailyBinding;
import com.example.recovis.databinding.FragmentDateCommentBinding;
import com.example.recovis.model.Comment;
import com.example.recovis.retrofit.PatientAPI;
import com.example.recovis.retrofit.RetrofitService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateCommentFragment extends Fragment {

    private FragmentDateCommentBinding binding;

    private MaterialDatePicker<Long> startDatePicker;
    private MaterialDatePicker<Long> endDatePicker;

    String patient_id;

    RetrofitService retrofitService;
    PatientAPI patientAPI;



    public DateCommentFragment(){

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
        binding = FragmentDateCommentBinding.inflate(getLayoutInflater());

        patient_id = getArguments().getString("patient_id");

        startDatePicker = buildDatePicker("Select Start Date", binding.startDateEditText);
        endDatePicker = buildDatePicker("Select End Date", binding.endDateEditText);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set click listeners for TextInputEditText elements
        binding.startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker(v);
            }
        });

        binding.endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker(v);
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start_date_str = binding.startDateEditText.getText().toString();
                String end_date_str = binding.endDateEditText.getText().toString();
                String comment_str = binding.commentText.getText().toString();

                if (TextUtils.isEmpty(start_date_str)) {
                    showHelperText(binding.startDateInputLayout, "Start date cannot be empty");
                } else if (TextUtils.isEmpty(end_date_str)) {
                    showHelperText(binding.endDateInputLayout, "End date cannot be empty");
                } else if (TextUtils.isEmpty(comment_str)) {
                    showHelperText(binding.commentLayout, "Cannot save empty comment");
                } else {
                    // All fields are non-empty, reset helper text
                    resetHelperTexts();

                    try {
                        Date start_date = parseDate(start_date_str);
                        Date end_date = parseDate(end_date_str);

                        if (start_date != null && end_date != null && start_date.after(end_date)) {
                            showHelperText(binding.startDateInputLayout, "Start date is after end date");
                        } else {
                            // Continue with your logic if everything is valid
                            Comment comment = new Comment(patient_id,start_date,end_date,comment_str);
                            patientAPI.postComment(comment).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(), "Save successful!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getContext(), "Save failed!!!", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(getContext().getClass().getName()).log(Level.SEVERE, "Error occurred", t);
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private MaterialDatePicker<Long> buildDatePicker(String title, final TextInputEditText targetTextView) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(title);

        final MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(
                selection -> {
                    targetTextView.setText(formatDate(selection));
                }
        );

        return picker;
    }

    // Function to show the MaterialDatePicker for the start date
    public void showStartDatePicker(View view) {
        startDatePicker.show(getChildFragmentManager(), "startDatePicker");
    }

    // Function to show the MaterialDatePicker for the end date
    public void showEndDatePicker(View view) {
        endDatePicker.show(getChildFragmentManager(), "endDatePicker");
    }

    // Function to format the selected date
    private String formatDate(Long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(milliseconds));
    }

    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateStr);
    }
    private void showHelperText(TextInputLayout layout, String text) {
        layout.setHelperText(text);
        layout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
    }

    private void resetHelperTexts() {
        binding.startDateInputLayout.setHelperTextEnabled(false);
        binding.endDateInputLayout.setHelperTextEnabled(false);
        binding.commentLayout.setHelperTextEnabled(false);
    }
}
