# Lesson 1: Introduction to Java

**Based on:** Lecture 1 (Intro to Java)
**Time needed:** about 45 minutes

## What you will learn

By the end of this lesson you should be able to:

- explain how a Java program is compiled and run, and what the JVM does
- explain the main differences between Java and C
- write, compile and run a basic Java program
- name Java's eight primitive types, and use the right one for a value
- spot and fix the most common beginner compile errors

---

## 1. Why Java needs a "virtual machine"

A CPU only understands **machine code**: very low-level instructions that differ between
CPU types. In C, the compiler turns your `.c` file straight into machine code for *one*
kind of computer:

```
program.c  --(compiler)-->  program.exe  (only runs on that kind of CPU and operating system)
```

That causes two problems:

- different CPUs have different instructions, so the program must be compiled again for each one
- different operating systems provide different libraries (for printing, files, etc.), so a
  program built on macOS won't run on Windows

Java solves this with the **Java Virtual Machine (JVM)**:

```
Hello.java  --(javac)-->  Hello.class (bytecode)  --(java)-->  runs on ANY computer with a JVM
                                                     |
                                     JVM for Windows / JVM for Linux / JVM for macOS
```

- **Bytecode** is your program written for the JVM, not for a real CPU.
- There is a JVM for every CPU and operating system. Each one translates bytecode into that
  computer's own instructions.
- So you compile **once**, and the same `.class` file runs **anywhere**. Java's slogan is
  *"write once, run anywhere"*.

**Analogy:** bytecode is like a recipe written in English. Every kitchen (computer) has its
own chef (JVM) who reads English and cooks it with the local equipment. You only write the
recipe once.

### Compiling and running from the command line

```
javac Hello.java      creates Hello.class (bytecode)
java Hello            runs it: use the CLASS name, without ".class"
```

IntelliJ does both steps for you when you press Run, but you should be able to do it by hand.
The lecture stresses that many CS jobs expect you to be comfortable with command-line tools.

---

## 2. Java vs C

| | C | Java |
|---|---|---|
| Style | Procedural (functions) | **Object-oriented** (classes and objects) |
| Pointers | Yes: `*`, `&`, pointer arithmetic | **No pointers**. Every object variable is a **reference**, and Java follows it for you automatically (*dereferencing*) |
| Memory | You call `malloc` and `free` | **Managed for you**: the JVM allocates memory with `new`, and the **garbage collector** frees objects nothing refers to any more |
| Errors at run time | The program usually just crashes | Run-time errors are **exceptions**, and the program can **recover** from many of them (Lesson 8) |
| Unsigned integers | Yes (`unsigned int`) | **No**. All whole-number types are signed. (`char` is the only unsigned primitive) |

---

## 3. Your first Java program

```java
public class Hello {

    public static void main(String[] args) {
        System.out.println("hello world!");
    }
}
```

Line by line:

| Code | Meaning |
|---|---|
| `public` | This class can be used from anywhere |
| `class Hello` | Declares a class called `Hello`. **Every Java program needs at least one class** |
| `public static void main(String[] args)` | The **main method**: where the program starts. It is always written exactly like this |
| `static` | You can call this method without creating a `Hello` object first (more in Lesson 5) |
| `void` | The method doesn't return anything |
| `String[] args` | An array of Strings (any command-line arguments) |
| `System.out.println(...)` | Prints its argument, then a new line. `System` is a class, `out` is an object inside it, and `println` is a method of `out`. The dots join them together |
| `;` | Ends a statement, just like in C |

**The file name rule:** a `public` class must be saved in a file with **exactly** the same name.
`Hello` must be in `Hello.java`. Java is **case-sensitive**, so `hello.java` is wrong.

### Finding the mistakes

The lecture's "bad" program has three mistakes. Can you spot them before reading on?

```java
public class Hello {

    public static Void main(String[] args) {
        System.out.print1n("hello world!")
    }
}
```

