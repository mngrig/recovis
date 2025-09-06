package com.recovis.SpringServer.controller;

import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.all_fields.AllFieldsDao;
import com.recovis.SpringServer.model.eav.Eav;
import com.recovis.SpringServer.model.eav.EavDao;
import com.recovis.SpringServer.model.eav.EavID;
import com.recovis.SpringServer.model.patient.Patient;
import com.recovis.SpringServer.model.patient.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class EavController {

    @Autowired
    private EavDao eavDao;

    @Autowired
    private AllFieldsDao allFieldsDao;

    @Autowired
    private PatientDao patientDao;

    // Endpoint to save a list of Eav objects
    @PostMapping("/eav/save-eav")
    public void save(@RequestBody ArrayList<Eav> eav) {
        // Iterate over the Eav objects to set the patient and field associations
        for (int i = 0; i < eav.size(); i++) {
            String patient_id = eav.get(i).getId().getPatient_id();
            Integer field_id = eav.get(i).getId().getField_id();

            // Set the patient and field associated with the Eav entry
            eav.get(i).setPatient(patientDao.getPatient(patient_id).get());
            eav.get(i).setField(allFieldsDao.getField(field_id).get());
        }
        // Save all Eav entries to the database
        eavDao.saveAll(eav);
    }

    // Endpoint to retrieve all exams for a patient within a date range
    @GetMapping("/eav/get-all-exams")
    public List<Eav> getExams(@RequestParam String patient_id, @RequestParam String start_date, @RequestParam String end_date) {
        return eavDao.getAllExams(patient_id, start_date, end_date);
    }

    // Endpoint to retrieve summarized exams for a patient over a specified period.
    @GetMapping("/eav/get-summarized-exams")
    public List<Eav> getSummarizedExams(@RequestParam String patient_id, @RequestParam String start_date, @RequestParam String end_date, @RequestParam Integer days) {
        return eavDao.getSummarizedExams(patient_id, start_date, end_date, days);
    }

    // Endpoint to retrieve specific exams for a patient, filtered by field IDs and date range.
    @GetMapping("/eav/get-specific-exams")
    public List<Eav> getExams(@RequestParam String patient_id, @RequestParam List<Integer> field_ids, @RequestParam String start_date, @RequestParam String end_date) {
        List<Eav> allEavs = eavDao.getAllEAVs(); // Get all Eav records from the database
        List<Eav> examsValues = new ArrayList<>(); // List to hold filtered Eav records
        Date startDate = Date.valueOf(start_date);
        Date endDate = Date.valueOf(end_date);

        // Handle special case for daily urine percentage (field_id = 16)
        if (field_ids.contains(16)) {
            Float val1 = 0.0F;
            Float val2 = 0.0F;

            // Iterate over all Eav entries to calculate the daily percentage
            for (int i = 0; i < allEavs.size(); i++) {
                Eav entry = allEavs.get(i);
                Date entryDate = entry.getId().getExam_date();

                if (patient_id.equals(entry.getId().getPatient_id()) && entry.getId().getField_id().equals(14)
                        && (entryDate.before(endDate) && entryDate.after(startDate) || entryDate.equals(startDate) || entryDate.equals(endDate))) {
                    val1 = Float.parseFloat(entry.getVal());
                }
                if (patient_id.equals(entry.getId().getPatient_id()) && entry.getId().getField_id().equals(15)
                        && (entryDate.before(endDate) && entryDate.after(startDate) || entryDate.equals(startDate) || entryDate.equals(endDate))) {
                    val2 = Float.parseFloat(entry.getVal());
                }

                // If both required values are available, calculate the percentage
                if (!val1.equals(0.0F) && !val2.equals(0.0F)) {
                    float expressionValue = val1 / val2 * 100;
                    EavID createdID = new EavID(patient_id, entry.getId().getExam_date(), 16);
                    Eav createdEntry = new Eav(createdID, String.valueOf(Math.round(expressionValue)), patientDao.getPatient(patient_id).get(), allFieldsDao.getField(16).get());
                    examsValues.add(createdEntry);
                    val1 = 0.0F;
                    val2 = 0.0F;
                }
            }

            // Remove field_id 16 after calculating the daily percentage
            field_ids.remove(Integer.valueOf(16));
        }

        // Iterate over all Eav entries to filter by field IDs and date range
        for (int i = 0; i < allEavs.size(); i++) {
            Eav entry = allEavs.get(i);
            Date entryDate = entry.getId().getExam_date();

            for (int j = 0; j < field_ids.size(); j++) {
                if (patient_id.equals(entry.getId().getPatient_id()) && field_ids.get(j).equals(entry.getId().getField_id())
                        && (entryDate.before(endDate) && entryDate.after(startDate) || entryDate.equals(startDate) || entryDate.equals(endDate))) {

                    // Special handling for "prograf" field (field_id = 9) to sum parts
                    if (entry.getId().getField_id() == 9) {
                        String val = entry.getVal();
                        if (val != null && val.contains("-")) {
                            String[] parts = val.split("-");
                            try {
                                float part1 = Float.parseFloat(parts[0]);
                                float part2 = Float.parseFloat(parts[1]);
                                float sum = part1 + part2;
                                entry.setVal(String.valueOf(sum));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    examsValues.add(entry);
                }
            }
        }
        return examsValues;
    }
}
