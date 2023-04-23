package com.arm.ecommerce;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
@EntityScan(basePackages = "com.arm.ecommerce")
@EnableJpaRepositories("com.arm.ecommerce")
public class AppApplication {

    private static final Logger serverLogger = Logger.getLogger(AppApplication.class.getSimpleName());

    public static void main(String[] args) throws RemoteException {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        int i = 0;
        int hostPort = Integer.parseInt(arguments.get(i).split("=")[1]);
        String hostName = arguments.get(i + 1).split("=")[1];
        String coordinatorHost = arguments.get(i + 2).split("=")[1];
        int coordinatorPort = Integer.parseInt(arguments.get(i + 3).split("=")[1]);
        SpringApplication.run(AppApplication.class, args);
        registerServerWithCoordinator(coordinatorHost, coordinatorPort, hostName, hostPort);
    }

    private static void registerServerWithCoordinator(String coordinatorHostName, int coordinatorPort, String hostName, int port) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String uri = "http://" +coordinatorHostName+":"+coordinatorPort+"/api/coordinator/register-server?hostName="+hostName+"&&port="+port;
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        serverLogger.info(result.getBody());
        if (result.getStatusCode().is2xxSuccessful()) {
            serverLogger.info("Registered server with coordinator");
        }
    }

    @Bean
    CommandLineRunner runner(ProductService productService) {
        return args -> {
            productService.save(new Product(1L, "TV Set", 300.00, "http://placehold.it/200x100"));
            productService.save(new Product(2L, "Game Console", 200.00, "http://placehold.it/200x100"));
            productService.save(new Product(3L, "Sofa", 100.00, "http://placehold.it/200x100"));
            productService.save(new Product(4L, "Ice-cream", 5.00, "http://placehold.it/200x100"));
            productService.save(new Product(5L, "Beer", 3.00, "http://placehold.it/200x100"));
            productService.save(new Product(6L, "Phone", 500.00, "http://placehold.it/200x100"));
            productService.save(new Product(7L, "Watch", 30.00, "http://placehold.it/200x100"));
        };
    }

}
