package com.nevinabradley.recipeFinder.models;

import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeFinderTest {
    private RecipeFinder recipe1;
    private RecipeFinder recipe2;
    private RecipeFinder emptyRecipe1;
    private RecipeFinder emptyRecipe2;

    //indicates that the method should be executed before each test
    @BeforeEach
    public void setUp() {
        //sets up an empty recipe
        emptyRecipe1 = new RecipeFinder();
        //sets up an empty recipe
        emptyRecipe2 = new RecipeFinder();

        //sets the name of the fake data/recipe
        recipe1 = new RecipeFinder("Test Recipe 1");
        //sets the id of the fake data/recipe
        recipe1.setId(1);

        //sets the name of the fake data/recipe
        recipe2 = new RecipeFinder("Test Recipe 2");
        //sets the id of the fake data/recipe
        recipe2.setId(2);
    }

    @Test
    public void testEmptyToString() throws Exception {
        //tests to see if the empty fake data/recipes could be converted to strings and compares them
        assertEquals(emptyRecipe1.toString(), emptyRecipe2.toString(), "Both empty Recipe instances should have the same toString");
    }

    @Test
    public void testNotToString() throws Exception {
        //tests to see if the empty fake data/recipe and the full fake data/recipe could be converted to strings and compares them
        assertNotEquals(emptyRecipe1.toString(), recipe2.toString(), "The Recipe instances should not have the same toString");
    }
}
