package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) {
//        if(args.length == 0){
//            System.out.println("No URL specified!");
//            System.exit(0);
//        }

        Document doc = null;
        String URL = "https://en.wikipedia.org/wiki/Facebook";
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements allElements = doc.getAllElements();

        Element title = doc.getElementById("firstHeading");

        int titleIndex = allElements.indexOf(title);

        Elements pictures = doc.getElementsByTag("img");

        Element goalPicture = pictures.stream().filter(e -> allElements.indexOf(e) > titleIndex).findFirst().get();


    }

    private void loadPicture(Element picture, String name){

    }
}
