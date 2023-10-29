package com.example.hacknc.model;

public class MPothole {
    private String Description;
    private String ImageUrl;
    private String subLocality;
    private double lattitude;
    private double longitude;
    private String addressLine1;
    private String date;

    public MPothole(String description, String imageUrl, String subLocality, double lattitude, double longitude, String addressLine1, String date) {
        Description = description;
        ImageUrl = imageUrl;
        this.subLocality = subLocality;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.addressLine1 = addressLine1;
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MPothole() {
    }


}
