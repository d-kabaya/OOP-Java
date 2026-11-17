# Lesson 8: File IO and Exceptions

**Based on:** Lecture 8 (File IO and Exceptions)
**Time needed:** about 1 hour 30 minutes

## What you will learn

By the end of this lesson you should be able to:

- explain what a **stream** is, and the difference between byte and character streams
- use the `File` class, and fix "file not found" problems caused by paths
- read a text file with `BufferedReader` and with `Scanner`
- write a text file with `PrintWriter`, and explain why `close()` matters
- read and validate keyboard input
- explain **checked**, **unchecked** and **error** types of exception
- throw, catch and declare exceptions, write your own, and use `finally`
- follow the best practices: *throw early, catch late* and *don't squelch*

---

## 1. Streams

Java treats **all** input and output the same way: as a **stream**.

**Analogy:** a stream is a **water pipe**. Data flows through it one piece at a time, in **one direction**.

- An **input stream** brings data **into** your program, from a file, the keyboard or a website.
- An **output stream** sends data **out**, to a file or the screen.

Once you can read from one source, the others work almost the same way. Only the source of the pipe changes.

| | Works with | Input | Output |
|---|---|---|---|
| **Byte streams** | raw bytes: images, binary data | `InputStream` | `OutputStream` |
| **Character streams** | text | `Reader` | `Writer` |

These four are abstract classes (Lesson 7). You use their subclasses. For text files, you mostly need:

- **reading:** `BufferedReader` or `Scanner`
- **writing:** `PrintWriter`
- **the file itself:** `File`

---

## 2. The `File` class

A `File` object is just a **path**: a label saying where a file is. Creating one doesn't open,
create or read anything.

```java
import java.io.File;

File f = new File("grades.csv");
f.exists();              // is there really a file there?
f.getAbsolutePath();     // the FULL path Java is using: great for debugging
f.getName();             // just "grades.csv"
f.isFile(); f.isDirectory();
f.delete(); f.mkdir(); f.createNewFile();    // return true if they worked
f.listFiles();           // the files inside a folder
```

### Paths and "file not found"

- Windows uses `\` (`C:\Users\dylan\file.txt`); Mac and Linux use `/`. In a Java String, a backslash
  must be doubled: `"C:\\Users\\dylan\\file.txt"`. `File.separator` gives the right one automatically.
- A **relative path** like `"grades.csv"` is looked up in the **working directory**. In IntelliJ,
  that's the **project folder**, **not** `src`. If your file is in `src`, use `"src/grades.csv"`, or
  move the file to the project folder.

If you get "file not found", print `f.getAbsolutePath()` and `f.exists()` to see exactly where Java is looking.

---

## 3. Reading a file with `BufferedReader`

```java
import java.io.*;

try {
    BufferedReader in = new BufferedReader(new FileReader(new File("grades.csv")));
    String line;
    while ((line = in.readLine()) != null) {
        System.out.println(line);
    }
    in.close();
} catch (IOException ex) {
    ex.printStackTrace();
}
```

- `new FileReader(f)` opens the file as a character stream: the pipe connected to the file.
- `new BufferedReader(...)` **wraps** it. It reads big chunks at once (faster), and gives you `readLine()`.
- `while ((line = in.readLine()) != null)` does three things. Read it from the inside out:
  `readLine()` reads the next line; `line = ...` stores it; `!= null` checks it got one.
  At the end of the file, `readLine()` returns `null`, so the loop stops.
- `close()` gives the file back to the operating system.

**Wrapping** streams is like joining pipe pieces, each adding a feature:

```
file on disk → FileReader (characters) → BufferedReader (fast, whole lines) → your code
```

Tracing it for a file with two lines, `Simon,60` and `Mark,75`:

| Pass | `readLine()` returns | `!= null`? | Prints |
|---|---|---|---|
| 1 | `"Simon,60"` | true | `Simon,60` |
| 2 | `"Mark,75"` | true | `Mark,75` |
| 3 | `null` | **false** | loop ends |

### Splitting a CSV line

```java
String[] parts = line.split(",");            // "Simon,60" → ["Simon", "60"]
String name = parts[0].trim();
int mark = Integer.parseInt(parts[1].trim());   // NumberFormatException if not a number
```

---

## 4. Reading with `Scanner`

`Scanner` reads **tokens**: pieces of text separated by whitespace.

```java
import java.util.Scanner;

