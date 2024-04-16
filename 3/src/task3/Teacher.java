package task3;

final class Teacher
{
    private static int idCounter = 0;

    private final int ID;

    private String name;
    private String surname;
    private String patronymic;
    private String fullName;

    public Teacher(String name, String surname, String patronymic)
    {
        this.ID = ++Teacher.idCounter;

        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.fullName = String.format("%s %s %s", surname, name, patronymic);
    }

    public static int getAcademicsCount()
    {
        return Teacher.idCounter;
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

    public int generateGrade(int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }

        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
