# 240 Notes, Fall 2024

## Classes, Sep 12
  - Every class has a default constructor that takes no parameters and does nothing
  - The Special signature entry point for a class looks like: `public static void main(String[] args) {}`
  - The `this` command gives you a pointer directly to the object you are within
  - You can have as many different constructors as you want!
  - Different types of constructors:
      - Default
      - Parameterized
      - Copy (takes in another instance of that class, and usually calls the parameterized constructor)
  
  - When you are calling constructors, you must begin with the keyword `new`
  - By convention, classes are always capitalized (including enums)

  ### Enumerations
  - Similar to a class
  - Helps avoid string typo errors
  - Restricts inputs to a set
  - Look up what `OBJ.class` does/means
   
  - Hide details that don't have to be shared! It makes it easier to change your mind later.
  - Getters restrict direct access
  - Setters enforce constraints on how changes are made
  - Make sure your information remains safe by only returning copies to the user (especially private variables)

  ### The Object Class
   - All other objects inherit from this class
   - Has the following methods: (Items after a `:` are the return value)
      - toString() : string  ]
      - equals : boolean     ]  Commonly Overridden
      - hashCode() : int     ]
      - clone() : Object  --Creates Copies
      - wait()    }  Both help with synchronization
      - notify()  }
  - Include `@Override` if you are going to override built-in functions
  - Ways to check equals:
    - If they are identical (have the same memory address), return True
    - If check is null, or if the two objects have different classes, return False
    - If neither of those pass, cast the current object class to the object, and compare the values and return a boolean.
    - `==` compares memory addresses directly. Default implementation of `.equals()` method is also a memory compare. ***CHANGE IT!***
    #### Type Casting:
      - `Example name = (Example) o;` Where `o` is of class `Object` and `Example` is a class
  ### Hashcodes
  - Override hashcode to return a prime number multiplied by the sum of the values of your string
  ### Records
  - record ClassName(param1, param2, param3) {}
  - *Do more research on this!*

  ## For Chess Project:
  - Overrride toString, hashCode, and equals, on:
    - Game
    - Board
    - Move
    - Piece
    - Position
   
## Abstraction, September 17th

### Good Software Design
1. Think about the customer today (make sure it works)
2. Think about the customer tomorrow (extensible into the future)

### Why We Use Abstraction:
- Abstraction allows for flexibility and reuse
- It's like LEGO!
- Polymorphism: "many forms"
    - You are representing your object/design so it can have many forms
    - We present different forms depending on the context. Not all parts of the object are presented in every context

#### Interfaces
- *Just a definition* of something that I can interact with. **Not a concrete object**
Ex:
```
public interface List<E> {
    void add(int index, E element);
    E remove(int index);
    int size();
    void clear();
    ListIterator<E> listIterator();
}
```
- Notice the semicolons--there is *no* implementation!

### Inheritance
- Not just implementing a definition, you are gaining functionality from the parent
- Syntax: `public static class SubClass extends SuperClass {}`
- just as `this` points to current class, `super` calls parent class

### Abstract Classes
- Inheritance and Interfaces all in one!
- `public static abstract class X extends Interface`
- You must implement the methods from the interface, and then you can add new elements with tag `abstract` that are **NOT** implemented

### instanceOf
- An operator (just like `=`) that returns a boolean.
  - True if Inheriting Classes match
  - False if not
- Create a new variable and typecast using syntax: `item instanceOf Class classItem` If item is an instance of the class `Class`, it will create a new variable of name `classItem` and typecast `Class`

### Shallow vs. Deep Copying
- Deep copy creates a new object with new data
- Shallow copy creates a new object that points to the same data. When one object is changed, it then effects the values of the other object.

## Chess Rules: Applying Abstraction!
- Make Chess Piece class an interface and make a new class for each piece type?
- Create a seperate class for rules and make subclasses inherit from it with the rules for each piece

## Exceptions and Collections

Package: java.util
Interfaces: List, Map, Set, Iterattor
Implementations: ArrayList, HashMap, HashSet

Comparable interface allows you to compare objects (<, >, =) more than just equals

USE THE UTILS!

