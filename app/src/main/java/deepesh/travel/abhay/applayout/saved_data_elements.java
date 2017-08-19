package deepesh.travel.abhay.applayout;

/**
 * Created by abhay on 07/04/17.
 */

public class saved_data_elements extends cus_msg_elements {
    private String dest;
    private String lattitude;
    private String longitude;

    public saved_data_elements(){}

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

    public saved_data_elements(String dest, String lattitude, String longitude)
    {
        this.dest=dest;
        this.lattitude=lattitude;
        this.longitude=longitude;

    }



    public String getDest() {

        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

}
