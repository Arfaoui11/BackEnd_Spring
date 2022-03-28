package com.javachinna.controller;

import com.google.zxing.WriterException;
import com.javachinna.QrCode.QRCodeGenerator;
import com.javachinna.model.*;
import com.javachinna.payLoad.Response;
import com.javachinna.repo.ICandidacyRepository;
import com.javachinna.repo.UserRepository;
import com.javachinna.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ExchangeStudentUniversity")
public class ExchangeStudentUniversityController {
    @Autowired
    ICandidacyService candiService;
    @Autowired
    SendEmailService emailService;
    @Autowired
    UserRepository userrepo;

    @Autowired
    IFileService fileService;

    @Autowired
    exportPdf exportPdfservice;
    @Autowired
    exportExcel exportExcelservice;

    @Autowired
    UserService userservice;
    @Autowired
    ICommentUniversityService commentService;

    @Autowired
    IRateService rateService;
    @Autowired
    IReactService estimationService;
    @Autowired
    ICandidacyRepository candidacyRepository;



    @Autowired
    IPartnerService partnerservice;
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


    @PostMapping("/addDemandAndAssignToStudent/{idUser}")
    @ResponseBody
    @ApiOperation(value = "addDemandAndAssignToStudent ")
    public void addDemandAndAssignToStudent(StatusOfCandidacy status, Date DateOFCandidacy, @PathVariable("idUser") Long idUser){
        candiService.addDemandAndAssignToStudent(status,DateOFCandidacy,idUser);

    }
    @DeleteMapping ("/DeleteCandidacy/{id}")
    @ResponseBody
    @ApiOperation(value = "Delete Candidacy  ")
    public void deleteCandidacyById(@PathVariable("id") Integer Id){
        candiService.deleteCandidacyById(Id);
    }

    @PutMapping  ("/unassignStudentDemand/{idDemand}")
    @ResponseBody
    @ApiOperation(value = "unassignStudentDemand  ")
    public void unassignStudentDemand(@PathVariable("idDemand") Integer id){
        candiService.unassignStudentDemand(id);
    }


