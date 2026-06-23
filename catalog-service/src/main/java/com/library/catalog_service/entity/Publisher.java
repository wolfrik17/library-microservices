package com.library.catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.io.Serializable;

@Entity
@Table(name = "publishers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // First @OneToMany Requirement
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Book> books;
}