/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.pralki.database.DatabaseConfigurator;
import pl.utp.pralki.entities.Dormitory;
import pl.utp.pralki.entities.Laundry;

/**
 *
 * @author Darek
 */
@RequestMapping("/aka")
@RestController
public class DormsController {

    @RequestMapping("/all")
    public List<Dormitory> allDorms() {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Dormitory> dorms = session.createQuery("from Dormitory").list();
        session.close();
        sessionFactory.close();
        return dorms;
    }
    
    @RequestMapping("/l/{dormsName}")
    public List<Laundry> laundry(@PathVariable("dormsName") String dormitoryName) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Laundry> laundries = session.createQuery("from Laundry").list();
        laundries=laundries.stream().filter(l->l.getDormitory().getName().equals(dormitoryName)).collect(Collectors.toList());
        session.close();
        sessionFactory.close();
        return laundries;
    }

    @RequestMapping("/add/{name}")
    public List<String> addDormitory(@PathVariable("name") String name) {
        List<String> mess = new ArrayList<>();
        mess.add("Error");
        if (!name.equals("")) {
            try {
                Dormitory dormitory = new Dormitory();
                dormitory.setName(name);
                //dormsRepository.save(dormitory);
                SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
                Session session = sessionFactory.openSession();
                session.save(dormitory);
                session.close();
                sessionFactory.close();
                mess.set(0, "Success");
            } catch (Exception ex) {
                System.out.println(ex.toString());
                mess.set(0, ex.toString());
            }
        }
        return mess;
    }
    
    

    @RequestMapping("/del/{idDormitory}")
    public List<String> deleteDormitory(@PathVariable("idDormitory") int idDormitory) {
        List<String> mess = new ArrayList<>();
        mess.add("Error");
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Dormitory dormitory = session.get(Dormitory.class, idDormitory);
            Transaction tx = session.beginTransaction();
            session.delete(dormitory);
            tx.commit();
            session.close();
            sessionFactory.close();
            mess.set(0, "Success");
            mess.add(dormitory.getName());
        } catch (Exception ex) {
            mess.set(0, "Exception");
        }
        return mess;
    }

    @RequestMapping("/files")
    public List<String> files() {
        List<String> list = new ArrayList<>();
        try {
            File folder = new File("src/main/resources/static/views");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                list.add(file.toString());
            }


//            ClassLoader classLoader = getClass().getClassLoader();
//            File file = new File(classLoader.getResource("static").getFile());
//            list.add(file.toString());
            
//File resourcesDirectory = new File("src/main/resources");
            
            
        } catch (Exception ex) {
            list.add(ex.toString());
        }
        return list;
    }
   
}
