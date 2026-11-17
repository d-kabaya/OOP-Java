# Lesson 7: Inheritance, Abstract Classes and `final`

**Based on:** Lecture 7 (Inheritance)
**Time needed:** about 1 hour 30 minutes

## What you will learn

By the end of this lesson you should be able to:

- build a class hierarchy with `extends`, and explain the "is-a" relationship
- explain which inherited members a subclass can and can't access, and what `protected` does
- explain the role of the `Object` class
- use polymorphism and `instanceof` with inheritance
- **override** methods (including `toString` and `equals`), and tell overriding apart from **overloading**
- use `super` to call a superclass's constructor and methods
- write **abstract classes** and compare them with interfaces
- use `final` to stop a class being extended or a method being overridden

---

## 1. Hierarchies and "is-a"

We describe things in hierarchies. My bike is a mountain bike, but it's also a bike, and also a
vehicle. All three descriptions are true:

```
          Vehicle            ← more general
             │
          Bicycle
         /   │   \
 Mountain  Road  Tandem      ← more specific
  Bike     Bike   Bike
```

- Higher up is **more general**; lower down is **more specific**.
- A mountain bike **is a** bicycle, and **is a** vehicle. But a vehicle is **not** necessarily a bicycle.
- A class gets **all the functionality** of the classes above it. If `Bicycle` has `getGear()`
  and `Vehicle` has `getSpeed()`, then `MountainBike` has both.

This is **inheritance**. The class lower down is the **subclass** (child); the one above is the
**superclass** (parent). **Every class has exactly one superclass**, but can have many subclasses.

---

## 2. `extends`

```java
public class A {
    public void sayHi() {
        System.out.println("Hello!");
    }
}

public class B extends A {          // B is a subclass of A
    public void sayBye() {
        System.out.println("Goodbye");
    }
}
```

```java
A a = new A();
B b = new B();
a.sayHi();      // Hello!
b.sayHi();      // Hello!    B inherited sayHi from A
b.sayBye();     // Goodbye   B's own method
// a.sayBye();  // compile error: an A is not a B
```

A subclass **extends** the superclass: it adds functionality. Adding code to the subclass never
changes the superclass.

### The lecture's bank accounts

```java
public class BankAccount {
    private double balance;

    public void deposit(double amount) { balance += amount; }
    public void withdraw(double amount) { balance -= amount; }
    public double getBalance() { return balance; }
}

public class SavingsAccount extends BankAccount {
    public static final double INTEREST_RATE = 0.04;

    public void addInterest() {
        double interest = getBalance() * INTEREST_RATE;   // inherited method
        deposit(interest);                                 // inherited method
    }
}

public class CheckingAccount extends BankAccount {
    public static final double CHECKING_FEE = 1;

    public void deductFee() {
        withdraw(CHECKING_FEE);
    }
}
```

Each subclass only writes what's **new**. Deposit, withdraw and balance are written once, in
`BankAccount`.

---

## 3. Access: what a subclass can see

A subclass **inherits** every instance variable, even `private` ones. But:

> A `private` variable *"still exists, but we cannot access or change its value"* from the subclass.

That's why `SavingsAccount` uses `getBalance()` and `deposit()` above: it can't touch `balance` directly.

| Superclass field is... | Can the subclass use it directly? |
|---|---|
| `private` | ❌ It exists, but only through the superclass's methods |
| default (no modifier) | Only if the subclass is in the same package |
| `protected` | ✅ |
| `public` | ✅ (but so can everyone, which breaks encapsulation) |

The lecture's advice: if a subclass needs a field directly, `protected` keeps closer to
encapsulation than `public`. `protected` also opens the field to the whole package, so an
alternative is to keep it `private` and provide `protected` getters and setters that validate.

---

## 4. `Object`: the top of every hierarchy

If you don't write `extends`, your class automatically extends **`Object`**. So every class is, at
the top, an `Object`, and inherits its methods:

