package crawler;

import crawler.entity.Title;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

public class Application {

    private static final String DIRECTORY_PATH = "./images/";
    private static final String TITLE_ID = "firstHeading";
    private static final String IMAGE_TAG = "img";
    private static final String IMAGE_SOURCE_ATTRIBUTE_NAME = "src";
    private static final String RESULT_MESSAGE = "\n\n\n\nHere we go! Now in DataBase we have :\n%s\n\n\n\n" +
            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
            "image you can find in the directory \"/images\" which is located\nin the same directory " +
            "you have just executed this wonderful application from\n\n\n\n";


    private static SessionFactory sessionFactory;
    private static Session session;


    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("No URL specified!");

            System.exit(0);
        }

        Document doc = null;
        String URL = args[0];
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements allElements = doc.getAllElements();

        Element titleElement = doc.getElementById(TITLE_ID);

        int titleIndex = allElements.indexOf(titleElement);

        Elements pictures = doc.getElementsByTag(IMAGE_TAG);

        Element goalPicture = pictures.stream().filter(e -> allElements.indexOf(e) > titleIndex).findFirst().get();

        createDirectory();
        String titleText = titleElement.ownText();
        loadPicture(goalPicture, titleText);

        storeTitle(titleText);
        showDBContent();
        session.close();
        sessionFactory.close();

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

    private static void createDirectory() {
        File imagesDirectory = new File(DIRECTORY_PATH);
        if (!imagesDirectory.exists()){
            imagesDirectory.mkdir();
        }
    }

    private static void loadPicture(Element picture, String name){
        String source = picture.absUrl(IMAGE_SOURCE_ATTRIBUTE_NAME);
        String extention = source.substring(source.lastIndexOf('.'));

        try {
            URL url = new URL(source);
            InputStream in = url.openStream();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(DIRECTORY_PATH + name + extention));
            for (int b; (b = in.read()) != -1; ) {
                out.write(b);

            }

            out.close();

            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static SessionFactory sessionFactory(){
        sessionFactory = new Configuration().configure().buildSessionFactory();
        return sessionFactory;
    }
}
