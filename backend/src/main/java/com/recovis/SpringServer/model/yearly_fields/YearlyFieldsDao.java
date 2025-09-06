package com.recovis.SpringServer.model.yearly_fields;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recovis.SpringServer.model.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.Year;
import java.util.Calendar;
import java.util.Optional;

@Service
public class YearlyFieldsDao {


    @Autowired
    private YearlyFieldsRepository yearlyFieldsRepository;

    public YearlyFields getYearlyFields(String patient_id) {
        YearlyFieldsID yearlyFieldsID = new YearlyFieldsID(patient_id, Year.now().getValue());
        Optional<YearlyFields> yearlyFields = yearlyFieldsRepository.findById(yearlyFieldsID);
        return yearlyFields.orElse(null);
    }

    public void save(YearlyFields yearlyFields){
        yearlyFieldsRepository.save(yearlyFields);
    }
}