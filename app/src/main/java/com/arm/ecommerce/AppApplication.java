package com.arm.ecommerce;

import com.arm.ecommerce.model.Product;
import com.arm.ecommerce.service.ProductService;
import com.arm.ecommerce.model.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.rmi.RemoteException;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = "com.arm.ecommerce")
@EnableJpaRepositories("com.arm.ecommerce")
public class AppApplication {

    public static void main(String[] args) throws RemoteException {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        String hostPort = arguments.get(0).split("=")[1];
        String hostName = arguments.get(1).split("=")[1];
        String coordinatorHost = arguments.get(2).split("=")[1];
        String coordinatorPort = arguments.get(3).split("=")[1];
        Server server = new Server(hostName,Integer.parseInt(hostPort), coordinatorHost,Integer.parseInt(coordinatorPort),
                0.05, 0.05, 0.05);
        server.registerServerWithCoordinator();
        SpringApplication.run(AppApplication.class, args);
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
