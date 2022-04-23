package com.spring.pidev.repo;


import com.spring.pidev.model.DatabaseFile;
import com.spring.pidev.model.Domain;
import com.spring.pidev.model.Formation;
import com.spring.pidev.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;


@Repository
public interface IFormationRepo extends CrudRepository<Formation,Integer> {



    @Query(value= "select SUM(f.nbrHeures*f.formateur.tarifHoraire) from Formation f where f.formateur.id=:id and f.start>=:dateD and f.end<=:dateF")
    Integer getFormateurRemunerationByDate(@Param("id") Long idFormateur, @Param("dateD") Date dateDebut, @Param("dateF") Date dateFin);

    @Query(value= "select SUM(f.nbrHeures*f.formateur.tarifHoraire) ,f.formateur from Formation f where f.start>=:dateD and f.end<=:dateF group by f.formateur order by SUM(f.nbrHeures*f.formateur.tarifHoraire) desc ")
    List<Object> getFormateurRemunerationByDateTrie(@Param("dateD") Date dateDebut, @Param("dateF") Date dateFin);


    @Query(value = "select c.message,c.createAt,l.nbrLikes,d.nbrDislikes from PostComments c join c.likes l join c.Dislikes d where c.formation.idFormation=:id")
    List<Object[]> getCommentBylikesEtDislikes(@Param("id") Integer id);


    @Query(value= "select SUM(f.nbrHeures*f.formateur.tarifHoraire) from Formation f where f.formateur.id=:id and f.formateur.profession='FORMER'")
    Integer getFormateurRemuneration(@Param("id") Long idFormateur);

    @Query(value="select count(a.id) from Formation f join f.apprenant a where f.title=:titre")
    Integer getNbrApprenantByFormation(@Param("titre") String titre);

    @Query(value="select count(a.id) from Formation f join f.apprenant a where f.idFormation=:id")
    Integer getNbrApprenantByFormationId(@Param("id") Integer id);

    @Query(value = "select count(f.idFormation) from Formation f join f.apprenant a where a.id=:id and f.start>=:dateD and f.end<=:dateF and f.domain=:domain")
    Integer getNbrFormationByApprenant(@Param("id") Long idApp, @Param("domain") Domain domain, @Param("dateD") Date dateDebut, @Param("dateF") Date dateFin);


  //  @Query(value = "select f from Formation f where concat(f.title,f.level,f.domain,f.frais,f.nbrHeures,f.nbrMaxParticipant) like %?1% group by f order by sum(f.likes-f.dislikes) desc")
    @Query(value = "select f from Formation f where concat(f.title,f.level,f.domain,f.frais,f.nbrHeures,f.nbrMaxParticipant) like %?1% group by f order by f.rating desc")
    List<Formation> rech(String keyword);


    @Query(value = "select f from Formation f where f.start>=:dateD and f.end<=:dateF")
    List<Formation> listformationByDate(@Param("dateD") Date dateDebut, @Param("dateF") Date dateFin);

    @Query(value = "select f from Formation f where f.domain=:domain")
    List<Formation> listFormationByDomain(@Param("domain") Domain domain);

    @Query(value = "select f from Formation f join f.apprenant a where a.id=:id")
    List<Formation> listFormationParApprenant(@Param("id") Long idApp);


    @Query(value = "select count(f.idFormation) from Formation f join f.formateur fr where f.start>=:dateD and f.end<=:dateF and fr.id=:id and f.domain=:domain")
    Integer nbrCoursesParFormateur(@Param("id") Long idF, @Param("dateD") Date dateDebut, @Param("dateF") Date dateFin ,@Param("domain") Domain domain);

    @Query(value = "select d from DatabaseFile d join d.formation f where f.idFormation=:id")
    List<DatabaseFile> getfileFormation(@Param("id") Integer idF);

    @Query(value = "select u from Formation f join f.formateur u where f.idFormation=:id")
    User getFormateurFromFormation(@Param("id") Integer idFormateur);


    //get nbr likes by comments
    @Query(value = "select l.nbrLikes from PostComments c join c.likes l where c.idComn=:id")
    Integer getNbrLikesByComment(@Param("id") Integer id);

    //get nbr dislikes by comments
    @Query(value = "select d.nbrDislikes from PostComments c join c.Dislikes d where c.idComn=:id")
    Integer getNbrDislikesByComment(@Param("id") Integer id);




}
