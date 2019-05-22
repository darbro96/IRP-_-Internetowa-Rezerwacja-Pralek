/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.pralki.database.DatabaseConfigurator;
import pl.utp.pralki.entities.Admin;
import pl.utp.pralki.entities.Log;

/**
 *
 * @author Darek
 */
@RequestMapping("/log")
@RestController
public class LogController {

    @RequestMapping("/{id_user}/{password}")
    public boolean login(@PathVariable("id_user") int idUser, @PathVariable("password") String password) {
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Log log=session.get(Log.class, idUser);
            session.close();
            sessionFactory.close();
            if(log.getPassword().equals(password))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
    
    @RequestMapping("/admin/{id}/{password}")
    public boolean loginAdmin(@PathVariable("id") int idUser, @PathVariable("password") String password)
    {
        try{
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Admin admin=session.get(Admin.class,idUser);
            session.close();
            sessionFactory.close();
            if(admin.getPassword().equals(password))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception ex)
        {
            return false;
        }
    }
}
