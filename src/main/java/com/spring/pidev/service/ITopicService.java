package com.spring.pidev.service;


import com.spring.pidev.model.Topic;

import java.util.List;
import java.util.Map;

public interface ITopicService {


    public Topic AddTopic(Topic topic);


   void AddAffectTopicList(Topic topic, Long idUser);


    void deleteTopic(Long idTopic);
    public Topic upDateTopic (Topic topic, Long idUser);
    public List<Topic> getAllTopics();
    Integer getNbrCommentTopic(Long idTopic);

    void TopicWithRate(Long idTopic ,Double rate);

    public List<Topic> SearchTopicMultiple(String key);

    void DeleteTopicAfterfinalDate();

    Map<String,Double> PourcentageTopicByType();


}