1. `Void` should be `void`: Java is case-sensitive.
2. A semicolon is missing at the end of the `print` line. The compiler tells you the line number.
3. `print1n` uses the number **1** instead of the letter **l**.

The compiler usually reports only one error per line, so **fix errors one at a time and compile
again**. Debugging Java is usually a few rounds of this.

---

## 4. Packages and imports

Java comes with libraries called **packages**: groups of ready-made classes.

| Package | Contains |
|---|---|
| `java.lang` | Core classes like `String`, `Math`, `System`. **Imported automatically** |
| `java.util` | Utilities: `ArrayList`, `Scanner`, `Random` |
| `java.io` | Input and output: files, streams |
| `java.net` | Networking |

```java
import java.util.Scanner;    // import one class (preferred: it's precise)
import java.util.*;          // import every class in java.util
```

Imports go at the **top** of the file, before the class.

If two packages have a class with the same name (`java.util.Date` and `java.sql.Date`),
importing both and writing just `Date` is **ambiguous**, and the compiler will complain.
Write the full name instead: `java.sql.Date d = ...`.

---

## 5. The eight primitive types

Primitives are the basic building blocks. They hold a value directly (they are not objects).

| Type | Holds | Size | Example |
|---|---|---|---|
| `int` | Whole numbers, about ±2.1 billion | 4 bytes | `int age = 20;` |
| `long` | Very large whole numbers | 8 bytes | `long big = 3000000000L;` |
| `short` | Small whole numbers, ±32,767 | 2 bytes | `short s = 1200;` |
| `byte` | Tiny whole numbers, -128 to 127 | 1 byte | `byte b = 100;` |
| `double` | Decimal numbers, about 15 significant digits | 8 bytes | `double price = 4.99;` |
| `float` | Decimal numbers, about 7 significant digits | 4 bytes | `float f = 1.23f;` |
| `char` | One character | 2 bytes | `char grade = 'A';` |
| `boolean` | `true` or `false` | | `boolean done = false;` |

**Rules of thumb:** use `int` for whole numbers, `double` for decimals, `boolean` for
yes/no, and `char` for single characters. Use `long` only when a number won't fit in an `int`.

### Common mistakes with literals (from the lecture)

```java
float c = 1.23;       // WRONG: 1.23 is a double, and a double can't go into a float
float c = 1.23f;      // right: the f makes it a float

int f = 100,000;      // WRONG: no commas in numbers
int f = 100_000;      // right: underscores are allowed for readability

float g = 3 1/2;      // WRONG: no fractions
float g = 3.5f;       // right

long big = 3000000000;    // WRONG: the number is too big to be an int literal
long big = 3000000000L;   // right: the L makes it a long

char ch = "c";        // WRONG: double quotes make a String
char ch = 'c';        // right: single quotes make a char
```

---

## Examples to run

In the [`examples`](examples) folder:

| File | Shows |
|---|---|
| [`Hello.java`](examples/Hello.java) | The first program |
| [`Primitives.java`](examples/Primitives.java) | All eight primitives, their limits, and what happens when an `int` overflows |

To run from a terminal inside the `examples` folder:

```
javac Primitives.java
java Primitives
```

---

## Summary

- Java is compiled to **bytecode** (`javac`), which the **JVM** runs (`java`) on any computer.
- Java has **no pointers** (it uses references), **manages memory** with a garbage collector,
  and can **recover from errors** using exceptions.
- Every program needs a **class** and a **`main` method**. A public class must be in a file
  with the same name.
- There are **eight primitive types**. Watch out for `f` on floats, `L` on big longs, and single
  quotes for `char`.

---

## Quiz

Try each question before opening the answer.

**Q1.** What does the `javac` command produce?

- a) machine code for your CPU
- b) a `.class` file containing bytecode
- c) an `.exe` file
- d) a `.java` file

<details><summary>Answer</summary>

