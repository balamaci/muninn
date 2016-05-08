package ro.fortsoft.monitor.elasticsearch;

import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author sbalamaci
 */
public class ElasticSearchGateway {

    private TransportClient esClient;

    private JestClient client;

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchGateway.class);

    public ElasticSearchGateway(JestClient client) {
        this.client = client;
    }

    public void submit() {
        SearchResponse response = esClient.prepareSearch("index1", "index2")
                .setTypes("type1", "type2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .execute()
                .actionGet();


    }

    public JsonObject submit(List<String> indexes, String query) throws IOException {
        log.info("Query {}", query);
        Search.Builder searchBuilder = new Search.Builder(query);

        indexes.stream().forEach(searchBuilder::addIndex);

        Search search = searchBuilder
                // multiple index or types can be added.
                .build();

        JestResult result = client.execute(search);
        return result.getJsonObject();
    }
}