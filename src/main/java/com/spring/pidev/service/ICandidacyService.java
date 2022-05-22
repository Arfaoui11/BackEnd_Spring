package com.spring.pidev.service;

import com.spring.pidev.model.CandidacyUniversity;
import com.spring.pidev.model.StatusOfCandidacy;

import java.util.Date;
import java.util.List;

public interface ICandidacyService {
    public void addDemandAndAssignToStudent(StatusOfCandidacy status, Date DateOFCandidacy, Long idUser);
    public void deleteCandidacyById(Integer Id);
    public void unassignStudentDemand(Integer id);
    public void addDemandAndAssignToStudentAndUniversity(StatusOfCandidacy status, Date DateOFCandidacy, Long idUser, Integer idPartner);
    //public void uploadToLocal(MultipartFile file) ;
    //public void uploadToDb(MultipartFile file) ;
    public List<CandidacyUniversity> getAllAcceptedDemandByUniversity(Integer idUniversity, StatusOfCandidacy status);
    public int getNbrDemandByUniversity(Integer idUniversity);

    public int countAcceptedDemandsByUniversity(Integer idUniversity );
    public void AcceptDemand(Integer idDemand);
    public void rejectDemand(Integer idDemand);
    public List<CandidacyUniversity>demandByDateOfCreation(Date date1, Date date2);
    public List<CandidacyUniversity>findAllByStatus(StatusOfCandidacy status);
    public List<Object>countDemandsByDate();
    public List<Object[]> countAcceptedDemandByDate();
    public List <Object[]> countDemandByUniversity();
    //List<Object[]> countNumberStudentPerNationalityByYear(String ch,Date dateDebut,Date dateFin);
    public long getTimeAttendForDemandResponse(int idDemand);

    public long getAverageWaitingTimeDemandByUniversity(Integer idUniversity);



}
