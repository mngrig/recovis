package com.recovis.SpringServer.model.all_fields;

import com.recovis.SpringServer.model.patient_profile.PatientProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllFieldsRepository extends CrudRepository<AllFields, Integer> {



}