    @PostMapping("/addDemandAndAssignToStudentAndUniversity/{idUser}/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "addDemandAndAssignToStudentAndUniversity ")
    public void addDemandAndAssignToStudentAndUniversity(StatusOfCandidacy status, Date DateOFCandidacy, @PathVariable("idUser") Long idUser, @PathVariable("idUniversity") Integer idPartner){
        candiService.addDemandAndAssignToStudentAndUniversity(status,DateOFCandidacy,idUser,idPartner);

    }

    @GetMapping("/getNbrDemandByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "getNbrDemandByUniversity ")
    public int getNbrDemandByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return candiService.getNbrDemandByUniversity(idUniversity);
    }
    @PutMapping  ("/AcceptDemand/{idDemand}")
    @ResponseBody
    @ApiOperation(value = "AcceptDemand ")
    public void AcceptDemand(@PathVariable("idDemand") Integer idDemand){
        candiService.AcceptDemand(idDemand);

    }
    @PutMapping  ("/RefuseDemand/{idDemand}")
    @ResponseBody
    @ApiOperation(value = "refuseDemand ")
    public void rejectDemand(@PathVariable("idDemand") Integer idDemand){
        candiService.rejectDemand(idDemand);
    }


    @GetMapping("/demandByDateOfCreation/{d1}/{d2}")
    @ResponseBody
    @ApiOperation(value = "demandByDateOfCreation ")
    public List<CandidacyUniversity> demandByDateOfCreation(@PathVariable("d1") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date date1,
                                                            @PathVariable("d2") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date date2){
        return  candiService.demandByDateOfCreation(date1,date2);
    }
    @GetMapping("/findAllDemandByStatus")
    @ResponseBody
    @ApiOperation(value = "find All Demand By Status ")
    public List<CandidacyUniversity>findAllByStatus(StatusOfCandidacy status){
        return  candiService.findAllByStatus(status);
    }

    @GetMapping("/countAcceptedDemandsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "count Accepted Demands ByUniversity ")
    public int countAcceptedDemandsByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return candiService.countAcceptedDemandsByUniversity(idUniversity);
    }

    @GetMapping("/countDemandsByDate")
    @ApiOperation(value = "count demand by date  ")
    public List<Object> countDemandsByDate(){
        return  candiService.countDemandsByDate();
    }
    @GetMapping("/countAcceptedDemandByDate")
    @ApiOperation(value = "count Accepted Demand ByDate ")
    public List<Object[]> countAcceptedDemandByDate(){
        return candiService.countAcceptedDemandByDate();
    }
    @GetMapping("/countDemandByUniversity")
    @ApiOperation(value = "count Demand ByUniversity")
    public List<Object[]> countDemandByUniversity(){
        return candiService.countDemandByUniversity();
    }



    @PostMapping( "/uploadFile/{idStudent}")

    public Response uploadFileAndAffectToUser(@RequestParam("file") MultipartFile file, @PathVariable("idStudent") Long IdStudent) {
        DatabaseFile fileName = fileService.storeFile(file,IdStudent);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getFileName())
                .toUriString();

        return new Response(fileName.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    @GetMapping("/downloadFile/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request) throws FileNotFoundException {
        // Load file as Resource
        DatabaseFile databaseFile = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }

    @PostMapping("/addPartner")
    @ResponseBody
    @ApiOperation(value = "Add Partner ")
    public void addPartner (@RequestBody PartnerInstitution partnerInstitution){
        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(partnerInstitution.getEmail(),250,250);

            QRCodeGenerator.generateQRCodeImage(partnerInstitution.getEmail(),250,250,QR_CODE_IMAGE_PATH);

            // Generate and Save Qr Code Image in static/image folder

        } catch (WriterException | IOException e) {

            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        partnerservice.addPartner(partnerInstitution);
    }


    @DeleteMapping ("/DeletePartner/{id}")
    @ResponseBody
    @ApiOperation(value = "Delete Partner Institution ")
    public void deletePartnerById(@PathVariable("id") Integer Id){
        partnerservice.deletePartnerById(Id);
    }

    @GetMapping("/getAllPartners")
    @ResponseBody
    @ApiOperation(value = "GET Partners ")
    public List<PartnerInstitution> getAllPartners(){
        return partnerservice.getAllPartners();
    }




    @GetMapping("/getPartner/{IdPartner}")
    @ResponseBody
    @ApiOperation(value = "GET Partner ")
    public PartnerInstitution getPartnerById( @PathVariable("IdPartner") Integer IdPartner){
        return partnerservice.getPartnerById(IdPartner);
    }
   /* @PutMapping ("/updatePartner")
    @ResponseBody
    @ApiOperation(value = "update Partner ")
    public void updatePartner(@RequestBody PartnerInstitution partnerInstitution){
        partnerservice.updatePartner(partnerInstitution);
    }
    @PutMapping ("/updatePartner1")
    @ResponseBody
    @ApiOperation(value = "update Partner methode 2  ")
    public PartnerInstitution updatePartnerInstitution( @RequestBody PartnerInstitution p){
        return partnerservice.updatePartnerInstitution(p);
    }*/

    @GetMapping("/getPartner")
    @ResponseBody
    @ApiOperation(value = "GET Partner ")
    public List<PartnerInstitution> FindAllpartnersByArea( GeographicalArea Area){
        return partnerservice.FindAllpartnersByArea(Area);
    }
    @GetMapping("/FindAllpartnersByAreaAndSpecialty")
    @ResponseBody
    @ApiOperation(value = "FindAllpartnersByAreaAndSpecialty ")
    public List<PartnerInstitution> FindAllpartnersByAreaAndSpecialty(GeographicalArea Area, specialty s){
        return  partnerservice.FindAllpartnersByAreaAndSpecialty(Area,s);
    }
    @GetMapping("/getUniversityRemunerationByDate/{id}/{d1}/{d2}")
    @ResponseBody
    @ApiOperation(value = "getUniversityRemunerationByDate ")
    public double getUniversityRemunerationByDate(@PathVariable("id") Integer idUniversity,
                                                  @PathVariable("d1") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date dateDebut,
                                                  @PathVariable("d2") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date dateFin){
        return partnerservice.getUniversityRemunerationByDate(idUniversity,dateDebut,dateFin);
    }
    @GetMapping("/getUniversityRemunerationByDate/{d1}/{d2}")
    @ResponseBody
    @ApiOperation(value = "getUniversityRemunerationByDate ")
    public List<Object> getUniversitiesRemunerationByDateTrie( @PathVariable("d1") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)Date dateDebut,
                                                               @PathVariable("d2") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)Date dateFin){
        return partnerservice.getUniversitiesRemunerationByDateTrie(dateDebut,dateFin);
    }


