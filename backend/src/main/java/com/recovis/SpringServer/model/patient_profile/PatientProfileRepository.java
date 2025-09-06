package com.recovis.SpringServer.model.patient_profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends CrudRepository<PatientProfile, PatientProfileID> {
}
