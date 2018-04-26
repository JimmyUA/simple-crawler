package crawler;

import crawler.entity.Title;
import crawler.service.PictureUploader;
import crawler.service.TitleGetter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

import static crawler.service.DocumentGetter.getDocument;

public class Application {

    private static final String RESULT_MESSAGE = "\n\n\n\nHere we go! Now in DataBase we have :\n%s\n\n\n\n" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
            "image you can find in the directory \"/images\" which is located\nin the same directory " +
            "you have just executed this wonderful application from\n\n\n\n";


    private static SessionFactory sessionFactory;
    private static Session session;


    public static void main(String[] args) {
        checkArgs(args);

        Document doc = getDocument(args[0]);
        String titleText = new TitleGetter(doc).getTitle();
        PictureUploader.uploadTitlePicture(doc, titleText);


        storeTitle(titleText);
        showDBContent();
        session.close();
        sessionFactory.close();

    }

    private static void checkArgs(String[] args) {
        if(args.length == 0){
            System.out.println("No URL specified!");

            System.exit(0);
        }
    }

    private static void showDBContent() {
        session.beginTransaction();
        Query query = session.createQuery("from Title ");
        Object content = query.list().stream()
                .collect(
                        Collectors.toMap(Title::getId, Title::getTitle));

        System.out.printf(RESULT_MESSAGE, content);
        session.getTransaction().commit();
    }

    private static void storeTitle(String titleText) {
        session = sessionFactory().openSession();
        session.beginTransaction();
        Title title = getTitle(titleText);
        session.persist(title);
        session.getTransaction().commit();
    }

    private static Title getTitle(String titleText) {
        Title title = new Title();
        title.setTitle(titleText);
        return title;
    }



    private static SessionFactory sessionFactory(){
        sessionFactory = new Configuration().configure().buildSessionFactory();
        return sessionFactory;
    }
}
