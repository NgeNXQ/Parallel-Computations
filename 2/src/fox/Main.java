package fox;

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

        int[] threads = {4, 9, 16};
        int[] dimensions = {500, 1000, 1500, 2000, 2500, 3000};

        for (int thread : threads)
        {
            for (int dimension : dimensions)
            {
                System.out.print(String.format("Threads: %d; Dimensions: %d | ", thread, dimension));

                matrix1 = new MatrixInt(dimension, dimension);
                matrix2 = new MatrixInt(dimension, dimension);

                matrix1.fill(MIN_VALUE, MAX_VALUE);
                matrix2.fill(MIN_VALUE, MAX_VALUE);

                result = MatrixInt.multiplyFox(matrix1, matrix2, thread);

                result.printResults();
            }
        }
    }
}
