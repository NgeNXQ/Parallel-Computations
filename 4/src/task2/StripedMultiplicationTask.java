package task2;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class StripedMultiplicationTask extends RecursiveAction
{
    private static int Threshold = 100;

    private final MatrixInt MATRIX_1;
    private final MatrixInt MATRIX_2;
    private final MatrixInt MATRIX_RESULT;

    private final int INDEX_START;
    private final int INDEX_FINISH;

    public StripedMultiplicationTask(MatrixInt matrix1, MatrixInt matrix2, MatrixInt result, int indexStart, int indexFinish)
    {
        this.MATRIX_1 = matrix1;
        this.MATRIX_2 = matrix2;
        this.MATRIX_RESULT = result;

        this.INDEX_START = indexStart;
        this.INDEX_FINISH = indexFinish;
    }

    @Override
    protected void compute()
    {
        if (this.INDEX_FINISH - this.INDEX_START <= StripedMultiplicationTask.Threshold)
        {
            for (int i = this.INDEX_START; i < this.INDEX_FINISH; ++i)
            {
                final int INDEX_ROW = i / this.MATRIX_1.getColumns();
                final int INDEX_COLUMN = i % this.MATRIX_2.getColumns();

                final int[] ROW = this.MATRIX_1.getRow(INDEX_ROW);
                final int[] COLUMN = this.MATRIX_2.getColumn(INDEX_COLUMN);

                int result = 0;

                for (int j = 0; j < ROW.length; ++j)
                {
                    result += ROW[j] * COLUMN[j];
                }

                this.MATRIX_RESULT.set(INDEX_ROW, INDEX_COLUMN, result);
            }
        }
        else
        {
            final int INDEX_MID = (this.INDEX_START + this.INDEX_FINISH) / 2;

            StripedMultiplicationTask leftTask = new StripedMultiplicationTask(this.MATRIX_1, this.MATRIX_2, this.MATRIX_RESULT, this.INDEX_START, INDEX_MID);
            StripedMultiplicationTask rightTask = new StripedMultiplicationTask(this.MATRIX_1, this.MATRIX_2, this.MATRIX_RESULT, INDEX_MID, this.INDEX_FINISH);

            ForkJoinTask.invokeAll(leftTask, rightTask);
        }
    }

    public static int getThreshold()
    {
        return StripedMultiplicationTask.Threshold;
    }

    public static void setThreshold(int value)
    {
        StripedMultiplicationTask.Threshold = value;
    }
}
