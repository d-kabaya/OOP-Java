# Lesson 10: Threading and Concurrency

**Based on:** Lecture 10 (Threading)
**Time needed:** about 1 hour

## What you will learn

By the end of this lesson you should be able to:

- explain what a thread is, why we use threads, and what can go wrong
- create threads in Java in **two** ways, and explain why there are two
- describe the **thread lifecycle**, and use `start`, `run`, `sleep` and `join`
- explain a **race condition**, and fix one with `synchronized` methods or blocks
- explain locks, `wait` / `notifyAll`, **deadlock** and `InterruptedException`
- split a calculation across several threads

---

## 1. What is a thread?

When a program runs, it's a **process**. A **thread** is a **single sequence of steps** being
carried out inside that process, also called a *thread of execution*. Every program you've written
so far had one thread: the one running `main`.

A process can create **several** threads, which then run **at the same time**. That's a
**multithreaded** process.

**Analogy:** a kitchen (the process) with several cooks (threads). They share the same kitchen and
ingredients (the process's memory), and each works through their own recipe (sequence of steps) at
the same time as the others.

### Why use threads?

- **Parallel computation:** several workers each process part of the data.
- **Shared memory:** threads in the same process can all use the same objects.
- **Responsiveness and speed:** a program can keep responding while work happens in the
  background, and finish big jobs sooner.
- **Use all the CPU cores:** modern computers have several cores; one thread can only use one.

### Words of warning

Concurrency is **hard**. The lecture's list of pitfalls:

| Problem | Meaning |
|---|---|
| **Race condition** | Threads use shared data at the same time, and the result depends on timing |
| **Deadlock** | Threads are each waiting for the other, so **none** of them runs |
| **Livelock** | Threads keep running, but keep reacting to each other and never make progress |
| **Thread safety** | Shared data needs **access control**: locks and synchronisation |
| **Debugging** | Bugs may appear only sometimes, depending on timing, which makes them very hard to find |

---

## 2. Two ways to create a thread

### Way 1: extend `Thread` and override `run()`

```java
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);                                  // Thread's constructor: give it a name
    }

    @Override
    public void run() {                               // the work this thread does
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + ": " + i);
            try {
                sleep(100);                           // pause for 100 milliseconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

```java
public static void main(String[] args) throws InterruptedException {
    Thread t1 = new MyThread("MyThread 1");
    Thread t2 = new MyThread("MyThread 2");

    t1.start();          // both threads now run AT THE SAME TIME
    t2.start();

    t1.join();           // main waits here until t1 has finished
    t2.join();           // ...and until t2 has finished
}
```

The output of the two threads is **interleaved**, and the order can be **different every time** you run it.

### Way 2: implement `Runnable`, and pass it to a `Thread`

```java
public class CounterTask implements Runnable {
    private Counter counter;

    public CounterTask(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
    }
}

Thread t = new Thread(new CounterTask(counter));
t.start();
```

`Runnable` has exactly one abstract method, `run()`, so it's a **functional interface**, and you can
write the task as a **lambda** (Lesson 9):

```java
Runnable task = () -> {
    for (int i = 0; i < 1000; i++) {
        counter.increment();
    }
};
Thread t = new Thread(task);
```

### Why two ways?

Java has **no multiple inheritance**: a class can extend only one class. If your class already
extends something else, it **can't** also extend `Thread`. But it can always **implement**
`Runnable`, because a class can implement any number of interfaces (Lessons 6 and 7).

| | `extends Thread` | `implements Runnable` |
|---|---|---|
| Your class is... | a thread | a task that a thread runs |
| Can it extend another class too? | ❌ | ✅ |
| Can it be a lambda? | ❌ | ✅ |
| Usually preferred? | | ✅ more flexible |

---

## 3. `start()` vs `run()`, and the other main methods

| Method | What it does |
|---|---|
| `run()` | the **body** of the thread: the work it does. You write it; you don't usually call it |
| `start()` | **schedules** the thread. At some point the new thread calls `run()` |
| `sleep(ms)` | pauses the **current** thread for that many milliseconds |
| `join()` | the current thread **waits** until the other thread has **finished** |
| `wait()` / `notify()` / `notifyAll()` | wait until some condition is reached, and wake waiting threads (section 5) |

`stop`, `suspend` and `resume` used to exist, but they're deprecated and being removed.

**Calling `run()` instead of `start()` is a classic mistake.** `t.run()` just runs the method in the
**current** thread, like any normal method call: no new thread is created, and nothing happens in
parallel.

---

## 4. The thread lifecycle

`Thread` has a static `State` enum. At any moment, a thread is in **exactly one** of these states:

```
 NEW ──start()──► RUNNABLE ──run() finishes──► TERMINATED
                   │  ▲
   waiting for a   │  │  got the lock /
   lock            ▼  │  woken up / time's up
              BLOCKED, WAITING, TIMED_WAITING
```

| State | Meaning |
|---|---|
| **New** | created, but `start()` hasn't been called yet |
| **Runnable** | ready to run, and running or waiting for CPU time |
| **Blocked** | waiting to get a **lock**, to enter a `synchronized` method or block |
| **Waiting** | waiting with no time limit: `wait()` or `join()` |
| **Timed waiting** | waiting for a set time: `sleep(ms)` or `wait(ms)` |
| **Terminated** | `run()` has finished |

---

## 5. Race conditions and `synchronized`

### The problem

```java
class Counter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

Two threads each call `increment()` 1000 times on the **same** `Counter`:

```java
Thread thread1 = new Thread(task);
Thread thread2 = new Thread(task);
thread1.start();
thread2.start();
thread1.join();
thread2.join();
System.out.println("Final count: " + counter.getCount());   // should be 2000... but is it?
```

It's often **less than 2000**, and different each time. Why?

`count++` looks like one step, but it's really **three**: **read** `count`, **add 1**, **write** it
back. Two threads can interleave those steps:

| Thread 1 | Thread 2 | `count` |
|---|---|---|
| reads 5 | | 5 |
| | reads 5 | 5 |
| adds 1 → 6 | | 5 |
| | adds 1 → 6 | 5 |
| writes 6 | | 6 |
| | writes 6 | **6**, but it should be 7 |

One increment is **lost**. This is a **race condition**: the result depends on the timing of the threads.

### Fix 1: a `synchronized` method

```java
public synchronized void increment() {
    count++;
}
```

Now **only one thread at a time** can be inside `increment()` on this object. The other waits its
turn, so no increment is lost, and the result is always **2000**.

### Locks

- **Every object has a lock.**
- When a thread enters a `synchronized` method or block, it **acquires the lock** of the object it
  synchronises on, and **holds it** until it leaves.
- Any other thread that tries to get the same lock is **blocked** until it's released.

### Fix 2: a `synchronized` block

What if you **can't change** the `Counter` class? Synchronise in the task instead:

```java
Runnable task = () -> {
    for (int i = 0; i < 1000; i++) {
        synchronized (counter) {         // take counter's lock
            counter.increment();
            counter.notifyAll();         // tell any waiting threads the lock is free
        }                                // lock released here
    }
};
```

A `synchronized` block gives **finer control**: you lock only the code that needs it, on the object
you choose. Otherwise it has the same effect as a synchronized method.

`wait()`, `notify()` and `notifyAll()` are methods every class inherits from **`Object`** (Lesson 7).
`notifyAll()` tells threads waiting on that object that they can try to claim the lock. The lecture
warns: *forget it and expect deadlock situations to (potentially) arise.*

### Deadlock

A **deadlock** happens when threads are waiting for each other, so **none** of them can continue.
For example: thread A holds lock 1 and waits for lock 2, while thread B holds lock 2 and waits for
lock 1. Both wait forever.

---

## 6. Key exceptions and errors

### `InterruptedException`

Thrown when a thread is **interrupted** while it's **sleeping, waiting or otherwise suspended**.
It's a **checked** exception (Lesson 8), so any code that calls `sleep`, `join` or `wait` must
**catch it or declare it** with `throws`:

```java
public static void main(String[] args) throws InterruptedException {
    t1.join();
}
```
or
```java
try {
    Thread.sleep(500);
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
```

### `OutOfMemoryError`

If you create **too many threads**, together they can use up all the memory the JVM has. This is an
**error**, not an exception: you can't recover from it, and the JVM stops, not gracefully.

---

## 7. Splitting a calculation across threads

Some problems split naturally into **independent chunks**. Each thread works on one chunk, and the
results are combined at the end. These are called **"embarrassingly parallel"** problems.

The lecture's example is a factorial: n! = 1 × 2 × ... × n. With 2 threads:

```
thread 1:  1 × 2 × ... × (n/2 − 1)
thread 2:  (n/2) × ... × n
answer  =  thread 1's result × thread 2's result
```

You need to define two things:
1. **the computation**: a task that multiplies a range of numbers (a `Runnable`)
2. **the management**: create the threads, `start` them, `join` them, combine the results

See [`ParallelFactorial.java`](examples/ParallelFactorial.java), and Lab 11.

### Going further (assignment extension ideas from the lecture)

- the `java.util.concurrent` package
- `Callable`, `Executors` and `Future`
- design patterns such as **Producer-Consumer**

---

## Examples to run

| File | Shows |
|---|---|
| [`MyThread.java`](examples/MyThread.java), [`ThreadDemo.java`](examples/ThreadDemo.java) | Way 1 (`extends Thread`), Way 2 (a `Runnable` lambda), `start` vs `run`, `join`, thread states |
| [`Counter.java`](examples/Counter.java), [`RaceConditionDemo.java`](examples/RaceConditionDemo.java) | A race condition, then both fixes: a `synchronized` method and a `synchronized` block |
| [`FactorialTask.java`](examples/FactorialTask.java), [`ParallelFactorial.java`](examples/ParallelFactorial.java) | Splitting 20! across two threads |

Compile all: `javac *.java`, then run `java ThreadDemo`, `java RaceConditionDemo` or `java ParallelFactorial`.
Thread output can come out in a **different order each run**: that's expected.

---

## Summary

- A **thread** is one sequence of steps in a process. Several threads run at the same time and share memory.
- Create one by **extending `Thread`** or **implementing `Runnable`** (more flexible, and can be a lambda).
  There are two ways because Java has no multiple inheritance.
- `start()` creates the new thread, which calls `run()`. Calling `run()` yourself creates **no** thread.
  `join()` waits for a thread to finish; `sleep()` pauses.
- States: **New, Runnable, Blocked, Waiting/Timed waiting, Terminated**.
- A **race condition** happens when threads change shared data at the same time. Fix it with a
  `synchronized` method or a `synchronized (object) { ... }` block, which use the object's **lock**.
- `InterruptedException` is checked. `OutOfMemoryError` from too many threads can't be recovered from.

---

## Quiz

**Q1.** What is a thread, and give two reasons to use more than one?

<details><summary>Answer</summary>

A thread is a single sequence of steps being carried out inside a process. Reasons include: running
computations **in parallel** on several cores; keeping a program **responsive** while work happens in
the background; finishing large jobs **faster**; sharing memory between workers.
</details>

**Q2.** What are the two ways to create a thread in Java, and why does Java provide both?

<details><summary>Answer</summary>

1. Extend `Thread` and override `run()`.
2. Implement `Runnable` (write `run()`), and pass the object to `new Thread(...)`.

Java has **no multiple inheritance**, so a class that already extends another class can't extend
`Thread`. It can always implement `Runnable`, because a class can implement many interfaces.
</details>

**Q3.** What's the difference between calling `t.start()` and `t.run()`?

<details><summary>Answer</summary>

`t.start()` creates a **new thread**, which then calls `run()`, so the work happens **in parallel**.
`t.run()` is just a normal method call: the code runs in the **current** thread, and no new thread is created.
</details>

**Q4.** What does `t1.join()` do?

<details><summary>Answer</summary>

The current thread (for example, `main`) **waits** at that line until thread `t1` has **finished**.
It's used so you don't read results before the threads have produced them.
</details>

**Q5.** Name the thread states, and say which state a thread is in while it's waiting inside `sleep(500)`.

<details><summary>Answer</summary>

**New, Runnable, Blocked, Waiting, Timed waiting, Terminated.** During `sleep(500)` it's in **Timed waiting**,
because it's waiting for a set amount of time.
</details>

**Q6.** Two threads each call `counter.increment()` (which does `count++`) 1000 times, without
synchronisation. Why is the final count often less than 2000?

<details><summary>Answer</summary>

`count++` is three steps: read, add 1, write. If both threads read the same value before either
writes, they both write the same new value, and one increment is **lost**. This is a **race condition**:
the result depends on the timing of the threads.
</details>

**Q7.** Give two ways to fix the race condition in Q6.

<details><summary>Answer</summary>

1. Make the method synchronized: `public synchronized void increment()`.
2. Use a synchronized block where it's called: `synchronized (counter) { counter.increment(); }`.

Either way, only one thread at a time can run the increment, because it must hold the counter's **lock**.
</details>

**Q8.** Why can `Runnable` be written as a lambda, but `Thread` can't?

<details><summary>Answer</summary>

`Runnable` is an **interface** with exactly one abstract method, `run()`, which makes it a functional
interface. `Thread` is a **class**, and lambdas can only implement functional interfaces.
</details>

**Q9.** Why does this not compile, and how do you fix it?

```java
public static void main(String[] args) {
    Thread t = new Thread(() -> System.out.println("Hi"));
    t.start();
    t.join();
}
```

<details><summary>Answer</summary>

`join()` can throw `InterruptedException`, which is a **checked** exception, so it must be caught or
declared. Add `throws InterruptedException` to `main`, or wrap `t.join()` in a `try`/`catch`.
</details>

**Q10.** What is a deadlock?

<details><summary>Answer</summary>

A situation where threads are each **waiting for the other** (for example, each holding a lock the
other needs), so none of them can continue, and the program hangs forever.
</details>

**Q11.** What is printed? Could the order change between runs?

```java
Thread t = new Thread(() -> System.out.println("A"));
t.start();
t.join();
System.out.println("B");
```

<details><summary>Answer</summary>

```
A
B
```
The order is **always** the same, because `t.join()` makes `main` wait until `t` (which prints "A")
has finished before printing "B". Without the `join()`, the order could vary.
</details>

**Q12.** How would you split the calculation of 10! across two threads?

<details><summary>Answer</summary>

Give each thread half of the numbers to multiply. For example, thread 1 computes 1 × 2 × 3 × 4
(= 24), and thread 2 computes 5 × 6 × 7 × 8 × 9 × 10 (= 151200). `start` both, `join` both, then
multiply the two results: 24 × 151200 = 3628800. That's an "embarrassingly parallel" problem, because
the halves don't depend on each other.
</details>

---

[← Previous: Lesson 9](../09-lambdas-and-streams/README.md) · [Back to all lessons](../../README.md)
