/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.activation.FileTypeMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Darek
 */
@RequestMapping("/img")
@RestController
public class ImageController {

//    @RequestMapping(value = "/img/{name}", method = RequestMethod.GET,
//            produces = MediaType.IMAGE_PNG_VALUE)
//
//    public void getImage(HttpServletResponse response, @PathVariable("style") String name) throws IOException {
//
//        var imgFile = new ClassPathResource("static/views/img/" + name);
//
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
//    }
    
    
        @RequestMapping("/{name}")
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {
        File img = new File("src/main/resources/static/views/img/"+name);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }
}