| Method from `Object` | Default behaviour |
|---|---|
| `String toString()` | the class name and a code, e.g. `BankAccount@72ea2f77` |
| `boolean equals(Object obj)` | `true` only if both are the **same object** (like `==`) |
| `int hashCode()` | a number used by collections such as `HashMap` |
| `Object clone()`, `wait()`, `notify()` | copying, and threads (Lesson 10) |

The defaults are *"usually not very good"*, so you **override** `toString` and `equals` in your own
classes (section 6).

This isn't multiple inheritance (which Java doesn't allow). It's a **chain**:
`SavingsAccount` → `BankAccount` → `Object`.

---

## 5. Polymorphism with inheritance

If `C extends D`, a `C` object can be stored in a `D` variable:

```java
CheckingAccount c = new CheckingAccount();
BankAccount b = c;           // fine: a CheckingAccount IS a BankAccount
Object o = c;                // fine: everything IS an Object
```

As with interfaces (Lesson 6), the **variable's type** decides which methods you can call:

| Stored in a... | You can call |
|---|---|
| `CheckingAccount` variable | all of `CheckingAccount`'s methods, including inherited ones |
| `BankAccount` variable | only `BankAccount`'s methods (`deductFee()` won't compile) |
| `Object` variable | only `Object`'s methods (`toString()`, `equals()`, ...) |

But the **object's real class** decides **which version** runs: if `CheckingAccount` overrides
`withdraw`, then `b.withdraw(10)` runs `CheckingAccount`'s version.

### `instanceof`

`instanceof` is `true` for the object's own class, **every superclass above it**, and every
interface they implement:

```java
CheckingAccount c = new CheckingAccount();
c instanceof CheckingAccount    // true
c instanceof BankAccount        // true
c instanceof Object             // true
c instanceof SavingsAccount     // compile error here: a CheckingAccount can never be a SavingsAccount
```

---

## 6. Overriding: changing inherited behaviour

**Overriding** means writing a method in the subclass with the **same signature** as one in the
superclass, so the subclass's version is used instead.

The rules (from the lecture):
1. the **same name**
2. the **same parameters**
3. the **same return type**
4. the **same visibility** (Java also allows *more* visible, but never less)

Put `@Override` above it. If you get the signature wrong, the compiler tells you, instead of
silently creating a new method.

### Overriding `toString`

```java
@Override
public String toString() {
    return "Account of " + accountName + ", balance " + balance;
}

System.out.println(account);    // Account of Sean, balance 0.0  (instead of BankAccount@72ea2f77)
```

### Overriding `equals`

The default `equals` only checks whether two variables point to the **same object**. Override it to
compare **contents**. The parameter **must be type `Object`**, so check the type first:

```java
@Override
public boolean equals(Object obj) {
    if (obj instanceof BankAccount) {
        BankAccount other = (BankAccount) obj;
        return other.accountNumber == this.accountNumber;   // the account number identifies the account
    } else {
        return false;
    }
}
```

1. Is `obj` a `BankAccount`? If not, they can't be equal.
2. Cast it, so you can reach its fields.
3. Compare the fields that make two accounts "the same".

If you write `equals(BankAccount obj)` instead, that's **overloading**, not overriding, and code
such as `ArrayList.contains` will still use the old `equals`.

---

## 7. `super`

`super` refers to the **direct superclass**. It has three uses.

### Use 1: calling the superclass's version of an overridden method

The lecture's `CheckingAccount` charges a fee on every transaction:

```java
public class CheckingAccount extends BankAccount {
    public static final double CHECKING_FEE = 1;

    public void deductFee() {
        withdraw(CHECKING_FEE);              // ❌ calls OUR withdraw, which calls deductFee again...
    }

    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        deductFee();
    }
}
```

`deductFee` calls `withdraw`, which calls `deductFee`, which calls `withdraw`... forever, until the
program crashes. The fix: `deductFee` must call the **superclass's** `withdraw`, which doesn't charge
a fee:

