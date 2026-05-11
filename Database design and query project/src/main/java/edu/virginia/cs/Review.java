package edu.virginia.cs;

import javax.persistence.*;

//Reviews:
//        An id number [Auto increment primary key]
//        A foreign-key to the Students Table (the student who wrote the review)
//        A foreign-key to the Courses Table (the course the review is about)
//        A text message from the student reviewing the course
//        A rating from 1-5 (integer) of the course. Your table should disallow other values
//        Modification: A check constraint is encouraged, but not required, so long as your course review program never inserts an invalid number into the table.
@Entity
@Table(name = "REVIEWS")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    //A foreign-key to the Students Table (the student who wrote the review)
    //MapToOne, a student can have many reviews
    @ManyToOne
    @JoinColumn(name = "StudentID",referencedColumnName = "ID")
    private Student student;

    //A foreign-key to the Courses Table (the course the review is about)
    //A course can have many reviews
    @ManyToOne
    @JoinColumn(name = "CourseID",referencedColumnName = "ID")
    private Course course;

    //A text message from the student reviewing the course
    @Column(name = "Message", nullable = false)
    private String message;

    //A rating from 1-5 (integer) of the course. Your table should disallow other values
    //Modification A check constraint is encouraged, but not required,
    // so long as your course review program never inserts an invalid number into the table.
    @Column(name = "Rating", nullable = false)
    private int rating;

    public Review(Student student, Course course, String message, int rating) {
        this.student = student;
        this.course = course;
        this.message = message;
        this.rating = rating;
    }

    public Review() {
        //a zero-argument constructor is required for hibernate to work correctly

    }

    //all fields must have public getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    //
}

