package ru.saidgadjiev.overtalk.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.overtalk.application.model.ProjectDetails;

@Controller
public class FileController {

    @ResponseBody
    @RequestMapping(value = "/data/fileupload", method = RequestMethod.POST,  consumes = {"multipart/form-data"})
    public String postFile(@RequestPart(value="data") ProjectDetails data, @RequestPart(value="file", required=false) MultipartFile file) throws Exception {
        System.out.println("data = " + data);

        return "OK!";
    }
}
