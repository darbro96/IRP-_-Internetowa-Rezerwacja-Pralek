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
import pl.utp.pralki.entities.Log;
import pl.utp.pralki.entities.Reservation;
import pl.utp.pralki.entities.User;

/**
 *
 * @author Darek
 */
@RequestMapping("/users")
@RestController
public class UserController {

    @RequestMapping("/all")
    public List<User> allUsers() {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<User> dorms = session.createQuery("from User").list();
        session.close();
        sessionFactory.close();
        return dorms;
    }

    @RequestMapping("/id/{id_user}")
    public User userById(@PathVariable("id_user") int idUser) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        //List<User> dorms = session.createQuery("from User").list();
        User user = session.get(User.class, idUser);
        //dorms=dorms.stream().filter(u->u.getIdUser()==idUser).collect(Collectors.toList());
        session.close();
        sessionFactory.close();
        return user;
    }

    @RequestMapping("/add/{userName}/{userLastName}/{dormitoryId}/{laundryId}")
    public User addUsers(@PathVariable("userName") String name, @PathVariable("userLastName") String lastName, @PathVariable("dormitoryId") int dormitoryId, @PathVariable("laundryId") int laundryId) {
        User user = new User();
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Dormitory dormitory = session.get(Dormitory.class, dormitoryId);
            Laundry laundry = session.get(Laundry.class, laundryId);

            user.setDormitory(dormitory);
            user.setLaundry(laundry);
            user.setLastName(lastName);
            user.setName(name);
            Transaction transaction = session.beginTransaction();
            session.save(user);
            //add=true;
            transaction.commit();
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            //add = false;
            return null;
        }
        return user;
    }

    @RequestMapping("/setPassword/{idUser}/{password}")
    public boolean setPasswrod(@PathVariable("idUser") int idUser, @PathVariable("password") String passsword) {
        boolean ok = true;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Log log = new Log(idUser, passsword);
            Transaction transaction = session.beginTransaction();
            session.save(log);
            transaction.commit();
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/mod/{idUser}/{name}/{lastName}/{idDormitory}/{idLaundry}/{password}")
    public boolean modifyUser(@PathVariable("idUser") int idUser, @PathVariable("name") String name, @PathVariable("lastName") String lastName, @PathVariable("idDormitory") int idDormitory, @PathVariable("idLaundry") int idLaundry, @PathVariable("password") String password) {
        boolean ok = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            User user = session.get(User.class, idUser);
            user.setName(name);
            user.setLastName(lastName);
            Dormitory dormitory = session.get(Dormitory.class, idDormitory);
            user.setDormitory(dormitory);
            Laundry laundry = session.get(Laundry.class, idLaundry);
            user.setLaundry(laundry);
            Log log = session.get(Log.class, user.getIdUser());
            log.setPassword(password);
            Transaction transaction = session.beginTransaction();
            session.update(user);
            session.update(log);
            transaction.commit();
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/del/{idUser}")
    public boolean delUser(@PathVariable("idUser") int idUser) {
        boolean ok = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            User user = session.get(User.class, idUser);
            Log log = session.get(Log.class, user.getIdUser());
            List<Reservation> reservations = session.createQuery("from Reservation").list();
            reservations = reservations.stream().filter(r -> r.getUser().getIdUser() == user.getIdUser()).collect(Collectors.toList());
            if (reservations.size() > 0) {
                for (Reservation r : reservations) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(r);
                    transaction.commit();
                }
            }
            Transaction transaction = session.beginTransaction();
            session.delete(log);
            session.delete(user);
            transaction.commit();
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }
}
