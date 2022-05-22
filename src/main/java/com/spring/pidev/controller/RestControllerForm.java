package com.spring.pidev.controller;


import com.spring.pidev.config.BadWordConfig;
import com.spring.pidev.model.*;
import com.spring.pidev.model.*;
import com.spring.pidev.repo.DatabaseFileRepository;
import com.spring.pidev.repo.IResultRepo;
import com.spring.pidev.service.*;
import com.spring.pidev.payLoad.Response;
import com.itextpdf.text.DocumentException;
import com.spring.pidev.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/Courses")
@CrossOrigin()
public class RestControllerForm {

    @Autowired
    private IServiceFormation iServiceFormation;

    @Autowired
    private IServicesQuiz iServicesQuiz;
    @Autowired
    private IResultRepo iResultRepo;
    @Autowired
    exportExcel exportExcelservice;
    @Autowired
    DatabaseFileRepository databaseFileRepository;
    @Autowired
    private DatabaseFileService fileStorageService;

    private final PDFGeneratorService pdfGeneratorService;

    BadWordConfig badWordConfig  = new BadWordConfig();

    @Autowired
    private exportPdf export;



    public RestControllerForm(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }


    @ApiOperation(value = "Add Formateur")
    @PostMapping("/addFormateur")
    public void addFormateur(@RequestBody User formateur) {
        iServiceFormation.ajouterFormateur(formateur);
    }

