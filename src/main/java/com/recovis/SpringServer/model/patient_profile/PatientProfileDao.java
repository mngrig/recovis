package com.recovis.SpringServer.model.patient_profile;

import com.recovis.SpringServer.model.all_fields.AllFieldsRepository;
import com.recovis.SpringServer.model.group_fields.GroupFields;
import com.recovis.SpringServer.model.group_fields.GroupFieldsDao;
import com.recovis.SpringServer.model.group_fields.GroupFieldsID;
import com.recovis.SpringServer.model.group_fields.GroupFieldsRepository;
import com.recovis.SpringServer.model.patient.PatientDao;
import com.recovis.SpringServer.model.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientProfileDao {


    @Autowired
    private PatientProfileRepository patientProfile_rep;

    @Autowired
    private PatientRepository patient_rep;

    @Autowired
    private AllFieldsRepository allFields_rep;

    @Autowired
    private GroupFieldsRepository groupFieldsRepository;


    public PatientProfile save(PatientProfile patientProfile){

        return patientProfile_rep.save(patientProfile);
    }

    public String saveAll(List<PatientProfile> patientProfile){
        return patientProfile_rep.saveAll(patientProfile).toString();
    }


    public void delete(PatientProfile patientProfile){
        patientProfile_rep.delete(patientProfile);
    }

    public List<PatientProfile> getAllPatientProfiles(){
        List<PatientProfile> patients = new ArrayList<>();
        Streamable.of(patientProfile_rep.findAll())
                .forEach(patients::add);
        return patients;
    }

    public List<PatientProfile> getPatientProfile(String patient_id){

        List<PatientProfile> patientFields = new ArrayList<>();

        //we return only the fields that don't exist in group_fields table
        for(int i = 1; i <= allFields_rep.count(); i++){
            PatientProfileID patientProfileID = new PatientProfileID(patient_id, i);
            Optional<PatientProfile> patientProfile= patientProfile_rep.findById(patientProfileID);
            if(patientProfile.isPresent()){
                    patientFields.add(patientProfile.get());
                }
        }

        return patientFields;
    }
}
