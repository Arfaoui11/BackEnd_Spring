package com.spring.pidev.service;


import com.spring.pidev.model.Topic;
import com.spring.pidev.model.Type;
import com.spring.pidev.repo.ITopicRepo;
import com.spring.pidev.repo.UserRepository;
import com.spring.pidev.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@Transactional
public class TopicService implements ITopicService{



    @Autowired
    ITopicRepo iTopicRepo;
    @Autowired

    UserRepository userRepository;


    @Override
    public Topic AddTopic(Topic topic) {
        return iTopicRepo.save(topic);
    }



    @Override
    public void AddAffectTopicList(Topic topic, Long idUser) {

        User user = (User) userRepository.findById(idUser).orElse(null);


                topic.setUser(user);
                iTopicRepo.save(topic);

    }

    @Override
    public Topic upDateTopic(Topic topic, Long idUser) {

        User user =  userRepository.findById(idUser).orElse(null);
        topic.setUser(user);
        return iTopicRepo.save(topic);
    }




    public void deleteTopic(Long idTopic) {
        log.info("In methode deleteTopic");
        log.warn("Are you sure you want to delete topic");

        iTopicRepo.deleteById(idTopic);
        log.error("exeption");

    }

    @Override
    public List<Topic> getAllTopics() {
        return (List<Topic>) iTopicRepo.findAll();
    }


    @Override
    public Integer getNbrCommentTopic(Long idTopic){
        Topic topic = iTopicRepo.findById(idTopic).orElse(null);
        return iTopicRepo.getNbrCommentTopic(idTopic);
    }

    @Override
    public void TopicWithRate(Long idTopic, Double rate) {
        Topic topic = iTopicRepo.findById(idTopic).orElse(null);

        if(topic.getRating()==0)
        {
            topic.setRating(rate);
        }else {
            topic.setRating(((topic.getRating()+rate)/2.0));
        }

        iTopicRepo.save(topic);
    }
////////////////////////SearchMultiple///////////////////
    @Override
    public List<Topic> SearchTopicMultiple(String key) {

        if (key.equals(""))
        {
            return (List<Topic>) iTopicRepo.findAll();
        }else
        {
            return iTopicRepo.searchmultilpltopic(key);
        }

    }
//////////////////////////DeleteAuto/////////////////////////////
@Override
@Scheduled(cron = "0 0/2 * * * *")
public void DeleteTopicAfterfinalDate() {
    LocalDate currentdDate1 = LocalDate.now();

    ZoneId defaultZoneId = ZoneId.systemDefault();

    Date dd = Date.from(currentdDate1.atStartOfDay(defaultZoneId).toInstant());

    ///pour définir la date__debut mois fin mois//////
    Calendar calLast = Calendar.getInstance();
    Calendar calFirst = Calendar.getInstance();

    calLast.set(Calendar.DATE, calLast.getActualMaximum(Calendar.DATE));
    calFirst.set(Calendar.DATE, calFirst.getActualMinimum(Calendar.DATE));

    Date lastDayOfMonth = calLast.getTime();
    Date firstDayOfMonth = calFirst.getTime();


    for (Topic a : iTopicRepo.DeleteTopicAfterfinalDate(lastDayOfMonth)) {
    /*
    {
        ArchiveAppointment ar = new ArchiveAppointment();
        ar.setUsers(a.getUsers());
        ar.setDoctor(a.getDoctor());
        ar.setIdApp(a.getIdApp());
        ar.setRemark(a.getRemark());
        ar.setDateApp(a.getDateApp());
        ar.setDelete_At(new Date());

        iRendezVousArchiveRepos.save(ar);

     */
        iTopicRepo.delete(a);
    }
}
///////////////////////////////////
@Override
public Map<String,Double> PourcentageTopicByType() {

    Map<String,Double> pourcentages=new HashMap<>();

    double SKILLS = 0;
    double  STUDIES= 0;
    double  WORK= 0;
    double  RIGHTS= 0;
    double  MARKETING= 0;
    double  CHAMPION= 0;
    double  HEALTH= 0;
    double  DISCRIMINATION= 0;
    double  CULTURAL_EMPOWERMENT= 0;


    List<Topic> topics=  (List<Topic>) iTopicRepo.findAll();

    System.out.println(topics);

    for (Topic topic: topics) {

        if (topic.getType().equals(Type.SKILLS)) {
            SKILLS++;
        }

        else if (topic.getType().equals(Type.STUDIES)) {
            STUDIES++;}

        else if (topic.getType().equals(Type.WORK)) {
            WORK++;}

        else if (topic.getType().equals(Type.RIGHTS)) {
            RIGHTS ++;}
        else if (topic.getType().equals(Type.MARKETING)) {
            MARKETING ++;}
        else if (topic.getType().equals(Type.CHAMPION)) {
            CHAMPION++;}
        else if (topic.getType().equals(Type.HEALTH)) {
            HEALTH ++;}
        else if (topic.getType().equals(Type.DISCRIMINATION)) {
            DISCRIMINATION ++;}
        else if (topic.getType().equals(Type.CULTURAL_EMPOWERMENT)) {
            CULTURAL_EMPOWERMENT ++;}
    }
    if (topics.size() !=0) {

        System.out.println("topic_number:"+topics.size());

        SKILLS = ((SKILLS/(topics.size()))*100);
        STUDIES =  ((STUDIES/(topics.size()))*100);
        WORK=  ((WORK/(topics.size()))*100);
        RIGHTS =  ((RIGHTS/(topics.size()))*100);
        MARKETING =  ((MARKETING/(topics.size()))*100);
        CHAMPION =  ((CHAMPION/(topics.size()))*100);
        HEALTH =  ((HEALTH/(topics.size()))*100);
        DISCRIMINATION =  ((DISCRIMINATION/(topics.size()))*100);
        CULTURAL_EMPOWERMENT = ((CULTURAL_EMPOWERMENT/(topics.size()))*100);

    }

    pourcentages.put("SKILLS",SKILLS);
    pourcentages.put("STUDIES",STUDIES);
    pourcentages.put("WORK",WORK);
    pourcentages.put("RIGHTS",RIGHTS);
    pourcentages.put("MARKETING",MARKETING);
    pourcentages.put("CHAMPION",CHAMPION);
    pourcentages.put("HEALTH",HEALTH);
    pourcentages.put("DISCRIMINATION",DISCRIMINATION);
    pourcentages.put("CULTURAL_EMPOWERMENT",CULTURAL_EMPOWERMENT);


        /*
        pourcentages.add(SKILLS);


         */

    System.out.println(pourcentages);

    return pourcentages;
}

}

