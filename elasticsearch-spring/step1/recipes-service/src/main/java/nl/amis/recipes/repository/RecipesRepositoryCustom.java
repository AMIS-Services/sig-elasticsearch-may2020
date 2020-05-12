package nl.amis.recipes.repository;

import nl.amis.recipes.document.Recipe;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class RecipesRepositoryCustom {

  @Autowired
  private ElasticsearchRestTemplate esTemplate;

  // Step 3.4
  public List<Recipe> findByName(String name) {
    return null;
  }

  // Step 3.5
  public List<Recipe> find() {
    return null;
  }


}
