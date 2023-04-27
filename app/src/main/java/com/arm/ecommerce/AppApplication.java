package com.arm.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;


/**
 * The main class that starts the Spring Boot application.
 * It registers the server with the coordinator by sending a GET request to the coordinator REST API.
 */
@SpringBootApplication
@EntityScan(basePackages = "com.arm.ecommerce")
@EnableJpaRepositories("com.arm.ecommerce")
public class AppApplication {

    /**
     * Logger instance for logging server events.
     */
    private static final Logger serverLogger = Logger.getLogger(AppApplication.class.getSimpleName());

    /**
     * The main method that starts the Spring Boot application.
     * It extracts the host and port information from runtime arguments and registers the server with the coordinator.
     *
     * @param args The command line arguments.
     * @throws RemoteException if there is an error with remote method invocation.
     */
    public static void main(String[] args) throws RemoteException {
        // Extract the host and port information from runtime arguments.
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        int i = 0;
        int hostPort = Integer.parseInt(arguments.get(i).split("=")[1]);
        String hostName = arguments.get(i + 1).split("=")[1];
        String coordinatorHost = arguments.get(i + 2).split("=")[1];
        int coordinatorPort = Integer.parseInt(arguments.get(i + 3).split("=")[1]);

        // Start the Spring Boot application.
        SpringApplication.run(AppApplication.class, args);

        // Register the server with the coordinator.
        registerServerWithCoordinator(coordinatorHost, coordinatorPort, hostName, hostPort);
    }

    /**
     * Sends a GET request to the coordinator REST API to register the server.
     *
     * @param coordinatorHostName The hostname of the coordinator server.
     * @param coordinatorPort     The port number of the coordinator server.
     * @param hostName            The hostname of the current server.
     * @param port                The port number of the current server.
     */
    private static void registerServerWithCoordinator(String coordinatorHostName, int coordinatorPort, String hostName, int port) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String uri = "http://" + coordinatorHostName + ":" + coordinatorPort + "/api/coordinator/register-server?hostName=" + hostName + "&&port=" + port;
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        serverLogger.info(result.getBody());
        if (result.getStatusCode().is2xxSuccessful()) {
            serverLogger.info("Registered server with coordinator");
        }
    }

}