Scanner in = new Scanner(new File("data.txt"));   // can take a File directly
while (in.hasNext()) {
    System.out.println(in.next());
}
in.close();
```

| Method | Reads |
|---|---|
| `next()` | the next word, as a `String` |
| `nextLine()` | the rest of the current line |
| `nextInt()` / `nextDouble()` | the next token, converted to a number |
| `hasNext()` / `hasNextInt()` | checks what's next **without using it up** |

You must read in the **same order** as the data. For the line `100 B+ 88.6 Joe Bloggs`:

```java
int assignment = in.nextInt();      // 100
String letter = in.next();          // B+
double exam = in.nextDouble();      // 88.6
String name = in.nextLine();        // " Joe Bloggs": the rest of the line
```

Calling `nextInt()` when the next token isn't a number throws an `InputMismatchException`.

| | `BufferedReader` | `Scanner` |
|---|---|---|
| Reads | whole lines | tokens or lines |
| Converts to numbers? | no: use `Integer.parseInt` | yes: `nextInt()`, `nextDouble()` |
| Speed | faster | slower |
| Good for | line-based files such as CSVs | mixed data, keyboard input |

---

## 5. Writing a file with `PrintWriter`

```java
try {
    PrintWriter out = new PrintWriter(new FileOutputStream(new File("passed.csv")));
    for (String s : lines) {
        out.println(s);
    }
    out.close();
} catch (IOException ex) {
    ex.printStackTrace();
}
```

- `new FileOutputStream(f)` **creates** the file, or **wipes** it if it exists. To add to the end
  instead, use `new FileOutputStream(f, true)`.
- `PrintWriter` gives you the familiar `print` and `println`, exactly like `System.out`.

### Why `close()` matters so much when writing

`PrintWriter` keeps text in a **buffer** (a waiting area in memory) and writes it to disk in
batches, because disk writes are slow. `close()` **flushes** the buffer, forcing everything waiting
to be written. **Forget `close()` and the file can end up empty or half-written**, even though the
code "worked". (`flush()` pushes the buffer out without closing.)

---

## 6. Keyboard input and validation

Reading from the keyboard is the same idea, with a different source: `System.in`.

```java
Scanner fromFile = new Scanner(new File("data.txt"));
Scanner fromKeyboard = new Scanner(System.in);
```

Users can type anything, so validate. The lecture's pattern uses **two loops**: one for the wrong
**type**, one for the wrong **range**:

```java
Scanner in = new Scanner(System.in);
System.out.println("Enter a grade from 0 to 100:");
int grade = -1;                                   // start invalid, so the outer loop runs

while (grade < 0 || grade > 100) {                // wrong RANGE
    while (!in.hasNextInt()) {                    // wrong TYPE
        String bad = in.next();                   // throw away the bad token
        System.out.println("You entered <" + bad + ">, try again");
    }
    grade = in.nextInt();                         // safe now
    if (grade < 0 || grade > 100) {
        System.out.println("The value must be between 0 and 100, try again");
    }
}
```

Without `in.next()` removing the bad token, `hasNextInt()` keeps seeing it, and you get an
**infinite loop**. And always print a **prompt**, so the program doesn't look frozen.

---

## 7. Exceptions

An **exception** is an object that represents an error, **thrown** when something goes wrong while
the program is running. If nothing catches it, the program stops and prints a **stack trace**.

### Three kinds

Lecture 8's key question: **whose fault is it?**

| Kind | Extends | Cause | Must you handle it? | Examples |
|---|---|---|---|---|
| **Error** | `Error` | the JVM itself is in trouble | No: you can't recover | `OutOfMemoryError` |
| **Unchecked** | `RuntimeException` | **a bug in your code** | No: fix the code instead | `IllegalArgumentException`, `NullPointerException`, `ArrayIndexOutOfBoundsException`, `NumberFormatException`, `InputMismatchException` |
| **Checked** | `Exception` (not `RuntimeException`) | something **outside your control** | **Yes**: the compiler forces you | `IOException`, `FileNotFoundException`, `InterruptedException` |

```
                    Throwable
                   /         \
               Error          Exception                   ← checked
                             /         \
                     IOException     RuntimeException     ← unchecked
                          |               |
           FileNotFoundException    IllegalArgumentException, NullPointerException, ...
