# Lesson 2: Basic Java Syntax

**Based on:** Lecture 2 (Basic Java Syntax)
**Time needed:** about 1 hour

## What you will learn

By the end of this lesson you should be able to:

- use `boolean` values and the operators `&&`, `||` and `!`
- work with `String`s: joining them, and using their methods
- name variables correctly, and work out the type of an expression
- use the wrapper classes (`Integer`, `Double`, ...) to convert text to numbers
- write `if`, `switch`, `while` and `for` statements
- create and loop over arrays, and know when to use an `ArrayList` instead
- read and fix the most common syntax errors

---

## 1. Booleans and operators

A `boolean` holds only `true` or `false`. Every comparison gives a `boolean`:

```java
int x = 60;
boolean inRange = x > 45 && x < 100;    // true
```

| Operator | Meaning | `true` when... |
|---|---|---|
| `a && b` | AND | **both** are true |
| `a \|\| b` | OR | **at least one** is true |
| `!a` | NOT | `a` is false |

### Number operators

`+`, `-`, `*`, `/` work as you'd expect, with one big trap:

```java
int a = 7 / 2;          // 3: int divided by int throws away the decimal part
double b = 7 / 2;       // 3.0: still int division! The .5 is lost BEFORE it's stored
double c = 7.0 / 2;     // 3.5: one side is a double, so the division keeps decimals
int r = 7 % 2;          // 1: % is the remainder ("modulus")
```

---

## 2. Strings

`String` is a **class**, not a primitive, so it has a capital S and comes with methods.

```java
String name = "Simon";
String greeting = "hello " + "world";    // + joins Strings together ("concatenation")
```

**Strings never change.** `+` and methods like `toUpperCase()` create a **new** String and
leave the original alone:

```java
String s = "hi";
s.toUpperCase();              // creates "HI"... and throws it away
System.out.println(s);        // hi
s = s.toUpperCase();          // store the new String to keep it
System.out.println(s);        // HI
```

### Joining Strings and numbers: read left to right

```java
int age = 20;
System.out.println("John will be " + age + 1 + " years old.");     // John will be 201 years old.
System.out.println("John will be " + (age + 1) + " years old.");   // John will be 21 years old.
```

Java works **left to right**. `"John will be " + age` is already a String, so `+ 1` just
sticks a "1" on the end. Brackets force the maths to happen first.

### Useful String methods

| Method | Returns | Example with `s = "Simon"` |
|---|---|---|
| `length()` | number of characters | `5` |
| `isEmpty()` | `true` if the length is 0 | `false` |
| `charAt(i)` | the character at index `i` | `s.charAt(0)` is `'S'` |
| `indexOf(str)` | position of the first match, or **-1** if not found | `s.indexOf("mon")` is `2` |
| `substring(start, end)` | the part from `start` up to but **not including** `end` | `s.substring(1, 3)` is `"im"` |
| `toUpperCase()` / `toLowerCase()` | a new String in upper / lower case | `"SIMON"` |
| `trim()` | a new String without spaces at either end | |
| `equals(other)` | `true` if the text is the same | |
| `split(",")` | an array of the pieces between commas | |

Indexes start at **0**:

```
 S  i  m  o  n
 0  1  2  3  4
```

**Compare Strings with `.equals()`, never `==`.** `==` checks whether two variables refer to
the *same object*, not whether the text is the same. It sometimes appears to work, which makes
this bug hard to spot.

```java
if (answer.equals("yes")) { ... }    // correct
if (answer == "yes") { ... }         // bug
```

### Example: parsing a String (from the lecture)

Turn `"lincoln, abraham"` into `"Abraham Lincoln"`:

```java
String s = "lincoln, abraham";
int comma = s.indexOf(',');                          // 7
String last = s.substring(0, comma).trim();          // "lincoln"
String first = s.substring(comma + 1).trim();        // "abraham"

first = first.substring(0, 1).toUpperCase() + first.substring(1);   // "Abraham"
last = last.substring(0, 1).toUpperCase() + last.substring(1);      // "Lincoln"
System.out.println(first + " " + last);              // Abraham Lincoln
```

