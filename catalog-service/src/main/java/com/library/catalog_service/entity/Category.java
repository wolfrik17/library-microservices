package com.library.catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.io.Serializable;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Book> books;
}