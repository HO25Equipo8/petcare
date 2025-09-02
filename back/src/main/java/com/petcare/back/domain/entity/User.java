package com.petcare.back.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.petcare.back.domain.enumerated.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email; // user login
    private String password;

    private String provider; // google sign in
    private String providerId; //google sign in

    @OneToOne(cascade = CascadeType.ALL) // si querés que se guarde Location automáticamente
    @JoinColumn(name = "location_id")
    private Location location;

    private String phone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference   // prevents recursion
    private List<Pet> pets = new ArrayList<>();

    private boolean verified;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_perfil_id")
    private Image profilePhoto;

    //Fotos del dni del Sitter para verificar la identidad
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Image> photosVerifyIdentity;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ElementCollection(targetClass = ProfessionalRoleEnum.class)
    @CollectionTable(name = "user_professional_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<ProfessionalRoleEnum> professionalRoles = new ArrayList<>(); // Ej: PASEADOR, PELUQUERO, VETERINARIO Solo para SITTER

    @OneToMany(mappedBy = "owner")
    private List<Booking> bookingsAsOwner;

    @OneToMany(mappedBy = "sitter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ScheduleConfig> scheduleConfigs;

    @Column(nullable = false)
    private Boolean active = true;

    //minimum atributes constructor
    public User(@Email String login, String encryptedPassword, Role role) {
        this.email = login;
        this.password = encryptedPassword;
        this.role = role;
    }

    // OAuth users constructor
    public User(String email, String name, String provider, String providerId, Role role) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    // Helper method for JWT claims
    public String getRoleName() {
        return role.name();
    }

}
