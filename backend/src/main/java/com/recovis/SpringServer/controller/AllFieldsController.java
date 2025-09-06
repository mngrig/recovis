package com.recovis.SpringServer.controller;


import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.all_fields.AllFieldsDao;
import com.recovis.SpringServer.model.group_fields.GroupFields;
import com.recovis.SpringServer.model.group_fields.GroupFieldsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AllFieldsController {

    @Autowired
    private AllFieldsDao allFieldsDao;

    @Autowired
    private GroupFieldsDao groupFieldsDao;

    @GetMapping("/fields/get-all")
    public List<AllFields> getAllFields(){
        return allFieldsDao.getAllFields();
    }

    @GetMapping("/fields/get-all-unique")
    public List<AllFields> getAllUniqueFields(){

        List<GroupFields> groupFields = groupFieldsDao.getGroupFields();
        List<AllFields> allFields = allFieldsDao.getAllFields();

        for(int i = 0 ; i < groupFields.size(); i++)
        {
            for(int j = 0; j < allFields.size(); j++) {
                if (groupFields.get(i).getField().getField_id() == allFields.get(j).getField_id() || allFields.get(j).getType().equals("computed")) {
                    allFields.remove(j);
                }
            }
        }
        return allFields;
    }
}
