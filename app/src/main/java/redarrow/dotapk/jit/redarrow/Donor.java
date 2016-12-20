package redarrow.dotapk.jit.redarrow;

import java.io.Serializable;

/**
 * Created by jit on 13-12-2016.
 */
public class Donor implements Serializable {
    private String id;
    private String name;
    private String dob;
    private String address;
    private String contact_no;
    private String blood_type;
    private String health_issues;
    private String lat;
    private String lon;
    Donor(String id,String name,String dob,String address,String contact_no,String blood_type,String health_issues,String lat,String lon)
    {
        this.setId(id);
        this.setName(name);
        this.setDob(dob);
        this.setAddress(address);
        this.setContact_no(contact_no);
        this.setBlood_type(blood_type);
        this.setHealth_issues(health_issues);
        this.setLat(lat);
        this.setLon(lon);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getHealth_issues() {
        return health_issues;
    }

    public void setHealth_issues(String health_issues) {
        this.health_issues = health_issues;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