    @GetMapping("/getNumberOfStudentDemandsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "getNumberOfStudentDemandsByUniversity ")
    public int getNumberOfStudentDemandsByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return partnerservice.getNumberOfStudentDemandsByUniversity(idUniversity);
    }
    @GetMapping("/getNumberOfStudentsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "getNumberOfStudentsByUniversity ")
    public int getNumberOfStudentsByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return partnerservice.getNumberOfStudentsByUniversity(idUniversity);
    }

    @PutMapping ("/affectStudentToPartnerInstitution/{idstudent}/{idpartner}")
    @ResponseBody
    @ApiOperation(value = "affectStudentToPartnerInstitution ")
    public void affectStudentToPartnerInstitution(@PathVariable("idstudent") Long idUser, @PathVariable("idpartner") Integer idPartner){
        partnerservice.affectStudentToPartnerInstitution(idUser,idPartner);

    }
    @PutMapping ("/desaffecterPartnerFromAllCandidacy/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "desaffecterPartnerFromAllCandidacy  ")
    public void desaffecterPartnerFromAllCandidacy(@PathVariable("idUniversity") Integer idUniversity){
        partnerservice.desaffecterPartnerFromAllCandidacy(idUniversity);

    }
    @GetMapping("/numPartnerBySpecialty")
    @ResponseBody
    @ApiOperation(value = "numPartnerBySpecialty ")
    int numPartnerBySpecialty(specialty s){
        return partnerservice.numPartnerBySpecialty(s);
    }


    @GetMapping("/getAllUniversityByTopRating")
    @ResponseBody
    @ApiOperation(value = "getAllUniversityByTopRating ")
    public List<PartnerInstitution> getAllUniversityByTopRating(){
        return partnerservice.getAllUniversityByTopRating();
    }

    @GetMapping("/exports/pdf")
    public ResponseEntity<InputStreamResource>exportsTermPdf(){
        List<PartnerInstitution> partnerInstitutions =(List<PartnerInstitution>) partnerservice.getAllPartners();
        ByteArrayInputStream b = exportPdfservice.universityPDFReport(partnerInstitutions );
        HttpHeaders h=new HttpHeaders();
        h.add("Content-Disposition","Inline;filename=universities.pdf");
        return ResponseEntity.ok().headers(h).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(b));
    }


