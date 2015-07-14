package ditv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SqlUtils {
    public void addSeries(Series series) {
        java.sql.Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = getConnection();
            String query = "insert into series (series_id,series_name,series_airs_dayofweek,series_airs_time,series_network,series_rating) "+
                    "values (?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, series.getId());
            ps.setString(2, series.getName());
            ps.setString(3, series.getAirsDayOfWeek());
            ps.setString(4, series.getAirsTime());
            ps.setString(5, series.getNetwork());
            ps.setFloat(6, series.getRating());
            ps.executeUpdate();
        }
         catch(Exception e) {    }
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
        }
    }
    
    public void addEpisode(Episode episode) {
        java.sql.Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = getConnection();
            String query = "insert into episode (episode_id,episode_name,episode_number,episode_firstaired,episode_seasonnumber,episode_absolute_number,episode_series_id) "+
                    "values (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, episode.getId());
            ps.setString(2, episode.getName());
            ps.setInt(3, episode.getNumber());
            ps.setString(4, episode.getFirstAired());
            ps.setInt(5, episode.getSeason());
            ps.setFloat(6, episode.getAbsoluteNumber());
            ps.setInt(7, episode.getSeries_id());
            ps.executeUpdate();
        }
         catch(Exception e) {    }
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
        }
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
