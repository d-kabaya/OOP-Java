# Lesson 6: Interfaces and Polymorphism

**Based on:** Lecture 6 (Interfaces)
**Time needed:** about 1 hour 15 minutes

## What you will learn

By the end of this lesson you should be able to:

- use an `ArrayList` confidently
- explain how interfaces let one piece of code work with many different classes
- write an interface, and implement it in a class
- use an interface as a type, and explain **polymorphism**
- convert between interface types and classes, safely, with `instanceof` and casting
- use `static` methods, `default` methods and constants in interfaces
- avoid the common interface errors, and use **mock objects** for testing

---

## 1. Reminder: `ArrayList`

An `ArrayList` is a list that grows and shrinks as needed (Lesson 2 compares it with arrays).

```java
import java.util.ArrayList;

ArrayList<String> list = new ArrayList<String>();
list.add("Simon");            // add to the end
list.add("Java");
list.remove("Simon");         // remove by value
list.get(0);                  // "Java": the items after a removed one move down
list.size();                  // 1
list.contains("Java");        // true
list.set(0, "Python");        // replace item 0
```

The `<String>` part says what the list holds. It must be a class (`Integer`, not `int`).

---

## 2. The problem interfaces solve

The lecture starts with a `DataSet` class that works out the average and maximum of some numbers:

```java
public class DataSet {
    private double sum;
    private double max;
    private int count;

    public void add(double x) {
        sum += x;
        if (count == 0 || x > max) {
            max = x;
        }
        count++;
    }

    public double getAverage() { ... }
    public double getMaximum() { return max; }
}
```

Now you want the same thing for **bank accounts** (average balance, richest account), and then for
**coins** (average value, most valuable coin). You'd have to copy the whole class twice, changing
only the type and **how each item is measured** (`x`, `x.getBalance()`, `x.getValue()`). The
algorithm is identical every time.

**The fix:** everyone agrees on **one method name** for "give me your measurement", say
`getMeasure()`. Then one `DataSet` can work for **anything** that has that method. An **interface**
is how you write down that agreement.

---

## 3. Writing and implementing an interface

An interface is **a list of methods an object must have** in order to "behave like" something.
Anything that behaves like a light must have `turnOn()` and `turnOff()`.

```java
public interface Measurable {
    double getMeasure();          // no body: just the signature, ending in ;
}
```

A class promises to have those methods by **implementing** the interface:

```java
public class BankAccount implements Measurable {
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public double getMeasure() {      // the method the interface requires
        return balance;
    }
}
```

```java
public class Coin implements Measurable {
    private double value;
    ...
    public double getMeasure() {
        return value;
    }
}
```

Now **one** `DataSet` works for both:

```java
public class DataSet {
    private double sum;
    private Measurable max;
    private int count;

    public void add(Measurable x) {
        sum += x.getMeasure();
        if (count == 0 || x.getMeasure() > max.getMeasure()) {
            max = x;
        }
        count++;
    }

    public Measurable getMaximum() { return max; }
    ...
}
```

### Implementing an interface is a contract

The compiler enforces these rules:

1. You must implement **every** method in the interface, or the class won't compile.
2. Each one must be **`public`**.
3. The **return type, name and parameters must match exactly**.

**Analogy:** an interface is a job description: "must be able to drive". It lists what you must be
able to do, but not how. Very different people (classes) can meet the same job description.

### Interface vs class

