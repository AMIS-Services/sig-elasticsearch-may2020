package nl.amis.recipes.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.Date;

@Getter
@Setter
public class Recipe {

  @Id
  private String id;

  private String name;

  private String source;

  private String url;

  private String recipeYield;

  private String[] ingredients;

  private String prepTime;

  private String cookTime;

  private Date datePublished;

  private String description;
}
