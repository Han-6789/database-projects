package edu.virginia.cs;

import java.util.Scanner;
import java.util.*;

import org.hibernate.Session;

import javax.persistence.TypedQuery;
import java.util.logging.Level;

import org.hibernate.query.Query;

public class CommandLineDatabase {
    private Scanner scanner;
    Student student;

    //private Session session;
    public static void main(String[] args) {
        CommandLineDatabase commandLineDatabase = new CommandLineDatabase();
        commandLineDatabase.run();
    }

    public CommandLineDatabase() {
        student = new Student();
        scanner = new Scanner(System.in);
    }

    public void run() {
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();
        logInChoice();
        mainMenu();
        //session.close();
    }

    public void mainMenu() {
        //ask the user to choose the option
        System.out.println("Press key for options: \n (1) Write Review \n (2) Read Reviews \n (3) Log-out");
        //check if number was inputted
        try {
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1 -> submitReview();
                case 2 -> seeReview();
                case 3 -> logOut();
                default -> throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            System.out.println("please input a number (1-3)");
            mainMenu();
        }
    }

    public void submitReview() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        System.out.println("Enter the department and catalog number of the course like \"CS 3140\"");
        String input = scanner.nextLine();
        String[] inputArray = input.split(" ");
        String department = inputArray[0];
        String catalogNumber = inputArray[1];
        checkUserInputFormat(department, catalogNumber);

        //check if the course is in the Courses Table
        String courseQuery = "FROM Course WHERE department = :department AND catalogNumber = :catalogNumber";
        Query<Course> query = session.createQuery(courseQuery, Course.class);
        query.setParameter("department", department);
        query.setParameter("catalogNumber", Integer.parseInt(catalogNumber));
        List<Course> courseList = query.getResultList();
        session.getTransaction().commit();
        if (courseList.isEmpty()) {
            session.beginTransaction();
            System.out.println("The course you entered is not in the database.");
            //add the course to the Courses Table
            Course course = new Course();
            course.setDepartment(department);
            course.setCatalogNumber(Integer.parseInt(catalogNumber));
            //session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
            //session.close();
           // HibernateUtil.shutdown();
        }
//        session.close();
//        HibernateUtil.shutdown();
        //A user can only review a course once. If the user tries to review the same course again.
        //print an error message and return to the main menu.
        //check if the user has reviewed the course
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
//        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String reviewQuery = "FROM Review WHERE studentID = :student AND courseID = :course";
        Query<Review> query2 = session.createQuery(reviewQuery, Review.class);
        //get the student id, the student is the current user
        String studentQuery = "FROM Student WHERE loginName = :name";
        Query<Student> query3 = session.createQuery(studentQuery, Student.class);
        query3.setParameter("name", student.getLoginName());
        Student s = query3.getSingleResult();
        int id = s.getId();
        student.setId(id);
        query2.setParameter("student", id);
        query2.setParameter("course", courseList.get(0).getId());
        List<Review> reviewList = query2.getResultList();
        session.getTransaction().commit();