    @GetMapping("/pdf/generate")
    @ApiOperation(value = " Generate PDF ")
    public void generatePDF(HttpServletResponse response,String p1 ,String p2 ,String qrcode) throws IOException, DocumentException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.exportFor(response,p1,p2,qrcode);
    }






    @RequestMapping(value = {"/ajouterFormation"}, method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = " ajouter Formation ")
    public Formation addFormation(@RequestBody Formation formation){

       return iServiceFormation.addFormation(formation);
    }


    @ApiOperation(value = "update Formation")
    @PutMapping("/updateFormation/{id}")
    @ResponseBody
    public void updateFormation(@RequestBody Formation formation,@PathVariable(name = "id") Integer idFormateur){
        iServiceFormation.updateFormation(formation,idFormateur);
    }

    @ApiOperation(value = "update Result")
    @PutMapping("/upDateResult/{idQ}/{idU}")
    @ResponseBody
    public Result upDateResult(@RequestBody Result result,@PathVariable(name = "idQ") Integer idQ,@PathVariable(name = "idU") Long idUser)
    {
        return  iServiceFormation.upDateResult(result, idQ, idUser);
    }

    @ApiOperation(value = "upDate Comment")
    @PutMapping("/upDateComment/{idf}/{idU}")
    @ResponseBody
    public PostComments upDateComment(@RequestBody PostComments postComments, @PathVariable(name = "idf") Integer idF, @PathVariable(name = "idU") Long idUser)
    {
        return iServiceFormation.upDateComment(postComments, idF, idUser);
    }


    @ApiOperation(value = "Delete Formation")
    @GetMapping("/deleteFormation/{id}")
    @ResponseBody
    public void deleteFormation(@PathVariable(name = "id") Integer idForm){
        iServiceFormation.deleteFormation(idForm);
    }

    @GetMapping("/exportPDF")
    @ResponseBody
    public ResponseEntity<InputStreamResource> exportPDF() throws IOException {
        ByteArrayInputStream bais = export.FormationPDFReport(iServiceFormation.afficherFormation());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","Inline ;filename=formation.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
    }

    @ApiOperation(value = "Retrieve All Formation")
    @GetMapping("/retrieveFormation")
    @ResponseBody
    public List<Formation> afficherFormation(){

        return iServiceFormation.afficherFormation();
    }




    @ApiOperation(value = "Retrieve All Formateur")
    @GetMapping("/retrieveFormateur")
    @ResponseBody
    public List<User> afficherFormateur(){
        return iServiceFormation.afficherFormateur();
    }

    @ApiOperation(value = "Retrieve All Apprenant")
    @GetMapping("/retrieveApprenant")
    @ResponseBody
    public List<User> afficherApprenant()
    {
        return iServiceFormation.afficherApprenant();
    }



    @RequestMapping(value = {"/ajouterEtAffecterFormationAFormateur/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "ajouter Et Affecter Formation A Formateur  ")
    public void ajouterEtAffecterFormationAFormateur(@RequestBody Formation formation,@PathVariable(name = "id") Long idFormateur)
    {
        iServiceFormation.ajouterEtAffecterFormationAFormateur(formation, idFormateur);
    }



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleUpload(HttpServletRequest request) {
        System.out.println(System.getProperty("java.io.tmpdir"));
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        factory.setFileCleaningTracker(null);
        // Configure a repository (to ensure a secure temp location is used)
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();

                if (!item.isFormField()) {
                    try (InputStream uploadedStream = item.getInputStream();
                         OutputStream out = new FileOutputStream("file.mov");) {
                        IOUtils.copy(uploadedStream, out);

                        out.close();
                    }
                }
            }
            // Parse the request with Streaming API
            upload = new ServletFileUpload();
            FileItemIterator iterStream = upload.getItemIterator(request);
            while (iterStream.hasNext()) {
                FileItemStream item = iterStream.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                    //Process the InputStream
                } else {
                    //process form fields
                    String formFieldValue = Streams.asString(stream);
                }
            }
            return "success!";
        } catch (IOException | FileUploadException ex) {
            return "failed: " + ex.getMessage();
        }
    }

    @PostMapping("/uploadFile/{idF}")
    @ResponseBody
    public Response uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(name = "idF") Integer idFormation) {
        DatabaseFile fileName = fileStorageService.storeFile(file,idFormation);


        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getId())
                .toUriString();

        return new Response(fileName.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles/{idF}")
    @ResponseBody
    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@PathVariable(name = "idF") Integer idFormation) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file,idFormation))
                .collect(Collectors.toList());
    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
        // Load file as Resource
        DatabaseFile databaseFile = fileStorageService.getFile(fileName);


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }





    @GetMapping(path = { "/get/{imageid}" })
    public DatabaseFile getImage(@PathVariable("imageid") String imageName) {

        final Optional<DatabaseFile> retrievedImage = databaseFileRepository.findById(imageName);
        DatabaseFile img = new DatabaseFile(retrievedImage.get().getFileName(), retrievedImage.get().getFileType(),
                retrievedImage.get().getData());
        return img;
    }



    @ApiOperation(value = "Récupérer filesName Formation")
    @GetMapping("/getFiles/{idF}")
    @ResponseBody
    public List<DatabaseFile> getfileFormation(@PathVariable(name = "idF") Integer idF)
    {
     return iServiceFormation.getfileFormation(idF);
    }




    @RequestMapping(value = {"/affecterApprenantFormationWithMax/{idA}/{idF}"}, method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "ajouter Et Affecter Formation A Formateur with Max  ")
    public void affecterApprenantFormationWithMax(@PathVariable(name = "idA") Long idApprenant,@PathVariable(name = "idF") Integer idFormation)
    {

        iServiceFormation. affecterApprenantFormationWithMax(idApprenant, idFormation);
    }



    @ApiOperation(value = "Récupérer Revenu Brut Formateur Remuneration By Date")
    @GetMapping("/getFormateurRemunerationByDate/{idFormateur}/{startDate}/{endDate}")
    @ResponseBody
    public Integer getFormateurRemunerationByDate(@PathVariable(name = "idFormateur") Long idFormateur, @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut, @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin)
    {
        return iServiceFormation.getFormateurRemunerationByDate(idFormateur, dateDebut, dateFin);
    }


    @ApiOperation(value = "Récupérer Revenu Brut Formatation")
    @GetMapping("/getRevenueByFormation/{idF}")
    @ResponseBody
    public Integer getRevenueByFormation(@PathVariable(name = "idF") Integer idFormation)
    {
        return iServiceFormation.getRevenueByFormation(idFormation);
    }


    @ApiOperation(value = " get Formateur Remuneration Max Salaire ")
    @GetMapping("/getFormateurRemunerationMaxSalaire")
    @ResponseBody
    public User getFormateurRemunerationMaxSalaire() throws MessagingException {
      return this.iServiceFormation.getFormateurRemunerationMaxSalaire();
    }

    @ApiOperation(value = " get Formateur Remuneration Max Salaire trier ")
    @GetMapping("/getFormateurRemunerationMaxSalaireTrie")
    @ResponseBody
    public TreeMap<Integer, User> getFormateurRemunerationMaxSalaireTrie()
    {
        return this.iServiceFormation.getFormateurRemunerationMaxSalaireTrie();
    }


    //get formateur from Formation by id
    @ApiOperation(value = "get Formateur from Formation by id")
    @GetMapping("/getFormateurFromFormation/{idF}")
    @ResponseBody
    public User getFormateurFromFormation(@PathVariable(name = "idF") Integer idFormation)
    {
        return iServiceFormation.getFormateurFromFormation(idFormation);
    }


    @ApiOperation(value = " get Formateur Max Salaire trier ")
    @GetMapping("/getFormateurMaxSalaireTrie")
    @ResponseBody
    public List<Object> getFormateurRemunerationByDateTrie()
    {
        return iServiceFormation.getFormateurRemunerationByDateTrie();
    }


    @ApiOperation(value = "Récupérer Nbr Apprenant By Formation")
    @GetMapping("/getNbrApprenantByFormation/{t}")
    @ResponseBody
    public Integer getNbrApprenantByFormation(@PathVariable(name = "t") String title)
    {
        return iServiceFormation.getNbrApprenantByFormation(title);
    }

    @ApiOperation(value = "Récupérer Nbr Formation By Apprenant ")
    @GetMapping("/getNbrFormationByApprenant/{id}/{domain}/{startDate}/{endDate}")
    @ResponseBody
    public Integer getNbrFormationByApprenant(@PathVariable(name = "id") Long idApp, @PathVariable(name = "domain") Domain domain, @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut, @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin){
        return iServiceFormation.getNbrFormationByApprenant(idApp,domain, dateDebut, dateFin);
    }



    @ApiOperation(value = "Récupérer Nbr Apprenantt By Formation")
    @GetMapping("/NbrApprenantByFormation")
    public List<Object[]> getNbrApprenantByFormation()
    {
        return iServiceFormation.getNbrApprenantByFormation();
    }

    @ApiOperation(value = "Récupérer Liste Apprenant By Formation")
    @GetMapping("/ApprenantByFormation/{idF}")
    @ResponseBody
    public List<User> getApprenantByFormation(@PathVariable(name = "idF") Integer idF){
        return iServiceFormation.getApprenantByFormation(idF);
    }

    @ApiOperation(value = "Formateur with Max Tarif")
    @GetMapping("/FormateurwithMaxHo")
    public User FormateurwithMaxHo()
    {
        return iServiceFormation.FormateurwithMaxHo();
    }



    @PostMapping("/addQuiz/{idF}")
    @ApiOperation(value = " add Quiz ")
    public void addQuiz(@RequestBody QuizCourses quiz, @PathVariable(name = "idF") Integer idF)
    {
        iServicesQuiz.addQuiz(quiz,idF);
    }

    @PostMapping("/addQuestionAndAsigntoQuiz/{idQuiz}")
    @ApiOperation(value = " add Question And Asign To Quiz ")
    public void addQuestionAndAsigntoQuiz(@RequestBody QuestionCourses question, @PathVariable(name = "idQuiz")  Integer idQuiz)
    {
        iServicesQuiz.addQuestionAndAsigntoQuiz(question, idQuiz);
    }




    @ApiOperation(value = "get Quiz Question")
    @GetMapping("/getQuizQuestion/{id}")
    public List<QuestionCourses> getQuizQuestion(@PathVariable("id") Integer idQ)
    {
        return iServicesQuiz.getQuizQuestion(idQ);
    }

    @ApiOperation(value = "get Question by quiz")
    @GetMapping("/getQuestionByQuiz/{id}")
    public List<QuestionCourses> getQuestionByQuiz(@PathVariable("id") Integer idQuiz)
    {
        return iServicesQuiz.getQuestionByQuiz(idQuiz);
    }





    @ApiOperation(value = "get Quiz Question M2")
    @GetMapping("/getQuizQuestionM")
    public List<QuestionCourses> getQuestions()
    {
        return iServicesQuiz.getQuestions();
    }


    @PostMapping("/SaveScore/{idU}/{idQ}")
    @ApiOperation(value = " Save Score Quiz ")
    public Integer saveScore(@RequestBody Result result,@PathVariable(name = "idU") Long idUser,@PathVariable(name = "idQ") Integer idQuiz)
    {
       return   this.iServicesQuiz.saveScore(result,idUser,idQuiz);
    }

