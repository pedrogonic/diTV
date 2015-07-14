package ditv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DiTVWs {
    public ArrayList search(String search) {
        ArrayList response = new ArrayList();
        
        ArrayList episode; //DAO!!!
        
        java.sql.Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = getConnection();
            ps = con.prepareStatement("select * from series where lower(series_name) like lower('%"+search+"%')");

            rs = ps.executeQuery();
            while(rs.next()) {
                episode = new ArrayList();
                episode.add(rs.getInt("series_id"));
                episode.add(rs.getString("series_name"));
                response.add(episode.get(0));
                response.add(episode.get(1));
            }
        } catch(Exception e){
            response.add(e.getMessage());
            return response;
        }
        finally {
            if (con != null) {
                try{
                    con.close();
                }catch(SQLException e){}
            }
            if (ps != null) {
                try{
                    ps.close();
                }catch(SQLException e){}
            }
            if (rs != null) {
                try{
                    rs.close();
                }catch(SQLException e){}
            }
        }
        return response;
    }
    
    public ArrayList getSeries(int id) {
        ArrayList response = new ArrayList();
        java.sql.Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        API api = new API();
        
        try {
            con = getConnection();
            ps = con.prepareStatement("select * from series where series_id = ?");
            ps.setInt(1,id);
            
            rs = ps.executeQuery();
            int size = 0;
            while(rs.next()) {
                response.add(rs.getString("series_id"));
                response.add(rs.getString("series_name"));
                response.add(rs.getString("series_airs_dayofweek"));
                response.add(rs.getString("series_airs_time"));
                response.add(rs.getString("series_network"));
                response.add(rs.getString("series_rating"));
                size++;
            }
            if (size == 0) {
                org.w3c.dom.Document doc = API.getXml(Integer.toString(id), "series");
                Series series = new Series();
                NodeList nodeList = (NodeList)doc.getElementsByTagName("Series");
                nodeList = nodeList.item(0).getChildNodes();
    
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    switch (node.getNodeName()) {
                        case "id": series.setId(Integer.parseInt(node.getTextContent()));
                            break;
                        case "Airs_DayOfWeek": series.setAirsDayOfWeek(node.getTextContent());
                            break;
                        case "Airs_Time": series.setAirsTime(node.getTextContent());
                            break;
                        case "Network": series.setNetwork(node.getTextContent());
                            break;
                        case "Rating": series.setRating(Float.parseFloat(node.getTextContent()));
                            break;
                        case "SeriesName": series.setName(node.getTextContent());
                            break;
                    }
                    
                }
                SqlUtils db = new SqlUtils();
                db.addSeries(series);
                response.add(series.getId());
                response.add(series.getName());
                response.add(series.getAirsDayOfWeek());
                response.add(series.getAirsTime());
                response.add(series.getNetwork());
                response.add(Float.toString(series.getRating()));
            }
        }
        catch(Exception e) {
            response.add(e.getMessage());
            return response;
        }
        finally {
            if (con != null) {
                try{
                    con.close();
                }catch(SQLException e){}
            }
            if (ps != null) {
                try{
                    ps.close();
                }catch(SQLException e){}
            }
            if (rs != null) {
                try{
                    rs.close();
                }catch(SQLException e){}
            }
        }
        
        return response;
    }
    
    public ArrayList getEpisodes(int seriesId) {
        ArrayList response = new ArrayList();
        java.sql.Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = getConnection();
            ps = con.prepareStatement("select * from episode where episode_series_id=?");
            ps.setInt(1, seriesId);

            rs = ps.executeQuery();
            int size = 0;
            while(rs.next()) {
                response.add(rs.getString("episode_id"));
                response.add(rs.getString("episode_name"));
                response.add(rs.getInt("episode_number"));
                response.add(rs.getString("episode_firstAired"));
                response.add(rs.getInt("episode_seasonnumber"));
                response.add(rs.getInt("episode_absolute_number"));
                size++;
            }
            if (size == 0) {
                org.w3c.dom.Document doc = API.getXml(Integer.toString(seriesId), "episodes");
                NodeList nodeList = (NodeList)doc.getElementsByTagName("Episode");
                SqlUtils db = new SqlUtils();
                for (int c = 0; c < nodeList.getLength();c++) {
                    NodeList nl = nodeList.item(c).getChildNodes();
                    Episode episode = new Episode();
                    episode.setSeries_id(seriesId);
                    String val;
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node node = nl.item(i);
                        switch (node.getNodeName()) {
                            case "id": 
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "0";}
                                episode.setId(Integer.parseInt(val));
                                break;
                            case "EpisodeName": 
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "empty";}
                                episode.setName(val);
                                break;
                            case "EpisodeNumber": 
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "0";}
                                episode.setNumber(Integer.parseInt(val));
                                break;
                            case "FirstAired":
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "empty";}
                                episode.setFirstAired(val);
                                break;
                            case "SeasonNumber": 
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "0";}
                                episode.setSeason(Integer.parseInt(val));
                                break;
                            case "absolute_number": 
                                val = node.getTextContent();
                                if (val == null || val == "") { val = "0";}
                                episode.setAbsoluteNumber(Integer.parseInt(val));
                                break;
                        }
                    }
                    db.addEpisode(episode);
                    response.add(Integer.toString(episode.getId()));
                    response.add(episode.getName());
                    response.add(Integer.toString(episode.getNumber()));
                    response.add(episode.getFirstAired());
                    response.add(Integer.toString(episode.getSeason()));
                    response.add(Integer.toString(episode.getAbsoluteNumber()));
                }
            }
        }
        catch(Exception e){
            response.add(e.getMessage());
            return response;
        }
        finally {
            if (con != null) {
                try{
                    con.close();
                }catch(SQLException e){}
            }
            if (ps != null) {
                try{
                    ps.close();
                }catch(SQLException e){}
            }
            if (rs != null) {
                try{
                    rs.close();
                }catch(SQLException e){}
            }
        }
        return response;
    }
    
    private java.sql.Connection getConnection() throws Exception{
        java.sql.Connection con;
        try {
            InitialContext cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres/diTV" );
            con = ds.getConnection();
        }
        catch (Exception e) {
                throw e;
        }
        return con;
    }
}
