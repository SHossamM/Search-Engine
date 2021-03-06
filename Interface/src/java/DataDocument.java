

import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataDocument {

    private Integer id;
    private Set<String> titleKeywords;
    private Set<String> metaKeywords;
    String description;
    private String text;
    String title;

    public DataDocument(Integer id, String url, String html) {

        Document parsedHtml = Jsoup.parse(html, url);
        StringBuilder parsedText = new StringBuilder();


        // Extract title
       title = parsedHtml.title().toLowerCase().replaceAll("[^a-z0-9]", " ");

        // Extract meta keywords

        String metaKeywords = "";
        for(Element metaTag: parsedHtml.getElementsByTag("meta")) {
            if (metaTag.attr("name").equals("description")) {
                metaKeywords = metaTag.attr("content");
                description=metaKeywords;
                break;
            }
        }

        // Extract text
        parsedHtml.body().traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int i) {
                if (node instanceof TextNode && !((TextNode) node).isBlank()) {
                    parsedText.append(((TextNode) node).text().toLowerCase().replaceAll("[^a-z0-9]", " "))
                            .append(" ");
                }
            }

            @Override
            public void tail(Node node, int i) {

            }
        });

        WhitespaceTokenizer wsTokenizer = WhitespaceTokenizer.INSTANCE;

        this.id = id;
        this.titleKeywords = new HashSet<>(Arrays.asList(wsTokenizer.tokenize(title)));
        this.metaKeywords = new HashSet<>(Arrays.asList(wsTokenizer.tokenize(metaKeywords)));
        this.text = parsedText.toString();
    }

    public Integer getId() {
        return id;
    }

    public Set<String> getTitleKeywords() {
        return titleKeywords;
    }

    public Set<String> getMetaKeywords() {
        return metaKeywords;
    }
    public String getDescription(){
        return description;
    }

    public String getBody() {
        return text;
    }
    public String getTitle(){
        return title;
    }

}
