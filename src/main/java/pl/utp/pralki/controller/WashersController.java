/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

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
import pl.utp.pralki.entities.Washer;

/**
 *
 * @author Darek
 */
@RequestMapping("/w")
@RestController
public class WashersController {

    @RequestMapping("/all")
    public List<Washer> allWashers() {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Washer> washers = session.createQuery("from Washer").list();
        session.close();
        sessionFactory.close();
        return washers;
    }

    @RequestMapping("/id/{id}")
    public Washer washer(@PathVariable("id") int idWasher) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Washer washer = session.get(Washer.class, idWasher);
        session.close();
        sessionFactory.close();
        return washer;
    }

    @RequestMapping("/del/{id}")
    public boolean delWasher(@PathVariable("id") int idWasher) {
        boolean ok = false;
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Washer washer = session.get(Washer.class, idWasher);
        try{
            Transaction transaction=session.beginTransaction();
            session.delete(washer);
            transaction.commit();
            ok=true;
        }
        catch(Exception ex)
        {
            ok=false;
        }
        session.close();
        sessionFactory.close();
        return ok;
    }

    @RequestMapping("laundry/{id}")
    public Laundry laundry(@PathVariable("id") int idLaundry) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Laundry laundry = session.get(Laundry.class, idLaundry);
        session.close();
        sessionFactory.close();
        return laundry;
    }

    @RequestMapping("/{dormitoryName}")
    public List<Washer> washerByDormitory(@PathVariable("dormitoryName") String dormitoryName) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Washer> washers = session.createQuery("from Washer").list();
        washers = washers.stream().filter(w -> w.getLaundry().getDormitory().getName().equals(dormitoryName)).collect(Collectors.toList());
        session.close();
        sessionFactory.close();
        return washers;
    }

    @RequestMapping("/wl/{laundry}")
    public List<Washer> washersByLaundries(@PathVariable("laundry") int idLaundry) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Washer> washers = session.createQuery("from Washer").list();
        washers = washers.stream().filter(w -> w.getLaundry().getIdLaundry() == idLaundry).collect(Collectors.toList());
        session.close();
        sessionFactory.close();
        return washers;
    }

    @RequestMapping("/add/{noWash}/{idLaundry}")
    public boolean addWasher(@PathVariable("noWash") int noWash, @PathVariable("idLaundry") int idLaundry) {
        boolean ok = false;
        try {
            Washer washer = new Washer();
            washer.setNoWasher(noWash);
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Laundry laundry = session.get(Laundry.class, idLaundry);
            washer.setLaundry(laundry);
            session.save(washer);
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/mod/{id}/{noWasher}/{idLaundry}")
    public boolean modWasher(@PathVariable("id") int idWash, @PathVariable("idLaundry") int idLaundry, @PathVariable("noWasher") int noWasher) {
        boolean ok = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Washer washer = session.get(Washer.class, idWash);
            Laundry laundry = session.get(Laundry.class, idLaundry);
            washer.setLaundry(laundry);
            washer.setNoWasher(noWasher);
            Transaction transaction = session.beginTransaction();
            session.update(washer);
            transaction.commit();
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/add/l/{name}/{dormitory}")
    public boolean addLaundry(@PathVariable("name") String name, @PathVariable("dormitory") int idDormitory) {
        boolean ok = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Laundry laundry = new Laundry();
            laundry.setName(name);
            Dormitory dormitory = session.get(Dormitory.class, idDormitory);
            laundry.setDormitory(dormitory);
            session.save(laundry);
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/laundry/mod/{id}/{name}/{idDormitory}")
    public boolean modLaundry(@PathVariable("id") int idLaundry, @PathVariable("name") String name, @PathVariable("idDormitory") int idDormitory) {
        boolean ok = false;
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Laundry laundry = session.get(Laundry.class, idLaundry);
        laundry.setName(name);
        Dormitory dormitory = session.get(Dormitory.class, idDormitory);
        laundry.setDormitory(dormitory);
        Transaction transaction = session.beginTransaction();
        session.update(laundry);
        transaction.commit();
        ok = true;
        session.close();
        sessionFactory.close();
        return ok;
    }

    @RequestMapping("/laundry/del/{id}")
    public boolean delLaundry(@PathVariable("id") int idLaundry) {
        boolean ok = false;
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Laundry laundry = session.get(Laundry.class, idLaundry);
        try {
            Transaction transaction = session.beginTransaction();
            session.delete(laundry);
            transaction.commit();
            ok = true;
        } catch (Exception ex) {
            ok = false;
        }
        session.close();
        sessionFactory.close();
        return ok;
    }
}
