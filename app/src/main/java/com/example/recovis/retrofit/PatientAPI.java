package com.example.recovis.retrofit;

import com.example.recovis.model.Comment;
import com.example.recovis.model.Eav;
import com.example.recovis.model.PatientProfile;
import com.example.recovis.model.YearlyFields;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PatientAPI {


    @GET("/patient/get-patient-id")
    Call<String> searchPatientID(@Query("username") String username, @Query("password") String password);

    @GET("/patient_profile/get-profile")
    Call<ArrayList<PatientProfile>> getPatientProfile(@Query("patient_id") String patient_id);

    @GET("/yearly_fields/get-yearly-fields")
    Call<YearlyFields> getYearlyFields(@Query("patient_id") String patient_id);

    @POST("/eav/save-eav")
    Call<Void> postEav(@Body ArrayList<Eav> eav);

    @POST("/comment/save-comment")
    Call<Void> postComment(@Body Comment comment);

    @POST("/comment/save-daily-comment")
    Call<Integer> postDailyComment(@Body Comment comment);

    @POST("/yearly_fields/save")
    Call<Void> postYearlyFields(@Body YearlyFields yearlyFields);
}