### `toString()`

Every object has a `toString()` method that turns it into a String. `println` calls it
automatically. When you write your own classes, always give them a meaningful `toString()`:
it makes debugging much easier.

---

## 3. Names (identifiers)

**Rules (the compiler enforces these):** names can contain letters, digits and `_`; they can't
**start** with a digit; no spaces or other symbols; they can't be a reserved word like `class`
or `int`; and they're **case-sensitive** (`age` and `Age` are different).

**Conventions (everyone follows these):**

| Kind | Style | Example |
|---|---|---|
| Variables and methods | start lowercase, camelCase | `boxWidth`, `getName()` |
| Classes | start Uppercase | `BankAccount` |
| Constants | ALL_CAPS with underscores | `MAX_SIZE` |

Names should be **descriptive**: `studentCount`, not `sc` or `x`.

---

## 4. Statements, expressions and types

- A **statement** *does* something and usually ends with `;`: `int n = 7;`
- An **expression** *gives a value*: `n + 2`, `x < 100`, `"Hi " + name`

Every expression has a **type**. The rules:
- If everything is the same type, that's the type: `500 / 10` is an `int`.
- If there's a mix of number types, the result is the **bigger** type: `12 + 6.7` is a `double`, and `5 * 4.5f` is a `float`.
- If there's a String anywhere in a `+`, the result is a `String`.
- Comparisons and `&&`, `||`, `!` give a `boolean`.

### Converting between number types

Converting to a **bigger** type happens automatically (`int` to `long` or `double`). Converting to a
**smaller** type needs a **cast**, because information might be lost:

```java
double d = 9.99;
int i = (int) d;      // 9: the cast chops off the decimal part (it doesn't round)
```

---

## 5. Wrapper classes

Each primitive has a matching **wrapper class**, which is an object version of it:

| Primitive | Wrapper |
|---|---|
| `int` | `Integer` |
| `double` | `Double` |
| `float` | `Float` |
| `long` | `Long` |
| `boolean` | `Boolean` |
| `char` | `Character` |
| `short`, `byte` | `Short`, `Byte` |

You need them for two reasons.

**1. Collections like `ArrayList` can only hold objects.** Java converts automatically
(**autoboxing** and **unboxing**):

```java
Integer boxed = 42;      // autoboxing: int to Integer
int plain = boxed;       // unboxing: Integer to int
```

**2. Their static methods convert text to numbers.** You call these on the class name, with no
object needed:

```java
int n = Integer.parseInt("42");
double d = Double.parseDouble("3.75");
// Integer.parseInt("abc") throws a NumberFormatException
```

---

## 6. Making decisions

### `if`, `else if`, `else`

```java
if (mark >= 70) {
    System.out.println("First");
} else if (mark >= 40) {
    System.out.println("Pass");
} else {
    System.out.println("Fail");
}
```

The condition **must** be a `boolean`. Unlike C, `if (x)` with an `int` won't compile.

### `switch`

`switch` picks a `case` to **start** from, and then runs everything after it **until a `break`**:

```java
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    default:
        System.out.println("Another day");
}
```

**Forgetting `break` is a classic bug.** Without it, Java "falls through" and runs the next
case's code too. You can switch on an `int`, a `String`, a `char` or an `enum`.

*Newer Java (covered in Lesson 9) also has an arrow form, `case 1 -> ...`, which never falls through.*

---

## 7. Loops

```java
// while: repeats while the condition is true
int count = 0;
while (count < 3) {
    System.out.println("count is " + count);
    count++;
}

// for: start; condition; step
for (int i = 0; i < 5; i++) {
    System.out.println("Hi");
}

// for-each: visits every item of an array or collection
for (String name : names) {
    System.out.println(name);
}
```

