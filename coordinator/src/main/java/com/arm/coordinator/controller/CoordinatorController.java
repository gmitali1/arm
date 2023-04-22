package com.arm.coordinator.controller;
import com.arm.coordinator.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.RemoteException;

@RestController
@RequestMapping("/api")
public class CoordinatorController {

    @Autowired
    CoordinatorService coordinatorService;

    @GetMapping("/register-server")
    @ResponseStatus(HttpStatus.OK)
    private void addAcceptor(@RequestParam("hostName") String hostName, @RequestParam("port") int port)
            throws RemoteException {
        coordinatorService.addAcceptor(hostName, port);
    }
}
