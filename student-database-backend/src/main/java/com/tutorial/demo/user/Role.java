package com.tutorial.demo.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.tutorial.demo.user.UserPermission.*;

public enum Role {
    STUDENT(Set.of(STUDENT_READ, COURSE_READ)),
    ADMIN(Set.of(STUDENT_READ, COURSE_READ, STUDENT_WRITE, COURSE_WRITE));

    private final Set<UserPermission> permissions;

    Role(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        System.out.println(permissions);
        return permissions;
    }
}