```java
public void deductFee() {
    super.withdraw(CHECKING_FEE);            // ✅ BankAccount's withdraw: no fee
}
```

`withdraw(...)` means *this class's* version (the override); `super.withdraw(...)` means the
**parent's** version.

### Use 2: calling the superclass's constructor

When you create a subclass object, the superclass part must be constructed **first**. If the
superclass's constructor needs parameters, the subclass passes them on with `super(...)`, which
**must be the first line** of the subclass's constructor:

```java
public class BankAccount {
    private double balance;

    public BankAccount(double initial) {
        balance = initial;
    }
}

public class CheckingAccount extends BankAccount {
    public CheckingAccount(double initial) {
        super(initial);                      // runs BankAccount's constructor
    }
}

public class SavingsAccount extends BankAccount {
    public SavingsAccount() {
        super(0);                            // savings accounts always start at 0
    }
}
```

If you leave out `super(...)`, Java calls `super()` with no parameters. That only works if the
superclass **has** a no-parameter constructor, which `BankAccount` above doesn't, so it won't compile.

### Use 3: superclass instance variables

`super.x` reaches a superclass field with the same name as a subclass field. The lecture's advice is
blunt: **don't give a subclass a field with the same name as its superclass's**. It's confusing.

---

## 8. Overloading vs overriding

**Overloading** means several methods with the **same name but different parameters**, in the same
class. Java picks one based on the arguments. `System.out.println` is overloaded many times, which
is why it can print an `int`, a `String` or any object:

```java
System.out.println(123);       // println(int)
System.out.println("Hi");      // println(String)
System.out.println(account);   // println(Object), which calls toString()
```

| | Overloading | Overriding |
|---|---|---|
| Where | Usually the same class | A subclass |
| Method name | Same | Same |
| Parameters | **Different** | **The same** |
| Return type | Can differ | Must be the same |
| Chosen | When **compiling**, by the arguments' types | When **running**, by the object's real class |
| Purpose | One action, different inputs | Change an inherited behaviour |

---

## 9. Abstract classes

**Abstract** means an idea that doesn't exist on its own. Nobody owns "a phone" in general: they own
a particular model, which is one way of building the idea of a phone.

In Java, `abstract` means **not implemented**:

```java
public abstract class Abstract2DDrawing {
    protected int x, y;                       // shared data

    public Abstract2DDrawing(int x, int y) {  // shared constructor
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {        // shared, working method
        x += dx;
        y += dy;
    }

    public abstract void draw();              // no body: each kind of drawing writes its own
}

public class Building extends Abstract2DDrawing {
    private int w, h;

    public Building(int x, int y, int w, int h) {
        super(x, y);
        this.w = w;
        this.h = h;
    }

    @Override
    public void draw() {
        System.out.println("Building at (" + x + ", " + y + "), " + w + "x" + h);
    }
}
```

The rules:
- You **can't create** an abstract class with `new`: `new Abstract2DDrawing(1, 2)` doesn't compile.
- A method with no body must be declared `abstract`.
- A class with **any** abstract method must be declared `abstract`.
- A subclass must implement **all** inherited abstract methods, or be declared `abstract` itself.

### Abstract class vs interface

The lecture sums it up as **"is-a" vs "can-do"**.

| | Abstract class | Interface |
|---|---|---|
| Idea | What something **is** | What something **can do** |
| Keyword | `extends` | `implements` |
| How many can a class use? | **One** | **Many** |
| Instance variables | ✅ | ❌ (only constants) |
| Constructors | ✅ | ❌ |
| Methods with bodies | ✅ normal methods | only `default` and `static` |
| Method visibility | any | always `public` |
| Can you use `new`? | ❌ | ❌ |

Use an abstract class when closely related classes **share data and code**. Use an interface when
**unrelated** classes need the same ability. A class can do both:
`class Dog extends Pet implements Swimmer`.

### Interfaces can extend interfaces

```java
public interface A { void methodA(); }
public interface B extends A { void methodB(); }   // B adds a requirement
```

