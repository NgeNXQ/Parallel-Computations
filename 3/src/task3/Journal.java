package task3;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import java.util.concurrent.locks.ReentrantLock;

final class Journal
{
    public static final int GRADE_MIN_VALUE = 0;
    public static final int GRADE_MAX_VALUE = 100;

    private static int idCounter = 0;

    private final int ID;
    private final String NAME;

    private final int WEEKS_COUNT;

    private final HashMap<Student, ReentrantLock[]> SHEET_LOCKS;
    private final HashMap<Group, HashMap<Student, Integer[]>> SHEET;

    public Journal(String name, int weeksCount, Group... groups)
    {
        this.ID = ++Journal.idCounter;

        this.NAME = name;

        this.WEEKS_COUNT = weeksCount;

        this.SHEET = new HashMap<>();
        this.SHEET_LOCKS = new HashMap<>();

        for (Group group : groups)
        {
            for (int i = 0; i < group.getStudentsCount(); ++i)
            {
                this.SHEET_LOCKS.computeIfAbsent(group.getStudent(i), k -> IntStream.range(0, weeksCount).mapToObj(j -> new ReentrantLock()).toArray(ReentrantLock[]::new));
                this.SHEET.computeIfAbsent(group, k -> new HashMap<>()).put(group.getStudent(i), IntStream.range(0, weeksCount).mapToObj(j -> 0).toArray(Integer[]::new));
            }
        }
    }

    public int getId()
    {
        return this.ID;
    }

    public String getName()
    {
        return this.NAME;
    }

    public int getWeeksCount()
    {
        return this.WEEKS_COUNT;
    }

    public void putGrade(Student student, int weekIndex, int grade)
    {
        if (student == null)
        {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (weekIndex < 0 || weekIndex >= this.WEEKS_COUNT)
        {
            throw new IllegalArgumentException("Invalid weekIndex value.");
        }

        if (grade < Journal.GRADE_MIN_VALUE || grade > Journal.GRADE_MAX_VALUE)
        {
            throw new IllegalArgumentException("Grade value is invalid.");
        }
        
        ReentrantLock lock = this.SHEET_LOCKS.get(student)[weekIndex];

        lock.lock();

        try
        {
            for (Group group : this.SHEET.keySet())
            {
                if (group.containsStudent(student))
                {
                    this.SHEET.get(group).get(student)[weekIndex] += grade;
                    break;
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public void printSheet()
    {
        List<Group> sortedGroups = new ArrayList<>(this.SHEET.keySet()).stream().sorted(Comparator.comparing(Group::getName)).collect(Collectors.toList());

        for (Group group : sortedGroups)
        {
            System.out.println(group.getName());

            List<Student> sortedStudents = new ArrayList<>(this.SHEET.get(group).keySet()).stream().sorted(Comparator.comparing(Student::getFullName)).collect(Collectors.toList());

            for (Student student : sortedStudents)
            {
                System.out.printf("%-10s: ", student.getFullName());

                for (int j = 0; j < this.WEEKS_COUNT; ++j)
                {
                    System.out.printf("%-3d|", this.SHEET.get(group).get(student)[j]);
                }

                System.out.println();
            }

            System.out.println();
        }
    }
}
