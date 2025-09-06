package com.recovis.SpringServer.model.eav;

import com.recovis.SpringServer.model.all_fields.AllFields;
import com.recovis.SpringServer.model.all_fields.AllFieldsRepository;
import com.recovis.SpringServer.model.comment.CommentRepository;
import com.recovis.SpringServer.model.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class EavDao {

    @Autowired
    private EavRepository eavRepository;

    @Autowired
    private AllFieldsRepository allFieldsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private CommentRepository commentRepository;

    // Method to save a list of Eav objects to the database
    public void saveAll(ArrayList<Eav> eav) {
        eavRepository.saveAll(eav);
    }

    // Method to retrieve all Eav records from the database
    public List<Eav> getAllEAVs() {
        return (List<Eav>) eavRepository.findAll();
    }

    // Method to get the total count of Eav records in the database
    public long getSize() {
        return eavRepository.count();
    }

    // Method to retrieve all exams for a specific patient between two dates
    public List<Eav> getAllExams(String patient_id, String start_date, String end_date) {

        // Fetch all EAVs from the database
        List<Eav> allEavs = getAllEAVs();
        List<Eav> patientEavs = new ArrayList<>();
        Date startDate = Date.valueOf(start_date);
        Date endDate = Date.valueOf(end_date);

        // Filter the EAVs to include only those matching the patient_id and within the date range
        for (int i = 0; i < allEavs.size(); i++) {
            Eav entry = allEavs.get(i);
            Date entryDate = entry.getId().getExam_date();
            if (patient_id.equals(entry.getId().getPatient_id()) &&
                    (entryDate.before(endDate) && entryDate.after(startDate) || entryDate.equals(startDate) || entryDate.equals(endDate))) {

                // Handle special case for daily comment fields
                if (entry.getId().getField_id() == 13) {
                    entry.setVal(commentRepository.findById(Integer.parseInt(entry.getVal())).get().getComment_text());
                }
                patientEavs.add(entry);
            }
        }

        // Fetch all field definitions from the database
        List<AllFields> allFields = new ArrayList<>();
        Streamable.of(allFieldsRepository.findAll()).forEach(allFields::add);

        // Create a map to easily look up the order_id by field_id
        Map<Integer, Integer> fieldIdToOrderId = allFields.stream()
                .collect(Collectors.toMap(AllFields::getField_id, AllFields::getOrder_id));


        // Define regex patterns to detect and handle specific field expressions
        // Computable fields
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(Pattern.compile("%(\\d+)/%(\\d+)\\*(\\d+)")); // Percentage pattern
        patterns.add(Pattern.compile("%(\\d+)#>(\\d+)")); // Count checker pattern

        List<Eav> newEntries = new ArrayList<>();

        // Iterate over all fields to calculate computed values
        for (int i = 0; i < allFields.size(); i++) {
            // Only process fields marked as "computed"
            if (allFields.get(i).getType().equals("computed")) {
                String expression = allFields.get(i).getExpression(); // Get the expression for computation

                // Handle the expression using defined regex patterns
                for (int j = 0; j < patterns.size(); j++) {
                    Matcher matcher = patterns.get(j).matcher(expression);
                    if (matcher.matches()) {
                        System.out.println("Match found for pattern " + j + " in expression: " + expression);

                        // Handle percentage computation (first pattern)
                        if (matcher.groupCount() == 3) {
                            System.out.println("Percentage");
                            int field_id_1 = Integer.parseInt(matcher.group(1));
                            int field_id_2 = Integer.parseInt(matcher.group(2));
                            int multiplier = Integer.parseInt(matcher.group(3));
                            System.out.println(field_id_1);

                            // Daily frequency computation
                            if (allFields.get(i).getFrequency().equals("daily")) {
                                for (int k = 0; k < patientEavs.size(); k++) {
                                    EavID eavID_1 = new EavID(patient_id, patientEavs.get(k).getId().getExam_date(), field_id_1);
                                    EavID eavID_2 = new EavID(patient_id, patientEavs.get(k).getId().getExam_date(), field_id_2);

                                    // If both required EAVs exist, calculate the expression value
                                    if (eavRepository.findById(eavID_1).isPresent()
                                            && eavRepository.findById(eavID_2).isPresent()) {

                                        float expressionValue = Float.parseFloat(eavRepository.findById(eavID_1).get().getVal())
                                                / Float.parseFloat(eavRepository.findById(eavID_2).get().getVal())
                                                * multiplier;

                                        // Create a new Eav entry with the computed value
                                        EavID createdID = new EavID(patient_id, patientEavs.get(k).getId().getExam_date(), allFields.get(i).getField_id());
                                        Eav createdEntry = new Eav(createdID, String.valueOf(Math.round(expressionValue)),
                                                patientRepository.findById(patient_id).get(),
                                                allFields.get(i));
                                        newEntries.add(createdEntry);
                                    }
                                }
                            }
                            // Monthly frequency computation
                            else if (allFields.get(i).getFrequency().equals("monthly")) {
                                System.out.println("Monthly Percentage");

                                Map<YearMonth, List<Eav>> monthlyEntries = new HashMap<>();
                                Map<YearMonth, LocalDate> lastDayOfMonthWithValue = new HashMap<>();

                                // Aggregate EAVs by month
                                for (Eav entry : allEavs) {
                                    if (patient_id.equals(entry.getId().getPatient_id()) &&
                                            entry.getId().getField_id() == field_id_1) {

                                        LocalDate entryDate = entry.getId().getExam_date().toLocalDate();
                                        YearMonth yearMonth = YearMonth.from(entryDate);

                                        monthlyEntries.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(entry);

                                        if (!lastDayOfMonthWithValue.containsKey(yearMonth) ||
                                                entryDate.isAfter(lastDayOfMonthWithValue.get(yearMonth))) {
                                            lastDayOfMonthWithValue.put(yearMonth, entryDate);
                                        }
                                    }
                                }

                                // Calculate the monthly percentage for each month
                                for (Map.Entry<YearMonth, List<Eav>> monthEntry : monthlyEntries.entrySet()) {
                                    YearMonth yearMonth = monthEntry.getKey();
                                    List<Eav> entries = monthEntry.getValue();

                                    float sum1 = 0.0f;
                                    float sum2 = 0.0f;

                                    for (Eav entry : entries) {
                                        EavID eavID_1 = new EavID(patient_id, entry.getId().getExam_date(), field_id_1);
                                        EavID eavID_2 = new EavID(patient_id, entry.getId().getExam_date(), field_id_2);

                                        if (eavRepository.findById(eavID_1).isPresent()
                                                && eavRepository.findById(eavID_2).isPresent()) {
                                            sum1 += Float.parseFloat(eavRepository.findById(eavID_1).get().getVal());
                                            sum2 += Float.parseFloat(eavRepository.findById(eavID_2).get().getVal());
                                        }
                                    }

                                    // If valid data exists, calculate the monthly percentage
                                    if (sum2 != 0) {
                                        float monthlyPercentage = sum1 / sum2 * multiplier;
                                        LocalDate lastDay = lastDayOfMonthWithValue.get(yearMonth);

                                        // Check if the last day of the month is within the start and end date range
                                        if (!lastDay.isBefore(startDate.toLocalDate()) && !lastDay.isAfter(endDate.toLocalDate())) {
                                            EavID createdID = new EavID(patient_id, Date.valueOf(lastDay), allFields.get(i).getField_id());
                                            Eav createdEntry = new Eav(createdID, String.valueOf(Math.round(monthlyPercentage)),
                                                    patientRepository.findById(patient_id).get(),
                                                    allFields.get(i));
                                            if (!createdEntry.getVal().equals("0")) {
                                                newEntries.add(createdEntry);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Handle count checker computation (second pattern)
                        else if (matcher.groupCount() == 2) {
                            System.out.println("Count check");
                        }
                    }
                }
            }
        }

        // Add computed entries to patientEavs and sort by order_id
        patientEavs.addAll(newEntries);
        patientEavs.sort((eav1, eav2) -> {
            int orderId1 = fieldIdToOrderId.getOrDefault(eav1.getId().getField_id(), Integer.MAX_VALUE);
            int orderId2 = fieldIdToOrderId.getOrDefault(eav2.getId().getField_id(), Integer.MAX_VALUE);
            return Integer.compare(orderId1, orderId2);
        });

        return patientEavs;
    }

    // Method to retrieve summarized exams for a patient over a specified period
    public List<Eav> getSummarizedExams(String patient_id, String start_date, String end_date, Integer days) {
        List<Eav> patientEavs = getAllExams(patient_id, start_date, end_date); // Get all exams for the patient
        List<Eav> summarizedEavs = new ArrayList<>();
        Date startDate = Date.valueOf(start_date);
        Date endDate = Date.valueOf(end_date);

        // Fetch all field definitions from the database
        List<AllFields> allFields = new ArrayList<>();
        Streamable.of(allFieldsRepository.findAll()).forEach(allFields::add);

        // Create a map to easily look up the order_id by field_id
        Map<Integer, Integer> fieldIdToOrderId = allFields.stream()
                .collect(Collectors.toMap(AllFields::getField_id, AllFields::getOrder_id));

        // Check if there are any exams for the patient
        if (patientEavs.isEmpty()) {
            System.out.println("No exams found for patient in the given date range.");
            return summarizedEavs;  // Return an empty list since there are no exams
        }

        System.out.println("Patient EAVs count: " + patientEavs.size());

        // Iterate over all fields to calculate summaries for stored fields
        for (AllFields field : allFields) {
            if (field.getType().equals("stored")) {
                Date firstExamDate = patientEavs.get(0).getId().getExam_date(); // Get the first exam date
                Date intervalStartDate = firstExamDate;

                System.out.println("First Exam Date: " + firstExamDate);
                System.out.println("Initial Interval Start Date: " + intervalStartDate);

                // Process intervals until the end date is reached
                while (!intervalStartDate.after(endDate)) {
                    Date intervalEndDate = addDaysToDate(intervalStartDate, days - 1);
                    if (intervalEndDate.after(endDate)) {
                        intervalEndDate = endDate;
                    }

                    System.out.println("Processing interval from " + intervalStartDate + " to " + intervalEndDate);

                    // Get EAVs within the current interval
                    List<Eav> intervalEavs = getIntervalEavs(patientEavs, field.getField_id(), intervalStartDate, intervalEndDate);

                    float sum = 0.0f;
                    int count = 0;

                    // Sum up values for numeric fields
                    for (Eav eav : intervalEavs) {
                        if (isNumeric(eav.getVal())) {
                            sum += Float.parseFloat(eav.getVal());
                            count++;
                            System.out.println("Adding value: " + eav.getVal());
                        }
                    }

                    // If there are valid values, calculate the average and create a summarized EAV entry
                    if (count > 0) {
                        float average = sum / count;
                        EavID createdID = new EavID(patient_id, intervalEndDate, field.getField_id());
                        Eav summarizedEav = new Eav(createdID, String.format("%.1f", average), patientRepository.findById(patient_id).get(), field);
                        summarizedEavs.add(summarizedEav);
                        System.out.println("Added summarized EAV for date " + intervalEndDate + " with average " + average);
                    }

                    // Move to the next interval
                    intervalStartDate = addDaysToDate(intervalStartDate, days);
                    System.out.println("Moving to next interval start date: " + intervalStartDate);
                }
            }
        }

        // Sort summarized entries by order_id
        summarizedEavs.sort((eav1, eav2) -> {
            int orderId1 = fieldIdToOrderId.getOrDefault(eav1.getId().getField_id(), Integer.MAX_VALUE);
            int orderId2 = fieldIdToOrderId.getOrDefault(eav2.getId().getField_id(), Integer.MAX_VALUE);
            return Integer.compare(orderId1, orderId2);
        });

        return summarizedEavs;
    }


    // Helper method to add a specified number of days to a date
    private Date addDaysToDate(Date date, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new Date(calendar.getTimeInMillis());
    }

    // Helper method to filter EAVs within a date range for a specific field
    private List<Eav> getIntervalEavs(List<Eav> eavs, int fieldId, Date start, Date end) {
        return eavs.stream()
                .filter(eav -> eav.getField().getField_id() == fieldId
                        && !eav.getId().getExam_date().before(start)
                        && !eav.getId().getExam_date().after(end))
                .collect(Collectors.toList());
    }

    // Helper method to check if a string can be parsed as a numeric value
    private boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
