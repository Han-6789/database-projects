package edu.virginia.cs;

import javax.persistence.*;
import java.util.List;

//Courses: note that these fields are only for a course, not individual sections.
//        An id number [Auto increment primary key]
//        Department (such as "CS")
//        Catalog_Number (such as "3140")
@Entity
@Table(name = "COURSES")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Column(name = "Department", nullable = false, length = 32)
    private String department;

    @Column(name = "Catalog_Number", nullable = false)
    private int catalogNumber;

    //add OneToMany, a course can have many reviews
    @OneToMany(mappedBy = "course")
    private List<Review> reviews;

    public Course(String department, int catalogNumber) {
        this.department = department;
        this.catalogNumber = catalogNumber;
    }

    public Course() {
        //a zero-argument constructor is required for hibernate to work correctly
    }

    //all fields must have public getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    //add getter and setter for reviews
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
