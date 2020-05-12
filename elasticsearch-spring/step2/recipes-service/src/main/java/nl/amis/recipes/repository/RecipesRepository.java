package nl.amis.recipes.repository;

import nl.amis.recipes.document.Recipe;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipesRepository extends ElasticsearchCrudRepository<Recipe, String> {

  //  Step 3.2
//  List<Recipe> findByName(@Param("name") String name);

  // Step 3.3
}
