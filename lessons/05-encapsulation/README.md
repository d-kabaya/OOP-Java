# Lesson 5: Encapsulation and `static`

**Based on:** Lecture 5 (Encapsulation)
**Time needed:** about 1 hour 15 minutes

## What you will learn

By the end of this lesson you should be able to:

- explain the difference between passing a primitive and passing an object to a method
- choose the right access level: `public`, `protected`, default or `private`
- design a class well: accessors and mutators, validation, immutability, few side effects
- recognise the standard patterns for object data (totals, counters, collections, properties, states)
- use `static` correctly: class variables, constants, static methods and singletons

---

## 1. Passing values to methods

```java
public void changeInt(int i) { i = 99; }
public void changeList(ArrayList<String> list) { list.add("new"); }
```

| Passing a... | What the method gets | Can the method change the caller's data? |
|---|---|---|
| **Primitive** (`int`, `double`, ...) | a **copy of the value** | **No.** Changing `i` only changes the method's own copy |
| **Object** (`ArrayList`, `Student`, ...) | a copy of the **reference**, pointing to the **same object** | **Yes.** Changes made through the reference affect the caller's object |

```java
int x = 5;
changeInt(x);
System.out.println(x);          // 5: unchanged

ArrayList<String> names = new ArrayList<String>();
changeList(names);
System.out.println(names);      // [new]: the caller's list changed
```

The lecture calls these **pass by value** and **pass by reference**. Strictly speaking, Java
always passes a *copy*; for objects, it's a copy of the reference. The effect is what matters:
**a method can change an object you pass in, but not a primitive.**

This is why **immutable** objects like `String` and `Integer` are always safe to pass around:
nothing can change them.

---

## 2. Access levels

| Modifier | Same class | Same package | Subclass | Anywhere |
|---|---|---|---|---|
| `public` | ✅ | ✅ | ✅ | ✅ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| *(none)*, called **default** | ✅ | ✅ | ❌ | ❌ |
| `private` | ✅ | ❌ | ❌ | ❌ |

The question to ask is: **who needs to see or change this?**

- **Instance variables:** almost always `private`. This is **information hiding**: the class keeps
  control over how its data changes.
- **Methods:** `public` if other classes should use them; `private` for helpers used only inside the class.
- **Classes:** usually `public`. Only **one** public class is allowed per `.java` file.
- The lecture says you should rarely leave out the modifier (default) for methods or variables.
  `protected` matters once you use inheritance (Lesson 7).

### Why `public` fields are dangerous

If a `CashRegister` had `public double money;`, **anyone** could write `register.money = 0;` or
delete the transaction history to hide stealing from the till. Making the fields `private` means
the only way to change them is through methods **you** wrote, and those methods can check the values.

---

## 3. Designing good classes

### A cohesive public interface

A class should represent **one concept**, and all its public methods should relate to that
concept. If methods seem unrelated, the class probably needs splitting (Lesson 4).

### Minimise dependencies

A method that prints with `System.out.println` depends on there being a console, which an ATM,
a phone app or a drone may not have. It's better for a method to **return** a result (or an object
describing it) and let a separate part of the program decide how to show it. Dependencies between
classes are normal; the aim is to keep them to a minimum, not to remove them.

### Accessors and mutators

- An **accessor** (getter) returns information and **doesn't change** the object.
- A **mutator** (setter) **changes** the object.

A class with **no mutators** is **immutable**. `String` is the classic example: methods like
`toUpperCase()` return a *new* String. Immutable classes suit **values**: dates, money amounts,
colours, strings.

**Keep accessors and mutators separate.** A method that returns a value shouldn't also change the
object, because the caller might not expect it to (*accidental mutation*). So mutators are usually
`void`. There are exceptions in Java's own libraries, such as `Scanner.next()`, which both changes
the scanner's position and returns a value.

### Validate in mutators and constructors

```java
public void setName(String s) {
    if (s.length() > 0) {
        name = s;
    } else {
        throw new IllegalArgumentException("Name cannot be empty");
    }
}
```

Every method that changes a field should check the new value. Constructors should check too, and a
constructor can simply call the setter so the rule is written only once.

**Not every property needs a setter.** A student number should never change, so give it a getter
but no setter.

### Minimise side effects

A method shouldn't change its **parameters**. Because objects are shared by reference, changing a
parameter changes the caller's object, which they won't expect. Visit parameters, but don't modify
them.

The same applies in reverse. If a getter returns your private `ArrayList` directly, the caller can
change your data. Return a copy instead:

```java
public ArrayList<String> getTransactions() {
    return new ArrayList<String>(transactions);
}
```

### Be consistent

