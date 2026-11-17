# Lesson 3: Classes, Objects and Methods

**Based on:** Lecture 3 (Intro to Objects, Classes and Methods)
**Time needed:** about 1 hour

## What you will learn

By the end of this lesson you should be able to:

- explain the difference between a **class** and an **object**
- write a class with instance variables, constructors and methods
- create objects with `new` and call their methods
- use `this` to refer to the current object
- explain what a **reference** is, and why copying an object variable doesn't copy the object
- explain what `null` is, and what causes a `NullPointerException`

---

## 1. Why objects?

Large programs are hard to write in one go. Object-oriented programming (OOP) breaks them
into **classes**, and each class into small **methods**.

The lecture quotes Steve Jobs: **objects are like people**. If you hand a *laundry object*
your dirty clothes and say "please get these cleaned", you get clean clothes back. You have
no idea which laundry it used, how it got there or how it paid, and you don't need to. All
that complexity is **hidden inside** the object, and you interact with it through a simple
request.

That's the core idea of OOP: each object **knows things** (its data) and **can do things**
(its methods), and other code just asks it to do them.

A good class contains **related** functionality only. A `Laundry` class can clean clothes and
accept money; it shouldn't also book flights. This is called **cohesion** (more in Lesson 4).

---

## 2. Classes vs objects

| Class | Object |
|---|---|
| The **idea** or **blueprint** | One **real thing** built from the blueprint |
| Written once, in your code | Created as many times as you like, with `new` |
| Says *which* data and methods every object has | Holds its **own values** for that data |
| e.g. the idea of a "Chair": something with legs and a back | e.g. the chair you are sitting on |

**Analogy:** a class is a cookie cutter, and objects are the cookies. One cutter, many cookies.
Every cookie has the same shape (the same instance variables and methods), but each can have
different icing (different values).

`"Hello"` and `"Goodbye"` are two different objects of the same class, `String`. They have the
same methods (`length()`, `toUpperCase()`, ...) but store different text.

---

## 3. Writing a class

The lecture builds a `Date` class:

```java
public class Date {

    // ---- Instance variables: the data EVERY Date object has ----
    private int day;
    private int month;
    private int year;

    // ---- Constructor: runs when a new Date is created ----
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // ---- Methods: what a Date can do ----
    public void incrementYear() {
        year++;
    }

    public boolean sameDate(Date d) {
        return this.day == d.day && this.month == d.month && this.year == d.year;
    }

    public String toString() {
        return day + "/" + month + "/" + year;
    }
}
```

### Instance variables

Declared **inside the class but outside any method**. Every object gets its **own copy**.
Make them `private`, so only the class's own methods can change them (Lesson 5 explains why).

### Constructors

A constructor sets up a new object. It looks like a method, but:
- it has the **same name as the class**
- it has **no return type**, not even `void`
- it runs **once**, when you write `new`

```java
Date today = new Date(17, 11, 2026);
```

Why write a constructor? Because it can **force** users of the class to give the information
the object needs. A `Date` without a day, month and year makes no sense, so the constructor
requires all three.

**The default constructor:** if you don't write any constructor, Java gives you an invisible
one with no parameters. **As soon as you write your own, that default one disappears.** So
with the `Date` class above, `new Date()` no longer compiles.

You can write **several constructors** with different parameters (this is called
*overloading*):

```java
public Date(int year) {
    this(1, 1, year);    // calls the 3-parameter constructor: 1st January of that year
}
```

### Methods

