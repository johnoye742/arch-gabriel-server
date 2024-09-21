
# ArchGabriel Server
Named after the messenger, this project is an open source self hostable alternative to Pusher. It allows you to implement messaging in your web applications by just starting the server. To get the JavaScript client to this, it can be found here [here](https://github.com/johnoye742/arch-gabriel-client) 

## Installation
Running the server would require Maven as it is written in Spring Boot and compiled in maven (a better installation method will be available soon). Use the following command to start the server. First go inside the directory containing the code
```bash
./mvnw spring-boot:run
```

This will run the server at port 8080.

## Usage
When you run the server it can then be used via the Client, like the example below.

```javascript
// you could also just import it in your html instead
import Client from "../client-path"

// Instantiates the client and connects it to localhost:8080and connects the user to a room called secureRoom
const client = new Client("localhost:8080", "secureRoom")

// Starts the connection and listens for incoming messages on "secureRoom"
client.start(function(data) {
    // logs the content of the data coming in
    console.log(data.content)
})

client.send("Hey!")

    
```

## Contribution
Since ArchGabriel Server is technically a Spring microservice, you can submit your PRs on this repo and I'll review them, just be sure to use tabs instead of spaces in your code.
