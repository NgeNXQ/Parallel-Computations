package task2;

public final class Result
{
    private final MatrixInt MATRIX;
    private final long EXECUTION_TIME;

    public Result(MatrixInt matrix, long executionTime)
    {
        this.MATRIX = matrix;
        this.EXECUTION_TIME = executionTime;
    }

    public MatrixInt getMatrixInt()
    {
        return this.MATRIX;
    }

    public void printResults()
    {
        final double MILLISECONDS_IN_SECOND = 1000.0;

        double executionTimeSeconds = this.EXECUTION_TIME / MILLISECONDS_IN_SECOND;

        System.out.println(String.format("Execution time: %.3f seconds, (%d milliseconds).", executionTimeSeconds, this.EXECUTION_TIME));
    }
}