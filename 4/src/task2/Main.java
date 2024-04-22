package task2;

final class Main
{
    public static void main(String[] args)
    {
        final int MIN_VALUE = 0;
        final int MAX_VALUE = 1000;

        final int THRESHOLD_REDUCER = 8;

        Result result1;
        Result result2;
        MatrixInt matrix1;
        MatrixInt matrix2;

        final int[] THREADS = {4, 9, 16};
        final int[] DIMENSIONS = {500, 1000, 1500, 2000, 2500, 3000};

        for (final int THREAD : THREADS)
        {
            for (final int DIMENSION : DIMENSIONS)
            {
                final int THRESHOLD = DIMENSION / THRESHOLD_REDUCER;
                StripedMultiplicationTask.setThreshold(THRESHOLD);

                matrix1 = new MatrixInt(DIMENSION, DIMENSION);
                matrix2 = new MatrixInt(DIMENSION, DIMENSION);

                matrix1.fill(MIN_VALUE, MAX_VALUE);
                matrix2.fill(MIN_VALUE, MAX_VALUE);

                result1 = MatrixInt.multiplyStripedWithActions(matrix1, matrix2);
                result2 = MatrixInt.multiplyStripedWithThreads(matrix1, matrix2, THREAD);

                result1.printResults();
                result2.printResults();

                System.out.print(String.format("Threads: %d; Dimensions: %d; Threshold: %d | ", THREAD, DIMENSION, THRESHOLD));

                System.out.println(MatrixInt.AreEqual(result1.getMatrixInt(), result2.getMatrixInt()));
            }
        }
    }
}