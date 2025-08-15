package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false,name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(nullable = false,name = "age")
    private Integer age;

    @Column(nullable = false, name = "email", length = Integer.MAX_VALUE)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public User(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}