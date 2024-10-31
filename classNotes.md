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

  
 





