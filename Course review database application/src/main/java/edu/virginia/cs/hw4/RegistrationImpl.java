package edu.virginia.cs.hw4;
import java.net.http.HttpResponse;
import java.util.List;

public class RegistrationImpl implements Registration {
    private CourseCatalog courseCatalog;

    public RegistrationImpl(CourseCatalog courseCatalog){
        this.courseCatalog = courseCatalog;
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }

    @Override
    public void setCourseCatalog(CourseCatalog courseCatalog) {
        this.courseCatalog = courseCatalog;
    }

    @Override
    public boolean isEnrollmentFull(Course course) {
        return course.getEnrollmentCap() <= course.getCurrentEnrollmentSize();
    }

    @Override
    public boolean isWaitListFull(Course course) {
        return course.getWaitListCap() <= course.getCurrentWaitListSize();
    }

    @Override
    public Course.EnrollmentStatus getEnrollmentStatus(Course course) {
//        if (isEnrollmentFull(course)) {
//            if (isWaitListFull(course)) {
//                return Course.EnrollmentStatus.CLOSED;
//            } else {
//                return Course.EnrollmentStatus.WAIT_LIST;
//            }
//        } else {
//            return Course.EnrollmentStatus.OPEN;
//        }
        return course.getEnrollmentStatus();
    }

    @Override
    public boolean areCoursesConflicted(Course first, Course second) {
        //Check if the two courses are at the same time, use method getMeetingTime(). However,
        //the method getMeetingDays() returns a list of days, so we need to check each day.
        //For example, if the first course is on Monday and Wednesday, we need to check if the
        //second course is on Monday and Wednesday. If they are on the same day, we need to check
        //if the time is conflicted calling method checkCourseConflictOnSameDay().
        //If the two courses are conflicted on the same day, we return true.
        //If the two courses are not conflicted on the same day, we continue to check the next day.
        //If the two courses are not conflicted on any day, we return false.
        for (java.time.DayOfWeek day : first.getMeetingDays()) {
            if (second.getMeetingDays().contains(day)) {
                if (checkCourseConflictOnSameDay(first, second)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCourseConflictOnSameDay(Course first, Course second) {
        int firstStartHour = first.getMeetingStartTimeHour(); //13
        int firstStartMinute = first.getMeetingStartTimeMinute(); //30
        int firstDuration = first.getMeetingDurationMinutes(); //75
        int firstEndHour = firstStartHour + (firstDuration + firstStartMinute) / 60;  //14 = 13 + (75 + 30) / 60
        int firstEndMinute = (firstDuration + firstStartMinute) % 60; //45 = (75 + 30) % 60
        int secondStartHour = second.getMeetingStartTimeHour();
        int secondStartMinute = second.getMeetingStartTimeMinute();
        int secondDuration = second.getMeetingDurationMinutes();
        int secondEndHour = secondStartHour + (secondDuration + secondStartMinute) / 60;
        int secondEndMinute = (secondDuration + secondStartMinute) % 60;
        if(firstStartHour > secondEndHour) {
            return false;
        }
        else if(firstStartHour == secondEndHour && firstStartMinute >= secondEndMinute) {
            return false;
        }
        else if(firstEndHour < secondStartHour) {
            return false;
        }
        else if(firstEndHour == secondStartHour && firstEndMinute <= secondStartMinute) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasConflictWithStudentSchedule(Course course, Student student) {
        //check whether two courses have any overlapping time.
        //Check all the courses the students have enrolled and check whether any of them has a conflict with the course
        //the student wants to enroll

        for(Course c : student.getCourseHistory().keySet()) {
            if(areCoursesConflicted(c, course)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean hasStudentMeetsPrerequisites(Student student, List<Prerequisite> prerequisites) {
        for (Prerequisite p : prerequisites) {
            if (!student.meetsPrerequisite(p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RegistrationResult registerStudentForCourse(Student student, Course course) {

        if (!hasStudentMeetsPrerequisites(student, course.getPrerequisites())) {
            return RegistrationResult.PREREQUISITE_NOT_MET;
        }
        if (hasConflictWithStudentSchedule(course, student)) {
            return RegistrationResult.SCHEDULE_CONFLICT;
        }

        if (isEnrollmentFull(course) && isWaitListFull(course)) {
                return RegistrationResult.COURSE_FULL;
        }

        if (getEnrollmentStatus(course) == Course.EnrollmentStatus.CLOSED){
            return RegistrationResult.COURSE_CLOSED;
        }

        if (isEnrollmentFull(course) && !isWaitListFull(course)){
            course.addStudentToWaitList(student);
            if(isWaitListFull(course)){
                course.setEnrollmentStatus(Course.EnrollmentStatus.CLOSED);
            }
            return RegistrationResult.WAIT_LISTED;
        }

        if (!isEnrollmentFull(course)){
            course.addStudentToEnrolled(student);
            if(isEnrollmentFull(course)){
                course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
            }
            return RegistrationResult.ENROLLED;
        }

        return null;
//            else {
//                course.addStudentToWaitList(student);
//                if(isWaitListFull(course)){
//                    course.setEnrollmentStatus(Course.EnrollmentStatus.CLOSED);
//                }
//                return RegistrationResult.WAIT_LISTED;
//            }
//        } else {
//                course.addStudentToEnrolled(student);
//                if(isEnrollmentFull(course)){
//                    course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
//                }
//                return RegistrationResult.ENROLLED;
//        }
    }


    /**
     * Attempts to remove the student from the course enrollment OR the wait list
     *
     * If the student is enrolled and the course is in WAIT_LIST mode, then first student on the wait list
     * should be moved into the enrolled list. If the wait list is empty, then the state course enrollmentStatus
     * should be changed from WAIT_LIST to OPEN
     *
     * If the course enrollmentStatus is CLOSED, it should be changed to WAIT_LIST after the student is removed
     *
     * @throws IllegalArgumentException if Student is not in the course - that is neither enrolled nor wait listed)
     */
    @Override
    public void dropCourse(Student student, Course course) {
        if (!course.isStudentEnrolled(student) && !course.isStudentWaitListed(student)){
            throw new IllegalArgumentException("Student not enrolled in course");
        }

        if (course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST && course.getCurrentWaitListSize() == 0){
            course.removeStudentFromEnrolled(student);
            course.setEnrollmentStatus(Course.EnrollmentStatus.OPEN);
            return;
        }

        if (course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST && course.getCurrentWaitListSize() > 0){
            course.removeStudentFromEnrolled(student);
            Student firstStudentOnWaitList = course.getFirstStudentOnWaitList();
            course.removeStudentFromWaitList(firstStudentOnWaitList);
            course.addStudentToEnrolled(firstStudentOnWaitList);
            return;
        }

        if (course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED){
            course.removeStudentFromEnrolled(student);
            Student firstStudentOnWaitList = course.getFirstStudentOnWaitList();
            course.removeStudentFromWaitList(firstStudentOnWaitList);
            course.addStudentToEnrolled(firstStudentOnWaitList);
            course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
            return;
        }

        if (course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.OPEN){
            course.removeStudentFromEnrolled(student);
            return;
        }

        if (course.isStudentWaitListed(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED){
            course.removeStudentFromWaitList(student);
            course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
            return;
        }

        if(course.isStudentWaitListed(student)){
           course.removeStudentFromWaitList(student);
           return;
        }
    }
}