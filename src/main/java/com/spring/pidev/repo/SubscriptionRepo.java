package com.spring.pidev.repo;

import com.spring.pidev.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {

    @Query("select count(subs) from Subscription subs join subs.users u where u.id=:id")
    int GetNbrSubscriptionByUser(@Param("id") Long idUser);

    @Query(value = "select COUNT (u.id) from User  u join u.subscs sub where sub.idSubscription=:id")
    Integer getNberOfUserInThisSubscription(@Param("id") Integer idSubscription);

   // List<Subscription> reaserch(String keyword);






}

