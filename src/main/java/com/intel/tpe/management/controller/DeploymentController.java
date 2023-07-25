package com.intel.tpe.management.controller;

import com.intel.tpe.management.service.DeploymentServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RequestMapping("/management")
@RestController
public class DeploymentController {

    @Autowired
    private DeploymentServiceClient deploymentServiceClient;
    // Call deployment service to create new deployment
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String DAPR_HTTP_PORT = System.getenv().getOrDefault("DAPR_HTTP_PORT", "3500");

    @PostMapping("/deploy/{customerId}")
    public ResponseEntity<Object> callDeployment(@PathVariable String customerId) throws Exception {
        log.info("calling deployment service ");
        deploymentServiceClient.callDeployment(customerId);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("customerId", customerId);
        requestBody.put("ledgerId", UUID.randomUUID());
        requestBody.put("keyToDeploy", UUID.randomUUID().toString());
        String dapr_url = "http://localhost:" + DAPR_HTTP_PORT + "/deployment/deploy";
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .uri(URI.create(dapr_url))
                .header("Content-Type", "application/json")
                .header("app-id", "deployment")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("response {} ", response.body());
        return new ResponseEntity(response.body(), HttpStatus.OK);
    }

    // Get all deployments status by a customer
}