Use a consistent naming scheme and parameter order across your methods. If one method is
`addItem(String name, int qty)`, don't make a similar one `removeItem(int qty, String name)`.

---

## 4. Standard patterns for object data

You'll use at least one of these in almost every class:

| Pattern | Example | How |
|---|---|---|
| **Keeping a total** | a bank balance, money in a till | a private variable, with methods to increase and decrease it |
| **Counting events** | number of sales | a private counter that a method increments, and maybe a `reset()` |
| **Collecting values** | a list of transactions | a private `ArrayList`, created in the constructor |
| **Managing properties** | a name, an age | a private field with a getter and a validating setter |
| **Modelling distinct states** | hungry / not hungry | an `enum`, or `static final int` constants |
| **Sets of related features** | a bug's x, y, direction and speed | several fields updated together by one method like `move()` |

---

## 5. `static`: belonging to the class, not to an object

### Class variables (static variables)

An **instance variable** has a separate value **in every object**. A **static variable** has
**one** value **shared by all objects** of the class.

```java
public class Counter {
    private int count;              // instance: each Counter has its own
    private static int total;       // static: ONE value shared by all Counters

    public void increment() {
        count++;
        total++;
    }
}
```

```java
Counter a = new Counter();
Counter b = new Counter();
a.increment();
a.increment();
// a's count is 2, b's count is 0, and the shared total is 2
```

A public static variable is accessed through the **class name**: `Math.PI`, `Integer.MAX_VALUE`.
Even `System.out` is a static variable of the `System` class.

### Constants: `static final`

- `final` means the variable can be assigned **only once**.
- `static final` means one shared value that **never changes**: a **constant**.

```java
public static final int MAX_STUDENTS = 30;
public static final double VAT_RATE = 0.23;
```

Constants are named in **UPPER_CASE_WITH_UNDERSCORES**. They replace "magic numbers": compare
`if (n > 30)` with `if (n > MAX_STUDENTS)`. It's fine for them to be `public`, since `final`
means nobody can change them.

### Static methods (class methods)

A static method belongs to the class, so you call it **without an object**:
`ClassName.methodName(...)`.

```java
public class Dice {
    private static Random rng = new Random();

    public static int roll1D6() {
        return 1 + rng.nextInt(6);       // nextInt(6) gives 0 to 5
    }

    public static int roll2D6() {
        return roll1D6() + roll1D6();
    }
}

int roll = Dice.roll2D6();               // no "new Dice()" needed
```

**The rule:** a static method has no object, so there's no `this`, and it **cannot use instance
variables** or call instance methods directly. It can use its parameters, local variables, static
variables and other static methods.

```java
public class Student {
    private int age;

    public static void printAge() {
        System.out.println(age);   // compile error: non-static variable age
                                   // cannot be referenced from a static context
    }
}
```

**How to decide:** does the method need a particular object's fields?
**Yes** → instance method. **No** → it *can* be static.

Familiar static methods: `main`, `Integer.parseInt`, `Math.sqrt`, `Math.max`.

The lecture warns that static methods **aren't very object-oriented**: a program full of them is
really C written in Java. Use them for utility jobs, not as a replacement for objects.

### Static imports

`import static java.lang.Math.*;` lets you write `sqrt(x)` instead of `Math.sqrt(x)`. The lecture's
view is that this usually makes code **more** confusing, because it's no longer clear where `sqrt`
comes from.

### Singletons

A **singleton** is a class that allows **only one object** to ever exist. It has no public
constructor; instead, a static method hands out the single instance:

```java
public class DBConnector {
    private static DBConnector instance;      // the one and only object

    private DBConnector() { }                 // private: nobody else can call "new"

    public static DBConnector getDBConnector() {
        if (instance == null) {
            instance = new DBConnector();     // created the first time it's needed
        }
        return instance;
    }
}
```

Use a singleton for something with a major role where several copies would be wasteful or wrong,
like a database connection. Java's `Runtime.getRuntime()` is a real example.

---

## Examples to run

| File | Shows |
|---|---|
| [`CashRegister.java`](examples/CashRegister.java) | Keeping a total, counting events, collecting values, validation, constants, a static count of registers, returning a copy |
| [`Dice.java`](examples/Dice.java) | A class made only of static methods |
| [`Logger.java`](examples/Logger.java) | A singleton |
| [`EncapsulationDemo.java`](examples/EncapsulationDemo.java) | Uses all of the above, plus passing primitives vs objects |

Compile all: `javac *.java`, then run `java EncapsulationDemo`.

---

## Summary

