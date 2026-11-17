# Lesson 9: Lambda Expressions and the Stream API

**Based on:** Lecture 9 (Lambda Expressions and Functional Interfaces)
**Time needed:** about 1 hour 15 minutes

## What you will learn

By the end of this lesson you should be able to:

- write an **anonymous inner class**, and explain why lambdas replace it
- explain what a **functional interface** is
- write lambda expressions in their short and long forms, and call them
- pass lambdas to methods, including Java's own (`sort`, `assertThrows`)
- use the newer **arrow form of `switch`**
- sort with `Comparable`, with a lambda, and with a **method reference**
- build simple **stream** pipelines: source → intermediate operations → terminal operation

The lecture notes that **streams are not in the exam scope**, but lambdas are, and you'll meet
streams constantly in real code (and in AI-generated code).

---

## 1. The problem: lots of code for one small job

Here's an interface from the lecture:

```java
public interface Calculator {
    double calculate(double width, double height);
}
```

Say you want a `Calculator` that works out an area. You could write a whole named class
(Lesson 6), but if you'll only use it once, that's a lot of packaging for one multiplication.

### Anonymous inner classes

An **anonymous inner class** is a class **with no name**, written right where you need it. It
creates one object that implements the interface:

```java
Calculator area = new Calculator() {
    @Override
    public double calculate(double width, double height) {
        return width * height;
    }
};

area.calculate(4, 5);   // 20.0
```

Better, but still six lines to say "multiply width by height".

### The lambda version

```java
Calculator area = (width, height) -> width * height;
```

All three do **exactly the same thing**. The lambda removes the packaging (`new`, the class body,
the method name, the return type, `public`) and keeps only the **parameters** and the **calculation**.

The lecture describes a lambda as *"essentially an anonymous function: has no name, no explicit
return type, no visibility modifiers"*.

---

## 2. Functional interfaces

A lambda can only be used where Java expects a **functional interface**: an interface with
**exactly one abstract method**, also called a **Single Abstract Method (SAM)** interface.

Why? A lambda doesn't say which method it's writing. With only one abstract method, there's only one
possibility, so there's no ambiguity: the lambda must be `calculate`.

```java
@FunctionalInterface
public interface Calculator {
    double calculate(double width, double height);
}
```

`@FunctionalInterface` is an **annotation** (a tag for the compiler, like `@Override`). It makes the
compiler complain if anyone adds a second abstract method. `default` and `static` methods don't count:
an interface can have any number of those and still be functional.

---

## 3. Reading and writing a lambda

```java
Calculator area = (width, height) -> width * height;
//                └─ parameters ─┘ ↑  └── body ───┘
//                          lambda operator
```

| Part | Meaning |
|---|---|
| `(width, height)` | the parameters. Java works out their types from the interface |
| `->` | the lambda operator |
| `width * height` | the body. A single expression is **returned automatically** |

### Different forms of the same lambda

```java
Calculator a1 = (width, height) -> width * height;                 // short form

Calculator a2 = (double width, double height) -> {                 // long form
    return width * height;                                         // with { }, you MUST write return
};
```

The lecturer recommends the long form: *"you can't forget what types the input parameters have"*.

| Situation | Syntax |
|---|---|
| No parameters | `() -> System.out.println("Hi")` |
| One parameter | `name -> System.out.println("Hello, " + name)` (brackets optional) |
| Several parameters | `(a, b) -> a + b` |
| Several statements | `(a, b) -> { int s = a + b; return s * 2; }` |

---

## 4. Using (calling) a lambda

**Writing a lambda doesn't run it.** It packages the code into an object. The code runs when
something calls the interface's method on that object.

### Store it, then call the interface's method

```java
Calculator area = (w, h) -> w * h;     // nothing is calculated yet
double r = area.calculate(4, 5);       // NOW w * h runs: 20.0
```

A lambda has no name of its own, so `area(4, 5)` doesn't work. Call it through the interface
method's name: `area.calculate(4, 5)`.

### Pass it to a method

```java
public static void printResult(Calculator calc, double w, double h) {
    System.out.println("Result: " + calc.calculate(w, h));
}

printResult((w, h) -> w * h, 4, 5);         // Result: 20.0
printResult((w, h) -> 2 * (w + h), 4, 5);   // Result: 18.0
```

`printResult` doesn't know what calculation it's doing: the **caller** decides by passing a lambda.
This is polymorphism (Lesson 6) in a very compact form.

### Java's methods often call your lambda for you

| Your code | Who calls the lambda | Interface |
|---|---|---|
| `list.sort((a, b) -> ...)` | `sort`, once per comparison | `Comparator` (`compare`) |
| `.filter(s -> ...)` in a stream | the stream, once per element | `Predicate` (`test`) |
| `assertThrows(X.class, () -> ...)` | `assertThrows`, inside a try/catch | `Executable` (`execute`) |
| `new Thread(() -> ...)` | the new thread (Lesson 10) | `Runnable` (`run`) |

