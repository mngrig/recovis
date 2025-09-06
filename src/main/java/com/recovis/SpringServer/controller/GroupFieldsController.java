package com.recovis.SpringServer.controller;

import com.recovis.SpringServer.model.group_fields.GroupFields;
import com.recovis.SpringServer.model.group_fields.GroupFieldsDao;

import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupFieldsController {

    @Autowired
    private GroupFieldsDao groupFieldsDao;

    @GetMapping("/group_fields/get-all")
    public List<GroupFields> getGroupFields(){
        return groupFieldsDao.getGroupFields();
    }

    @GetMapping("/group_fields/get-profile-grouped")
    public List<GroupFields> getActivePatientGroupFields(@RequestParam String patient_id) {
        return groupFieldsDao.getActivePatientGroupFields(patient_id);
    }
}
