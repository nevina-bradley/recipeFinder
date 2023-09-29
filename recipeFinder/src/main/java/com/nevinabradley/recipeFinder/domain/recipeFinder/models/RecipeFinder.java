package com.nevinabradley.recipeFinder.domain.recipeFinder.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class RecipeFinder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String ingredients;

    @NonNull
    private String steps;

    @NonNull
    @Column(name = "`user`")
    private String user;

    @NonNull
    private Integer rating;

    public RecipeFinder(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format("%d %s %s %s %s %d", id, name, ingredients, steps, user, rating);
    }
}
