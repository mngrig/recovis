package com.recovis.SpringServer.model.all_fields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AllFieldsDao {

    @Autowired
    private AllFieldsRepository repository;

    public AllFields save(AllFields field){
        return repository.save(field);
    }

    public Optional<AllFields> getField(Integer field_id){ return repository.findById(field_id); }

    public List<AllFields> getAllFields(){
        List<AllFields> allFields = new ArrayList<>();
        Streamable.of(repository.findAll())
                .forEach(allFields::add);

        return allFields;
    }
}