To design a method, answer three questions (the lecture's approach):

| Question | For `incrementYear` | For `sameDate` |
|---|---|---|
| What does it return? | nothing: `void` | yes or no: `boolean` |
| What's its name? | `incrementYear` | `sameDate` |
| What does it need to do its job? | nothing | another `Date` to compare with |

That gives you the **method signature**, and then you fill in the body.

---

## 4. Using objects

```java
Date birthday = new Date(3, 5, 2006);    // create an object
birthday.incrementYear();                // call a method: objectName.methodName(parameters)
System.out.println(birthday);            // 3/5/2007 (println calls toString)
```

In C you'd call a function directly: `max(1, 2)`. In OOP, methods belong to a class, so you
need an **object** to call them on.

Each object keeps its own values:

```java
Date a = new Date(1, 1, 2000);
Date b = new Date(25, 12, 2026);
a.incrementYear();          // only a changes
// a is 1/1/2001, b is still 25/12/2026
```

---

## 5. The keyword `this`

Inside a method, `this` means **"the object this method was called on"**. The lecture calls it
the **implicit parameter**: it is always there, even when you don't write it.

### Use 1: telling a field apart from a parameter with the same name

```java
public Date(int day, int month, int year) {
    this.day = day;      // this.day = the object's field;  day = the parameter
}
```

Without `this`, `day = day;` assigns the parameter to itself, and the field never changes. It
compiles without any error, which makes it an easy bug to miss.

### Use 2: comparing "my" data with another object's data

```java
public boolean sameDate(Date d) {
    return this.day == d.day && this.month == d.month && this.year == d.year;
}
```

Think of comparing birthdays with the person beside you, called `d`. You know your own day
(`this.day`) and you ask for theirs (`d.day`).

```java
if (today.sameDate(birthday)) { ... }   // inside sameDate, this = today, d = birthday
```

`private` fields of **another object of the same class** are accessible too (`d.day`), because
`private` means "private to the class", not "private to the object".

When there is no name clash, you can leave `this.` out: in `incrementYear`, `year++` means
`this.year++`.

---

## 6. References: what an object variable really holds

Java has no pointers, but it has **references**. When you write `new Date(...)`, the object is
created somewhere in memory, and `new` gives back a **reference**: its address. The variable
stores that address, **not the object itself**.

### Copying a primitive copies the value

```java
int x = 120;
int y = x;        // y gets its own copy of 120
y++;              // y is 121, x is still 120
```

### Copying an object variable copies the reference, not the object

```java
Date t = new Date(1, 1, 2000);
Date b = t;               // b now points to the SAME object as t
b.incrementYear();
System.out.println(t);    // 1/1/2001: t changed too!
```

```
 t ───┐
      ├──►  [ Date object: 1/1/2001 ]      there is only ONE object
 b ───┘
```

To get a separate copy, you must build a **new object**:

```java
Date copy = new Date(1, 1, 2000);   // a second, independent object
```

The same thing explains why `==` on objects compares **addresses**: `t == b` is `true` (same
object), but two different `Date` objects holding the same date give `false`. To compare
contents, write a method like `sameDate` (or `equals`, Lesson 7).

### `null`

A reference variable that doesn't point to any object holds the special value `null`. Calling
a method on it crashes the program with a **`NullPointerException`**:

```java
Date d = null;
d.incrementYear();     // NullPointerException: there's no object to increment
```

---

## 7. Packages and the Java API

Classes are organised into **packages** (folders of related classes), such as `java.util`.
The **Java API documentation** lists every class in the standard library, with its
constructors and methods. When you want to know what a class can do (for example, `String`),
look it up there: <https://docs.oracle.com/en/java/javase/21/docs/api/>

---

## Examples to run

| File | Shows |
|---|---|
| [`Date.java`](examples/Date.java) | The lecture's `Date` class, with two constructors, `incrementYear`, `sameDate` and `toString` |
| [`DateDemo.java`](examples/DateDemo.java) | Creating objects, calling methods, `this`, copying references, and `null` |

Compile both together: `javac Date.java DateDemo.java`, then `java DateDemo`.

---

## Summary

- A **class** is a blueprint; an **object** is one instance of it, with its own values.
- A class has **instance variables** (data), **constructors** (set-up) and **methods** (behaviour).
- Writing your own constructor **removes** the default one.
- `this` is the current object. Use `this.x = x` when a parameter has the same name as a field.
- Object variables hold **references**. Copying the variable gives two names for **one** object.
- Using a `null` reference causes a `NullPointerException`.

---

## Quiz

**Q1.** In one sentence each: what is a class, and what is an object?

<details><summary>Answer</summary>

A **class** is a blueprint that defines what data (instance variables) and behaviour (methods)
its objects will have. An **object** is one actual instance created from that class with `new`,
holding its own values for that data.
</details>

**Q2.** What's wrong with this constructor?

```java
public class Book {
    private String title;

    public void Book(String title) {
        this.title = title;
    }
}
```

<details><summary>Answer</summary>

A constructor has **no return type**. Because of `void`, Java treats `Book(...)` as an ordinary
method that happens to have the class's name, not as a constructor. So `new Book("Dune")` won't
compile, since the only constructor is the default one, which takes no parameters.
Remove `void`.
</details>

**Q3.** What is printed?

```java
public class Counter {
    private int count;

    public Counter(int count) {
        count = count;
    }

    public int getCount() {
        return count;
    }
}
// in main:
Counter c = new Counter(5);
System.out.println(c.getCount());
```

<details><summary>Answer</summary>

`0`. In the constructor, `count = count;` assigns the **parameter** to itself, because the
parameter hides the field. The field keeps its default value of 0. The fix is `this.count = count;`.
</details>

**Q4.** What is printed?

```java
Date a = new Date(1, 1, 2000);
Date b = a;
b.incrementYear();
System.out.println(a);
System.out.println(a == b);
```

<details><summary>Answer</summary>

```
1/1/2001
true
```
`Date b = a;` copies the **reference**, so `a` and `b` point to the **same** object. Changing it
through `b` changes what `a` sees, and `a == b` is `true` because both hold the same address.
</details>

**Q5.** And this?

```java
int x = 10;
int y = x;
y++;
System.out.println(x);
```

<details><summary>Answer</summary>

`10`. `int` is a primitive, so `y = x` copies the **value**. Changing `y` doesn't affect `x`.
Compare with Q4, where an object variable was copied.
</details>

**Q6.** The class `Date` has only the constructor `Date(int day, int month, int year)`.
Does `Date d = new Date();` compile? Why or why not?

<details><summary>Answer</summary>

No. Java only provides the default (no-parameter) constructor when a class has **no constructors
at all**. Once you write your own, the default one is removed. You would need to add a
`public Date()` constructor yourself.
</details>

**Q7.** What happens here, and what's the name of the error?

```java
Date d = null;
System.out.println(d.toString());
```

<details><summary>Answer</summary>

The program crashes with a **`NullPointerException`**, because `d` doesn't refer to any object,
so there's nothing to call `toString()` on.
</details>

**Q8.** You want a method in `Date` that says whether this date is in the same year as another
date. Write its **signature** (return type, name, parameters).

<details><summary>Answer</summary>

`public boolean sameYear(Date other)`. It returns yes or no (`boolean`), and it needs the other
`Date` to compare with. The body would be `return this.year == other.year;`.
</details>

**Q9.** What does `this` mean inside a method?

<details><summary>Answer</summary>

The object the method was **called on**. In `today.sameDate(birthday)`, inside `sameDate`, `this`
is `today` and the parameter `d` is `birthday`. The lecture calls `this` the *implicit parameter*.
</details>

**Q10.** Two `Date` objects both hold 25/12/2026. Why might `d1 == d2` be `false`?

<details><summary>Answer</summary>

`==` on objects compares **references** (addresses), not contents. If they are two separately
created objects, they're at different addresses, so `==` is `false` even though the dates match.
To compare contents, use a method like `sameDate` (or `equals`, which you override in Lesson 7).
</details>

---

[← Previous: Lesson 2](../02-basic-syntax/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 4, OOP and Unit Testing →](../04-oop-and-unit-testing/README.md)
