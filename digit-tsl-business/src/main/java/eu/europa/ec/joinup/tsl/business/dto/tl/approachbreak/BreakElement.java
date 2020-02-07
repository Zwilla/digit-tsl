package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.util.DateUtils;

/**
 * Break Element with expiration in days (negative if expired), alert and breakDay flag
 */
public class BreakElement {

    private boolean alert;

    private boolean breakDay;

    private int expireIn;

    public BreakElement() {
        super();
        alert = false;
        breakDay = false;
        expireIn = 0;
    }

    /**
     * Loop through @limitList and detect if @elementDate is expired or require alert;
     *
     * @param elementDate
     * @param checkDate
     * @param limitList
     */
    public void verify(Date elementDate, Date checkDate, List<Integer> limitList) {
        expireIn = DateUtils.getDifferenceBetweenDatesInDays(elementDate, checkDate);
        if (expireIn < 0) {
            alert = true;
        } else if (limitList.get(0) > expireIn) {
            alert = true;
            if (limitList.contains(expireIn)) {
                breakDay = true;
            }
        }
    }

    public boolean isExpired() {
        return expireIn < 0;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isBreakDay() {
        return breakDay;
    }

    public void setBreakDay(boolean breakDay) {
        this.breakDay = breakDay;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

}
