package edu.virginia.cs;

import javax.persistence.*;
import java.util.List;

//Students containing the following
//An id number [Auto increment primary key]
//A name used for login (could be computing ID) - must be unique
//A password - a note that you'll be saving the password as plain text,
//        but this is generally "bad practice" - this was simplified because handling
//        that security is beyond the scope of this course.
@Entity
@Table(name = "STUDENTS")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "ID")
    private int id;

    @Column(name="LoginName", unique = true, nullable = false, length = 32)
    private String loginName;

    @Column(name = "Password", nullable = false)
    private String password;

    //add OneToMany, a student can have many reviews and many courses
   @OneToMany(mappedBy = "student")
    private List<Review> reviews;

    public Student(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }


    //The constructor contains all fields except id, which is auto-generated
    public Student(String loginName, String password, List<Review> reviews) {
        this.loginName = loginName;
        this.password = password;
        this.reviews = reviews;
    }

    public Student() {
        //a zero-argument constructor is required for hibernate to work correctly
    }

    //all fields must have public getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //add getter and setter for reviews


}

