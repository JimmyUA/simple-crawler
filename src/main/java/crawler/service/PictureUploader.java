package crawler.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class PictureUploader {
    private static final String IMAGE_TAG = "img";
    private static final String IMAGE_SOURCE_ATTRIBUTE_NAME = "src";
    private static final String DIRECTORY_PATH = "./images/";
    private static final String TITLE_ID = "firstHeading";

    public static void uploadTitlePicture(Document document, String name){
        Element goalPicture = getTitlePictureElement(document);
        loadPicture(goalPicture, name);
    }

    private static Element getTitlePictureElement(Document document) {
        Elements allElements = document.getAllElements();
        int titleIndex = getTitleIndex(document, allElements);
        Elements pictures = document.getElementsByTag(IMAGE_TAG);

        return pictures.stream().
                filter(e -> allElements.indexOf(e) > titleIndex)
                .findFirst().get();
    }

    private static int getTitleIndex(Document document, Elements allElements) {
        Element titleElement = document.getElementById(TITLE_ID);
        return allElements.indexOf(titleElement);
    }

    private static void loadPicture(Element picture, String name){
        String source = picture.absUrl(IMAGE_SOURCE_ATTRIBUTE_NAME);
        String extension = source.substring(source.lastIndexOf('.'));

        createDirectory();

        try {
            URL url = new URL(source);
            InputStream in = url.openStream();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(DIRECTORY_PATH + name + extension));
            for (int b; (b = in.read()) != -1; ) {
                out.write(b);

            }

            out.close();

            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createDirectory() {
        File imagesDirectory = new File(DIRECTORY_PATH);
        if (!imagesDirectory.exists()){
            imagesDirectory.mkdir();
        }
    }
}
