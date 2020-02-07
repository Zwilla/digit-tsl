package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.util.List;
import java.util.Set;

import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.model.DBUser;

public class UserChangeDTO {

    private String territory;
    private List<DBUser> dbUsers;
    private List<User> users;
    private Set<String> userRemoved;
    private Set<String> userAdded;

    public UserChangeDTO(String territory, List<DBUser> dbUsers, List<User> users, Set<String> userRemoved, Set<String> userAdded) {
        super();
        this.territory = territory;
        this.dbUsers = dbUsers;
        this.users = users;
        this.userRemoved = userRemoved;
        this.userAdded = userAdded;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public List<DBUser> getDbUsers() {
        return dbUsers;
    }

    public void setDbUsers(List<DBUser> dbUsers) {
        this.dbUsers = dbUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<String> getUserRemoved() {
        return userRemoved;
    }

    public void setUserRemoved(Set<String> userRemoved) {
        this.userRemoved = userRemoved;
    }

    public Set<String> getUserAdded() {
        return userAdded;
    }

    public void setUserAdded(Set<String> userAdded) {
        this.userAdded = userAdded;
    }

}
