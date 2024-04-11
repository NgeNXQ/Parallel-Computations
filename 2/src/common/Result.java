package common;

public final class Result
{
    private MatrixInt matrix; 

    public Result(MatrixInt matrix, long executionTime)
    {
        this.matrix = matrix;

        final double MILLISECONDS_IN_SECOND = 1000.0;

        double executionTimeSeconds = executionTime / MILLISECONDS_IN_SECOND;

        System.out.println(String.format("Execution time: %.3f seconds, (%d milliseconds).", executionTimeSeconds, executionTime));
    }

    public MatrixInt getMatrixInt()
    {
        return this.matrix;
    }
}