### Exceptions
- Pushing up the handling of errors
- Jumps back up through the call stack and returns an error. It must be _caught_ somewhere along the line or the program will crash!
Basic syntax:
```
try {
  // Code that might throw an exception
} catch (FileNotFoundException) {
  // Specific Exceptions
} catch (Exception e) {
  // General Exception
} finally {
  // Code HERE after the catch statements will still be executed, regardless of whether or not an esception is thrown!
}
```
- Functions that can throw an exception must document that they can throw an exception. Functions that call a function that trows an exception must also document this. Ex:
`public void func() throw Exception`
- Functions that catch an exception do not need this documentation.
- You can have a block with just `try` and `finally` that will not handle exceptions but will still run the code in finally before it continues to throw the exception up.
  - This is especially useful for file handlers! Close files on your way up the stack
- **tryWithResources block shortens the syntax for `try` with `finally` block**

#### Exceptions Must Be Exceptional!!
-Don't use exceptions as flow of execution logic!!

# Good Design Principles!
## Domain Driven Design
  - Domain = problem we are trying to solve
  - Stop thinking about the hardware, and start thinking about the domain
  - Think about what the customer wants in the real world and represent that in your software

  - **Questions to ask yourself:**
      - Who are the *actors* in the system?
      - What tasks do they want to accomplish?
      - What are the *objects* that the actors use?
      - What are the *interactions* between that actors and objects?
  
  ### Object Oriented Design
  - Model real world objects in code
  - Don't try to represent every part of your object, remember who the actors are, what they want to accomplish, and what they will need to interact with and how. Add properties and methods to reflect that.
  - Don't make things that are transitive (like age) properties. Instead have them as methods and calculate them each time it is needed. (Calculate age based on the current date and the person's birthday

  #### Properties to consider:
  - Is-A relationship: Inheritance, an aspect of what something is (A Programmer is a Person)
  - Has-A relationship: Ecapsulation, Classes as properties of classes (A Programmer has a Computer)
  - Uses-A relationship: Transient association. This is often connected to an operation (A Person uses a Car to travel)

#### Design Goal: Simplicity
##### Tools: Decomposition, KISS, YAGNI, DRY
  - **K**eep **I**t **S**imple **S**marty: Do everything as simply as possible first, then expland it if necessary as you go
  - **Y**ou **A**ren't **G**onna **N**eed **I**t: Don't add functionality for the future if you're not certain you are going to use it
  - **D**on't **R**epeat **Y**ourself: Don't copy code from one function to the other! Find patterns and overlap and use classes and functions to make it work

