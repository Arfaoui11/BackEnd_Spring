package com.spring.pidev.controller;


import com.spring.pidev.model.Topic;
import com.spring.pidev.service.ITopicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/Topic")
@CrossOrigin()
public class TopicRestController

{

    @Autowired
    ITopicService iTopicService;


    @PostMapping("/addTopic")
    public void AddTopic(@RequestBody Topic topic)
    {iTopicService.AddTopic(topic);}

    @PostMapping("/addTopic/{idUser}")
   public void AddAffectTopicList(@RequestBody Topic topic, @PathVariable("idUser") Long idUser) {
        iTopicService.AddAffectTopicList(topic,idUser);
    }

    @ApiOperation(value = "Update Topic")
    @PutMapping("/updateTopic/{idUser}")
    @ResponseBody

    public Topic updateTopic(@RequestBody Topic topic, @PathVariable("idUser") Long idUser)
    {
        return iTopicService.upDateTopic(topic,idUser);
    }


    @ApiOperation(value = "See All topics list")
    @GetMapping("/getAllTopics")
    @ResponseBody
    public List<Topic> getAllTopics()
        { return iTopicService.getAllTopics(); }


        @ApiOperation(value = "Delete Topic")
        @GetMapping("/deleteTopic/{id}")
        @ResponseBody
        public void deleteTopic(@PathVariable("id") Long idTopic)
        {
            iTopicService.deleteTopic(idTopic);
        }



        @ApiOperation(value ="Give the nbr of comment per topic")
        @GetMapping("/getNbrCommentTopic/{idTopic}")
        @ResponseBody
        public Integer getNbrCommentTopic(@PathVariable("idTopic") Long idTopic){
        return iTopicService.getNbrCommentTopic(idTopic);
    }

    @PutMapping("/TopicWIthRate/{idTopic}/{nbr}")
    @ApiOperation(value = " add Rating ")
    public void TopicWIthRate(@PathVariable(name = "idTopic") Long idTopic,@PathVariable(name = "nbr") Double rate)
    {
        iTopicService.TopicWithRate(idTopic, rate);
    }


    @ApiOperation(value = " Search Topic Multiple  ")
    @GetMapping("/SearchMultiple/{keyword}")
    @ResponseBody
    public List<Topic> SearchTopicMultiple(@PathVariable("keyword")  String key){
        return  iTopicService.SearchTopicMultiple(key);

    }

    @GetMapping("/CountTopicByType")
    // @ResponseBody
    public Map<String, Double> PourcentageTopicByType(){

        return iTopicService.PourcentageTopicByType();

    }
}
