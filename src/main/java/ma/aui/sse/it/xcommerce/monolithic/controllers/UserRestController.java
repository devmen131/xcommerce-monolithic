package ma.aui.sse.it.xcommerce.monolithic.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import java.util.Set;
import java.util.stream.Collectors;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.UserDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Authority;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.User;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.AuthorityRepository;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.UserRepository;
import ma.aui.sse.it.xcommerce.monolithic.security.JwtHelper;
import ma.aui.sse.it.xcommerce.monolithic.services.UserService;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/rest/user")
public class UserRestController {

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    @Inject
    UserRepository userRepository;

    @Inject
    AuthorityRepository authorityRepository;

    @Inject
    UserService userService;

    @POST
    @Path("/authenticate")
    @PermitAll
    public String authenticate(UserDto dto) {
        String username = dto != null ? dto.getUsername() : null;
        LOG.debug("authenticate : username={}", username);
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            throw new WebApplicationException("Missing credentials", Response.Status.BAD_REQUEST);
        }
        User user = userRepository.findByUsername(username);
        if (user == null || !BcryptUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }
        Set<String> roles = authorityRepository.findByUsername(username).stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
        return JwtHelper.generateToken(username, roles);
    }

    @POST
    @Path("/admin")
    @RolesAllowed("ROLE_SUPERADMIN")
    public boolean createAdmin(UserDto dto) {
        String username = dto != null ? dto.getUsername() : null;
        LOG.debug("createAdmin : username={}", username);
        if (!check(dto))
            return false;

        userService.createAdmin(dto.getUsername(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getEmailAddress(), dto.getAddress());
        return true;
    }

    @POST
    @PermitAll
    public boolean createUser(UserDto dto) {
        String username = dto != null ? dto.getUsername() : null;
        LOG.debug("createUser : username={}", username);
        if (!check(dto))
            return false;

        userService.createUser(dto.getUsername(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getEmailAddress(), dto.getAddress());
        return true;
    }

    @PATCH
    @Path("/{userId}")
    public UserDto update(@PathParam("userId") long userId, UserDto dto) {
        String username = dto != null ? dto.getUsername() : null;
        String email = dto != null ? dto.getEmailAddress() : null;
        LOG.debug("update : userId={}, username={}, email={}", userId, username, email);
        if (userId < 1)
            throw new RuntimeException();

        userService.update(userId, dto.getPassword(), dto.getNewPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getEmailAddress(), dto.getAddress());

        return new UserDto(dto.getUsername(), null, dto.getFirstName(), dto.getLastName(), dto.getEmailAddress(),
                dto.getAddress());
    }

    private boolean check(UserDto dto) {
        return (dto.getUsername() != null && dto.getPassword() != null && dto.getFirstName() != null
                && dto.getLastName() != null && dto.getEmailAddress() != null && dto.getAddress() != null);
    }
}
