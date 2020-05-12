package nl.amis.recipes.repository;

import nl.amis.recipes.document.Recipe;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class RecipesRepositoryCustom {

  @Autowired
  private ElasticsearchRestTemplate esTemplate;

  public List<Recipe> findByName(String name) {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
      .withQuery(QueryBuilders.matchQuery("name", name))
      .build();

    return esTemplate.queryForList(searchQuery, Recipe.class);
  }

  // Step 3.5
  public long notTop10Sources() {
    TermsAggregationBuilder aggregation = AggregationBuilders.terms("top_source")
      .field("source.keyword");
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
      .withIndices("recipes")
      .addAggregation(aggregation)
      .build();
    Aggregations aggregations = esTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
      @Override
      public Aggregations extract(SearchResponse response) {
        return response.getAggregations();
      }
    });
    return ((ParsedStringTerms)aggregations.asMap().get("top_source")).getSumOfOtherDocCounts();
  }


}
