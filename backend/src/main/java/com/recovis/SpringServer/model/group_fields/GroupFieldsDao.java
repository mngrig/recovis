package com.recovis.SpringServer.model.group_fields;

import com.recovis.SpringServer.model.patient.PatientRepository;
import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import com.recovis.SpringServer.model.patient_profile.PatientProfileID;
import com.recovis.SpringServer.model.patient_profile.PatientProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupFieldsDao {

    @Autowired
    private GroupFieldsRepository groupFieldsRepository;

    @Autowired
    private PatientProfileRepository patientProfile_rep;

    public List<GroupFields> getGroupFields(){
        List<GroupFields> groupFields = new ArrayList<>();
        Streamable.of(groupFieldsRepository.findAll())
                .forEach(groupFields::add);
        return groupFields;
    }

    public List<GroupFields> getActivePatientGroupFields(String patient_id){
        List<GroupFields> allGroupFields = new ArrayList<>();
        Streamable.of(groupFieldsRepository.findAll())
                .forEach(allGroupFields::add);

        List<GroupFields> groupFields = new ArrayList<>();
        for(int i = 0; i < allGroupFields.size() - 1; i++){

            PatientProfileID patientProfileID1 = new PatientProfileID(patient_id,allGroupFields.get(i).getField().getField_id());
            PatientProfileID patientProfileID2 = new PatientProfileID(patient_id,allGroupFields.get(i+1).getField().getField_id());

            if(patientProfile_rep.findById(patientProfileID1).isPresent() && patientProfile_rep.findById(patientProfileID2).isPresent()
                    && Objects.equals(allGroupFields.get(i).getId().getGroup_id(), allGroupFields.get(i + 1).getId().getGroup_id()))
            {
                GroupFieldsID groupFieldsID1 = new GroupFieldsID(allGroupFields.get(i).getId().getGroup_id(),allGroupFields.get(i).getField().getField_id());
                GroupFieldsID groupFieldsID2 = new GroupFieldsID(allGroupFields.get(i+1).getId().getGroup_id(),allGroupFields.get(i+1).getField().getField_id());
                groupFields.add(groupFieldsRepository.findById(groupFieldsID1).get());
                groupFields.add(groupFieldsRepository.findById(groupFieldsID2).get());
            }
        }

        return groupFields;
    }
}
