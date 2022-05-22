package com.spring.pidev.service;

import com.spring.pidev.model.PartnerInstitution;
import com.spring.pidev.model.Profession;
import com.spring.pidev.model.Rating;
import com.spring.pidev.model.User;
import com.spring.pidev.repo.IPartnerRepository;
import com.spring.pidev.repo.RatingRepository;
import com.spring.pidev.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RateServiceImpl implements IRateService{


    @Autowired
    RatingRepository raterepo;

    @Autowired
    UserRepository userrepo;

    @Autowired
    IPartnerRepository partnerrepo;

    @Override
    @Transactional
    public void addRateAndAffectToStudentAndUniversity(Rating rating, Long idStudent, Integer idUniversity) {
        User student = userrepo.findById(idStudent).orElse(null);
        PartnerInstitution university=partnerrepo.findById(idUniversity).orElse(null);
        assert student != null;
        int b = student.getProfession().compareTo(Profession.STUDENT);
        if(b==0){
            rating.setUser(student);
            rating.setPartnerInstitution(university);
            raterepo.save(rating);
        }


    }




    @Override
    public List<Rating> findAllByUniversityId(Integer id) {
        return null;
    }

    @Override
    public List<Rating> getRatingByUniversityAndUser(Integer idUniversity, Long idStudent) {
        return raterepo.getRatingByUniversityAndUser(idUniversity,idStudent);
    }
}