#### Design Goal: High Cohesion, Low Coupling
- High Cohesion: Within the object, everything is connected well (Everything that goes with the kitchen is in the kitchen. Don't put a toilet in your kitchen)
- Low Coupling: Between objects, there is little connection. Have connector objects to connect things when necessary but don't connect the objects directly (Kitchen object and bathroom object are connected by plumbing object, but kitchen does not know about bathroom)

Uncle Bob S.O.L.I.D:
**Single Responsibility** - an actor only has one reason to use you
**Open Closed** - Open for extention, Closed to modification
**Liskov Substitution** - If you extend an interface, implement it!
**Interface Segregation** - Make my interfaces as cohesive as possible (Single Responsibility!)
**Dependency Inversion** - Expose your details as part of the top level. Push information up to the high level and pass it in to the lower level. This makes extensibility much easier

#### P.O.L.A: The Principle of Least Astonishment
- Make your code easy to understand so others who read it understand what you are doing
- How do we move a piece on the Chess Board? *Add a MovePiece() method to your ChessBoard class!*

## Inner Classes, Lambdas
- Class within a class. You still have one top-level class per file, but you can hide other classes within another class
- Static Inner Classes, Local Inner Classes, Anonymous Inner Classes, Inner Classes

- Static Inner Class:
  - Can be created independent of it's outer class (becuase of `static` keyword)
  
- Inner Class:
  - Now that there is no static, the inner class can see all other information about the outer class. It has access to the parent `.this` pointer! (`OuterClass.this`)
  
- Local Inner Class:
  - Used within a function
  - Knows all the information (Local Variables, Function Parameters etc.) within the scope of the function that it was called. If returned out of the function, it retains all of that information!

- Closure: Close around the surrounding creation state
- Factory Pattern:
  - **Look up videos on the factory pattern because that was very confusing**
  - Implements the principle of closure. Allows you to retain information that was only in the scope of a function and carry it with a class to the rest of the program
 
- Anonymous Classes:
  - Forget about the syntax of actually declaring a class
  - Used when you're implementing an interface. Implement the methods right in line with the call to the interface constructor. NEVER do this if you're implementing a large interface!

- Lambda Functions:
  -Works **ONLY** with a Functional Interface:
    -An interface that only defines **one** Method
  - Used to shorten a Functional Interface
  - Use cases: When a function takes as a parameter a class that implements a funcitonal interface
  - Syntax: `functionBeingCalled((parameters) -> returnValue))`
    - Ex: `(a,b) -> {if (a > b) {return a + b};}`
  - Lambda knows the interface type and the return type of the single method of the functional interface. This makes it so that you don't have to include that information in the function call!
 
## I/O, Generics, and Serialization

### I/O Streams
- Has read and write methods depending on whether its an input or an output
- The concept of a stream is actually based on a river: its first in, first out
- Reader/Writer classes alow us to read/write whole streams of words/characters rather than just bytes
  - I/O layers:
    -`read()` and `write()` -> one byte at a time
    -`reader()` and `writer()` -> one line at a time
    -`Scanner()` -> whole files at a time

### ArrayList and Generics
- Historically, ArrayLists could hold any type, but Generics are ArrayLists that can only hold items of a specific type.
  ```
  class Storage<T> {
    List<T> items = new ArrayList<>();

    void add(T item) {
      items.add(item);
    }
  }

  // Now any type can be passed in!:

  var intStorage = new Storage<Integer>();
  var stringStorage = new Storage<String>();
  ```

  ### Serialization
  - Makes an object able to be copied to another system that doesn't have accesss to your device's memory
  - Commonly uses a "Simplified JavaScript Object"
  - That's how we send object info across from client -> server -> client!
    ```
    new serializer = new Gson();

    new json = serializer.toJson(obj);

    var objFromJson = serializer.fromJson(json, map.class)
    ```

  ## Phase 2
  - Makse sure you thoughroughly read the project 3 specifications so you understand what you are designing in phase two!
  - Must have UserData, AuthData, and GameData
  
## HTTP and Web API
- HTTP is  a client to server protocall, meaning the client has to request something from the server and the server will send something back.
- Two things you need to make a connection: domain and port #
- URL = Uniform resource locator
  - Scheme (https)
  - Domain (server name)
  - Port
  - Path (path to the resource I'm asking for)
  - Parameters (restricts how the resources is returned)
  - Anchor tag (information for client rendering)
- For our chess servers:
  - `http://localhost:8080/user`
  - Sheme = `http`
  - domain = `localhost`
  - port = `8080`
  - path = `user`
- Encodings for cURL `-X` = change method, `-H` = change header, `-d` = change data

### Requests
  - Method, Path, and version
  - Headers (Headers describe what types of data the client is expecting back, where is is coming from, etc)
  - Body
  - METHODS:
    - `GET`, `POST`, `PUT`, `DELETE`, (Theres more but I missed tham
   
### Response
  - Version, Status code, Status Message
  - Headers (can include connection status, content type, content encoding)
  - Body

  - Status codes:
    - `2xx`: Success
    - `3xx`: Successful, but I'm not giving what you asked for (redirect)
    - `4xx`: Client made an error in the request
    - `5xx`: Server errors

### Web API
- Use Java spark to deserialize and parse HTTP requests, and to serialie and send it back

## Code Quality Goals
- I can understand it today
- I can understand it tomorrow
- I can understand it quickly
- I can enhance it tomorrow

#### How do I do this?
- Use conventions - Naming, team, language
- Choose simplicity over clever or concise
- Clarity over verbosity
- Decomposition, abstraction, encapsulation

#### Conventions:
- Naming
  - If names are not enough for clarity, add documentation
    - This is not ideal though. Then you have written the function twice--once in human language and once in code. When you change one, you have to change the other. Longer comments also discourage reading
  - Naming rules for Java:
    - Object names are nouns
    - Method names are verbs
    - Classes begin with uppercase
    - Methods begin with lowercase
    - Variables begin with lowercase
    - Package names begin with lowercase
    - Constants are all uppercase
    - CamelCase should be used for all names
  - Use self-documenting code with naming
  - `computeAndPrint()` -> breaks cohesion: does two things
  - reduce code duplication
  - reduce nested statements!
 
- Parameters:
  - Use symbols -> use an Enum instead of a string, bool, or int
  - Reduce parameters -> pass configuration objects, use setters, or have valid defaults, rather than long parameter lists
  - be consistent -> use consistent ordering (e.g. input params followed by output params)
  - single return -> only have one place in your code that returns


## Test Driven Development (TDD)
- Go back and forth between the test and the code. Write a test for a very small part of your program. Then, go write the code. Run the tests, and then repeat the process.
- Ensure your tests follow good design principles!
  - Cohesion: Only does one thing
  - Quick to execute: For example, if your code would usually communicate over a network and take time, call into it directly just to test hte functionality
  - Do not repeat: don't test things twice!
  - Stable: If it passes once, it should pass every time!
  - Automated: One keystroke to run them

  ### JUnit
  - Include the `@Test` tag to tell the compiler that the funciton is a test
  - The `@Disabeled` tag disables the test so it doesn't run, but it still lists it when all tests are run (so you don't forget to finish it!)
  - `@ParameterizedTest` allows you to run the same test multiple times with different inputs/implementations **Use these for DataAccess Tests**

  ### Code Coverage
  - Make sure your tests test every part fo your code! Don't leave anything untested.
  - You can `Run tests with coverage` and get data on what lines of your code were tested and how much of it works
  - **THIS IS SUPER COOL! USE IT**
 

## Relational Databases
- Like a simple 2D table
  - There are row relationships and column relationships
  - Column relationships = fields
  - Row relationships = objects
- Seperate tables can also be related if they have similar column headers
- Primary keys are in the first column of a table, giving it's row a specific ID
- Secondary keys are references in one table to a primary key of a different table to map information from one table to the other
- Key Characteristics:
  - Unique -> No duplicates
  - Stable -> Doesn't change
  - Simple -> Avoid composites
- Use UUID for a db key for users and maybe games? It meets all of the criteria, and has some useful implementation effects.
  -> or not, because SQL has built in functionality to assign keys and return them

## JDBC
What is it?
  - Java code connecting over the network to a database
  - Pull it down from Maven to connect it to your project
  - Three steps:
    1. Get a connection
    2. Prepare a statement
    3. Run
  - Try blocks w/ no catch statement is a "try with resources" block. This will call the `close()` method on all open resources when the block is finished.
  - Use `prepareStatement(vars)` with question marks to keep data safe. Then use `preparedStatement.setInt(index, var)`, remembering that indexes are 1-based and the `setInt` function can be replaced with a setter for any type.
  ### During Startup
  - `CREATE DATABASE IF NOT EXISTS chess`
  - `CREATE TABLE IF NOT EXISTS games ( x... )`
  - Look at petShop code `MySqlDataAccess`. It's got some really useful ideas!

  ### Securing Passwords
  - Bring in mindrot bcrypt from Maven
  - Generate Salt? haha
  - The TA's will run a test to make sure you are serializing your passwords! So do it.


## Preparing for Phase 5
### CLI: Command Line Interface
- Mostly just call a command and get a response
- REPL = Read, Evaluate, Print, Loop -> A continuously runnning program in the console that updates based on compute state or user interaction
  - **This is what we will do for chess!**
- Look at the Tic Tac Toe example to get an idea of how we will do this for chess
### Chess phase 5: PreGame
- Everything up until you would start the game
- Typing help would print out possible commands based on the player state (logged in or not, playing a game or not)


## Logging and Debugging

#### What is Logging?
- Logging is an external record of application execution of your users
  
#### Benefits of Logging
- Allows for efficient debugging: when a customer has a problem, you can go look at the log to see what they did and what went wrong
- Security: Keep records of important actions --> is someone putting in 10,000 passwords?
- Auditing
- Performance Monitoring: Health of your system, data for request times
  
#### Desirable Logging Characteristics
- Persistent: Data remains somewhere (whether in the console or somewhere else) so you can go find specific moments or calls
- Immutable: Can't be changed--Read only
- Aggregated: Should collect all the data from all parts of your service into one place
- Accessible: Combine and sort data. You should be able ot find the information you're looking for quickly!
- Performant: Speedy fast and optimized for your environment

Log Levels:
  - OFF
  - SEVERE -> Take care of it immediately!
  - WARNING -> Database latency increasing, Nearly out of storage
  - INFO -> Depreated code being used, user logged in, etc.
  - FINE----\
  - FINER----)--> These are strange and not often used. If anything, only the FINE will be used to report function parameters or similar info.
  - FINEST--/
  - ALL

#### DEBUGGING
- First step!! Make sure that the expected output should acutally be the expected output! Check if the customer was actually right
- Step Two: Recreate the problem/the user's input
- Write Code to test your Code!!! You're a programmer! `@Test`
- Give updates to the cusotmer
 
### Chess Websocket Connections
- Make your connection when a user joins a game, not when they login
- Websocket is not a request, response protocol! You must intentionally send messages both ways.
- Don't call your websocket events from the Server, call them in the service--its business logic
- An observer gets passed in to deeper layers of your server so that when a message comes back from the server, it knows where to send it back to.

## Security and Hashing
- Authentication: Who am I?
- Authorization: What can I do?
- Data Integrity: Non-Counterfeit
- Non-Repudiation: Cannot contest ownership

### Cryptographic Hashing:
- Desired Hash Characteristics:
  - Fixed-Size -> Always the same size
  - Deterministic -> Same in, same out
  - One-Way -> Can't get the original text
  - Resistance to collisions -> One to one mapping
  - Preimage resistance -> can't guess
 
- Hash functions
  - Bcrypt
  - SHA-256

## Encryption:
- Encryption -> makes things unreadable
- Decryption -> makes things readable again


# Concurrency
- How do I implement concurrency?
    - Java `Thread` class
      - Override `run` and call the `start()` method
    - Provide `runnable` using lambdas. `new Thread(() -> runnable code).start();`
    - The Thread `.join()` method blocks the main thread until the other thread returns. The data from the other thread is then available in the main thread
    - Using `.submit()` uses the `Callable` class, which allows for a return value, unlike `run`.
- Most effecient when you run multiple programs parallel concurrent
- There is always one "main" thread, so when you create a thread you are making the second one, not the first. All threads will be running at the same time.
- Task Submitter --> Executor Service ( Task Queue --> Thread Pool )

Critical Resources:
- A critical resource is anything that can be read from/written to at the same time, and you have to protect it!!
- Have a Critical Section of your code to manage critical resources

Synchronization:
- keyword `synchronized`
- Put it on function declarations or around critical sections of your code which will only allow one thread access to the critical resource at a time.
- Atomic methods allow us to be thread safe without using synchronization, which makes the runtime shorter. Atomic methods are methods that are only one single instruction at the CPU level.

Classes:
- Atomic Integer
- Atomic Boolean
- Blocking Queues (Array, List, others)
- Concurrent HashMap

You can create 'atomic commands' for a database by cancelling autoCommit and committing several operations as a single operation. The database will essentially create a queue and pull in one operation at a time and do the next one when it is done.

# Maven
### Command Line builds
Installation: maven.apache.org/download.cgi
It's also already installed with IntelliJ, so you can just go to the IntelliJ file that has Maven and access it from there.
Maven files are pom files written in xml

Project Structure:
Must have the following directory structure: src -> main -> java -> Main.java (src -> test -> java -> MainTest.java for tests)
Will not compile otherwise!

### Terms and Definitions
Lifecycle - collection of phases in the programming and distribution process
phase
code

Phases:
- clean : delete the target directory
- compile : compile the target file
- test : run the tests in the target directory
- package : check that the previous phases are done and then package the target directory for distribution

### Jar Files
- Byte code representations of all your classes

Professor Jensen Teaches 329

QCon by InfoQ
- Very unbiased, focuses on all emerging trends from different parts of the industry
4 Hot takeways
- MLOps "Machine Learning Operations" is hot market
- Using AI for Software Engineering
- Soft Skills for delivering software -> Curiosity, Creativity, Christlike, Collaborative (Work well with others)
- Rust is hot, but mostly has a lot of hype and isn't going to take over

- 85% of all AI projects fail!
- Common problems:
  - Tackling the wrong problem
  - Acquiring data to train models
  - Turning model into a product
  - Works offline but not online
  - Unseen non-technical obstacles

Most important parts of a successful team:
- Trust -> not a competition
- Autonomy -> everyone can perform their job
- Purpose -> more than just coding or getting a paycheck
- Psychological Safety -> feel comfortable asking questions, expressing concerns, proposing alternitives

"The fastest API call is one that doesn't happen"

Tell the AI what you want to accomplish, and have it ask you how you want it to be done and share the trade offs and options

https://architechtelevator.com/book
Architects create ideas that they can share with people of all levels and responsibilities

"most likely you will not be competing with AI for your job, but you will be competing with people who know how to use AI to do your job better"
