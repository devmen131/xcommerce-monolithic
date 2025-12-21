package ma.aui.sse.it.xcommerce.monolithic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Authority;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.User;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.AuthorityRepository;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.quarkus.elytron.security.common.BcryptUtil;

import java.util.Arrays;

@ApplicationScoped
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    @Inject
    AuthorityRepository authorityRepository;

    public boolean createSuperAdmin(String username, String password, String firstName, String lastName,
            String emailAddress, String address) {
        LOG.debug("Creation d'un super admin (username={}, email={})", username, emailAddress);

        if (!authorityRepository.findByAuthority("ROLE_SUPERADMIN").isEmpty())
            return false;

        String[] authorities = { "ROLE_SUPERADMIN" };
        create(username, password, firstName, lastName, emailAddress, address, authorities);
        return true;
    }

    public void createAdmin(String username, String password, String firstName, String lastName, String emailAddress,
            String address) {
        LOG.debug("Creation d'un admin (username={}, email={})", username, emailAddress);

        String[] authorities = { "ROLE_ADMIN", "ROLE_USER" };
        create(username, password, firstName, lastName, emailAddress, address, authorities);
    }

    public void createUser(String username, String password, String firstName, String lastName, String emailAddress,
            String address) {
        LOG.debug("Creation d'un utilisateur (username={}, email={})", username, emailAddress);

        String[] authorities = { "ROLE_USER" };
        create(username, password, firstName, lastName, emailAddress, address, authorities);
    }

    private void create(String username, String password, String firstName, String lastName, String emailAddress,
            String address, String[] authorities) {
        LOG.debug("Creation de compte (username={}, email={}, authorities={})", username, emailAddress,
                  authorities != null ? Arrays.toString(authorities) : null);

        User user = new User(username, password, firstName, lastName, emailAddress, address);
        userRepository.save(user);
        for (String auth : authorities) {
            Authority authority = new Authority(username, auth);
            authorityRepository.save(authority);
        }
    }

    public void update(long userId, String password, String newPassword, String firstName, String lastName,
            String emailAddress, String address) {
        LOG.debug("Mise a jour utilisateur {} (nouveauMotDePasse={}, prenom={}, nom={}, email={}, adresse={})",
                  userId, newPassword != null, firstName != null, lastName != null, emailAddress != null,
                  address != null);
        User user = userRepository.findById(userId).get();
        if (password != null && (newPassword == null || !BcryptUtil.matches(password, user.getPassword()))) {
            return;
        }

        if (newPassword != null)
            user.setPassword(BcryptUtil.bcryptHash(newPassword));
        if (firstName != null)
            user.setFirstName(firstName);
        if (lastName != null)
            user.setLastName(lastName);
        if (emailAddress != null)
            user.setEmailAddress(emailAddress);
        if (address != null)
            user.setAddress(address);

        userRepository.save(user);
    }
}
