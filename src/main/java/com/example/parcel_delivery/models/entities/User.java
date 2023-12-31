package com.example.parcel_delivery.models.entities;

import java.util.HashSet;
import java.util.Set;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
    
    private String phoneNumber;

    private String email;

    private String firstName;
    private String lastName;

    private String address;
    private String postcode;
    private String city;

    @Column(name = "location_point", columnDefinition = "geometry(Point,4326)")
    @JsonIgnore
    private Point userPoint;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Driver driver;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Customer customer;

    //notification relation
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Notification> notifications = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinTable(name = "users_roles", 
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

}
