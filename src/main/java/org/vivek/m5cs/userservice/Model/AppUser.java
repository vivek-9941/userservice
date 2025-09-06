package org.vivek.m5cs.userservice.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AppUser  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column( nullable = true)
    private String firstName;
    @Column( nullable = true)
    private String lastName;
    @Column( nullable = false, unique = true)
    private String username;
    @Column( nullable = false)
    private String password;
    @Column( nullable = true)
    private boolean isVerified;
    @Column( nullable = true)
    private String aadharNumber;
    @Column( nullable = true)
    private String email;
    @Column( nullable = true)
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    Role role;


//    @OneToMany
//    @JsonManagedReference
//    List<Complaint> listComplaints;


}
