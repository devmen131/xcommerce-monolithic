package ma.aui.sse.it.xcommerce.monolithic.data.entities;

import io.quarkus.elytron.security.common.BcryptUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Omar IRAQI
 */
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {

    @NotNull
    protected String username;

    @NotNull
    protected String password;

    @NotNull
    @Column(name = "first_name")
    protected String firstName;

    @NotNull
    @Column(name = "last_name")
    protected String lastName;

    @NotNull
    @Column(name = "email_address")
    protected String emailAddress;

    @NotNull
    protected String address;

    protected User() {
    }

    public User(String username, String password, String firstName, String lastName, String emailAddress,
            String address) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
