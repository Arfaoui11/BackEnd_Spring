package com.spring.pidev.repo;

import com.spring.pidev.model.Comment;
import org.springframework.data.repository.CrudRepository;


public interface ICommentRepo extends CrudRepository<Comment, Integer> {


}
