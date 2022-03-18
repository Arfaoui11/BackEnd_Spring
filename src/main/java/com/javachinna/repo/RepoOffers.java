package com.javachinna.repo;


import com.javachinna.model.Offres;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RepoOffers extends CrudRepository<Offres,Integer> {


    @Query(value = "select o from Offres o where o.dateBeginOffer>=:dateBeginOffer AND o.dateEndOffer<=:dateEndDate")
    List<Offres> OffresParDateCreation(@Param("dateBeginOffer") Date date1, @Param("dateEndDate") Date date2);


    @Query(value = "select COUNT (u.id) from Candidacy  c join c.usersW u join  c.offers o where o.idOffer=:id")
    Integer getNumberOfUserInThisCandidacy(@Param("id") Integer idOffer);


    @Query("SELECT o FROM Offres o WHERE CONCAT(o.dateEndOffer, o.dateBeginOffer, o.dateInterview , o.priceOffer ,o.profession) LIKE %?1%")
    public List<Offres> findAll(String keyword);

}