Use `for` when you know how many times to loop, `while` when you loop until something
happens, and for-each when you just want every item and don't need the index.

---

## 8. Arrays

An array holds a **fixed number** of values of one type.

```java
int[] numbers = new int[5];          // 5 slots, all starting at 0
numbers[0] = 123;                    // indexes go from 0 to length - 1
System.out.println(numbers.length);  // 5: length is a field, so there are no brackets

int[] primes = {2, 3, 5, 7};         // create and fill in one step

for (int i = 0; i < primes.length; i++) {
    System.out.println(primes[i]);
}
```

**Arrays can't grow.** Once created, the size is fixed. Using an index outside `0` to
`length - 1` throws an `ArrayIndexOutOfBoundsException`.

### When the size isn't known: `ArrayList`

`ArrayList` (in `java.util`) grows and shrinks as you add and remove items. It's covered
properly in Lesson 6, but here is the basic idea:

```java
import java.util.ArrayList;

ArrayList<Integer> list = new ArrayList<Integer>();   // must use Integer, not int
list.add(5);
list.add(8);
System.out.println(list.size());     // 2: size() is a method, so it has brackets
System.out.println(list.get(0));     // 5
System.out.println(list);            // [5, 8]
```

| | Array | `ArrayList` |
|---|---|---|
| Size | Fixed | Grows and shrinks |
| Holds | Primitives or objects | Objects only (use `Integer`, not `int`) |
| Read / write | `a[i]`, `a[i] = x` | `a.get(i)`, `a.set(i, x)` |
| Size | `a.length` | `a.size()` |
| Printing | prints a memory address | prints `[5, 8]` |

---

## 9. Reading syntax errors

The compiler tells you the **file**, **line number** and a description. The common ones:

| Error message | Usual cause |
|---|---|
| `class X is public, should be declared in a file named X.java` | The file name doesn't match the class name |
| `';' expected` | Missing semicolon at the end of the line it points to (or the line before) |
| `cannot find symbol` | A misspelled variable or method, a variable used outside the block it was declared in, or a missing `import` |
| `class, interface, or enum expected` | A misspelled keyword (e.g. `Class` instead of `class`) or an extra `}` |
| `incompatible types` | Putting a value into a variable of the wrong type, e.g. a `double` into an `int` |

**Fix one error at a time, then compile again.** One mistake often causes several messages.

---

## Examples to run

| File | Shows |
|---|---|
| [`StringsAndOperators.java`](examples/StringsAndOperators.java) | Integer division, String methods, the Lincoln parsing example, concatenation traps |
| [`ControlFlow.java`](examples/ControlFlow.java) | `if`/`else`, `switch` with and without `break`, all three loops, arrays, `ArrayList`, `parseInt` |

---

## Summary

- `&&`, `||` and `!` combine booleans. `int / int` throws away the decimal part.
- `String` is a class: it never changes, has useful methods, and must be compared with `.equals()`.
- Wrapper classes (`Integer`, `Double`, ...) let primitives act as objects, and convert text with `parseInt` / `parseDouble`.
- `switch` falls through without `break`.
- Arrays have a **fixed** size (`.length`). `ArrayList` grows (`.size()`).

---

## Quiz

**Q1.** What does each line print?

```java
System.out.println(7 / 2);
System.out.println(7.0 / 2);
System.out.println(7 % 3);
double d = 7 / 2;
System.out.println(d);
```

<details><summary>Answer</summary>

```
3
3.5
1
3.0
```
`7 / 2` is int division, so the `.5` is thrown away. In the last one, the division happens
**before** the result is stored in the `double`, so it's still `3`, which then prints as `3.0`.
</details>

**Q2.** What does this print?

```java
int age = 20;
System.out.println("Next year: " + age + 1);
```

<details><summary>Answer</summary>

`Next year: 201`. Java works left to right: `"Next year: " + age` is already the String
`"Next year: 20"`, so `+ 1` adds the character "1". Write `(age + 1)` to get `21`.
</details>

