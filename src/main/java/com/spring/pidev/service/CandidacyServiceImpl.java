package com.spring.pidev.service;

import com.spring.pidev.model.*;
import com.spring.pidev.repo.DatabaseFileRepository;
import com.spring.pidev.repo.ICandidacyRepository;
import com.spring.pidev.repo.IPartnerRepository;
import com.spring.pidev.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class CandidacyServiceImpl implements ICandidacyService{
    private String uploadFolderPath="/Users/Drissi Omar/Desktop/uploadfolder";

    @Autowired
    ICandidacyRepository candidacyRepo;
    @Autowired
    UserRepository userrepo;
    @Autowired
    IPartnerRepository partnerrepo;
    @Autowired
    DatabaseFileRepository filerepo;
    @Autowired
    SendEmailService emailService;
    @Autowired
    SmsService smsService;
    @Autowired
    PartnerServiceImpl partnerService;

    @Autowired
    private SimpMessagingTemplate webSocket;

    private final String  TOPIC_DESTINATION = "/lesson/sms";





    @Override
    @Transactional
        public void addDemandAndAssignToStudent(StatusOfCandidacy status, Date DateOFCandidacy, Long idUser) {

            CandidacyUniversity candidacy=new CandidacyUniversity();
            candidacy.setDateOFCandidacy(DateOFCandidacy);
            candidacy.setStatus(status);


            User student = userrepo.findById(idUser).orElse(null);
            assert student != null;
            int b = student.getProfession().compareTo(Profession.STUDENT);
            if(b==0){
                candidacy.setUser(student);
                candidacyRepo.save(candidacy);
            }

        }
    @Override
    public void deleteCandidacyById(Integer Id) {
        candidacyRepo.deleteById(Id);
    }

    @Override
    public void unassignStudentDemand(Integer id) {
        CandidacyUniversity demand=candidacyRepo.findById(id).orElse(null);
        assert demand!=null;
        int d = demand.getUser().getProfession().compareTo(Profession.STUDENT);
        if(d==0){
            demand.setUser(null);
            candidacyRepo.save(demand);

        }


    }

    @Override
    @Transactional
    public void addDemandAndAssignToStudentAndUniversity(StatusOfCandidacy status, Date DateOFCandidacy,
                                                          Long idUser, Integer idPartner) {
        CandidacyUniversity candidacy=new CandidacyUniversity();
        candidacy.setDateOFCandidacy(DateOFCandidacy);
        candidacy.setStatus(status);


        User student = userrepo.findById(idUser).orElse(null);
        PartnerInstitution university=partnerrepo.findById(idPartner).orElse(null);

        assert student != null;
        assert university!=null;
        int b = student.getProfession().compareTo(Profession.STUDENT);
        int a = userrepo.studentDemands(idUser,idPartner);

        if(b==0 && a <1 && university.isActive()  ){

            candidacy.setUser(student);
            candidacy.setPartnerInstitution(university);
            candidacy.setStatus(StatusOfCandidacy.WAITING);
            candidacyRepo.save(candidacy);
            String text = "Hello , your demand :"+candidacy.getIdCandidacy()+"sent :"+candidacy.getDateOFCandidacy()+"" +
                    "to the University :"+candidacy.getPartnerInstitution().getIdPartner()+"have been added Successfully:"+candidacy.getStatus();
            emailService.sendSimpleEmail(student.getEmail(),"Demand Added Successfully",text);
        }else{
            if(partnerService.getRemainingCapacityReception(idPartner)==0 || !university.isActive()){
                String text = "Hello Sorry , your demand have been deleted";
                emailService.sendSimpleEmail(student.getEmail(),"Capacity reception completed ",text);

            }if (b!=0) {
                String text = "Hello Sorry , your demand have been deleted";
                emailService.sendSimpleEmail(student.getEmail(),"you are not allowed to add demand  ",text);

            }
            if (a>=1){
                String text = "Hello Sorry , your demand have been deleted";
                emailService.sendSimpleEmail(student.getEmail(),"u can not add two demands for the same university twice  ",text);
            }




        }

    }



    /*@Override
    public void uploadToLocal(MultipartFile file) {
        try{
            byte[] data =file.getBytes();
            Path path = Paths.get(uploadFolderPath+file.getOriginalFilename());
            Files.write(path,data);

        }catch (IOException e){
            e.printStackTrace();

        }

    }*/

    /*@Override
    public void uploadToDb(MultipartFile file)  {
        fileUpload CV = new fileUpload();
        try{
            CV.setFileData(file.getBytes());
            CV.setFileType(file.getContentType());
            CV.setFileName(file.getOriginalFilename());
            filerepo.save(CV);

        }catch (IOException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public List<CandidacyUniversity> getAllAcceptedDemandByUniversity(Integer idUniversity, StatusOfCandidacy status) {
        return null;
    }

    @Override
    public int getNbrDemandByUniversity(Integer idUniversity) {
        int nb = 0 ;
        for (CandidacyUniversity demand : candidacyRepo.findAll()){
            if(demand.getPartnerInstitution().getIdPartner()==idUniversity){
                nb++;

            }
        }
        return nb ;
    }

    @Override
    public int countAcceptedDemandsByUniversity(Integer idUniversity) {
       int nb =0;
       for (CandidacyUniversity demand: candidacyRepo.findAll()){
           if(demand.getPartnerInstitution().getIdPartner()==idUniversity && demand.getStatus().compareTo(StatusOfCandidacy.ACCEPTED)==0){
               nb++;
           }
       }
       return nb;

    }

    @Override
    public void AcceptDemand(Integer idDemand) {
        CandidacyUniversity demand = candidacyRepo.findById(idDemand).orElse(null);
        assert demand != null;
        PartnerInstitution university = partnerrepo.findById(demand.getPartnerInstitution().getIdPartner()).orElse(null);
        Date date = new Date();

        assert university != null;

        if(partnerService.checkAvailableUniversity(university.getIdPartner())==true){
            demand.setStatus(StatusOfCandidacy.ACCEPTED);
            demand.setDateResponse(date);
            candidacyRepo.save(demand);
            String text = "Hello , your demand :"+demand.getIdCandidacy()+"sent :"+demand.getDateOFCandidacy()+"" +
                    "to the University :"+demand.getPartnerInstitution().getIdPartner()+"have been :"+demand.getStatus();
            emailService.sendSimpleEmail(demand.getUser().getEmail(),"Demand Accepted",text);
            /*try{
            smsService.send(demand.getUser().getPhoneNumber(),text);
        }
        catch(Exception e){

            webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": Error sending the SMS: "+e.getMessage());
            throw e;
        }
        webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: "+demand.getUser().getPhoneNumber());*/

        }
        else {
            demand.setStatus(StatusOfCandidacy.REFUSED);
            demand.setDateResponse(date);
            candidacyRepo.save(demand);
            String text = "Hello , your demand :"+demand.getIdCandidacy()+"sent :"+demand.getDateOFCandidacy()+"" +
                    "to the University :"+demand.getPartnerInstitution().getIdPartner()+"have been :"+demand.getStatus();
            emailService.sendSimpleEmail(demand.getUser().getEmail(),"Demand Refused Capacity completed",text);

        }

    }
    private String getTimeStamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }



    @Override
    public void rejectDemand(Integer idDemand) {
        CandidacyUniversity demand = candidacyRepo.findById(idDemand).orElse(null);
        assert demand != null;
        demand.setStatus(StatusOfCandidacy.REFUSED);
        Date date = new Date();
        demand.setDateResponse(date);
        candidacyRepo.save(demand);
        String text = "\n Hello , your demand :"+demand.getIdCandidacy()+"sent :"+demand.getDateOFCandidacy()+"" +
                "to the University :"+demand.getPartnerInstitution().getIdPartner()+"have been :"+demand.getStatus()+"\n";
        emailService.sendSimpleEmail(demand.getUser().getEmail(),text,"Sorry your Demand is  refused");
        /*try{
            smsService.send(demand.getUser().getPhoneNumber(),text);
        }
        catch(Exception e){

            webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": Error sending the SMS: "+e.getMessage());
            throw e;
        }
        webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: "+demand.getUser().getPhoneNumber());*/


    }

    @Override
    public List<CandidacyUniversity> demandByDateOfCreation(Date date1, Date date2) {
        return candidacyRepo.demandByDateOfCreation(date1,date2);
    }

    @Override
    public List<CandidacyUniversity> findAllByStatus(StatusOfCandidacy status) {
        return candidacyRepo.findAllByStatus(status);
    }

    @Override
    public List<Object> countDemandsByDate() {
        return candidacyRepo.countDemandsByDate();
    }

    @Override
    public List<Object[]> countAcceptedDemandByDate() {
        return candidacyRepo.countAcceptedDemandByDate();
    }

    @Override
    public List<Object[]> countDemandByUniversity() {
        return candidacyRepo.countDemandByUniversity();
    }

    /*@Override
    public List<Object[]> countNumberStudentPerNationalityByYear(String ch,Date dateDebut, Date dateFin) {
        return candidacyRepo.countNumberStudentPerNationalityByYear(ch,dateDebut,dateFin);
    }*/
    @Override
    public long getTimeAttendForDemandResponse(int idDemand) {
        CandidacyUniversity demand=candidacyRepo.findById(idDemand).orElse(null);
        assert demand != null;
        return Math.abs(demand.getDateResponse().getTime()-demand.getDateOFCandidacy().getTime());
    }

    @Override
    public long getAverageWaitingTimeDemandByUniversity(Integer idUniversity) {
        long total=0;
        List<CandidacyUniversity>candidacyUniversities=candidacyRepo.findAllTreatedByUniversity(idUniversity);
        for(CandidacyUniversity c : candidacyUniversities){
            total+= Math.abs(c.getDateResponse().getTime()-c.getDateOFCandidacy().getTime());
        }
        return total/candidacyUniversities.size();

    }


}
