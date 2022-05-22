package com.spring.pidev.controller;


import com.spring.pidev.model.Publicity;
import com.spring.pidev.service.IPublicityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Publicity")
public class PublicityRestController {


    @Autowired
    IPublicityService iPublicityService;


    @PostMapping("/addPublicity")
    public void AddPublicity(@RequestBody Publicity publicity)
    {iPublicityService.AddPublicity(publicity);}



    @ApiOperation(value = "See All Publicities list")
    @GetMapping("/getAllPublicities")
    @ResponseBody
    public List<Publicity> getAllPublicities()
    { return iPublicityService.getAllPublicities(); }


    @ApiOperation(value = "Delete Publicity")
    @DeleteMapping("/deletePublicity")
    @ResponseBody

    public void deletePublicity(Integer idPublicity)
    {
        iPublicityService.deletePublicity(idPublicity);
    }

    @ApiOperation(value = "Update Publicity")
    @PutMapping("/updatePublicity")
    @ResponseBody

    public Publicity updatePublicity(@RequestBody Publicity publicity)
    {
        return iPublicityService.upDatePublicity(publicity);


    }

}
