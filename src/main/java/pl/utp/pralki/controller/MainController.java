/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Darek
 */
@Controller
public class MainController {

    @RequestMapping("")
    public String home() {
        return "index.html";
    }
    
    @RequestMapping("/admin")
    public String admin() {
        return "admin.html";
    }
    
    @RequestMapping("/recepcja")
    public String recepcja() {
        return "recepcja.html";
    }
    
    @RequestMapping("/b")
    public String bladLog() {
        return "bindex.html";
    }

    @RequestMapping("/test")
    public String test() {
        return "test.html";
    }

    @RequestMapping("/panel")
    public String panel() {
        return "panel.html";
    }
    
    @RequestMapping("/wyloguj")
    public String logout() {
        return "wyl.html";
    }

    @RequestMapping("/zalogowany")
    public String zalogowany() {
        return "zalogowany.html";
    }

    @RequestMapping("/style/{style}.css")
    public String css(@PathVariable("style") String name) {
        return name + ".css";
    }

    @RequestMapping("/help")
    public String help() {
        return "help.html";
    }

    @RequestMapping("/addDormitory")
    public String addDormitory() {
        return "addDormitory.html";
    }

    @RequestMapping("/addWasher")
    public String addWasher() {
        return "addWasher.html";
    }
}
