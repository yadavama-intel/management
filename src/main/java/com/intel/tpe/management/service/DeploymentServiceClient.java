package com.intel.tpe.management.service;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class DeploymentServiceClient {

    public byte[] callDeployment(String customerId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("customerId", customerId);
        requestBody.put("ledgerId", UUID.randomUUID());
        requestBody.put("keyToDeploy", UUID.randomUUID().toString());
        try (DaprClient client = (new DaprClientBuilder()).build()) {
            // invoke a 'GET' method (HTTP) skipping serialization: \say with a Mono<byte[]> return type
            // for gRPC set HttpExtension.NONE parameters below
//            Mono<byte[]> response = client.invokeMethod("deployment", "runDeployment", "{\"name\":\"World!\"}", HttpExtension.GET, byte[].class);

            // invoke a 'POST' method (HTTP) skipping serialization: to \say with a Mono<byte[]> return type
            byte[] response = client.invokeMethod("deployment", "runDeployment", requestBody, HttpExtension.POST, byte[].class).block();
            log.info("response {} ", response);
            System.out.println(new String(response));
            return response;
        }
    }
}