        if (!reviewList.isEmpty()) {
            System.out.println("You have already reviewed this course.");
            mainMenu();
            session.close();
            HibernateUtil.shutdown();
        } else {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            //if the user has not reviewed the course, prompt the user to enter a review and a rating
            System.out.println("Enter your review:");
            String review = scanner.nextLine();
            System.out.println("Enter your rating:");
            String rating = scanner.nextLine();
            //the rating must be an integer between 1 and 5
            try {
                int ratingInt = Integer.parseInt(rating);
                if (ratingInt < 1 || ratingInt > 5) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number between 1 and 5");
                submitReview();
            }
            //add the review to the Reviews Table
            Review review1 = new Review();
            review1.setStudent(student);
            review1.setCourse(courseList.get(0));
            review1.setMessage(review);
            review1.setRating(Integer.parseInt(rating));
            session.persist(review1);
            session.getTransaction().commit();
            session.close();
            HibernateUtil.shutdown();

        }

    }

    public void seeReview() {
        //Have the user write the course name like "CS 3140"
        System.out.println("Enter the department and catalog number of the course like \"CS 3140\" to see the reviews");
        String input = scanner.nextLine();
        String[] inputArray = input.split(" ");
        String department = inputArray[0];
        String catalogNumber = inputArray[1];
        checkUserInputFormat(department, catalogNumber);
        //If the user enters a valid course with no reviews, print an appropriate message and take them back to the main menu
        //Check whether the course has a review in the review table
        //If the course has reviews, print all the review messages for that course (one per line)
        //and print the average of the review numeric scores for that course in the format "Average rating: 4.5" and then back to main
        //If the course has no reviews, print an appropriate message and then back to main
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        //get the course id from the course table and then get the review from the review table
        String course = "SELECT c FROM Course c WHERE c.department = :department AND c.catalogNumber = :catalogNumber";
        Query<Course> courseQuery = session.createQuery(course, Course.class);
        courseQuery.setParameter("department", department);
        courseQuery.setParameter("catalogNumber", Integer.parseInt(catalogNumber));
        List<Course> courseList = courseQuery.getResultList();
        session.getTransaction().commit();
        //get the matched course ID
        int courseID = courseList.get(0).getId();
        //get the review from the review table
        session.beginTransaction();
        String review = "SELECT r FROM Review r WHERE r.course.id = :courseID";
        Query<Review> reviewQuery = session.createQuery(review, Review.class);
        reviewQuery.setParameter("courseID", courseID);
        List<Review> reviewList = reviewQuery.getResultList();
        session.getTransaction().commit();
        //check if the reviewList is empty
        if (reviewList.isEmpty()) {
            System.out.println("There is no review for this course");
        }
        //the reviewList is not empty
        else {
            //print all the review messages for that course (one per line)
            //and print the average of the review numeric scores for that course in the format "Average rating: 4.5" and then back to main
            System.out.println("The review messages for this course are:");
            for (Review r : reviewList) {
                System.out.println(r.getMessage());
            }
            //calculate the average of the review numeric scores for that course
            double sum = 0;
            for (Review r : reviewList) {
                sum += r.getRating();
            }
            double average = sum / reviewList.size();
            System.out.println("Average rating: " + average);
        }
        session.close();
        HibernateUtil.shutdown();
    }

    private void logOut() {
        //return to Login
        System.out.println("You have logged out");
        logInChoice();
    }

    public void checkUserInputFormat(String department, String catalogNumber) {
        //Have the user enter the department and catalog number of the course like "CS 3140"
        //There is a space, the thing before the space is the department and the thing after the space is the catalog number
        //All departments are Strings are of at most 4 capital letters. All numbers are 4 digits.
        //If the user enters an invalid String ("CompSci 101"), print error message and take them to mainMenu(
        if (department.length() > 4 || !department.matches("[A-Z]+")) {
            System.out.println("Invalid department. Please try again.");
            mainMenu();
        }
        if (catalogNumber.length() != 4 || !catalogNumber.matches("[0-9]+")) {
            System.out.println("Invalid catalog number. Please try again.");
            mainMenu();
        }

    }

    private void logInChoice() {
        //When someone opens the app, they should be shown the below two options:
        //1. Log in to an existing user. This prompts for a login name and password, (stored in table Students)
        //2. Create a new user. This prompts for a login name and password and confirms password.
        //If the passwords match, add the user to the table Students and log-in. If the passwords don't match,
        //do not add anything to the Students table, and take the user back to 1)Login
        System.out.println("Welcome to the UVA Course Review Database!");
        System.out.println("1. Log in to an existing user");
        System.out.println("2. Create a new user");
        //ask the user to choose one of the two options
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            logInExistingUser();
        } else if (choice == 2) {
            longInNewUser();
        } else {
            System.out.println("Invalid choice. Please try again.");
            logInChoice();
        }
    }

    private void longInNewUser() {
        //ask the user to enter the login name and password
        System.out.println("Please enter your login name:");
        String loginName = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Please confirm your password:");
        String confirmPassword = scanner.nextLine();
        //check if the password and confirm password are the same
        if (password.equals(confirmPassword)) {
            //if the password and confirm password are the same, add the user to the table Students and log-in
            //add the user to the table Students
            //log-in
            System.out.println("You have successfully created a new user!");
            addToTableStudent(loginName, password);
        } else {
            //if the password and confirm password are not the same, do not add anything to the Students table,
            //and take the user back to 1)Login
            System.out.println("The password and confirm password are not the same. Please try again.");
            logInChoice();
        }
    }

    private void addToTableStudent(String name, String pass) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        student = new Student();
        student.setLoginName(name);
        student.setPassword(pass);
        session.persist(student);
        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

    private void logInExistingUser() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Enter login name and password
        System.out.println("Enter login name:");
        String loginName = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        student.setLoginName(loginName);
        student.setPassword(password);
        session.beginTransaction();
        String student = "SELECT s FROM Student s WHERE s.loginName = :loginName AND s.password = :password";
        Query<Student> studentQuery = session.createQuery(student, Student.class);
        studentQuery.setParameter("loginName", loginName);
        studentQuery.setParameter("password", password);
        List<Student> studentList = studentQuery.getResultList();
        session.getTransaction().commit();
        session.close();
//        HibernateUtil.shutdown();

        if (studentList.isEmpty()) {
            System.out.println("The login name and password are not correct. Please try again.");
            logInChoice();
        } else {
            System.out.println("You have successfully logged in!");
            mainMenu();
        }
    }


}