A class that implements `B` must write **both** `methodA` and `methodB`.

---

## 10. `final` for classes and methods

In Lesson 5, `final` made a variable a constant. It also controls inheritance:

| | Meaning | Example use |
|---|---|---|
| `final` class | **cannot be extended** | `public final class BankAccount`: nobody can subclass it and override, say, the fee method to add money instead |
| `final` method | **cannot be overridden** | `public final boolean checkPassword(String p)`: subclasses are allowed, but can't change this method |

`String` is a final class.

**Methods called from a constructor should be `final`.** Otherwise a subclass could override them,
and the overridden version would run **during the superclass's constructor**, before the subclass
has been set up.

---

## Examples to run

| File | Shows |
|---|---|
| [`BankAccount.java`](examples/BankAccount.java) | A superclass with a constructor, `toString` and `equals` overridden |
| [`SavingsAccount.java`](examples/SavingsAccount.java), [`CheckingAccount.java`](examples/CheckingAccount.java) | Subclasses using `super(...)`, `super.method()` and `@Override` |
| [`Drawing.java`](examples/Drawing.java), [`Building.java`](examples/Building.java), [`Tree.java`](examples/Tree.java) | An abstract class with two subclasses |
| [`InheritanceDemo.java`](examples/InheritanceDemo.java) | Uses all of the above: polymorphism, `instanceof`, overloading |

Compile all: `javac *.java`, then run `java InheritanceDemo`.

---

## Summary

- `extends` creates an **"is-a"** relationship. A class has exactly **one** superclass, and `Object` is at the top.
- Subclasses inherit everything but **can't access `private`** members directly. `protected` allows access.
- **Polymorphism:** the variable's type decides which methods you can call; the object's real class
  decides which version runs.
- **Overriding** = same signature in a subclass (use `@Override`). **Overloading** = same name, different parameters.
- `super(...)` calls the parent's constructor (first line). `super.method()` calls the parent's version of a method.
- Override `toString` for readable output, and `equals(Object)` to compare contents.
- **Abstract classes** can't be created, can have abstract methods, and share code. Interface = can-do; abstract class = is-a.
- `final` class: can't be extended. `final` method: can't be overridden.

---

## Quiz

**Q1.** `Dog extends Animal`. Which of these are true?
(a) a Dog is an Animal, (b) an Animal is a Dog, (c) a Dog is an Object, (d) Dog can have two superclasses

<details><summary>Answer</summary>

(a) and (c) are true. An Animal isn't necessarily a Dog. A class has **exactly one** superclass, so (d) is false.
</details>

**Q2.** Why doesn't this compile?

```java
public class BankAccount {
    private double balance;
}

public class SavingsAccount extends BankAccount {
    public void addInterest() {
        balance = balance * 1.04;
    }
}
```

<details><summary>Answer</summary>

`balance` is `private` in `BankAccount`. `SavingsAccount` inherits it (every savings account *has* a
balance), but **can't access it directly**. Either use the superclass's methods
(`deposit(getBalance() * 0.04)`) or make `balance` `protected`.
</details>

**Q3.** What is printed?

```java
public class Animal {
    public String sound() { return "..."; }
}
public class Cat extends Animal {
    @Override
    public String sound() { return "Meow"; }
}

// in main:
Animal a = new Cat();
System.out.println(a.sound());
```

<details><summary>Answer</summary>

`Meow`. The variable is type `Animal`, but the object is really a `Cat`, so `Cat`'s overriding
`sound()` runs (polymorphism).
</details>

**Q4.** `Cat` also has a method `purr()` that `Animal` doesn't have. Does `a.purr()` compile, with
`a` from Q3? How would you call it?

<details><summary>Answer</summary>

No. Through an `Animal` variable you can only call `Animal`'s methods. Check and cast:

```java
if (a instanceof Cat) {
    Cat c = (Cat) a;
    c.purr();
}
```
</details>

