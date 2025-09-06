package com.recovis.SpringServer.controller;

import com.recovis.SpringServer.model.patient.Patient;
import com.recovis.SpringServer.model.patient.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {

    @Autowired
    private PatientDao patientDao;

    @GetMapping("/patient/get-all")
    public List<Patient> getAllPatients(){
        return patientDao.getAllPatients();
    }

    @PostMapping("/patient/save-patient")
    public ResponseEntity<Patient> save(@RequestBody Patient patient) {
            Patient savedPatient = patientDao.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }
    @DeleteMapping("/patient/delete")
    public void delete(@RequestParam String patient_id) {
        patientDao.delete(patient_id);
    }

    @GetMapping("/patient/get-patient-id")
    public Optional<String> searchPatientID(@RequestParam String username, @RequestParam String password)
    {
        return patientDao.getPatientID(username,password);
    }

    @GetMapping("/patient/get-patient")
    public Optional<Patient> searchPatient(@RequestParam String patient_id)
    {
        return patientDao.getPatient(patient_id);
    }

}

