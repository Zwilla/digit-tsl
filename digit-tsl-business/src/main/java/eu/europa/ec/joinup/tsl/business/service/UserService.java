package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.notification.UserChangeDTO;
import eu.europa.ec.joinup.tsl.business.repository.RoleRepository;
import eu.europa.ec.joinup.tsl.business.repository.UserRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBRole;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * User management service
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final String superAdmin = "SUP";
    private static final String admin = "MAN";
    private static final String lotlSealer = "SIG";
    private static final String unsignedNotifier = "NOT";
    private static final String lotlManager = "LMN";
    private static final String authenticated = "ATH";

    private static final List<String> superAdminRoles = Collections.singletonList(superAdmin);
    private static final List<String> managementRoles = Arrays.asList(superAdmin, admin);
    private static final List<String> lotlSealerRoles = Arrays.asList(superAdmin, lotlSealer);
    private static final List<String> unsignedNotifierRoles = Arrays.asList(superAdmin, unsignedNotifier);
    private static final List<String> lotlManagerRoles = Arrays.asList(superAdmin, lotlManager);
    private static final List<String> authenticatedRoles = Arrays.asList(superAdmin, admin, lotlSealer, unsignedNotifier, lotlManager, authenticated);

    private static final String addRole = "addRole";
    private static final String removeRole = "removeRole";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditService auditService;

    /*----- RIGHTS -----*/

    public boolean isManagement(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, managementRoles) != null;
    }

    public boolean isLotlManager(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, lotlManagerRoles) != null;
    }

    public boolean isUnsignedNotifier(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, unsignedNotifierRoles) != null;
    }

    public boolean isSuperAdmin(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, superAdminRoles) != null;
    }

    public boolean islotlSealer(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, lotlSealerRoles) != null;
    }

    public boolean isAuthenticated(String ecasId) {
        return userRepository.findByEcasIdAndRoleCodeIn(ecasId, authenticatedRoles) != null;
    }

    /*----- MANAGEMENT -----*/

    /**
     * Get user order by country code and user name
     */
    public List<User> getUsersOrderByCountryAndName() {
        List<User> list = new ArrayList<>();
        List<DBUser> usrList = userRepository.findAllByOrderByTerritoryCodeTerritoryAscNameAsc();
        usrList.sort((u1, u2) -> {
            int sComp = u1.getTerritory().getCodeTerritory().compareTo(u2.getTerritory().getCodeTerritory());
            if (sComp != 0) {
                return sComp;
            } else {
                return u1.getEcasId().compareTo(u2.getEcasId());
            }
        });

        for (DBUser dbUser : usrList) {
            list.add(new User(dbUser));
        }
        return list;
    }

    public User getUser(String ecasId) {
        DBUser dbUser = getDBUser(ecasId);
        if (dbUser == null) {
            LOGGER.error("Get user null " + ecasId);
            return null;
        }
        return new User(dbUser);
    }

    /**
     * Check if user with given ecas id already exist in DB
     *
     * @param ecasId
     * @return true if user find
     */
    public boolean userExist(String ecasId) {
        return getDBUser(ecasId) != null;
    }

    public DBUser getDBUser(String ecasId) {
        if (StringUtils.isEmpty(ecasId)) {
            LOGGER.error("EcasId is null ot empty");
            return null;
        }
        return userRepository.findByEcasId(ecasId);
    }

    public String getDbUserName(int id) {
        DBUser user = userRepository.findOne(id);
        return user.getEcasId();
    }

    /**
     * Update : add role;
     *
     * @param userId
     * @param roleId
     * @return action success result
     */
    public boolean addRole(int userId, int roleId) {
        return updateRole(userId, roleId, addRole);
    }

    /**
     * Update : remove role;
     *
     * @param userId
     * @param roleId
     * @return action success result
     */
    public boolean removeRole(int userId, int roleId) {
        return updateRole(userId, roleId, removeRole);
    }

    /**
     * Update role by type (Add/Remove) Error : user is null Error : role is null
     *
     * @param userId
     * @param roleId
     * @param updateType
     * @return action success result
     */
    private boolean updateRole(int userId, int roleId, String updateType) {
        DBUser user = userRepository.findOne(userId);
        if (user == null) {
            LOGGER.error("Update role, user is null :" + userId);
            return false;
        }
        DBRole role = roleRepository.findOne(roleId);
        if (role == null) {
            LOGGER.error("Update role, role is null : " + roleId);
            return false;
        }

        if (updateType.equals(addRole)) {
            user.getRole().add(role);
        } else {
            user.getRole().remove(role);
        }
        return true;
    }

    /**
     * Update user territory; Error : user is null; Error : country is null;
     *
     * @param ecasId
     * @param territory
     * @return action success result
     */
    public boolean updateTerritory(String ecasId, String territory) {
        DBUser user = getDBUser(ecasId);
        if (user == null) {
            LOGGER.error("Update territory, user is null");
            return false;
        }
        DBCountries country = countryService.getCountryByTerritory(territory);
        if (country == null) {
            LOGGER.error("Update territory, country is null");
            return false;
        }
        user.setTerritory(country);
        return true;
    }

    public User addUser(String ecasName, String territory) {
        if (StringUtils.isEmpty(ecasName)) {
            LOGGER.error("Add User, Ecas name to add is not a valid string input");
            return null;
        }
        DBCountries country = countryService.getCountryByTerritory(territory);
        if (country == null) {
            return null;
        }
        DBUser dbUser = new DBUser();
        dbUser.setEcasId(ecasName);
        dbUser.setTerritory(country);
        try {
            userRepository.save(dbUser);
            return new User(dbUser);
        } catch (Exception e) {
            LOGGER.error("Ecas id already exist", e);
            return null;
        }
    }

    public boolean deleteUser(int id) {
        DBUser dbUser = userRepository.findOne(id);
        if (dbUser == null) {
            return false;
        }
        dbUser.getRole().removeAll(dbUser.getRole());
        userRepository.delete(dbUser);
        return true;
    }

    public String getCodeRole(int roleId) {
        DBRole role = roleRepository.findOne(roleId);
        if (role == null) {
            LOGGER.error("Get code, role is null : " + roleId);
            return null;
        }
        return role.getCode();
    }

    /**
     * Find authenticated user by territory;
     *
     * @param territory
     * @return list of user from specific territory with an authenticated role
     */
    public List<DBUser> findAuthenticatedUserByTerritory(String territory) {
        if (territory == null) {
            LOGGER.error("FindAuthenticatedUser , Code territory is null");
            return Collections.emptyList();
        }
        DBCountries country = countryService.getCountryByTerritory(territory);

        if (country == null) {
            LOGGER.error("FindAuthenticatedUser , Country with territory code " + territory + " doesn't exist");
            return Collections.emptyList();
        }
        return userRepository.findByTerritoryAndRoleCode(country, authenticated);
    }

    /**
     * Update user (create & remove) between user in database & tmp list; Set Authenticated Role by default to all users persisted;
     *
     * @param territory
     * @param users
     * @return true if users were updated
     */
    public boolean updateAuthenticatedUser(UserChangeDTO userChange) {

        for (DBUser dbUser : userChange.getDbUsers()) {
            if (userChange.getUserRemoved().contains(dbUser.getEcasId())) {
                userRepository.delete(dbUser);
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.DELETE, AuditStatus.SUCCES, userChange.getTerritory(), 0, "SYSTEM", "Delete user: " + dbUser.getEcasId());
            }
        }

        if (!CollectionUtils.isEmpty(userChange.getUserAdded())) {
            DBUser userAth;
            DBRole athRole = roleRepository.findByCode(authenticated);
            DBCountries country = countryService.getCountryByTerritory(userChange.getTerritory());
            for (User user : userChange.getUsers()) {
                if (userChange.getUserAdded().contains(user.getEcasId())) {
                    userAth = userRepository.findByEcasId(user.getEcasId());
                    if (userAth == null) {
                        userAth = new DBUser();
                        userAth.setEcasId(user.getEcasId());
                        userAth.getRole().add(athRole);
                        userAth.setTerritory(country);
                        auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.CREATE, AuditStatus.SUCCES, userChange.getTerritory(), 0, "NOTIFICATION",
                                "New user authenticated: " + user.getEcasId());
                    } else {
                        if (userAth.getTerritory() == null) {
                            auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.ERROR, userChange.getTerritory(), 0, "NOTIFICATION",
                                    "User " + userAth.getEcasId() + " cannot be updated due to territory NULL");
                        } else if (!userAth.getTerritory().getCodeTerritory().equals(userChange.getTerritory())) {
                            auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.ERROR, userChange.getTerritory(), 0, "NOTIFICATION",
                                    "User " + userAth.getEcasId() + " cannot be updated by " + userChange.getTerritory() + ". Already set for " + userAth.getTerritory().getCodeTerritory());

                        } else {
                            userAth.setName(user.getName());
                            userAth.getRole().add(athRole);
                            auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.SUCCES, userChange.getTerritory(), 0, "NOTIFICATION",
                                    "Add user authenticated role: " + user.getEcasId());
                        }
                    }
                    userRepository.save(userAth);
                }
            }
        }
        return (!CollectionUtils.isEmpty(userChange.getUserRemoved()) || !CollectionUtils.isEmpty(userChange.getUserAdded()));
    }

    public UserChangeDTO initUserChange(String territory, List<User> users) {
        List<DBUser> dbUsers = findAuthenticatedUserByTerritory(territory);

        Set<String> userRemoved = getUserRemoved(dbUsers, users);
        Set<String> userAdded = getUserAdded(dbUsers, users);

        return new UserChangeDTO(territory, dbUsers, users, userRemoved, userAdded);
    }

    /*----- DIFFERENCE -----*/

    /**
     * Get list of ecasId difference between Authenticated user stored in database for a specific country and temp list;
     *
     * @param parentId
     * @param users
     * @param territory
     * @return
     */
    public List<TLDifference> getUserDifference(String parentId, List<User> users, String territory) {
        List<TLDifference> differences = new ArrayList<>();

        UserChangeDTO userChange = initUserChange(territory, users);
        TLDifference diff = new TLDifference();
        for (String remove : userChange.getUserRemoved()) {
            diff = new TLDifference(parentId + "_" + Tag.USER, remove, "");
            diff.setHrLocation(" || " + bundle.getString("ecas.name"));
            differences.add(diff);
        }

        for (String added : userChange.getUserAdded()) {
            diff = new TLDifference(parentId + "_" + Tag.USER, "", added);
            diff.setHrLocation(" || " + bundle.getString("ecas.name"));
            differences.add(diff);
        }
        return differences;
    }

    /**
     * Get User removed between Database user & temp list;
     *
     * @param dbUsers
     * @param users
     * @return list of ecasId from users removed;
     */
    private Set<String> getUserRemoved(List<DBUser> dbUsers, List<User> users) {
        Set<String> userRemoved = new HashSet<>();
        boolean found;
        int it;
        if (dbUsers == null) {
            return userRemoved;
        }
        for (DBUser dbUser : dbUsers) {
            found = false;
            it = 0;
            while (!found && (it < users.size())) {
                if (dbUser.getEcasId().equals(users.get(it).getEcasId())) {
                    found = true;
                }
                it++;
            }
            if (!found) {
                if (!StringUtils.isEmpty(dbUser.getEcasId())) {
                    userRemoved.add(dbUser.getEcasId());
                }
            }
        }
        return userRemoved;
    }

    /**
     * Get User added between temp list & Database user;
     *
     * @param dbUsers
     * @param users
     * @return list of ecasId from users removed;
     */
    private Set<String> getUserAdded(List<DBUser> dbUsers, List<User> users) {
        Set<String> userAdded = new HashSet<>();
        boolean found;
        int it;
        if (users == null) {
            return userAdded;
        }
        for (User user : users) {
            found = false;
            it = 0;
            while (!found && (it < dbUsers.size())) {
                if ((user.getEcasId() != null) && user.getEcasId().equals(dbUsers.get(it).getEcasId())) {
                    found = true;
                }
                it++;
            }
            if (!found) {
                if (!StringUtils.isEmpty(user.getEcasId())) {
                    userAdded.add(user.getEcasId());
                }
            }
        }
        return userAdded;
    }

}
