package redarrow.dotapk.jit.redarrow;

import java.io.Serializable;

/**
 * Created by jit on 31-01-2017.
 */
public class Notific implements Serializable{

    private String id;
    private String hospital_id;
    private String created_at;
    private String hospital_name;
    private String lat;
    private String lng;
    private String hospital_address;
    private String review;
    private String donorname;
    private String donor_map_lat;
    private String donor_map_lng;
    private String donor_address;

    Notific(){}

    Notific(String id,String hospital_id,String created_at,String hospital_name,String lat,String lng,String hospital_address)
    {
        this.id=id;
        this.hospital_id=hospital_id;
        this.created_at=created_at;
        this.hospital_name=hospital_name;
        this.lat=lat;
        this.lng=lng;
        this.setHospital_address(hospital_address);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getHospital_id(){
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
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

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDonorname() {
        return donorname;
    }

    public void setDonorname(String donorname) {
        this.donorname = donorname;
    }

    public String getDonor_map_lat() {
        return donor_map_lat;
    }

    public void setDonor_map_lat(String donor_map_lat) {
        this.donor_map_lat = donor_map_lat;
    }

    public String getDonor_map_lng() {
        return donor_map_lng;
    }

    public void setDonor_map_lng(String donor_map_lng) {
        this.donor_map_lng = donor_map_lng;
    }

    public String getDonor_address() {
        return donor_address;
    }

    public void setDonor_address(String donor_address) {
        this.donor_address = donor_address;
    }
}
