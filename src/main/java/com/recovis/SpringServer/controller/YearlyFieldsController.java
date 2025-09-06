package com.recovis.SpringServer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recovis.SpringServer.model.patient.PatientDao;
import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import com.recovis.SpringServer.model.patient_profile.PatientProfileDao;
import com.recovis.SpringServer.model.yearly_fields.YearlyFields;
import com.recovis.SpringServer.model.yearly_fields.YearlyFieldsDao;
import com.recovis.SpringServer.model.yearly_fields.YearlyFieldsID;
import com.recovis.SpringServer.model.yearly_fields.YearlyFieldsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@RestController
public class YearlyFieldsController {

    @Autowired
    private YearlyFieldsDao yearlyFieldsDao;

    @Autowired
    private PatientDao patientDao;

    @GetMapping("/yearly_fields/get-yearly-fields")
    public YearlyFields getYearlyFields(@RequestParam String patient_id) {

        // new year
        if(yearlyFieldsDao.getYearlyFields(patient_id) == null){
            YearlyFieldsID yearlyFieldsID = new YearlyFieldsID(patient_id, Year.now().getValue());
            YearlyFields yearlyFields = new YearlyFields(yearlyFieldsID,0,0,0,patientDao.getPatient(patient_id).get());
            yearlyFieldsDao.save(yearlyFields);
        }
        return yearlyFieldsDao.getYearlyFields(patient_id);
    }

    @PostMapping("/yearly_fields/save")
    public void save(@RequestBody YearlyFields yearlyFields) {
         yearlyFields.setPatient(patientDao.getPatient(yearlyFields.getId().getPatient_id()).get());
         yearlyFieldsDao.save(yearlyFields);
    }




}
