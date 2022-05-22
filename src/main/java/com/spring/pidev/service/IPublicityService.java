package com.spring.pidev.service;


import com.spring.pidev.model.Publicity;

import java.util.List;

public interface IPublicityService {

    public Publicity AddPublicity(Publicity publicity);

    public List<Publicity> getAllPublicities();

    void deletePublicity(Integer idPublicity);

    public Publicity upDatePublicity(Publicity publicity);
}
