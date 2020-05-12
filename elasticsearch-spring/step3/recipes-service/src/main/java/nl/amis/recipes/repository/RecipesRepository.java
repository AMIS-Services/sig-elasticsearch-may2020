package nl.amis.recipes.repository;

import nl.amis.recipes.document.Recipe;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipesRepository extends ElasticsearchCrudRepository<Recipe, String> {

  @Query("{\"match\": {\"name\": \"?0\"} }")
  List<Recipe> findByName(@Param("name") String name);

  @Query("{\"range\" : {\"datePublished\" : {\"gte\" : \"now-5y/y\"} }}")
  List<Recipe> findFromLast5Years();
}
