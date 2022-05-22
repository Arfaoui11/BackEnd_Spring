package com.spring.pidev.repo;

import com.spring.pidev.model.QuestionCourses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionRepo extends CrudRepository<QuestionCourses,Integer> {
}
