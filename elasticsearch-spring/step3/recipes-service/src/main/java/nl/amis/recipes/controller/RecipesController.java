package nl.amis.recipes.controller;

import nl.amis.recipes.document.Recipe;
import nl.amis.recipes.repository.RecipesRepository;
import nl.amis.recipes.repository.RecipesRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class RecipesController {

  @Autowired
  RecipesRepository recipesRepository;

  @Autowired
  RecipesRepositoryCustom recipesRepositoryCustom;

  @GetMapping("recipes/{id}")
  public Recipe getRecipe(@PathVariable("id") String id) {
    return recipesRepository.findById(id).get();
  }

  @PostMapping("recipes")
  public Recipe insertRecipe(@RequestBody Recipe recipe) {
      return recipesRepository.save(recipe);
  }

  @DeleteMapping("recipes/{id}")
  public void deleteRecipe(@PathVariable("id") String id) {
    recipesRepository.deleteById(id);
  }

  @GetMapping("recipes/findbyname")
  public List<Recipe> findRecipeByName(@RequestParam("name") String name) {
    return recipesRepository.findByName(name);
  }

  @GetMapping("recipes/findlast5years")
  public List<Recipe> findRecipesLast5Years() {
    return recipesRepository.findFromLast5Years();
  }

  @GetMapping("recipes/findbynamecustom")
  public List<Recipe> findRecipeByNameCustom(@RequestParam("name") String name) {
    return recipesRepositoryCustom.findByName(name);
  }

  @GetMapping("recipes/nottop10sources")
  public long findRecipeByNameCustom() {
    return recipesRepositoryCustom.notTop10Sources();
  }

}
