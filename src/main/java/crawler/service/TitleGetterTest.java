package crawler.service;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static crawler.service.DocumentGetter.getDocument;
import static org.junit.Assert.assertEquals;

public class TitleGetterTest {

    private String facebookTitle = "Facebook";
    private TitleGetter titleGetter;
    private String wikiFacebookURL = "https://en.wikipedia.org/wiki/Facebook";

    @Before
    public void setUp() throws Exception {
        Document document = getDocument(wikiFacebookURL);
        titleGetter = new TitleGetter(document);
    }

    @Test
    public void facebookTitleIsFacebook(){
        assertEquals(facebookTitle, titleGetter.getTitle());
    }
}