/*
    @ApiOperation(value = " Apprenent With Max Score In Formation ")
    @GetMapping("/ApprenentwithMaxScoreInFormation/{idF}")
    @ResponseBody
    public User ApprenentwithMaxScoreInFormation(@PathVariable(name = "idF") Integer id)
    {
        return this.iServicesQuiz.ApprenentwithMaxScoreInFormation(id);
    }


 */

    //get Comment By likes Et Dislikes
    @ApiOperation(value = "get Comment By likes Et Dislikes")
    @GetMapping("/getCommentByLikes/{id}")
    @ResponseBody
    public List<Object[]> getCommentByLikes(@PathVariable(name = "id") Integer id){
        return iServiceFormation.getCommentBylikesEtDislikes(id);
    }



    @ApiOperation(value = " Max Score In Formation")
    @GetMapping("/MaxScoreInFormation")
    @ResponseBody
    public Integer MaxScoreInFormation()
    {
        return this.iServicesQuiz.MaxScoreInFormation();
    }


    //get nbr of likes by comments
    @ApiOperation(value = "get nbr of likes by comments")
    @GetMapping("/getNbrLikesByComment/{id}")
    @ResponseBody
    public Integer getNbrLikesByComment(@PathVariable(name = "id") Integer id)
    {
        return this.iServiceFormation.getNbrLikesByComments(id);
    }

    // get nbr of dislikes by comments
    @ApiOperation(value = "get nbr of dislikes by comments")
    @GetMapping("/getNbrDislikesByComment/{id}")
    @ResponseBody
    public Integer getNbrDislikesByComment(@PathVariable(name = "id") Integer id)
    {
        return this.iServiceFormation.getNbrDislikesByComments(id);
    }

    @ApiOperation(value = "get Comments By Formation")
    @GetMapping("/getCommentsByFormation/{id}")
    @ResponseBody
    public List<PostComments> getCommentsByFormation(@PathVariable(name = "id") Integer idF)
    {
        return this.iServiceFormation.getCommentsByFormation(idF);
    }


    @PutMapping("/addLikes/{idC}/{idU}")
    @ApiOperation(value = " add Likes ")
    public void likeComments(@PathVariable(name = "idC") Integer idC ,@PathVariable(name = "idU") Long idUser ){
        iServiceFormation.likeComments(idC,idUser);
    }


    @PutMapping("/addDisLikes/{idC}/{idU}")
    @ApiOperation(value = " add DisLikes ")
    public void dislikeComments(@PathVariable(name = "idC") Integer idC,@PathVariable(name = "idU") Long idUser )
    {
        iServiceFormation.dislikeComments(idC,idUser);
    }

