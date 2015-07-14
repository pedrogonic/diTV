package ditv;

public class Episode {
    private int id;
    private String name;
    private int number;
    private String firstAired;
    private int season;
    private int absoluteNumber;
    private int series_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getAbsoluteNumber() {
        return absoluteNumber;
    }

    public void setAbsoluteNumber(int absoluteNumber) {
        this.absoluteNumber = absoluteNumber;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }
    
    
}
