package co.istad.ifinder.features.typesense;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.typesense.api.Client;
import org.typesense.model.ApiKey;
import org.typesense.model.ApiKeySchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypesenseService {

    private final Client client;

    public void generateApiKey() throws Exception {

        String keyWithSearchPermissions = "12xYtd95dD5brGjP2HtjEkJ9iLY4srgN";

        HashMap<String, Object> parameters = new HashMap<>();

        String key = client.keys().generateScopedSearchKey(keyWithSearchPermissions,parameters);

    }
}