| Interface | Class |
|---|---|
| Methods have no body (they're **abstract**) | Methods have bodies |
| All methods are automatically **public** | Methods can have any access level |
| **No instance variables** (only constants) | Can have instance variables |
| **You can't create one** with `new` | You can create objects with `new` |
| A class can implement **many** | A class can extend only **one** (Lesson 7) |

### Implementing several interfaces

An object can behave like many things. List the interfaces with commas, and implement every
method from **all** of them:

```java
public class BankAccount implements Measurable, Taxable { ... }
```

### Interfaces you'll meet in Java

| Interface | Used for |
|---|---|
| `Comparable` | Sorting objects (it has `compareTo`) |
| `Runnable` | Threads (Lesson 10) |
| `List` | Lists: `ArrayList` implements it. `Collections.sort(List)` was written once and works for every kind of list |

---

## 4. Interfaces as types, and polymorphism

You can use an interface as the type of a **variable**, **parameter** or **return value**:

```java
Measurable m = new BankAccount(1000);   // allowed: a BankAccount IS a Measurable
Measurable m2 = new String("hi");       // compile error: String doesn't implement Measurable
```

No object ever *is* just a `Measurable`. The object is really a `BankAccount` or a `Coin`, and the
variable only knows it as a `Measurable`.

### Which method runs?

```java
Shape s1 = new Square(5);
Shape s2 = new Circle(5);
double a1 = s1.getArea();      // runs SQUARE's getArea
double a2 = s2.getArea();      // runs CIRCLE's getArea
```

Both variables are type `Shape`, but the JVM looks at the **real class** of each object and runs
that class's version of the method. This is called **dynamic method lookup**, and it makes
**polymorphism** possible: the same method call behaves differently depending on the object.

That's why `DataSet` works: `x.getMeasure()` runs `BankAccount`'s version for bank accounts and
`Coin`'s version for coins, without `DataSet` knowing or caring which.

### You can only call methods the variable's type knows about

```java
BankAccount ac = new BankAccount(1234);
Measurable meas = ac;

meas.getMeasure();     // OK: getMeasure is in Measurable
meas.getBalance();     // compile error: Measurable has no getBalance
```

The object really is a `BankAccount`, but through a `Measurable` variable Java only lets you use
`Measurable`'s methods.

---

## 5. Converting back: casting and `instanceof`

To use the class's own methods again, **cast** the reference back to the class:

```java
Measurable m = dataset.getMaximum();
BankAccount b = (BankAccount) m;       // a cast: (NewType) variable
b.getBalance();                        // OK now
```

A cast **doesn't change the object**. It just tells Java to treat the reference as a different
type. You now have two references to the same object.

If the object isn't really that type, the program **crashes** with a `ClassCastException`. So check
first with **`instanceof`**:

```java
if (m instanceof BankAccount) {
    BankAccount b = (BankAccount) m;
    System.out.println(b.getBalance());
}
```

| Direction | Example | Needs a cast? | Can it fail? |
|---|---|---|---|
| Class → interface | `Measurable m = account;` | No: automatic | No |
| Interface → class | `BankAccount b = (BankAccount) m;` | **Yes** | **Yes**, if the object isn't that class. Check with `instanceof` |

---

## 6. What else can go in an interface

### Static methods

Utility code that belongs to the interface. Call them with the **interface name**:

```java
public interface Shape {
    double getArea();

    static Shape getLargest(Shape s1, Shape s2) {
        if (s1.getArea() > s2.getArea()) {
            return s1;
        } else {
            return s2;
        }
    }
}

Shape biggest = Shape.getLargest(circle, square);
```

A static interface method **isn't inherited** by implementing classes: `Square.getLargest(...)`
doesn't compile.

### Default methods

A method **with a body** that every implementing class gets automatically. A class can override it.

```java
public interface Shape {
    double getArea();

    default void printArea() {
        System.out.println("My area is " + getArea());
    }
}
```

The lecturer prefers putting shared code like this in an abstract class (Lesson 7), but you should
recognise default methods when you see them.

### Constants

Interfaces can't have instance variables, but they can have constants. Every variable in an
interface is automatically `public static final`:

```java
public interface SwingConstants {
    int NORTH = 1;      // really: public static final int NORTH = 1;
    int EAST = 3;
}
```

### Summary of interface methods

| Kind | Has a body? | How it's called | Must implementing classes write it? |
|---|---|---|---|
| Abstract (normal) | No | `obj.getArea()` | **Yes**, and it must be `public` |
| `static` | Yes | `Shape.getLargest(a, b)` (interface name only) | No |
| `default` | Yes | `obj.printArea()` | No, but they can override it (still `public`) |

---

## 7. Common errors

| Mistake | Result |
|---|---|
| Implementing an interface method without `public` | `attempting to assign weaker access privileges; was public` |
| Missing one of the interface's methods | `X is not abstract and does not override abstract method ...` |
| A slightly different signature (e.g. `int getMeasure()`) | It doesn't count as implementing the method: the same error as above |
| `new Measurable()` | `Measurable is abstract; cannot be instantiated`. Only classes can be created |
| Calling a class-only method through an interface variable | `cannot find symbol`. Cast first, after checking with `instanceof` |
| Casting to the wrong class | `ClassCastException` when the program runs |

---

## 8. Mock objects

In a big program, you often want to test one class **before** the classes it depends on are
finished. A **mock object** is a very simplified stand-in that "responds appropriately" to the
input you give it.

The lecture's example: a `GradeBook` that stores students' scores. First, agree on an interface:

```java
public interface GradeBook {
    void addScore(int studentId, double score);
    double getAverageScore(int studentId);
    void save(String filename);
}
```

Then write a quick mock version that takes shortcuts:

```java
public class MockGradeBook implements GradeBook {
    private ArrayList<Double> scores = new ArrayList<Double>();

    public void addScore(int studentId, double score) {
        scores.add(score);                  // shortcut: ignores studentId
    }

    public double getAverageScore(int studentId) {
        double total = 0;
        for (double x : scores) {
            total += x;
        }
        return total / scores.size();
    }

    public void save(String filename) {
        // shortcut: does nothing
    }
}
```

Code that uses a `GradeBook` can now be written and **tested** with the mock. Later, the real
`GradeBook` replaces it, and nothing else has to change, because of polymorphism.

Mock objects are very useful in **team projects**: (1) agree on the interfaces, (2) each person
develops their part separately, testing against mocks, (3) put the real parts together.

---

## Examples to run

| File | Shows |
|---|---|
| [`Measurable.java`](examples/Measurable.java), [`BankAccount.java`](examples/BankAccount.java), [`Coin.java`](examples/Coin.java), [`DataSet.java`](examples/DataSet.java) | The lecture's `DataSet` example |
| [`Shape.java`](examples/Shape.java), [`Square.java`](examples/Square.java), [`Circle.java`](examples/Circle.java) | Polymorphism, plus `static` and `default` interface methods |
| [`InterfaceDemo.java`](examples/InterfaceDemo.java) | Uses all of the above, including casting with `instanceof` |

Compile all: `javac *.java`, then run `java InterfaceDemo`.

---

## Summary

- An **interface** lists methods a class must have. A class **implements** it with `implements`,
  and must write every method, `public`, with exactly matching signatures.
- Interfaces let **one piece of code work with many classes**, such as `DataSet` with any `Measurable`.
- An interface can be used as a **type**, but you can't create one with `new`.
- **Polymorphism:** the JVM runs the method of the object's **real class** (dynamic method lookup).
- Through an interface variable, you can only call the interface's methods. **Cast** back to the
  class, after checking with **`instanceof`**.
- Interfaces can also have `static` methods, `default` methods and constants (`public static final`).
- **Mock objects** are simple stand-ins that let you test code before everything is finished.

---

## Quiz

**Q1.** Why does the lecture's `DataSet` class benefit from the `Measurable` interface?

<details><summary>Answer</summary>

Without it, you'd need a separate copy of `DataSet` for doubles, bank accounts, coins and so on,
even though the algorithm is identical. With `Measurable`, **one** `DataSet` works for **any** class
that implements `getMeasure()`, so the code is written once and reused.
</details>

**Q2.** What three rules must a class follow when it implements an interface?

<details><summary>Answer</summary>

1. Implement **every** method in the interface.
2. Make each one **`public`**.
3. Match the **return type, name and parameters** exactly.
</details>

**Q3.** Which line(s) don't compile, and why?

```java
Measurable a = new Coin("50 cent", 0.50);
Measurable b = new Measurable();
Measurable c = "hello";
double d = a.getMeasure();
```

<details><summary>Answer</summary>

- `new Measurable()`: you **can't create an interface**, only objects of classes that implement it.
- `Measurable c = "hello";`: `String` doesn't implement `Measurable`.

The other two lines are fine.
</details>

**Q4.** What is printed?

```java
Shape s = new Circle(1);
System.out.println(s.getArea());
```
(where `Circle`'s `getArea()` returns `Math.PI * radius * radius`)

<details><summary>Answer</summary>

`3.141592653589793`. The variable's type is `Shape`, but the object is a `Circle`, so **Circle's**
`getArea()` runs. This is polymorphism (dynamic method lookup).
</details>

**Q5.** Why doesn't this compile, and how do you fix it?

```java
Measurable m = new BankAccount(500);
System.out.println(m.getBalance());
```

<details><summary>Answer</summary>

Through a `Measurable` variable, you can only call methods that are in `Measurable`, and
`getBalance()` isn't. Cast back to `BankAccount` (safely):

```java
if (m instanceof BankAccount) {
    BankAccount b = (BankAccount) m;
    System.out.println(b.getBalance());
}
```
</details>

**Q6.** What happens when this runs?

```java
Measurable m = new Coin("1 euro", 1.0);
BankAccount b = (BankAccount) m;
```

<details><summary>Answer</summary>

It compiles, but crashes when it runs with a **`ClassCastException`**, because the object is really a
`Coin`, not a `BankAccount`. Check with `if (m instanceof BankAccount)` before casting.
</details>

**Q7.** What is wrong with this class?

```java
public interface Light {
    void turnOn();
    void turnOff();
}

public class Lamp implements Light {
    void turnOn() { System.out.println("On"); }
}
```

<details><summary>Answer</summary>

Two problems:
1. `turnOn()` isn't `public`. Interface methods are automatically public, so the implementation must be too.
2. `turnOff()` is missing. Every interface method must be implemented (unless the class is abstract).
</details>

**Q8.** What is the difference between a `default` method and a `static` method in an interface?

<details><summary>Answer</summary>

Both have a body. A **default** method is called **on an object** (`lamp.printStatus()`); every
implementing class gets it, and can override it. A **static** method is called **on the interface
name** (`Shape.getLargest(a, b)`), isn't inherited by implementing classes, and has no object (no `this`).
</details>

**Q9.** In an interface, `int MAX = 10;` is really short for what?

<details><summary>Answer</summary>

`public static final int MAX = 10;`. Every variable in an interface is a public constant.
</details>

**Q10.** What is a mock object, and give one reason to use one.

<details><summary>Answer</summary>

A simplified stand-in for a real class, usually implementing the same interface and taking shortcuts.
Reasons include: testing a class **before** the classes it depends on are finished; making tests
simpler and faster; and letting a **team** work on different classes at the same time after agreeing
on the interfaces.
</details>

**Q11.** Can a class implement more than one interface? How?

<details><summary>Answer</summary>

Yes. List them separated by commas, e.g. `public class BankAccount implements Measurable, Taxable`,
and implement **every** method from **all** of them.
</details>

---

[← Previous: Lesson 5](../05-encapsulation/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 7, Inheritance →](../07-inheritance/README.md)
