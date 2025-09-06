package com.recovis.SpringServer.model.comment;


import com.recovis.SpringServer.model.eav.Eav;
import com.recovis.SpringServer.model.eav.EavRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommentDao {

    @Autowired
    private CommentRepository repository;

    public void save(Comment comment){
        repository.save(comment);
    }
}
