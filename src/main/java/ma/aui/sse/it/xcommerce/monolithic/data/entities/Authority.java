package ma.aui.sse.it.xcommerce.monolithic.data.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class Authority extends BaseEntity {

    @NotNull
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
