package sequential;

import common.*;

public class Main 
{
    public static void main(String[] args)
    {
        final int MIN_VALUE = 0;
        final int MAX_VALUE = 1000;

        Result result;
        MatrixInt matrix1;
        MatrixInt matrix2;

        int[] dimensions = {500, 1000, 1500, 2000, 2500, 3000};

        for (int dimension : dimensions)
        {
            System.out.print(String.format("Dimensions: %d | ", dimension));

            matrix1 = new MatrixInt(dimension, dimension);
            matrix2 = new MatrixInt(dimension, dimension);

            matrix1.fill(MIN_VALUE, MAX_VALUE);
            matrix2.fill(MIN_VALUE, MAX_VALUE);

            result = MatrixInt.multiplySequential(matrix1, matrix2);

            result.printResults();
        }
    }
}
