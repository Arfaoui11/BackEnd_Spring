package com.spring.pidev.repo;


import com.spring.pidev.model.QuestionCourses;
import com.spring.pidev.model.QuizCourses;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizRepo extends CrudRepository<QuizCourses,Integer> {

    @Query(value = "select  a from QuizCourses q join q.question a where q.idQuiz=:id")
    List<QuestionCourses> getQuizQuestion(@Param("id") Integer idQ);


    @Query(value = "select  q from QuizCourses q join q.formation f where f.idFormation=:id")
    List<QuizCourses> getQuizByCourses(@Param("id") Integer idF);


    @Query(value = "select q from Result r join r.quiz q join r.sUser u where u.id=:id")
    List<QuizCourses> listQuiqtestedbuUser(@Param("id") Long idU);


  //  @Query(value = "select q from Result r join r.quiz q join r.sUser u where u.id=:id and q.idQuiz <> (select q.idQuiz from QuizCourses q join q.formation f where f.idFormation =:idf)")
   // List<QuizCourses> listQuiqtestedbuUser(@Param("id") Long idU,@Param("idf") Integer idF);

   // @Query(value = "select q from QuizCourses q join q.formation f where f.idFormation=:idf and q.idQuiz=(select q.idQuiz from Result r join r.quiz q join r.sUser u where u.id=:id)")
   // List<QuizCourses> listQuiqtestedbuUser(@Param("id") Long idU,@Param("idf") Integer idF);

}
