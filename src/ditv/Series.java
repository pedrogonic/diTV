package ditv;

public class Series {
    private int id;
    private String name;
    private String airsDayOfWeek;
    private String airsTime;
    private String network;
    private float rating;
    
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

    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    public void setAirsDayOfWeek(String airsDayOfWeek) {
        this.airsDayOfWeek = airsDayOfWeek;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