```

**Analogy:** a checked exception is like an airline making you buy travel insurance: the flight
might be cancelled through no fault of yours, so you must have a plan. An unchecked exception is
like tripping over your own laces: nobody makes you insure against that; you tie your laces.

### Throwing an exception

When **you** detect a problem, throw an exception. The method **stops immediately**, like a
circuit breaker.

```java
public void setMark(int mark) {
    if (mark < 0 || mark > 100) {
        throw new IllegalArgumentException("Mark must be 0 to 100: " + mark);
    }
    this.mark = mark;
}
```

`throw` (no "s") goes inside the method, followed by `new`, then the exception class and a message.

### Handling a checked exception: catch it or declare it

This doesn't compile:

```java
public static void read(File f) {
    BufferedReader in = new BufferedReader(new FileReader(f));   // FileNotFoundException!
}
```
```
error: unreported exception FileNotFoundException; must be caught or declared to be thrown
```

**Option 1: catch it**, if this method can sensibly deal with the problem:

```java
try {
    BufferedReader in = new BufferedReader(new FileReader(f));
    ...
} catch (FileNotFoundException e) {
    System.out.println("No file at " + f.getAbsolutePath());
}
```

**Option 2: declare it with `throws`**, so the **caller** must deal with it:

```java
public static void read(File f) throws IOException { ... }
```

| | `throw` | `throws` |
|---|---|---|
| Where | inside a method body | in the method header |
| Does what | actually throws an exception, now | warns callers the method *might* throw it |
| Required for checked exceptions? | to throw one | **yes** |

### Several `catch` blocks

Java checks `catch` blocks **top to bottom** and uses the first match. Put the **most specific
first**: `FileNotFoundException` is a subclass of `IOException`, which is a subclass of `Exception`.

```java
try {
    ...
} catch (FileNotFoundException e) {     // most specific
    ...
} catch (IOException e) {
    ...
} catch (Exception e) {                 // most general: last
    ...
}
```

The other way round doesn't compile: the specific `catch` could never be reached.

`e.getMessage()` gives the message the exception was created with.

### `finally`: code that always runs

```java
PrintWriter out = new PrintWriter(fileName);
try {
    writeData(out);
} finally {
    out.close();      // runs whether writeData worked or threw an exception
}
```

Use `finally` for **clean-up** such as closing files, so an exception halfway through doesn't leave
a file open.

You may also see **try-with-resources**, `try (BufferedReader in = new BufferedReader(...)) { ... }`,
which closes the file for you automatically. The lecture doesn't cover it, but it's correct and common.

### Your own exception classes

Extend an existing exception, and pass the message up with `super`:

```java
public class InsufficientFundsException extends RuntimeException {   // unchecked
    public InsufficientFundsException(String message) {
        super(message);
    }
}
```

Extend `RuntimeException` for a programmer's mistake (unchecked), or `Exception` for something outside
the program's control (checked, so it needs `throws`).

---

## 8. Best practices (from the lecture)

1. **Throw early, catch late.** Throw as soon as you detect a problem. Only **catch** it where you
   can do something useful about it; otherwise let it pass up to the caller with `throws`. As the
   lecture says, this *"seems lazy, [but] it's actually really good practice"*.
2. **Don't squelch exceptions.** Never write an empty `catch` just to make code compile. The error
   vanishes silently, and debugging becomes a nightmare.
   ```java
   } catch (Exception ex) {
       // worry about this later      ← don't
   }
   ```
3. **Throw specific exceptions, with precise messages.** `"Mark must be 0 to 100: 105"` is far more
   useful than `"error"`.
4. **Keep `try`/`catch`/`finally` simple.** An inner `try`/`finally` inside an outer `try`/`catch`
   is clearer than mixing everything in one block.

### Applying it: reading a file with bad lines

A common task (your Abalone lab and the Car quiz) is reading a CSV where some lines are invalid.
The pattern: **catch around each line**, so one bad line is reported and skipped, and the rest are still read.

```java
while ((line = in.readLine()) != null) {
    try {
        String[] v = line.split(",");
        list.add(new Student(v[0].trim(), Integer.parseInt(v[1].trim())));
    } catch (IllegalArgumentException e) {      // also catches NumberFormatException (a subclass)
        System.out.println("Skipping invalid line \"" + line + "\": " + e.getMessage());
    }
}
```

---

## Examples to run

| File | Shows |
|---|---|
| [`grades.csv`](examples/grades.csv) | A sample file with some invalid lines |
| [`Student.java`](examples/Student.java) | A class whose constructor validates and throws |
| [`FileDemo.java`](examples/FileDemo.java) | `File`, reading with `BufferedReader` and `Scanner`, skipping bad lines, writing with `PrintWriter`, `finally`, a missing file |
| [`InsufficientFundsException.java`](examples/InsufficientFundsException.java), [`Wallet.java`](examples/Wallet.java), [`ExceptionDemo.java`](examples/ExceptionDemo.java) | A custom exception, `throw`, catching in order, `getMessage()` |
| [`KeyboardInput.java`](examples/KeyboardInput.java) | The two-loop keyboard validation (interactive: you type the input) |

Run from inside the `examples` folder, so `grades.csv` is found:
`javac *.java`, then `java FileDemo`, `java ExceptionDemo` or `java KeyboardInput`.

---

## Summary

- Java does all IO through **streams**. For text: `BufferedReader`/`Scanner` to read, `PrintWriter` to write.
- `File` is just a path. Relative paths start from the **working directory** (the project folder in IntelliJ).
- `readLine()` returns `null` at the end of the file. **Always `close()`**, especially when writing.
- Validate keyboard input with `hasNextInt()`, and throw away bad tokens with `next()`.
- **Checked** exceptions (outside your control) must be caught or declared with `throws`.
  **Unchecked** ones (bugs) don't have to be.
- `throw new X("message")` throws. `catch` the most specific first. `finally` always runs.
- **Throw early, catch late. Don't squelch.**

---

## Quiz

**Q1.** What does `readLine()` return when there are no more lines?

<details><summary>Answer</summary>

`null`. That's why the loop condition is `(line = in.readLine()) != null`.
</details>

**Q2.** Your program writes 10 lines with a `PrintWriter`, but the file is empty. What's the most likely cause?

<details><summary>Answer</summary>

You forgot to call `close()` (or `flush()`). `PrintWriter` buffers its output, and `close()` is what
forces the buffered text to be written to the file.
</details>

**Q3.** Your file `data.csv` is in IntelliJ's `src` folder, but `new File("data.csv")` gives
"file not found". Why, and give two fixes.

<details><summary>Answer</summary>

Relative paths are looked up in the **working directory**, which is the project folder, not `src`.
Fixes: use `new File("src/data.csv")`, or move the file into the project folder. Printing
`f.getAbsolutePath()` shows exactly where Java is looking.
</details>

**Q4.** Checked or unchecked? (a) `FileNotFoundException` (b) `NullPointerException`
(c) `NumberFormatException` (d) `IOException` (e) `IllegalArgumentException`

<details><summary>Answer</summary>

Checked: (a) and (d). Unchecked: (b), (c) and (e). `NumberFormatException` is a subclass of
`IllegalArgumentException`, which extends `RuntimeException`.
</details>

**Q5.** Why doesn't this compile, and give two ways to fix it?

```java
public static void show(File f) {
    Scanner in = new Scanner(f);
    System.out.println(in.nextLine());
}
```

<details><summary>Answer</summary>

`new Scanner(f)` can throw `FileNotFoundException`, a **checked** exception, which must be caught or
declared. Either wrap the code in `try { ... } catch (FileNotFoundException e) { ... }`, or add
`throws FileNotFoundException` to the method header.
</details>

**Q6.** What's wrong here?

```java
try {
    ...
} catch (Exception e) {
    System.out.println("Something went wrong");
} catch (IOException e) {
    System.out.println("File problem");
}
```

<details><summary>Answer</summary>

`IOException` is a subclass of `Exception`, so the first `catch` already catches every `IOException`.
The second `catch` can never be reached, and Java gives a compile error. Put the **more specific**
`catch` first.
</details>

**Q7.** What's the difference between `throw` and `throws`?

<details><summary>Answer</summary>

`throw` goes **inside a method** and actually throws an exception: `throw new IllegalArgumentException("...")`.
`throws` goes in the **method header** and declares that the method might throw an exception, passing the
responsibility to the caller: `public void read() throws IOException`.
</details>

**Q8.** In the keyboard validation loop, what happens if you remove `in.next()` from the inner loop and the user types `abc`?

<details><summary>Answer</summary>

An **infinite loop**. `hasNextInt()` looks at the next token without removing it, so it keeps seeing
`abc` and returning `false` forever. `in.next()` is what throws the bad token away.
</details>

**Q9.** What does "squelching" an exception mean, and why is it bad?

<details><summary>Answer</summary>

Catching an exception and doing nothing with it (an empty `catch` block), usually just to make the code
compile. The error disappears silently, the program carries on in a broken state, and there's no clue
about what went wrong, which makes debugging very hard.
</details>

**Q10.** What does "throw early, catch late" mean?

<details><summary>Answer</summary>

**Throw** an exception as soon as you detect a problem, with a specific type and a clear message.
Only **catch** it at the point where the program can actually do something useful about it, which is
often further up, in the caller. Methods in between just declare it with `throws`.
</details>

**Q11.** When does the code in a `finally` block run?

<details><summary>Answer</summary>

**Always**: whether the `try` block finishes normally or throws an exception. That makes it the right
place for clean-up such as closing a file.
</details>

**Q12.** A CSV has 5 lines and 2 are invalid. Your `read` method should return 3 objects and print 2
error messages. Where should the `try`/`catch` go, and why?

<details><summary>Answer</summary>

**Inside** the loop, around the processing of each line. Then a bad line throws, the `catch` prints a
message, and the loop carries on with the next line. If the `try`/`catch` were around the **whole**
loop, the first bad line would end the loop, and the remaining lines would never be read.
</details>

**Q13.** How would you write a custom **checked** exception called `GradeException`?

<details><summary>Answer</summary>

```java
public class GradeException extends Exception {
    public GradeException(String message) {
        super(message);
    }
}
```
Extending `Exception` (not `RuntimeException`) makes it checked, so any method that throws it must
declare `throws GradeException`.
</details>

---

[← Previous: Lesson 7](../07-inheritance/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 9, Lambdas and Streams →](../09-lambdas-and-streams/README.md)
