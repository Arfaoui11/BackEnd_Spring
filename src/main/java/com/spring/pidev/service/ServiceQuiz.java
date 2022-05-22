package com.spring.pidev.service;


import com.spring.pidev.model.*;
import com.spring.pidev.repo.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ServiceQuiz implements IServicesQuiz {

    @Autowired
    private UserRepository iUserRepo;
    @Autowired
    private IFormationRepo iFormationRepo;
    @Autowired
    private IQuestionRepo iQuestionRepo;
    @Autowired
    private IQuizRepo iQuizRepo;
    @Autowired
    private IResultRepo iResultRepo;
    @Autowired
    QuestionCourses question;
    @Autowired
    QuestionForm qForm;
    @Autowired
    Result result;
    @Autowired
    private SendEmailService emailSenderService;
    @Autowired
    exportExcel exportExcelservice;




    @Override
    public void addQuiz(QuizCourses quiz, Integer idF) {
        Formation formation = this.iFormationRepo.findById(idF).orElse(null);
        quiz.setFormation(formation);
     //   quiz.setCreateAt(new Date());
        iQuizRepo.save(quiz);
    }

    @Override
    public void addQuestionAndAsigntoQuiz(QuestionCourses question, Integer idQuiz) {
    QuizCourses quiz = iQuizRepo.findById(idQuiz).orElse(null);

    question.setQuiz(quiz);

    iQuestionRepo.save(question);
    }







    @Override
    public List<QuestionCourses> getQuizQuestion(Integer idQuiz ) {
        List<QuestionCourses> allQues =  iQuizRepo.getQuizQuestion(idQuiz);
        List<QuestionCourses> qList = new ArrayList<>();

        Random random = new Random();

        for(int i=0; i<5; i++) {
            int rand = random.nextInt(allQues.size());
            qList.add(allQues.get(rand));
            allQues.remove(rand);
        }

        return qList;

    }
    @Override
    public List<QuestionCourses> getQuestionByQuiz(Integer idQuiz) {
        List<QuestionCourses> allQues =  iQuizRepo.getQuizQuestion(idQuiz);
        return allQues;
    }


    public List<QuestionCourses> getQuestions() {
        List<QuestionCourses> allQues = (List<QuestionCourses>) iQuestionRepo.findAll();
        List<QuestionCourses> qList = new ArrayList<QuestionCourses>();

        Random random = new Random();

        for(int i=0; i<5; i++) {
            int rand = random.nextInt(allQues.size());
            qList.add(allQues.get(rand));
            allQues.remove(rand);
        }

        return allQues;
    }


    public int getResult(QuestionForm qForm) {
        int correct = 0;

        for(QuestionCourses q: qForm.getQuestions())
            if(q.getAns() == q.getChose())
                correct++;

        return correct;
    }



    @Override
    public Integer saveScore(Result result, Long idUser, Integer idQuiz ) {
        Result saveResult = new Result();

        User user = this.iUserRepo.findById(idUser).orElse(null);
        QuizCourses quiz = this.iQuizRepo.findById(idQuiz).orElse(null);
        if (iResultRepo.findUserQuiz(idUser,idQuiz) == 0)
        {
            saveResult.setSUser(user);
            saveResult.setQuiz(quiz);
            saveResult.setUsername(result.getUsername());
            saveResult.setTotalCorrect(result.getTotalCorrect());
            saveResult.setCorrectAnswer(result.getCorrectAnswer());
            saveResult.setInCorrectAnswer(result.getInCorrectAnswer());
            iResultRepo.save(saveResult);
            return 1;

        }else{
            log.info("This user is tested with this quiz");
            return 0;
        }

    }



    @Override
   // @Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(cron = "0 0 0/4 ? * *") //every day 20:00
    public void giftsToUserMaxScoreInCourses() {
        User user = new User();

        Date date = new Date();
        boolean status=false;

        for(Formation form : iFormationRepo.findAll())
        {
            if(iUserRepo.getApprenantWithScoreForGifts(form.getIdFormation()).size()!=0 )
            {
                user = iUserRepo.getApprenantWithScoreForGifts(form.getIdFormation()).get(0);

                Date tomorrow = new Date(form.getEnd().getTime() + (1000 * 60 * 60 * 48));
                log.info("Date : "+tomorrow);
                if (date.after(form.getEnd()) && date.before(tomorrow) && iResultRepo.getNbrQuiz(user.getId()) == 5)
                {
                    status=true;
                }
                if (status)
                {
                    this.emailSenderService.sendSimpleEmail(user.getEmail(), " we have max Score in courses   ", "Congratulations Mr's : " + user.getLastName() + "--" + user.getFirstName() + " We have Courses free and access in all domain Formation Id : "+ form.getIdFormation() + " .");
                    status=false;
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0/10 * * * *")
    //@Scheduled(cron = "0 0 20 ? * *") //every day 20:00
    public void  ResultQuiz() throws IOException, MessagingException {

        List<Result> r = new ArrayList<>();

        User user = new User();
        Formation form = new Formation();

        for (Formation f : iFormationRepo.findAll()) {

             //   if(iResultRepo.nbrResultByCourses(f.getIdFormation())>=1 && f.getEnd().before(new Date()))
             //   {
                    for (QuizCourses quiz : iQuizRepo.getQuizByCourses(f.getIdFormation()))
                    {
                        for (Result result : iResultRepo.getResultByQuizCourses(quiz.getIdQuiz()))
                        {
                            r.add(result);
                            user = f.getFormateur();
                            form = f;
                        }
                    }
              //  }

        }
        Date tomorow = new Date(form.getEnd().getTime() + (1000 * 60 * 60 * 48));
        Date now = new Date();
        System.out.println(now);
        System.out.println(tomorow);
       if(now.after(form.getEnd()) && now.before(tomorow))
        {
            ByteArrayInputStream stream = exportExcelservice.quizexportexcel(r);

            FileOutputStream out = new FileOutputStream("C:\\Users\\LEGION-5\\IdeaProjects\\newProjet2020Pidev\\src\\main\\resources\\static\\ResultQuiz"+r.get(0).getQuiz().getFormation().getIdFormation()+".xlsx");

            byte[] buf = new byte[1024];
            int len;
            while ((len = stream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            wait(1000);

            this.emailSenderService.sendSimpleEmailWithFils(user.getEmail(),"your courses was finished yours Excel liste of score  " ," finished At : "+ new Date()+"  in Courses of Domain "+form.getDomain()+" "+" And Niveau : " +form.getLevel() +" .","C:\\Users\\LEGION-5\\IdeaProjects\\newProjet2020Pidev\\src\\main\\resources\\static\\ResultQuiz"+r.get(0).getQuiz().getFormation().getIdFormation()+".xlsx");

       }
    //  return r;
    }

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public User ApprenentwithMaxScoreInFormation(Integer id) {
        return this.iUserRepo.ApprenentwithMaxScoreInFormation(id);
    }

    @Override
    public List<Object> ApprenentwithMaxScore(Integer id) {

        return  iUserRepo.getApprenantWithScoreQuiz(id);


    }

    @Override
    public User ApprenentwithMaxScoreQuiz(Integer id) {

        return iUserRepo.getApprenantWithScoreForGifts(id).get(0);
    }

    @Override
    public Integer MaxScoreInFormation() {
        return this.iUserRepo.MaxScoreInFormation();
    }

    @Override
    public List<User> getApprenantWithScoreQuiz(Integer id) {
        return this.iUserRepo.getApprenantWithScore(id);
    }

    @Override
     public List<Result> getTopScore() {
        List<Result> sList = (List<Result>) iResultRepo.findAll(Sort.by(Sort.Direction.DESC, "totalCorrect"));

        return sList;
   }

    @Override
    public Integer getScore(Long idU) {
        return iResultRepo.getScore(idU);
    }

    @Override
    public List<QuizCourses> getQuizByFormation(Integer idF) {
        return this.iQuizRepo.getQuizByCourses(idF);
    }


    @Override
    public void DeleteQuiz(Integer idQ) {
        this.iQuizRepo.deleteById(idQ);
    }

    @Transactional
    @Override
    public void DeleteQuestion(Integer idQ) {
        this.iQuestionRepo.deleteById(idQ);
    }

    @Override
    public List<QuizCourses> listQuiqtestedbuUser(Long idU,Integer idF)
    {
      List<QuizCourses> quizCourses = iQuizRepo.getQuizByCourses(idF);
      List<QuizCourses> quizTested = iQuizRepo.listQuiqtestedbuUser(idU);

        List<QuizCourses> unavailable = quizCourses.stream()
                .filter(e -> !quizTested.contains(e))
                .collect(Collectors.toList());

        return unavailable;
    }



}