- A method gets a **copy** of a primitive (it can't change the caller's), but a **reference** to an
  object (it **can** change the caller's object).
- Make fields `private`. Change them only through methods that **validate**.
- Separate accessors from mutators. Don't change parameters. Return **copies** of private collections.
- `static` variables are shared by all objects. `static final` makes a constant.
- `static` methods have no `this`, so they can't use instance variables. Call them on the class name.
- A **singleton** allows only one instance, handed out by a static method.

---

## Quiz

**Q1.** What is printed?

```java
public static void addTen(int n) { n = n + 10; }
public static void addItem(ArrayList<Integer> list) { list.add(10); }

// in main:
int x = 5;
ArrayList<Integer> nums = new ArrayList<Integer>();
addTen(x);
addItem(nums);
System.out.println(x + " " + nums);
```

<details><summary>Answer</summary>

`5 [10]`. `addTen` gets a **copy** of the `int`, so `x` is unchanged. `addItem` gets a reference to
the **same** `ArrayList`, so the item is added to `nums`.
</details>

**Q2.** Which access level makes something visible to its own class, its package and its
subclasses, but nowhere else?

<details><summary>Answer</summary>

`protected`.
</details>

**Q3.** Why should instance variables usually be `private`?

<details><summary>Answer</summary>

So the class stays in control of its own data (**information hiding**). Other code can only
change the values through the class's methods, which can **validate** them. With public fields,
anyone could set invalid values, such as a negative balance.
</details>

**Q4.** What is an immutable class? Give an example from Java's library.

<details><summary>Answer</summary>

A class whose objects **can't be changed** after they're created: it has no mutator methods.
`String` is the classic example (methods like `toUpperCase()` return a new String). The wrapper
classes such as `Integer` are immutable too.
</details>

**Q5.** What is wrong with this getter, and how do you fix it?

```java
private ArrayList<String> transactions;

public ArrayList<String> getTransactions() {
    return transactions;
}
```

<details><summary>Answer</summary>

It returns a reference to the class's **private list itself**, so the caller can add, remove or
clear items and bypass any validation. Return a copy instead:
`return new ArrayList<String>(transactions);`
</details>

**Q6.** What is printed?

```java
public class Counter {
    private static int count = 0;
    public Counter() { count++; }
    public static int getCount() { return count; }
}

// in main:
new Counter();
new Counter();
Counter c = new Counter();
System.out.println(Counter.getCount());
```

<details><summary>Answer</summary>

`3`. `count` is **static**, so there's one shared value. Each constructor call adds 1 to it.
</details>

**Q7.** Why doesn't this compile?

```java
public class Circle {
    private double radius;

    public static double area() {
        return Math.PI * radius * radius;
    }
}
```

<details><summary>Answer</summary>

`area` is **static**, so it runs without an object and has no `this`. It can't know **which**
circle's `radius` to use. The error is *"non-static variable radius cannot be referenced from a
static context"*. Remove `static` from `area`, since it needs a particular circle's data.
</details>

**Q8.** What is the difference between `final` and `static final`?

<details><summary>Answer</summary>

`final` means a variable can be assigned only **once**. If it's an instance variable, each object
can still have a different (fixed) value. `static final` means there is **one shared value** for the
whole class that never changes: a **constant**, like `public static final int MAX_SIZE = 30;`.
</details>

**Q9.** How do you call a static method `roll2D6()` in a class `Dice`? Do you need an object?

<details><summary>Answer</summary>

`Dice.roll2D6()`. No object is needed: static methods belong to the class, so you call them on the
**class name**.
</details>

**Q10.** What two things make a class a singleton?

<details><summary>Answer</summary>

1. A **private constructor**, so no other code can create objects with `new`.
2. A **static method** (such as `getInstance()`) that creates the single object the first time it's
   needed, stores it in a **static variable**, and returns that same object every time.
</details>

**Q11.** A method called `getBalance()` also adds a monthly fee to the balance each time it's
called. Which design principle does this break, and why is it a problem?

<details><summary>Answer</summary>

It mixes an **accessor** with a **mutator**. Someone calling `getBalance()` just to look at the
balance would change it without meaning to (**accidental mutation**). Keep them separate: a
`getBalance()` that only returns the value, and an `applyMonthlyFee()` that changes it.
</details>

**Q12.** Replace the magic numbers in this code with constants:

```java
if (age >= 18 && age <= 65) { ... }
```

<details><summary>Answer</summary>

```java
public static final int MIN_WORKING_AGE = 18;
public static final int MAX_WORKING_AGE = 65;

if (age >= MIN_WORKING_AGE && age <= MAX_WORKING_AGE) { ... }
```
The code now explains itself, and if a limit changes, you only edit it in one place.
</details>

---

[← Previous: Lesson 4](../04-oop-and-unit-testing/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 6, Interfaces →](../06-interfaces/README.md)
