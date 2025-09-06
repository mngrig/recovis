package com.recovis.SpringServer.controller;

import com.recovis.SpringServer.model.all_fields.AllFieldsDao;
import com.recovis.SpringServer.model.patient.Patient;
import com.recovis.SpringServer.model.patient.PatientDao;
import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import com.recovis.SpringServer.model.patient_profile.PatientProfileDao;
import com.recovis.SpringServer.model.patient_profile.PatientProfileID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PatientProfileController {

    @Autowired
    private PatientProfileDao patientProfileDao;

    @Autowired
    private PatientDao patientDao;

    @GetMapping("/patient_profile/get-all")
    public List<PatientProfile> getAllPatients(){
        return patientProfileDao.getAllPatientProfiles();
    }

    @GetMapping("/patient_profile/get-profile")
    public List<PatientProfile> getPatientProfile(@RequestParam String patient_id) {
        return patientProfileDao.getPatientProfile(patient_id);
    }

    @PostMapping("/patient_profile/save-profile")
    public ResponseEntity<String> saveAll(@RequestParam String patient_id, @RequestBody List<PatientProfile> patientProfile) {

        for(int i = 0 ; i < patientProfile.size(); i++)
        {
            patientProfile.get(i).setPatient(patientDao.getPatient(patient_id).get());
            PatientProfileID patientProfileID = new PatientProfileID(patient_id,patientProfile.get(i).getField().getField_id());
            patientProfile.get(i).setId(patientProfileID);
        }

        //delete old patient fields
        List<PatientProfile> prevPatientProfile = getPatientProfile(patient_id);
        if(!patientProfile.isEmpty()) {
            for (PatientProfile currentProfile : patientProfile) {
                for (PatientProfile oldProfile : prevPatientProfile) {
                    if (!oldProfile.getField().equals(currentProfile.getField())) {
                        patientProfileDao.delete(oldProfile);
                    }
                }
            }
        }
        // if patientprofile is empty, it means that the profile is closed
        else
        {
            for (PatientProfile profile : prevPatientProfile) {
                patientProfileDao.delete(profile);
            }
        }

        String answer = patientProfileDao.saveAll(patientProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }
}
