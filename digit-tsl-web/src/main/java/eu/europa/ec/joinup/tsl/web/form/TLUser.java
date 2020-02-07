package eu.europa.ec.joinup.tsl.web.form;

public class TLUser {

    private int userId;
    private int roleId;

    public TLUser() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
