package task3;

final class Student
{
    private static int idCounter = 0;

    private final int ID;

    private String name;
    private String surname;
    private String patronymic;
    private String fullName;

    public Student(String name, String surname, String patronymic)
    {
        this.ID = ++Student.idCounter;

        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.fullName = String.format("%s %s %s", surname, name, patronymic);
    }

    public static int getStudentsCount()
    {
        return Student.idCounter;
    }

    public long getId()
    {
        return this.ID;
    }

    public String getName()
    {
        return this.name;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public String getPatronymic()
    {
        return this.patronymic;
    }

    public String getFullName()
    {
        return this.fullName;
    }
}
