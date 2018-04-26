package crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TitleGetter {
    private static final String TITLE_ID = "firstHeading";
    private Document document;

    public TitleGetter(Document document) {
        this.document = document;
    }

    public String getTitle() {
        Elements allElements = document.getAllElements();
        Element titleElement = document.getElementById(TITLE_ID);

        return titleElement.ownText();
    }

}
