package com.spring.pidev.service;

import com.spring.pidev.model.Candidacy;
import com.spring.pidev.model.Offres;
import com.spring.pidev.model.Profession;
import com.spring.pidev.model.User;

import java.util.Date;
import java.util.List;

public interface IServices {


    void updateOffer (Offres offer, Long idUser );
    void  updateOffer(Offres offer);
    void deleteOffer(Integer idOffer);
    List<Offres> GetOffer();

    void add(Candidacy candidacy, int idO, Long idU);
    void  updateCandidacy (Candidacy candidacy);
    void  deleteCandidacy(Integer idCandidacy);
    List<Candidacy> GetCandidacy();
    void addUser (User user);

    List<Offres> OffresParDateCreation(Date date1,Date date2);
    List<Candidacy> offerByProfession( Profession profession);
    Integer  getNumberOfUserInThisCandidacy( Integer idCandidacy);
    Offres getOffresHighRecommended();
    List<Offres> listAllOffres (String keyword);
    List<Candidacy> listAllCandidacy(String keyword);
    List<Candidacy> CandidacyByProfession(Profession profession);

    List<Object>  nbrOffer();




    //List<Offres> OffreByProf(Profession profession);

   // List<Offres> OffreByProfession(Profession profession);
}
