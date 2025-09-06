package com.recovis.SpringServer.controller;


import com.recovis.SpringServer.model.comment.Comment;
import com.recovis.SpringServer.model.comment.CommentDao;
import com.recovis.SpringServer.model.eav.Eav;
import com.recovis.SpringServer.model.patient.Patient;
import com.recovis.SpringServer.model.patient.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CommentController {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PatientDao patientDao;

    @PostMapping("/comment/save-comment")
    public void save(@RequestBody Comment comment){

        Comment final_comment = new Comment();
        Patient p = patientDao.getPatient(comment.getPatient_id()).get();
        final_comment.setPatient_id(comment.getPatient_id());
        final_comment.setPatient(p);
        final_comment.setComment_text(comment.getComment_text());
        final_comment.setFrom_date(comment.getFrom_date());
        final_comment.setTo_date(comment.getTo_date());

        commentDao.save(final_comment);
    }


    // for daily comments we want to return the ID value so that it can be saved later like EAV.val = Comment.comment_id
    @PostMapping("/comment/save-daily-comment")
    public Integer saveDaily(@RequestBody Comment comment){

        Comment final_comment = new Comment();
        Patient p = patientDao.getPatient(comment.getPatient_id()).get();
        final_comment.setPatient_id(comment.getPatient_id());
        final_comment.setPatient(p);
        final_comment.setComment_text(comment.getComment_text());
        final_comment.setFrom_date(comment.getFrom_date());
        final_comment.setTo_date(comment.getTo_date());
        commentDao.save(final_comment);

        return final_comment.getComment_id();
    }
}
