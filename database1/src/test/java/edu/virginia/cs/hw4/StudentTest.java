package edu.virginia.cs.hw4;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Set;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest{

    private Course mockCourse1;
    private Course mockCourse2;
    private Prerequisite prerequisite;
    private Transcript transcript;
    private Student testStudent;
    private Map<Course,Grade> mockCourseHistory;

    @BeforeEach
    public void setUp() {
        mockCourse1 = mock(Course.class);
        mockCourse2 = mock(Course.class);
        mockCourseHistory = (Map<Course,Grade>) mock(Map.class);
        transcript = new Transcript(testStudent, mockCourseHistory);
        testStudent = new Student(12345,"han","12345@",transcript);
        prerequisite = getPrerequisite();
    }

    private Prerequisite getPrerequisite() {
        Prerequisite prerequisite = new Prerequisite(mockCourse1, Grade.C);
        return prerequisite;
    }

    @Test
    public void testHasStudentTakenCourse_Taken(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        assertTrue(testStudent.hasStudentTakenCourse(mockCourse1));
    }

    @Test
    public void testHasStudentTakenCourse_NotTaken_DifferetDepartment() {
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("PHIL");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        assertFalse(testStudent.hasStudentTakenCourse(mockCourse1));
    }

    @Test
    public void testHasStudentTakenCourse_NotTaken_DifferentCatalogNumber(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(1110);
        assertFalse(testStudent.hasStudentTakenCourse(mockCourse1));
    }

    @Test
    public void testAddCourseGrade() {
        testStudent.addCourseGrade(mockCourse1,Grade.A);
        verify(mockCourseHistory).put(mockCourse1,Grade.A);
    }

    @Test
    public void testGetCourseGrade_EquivalenceCase() {
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        when(mockCourseHistory.get(mockCourse1)).thenReturn(Grade.A);
        assertEquals(Grade.A,testStudent.getCourseGrade(mockCourse1));
    }

    @Test
    public void testGetCourseGrade_Unsuccessful_CourseDoesNotExist(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("PHIL");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        assertThrows(IllegalArgumentException.class, () ->
                testStudent.getCourseGrade(mockCourse1));
    }

    @Test
    public void testMeetsPrerequisite_Take_Pass_withExactGrade() {
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        when(mockCourseHistory.get(prerequisite.course)).thenReturn(Grade.C);
        assertTrue(testStudent.meetsPrerequisite(prerequisite));
    }

    @Test
    public void testMeetsPrerequisite_Take_Pass_withAboveGrade(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        when(mockCourseHistory.get(prerequisite.course)).thenReturn(Grade.A);
        assertTrue(testStudent.meetsPrerequisite(prerequisite));
    }

    @Test
    public void testMeetPrerequisite_Take_NotPass(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(3140);
        when(mockCourseHistory.get(prerequisite.course)).thenReturn(Grade.C_MINUS);
        assertFalse(testStudent.meetsPrerequisite(prerequisite));

    }

    @Test
    public void testMeetPrerequisite_NotTaken(){
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse2));
        when(mockCourse1.getDepartment()).thenReturn("CS");
        when(mockCourse2.getDepartment()).thenReturn("CS");
        when(mockCourse1.getCatalogNumber()).thenReturn(3140);
        when(mockCourse2.getCatalogNumber()).thenReturn(2100);
        when(mockCourseHistory.get(prerequisite.course)).thenReturn(Grade.C_MINUS);
        assertFalse(testStudent.meetsPrerequisite(prerequisite));
    }

    @Test
    public void testGetGPA_oneCourse() {
        when(mockCourseHistory.size()).thenReturn(1);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse1));
        when(mockCourseHistory.get(mockCourse1)).thenReturn(Grade.A);
        when(mockCourse1.getCreditHours()).thenReturn(3);
        assertEquals(4.0,testStudent.getGPA());
    }

    @Test
    public void testGPA_TwoCourses(){
        when(mockCourseHistory.size()).thenReturn(2);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse1,mockCourse2));
        when(mockCourseHistory.get(mockCourse1)).thenReturn(Grade.A);
        when(mockCourseHistory.get(mockCourse2)).thenReturn(Grade.B);
        when(mockCourse1.getCreditHours()).thenReturn(3);
        when(mockCourse2.getCreditHours()).thenReturn(3);
        assertEquals(3.5,testStudent.getGPA());
    }

    @Test
    public void testGPA_ThreeCourses(){
        Course mockCourse3 = mock(Course.class);
        when(mockCourseHistory.size()).thenReturn(3);
        when(mockCourseHistory.keySet()).thenReturn(Set.of(mockCourse1,mockCourse2, mockCourse3));
        when(mockCourseHistory.get(mockCourse1)).thenReturn(Grade.A);
        when(mockCourseHistory.get(mockCourse2)).thenReturn(Grade.B);
        when(mockCourseHistory.get(mockCourse3)).thenReturn(Grade.C);
        when(mockCourse1.getCreditHours()).thenReturn(3);
        when(mockCourse2.getCreditHours()).thenReturn(3);
        when(mockCourse3.getCreditHours()).thenReturn(4);
        assertEquals(2.9,testStudent.getGPA());
    }

    @Test
    public void testGetGPA_ZeroCourse(){
        when(mockCourseHistory.size()).thenReturn(0);
        assertThrows(IllegalStateException.class, () ->
                testStudent.getGPA());
    }
}