**Q5.** Overloading or overriding?
(a) `print(int x)` and `print(String s)` in the same class
(b) `toString()` written in your `Student` class
(c) `Cat`'s `sound()` in Q3

<details><summary>Answer</summary>

(a) **Overloading**: same name, different parameters.
(b) **Overriding**: it replaces `Object`'s `toString()`.
(c) **Overriding**: it replaces `Animal`'s `sound()`.
</details>

**Q6.** What's wrong with this constructor?

```java
public class Vehicle {
    private int wheels;
    public Vehicle(int wheels) { this.wheels = wheels; }
}

public class Bike extends Vehicle {
    private String type;
    public Bike(String type) {
        this.type = type;
    }
}
```

<details><summary>Answer</summary>

`Bike`'s constructor doesn't call `super(...)`, so Java tries `super()` with no parameters, but
`Vehicle` has no no-parameter constructor. It doesn't compile. The fix:

```java
public Bike(String type) {
    super(2);              // must be the first line
    this.type = type;
}
```
</details>

**Q7.** In the lecture's `CheckingAccount`, why must `deductFee()` call `super.withdraw(...)` instead of `withdraw(...)`?

<details><summary>Answer</summary>

`CheckingAccount` overrides `withdraw` so that it calls `deductFee()`. If `deductFee()` called
`withdraw(...)`, that would be the **overridden** version, which calls `deductFee()` again, forever
(infinite recursion, ending in a crash). `super.withdraw(...)` calls `BankAccount`'s version, which
just takes the money, with no fee.
</details>

**Q8.** Why must the parameter of `equals` be type `Object`, and what's the first thing the method should do?

<details><summary>Answer</summary>

To **override** `Object`'s `equals(Object obj)`, the signature must match exactly. (With a different
parameter type, it would be an overload, and code like `ArrayList.contains` would still use the
default version.) The method should first check the type with `instanceof` (and return `false` if it
doesn't match) before casting and comparing fields.
</details>

**Q9.** Which of these compile?

```java
public abstract class Shape {
    public abstract double area();
}
public class Square extends Shape {
    private double w;
    public Square(double w) { this.w = w; }
}

Shape s1 = new Shape();
Shape s2 = new Square(3);
```

<details><summary>Answer</summary>

None of it works as written:
- `Square` doesn't compile: it extends `Shape` but doesn't implement the abstract method `area()`,
  and it isn't declared `abstract` itself.
- `new Shape()` doesn't compile: you can't create an abstract class.

Once `Square` implements `area()`, `Shape s2 = new Square(3);` is fine.
</details>

**Q10.** Give three differences between an abstract class and an interface.

<details><summary>Answer</summary>

Any three of: an abstract class can have **instance variables**, **constructors** and **normal
methods with bodies**, and its methods can have **any access level**; an interface has only
constants, no constructors, only `default`/`static` methods with bodies, and all its methods are
`public`. A class can extend only **one** abstract class but implement **many** interfaces. The
design idea is **"is-a"** (abstract class) vs **"can-do"** (interface).
</details>

**Q11.** What does `final` mean on (a) a variable, (b) a method, (c) a class?

<details><summary>Answer</summary>

(a) It can be assigned only once. (b) It **can't be overridden** by subclasses. (c) It **can't be extended**: no subclasses at all.
</details>

**Q12.** What is printed?

```java
Object o = new CheckingAccount(1002, "Sean", 100);
System.out.println(o instanceof BankAccount);
System.out.println(o instanceof Object);
System.out.println(o instanceof SavingsAccount);
```

<details><summary>Answer</summary>

```
true
true
false
```
A `CheckingAccount` is a `BankAccount` and an `Object`, but it isn't a `SavingsAccount` (they're
siblings, not parent and child). This compiles because the variable's type is `Object`, which
*could* hold a `SavingsAccount`.
</details>

---

[← Previous: Lesson 6](../06-interfaces/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 8, File IO and Exceptions →](../08-file-io-and-exceptions/README.md)
