package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

public class TLAvailability {

    private int id;
    private Date dMin;
    private Date dMax;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getdMin() {
        return dMin;
    }

    public void setdMin(Date dMin) {
        this.dMin = dMin;
    }

    public Date getdMax() {
        return dMax;
    }

    public void setdMax(Date dMax) {
        this.dMax = dMax;
    }

}
