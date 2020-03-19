package reporting.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

@CrossOrigin("*")
@RestController
@RequestMapping("/java/api")
class Api {

    final static String UPLOAD_FILE_EXTN_PATTERN = "([^\\s]+(\\.(?i)(docx))$)";
    private UtilityHelper uHelper= new UtilityHelper();

    @Value("${template.path}")
    private String templatePath;

    @Value("${download.path}")
    private String downloadPath;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("select a file to upload");
            }
            if (!uHelper.matchingPattern(UPLOAD_FILE_EXTN_PATTERN, file.getOriginalFilename())) {
                throw new RuntimeException("only supportive file extension are docx");
            }
            Path path = Paths.get(templatePath);
            Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
            return ResponseEntity.ok().body("{ \"result\": \"SUCCESS\"}");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @GetMapping("/download")
    public ResponseEntity download(@RequestParam(value = "fileName", required = true) String fileName) {
        try {
            Path path =  Paths.get(downloadPath).resolve(fileName);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("file not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity readTemplate(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "path",defaultValue = "DOWNLOAD") String path) {
        Path msWordPath;
        if("DOWNLOAD".equals(path)){
            msWordPath = Paths.get(downloadPath+fileName);
        }else{
            msWordPath = Paths.get(templatePath+fileName);
        }

        XWPFDocument document = null;
        try {
            document = new XWPFDocument(Files.newInputStream(msWordPath));
            XWPFWordExtractor we = new XWPFWordExtractor(document);
            document.close();
            return ResponseEntity.ok(we.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/templates/all")
    public ResponseEntity all() {
        List<Template> templates = uHelper.grabFileSystem(templatePath,"FILE");
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/templates/prop/{fileName}")
    public ResponseEntity templateProperties(@PathVariable(value = "fileName") String fileName) {
        Path msWordPath = Paths.get(templatePath+fileName);
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(Files.newInputStream(msWordPath));
            XWPFWordExtractor we = new XWPFWordExtractor(document);
            document.close();
            String regex = "(\\{\\{.*?\\}\\})";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(we.getText());
            Set<String> props = new HashSet<>();
            while (matcher.find())
            {   /*
                System.out.print("Start index: " + matcher.start());
                System.out.print(" End index: " + matcher.end() + " ");
                System.out.println(matcher.group());*/
                int len = matcher.group().length();
                props.add(matcher.group().substring(2,(len - 2)));
            }
            return ResponseEntity.ok(props);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO enhance this api which will support for all folder/path
    @GetMapping("/filesystem")
    public ResponseEntity fileSystem(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "fromFolder", defaultValue = "DEFAULT_PATH") String fromFolder){
        if("DEFAULT_PATH".equals(fromFolder)){
            return ResponseEntity.ok(uHelper.grabFileSystem(downloadPath,type));
        } else {
            return ResponseEntity.ok(uHelper.grabFileSystem(downloadPath+fromFolder,type));
        }

    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Template {
    private String name;
    private String path;
    private String lastModified;
}

/**
 * Utility class
 */
class UtilityHelper {

    public String formatTheDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public boolean matchingPattern(String pattern, String matchFor) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(matchFor);
        return matcher.matches();
    }

    public String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        BufferedReader reader;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            if(p.exitValue() == 0){
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine())!= null) {
                    output.append(line + "\n");
                }
                return "{ \"result\": \"SUCCESS\" , \"debug\" : "+output.toString()+"}";
            }else {
                reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
                throw new RuntimeException(output.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Template> grabFileSystem(String path, String type) {
        List<Template> templates = new ArrayList<>();
        Template template;
        File file = new File(path);
        if(!file.exists()){
            throw new RuntimeException("FILE or FOLDER does not exist");
        }
        for(File f : file.listFiles()) {
            if("FILE".equals(type)) {
                if(f.isFile()) {
                    template = new Template(f.getName(),getExpectedPath(f),formatTheDate(f.lastModified()));
                    templates.add(template);
                }
            } else {
                if(f.isDirectory()) {
                    template = new Template(f.getName(),getExpectedPath(f),formatTheDate(f.lastModified()));
                    templates.add(template);
                }
            }
        }
        //sorting on folder date wise
        if("FOLDER".equals(type)) {
            SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
            Collections.sort(templates, (s1, s2) -> {
                try {
                    return format.parse(s1.getName()).
                            compareTo(format.parse(s2.getName()));
                } catch (ParseException e) {
                    return 0;
                }
            });
        }
        return templates;
    }

    private String getExpectedPath(File f) {
        String[] paths = f.getAbsolutePath().split("/");
        if(paths.length > 0) {
            String p = paths[paths.length -2]+"/"+paths[paths.length -1];
            return p;
        } else {
            return f.getAbsolutePath();
        }
    }
}
