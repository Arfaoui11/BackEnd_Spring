package com.spring.pidev.service;

import com.spring.pidev.model.ReactComment;
import com.spring.pidev.model.TypeRating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReactCommentService {

   public void addReact (ReactComment reactComment);
    public void addReactAndAffectToUserAndComment(TypeRating typeRating, Long idUser, Integer idComment);
    List<ReactComment> getReactByCommentAndUser(Integer idComment , Long idUser  );

}
