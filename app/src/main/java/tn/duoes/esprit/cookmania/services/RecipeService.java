package tn.duoes.esprit.cookmania.services;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.models.Recipe;

public class RecipeService {

    public static List<Recipe> getTopRatedRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(1, "TestRes1", "im_1"));
        recipes.add(new Recipe(2, "TestRes2", "im_2"));
        recipes.add(new Recipe(3, "TestRes3", "im_3"));
        recipes.add(new Recipe(4, "TestRes4", "im_4"));
        recipes.add(new Recipe(5, "TestRes5", "im_2"));

        return recipes;
    }

    public static List<Recipe> getHealthyRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(1, "TestRes1", "im_1"));
        recipes.add(new Recipe(2, "TestRes2", "im_2"));
        recipes.add(new Recipe(3, "TestRes3", "im_3"));
        recipes.add(new Recipe(4, "TestRes4", "im_4"));
        recipes.add(new Recipe(5, "TestRes5", "im_2"));

        return recipes;
    }

    public static List<Recipe> getCheapRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(1, "TestRes1", "im_1"));
        recipes.add(new Recipe(2, "TestRes2", "im_2"));
        recipes.add(new Recipe(3, "TestRes3", "im_3"));
        recipes.add(new Recipe(4, "TestRes4", "im_4"));
        recipes.add(new Recipe(5, "TestRes5", "im_2"));

        return recipes;
    }

}
