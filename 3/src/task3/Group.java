package task3;

import java.util.ArrayList;
import java.util.Arrays;

final class Group
{
    private static int idCounter = 0;

    private final int ID;
    private final String NAME;
    private final ArrayList<Student> STUDENTS;

    public Group(String name, Student... students)
    {
        this.ID = ++Group.idCounter;

        this.NAME = name;
        this.STUDENTS = new ArrayList<>(Arrays.asList(students));
    }

    public static int getGroupsCount()
    {
        return Group.idCounter;
    }

    public long getId()
    {
        return this.ID;
    }

    public String getName()
    {
        return this.NAME;
    }

    public int getStudentsCount()
    {
        return this.STUDENTS.size();
    }

    public Student getStudent(int index)
    {
        if (index >= this.STUDENTS.size() || index < 0)
        {
            throw new IllegalArgumentException("Sequence number is out of bounds.");
        }

        return this.STUDENTS.get(index);
    }

    public int getStudentIndex(Student student)
    {
        if (student == null)
        {
            throw new IllegalArgumentException("Student cannot be null");
        }

        final int RESULT_FAILURE = -1;

        int index = this.STUDENTS.indexOf(student);

        return (index != RESULT_FAILURE) ? index : RESULT_FAILURE;
    }

    public boolean containsStudent(Student student)
    {
        if (student == null)
        {
            throw new IllegalArgumentException("Student cannot be null");
        }

        return this.STUDENTS.contains(student);
    }
}