### Why `assertThrows` needs a lambda

```java
assertThrows(IllegalArgumentException.class, () -> new House(-1));   // ✅
assertThrows(IllegalArgumentException.class, new House(-1));         // ❌
```

Java always works out a method's arguments **before** calling it. In the second line, `new House(-1)`
runs first and throws, so the test crashes before `assertThrows` even starts. The lambda **packages the
code without running it**, so `assertThrows` can run it later, inside its own safety net.

---

## 5. The arrow form of `switch` (Java 14+)

Lambda-style arrows also appear in a newer `switch`:

```java
switch (itemCode) {
    case 1, 2 -> System.out.println("It's a laptop!");
    case 3 -> System.out.println("It's a desktop!");
    default -> System.out.println("Unknown device!");
}
```

- Several values can share a `case`: `case 1, 2`.
- There's **no fall-through**, so **no `break`** is needed.

It can also **produce a value**:

```java
String message = switch (day) {
    case "MONDAY" -> "Start of the work week";
    case "SATURDAY", "SUNDAY" -> "Weekend!";
    default -> "Midweek day";
};
```

---

## 6. Sorting

### With `Comparable`

`Comparable<T>` is a functional interface with one method, `int compareTo(T other)`. It defines the
**natural order** of a class: negative means "this comes first", positive means "other comes first",
and 0 means equal.

```java
public class Person implements Comparable<Person> {
    private String name;
    private int age;
    ...
    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);    // youngest first
    }
}

Collections.sort(people);      // uses compareTo
```

### With a lambda (no `Comparable` needed)

```java
people.sort((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));   // youngest first
people.sort((p1, p2) -> Integer.compare(p2.getAge(), p1.getAge()));   // swap them: oldest first
```

### With a method reference

A **method reference** is an even shorter lambda that just calls one existing method:

```java
people.sort(Person::compareTo);           // same as (a, b) -> a.compareTo(b)
names.forEach(System.out::println);       // same as n -> System.out.println(n)
```

Read `Person::compareTo` as "use `Person`'s `compareTo` method here".

---

## 7. The Stream API

A **stream** processes a sequence of elements, usually from a collection, as a **chain of steps**.
It focuses on **what** you want, not **how** to loop.

### Loop vs stream

```java
// With a loop
List<String> result = new ArrayList<String>();
for (String name : names) {
    if (name.length() > 3) {
        result.add(name.toUpperCase());
    }
}

// With a stream
List<String> result = names.stream()                 // 1. source
        .filter(name -> name.length() > 3)           // 2. intermediate: keep some
        .map(name -> name.toUpperCase())             // 2. intermediate: convert each
        .collect(Collectors.toList());               // 3. terminal: produce the result
```

Every stream has **three parts**:

| Part | What it is | Examples |
|---|---|---|
| **Source** | where the elements come from | `list.stream()` |
| **Intermediate operations** | processing steps. Each returns a **new stream**, so they can be chained | `filter`, `map`, `sorted`, `distinct`, `limit` |
| **Terminal operation** | the final step. It returns a **real result** (a `List`, a number, a `boolean`), not a stream | `collect`, `count`, `forEach`, `anyMatch`, `sum` |

Two key facts:
- **A stream never modifies its source.** After the code above, `names` is unchanged.
- **Intermediate operations are lazy.** If you forget the terminal operation, **nothing runs at all**.

### Common operations

| Operation | Does |
|---|---|
| `filter(x -> condition)` | keeps the elements for which the lambda returns `true` |
| `map(x -> newValue)` | converts each element |
| `sorted()` / `sorted((a, b) -> ...)` | puts the elements in order |
| `distinct()` | removes duplicates |
| `limit(n)` | keeps the first `n` |
| `mapToInt(x -> number)` | converts to a stream of `int`s, which has `sum()` and `average()` |
| `collect(Collectors.toList())` | gathers the elements into a `List` |
| `count()` | how many elements (returns a `long`) |
| `anyMatch` / `allMatch` / `noneMatch` | checks a condition, returns a `boolean` |
| `forEach(x -> ...)` | does something with each element |

Imports: `java.util.List`, `java.util.stream.Collectors`.

---

## Examples to run

| File | Shows |
|---|---|
| [`Calculator.java`](examples/Calculator.java) | A functional interface |
| [`LambdaDemo.java`](examples/LambdaDemo.java) | Anonymous inner class vs lambda, both forms, passing lambdas to a method, `Runnable`, arrow `switch` |
| [`Person.java`](examples/Person.java), [`SortingDemo.java`](examples/SortingDemo.java) | Sorting with `Comparable`, a lambda and a method reference |
| [`StreamDemo.java`](examples/StreamDemo.java) | Stream pipelines: `filter`, `map`, `sorted`, `distinct`, `limit`, `count`, `sum`, `anyMatch`, laziness |

Compile all: `javac *.java`, then run each demo with `java LambdaDemo`, `java SortingDemo` or `java StreamDemo`.

