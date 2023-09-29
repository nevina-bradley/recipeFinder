package com.nevinabradley.recipeFinder.domain.recipeFinder.repos;

import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeFinderRepo extends JpaRepository<RecipeFinder, Integer> {
    Optional<RecipeFinder> findByName(String name);
}
