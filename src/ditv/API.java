package ditv;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class API {

public static void main(String[] args){
    org.w3c.dom.Document doc = getXml("79755", "episodes");
                NodeList nodeList = (NodeList)doc.getElementsByTagName("Episode");
                System.out.println(nodeList.getLength());
                //nodeList = nodeList.item(0).getChildNodes();
                for (int c = 0; c < nodeList.getLength();c++) {
                    NodeList nl = nodeList.item(c).getChildNodes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node node = nl.item(i);
                        switch (node.getNodeName()) {
                            case "id": System.out.println(node.getTextContent());
                                break;
                            case "EpisodeName": System.out.println(node.getTextContent());
                                break;
                            case "EpisodeNumber": System.out.println(node.getTextContent());
                                break;
                            case "FirstAired": System.out.println(node.getTextContent());
                                break;
                            case "SeasonNumber": System.out.println(node.getTextContent());
                                break;
                            case "absolute_number": System.out.println(node.getTextContent());
                        }
                    }
                    }
                    
}
public static Document getXml(String seriesID, String retrieve){
    org.w3c.dom.Document doc = null;
    try {
        String uri = "";
        ArrayList<String> toRemove = new ArrayList<String>();
        if (retrieve == "series"){
            uri = "http://thetvdb.com/api/FF797193CA702063/series/" + seriesID;
            toRemove.add("Actors");
            toRemove.add("ContentRating");
            toRemove.add("FirstAired");
            toRemove.add("Genre");
            toRemove.add("IMDB_ID");
            toRemove.add("Language");
            toRemove.add("NetworkID");
            toRemove.add("Overview");
            toRemove.add("RatingCount");
            toRemove.add("Runtime");
            toRemove.add("SeriesID");
            toRemove.add("Status");
            toRemove.add("added");
            toRemove.add("addedBy");
            toRemove.add("banner");
            toRemove.add("fanart");
            toRemove.add("lastupdated");
            toRemove.add("poster");
            toRemove.add("tms_wanted_old");
            toRemove.add("zap2it_id");
        } else if (retrieve == "episodes"){
            uri = "http://thetvdb.com/api/FF797193CA702063/series/" + seriesID + "/all";
            //toRemove.add("Series");
            toRemove.add("Combined_episodenumber");
            toRemove.add("Combined_season");
            toRemove.add("DVD_chapter");
            toRemove.add("DVD_discid");
            toRemove.add("DVD_episodenumber");
            toRemove.add("DVD_season");
            toRemove.add("Director");
            toRemove.add("EpImgFlag");
            toRemove.add("GuestStars");
            toRemove.add("IMDB_ID");
            toRemove.add("Language");
            toRemove.add("Overview");
            toRemove.add("ProductionCode");
            toRemove.add("Rating");
            toRemove.add("RatingCount");
            toRemove.add("Writer");
            toRemove.add("airsafter_season");
            toRemove.add("airsbefore_episode");
            toRemove.add("airsbefore_season");
            toRemove.add("filename");
            toRemove.add("lastupdated");
            toRemove.add("seasonid");
            toRemove.add("seriesid");
            toRemove.add("thumb_added");
            toRemove.add("thumb_height");
            toRemove.add("thumb_width");
        }
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        InputStream xml = connection.getInputStream();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(xml);
        if (retrieve == "series"){
            doc = removeNodeSeries(doc, toRemove);
        } else if (retrieve == "episodes"){
            doc = removeNodeEpisodes(doc, toRemove);
        }

        printDocument(doc, System.out);
        } catch (Exception e) {
    }
    return doc;
}

//remove nodes from xml
public static Document removeNodeSeries(Document doc, ArrayList<String> toRemove){
    try{
        for(int i = 0; i < toRemove.size(); i++){
            Element element = (Element) doc.getElementsByTagName(toRemove.get(i)).item(0);
            element.getParentNode().removeChild(element);
            doc.normalize();
        }
        return removeEmptyNodes(doc);
    } catch (Exception e) {
        System.out.println("FAILED");
        return doc;
    }
}

public static Document removeNodeEpisodes(Document doc, ArrayList<String> toRemove){
    try{
        Element seriesInfo = (Element) doc.getElementsByTagName("Series").item(0);
        seriesInfo.getParentNode().removeChild(seriesInfo);
        doc.normalize();

        for(int i = 0; i < toRemove.size(); i++){
            NodeList nodeList = (NodeList) doc.getElementsByTagName(toRemove.get(i));
            int nl = nodeList.getLength();
            for (int y = 0; y < nl; y++){
                nodeList.item(0).getParentNode().removeChild(nodeList.item(0));
            }
        }
        doc.normalize();
        return removeEmptyNodes(doc);
    } catch (Exception e) {
        System.out.println("FAILED");
        return doc;
    }
}

public static Document removeEmptyNodes(Document doc){
        try{
            XPath xp = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc, XPathConstants.NODESET);

            for (int i=0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                node.getParentNode().removeChild(node);
            }
            return doc;
        } catch (Exception e){
            System.out.println("Failed to remove empty nodes.");
            return doc;
        }
}

// convert InputStream to String
public static String getStringFromInputStream(InputStream is) {
    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();
    String line;
    try {
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
        sb.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    return sb.toString();
}

public static void printDocument(org.w3c.dom.Document doc, OutputStream out) throws IOException, TransformerException {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
}
}
