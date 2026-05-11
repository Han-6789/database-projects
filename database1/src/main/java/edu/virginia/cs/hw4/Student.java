package edu.virginia.cs.hw4;
import java.util.*;

public class Student {
    private int studentNumber;
    private String name;
    private String email;
    private Transcript transcript;

    public Student(int studentNumber, String name, String email) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.transcript = new Transcript(this);
    }

    public Student(int studentNumber, String name, String email, Transcript transcript) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.transcript = transcript;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Transcript getTranscript() {
//        return transcript;
//    }

    public Map<Course, Grade> getCourseHistory(){
        return transcript.courseHistory;
    }

    public void addCourseGrade(Course course, Grade grade) {
        transcript.addCourseGrade(course, grade);
    }

    public boolean hasStudentTakenCourse(Course course) {
        return transcript.hasStudentTakenCourse(course);
    }

    public Grade getCourseGrade(Course course) {
        if (hasStudentTakenCourse(course)) {
            return transcript.getCourseGrade(course);
        }
        throw new IllegalArgumentException("ERROR: Student has no grade for " + course);
    }

    public boolean meetsPrerequisite(Prerequisite prerequisite) {
        if (hasStudentTakenCourse(prerequisite.course)){
            return transcript.meetsPrerequisite(prerequisite);
        }
        return false;
    }

    public double getGPA() {
        return transcript.getGPA();
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Student otherStudent) {
            return this.studentNumber == otherStudent.studentNumber;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return studentNumber;
    }
}
