package edu.virginia.cs.hw4;
import java.util.HashMap;
import java.util.Map;

public class Transcript {
    Student student;
    Map<Course, Grade> courseHistory;

    public Transcript(Student student) {
        this.student = student;
        courseHistory = new HashMap<>();
    }

    public Transcript(Student student, Map<Course,Grade> courseHistory) {
        this.student = student;
        this.courseHistory = courseHistory;
    }

    public void addCourseGrade(Course course, Grade grade) {
        courseHistory.put(course, grade);
    }

    public Grade getCourseGrade(Course course) {
        //to check whether the course is in the courseHistory
        return courseHistory.get(course);
    }

    public Map<Course, Grade> getCourseHistory() {
        return courseHistory;
    }

    public void setCourseHistory(Map<Course, Grade> courseHistory) {
        this.courseHistory = courseHistory;
    }

    public double getGPA() {
        if (courseHistory.size() == 0) {
            throw new IllegalStateException("No courses taken, cannot get GPA");
        }
        double totalGradePoints = 0.0;
        int creditsAttempted = 0;
        for (Course course : courseHistory.keySet()) {
            Grade grade = courseHistory.get(course);
            int credits = course.getCreditHours();
            totalGradePoints += grade.gpa * credits;
            creditsAttempted += credits;
        }
        return totalGradePoints / creditsAttempted;

    }

    public boolean hasStudentTakenCourse(Course course) {
        for (Course c : courseHistory.keySet()) {
            if (course.getDepartment().equals(c.getDepartment()) && course.getCatalogNumber() == c.getCatalogNumber()){
                return true;
            }
        }
        return false;
    }


    public boolean meetsPrerequisite(Prerequisite prerequisite) {
        Grade studentGrade = courseHistory.get(prerequisite.course);
        return studentGrade.gpa >= prerequisite.minimumGrade.gpa;
    }

}




