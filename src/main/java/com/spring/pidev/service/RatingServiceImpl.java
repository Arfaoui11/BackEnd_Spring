package com.spring.pidev.service;


import com.spring.pidev.model.*;
import com.spring.pidev.repo.IPartnerRepository;
import com.spring.pidev.repo.IRatingRepository;
import com.spring.pidev.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingServiceImpl implements IRatingService{

    @Autowired
    IRatingRepository raterepo;
    @Autowired
    UserRepository userrepo;
    @Autowired
    IPartnerRepository partnerrepo;

    @Override
    public void addRating(RatingPartner ratingPartner) {
        raterepo.save(ratingPartner);
    }

    @Override
    @Transactional
    public void addRateAndAffectToStudentAndUniversity(TypeRating typeRating, Long idStudent, Integer idUniversity) {
        RatingPartner ratingPartner=new RatingPartner();
        ratingPartner.setTypeRating(typeRating);

        User student = userrepo.findById(idStudent).orElse(null);
        PartnerInstitution university=partnerrepo.findById(idUniversity).orElse(null);
        assert student != null;
        int b = student.getProfession().compareTo(Profession.STUDENT);
        if(b==0){
            ratingPartner.setUser(student);
            ratingPartner.setPartnerInstitution(university);
            raterepo.save(ratingPartner);
        }
    }

    @Override
    public List<RatingPartner> getRatingByUniversityAndUser(Integer idUniversity, Long idStudent) {
        return raterepo.getRatingByUniversityAndUser(idUniversity,idStudent);
    }




}
