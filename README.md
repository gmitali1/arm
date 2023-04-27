# Distributed E-Commerce Store Implemented using SpringBoot and Angular.

## Requirements
1. Java 17 or higher
2. npm 16.13.1 or higher


## How to start the application

The execution order of all this project shall be 
1. Start the java coordinator.
2. Start required number of apps (server instances of backend ecommerce store).
3. Start the frontend.

### Steps to run from command line
1. Start the coordinator instance using - java -jar coordinator.jar -Dserver.port=8080 -Dserver.host=localhost
2. Start the required app instance using - java -jar server.jar -Dserver.port=9090 -Dserver.host=localhost -Dcoordinator.host=localhost -Dcoordinatorport=8080
   *Note : The server port numbers (8080 and 9090 in this case) can be replaced by any other port numbers*
3. Start up the frontend
   1. Navigate to the /src/main/js directory
   2. Run 'npm install' ton install all dependencies
   3. After installing all dependencies, start the frontend app using 'npm start'
   4. Navigate to http://localhost:4200/signup to begin using the app.

## How to navigate the application
1. Navigate to http://localhost:4200/signup and create a user account.
2. After signing up, navigate to http://localhost:4200/ to see a list of available products
3. Add the required amount of products to your card, click checkout.
4. After checking out, click pay, to create an order.
5. Navigate to http://localhost:4200/orders to check all your previous orders.