//get formation by id
    @ApiOperation(value = "get formation by id")
    @GetMapping("/getFormationById/{id}")
    public Formation getFormationById(@PathVariable(name = "id") Integer id)
    {
        return iServiceFormation.getFormationById(id);
    }




    @PutMapping("/FormationWIthRate/{idF}/{nbr}")
    @ApiOperation(value = " add Rating ")
    public void FormationWIthRate(@PathVariable(name = "idF") Integer idF,@PathVariable(name = "nbr") Double rate)
    {
        iServiceFormation.FormationWithRate(idF, rate);
    }

    @PostMapping("/addComments/{idF}/{idU}")
    @ApiOperation(value = " ajouter Comments ")
   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public Integer addComments(@RequestBody PostComments postComments,@PathVariable(name = "idF") Integer idF,@PathVariable(name = "idU") Long idUser)
    {
        PostComments p = new PostComments();
        p.setMessage(badWordConfig.filterText(postComments.getMessage()));
        p.setLikes(postComments.getLikes());
        p.setDislikes(postComments.getDislikes());
        p.setCreateAt(postComments.getCreateAt());
       return  iServiceFormation.addComments(p,idF,idUser);

    }

    @PostMapping("/desaffecterApprenant/{idU}/{idF}")
    @ApiOperation(value = " dessaffecter Apprenant ")
    @ResponseBody
    public void desaffecterApprenant(@PathVariable(name = "idU") Long idUser,@PathVariable(name = "idF") Integer idF)
    {
        iServiceFormation.desaffecterApprenant(idUser, idF);
    }


    //get all comments
    @ApiOperation(value = "get all comments")
    @GetMapping("/getAllComments")
    public List<PostComments> getAllComments()
    {
        return iServiceFormation.getAllComments();
    }

    @ApiOperation(value = "get all search historique")
    @GetMapping("/getAllSearch")
    public  List<Object[]> getAllSearch()
    {
        return iServiceFormation.getAllSearch();
    }

    @ApiOperation(value = " list Quiz tested bu User  ")
    @GetMapping("/listQuiqtestedbuUser/{id}/{idf}")
    @ResponseBody
    public List<QuizCourses> listQuiqtestedbuUser(@PathVariable(name = "id") Long idU ,@PathVariable(name = "idf") Integer idF)
    {
        return iServicesQuiz.listQuiqtestedbuUser(idU,idF);
    }

    @ApiOperation(value = "get pourcentage")
    @GetMapping("/getPourcentage")
    public List<Object[]> getPourcentage()
    {
        return iServiceFormation.getPourcentage();
    }


    @ApiOperation(value = "Download The List of Result Quiz")
    @GetMapping("/download/ResultQuiz.xlsx")
    public void downloadCsv(HttpServletResponse response) throws IOException {
     //   SXSSFWorkbook wb = new SXSSFWorkbook(100);

        List<Result> list =(List<Result>) iResultRepo.findAll();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=ResultQuiz.xlsx");
        ByteArrayInputStream stream = exportExcelservice.quizexportexcel(list);

        FileOutputStream out = new FileOutputStream("/Users/macos/IdeaProjects/springPidev/src/main/resources/static/ResultQuiz.xlsx");

        byte[] buf = new byte[1024];
        int len;
        while ((len = stream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
/* version 2
        ByteArrayInputStream stream  = <<Assign stream>>;
        byte[] bytes = new byte[1024];
        stream.read(bytes);
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("FileLocation")));
        writer.write(new String(bytes));
        writer.close();

 */

        IOUtils.copy(stream, response.getOutputStream());



    }

    @ApiOperation(value = "Delete Comments")
    @GetMapping("/deleteComments/{id}")
    @ResponseBody
    public void deleteComments(@PathVariable(name = "id") Integer idC)
    {
        iServiceFormation.deleteComments(idC);
    }

    @ApiOperation(value = "Delete files")
    @GetMapping("/deleteFiles/{fileName:.+}")
    public void deleteFiles(@PathVariable String fileName)
    {
        iServiceFormation.deleteFiles(fileName);
    }

    @ApiOperation(value = " get Apprenant With Score Quiz ")
    @GetMapping("/getAllApprenantWithScoreQuiz/{idF}")
    @ResponseBody
    List<User> getApprenantWithScoreQuiz(@PathVariable("idF") Integer id)
    {
        return this.iServicesQuiz.getApprenantWithScoreQuiz(id);
    }

    @ApiOperation(value = " get Apprenant With Score  ")
    @GetMapping("/ApprenentwithMaxScore/{idF}")
    @ResponseBody
    public List<Object> ApprenentwithMaxScore(@PathVariable("idF") Integer id)
    {
        return iServicesQuiz.ApprenentwithMaxScore(id);
    }


    @ApiOperation(value = " get Apprenant With Score in All Quiz formation ")
    @GetMapping("/ApprenentwithMaxScoreQuiz/{idF}")
    @ResponseBody
    public User ApprenentwithMaxScoreQuiz(@PathVariable("idF") Integer id)
    {
        return iServicesQuiz.ApprenentwithMaxScoreQuiz(id);
    }

/*
    @ApiOperation(value = " get Result Formateur")
    @GetMapping("/ResultQuiz")
    @ResponseBody
    public List<Result> ResultQuiz() throws IOException, MessagingException {
        return iServicesQuiz.ResultQuiz();
    }
 */

    @ApiOperation(value = " get Pourcentage Courses By Domain  ")
    @GetMapping("/PourcentageCoursesByDomain")
    @ResponseBody
    public Map<String,Double>  PourcentageCoursesByDomain() throws IOException, MessagingException {
        return iServiceFormation.PourcentageCoursesByDomain();
    }









    @ApiOperation(value = " Search Multiple  ")
    @GetMapping("/SearchMultiple/{keyword}")
    @ResponseBody
    public List<Formation> SearchMultiple(@PathVariable("keyword") String key)
    {
        return iServiceFormation.SearchMultiple(key);
    }

    @ApiOperation(value = " get Quiz By Formation ")
    @GetMapping("/getQuizByFormation/{id}")
    @ResponseBody
    public List<QuizCourses> getQuizByFormation(@PathVariable("id") Integer idF)
    {
        return this.iServicesQuiz.getQuizByFormation(idF);
    }

    @ApiOperation(value = "Delete Quiz")
    @GetMapping("/DeleteQuiz/{id}")
    @ResponseBody
    public void DeleteQuiz(@PathVariable("id") Integer idQ)
    {
        this.iServicesQuiz.DeleteQuiz(idQ);
    }


    @ApiOperation(value = "Delete Question")
    @GetMapping("/DeleteQuestion/{id}")
    @ResponseBody
    public void DeleteQuestion(@PathVariable("id") Integer idQ)
    {
        this.iServicesQuiz.DeleteQuestion(idQ);
    }

    @ApiOperation(value = " Search Historique ")
    @PostMapping("/SearchHistorique/{keyword}")
    @ResponseBody
    public void SearchHistorique(@PathVariable("keyword") String keyword)
    {
        iServiceFormation.SearchHistorique(keyword);
    }


    @ApiOperation(value = " get Top Score  ")
    @GetMapping("/getTopScore")
    @ResponseBody
    public List<Result> getTopScore()
    {
     return iServicesQuiz.getTopScore();
    }

    @ApiOperation(value = " get Score  ")
    @GetMapping("/getScore/{id}")
    @ResponseBody
    public Integer getScore(@PathVariable("id") Long idU)
    {
        return iServicesQuiz.getScore(idU);
    }



    //get formation by formateur
    @ApiOperation(value = " get Formation By Formateur ")
    @GetMapping("/getFormationByFormateur/{id}")
    @ResponseBody
    public List<Formation> getFormationByFormateur(@PathVariable("id") Long id)
    {
        return iServiceFormation.getFormationByFormateur(id);
    }

    //get formation by apprenant
    @ApiOperation(value = " get Formation By Apprenant ")
    @GetMapping("/getFormationByApprenant/{id}")
    @ResponseBody
    public List<Formation> getFormationByApprenant(@PathVariable("id") Long id)
    {
        return iServiceFormation.getFormationByApprenant(id);
    }

}
