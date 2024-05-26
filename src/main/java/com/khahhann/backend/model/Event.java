package com.khahhann.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "img", columnDefinition = "LONGTEXT")
    @Lob
    private String image;

    @Column(name = "link", columnDefinition = "LONGTEXT")
    private String link;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;
}
