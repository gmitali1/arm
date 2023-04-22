package com.arm.coordinator.service;

import com.arm.coordinator.model.Coordinator;
import com.arm.coordinator.model.CoordinatorInterface;
import org.springframework.stereotype.Service;
import java.rmi.RemoteException;
@Service
public class CoordinatorService {
    public void addAcceptor(String hostName, int port) throws RemoteException {
        CoordinatorInterface coordinatorInterface = new Coordinator();
        coordinatorInterface.addAcceptor(hostName,port);
    }
}
