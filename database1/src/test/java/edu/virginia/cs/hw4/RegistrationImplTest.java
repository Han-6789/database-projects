package edu.virginia.cs.hw4;
import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class RegistrationImplTest {

    private Course mockCourse1;
    private Course mockCourse2;
    private Course mockCourse3;
    private Prerequisite mockPrerequisite1;
    private Prerequisite mockPrerequisite2;
    private Student mockStudent1;
    private CourseCatalog mockCourseCatalog;
    private RegistrationImpl testRegistrationImpl;
    private Map<Course,Grade> mockCourseHistory;
    private List<Prerequisite> testPrerequisites;

    @BeforeEach
    public void setUp(){
        mockCourse1 = mock(Course.class);
        mockCourse2 = mock(Course.class);
        mockCourse3 = mock(Course.class);
        mockPrerequisite1 = mock(Prerequisite.class);
        mockPrerequisite2 = mock(Prerequisite.class);
        mockStudent1 = mock(Student.class);
        mockCourseCatalog = mock(CourseCatalog.class);
        mockCourseHistory = (Map<Course,Grade>) mock(Map.class);
        testRegistrationImpl = new RegistrationImpl(mockCourseCatalog);
    }

    @Test
    public void testIsEnrollmentFull_Full(){
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(200);
        assertTrue(testRegistrationImpl.isEnrollmentFull(mockCourse1));
    }

    @Test
    public void testIsEnrollmentFull_notFull(){
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(199);
        assertFalse(testRegistrationImpl.isEnrollmentFull(mockCourse1));
    }

    @Test
    public void testIsWaitlistFull_Full(){
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(100);
        assertTrue(testRegistrationImpl.isWaitListFull(mockCourse1));
    }

    @Test
    public void testIsWaitlistFull_notFull(){
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(99);
        assertFalse(testRegistrationImpl.isWaitListFull(mockCourse1));
    }

    @Test
    public void testGetEnrollmentStatus_Closed(){
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        assertEquals(Course.EnrollmentStatus.CLOSED, testRegistrationImpl.getEnrollmentStatus(mockCourse1));
    }

    @Test
    public void testGetEnrollmentStatus_Open(){
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        assertEquals(Course.EnrollmentStatus.OPEN, testRegistrationImpl.getEnrollmentStatus(mockCourse1));
    }

    @Test
    public void testGetEnrollmentStatus_Waitlist(){
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        assertEquals(Course.EnrollmentStatus.WAIT_LIST, testRegistrationImpl.getEnrollmentStatus(mockCourse1));
    }

    @Test
    public void testAreCoursesConflicted_Conflicted_Equivalence_Case(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertTrue(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_Conflicted_Boundary_Case(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(59);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertTrue(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_NotConflicted_Equivalence_Case(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(20);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(18);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertFalse(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_NOTConflicted_Equivalence_Case2(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertFalse(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_NOTConflicted_Equivalence_Case3(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertFalse(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_NOTConflicted_Equivalence_Case4(){
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertFalse(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testAreCoursesConflicted_NotConflicted_Boundary_Case(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(20);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(18);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertFalse(testRegistrationImpl.areCoursesConflicted(mockCourse1,mockCourse2));
    }

    @Test
    public void testHasConflictedWithStudentSchedule_Conflicted_EquivalenceCase(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(59);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        assertTrue(testRegistrationImpl.hasConflictWithStudentSchedule(mockCourse1, mockStudent1));
    }

    @Test
    public void testHasConflictedWithStudentSchedule_Conflicted_BoundaryCase(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse1,mockCourse2));
        assertTrue(testRegistrationImpl.hasConflictWithStudentSchedule(mockCourse1, mockStudent1));
    }

    @Test
    public void testHasConflictedWithStudentSchedule_NotConflicted(){
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(20);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(18);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse3.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse3.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse3.getMeetingDurationMinutes()).thenReturn(180);
        when(mockCourse3.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2, mockCourse3));
        assertFalse(testRegistrationImpl.hasConflictWithStudentSchedule(mockCourse1, mockStudent1));
    }

    @Test
    public void testHasStudentMeetsPrerequisites_Met(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        assertTrue(testRegistrationImpl.hasStudentMeetsPrerequisites(mockStudent1,testPrerequisites));
    }

    @Test
    public void testHasStudentMeetsPrerequisites_NotMet(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(false);
        assertFalse(testRegistrationImpl.hasStudentMeetsPrerequisites(mockStudent1, testPrerequisites));
    }

    @Test
    public void testRegisterStudentForCourse_Prerequisite_Not_Met(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(false);
        assertEquals(RegistrationResult.PREREQUISITE_NOT_MET, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
    }


    @Test
    public void testRegisterStudentForCourse_Schedule_Conflict(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(59);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        assertEquals(RegistrationResult.SCHEDULE_CONFLICT, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
    }

    @Test
    public void testRegisterStudentForCourse_Course_Full(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(200);
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(100);
        assertEquals(RegistrationResult.COURSE_FULL, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
    }

    @Test
    public void testRegisterStudentForCourse_Course_Closed(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(200);
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(99);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        assertEquals(RegistrationResult.COURSE_CLOSED, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
    }

    @Test
    public void testRegisterStudentForCourse_Wait_Listed(){
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(200);
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(99);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        assertEquals(RegistrationResult.WAIT_LISTED, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
        verify(mockCourse1).addStudentToWaitList(mockStudent1);
//        verify(mockCourse1).setEnrollmentStatus(Course.EnrollmentStatus.CLOSED); (wanted yet cannot be invoked)
    }

    @Test
    public void testRegisterStudentForCourse_Enrolled(){
        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(199);
        when(mockCourse1.getWaitListCap()).thenReturn(100);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(0);
        testPrerequisites = List.of(mockPrerequisite1, mockPrerequisite2);
        when(mockCourse1.getPrerequisites()).thenReturn(testPrerequisites);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite1)).thenReturn(true);
        when(mockStudent1.meetsPrerequisite(mockPrerequisite2)).thenReturn(true);
        when(mockCourse1.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse1.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse1.getMeetingDurationMinutes()).thenReturn(60);
        when(mockCourse1.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(14);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(00);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(20);
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(mockStudent1.getCourseHistory()).thenReturn(mockCourseHistory);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
//        when(mockCourse1.getCurrentEnrollmentSize()).thenReturn(200);
//        when(mockCourse1.getEnrollmentCap()).thenReturn(200);
        assertEquals(RegistrationResult.ENROLLED, testRegistrationImpl.registerStudentForCourse(mockStudent1, mockCourse1));
        verify(mockCourse1).addStudentToEnrolled(mockStudent1);
//        verify(mockCourse1).setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
    }

    @Test
    public void testDropCourse_Exception_Case(){
        when(mockCourse1.isStudentEnrolled(mockStudent1)).thenReturn(false);
        when(mockCourse1.isStudentWaitListed(mockStudent1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () ->
                testRegistrationImpl.dropCourse(mockStudent1, mockCourse1));
    }

    @Test
    @DisplayName("course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST && course.getCurrentWaitListSize() == 0")
    public void testDropCourse_Equivalence_Case1(){
        when(mockCourse1.isStudentEnrolled(mockStudent1)).thenReturn(true);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(0);
        testRegistrationImpl.dropCourse(mockStudent1, mockCourse1);
        verify(mockCourse1).removeStudentFromEnrolled(mockStudent1);
        verify(mockCourse1).setEnrollmentStatus(Course.EnrollmentStatus.OPEN);

    }

    @Test
    @DisplayName("course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST && course.getCurrentWaitListSize() > 0")
    public void testDropCourse_Equivalence_Case2(){
        when(mockCourse1.isStudentEnrolled(mockStudent1)).thenReturn(true);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        when(mockCourse1.getCurrentWaitListSize()).thenReturn(10);
        testRegistrationImpl.dropCourse(mockStudent1, mockCourse1);
        verify(mockCourse1).removeStudentFromEnrolled(mockStudent1);
        verify(mockCourse1).removeStudentFromWaitList(mockCourse1.getFirstStudentOnWaitList());
        verify(mockCourse1).addStudentToEnrolled(mockCourse1.getFirstStudentOnWaitList());
    }

    @Test
    @DisplayName("course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED")
    public void testDropClass_Equivalence_Case3(){
        when(mockCourse1.isStudentEnrolled(mockStudent1)).thenReturn(true);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        testRegistrationImpl.dropCourse(mockStudent1, mockCourse1);
        verify(mockCourse1).removeStudentFromEnrolled(mockStudent1);
        verify(mockCourse1).removeStudentFromWaitList(mockCourse1.getFirstStudentOnWaitList());
        verify(mockCourse1).addStudentToEnrolled(mockCourse1.getFirstStudentOnWaitList());
        verify(mockCourse1).setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
    }

    @Test
    @DisplayName("course.isStudentEnrolled(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.OPEN")
    public void testDropClass_Equivalence_Case4(){
        when(mockCourse1.isStudentEnrolled(mockStudent1)).thenReturn(true);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        testRegistrationImpl.dropCourse(mockStudent1,mockCourse1);
        verify(mockCourse1).removeStudentFromEnrolled(mockStudent1);
    }

    @Test
    @DisplayName("course.isStudentWaitListed(student) && course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED")
    public void testDropClass_Equivalence_Case5(){
        when(mockCourse1.isStudentWaitListed(mockStudent1)).thenReturn(true);
        when(mockCourse1.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        testRegistrationImpl.dropCourse(mockStudent1,mockCourse1);
        verify(mockCourse1).removeStudentFromWaitList(mockStudent1);
        verify(mockCourse1).setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
    }

    @Test
    @DisplayName("course.isStudentWaitListed(student)")
    public void testDropClass_Equivalence_Case6(){
        when(mockCourse1.isStudentWaitListed(mockStudent1)).thenReturn(true);
        testRegistrationImpl.dropCourse(mockStudent1,mockCourse1);
        verify(mockCourse1).removeStudentFromWaitList(mockStudent1);
    }

}
