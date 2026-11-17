# Object-Oriented Programming in Java

A step-by-step course in Java and object-oriented programming, built from the lectures of
**COMP20350 Object-Oriented Programming** at UCD.

Each lesson explains one lecture in plain language, with runnable code examples, common mistakes,
and a **quiz at the end** to check your understanding.

## How to use this course

1. **Go in order.** Each lesson builds on the ones before it.
2. **Read the lesson**, then **run the examples** in its `examples` folder. Change them and see what happens.
3. **Do the quiz** at the end without looking back. Try each question first, then open the answer.
4. If you get a question wrong, re-read that section of the lesson before moving on.

## Lessons

| # | Lesson | Lecture | Main topics | Done? |
|---|---|---|---|---|
| 1 | [Introduction to Java](lessons/01-intro-to-java/README.md) | 1 | The JVM and bytecode, Java vs C, your first program, primitive types | ☐ |
| 2 | [Basic Java Syntax](lessons/02-basic-syntax/README.md) | 2 | Operators, Strings, naming, expression types, wrapper classes, `if`, `switch`, loops, arrays, syntax errors | ☐ |
| 3 | [Classes, Objects and Methods](lessons/03-classes-and-objects/README.md) | 3 | Classes vs objects, constructors, methods, `this`, references, `null` | ☐ |
| 4 | [OOP in Practice and Unit Testing](lessons/04-oop-and-unit-testing/README.md) | 4 | Enums, why OOP, debugging, JUnit, choosing test cases, class diagrams, cohesion | ☐ |
| 5 | [Encapsulation and `static`](lessons/05-encapsulation/README.md) | 5 | Passing values, access levels, good class design, data patterns, static variables and methods, constants, singletons | ☐ |
| 6 | [Interfaces and Polymorphism](lessons/06-interfaces/README.md) | 6 | `ArrayList`, interfaces, polymorphism, casting and `instanceof`, default and static methods, mock objects | ☐ |
| 7 | [Inheritance, Abstract Classes and `final`](lessons/07-inheritance/README.md) | 7 | `extends`, `Object`, overriding vs overloading, `super`, `toString`/`equals`, abstract classes, `final` | ☐ |
| 8 | [File IO and Exceptions](lessons/08-file-io-and-exceptions/README.md) | 8 | Streams, `File`, reading and writing text files, keyboard input, checked vs unchecked exceptions, best practices | ☐ |
| 9 | [Lambda Expressions and the Stream API](lessons/09-lambdas-and-streams/README.md) | 9 | Anonymous inner classes, functional interfaces, lambdas, arrow `switch`, sorting, method references, streams | ☐ |
| 10 | [Threading and Concurrency](lessons/10-threading/README.md) | 10 | Threads vs `Runnable`, lifecycle, `start`/`join`/`sleep`, race conditions, `synchronized`, parallel computation | ☐ |

Tick off each lesson as you finish it by editing this file (change `☐` to `✅`).

## Running the examples

Every lesson has an `examples` folder with complete programs. Each file's comment at the top says how to run it.

**From a terminal**, inside a lesson's `examples` folder:

```
javac *.java
java NameOfTheDemoClass
```

**In IntelliJ:** create a new Java project, copy one lesson's example files into its `src` folder,
then click the green ▶ next to the class with the `main` method. (Lesson 8's `FileDemo` reads
`grades.csv`, so put that file in the **project folder**, not in `src`.)

The JUnit tests in Lesson 4 (`examples/test/StudentTest.java`) need JUnit 5: in IntelliJ, click the
red `junit` import, press **Alt + Enter**, and choose **Add 'JUnit5.x' to classpath**.

All the examples were compiled and run with Java 21.

## Quick revision: the big ideas

| Idea | One-line summary | Lesson |
|---|---|---|
| Class vs object | A class is a blueprint; an object is one thing built from it | 3 |
| Reference | An object variable holds the object's address, not the object | 3 |
| Encapsulation | Keep fields `private`; change them only through methods that validate | 5 |
| `static` | Belongs to the class, not to any one object | 5 |
| Interface | A list of methods a class promises to have ("can-do") | 6 |
| Polymorphism | The same method call runs different code depending on the object's real class | 6, 7 |
| Inheritance | A subclass "is a" superclass, and reuses its code with `extends` | 7 |
| Overriding vs overloading | Same signature in a subclass vs same name with different parameters | 7 |
| Abstract class | Can't be created; shares code and leaves some methods for subclasses | 7 |
| Checked exception | The compiler forces you to catch it or declare it with `throws` | 8 |
| Lambda | A short way to write the one method of a functional interface | 9 |
| Race condition | Threads changing shared data at the same time; fixed with `synchronized` | 10 |
