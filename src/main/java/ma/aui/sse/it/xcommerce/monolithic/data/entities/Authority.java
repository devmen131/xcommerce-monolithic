package ma.aui.sse.it.xcommerce.monolithic.data.entities;

import io.micrometer.core.lang.NonNull;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Authority extends BaseEntity {

    @NonNull
    private String username;

    @NotNull
    private String authority;

    protected Authority(){ }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}