package co.istad.ifinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.typesense.api.Client;
import org.typesense.api.Configuration;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@org.springframework.context.annotation.Configuration
public class TypesenseConfig {

    @Value("${typesense.port}")
    String typesensePort;

    @Value("${typesense.apiKey}")
    String typesenseApiKey;

    @Value("${typesense.ip}")
    String typesenseIp;

    @Bean
    public List<Node> nodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(
                "http",       // For Typesense Cloud use https
                typesenseIp,  // For Typesense Cloud use xxx.a1.typesense.net
                typesensePort
        ));
        return nodes;
    }

    @Bean
    Client client() {
        Configuration configuration = new Configuration(nodes(), Duration.ofSeconds(2), typesenseApiKey);
        return new Client(configuration);
    }

}
