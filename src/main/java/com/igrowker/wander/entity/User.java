package com.igrowker.wander.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;

    @NotNull(message = "The email is required")
    @Email(message = "must be a well-formed email address")
    private String email;

    @Indexed(unique = true)
    private String name;

    @NotNull(message = "The password is required")
    @Size(min = 6, message = "The password is too short")
    private String password;

    private String role = "TOURIST";

    private String avatar = "https://medvirturials.com/img/default-image.png";

    private boolean enabled = false;

    private List<String> preferences;

    private String location;

    private String phone;

    private Date createdAt = new Date();

    private List<Long> bookings;

    private String verificationCode;

    private Date verificationCodeExpiresAt;

    private String passwordResetCode;
    private Date passwordResetCodeExpiresAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + this.role);
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.enabled;
    }
}