**Q3.** What is the type of each expression?
(a) `12 + 6.7`  (b) `500 / 10`  (c) `"Cost: " + 1 + 10`  (d) `100 < 56`  (e) `5 * 4.5f`

<details><summary>Answer</summary>

(a) `double`  (b) `int`  (c) `String` (and its value is `"Cost: 110"`)  (d) `boolean`  (e) `float`
</details>

**Q4.** What do these print, if `s = "Programming"`?

```java
System.out.println(s.length());
System.out.println(s.charAt(3));
System.out.println(s.indexOf("gram"));
System.out.println(s.substring(0, 4));
System.out.println(s.indexOf("z"));
```

<details><summary>Answer</summary>

```
11
g
3
Prog
-1
```
`indexOf` returns **-1** when the text isn't found. `substring(0, 4)` includes index 0 but
**not** index 4.
</details>

**Q5.** Why should you write `name.equals("Ann")` instead of `name == "Ann"`?

<details><summary>Answer</summary>

`==` checks whether both sides refer to the **same object** in memory. `.equals()` checks
whether the **text** is the same. Two different String objects can hold the same text, and
`==` would then give `false`.
</details>

**Q6.** Which of these are **valid** variable names? Which follow the **conventions**?
`totalMarks`, `2ndPlace`, `student_count`, `class`, `MaxScore`, `my name`

<details><summary>Answer</summary>

- Valid and conventional: `totalMarks`.
- Valid but not conventional: `student_count` (should be camelCase: `studentCount`), and
  `MaxScore` (a variable should start lowercase; uppercase is for classes).
- **Invalid:** `2ndPlace` (starts with a digit), `class` (reserved word), `my name` (contains a space).
</details>

**Q7.** What does this print when `day` is `1`?

```java
switch (day) {
    case 1:
        System.out.println("Mon");
    case 2:
        System.out.println("Tue");
        break;
    case 3:
        System.out.println("Wed");
}
```

<details><summary>Answer</summary>

```
Mon
Tue
```
There's no `break` after case 1, so execution **falls through** into case 2, then stops at
its `break`.
</details>

**Q8.** What's wrong with this loop?

```java
int[] marks = {72, 38, 40};
for (int i = 0; i <= marks.length; i++) {
    System.out.println(marks[i]);
}
```

<details><summary>Answer</summary>

The condition should be `i < marks.length`, not `<=`. The array has indexes 0, 1 and 2, so
when `i` reaches 3 it throws an `ArrayIndexOutOfBoundsException`. This is called an
**off-by-one** error.
</details>

**Q9.** How do you convert the String `"2024"` into an `int`? What happens with `"20x4"`?

<details><summary>Answer</summary>

`int year = Integer.parseInt("2024");`. With `"20x4"`, `parseInt` throws a
`NumberFormatException`, because the text isn't a valid whole number.
</details>

**Q10.** Why does `ArrayList<int>` not compile, and what should you write instead?

<details><summary>Answer</summary>

An `ArrayList` can only hold **objects**, and `int` is a primitive. Use the wrapper class:
`ArrayList<Integer>`. Java then converts between `int` and `Integer` automatically (autoboxing).
</details>

**Q11.** What does this print?

```java
String s = "hello";
s.toUpperCase();
System.out.println(s);
```

<details><summary>Answer</summary>

`hello`. Strings never change. `toUpperCase()` **returns a new String**, but the result isn't
stored anywhere. To keep it, write `s = s.toUpperCase();`.
</details>

**Q12.** What is the value of `n`?

```java
int n = (int) 9.99;
```

<details><summary>Answer</summary>

`9`. Casting a `double` to an `int` **chops off** the decimal part. It does not round.
</details>

---

[← Previous: Lesson 1](../01-intro-to-java/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 3, Classes and Objects →](../03-classes-and-objects/README.md)
