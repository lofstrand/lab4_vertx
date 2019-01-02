package com.vertx;

import com.vertx.entity.BlogPostDto;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class DataServer extends AbstractVerticle {
    public static final String REST_BLOGPOSTS_URI = "http://192.168.99.100:8081/rest/api/blogposts";

    private MongoClient mongoClient;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Launcher.executeCommand("run", DataServer.class.getName());
    }

    /**
     *
     */
    @Override
    public void start()  {

        String uri = "mongodb://192.168.99.100:27017";
        String db = "vertx";

        JsonObject mongoConfig = new JsonObject();
        mongoConfig.put("connection_string", uri);
        mongoConfig.put("db_name", db);
        mongoClient = MongoClient.createShared(vertx, mongoConfig );

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/api/data").handler( routingContext -> {
            mongoClient.find("dataset", new JsonObject(), find -> {
                if (find.failed()) {
                    routingContext.fail(404);
                } else {
                    JsonArray json = new JsonArray();

                    for (JsonObject o : find.result()) {
                        json.add(o);
                    }

                    routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    routingContext.response().end(json.encode());
                }
            });
        });

        router.get("/api/data/:id").handler( routingContext -> {
            mongoClient.findOne("dataset", new JsonObject().put("_id", routingContext.request().getParam("id")), null, find -> {

                JsonObject dataset = find.result();

                if (dataset == null) {
                    routingContext.fail(404);
                } else {
                    routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    routingContext.response().end(dataset.encode());
                }
            });
        });

        router.post("/api/data").handler( routingContext -> {
            JsonObject bodyAsJson = routingContext.getBodyAsJson();
            JsonObject jsonObject = new JsonObject(bodyAsJson.getString("data"));

            // Seems like we need to insert as JSON (?). Either way this is how we parse - saving code for future needs..
            // DataSet pojo = Json.mapper.convertValue ( obj.getMap(), DataSet.class );
            // System.out.println(pojo.getName());
            mongoClient.insert("dataset", jsonObject, insert -> {

                if (insert.failed()) {
                    routingContext.fail(404);
                } else {
                    jsonObject.put("_id", insert.result());

                    routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    routingContext.response().end(jsonObject.encode());
                }
            });
        });

        router.post("/api/sharegraph").handler( routingContext -> {
            String url = routingContext.getBodyAsString();
            BlogPostDto blogpost = convertCtxToBlogPostDto(url);

            // POST TO BACKEND HERE!!!
            submitBlogPostToRest(blogpost);

            if (false) {
                routingContext.fail(404);
            } else {

                routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                routingContext.response().end();
            }
        });

        router.delete("/api/users/:id").handler(routingContext -> {
            mongoClient.findOne("users", new JsonObject().put("_id", routingContext.request().getParam("id")), null, find -> {

                JsonObject user = find.result();

                if (user == null) {
                    routingContext.fail(404);
                } else {
                    mongoClient.removeDocument("users", new JsonObject().put("_id", routingContext.request().getParam("id")), remove -> {
                        if (remove.failed()) {
                            routingContext.fail(404);
                        } else {
                            routingContext.response().setStatusCode(204);
                            routingContext.response().end();
                        }
                    });
                }
            });
        });

        router.route("/*").handler(StaticHandler.create("webapp"));
        vertx.createHttpServer().requestHandler(router).listen(8090);
    }

    /**
     * Posting blogpost to the Blog post Rest API
     *
     * @param blogpost
     */
    private void submitBlogPostToRest(BlogPostDto blogpost) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            URI uri = restTemplate.postForLocation(REST_BLOGPOSTS_URI + "/", blogpost, BlogPostDto.class);
            System.out.println("Location : " + uri.toASCIIString());
        } catch  (Exception e) {
            System.out.println("BP: " + blogpost.getGraph_url() + ", " + blogpost.getOwner());
            System.out.println("Something went wrong");
        }
    }

    /**
     * Helper method to create a Blog post Dto from an url
     *
     * @param url the url
     * @return the blogpost dto
     */
    private BlogPostDto convertCtxToBlogPostDto(String url) {
        String newUrl = url.replace("viewgraph", "graph");
        int startUID = newUrl.indexOf("?uid=");
        int endUID = newUrl.indexOf("#/");
        String replacement = "";
        String toBeReplaced = newUrl.substring(startUID, endUID);
        String graphURL = newUrl.replace(toBeReplaced, replacement);
        String uid = toBeReplaced.replace("?uid=", "");

        // Create the blogpost with uid and graphURL
        BlogPostDto blogPost = new BlogPostDto(Long.valueOf(uid), graphURL);
        return blogPost;
    }
}