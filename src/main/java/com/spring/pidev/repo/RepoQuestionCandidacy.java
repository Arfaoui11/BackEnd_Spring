package com.spring.pidev.repo;


import com.spring.pidev.model.QuestionCandidacy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RepoQuestionCandidacy extends CrudRepository<QuestionCandidacy, Integer> {
}
