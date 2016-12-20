package redarrow.dotapk.jit.redarrow;

import java.io.Serializable;

/**
 * Created by jit on 13-12-2016.
 */
public class Hospital implements Serializable{


    private String name;
    private String reg_no;
    private String address;
    private String contact_no;
    private String lat;
    private String lng;
    Hospital(String name,String reg_no,String address,String contact_no,String lat,String lng)
    {

        this.setName(name);
        this.setReg_no(reg_no);
        this.setAddress(address);
        this.setContact_no(contact_no);
        this.setLat(lat);
        this.setLng(lng);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
