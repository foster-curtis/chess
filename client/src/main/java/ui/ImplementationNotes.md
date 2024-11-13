# Implementation Notes:

1. Start by implementing one endpoint in the ServerFacade
2. Next, write functionality for the same endpoint in the UI
    - Use a repl to get and display information, use the client to control the functionality of the repl

    The point of the Server Facade is just to act like a sever, only that it's running on the client side. The Client
    should call the ServerFacade like it is the server, and the ServerFacade will turn those calls into HTTP requests
    to the actual server. It will also deserialize and return the responses from the server.

- There is no correct way to do this, as long as it has the required functionality as far as what options it gives to
  the user and printing them out in a way that is intuitive.