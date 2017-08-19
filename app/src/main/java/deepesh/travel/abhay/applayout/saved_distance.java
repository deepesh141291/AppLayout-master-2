package deepesh.travel.abhay.applayout;

/**
 * Created by deepesh on 22/04/17.
 */

public class saved_distance {
    private String dist;
    private String lattitude;
    private String longitude;

    public saved_distance(){}

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public saved_distance(String dist, String lattitude, String longitude)
    {
        this.dist=dist;
        this.lattitude=lattitude;
        this.longitude=longitude;

    }



    public String getDist() {

        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

}
