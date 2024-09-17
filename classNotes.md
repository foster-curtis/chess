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

  ## For Chess:
  - Overrride toString, hashCode, adn equals, on:
    - Game
    - Board
    - Move
    - Piece
    - Position






