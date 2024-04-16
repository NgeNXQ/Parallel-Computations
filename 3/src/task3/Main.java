package task3;

import java.util.ArrayList;

final class Main 
{
    public static void main(String[] args)
    {
        Group group1 = new Group("Group 1", Main.generateStudentsArray(25));
        Group group2 = new Group("Group 2", Main.generateStudentsArray(30));
        Group group3 = new Group("Group 3", Main.generateStudentsArray(27));

        Journal journal = new Journal("Journal", 10, group1, group2, group3);

        Teacher lecturer = new Teacher("Lecturer", "Lecturer", "Lecturer");
        Teacher assistant1 = new Teacher("Assistant 1", "Assistant 1", "Assistant 1");
        Teacher assistant2 = new Teacher("Assistant 2", "Assistant 2", "Assistant 2");
        Teacher assistant3 = new Teacher("Assistant 3", "Assistant 3", "Assistant 3");

        Group[] groups = { group1, group2, group3 };
        Teacher[] teachers = { lecturer, assistant1, assistant2, assistant3 };

        ArrayList<Thread> threads = new ArrayList<>();

        for (Teacher teacher : teachers)
        {
            threads.addLast(new Thread(() -> 
            {
                for (Group group : groups)
                {
                    for (int i = 0; i < group.getStudentsCount(); ++i)
                    {
                        for (int weekIndex = 0; weekIndex < journal.getWeeksCount(); ++weekIndex)
                        {
                            journal.putGrade(group.getStudent(i), weekIndex, teacher.generateGrade(Journal.GRADE_MIN_VALUE, Journal.GRADE_MAX_VALUE));
                        }
                    }
                }
            }));
        }

        for (Thread thread : threads)
        {
            thread.start();
        }

        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex.getStackTrace());
            }
        }

        journal.printSheet();
    }

    private static Student[] generateStudentsArray(int count)
    {
        Student[] students = new Student[count];

        String placeholder;

        for (int i = 0; i < count; ++i)
        {
            placeholder = Integer.toString(i);

            students[i] = new Student(placeholder, placeholder, placeholder);
        }

        return students;
    }
}
