package fox;

import common.*;

public class Main
{
    public static void main(String[] args)
    {
        final int ROWS_COUNT = 1500;
        final int COLUMNS_COUNT = 1500;

        final int MIN_VALUE = 0;
        final int MAX_VALUE = 1000;

        final int THREADS_COUNT = 25;

        final MatrixInt matrix1 = new MatrixInt(ROWS_COUNT, COLUMNS_COUNT);
        final MatrixInt matrix2 = new MatrixInt(ROWS_COUNT, COLUMNS_COUNT);

        matrix1.fill(MIN_VALUE, MAX_VALUE);
        matrix2.fill(MIN_VALUE, MAX_VALUE);

        System.out.println("...");

        Result result1 = MatrixInt.multiplySequential(matrix1, matrix2);
        Result result2 = MatrixInt.multiplyFox(matrix1, matrix2, THREADS_COUNT);

        System.out.println(MatrixInt.AreEqual(result1.getMatrixInt(), result2.getMatrixInt()));
    }
}
