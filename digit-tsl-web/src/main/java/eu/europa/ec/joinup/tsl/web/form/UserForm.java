package eu.europa.ec.joinup.tsl.web.form;

public class UserForm {

    private String ecasId;
    private String country;
    private int userId;

    public String getEcasId() {
        return ecasId;
    }

    public void setEcasId(String ecasName) {
        this.ecasId = ecasName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
