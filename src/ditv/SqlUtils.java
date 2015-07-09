package ditv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SqlUtils {
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
        
        try {
            con = getConnection();
            ps = con.prepareStatement("select * from series where series_id = ?");
            ps.setInt(1,id);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                response.add(rs.getString("series_id"));
                response.add(rs.getString("series_name"));
                response.add(rs.getString("series_airs_dayofweek"));
                response.add(rs.getString("series_airs_time"));
                response.add(rs.getString("series_network"));
                response.add(rs.getString("series_rating"));
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
            while(rs.next()) {
                response.add(rs.getString("episode_id"));
                response.add(rs.getString("episode_name"));
                response.add(rs.getInt("episode_number"));
                response.add(rs.getString("episode_firstAired"));
                response.add(rs.getInt("episode_seasonnumber"));
                response.add(rs.getInt("episode_absolute_number"));
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