**b)** `javac` compiles Java source code into **bytecode**, stored in a `.class` file. The JVM
then runs that bytecode. This is why the same `.class` file works on any computer that has a JVM.
</details>

**Q2.** You have compiled `Hello.java` successfully. Which command runs it?

- a) `java Hello.java`
- b) `java Hello.class`
- c) `java Hello`
- d) `javac Hello`

<details><summary>Answer</summary>

**c)** `java` takes the **class name**, without any file extension.
</details>

**Q3.** Give two ways Java is different from C.

<details><summary>Answer</summary>

Any two of:
- Java is **object-oriented**.
- Java has **no pointers** (no `*`, `&` or pointer arithmetic): every object variable is a reference.
- Java **manages memory** for you: no `malloc` or `free`, because the garbage collector frees unused objects.
- Java programs can **recover from run-time errors** using exceptions.
- Java has **no unsigned** integer types.
</details>

**Q4.** Which of these lines compile? For each one that doesn't, explain why.

```java
int a = 100,000;
float b = 2.5;
double c = 2.5;
long d = 5000000000L;
char e = "x";
boolean f = true;
```

<details><summary>Answer</summary>

| Line | Compiles? | Why |
|---|---|---|
| `int a = 100,000;` | ❌ | Commas aren't allowed in numbers. Use `100000` or `100_000` |
| `float b = 2.5;` | ❌ | `2.5` is a `double` literal, which could lose precision as a `float`. Use `2.5f` |
| `double c = 2.5;` | ✅ | |
| `long d = 5000000000L;` | ✅ | The `L` makes it a `long` literal |
| `char e = "x";` | ❌ | `"x"` is a `String`. A `char` needs single quotes: `'x'` |
| `boolean f = true;` | ✅ | |
</details>

**Q5.** Your class is called `BankAccount`, but you saved it in `bankaccount.java`. What happens, and why?

<details><summary>Answer</summary>

It doesn't compile. A `public` class must be saved in a file whose name matches the class
name **exactly**, and Java is case-sensitive. The error is:
`class BankAccount is public, should be declared in a file named BankAccount.java`.
</details>

**Q6.** What is the garbage collector?

<details><summary>Answer</summary>

The part of the JVM that automatically **frees the memory** of objects that the program no
longer has any reference to. It's why Java has no `free()` like C. You still need to think a
little about memory: an object can only be freed once nothing refers to it.
</details>

**Q7.** Which primitive type would you use for each of these?
(a) the number of students in a class, (b) a product's price, (c) whether a door is locked,
(d) a student's grade letter, (e) the number of milliseconds since 1970.

<details><summary>Answer</summary>

(a) `int`, (b) `double`, (c) `boolean`, (d) `char`, (e) `long`, because the number is far
bigger than an `int` can hold (`System.currentTimeMillis()` returns a `long`).
</details>

**Q8.** Find all three mistakes:

```java
public class Greeting {
    public static void Main(String[] args) {
        system.out.println("Hi there")
    }
}
```

<details><summary>Answer</summary>

1. `Main` should be `main`. With a capital M it compiles, but Java can't find a `main` method to start the program.
2. `system` should be `System`: it's a class name, and Java is case-sensitive.
3. A semicolon is missing after `println("Hi there")`.
</details>

**Q9.** True or false: Java has an `unsigned int` type, like C.

<details><summary>Answer</summary>

**False.** All of Java's whole-number types (`byte`, `short`, `int`, `long`) are signed.
The only unsigned primitive is `char`. If you need bigger positive numbers, use a bigger
type, such as `long` instead of `int`.
</details>

**Q10.** Which package is imported automatically into every Java program, and name one class in it.

<details><summary>Answer</summary>

`java.lang`. It contains `String`, `Math`, `System`, `Integer` and the other wrapper classes,
among others.
</details>

---

[Back to all lessons](../../README.md) · [Next: Lesson 2, Basic Syntax →](../02-basic-syntax/README.md)
