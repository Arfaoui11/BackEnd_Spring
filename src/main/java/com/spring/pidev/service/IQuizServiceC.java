package com.spring.pidev.service;



import com.spring.pidev.model.QuestionCandidacy;
import com.spring.pidev.model.QuizCandidacy;
import com.spring.pidev.model.ResultQuiz;

import java.util.List;




public interface IQuizServiceC {


    void addQuiz(QuizCandidacy quiz, Integer idF);
    void addQuestionAndAsigntoQuiz(QuestionCandidacy question, Integer idQuiz);
   // void addAnswerAndAsigntoQuestion(Answer answer,Integer idQuestion,Integer idQuiz);

    List<QuestionCandidacy> getQuizQuestion(Integer idQuiz);
    List<QuestionCandidacy> getQuestions();
    //int getResult(QuestionCandidacy questionCandidacy);

    Integer SaveScore (ResultQuiz result, Long idUser, Integer idQuiz);
    void DeleteQuiz(Integer idQuiz);
  /*
    Integer MaxScoreInCandidacy();

    List<Object> getCandidacyWithScoreQuiz(@Param("id") Integer id);

    List<Object> getCandidacytWithScoreQuiz(Integer id);

    List<ResultQuiz> getTopScore();

    Integer getScore(Long idU);

    List<QuizCandidacy> getQuizByCandidacy(Integer idF);




    Integer getScoreByUser(Integer idUser);


 */
}