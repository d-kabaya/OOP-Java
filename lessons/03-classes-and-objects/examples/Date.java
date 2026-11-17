/**
 * Lesson 3: the Date class from the lecture.
 */
public class Date {

    // Instance variables: every Date object has its own copy of these
    private int day;
    private int month;
    private int year;

    // Constructor: runs once, when "new Date(...)" is called
    public Date(int day, int month, int year) {
        this.day = day;        // this.day is the field, day is the parameter
        this.month = month;
        this.year = year;
    }

    // A second constructor (overloading): 1st January of the given year
    public Date(int year) {
        this(1, 1, year);      // calls the constructor above
    }

    // A mutator: changes the object's data
    public void incrementYear() {
        year++;                // same as this.year++
    }

    // Compares THIS date with another date d
    public boolean sameDate(Date d) {
        return this.day == d.day && this.month == d.month && this.year == d.year;
    }

    // Called automatically by println and by string concatenation
    public String toString() {
        return day + "/" + month + "/" + year;
    }
}