    /*@GetMapping("/exports/excel")
    public ResponseEntity<InputStreamResource>exportsTermExcel() throws IOException {
        List<PartnerInstitution> partnerInstitutions =(List<PartnerInstitution>) partnerservice.getAllPartners();
        ByteArrayInputStream a = exportExcelservice.universityExcelReport(partnerInstitutions );
        HttpHeaders h=new HttpHeaders();
        h.add("Content-Disposition","Inline;filename=universities.xlsx");
        return ResponseEntity.ok().headers(h).body(new InputStreamResource(a));
    }*/
    @GetMapping("/download/universities.xlsx")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        List<PartnerInstitution> partnerInstitutions =(List<PartnerInstitution>) partnerservice.getAllPartners();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=universities.xlsx");
        ByteArrayInputStream stream = exportExcelservice.universityExcelFile(partnerInstitutions);
        IOUtils.copy(stream, response.getOutputStream());
    }

    @GetMapping("/getRemainingCapacityReception/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "get Remaining CapacityReception ")
    public int getRemainingCapacityReception(@PathVariable("idUniversity") Integer idUniversity){
        return partnerservice.getRemainingCapacityReception(idUniversity);
    }
    @GetMapping("/checkAvailableUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "check Available University ")
    public boolean checkAvailableUniversity(@PathVariable("idUniversity")Integer IdUniversity){
        return partnerservice.checkAvailableUniversity(IdUniversity);
    }







    @GetMapping("/numStudentWithoutRatings")
    @ResponseBody
    @ApiOperation(value = "numStudentWithoutRatings")
    public int  numStudentWithoutRatings(){
        return userservice.numStudentWithoutRatings();
    }


    @GetMapping("/acceptedStudentsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "acceptedStudentsByUniversity")
    public List<User> acceptedStudentsByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return userservice.acceptedStudentsByUniversity(idUniversity);
    }
    @GetMapping("/numStudentsWithRatingsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "numStudentsWithRatingsByUniversity")
    public int numStudentsWithRatingsByUniversity(@PathVariable("idUniversity") Integer idUniversity){
        return userservice.numStudentsWithRatingsByUniversity(idUniversity);

    }



    @GetMapping("/studentDemands/{IdStudent}/{IdUniversity}")
    public int studentDemands(@PathVariable("IdStudent") Long IdStudent,@PathVariable("IdUniversity")  Integer IdUniversity){
        return userservice.studentDemands(IdStudent,IdUniversity);
    }

    @GetMapping("/getTimeAttendForDemandResponse/{idDemand}")
    @ResponseBody
    @ApiOperation(value = "getTimeAttendForDemandResponse")
    public long getTimeAttendForDemandResponse(@PathVariable("idDemand") int idDemand){
        return candiService.getTimeAttendForDemandResponse(idDemand);
    }
    @GetMapping("/getAverageWaitingTimeDemandByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "get Average WaitingTime Demand ByUniversity")
    public long getAverageWaitingTimeDemandByUniversity(@PathVariable("idUniversity")Integer idUniversity){
        return candiService.getAverageWaitingTimeDemandByUniversity(idUniversity);
    }


    @PostMapping("/addcomment")
    @ResponseBody
    @ApiOperation(value = "add comment ")
    public CommentUniversity save(@RequestBody CommentUniversity commentUniversity){
        return commentService.save(commentUniversity);
    }
    @PutMapping("/ Updatecomment")
    @ResponseBody
    @ApiOperation(value = "Update comment ")
    public CommentUniversity Update(@RequestBody CommentUniversity commentUniversity){
        return commentService.Update(commentUniversity);
    }
    @GetMapping("/getComment/{IdComment}")
    @ResponseBody
    @ApiOperation(value = "GET comment ")
    public CommentUniversity findById(@PathVariable("IdComment") Long id){
        return  commentService.findById(id);
    }

    @DeleteMapping ("/deleteById/{IdComment}")
    @ResponseBody
    @ApiOperation(value = "Delete comment of  Institution ")
    public void deleteById(@PathVariable("IdComment") Long id){
        commentService.deleteById(id);
    }

    @GetMapping("/findAllCommentsByUniversityId/{IdPartner}")
    @ResponseBody
    @ApiOperation(value = "find All Comments By UniversityId")
    public List<CommentUniversity> findAllByUniversityId(@PathVariable("IdPartner") Integer id){
        return commentService.findAllByUniversityId(id);
    }

    @PostMapping("/addCommentAndAffectToStudentAndUniversity/{idStudent}/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "add Comment And Affect To Student And University ")
    public void addCommentAndAffectToStudentAndUniversity(@RequestBody CommentUniversity commentUniversity, @PathVariable("idStudent")  Long idStudent, @PathVariable("idUniversity")Integer idUniversity){
        commentService.addCommentAndAffectToStudentAndUniversity(commentUniversity,idStudent,idUniversity);

    }

    @PutMapping("/addSignal/{idComment}")
    @ResponseBody
    @ApiOperation(value = "addSignal")
    public void addSignal(@PathVariable ("idComment")Long IdComment){
        commentService.addSignal(IdComment);
    }

    @PostMapping("/addRateAndAffectToStudentAndUniversity/{idStudent}/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "addDemandAndAssignToStudentAndUniversity ")
    public void addRateAndAffectToStudentAndUniversity(@RequestBody Rating rating, @PathVariable("idStudent") Long idStudent
            , @PathVariable("idUniversity") Integer idUniversity){
        rateService.addRateAndAffectToStudentAndUniversity(rating,idStudent,idUniversity);
    }

    @GetMapping("/getRatingByUniversityAndUser/{idUniversity}/{idStudent}")
    @ResponseBody
    @ApiOperation(value = "get Rating By University And User ")
    public List<Rating> getRatingByUniversityAndUser(@PathVariable("idUniversity") Integer idUniversity,
                                                     @PathVariable("idStudent") Long idStudent){
        return rateService.getRatingByUniversityAndUser(idUniversity,idStudent);
    }

    @PostMapping("/addEstimation/{idComment}")
    @ResponseBody
    @ApiOperation(value = "add Estimation ")
    public React save(@PathVariable("idComment") Long id, @RequestBody React react){
        return estimationService.save(id, react);
    }

    @GetMapping("/findAllEstimationByCommentId/{IdComment}")
    @ResponseBody
    @ApiOperation(value = "find All Estimation ByCommentId ")
    public List<React> findAllByCommentId(@PathVariable("IdComment") Long id){
        return estimationService.findAllByCommentId(id);
    }
    @GetMapping("/findAllEstimationByCommentIdAndEmoji/{IdComment}")
    @ResponseBody
    @ApiOperation(value = "findAllByEstimationComment IdAnd Emoji ")
    public List<React> findAllByCommentIdAndEmoji(@PathVariable("IdComment") Long idComment, Emoji em){
        return estimationService.findAllByCommentIdAndEmoji(idComment,em);
    }
    @GetMapping("/countAllByCommentId/{IdComment}")
    @ResponseBody
    @ApiOperation(value = "count All ByCommentId  ")
    public Long countAllByCommentId(@PathVariable("IdComment") Long idComment){
        return  estimationService.countAllByCommentId(idComment);

    }

    @GetMapping("/download/students.xlsx")
    public void downloadCsv1(HttpServletResponse response) throws IOException {
        List<CandidacyUniversity> candidacyUniversities =(List<CandidacyUniversity>) candidacyRepository.findAllAcceptedDemands() ;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=AcceptedStudents.xlsx");
        ByteArrayInputStream stream = exportExcelservice.studentsExcelFile(candidacyUniversities);
        IOUtils.copy(stream, response.getOutputStream());
    }

    @GetMapping("/SearchMulti/{sh}")
    @ResponseBody
    @ApiOperation(value = "SearchMulti ")
    public List<PartnerInstitution> SearchMulti(@PathVariable("sh") String keyword){
        return partnerservice.SearchMulti(keyword);
    }
    @GetMapping("/checkAllCommentsByUniversity/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "checkAllCommentsByUniversity")
    public void checkAllCommentsByUniversity(@PathVariable("idUniversity")Integer idUniversity){
        partnerservice.checkAllCommentsByUniversity(idUniversity);
    }

    @GetMapping("/statNumberStudentByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberStudentByUniversity")
    public List<DataPoint> statNumberStudentByUniversity(){
        return partnerservice.statNumberStudentByUniversity();
    }
    @GetMapping("/statNumberCommentsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberCommentsByUniversity")
    public List<DataPoint> statNumberCommentsByUniversity(){
        return partnerservice.statNumberCommentsByUniversity();
    }
    @GetMapping("/statNumberGoodRatingsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberGoodRatingsByUniversity")
    public List<DataPoint> statNumberGoodRatingsByUniversity(){
        return partnerservice.statNumberGoodRatingsByUniversity();
    }
    @GetMapping("/statNumberBadRatingsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberBadRatingsByUniversity")
    public List<DataPoint> statNumberBadRatingsByUniversity(){
        return partnerservice.statNumberBadRatingsByUniversity();
    }
    @GetMapping("/PercentageUniversitiesByArea")
    @ResponseBody
    @ApiOperation(value = "Percentage Universities  ByArea")
    public Map<String, Double> PercentageUniversitiesByArea(){
        return partnerservice.PercentageUniversitiesByArea();
    }

    @GetMapping("/statNumberHappyReactsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberHappyReactsByUniversity ")
    public List<DataPoint> statNumberHappyReactsByUniversity() {
        return estimationService.statNumberHappyReactsByUniversity();
    }
    @GetMapping("/statNumberAngryReactsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberAngryReactsByUniversity ")
    public List<DataPoint> statNumberAngryReactsByUniversity(){
        return estimationService.statNumberAngryReactsByUniversity();
    }
    @GetMapping("/statNumberLikeReactsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberLikeReactsByUniversity ")
    public List<DataPoint> statNumberLikeReactsByUniversity(){
        return estimationService.statNumberLikeReactsByUniversity();
    }
    @GetMapping("/statNumberDislikeReactsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberDislikeReactsByUniversity ")
    public List<DataPoint> statNumberDislikeReactsByUniversity(){
        return estimationService.statNumberDislikeReactsByUniversity();
    }


    @GetMapping("/statNumberSadReactsByUniversity")
    @ResponseBody
    @ApiOperation(value = "statNumberSadReactsByUniversity")
    public List<DataPoint> statNumberSadReactsByUniversity(){
        return estimationService.statNumberSadReactsByUniversity();
    }


    @PostMapping("/addReactForUniversity/{idStudent}/{idUniversity}")
    @ResponseBody
    @ApiOperation(value = "addReactForUniversity")
    public React addReactForUniversity(@PathVariable("idStudent") long id, @RequestBody React react,@PathVariable("idUniversity") Integer idUniversity){
        return estimationService.addReactForUniversity(id,react,idUniversity);
    }

    @GetMapping("/PercentageStudentsByNationality")
    @ResponseBody
    @ApiOperation(value = "Percentage Students ByNationality")
    public Map<String, Double> PercentageStudentsByNationality(){
        return userservice.PercentageStudentsByNationality();
    }




































}
