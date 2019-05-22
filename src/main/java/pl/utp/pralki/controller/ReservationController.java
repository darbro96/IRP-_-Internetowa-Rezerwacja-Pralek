/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.pralki.database.DatabaseConfigurator;
import pl.utp.pralki.entities.Reservation;
import pl.utp.pralki.entities.SpecialReservation;
import pl.utp.pralki.entities.Timetable;
import pl.utp.pralki.entities.User;
import pl.utp.pralki.entities.Washer;

/**
 *
 * @author Darek
 */
@RequestMapping("/res")
@RestController
public class ReservationController {

    @RequestMapping("/all")
    public List<Reservation> allReservations() {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Reservation> reservations = session.createQuery("from Reservation").list();
        session.close();
        sessionFactory.close();
        return reservations;
    }

    @RequestMapping("/user/{idUser}")
    public long reservationsByUser(@PathVariable("idUser") int idUser) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Reservation> reservations = session.createQuery("from Reservation").list();
        long countReservation = reservations.stream().filter(r -> r.getUser().getIdUser() == idUser).count();
        session.close();
        sessionFactory.close();
        return countReservation;
    }

    @RequestMapping("/byuser/{idUser}")
    public List<Reservation> reservationsByUserList(@PathVariable("idUser") int idUser) {
        List<Reservation> reservations = new ArrayList<>();
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        reservations = session.createQuery("from Reservation").list();
//        reservations = reservations.stream().filter(r -> r.getUser().getIdUser() == idUser).collect(Collectors.toList());
//        reservations.sort(Comparator.comparing(r -> r.getDate()));
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        reservations = reservations.stream().filter(r -> r.getUser().getIdUser() == idUser).filter(r -> r.getDate().getYear() >= ld.getYear() - 1900).filter(r -> r.getDate().getMonth() >= ld.getMonthValue() - 1).filter(r -> r.getDate().toLocalDate().getDayOfMonth() >= ld.getDayOfMonth()).collect(Collectors.toList());

        for (Reservation res : reservations) {
            if (res.getDate().toLocalDate().isEqual(ld)) {
                if (res.getTimeStart().toLocalTime().isAfter(lt)) {
                    reservations.remove(res);
                }
            }
        }
        reservations.sort(Comparator.comparing(r -> r.getDate(), Comparator.reverseOrder()));
        session.close();
        sessionFactory.close();
        return reservations;
    }

    @RequestMapping("/byuser-old/{idUser}")
    public List<Reservation> oldReservationsByUserList(@PathVariable("idUser") int idUser) {
        List<Reservation> reservations = new ArrayList<>();
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        reservations = session.createQuery("from Reservation").list();
//        reservations = reservations.stream().filter(r -> r.getUser().getIdUser() == idUser).collect(Collectors.toList());
//        reservations.sort(Comparator.comparing(r -> r.getDate()));
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        reservations = reservations.stream().filter(r -> r.getUser().getIdUser() == idUser).filter(r -> r.getDate().toLocalDate().isBefore(ld)).collect(Collectors.toList());

//        for (Reservation res : reservations) {
//            if (res.getDate().toLocalDate().equals(ld)) {
//                if (res.getTimeStart().toLocalTime().isBefore(lt)) {
//                    reservations.remove(res);
//                }
//            }
//        }
        reservations.sort(Comparator.comparing(r -> r.getDate(), Comparator.reverseOrder()));
        session.close();
        sessionFactory.close();
        return reservations;
    }

    @RequestMapping("add/{idUser}/{idWasher}/{date}/{timeStart}/{option}")
    public boolean addReservation(@PathVariable("idUser") int idUser, @PathVariable("idWasher") int idWasher, @PathVariable("date") String sDate, @PathVariable("timeStart") String sTimeStart, @PathVariable("option") double option) {
        boolean result = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            User user = session.get(User.class, idUser);
            Washer washer = session.get(Washer.class, idWasher);
            int day = Integer.parseInt(sDate.substring(8, 10));
            int year = Integer.valueOf(sDate.substring(11, 15));
            sDate = sDate.substring(4, 7);
            int month = 1;
            switch (sDate) {
                case "Jan":
                    month = 1;
                    break;
                case "Feb":
                    month = 2;
                    break;
                case "Mar":
                    month = 3;
                    break;
                case "Apr":
                    month = 4;
                    break;
                case "May":
                    month = 5;
                    break;
                case "Jun":
                    month = 6;
                    break;
                case "Jul":
                    month = 7;
                    break;
                case "Aug":
                    month = 8;
                    break;
                case "Sep":
                    month = 9;
                    break;
                case "Oct":
                    month = 10;
                    break;
                case "Nov":
                    month = 11;
                    break;
                case "Dec":
                    month = 12;
                    break;
                default:
                    month = 1;
            }
            Date date = new Date(year - 1900, month - 1, day);
            sTimeStart = sTimeStart.substring(16, 24);
            Time time = new Time(Integer.valueOf(sTimeStart.substring(0, 2)), Integer.valueOf(sTimeStart.substring(3, 5)), Integer.valueOf(sTimeStart.substring(6, 8)));
            if (time.getMinutes() != 0 && time.getMinutes() != 30) {
                return false;
            }
            int option2 = (int) option;
            double option3 = option - (double) option2;
            if (option3 > 0) {
                option3 = 30;
            }
            Time time2 = new Time(time.getHours() + option2, time.getMinutes() + (int) option3, time.getSeconds());
            if (time.getHours() < 6 || time.getHours() > 21 || (time2.getHours() >= 23 && time2.getMinutes() > 0)) {
                return false;
            }
            java.util.Date dateNow = new java.util.Date();

            if (date.getYear() == dateNow.getYear() && date.getMonth() == dateNow.getMonth() && date.getDay() == dateNow.getDay()) {
                if (date.getHours() < dateNow.getHours() && date.getMinutes() < dateNow.getMinutes()) {
                    return false;
                }
            }

            Reservation reservation = new Reservation();
            reservation.setDate(date);
            reservation.setTimeStart(time);
            reservation.setTimeStop(time2);
            reservation.setUser(user);
            reservation.setWasher(washer);
            List<Reservation> list = session.createQuery("from Reservation").list();
            list = list.stream().filter(r -> r.getDate().equals(reservation.getDate())).collect(Collectors.toList());
            int ile = (int) list.stream().filter(r -> r.getUser().getIdUser() == idUser).count();
            if (ile < 1) {
                //return false;

                list = list.stream().filter(r -> r.getWasher().getIdWasher() == reservation.getWasher().getIdWasher()).collect(Collectors.toList());
                boolean add = true;
                for (Reservation r : list) {
                    if (reservation.getTimeStart().getHours() > r.getTimeStart().getHours()) {
                        if (reservation.getTimeStart().getMinutes() >= r.getTimeStart().getMinutes()) {
                            if (reservation.getTimeStart().getHours() < r.getTimeStop().getHours()) {
                                if (reservation.getTimeStart().getMinutes() <= r.getTimeStop().getMinutes()) {
                                    add = false;
                                }
                            }
                        }
                    } else if (reservation.getTimeStop().getHours() <= r.getTimeStop().getHours() && reservation.getTimeStop().getHours() > r.getTimeStart().getHours()) {
                        add = false;
                    }
                }
                if (add) {
                    Transaction transaction = session.beginTransaction();
                    session.save(reservation);
                    transaction.commit();
                    result = true;
                }
            }
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    @RequestMapping("/policz/{data}/{idUser}")
    public int policz(@PathVariable("data") String date, @PathVariable("idUser") int idUser) {
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        Date data = new Date(2019 - 1900, 1, 10);
        List<Reservation> list = session.createQuery("from Reservation").list();
        list = list.stream().filter(r -> r.getDate().equals(data)).collect(Collectors.toList());
        int ile = (int) list.stream().filter(r -> r.getUser().getIdUser() == idUser).count();

        return ile;
    }

    @RequestMapping("/timetable/{washerId}/{actUser}")
    public List<Timetable> timetable(@PathVariable("washerId") int washerId, @PathVariable("actUser") int userId) {
        List<Timetable> timetables = new ArrayList<>();
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<Reservation> reservations = session.createQuery("from Reservation").list();
            reservations = reservations.stream().filter(r -> r.getWasher().getIdWasher() == washerId).collect(Collectors.toList());
            session.close();
            sessionFactory.close();
            Time time = new Time(6, 0, 0);
            Date date = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth());
            SpecialReservation reservation = null;
            SpecialReservation reservation1 = null;
            SpecialReservation reservation2 = null;
            SpecialReservation reservation3 = null;
            SpecialReservation reservation4 = null;
            SpecialReservation reservation5 = null;
            SpecialReservation reservation6 = null;
            SpecialReservation reservation7 = null;
            SpecialReservation reservation8 = null;
            List<Reservation> list = reservations.stream().filter(r -> r.getDate().equals(date)).collect(Collectors.toList());
            Date date1 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 1);
            List<Reservation> list1 = reservations.stream().filter(r -> r.getDate().equals(date1)).collect(Collectors.toList());
            Date date2 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 2);
            List<Reservation> list2 = reservations.stream().filter(r -> r.getDate().equals(date2)).collect(Collectors.toList());
            Date date3 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 3);
            List<Reservation> list3 = reservations.stream().filter(r -> r.getDate().equals(date3)).collect(Collectors.toList());
            Date date4 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 4);
            List<Reservation> list4 = reservations.stream().filter(r -> r.getDate().equals(date4)).collect(Collectors.toList());
            Date date5 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 5);
            List<Reservation> list5 = reservations.stream().filter(r -> r.getDate().equals(date5)).collect(Collectors.toList());
            Date date6 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 6);
            List<Reservation> list6 = reservations.stream().filter(r -> r.getDate().equals(date6)).collect(Collectors.toList());
            Date date7 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 7);
            List<Reservation> list7 = reservations.stream().filter(r -> r.getDate().equals(date7)).collect(Collectors.toList());
            Date date8 = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth() + 8);
            List<Reservation> list8 = reservations.stream().filter(r -> r.getDate().equals(date8)).collect(Collectors.toList());
            for (int i = 0; i < 36; i++) {
                reservation = null;
                reservation1 = null;
                reservation2 = null;
                reservation3 = null;
                reservation4 = null;
                reservation5 = null;
                reservation6 = null;
                reservation7 = null;
                reservation8 = null;
                for (Reservation r : list) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list1) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation1 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation1 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list2) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation2 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation2 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list3) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation3 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation3 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list4) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation4 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation4 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list5) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation5 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation5 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list6) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation6 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation6 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list7) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation7 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation7 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                for (Reservation r : list8) {
                    if (r.getTimeStart().equals(time)) {
                        if (r.getUser().getIdUser() == userId) {
                            reservation8 = new SpecialReservation("#FF6347", r);
                        } else {
                            reservation8 = new SpecialReservation("#C71585", r);
                        }
                        break;
                    }
                }
                timetables.add(new Timetable(new Time(time.getHours(), time.getMinutes(), time.getSeconds()), reservation, reservation1, reservation2, reservation3, reservation4, reservation5, reservation6, reservation7, reservation8));
                time.setMinutes(time.getMinutes() + 30);
            }
            for (int i = 0; i < timetables.size() - 1; i++) {
                if (timetables.get(i).getResToday() != null) {
                    if (timetables.get(i).getResToday().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday(timetables.get(i).getResToday());
                    }
                    if (timetables.get(i).getResToday().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday(timetables.get(i).getResToday());
                    }
                }

                if (timetables.get(i).getResToday1() != null) {
                    if (timetables.get(i).getResToday1().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday1(timetables.get(i).getResToday1());
                    }
                    if (timetables.get(i).getResToday1().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday1().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday1(timetables.get(i).getResToday1());
                    }
                }

                if (timetables.get(i).getResToday2() != null) {
                    if (timetables.get(i).getResToday2().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday2(timetables.get(i).getResToday2());
                    }
                    if (timetables.get(i).getResToday2().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday2().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday2(timetables.get(i).getResToday2());
                    }
                }

                if (timetables.get(i).getResToday3() != null) {
                    if (timetables.get(i).getResToday3().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday3(timetables.get(i).getResToday3());
                    }
                    if (timetables.get(i).getResToday3().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday3().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday3(timetables.get(i).getResToday3());
                    }
                }
                if (timetables.get(i).getResToday4() != null) {
                    if (timetables.get(i).getResToday4().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday4(timetables.get(i).getResToday4());
                    }
                    if (timetables.get(i).getResToday4().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday4().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday4(timetables.get(i).getResToday4());
                    }
                }
                if (timetables.get(i).getResToday5() != null) {
                    if (timetables.get(i).getResToday5().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday5(timetables.get(i).getResToday5());
                    }
                    if (timetables.get(i).getResToday5().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday5().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday4(timetables.get(i).getResToday5());
                    }
                }
                if (timetables.get(i).getResToday6() != null) {
                    if (timetables.get(i).getResToday6().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday6(timetables.get(i).getResToday6());
                    }
                    if (timetables.get(i).getResToday6().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday6().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday6(timetables.get(i).getResToday6());
                    }
                }
                if (timetables.get(i).getResToday7() != null) {
                    if (timetables.get(i).getResToday7().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday7(timetables.get(i).getResToday7());
                    }
                    if (timetables.get(i).getResToday7().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday7().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday7(timetables.get(i).getResToday7());
                    }
                }
                if (timetables.get(i).getResToday8() != null) {
                    if (timetables.get(i).getResToday8().getReservation().getTimeStop().getHours() > timetables.get(i + 1).getTime().getHours()) {
                        timetables.get(i + 1).setResToday8(timetables.get(i).getResToday8());
                    }
                    if (timetables.get(i).getResToday8().getReservation().getTimeStop().getHours() == timetables.get(i + 1).getTime().getHours() && timetables.get(i).getResToday8().getReservation().getTimeStop().getMinutes() > timetables.get(i + 1).getTime().getMinutes()) {
                        timetables.get(i + 1).setResToday8(timetables.get(i).getResToday8());
                    }
                }
            }

        } catch (Exception ex) {

        }

        return timetables;
    }

    @RequestMapping("/delete/{id}")
    public boolean deleteReservation(@PathVariable("id") int idReservation) {
        boolean ok = false;
        try {
            SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
            Session session = sessionFactory.openSession();
            Reservation reservation = session.get(Reservation.class, idReservation);
            Transaction transaction = session.beginTransaction();
            session.delete(reservation);
            transaction.commit();
            ok = true;
            session.close();
            sessionFactory.close();
        } catch (Exception ex) {
            ok = false;
        }
        return ok;
    }

    @RequestMapping("/today")
    public List<SpecialReservation> reservationsToday() {
        List<Reservation> reservations = new ArrayList<>();
        SessionFactory sessionFactory = DatabaseConfigurator.getSessionFactory();
        Session session = sessionFactory.openSession();
        reservations = session.createQuery("from Reservation").list();
        java.util.Date dateNow = new java.util.Date();
        Date date = new Date(now().getYear() - 1900, now().getMonthValue() - 1, now().getDayOfMonth());
        reservations = reservations.stream().filter(r -> r.getDate().equals(date)).collect(Collectors.toList());
        reservations = reservations.stream().filter(r -> r.getTimeStop().getHours() > dateNow.getHours()).collect(Collectors.toList());
        reservations.sort(Comparator.comparing(r -> r.getDate()));
        List<SpecialReservation> specialReservations = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getTimeStart().getHours() <= dateNow.getHours() && r.getTimeStop().getHours() > dateNow.getHours()) {
                if (r.getTimeStart().getMinutes() < dateNow.getMinutes()) {
                    specialReservations.add(new SpecialReservation("#F0E68C", r));
                } else {
                    specialReservations.add(new SpecialReservation("#98FB98", r));
                }
            } else {
                specialReservations.add(new SpecialReservation("#98FB98", r));
            }
        }
        session.close();
        sessionFactory.close();
        return specialReservations;
    }
}
