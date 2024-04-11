package sequential;

import common.*;

public class Main 
{
    public static void main(String[] args)
    {
        final int ROWS_COUNT = 1500;
        final int COLUMNS_COUNT = 1500;

        final int MIN_VALUE = 0;
        final int MAX_VALUE = 1000;

        final MatrixInt matrix1 = new MatrixInt(ROWS_COUNT, COLUMNS_COUNT);
        final MatrixInt matrix2 = new MatrixInt(ROWS_COUNT, COLUMNS_COUNT);

        matrix1.fill(MIN_VALUE, MAX_VALUE);
        matrix2.fill(MIN_VALUE, MAX_VALUE);

        System.out.println("...");

        Result result = MatrixInt.multiplySequential(matrix1, matrix2);
    }
}
