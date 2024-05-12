package com.khahhann.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Column(name = "role_name", nullable = false, length = 10)
    private String roleName;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private List<Users> userList;

    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private Date updateAt;


}
