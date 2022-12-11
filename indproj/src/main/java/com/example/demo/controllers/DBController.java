package com.example.demo.controllers;

import com.example.demo.Service.ConsoleService;
import com.example.demo.config.DBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("backup")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class DBController {

    @GetMapping
    public String getMainPage(Model model) {

        model.addAttribute("restoreFiles", getRestoreList());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "backup/main";
    }

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    ServletContext context;

    public List<String> getRestoreList() {

        Path path = Paths.get("dumps");

        File folder = new File(path.toUri());

        List<String> dumps = new ArrayList<>();

        for(File dump: folder.listFiles()) {
            if(dump.isFile()) {
                dumps.add(dump.getName());
            }
        }
        Collections.reverse(dumps);
        return dumps;
    }
    @GetMapping("downloadSQL")
    @Deprecated
    public ResponseEntity<Resource> downloadDB(HttpServletResponse response, @RequestParam(value = "withData", required = false) Boolean withData, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DBConfig.initField();
        String command = String.format("mysqldump --no-tablespaces --column-statistics=0 -u%s -h%s %s > %s",
                DBConfig.username, DBConfig.host, DBConfig.dbname, "load/" + DBConfig.dbname + ".sql");
        if(withData == null) {
            command += " --no-data";
        }
        //String command2 = "mysqldump --no-tablespaces --column-statistics=0 -uf0723938_aviacom -pJ3FnBfhM -hf0723938.xsph.ru f0723938_aviacom > load/f0723938_aviacom.sql";
        try {
            ConsoleService.exec(command);
            String uri = Paths.get("load/" + DBConfig.dbname + ".sql").toUri().toString();
            Resource resource = resourceLoader.getResource(uri);
            System.out.println(uri);
            ResponseEntity<Resource> body = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
            return body;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("createDump")
    @Deprecated
    public String createDump(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DBConfig.initField();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
        String filename = sdf.format(new Date()) + "aquaPark.sql";
        String command = String.format("mysqldump --column-statistics=0 -u%s -p%s -h%s --databases %s > %s",
                DBConfig.username, DBConfig.password, DBConfig.host, DBConfig.dbname, "dumps/" + filename);
        try {
            ConsoleService.exec(command);
            model.addAttribute("restoreFiles", getRestoreList());
            return "backup/main";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("deleteDump")
    public String deleteDump(Model model, @RequestParam("filename") String filename) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        File file = new File(Paths.get("dumps/" + filename).toUri());

        Files.deleteIfExists(file.toPath());

        model.addAttribute("restoreFiles", getRestoreList());
        return "backup/main";
    }

    @PostMapping("restore")
    @Deprecated
    public String restore(Model model, @RequestParam("filename") String filename) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        restore(filename, "dumps", model);
        return "backup/main";
    }

    private void restore(String filename, String folder,  Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DBConfig.initField();
        File file = new File(Paths.get( folder + "/" + filename).toUri());

        String command = String.format("mysql -u%s -p%s -h%s %s < %s",
                DBConfig.username, DBConfig.password, DBConfig.host, DBConfig.dbname , file.toPath());
        try {
            ConsoleService.exec(command);
            model.addAttribute("restoreFiles", getRestoreList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("upload")
    public String uploadRestore(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Path path = Paths.get("load/" + "customDump.sql");

        File customDump = new File(path.toUri());

        FileOutputStream fos = new FileOutputStream(customDump);
        fos.write(file.getBytes());
        fos.close();

        restore("customDump.sql", "load", model);
        return "backup/main";
    }
}