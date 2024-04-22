package task2;

public final class Result
{
    private MatrixInt matrix;
    private long executionTime;

    public Result(MatrixInt matrix, long executionTime)
    {
        this.matrix = matrix;
        this.executionTime = executionTime;
    }

    public MatrixInt getMatrixInt()
    {
        return this.matrix;
    }

    public void printResults()
    {
        final double MILLISECONDS_IN_SECOND = 1000.0;

        double executionTimeSeconds = executionTime / MILLISECONDS_IN_SECOND;

        System.out.println(String.format("Execution time: %.3f seconds, (%d milliseconds).", executionTimeSeconds, executionTime));
    }
}