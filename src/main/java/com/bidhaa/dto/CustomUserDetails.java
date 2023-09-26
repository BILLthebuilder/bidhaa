package com.bidhaa.dto;

import com.bidhaa.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * Internally, user security requires a list of authorities, roles, that the user has. This method is a simple way to provide those.
     * Note that SimpleGrantedAuthority requests the format ROLE_role name all in capital letters!
     *
     * @return The list of authorities, roles, this user object has
     */
//    @JsonIgnore
//    public List<SimpleGrantedAuthority> getAuthorities()
//    {
//        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();
//        Set<UserRoles> roles = user.getRoles();
//
//        for (UserRoles r : roles)
//        {
//            String myRole = "ROLE_" + r.getRole()
//                    .getRoleName()
//                    .toUpperCase();
//            rtnList.add(new SimpleGrantedAuthority(myRole));
//        }
//
//        return rtnList;
//    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }
}