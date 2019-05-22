/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.database;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import pl.utp.pralki.entities.Admin;
import pl.utp.pralki.entities.Dormitory;
import pl.utp.pralki.entities.Laundry;
import pl.utp.pralki.entities.Log;
import pl.utp.pralki.entities.Reservation;
import pl.utp.pralki.entities.User;
import pl.utp.pralki.entities.Washer;

/**
 *
 * @author Darek
 */
public class DatabaseConfigurator {
    public static SessionFactory getSessionFactory() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/testowa2");
		properties.setProperty("hibernate.connection.username", "root");
		properties.setProperty("hibernate.connection.password", "");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "validate");

		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.use_sql_comments", "true");
                properties.setProperty("hibernate.id.new_generator_mappings", "false");
                //properties.setProperty("show_sql", "true");

		org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().addProperties(properties);
		configuration.addAnnotatedClass(Dormitory.class);
                configuration.addAnnotatedClass(Washer.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Laundry.class);
                configuration.addAnnotatedClass(Log.class);
                configuration.addAnnotatedClass(Admin.class);
                configuration.addAnnotatedClass(Reservation.class);
		org.hibernate.service.ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}
}
