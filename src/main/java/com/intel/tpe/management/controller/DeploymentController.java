package com.intel.tpe.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller to call deployment operations by customer
 * 1. deploy
 * 2. update - Day 2 ops
 * 3. health check
 * 4. Delete deployment
 */
@RequestMapping("/management")
@RestController
public class DeploymentController {
    // Call deployment service to create new deployment
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String DAPR_HTTP_PORT = System.getenv().getOrDefault("DAPR_HTTP_PORT", "3500");

    @PostMapping("/deploy/{customerId}")
    public ResponseEntity<Object> callDeployment(@PathVariable String customerId) throws IOException, InterruptedException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("customerId", customerId);
        requestBody.put("ledgerId", UUID.randomUUID());
        requestBody.put("keyToDeploy", UUID.randomUUID().toString());
        String dapr_url = "http://localhost:" + DAPR_HTTP_PORT + "/deployment/deploy";
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
                .uri(URI.create(dapr_url))
                .header("Content-Type", "application/json")
                .header("dapr-app-id", "deployment")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ResponseEntity(response.body(), HttpStatus.OK);
    }

    // Get all deployments status by a customer
}
