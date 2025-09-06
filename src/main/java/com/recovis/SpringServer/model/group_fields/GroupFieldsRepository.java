package com.recovis.SpringServer.model.group_fields;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupFieldsRepository extends CrudRepository<GroupFields, GroupFieldsID> {
}
