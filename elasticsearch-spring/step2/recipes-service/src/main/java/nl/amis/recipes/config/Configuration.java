package nl.amis.recipes.config;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@org.springframework.context.annotation.Configuration
public class Configuration {

  @Value("${elasticsearch.host}")
  private String hostname;
  @Value("${elasticsearch.port}")
  private int port;

  @Bean
  public RestHighLevelClient client() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
      .connectedTo(hostname + ":" + port)
      .build();

    return RestClients.create(clientConfiguration).rest();
  }
}