---

## Summary

- An **anonymous inner class** implements an interface without naming a class. A **lambda** is a
  much shorter way to do the same thing.
- Lambdas only work with **functional interfaces**: exactly **one abstract method**.
- Syntax: `(parameters) -> body`. With `{ }`, you must write `return`.
- Writing a lambda **doesn't run it**. Call it through the interface's method (`area.calculate(4, 5)`),
  or pass it to a method that calls it.
- The arrow `switch` allows several values per case, and never falls through.
- Sort with `Comparable` (natural order), a lambda, or a method reference like `Person::compareTo`.
- A **stream** = source → intermediate operations → **one** terminal operation. It never changes its
  source, and nothing runs without the terminal operation.

---

## Quiz

**Q1.** What is a functional interface?

<details><summary>Answer</summary>

An interface with **exactly one abstract method** (a Single Abstract Method, or SAM, interface).
It can also have any number of `default` and `static` methods. Lambdas can only be used where a
functional interface is expected.
</details>

**Q2.** Rewrite this anonymous inner class as a lambda:

```java
Calculator perimeter = new Calculator() {
    @Override
    public double calculate(double width, double height) {
        return 2 * (width + height);
    }
};
```

<details><summary>Answer</summary>

```java
Calculator perimeter = (width, height) -> 2 * (width + height);
```
</details>

**Q3.** Why doesn't this compile?

```java
Calculator area = (w, h) -> { w * h; };
```

<details><summary>Answer</summary>

When a lambda body uses braces `{ }`, it's a normal block of code, so you must write `return`:
`(w, h) -> { return w * h; }`. Without braces, the expression is returned automatically:
`(w, h) -> w * h`.
</details>

**Q4.** What is printed?

```java
Calculator c = (w, h) -> w - h;
System.out.println(c.calculate(10, 4));
```

<details><summary>Answer</summary>

`6.0`. `calculate` returns a `double`, so `10 - 4` is printed as `6.0`.
</details>

**Q5.** With `Calculator area = (w, h) -> w * h;`, why doesn't `area(3, 4)` work?

<details><summary>Answer</summary>

A lambda has no name of its own. `area` is an **object** that implements `Calculator`, so you call it
through the interface's method name: `area.calculate(3, 4)`.
</details>

**Q6.** Why does `assertThrows` need a lambda, like `() -> new House(-1)`, instead of just `new House(-1)`?

<details><summary>Answer</summary>

Java evaluates a method's arguments **before** calling the method. Without a lambda, `new House(-1)`
would run (and throw) before `assertThrows` starts, so the test would crash. The lambda packages the
code **without running it**; `assertThrows` then runs it inside its own try/catch and checks the exception.
</details>

**Q7.** What is printed?

```java
int day = 2;
switch (day) {
    case 1, 2 -> System.out.println("Early week");
    case 3 -> System.out.println("Midweek");
    default -> System.out.println("Other");
}
```

<details><summary>Answer</summary>

`Early week`, and **only** that. The arrow form never falls through, so no `break` is needed.
</details>

**Q8.** What does this `compareTo` do, and how would you reverse the order?

```java
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
```

<details><summary>Answer</summary>

It sorts people by age, **youngest first**. A negative result means `this` comes before `other`.
To sort oldest first, swap them: `Integer.compare(other.age, this.age)`.
</details>

**Q9.** What does the method reference `Person::compareTo` mean in `people.sort(Person::compareTo)`?

<details><summary>Answer</summary>

It's shorthand for the lambda `(a, b) -> a.compareTo(b)`: use `Person`'s own `compareTo` method to
compare two people.
</details>

**Q10.** Name the three parts of a stream, and give an example of each.

<details><summary>Answer</summary>

1. **Source**: where the elements come from, e.g. `list.stream()`.
2. **Intermediate operations**: steps that each return a new stream, e.g. `filter`, `map`, `sorted`.
3. **Terminal operation**: the final step that produces a result, e.g. `collect`, `count`, `forEach`.
</details>

**Q11.** What does this print?

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dee");
List<String> result = names.stream()
        .filter(n -> n.length() > 3)
        .map(n -> n.toUpperCase())
        .collect(Collectors.toList());
System.out.println(result);
System.out.println(names);
```

<details><summary>Answer</summary>

```
[ALICE, CHARLIE]
[Alice, Bob, Charlie, Dee]
```
The stream keeps names longer than 3 letters and converts them to upper case. The original list is
**unchanged**, because a stream never modifies its source.
</details>

**Q12.** Someone writes `names.stream().filter(n -> n.startsWith("A"));` and nothing seems to
happen. Why?

<details><summary>Answer</summary>

There's no **terminal operation**. Intermediate operations like `filter` are **lazy**: they don't
run until a terminal operation (`collect`, `count`, `forEach`, ...) asks for a result.
</details>

---

[← Previous: Lesson 8](../08-file-io-and-exceptions/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 10, Threading →](../10-threading/README.md)
