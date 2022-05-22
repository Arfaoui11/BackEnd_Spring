package com.spring.pidev.service;



import com.spring.pidev.model.*;
import org.springframework.data.repository.query.Param;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface IServiceFormation {

    void ajouterFormateur(User formateur);


    Formation addFormation(Formation formation);
    void updateFormation(Formation formation, Integer idFormateur);
    void deleteFormation(Integer idFormation);
    List<Formation> afficherFormation();
    List<User> afficherFormateur();
    List<User> afficherApprenant();

    User FormateurwithMaxHo();

     User getFormateurRemunerationMaxSalaire() throws MessagingException;

     TreeMap<Integer, User> getFormateurRemunerationMaxSalaireTrie();

    List<Object> getFormateurRemunerationByDateTrie();

    void CertifactionStudents();

    List<Object[]> getPourcentage();
    List<Formation>  SearchMultiple(String key);

    List<Object[]> getAllSearch();


    Map<String,Double> PourcentageCoursesByDomain() throws IOException, MessagingException;


    void deleteFiles(String id);

    void ajouterApprenant(User apprenant);
    void ajouterEtAffecterFormationAFormateur(Formation formation, Long idFormateur);
    Formation getFile(Integer fileId) throws FileNotFoundException;


    void affecterApprenantFormationWithMax(Long idApprenant, Integer idFormation);

    void affecterApprenantFormation(Long idApprenant,Integer idFormation);

    Result upDateResult(Result result, Integer idQ, Long idUser);


    Integer nbrCoursesParFormateur(Long idF, Date dateDebut, Date dateFin, Domain domain);

    Integer getNbrApprenantByFormation(String title);

    void getNbrApprenantByFormationn();

    Integer getNbrFormationByApprenant(Long idApp,Domain domain ,Date dateDebut, Date dateFin);

    List<Object[]> getNbrApprenantByFormation();

    List<Object[]> getCommentBylikesEtDislikes(Integer id);




    List<User> getApprenantByFormation(Integer idF);


    Integer getFormateurRemunerationByDate(Long idFormateur, Date dateDebut,Date dateFin);
    Integer getRevenueByFormation(Integer idFormation);



    void likeComments(Integer idC,Long idUser);
    void dislikeComments(Integer idC,Long idUser);

    void desaffecterApprenant(Long idUser,Integer idF);


    void FormationWithRate(Integer idF ,Double rate);



    void SearchHistorique(String keyword);



    Integer addComments(PostComments postComments , Integer idF, Long idUser);

    void deleteComments(Integer idC);

    PostComments upDateComment(PostComments postComments,Integer idF,Long idUser);

    List<PostComments> getAllComments();

    List<PostComments> getCommentsByFormation(Integer idF);

    Formation getFormationById(int id);

    void decisionUserPUNISHED();

    void LeanerStatus();

    void ListComplete();



    List<DatabaseFile> getfileFormation(Integer idF);


   User getFormateurFromFormation(Integer idFormateur);

   //get Formation by formateur
   List<Formation> getFormationByFormateur(Long idFormateur);

   //get Formation by apprenant
   List<Formation> getFormationByApprenant(Long idApprenant);

    Integer getNbrLikesByComments(Integer idC);

    Integer getNbrDislikesByComments(Integer idC);


}
