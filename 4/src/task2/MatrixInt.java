package task2;

import java.util.Random;

import java.util.concurrent.ForkJoinPool;

public final class MatrixInt
{
    private int[][] matrix;

    private int rows;
    private int columns;

    public MatrixInt(int rows, int columns)
    {
        final int MIN_DIMENSION_VALUE = 1;

        if (rows < MIN_DIMENSION_VALUE) 
        {
            throw new IndexOutOfBoundsException(String.format("Number of rows in the matrix must be at least %d.", MIN_DIMENSION_VALUE));
        }
        
        if (columns < MIN_DIMENSION_VALUE) 
        {
            throw new IndexOutOfBoundsException(String.format("Number of columns in the matrix must be at least %d.", MIN_DIMENSION_VALUE));
        }

        this.rows = rows;
        this.columns = columns;
        this.matrix = new int[rows][columns];
    }

    public int getRows()
    {
        return this.rows;
    }

    public int getColumns()
    {
        return this.columns;
    }

    public int[][] getMatrix()
    {
        return this.matrix;
    }

    public int[] getRow(int row)
    {
        final int MIN_DIMENSION_INDEX = 0;

        if (row < MIN_DIMENSION_INDEX || row >= this.rows)
        {
            throw new IndexOutOfBoundsException(String.format("Row index out of bounds: %d.", row));
        }

        return this.matrix[row];
    }

    public int[] getColumn(int column)
    {
        final int MIN_DIMENSION_INDEX = 0;

        if (column < MIN_DIMENSION_INDEX || column >= this.columns)
        {
            throw new IndexOutOfBoundsException(String.format("Column index out of bounds: %d.", column));
        }

        int[] result = new int[this.rows];

        for (int i = 0; i < this.rows; ++i) 
        {
            result[i] = matrix[i][column];
        }

        return result;
    }

    public int get(int row, int column)
    {
        final int MIN_DIMENSION_INDEX = 0;
        
        if (row < MIN_DIMENSION_INDEX || row >= this.rows)
        {
            throw new IndexOutOfBoundsException(String.format("Row index out of bounds: %d.", row));
        }

        if (column < MIN_DIMENSION_INDEX || column >= this.columns)
        {
            throw new IndexOutOfBoundsException(String.format("Column index out of bounds: %d.", column));
        }

        return this.matrix[row][column];
    }

    public void set(int row, int column, int value)
    {
        final int MIN_DIMENSION_INDEX = 0;
        
        if (row < MIN_DIMENSION_INDEX || row >= this.rows)
        {
            throw new IndexOutOfBoundsException(String.format("Row index out of bounds: %d.", row));
        }

        if (column < MIN_DIMENSION_INDEX || column >= this.columns)
        {
            throw new IndexOutOfBoundsException(String.format("Column index out of bounds: %d.", column));
        }

        this.matrix[row][column] = value;
    }

    public void fill(int minValue, int maxValue)
    {
        Random random = new Random();

        for (int i = 0; i < this.rows; ++i)
        {
            for (int j = 0; j < this.columns; ++j)
            {
                this.matrix[i][j] = random.nextInt(maxValue - minValue + 1) + minValue;
            }
        }
    }

    public static boolean AreEqual(MatrixInt matrix1, MatrixInt matrix2)
    {
        if (matrix1.rows != matrix2.rows || matrix1.columns != matrix2.columns) 
        {
            return false;
        }
    
        for (int i = 0; i < matrix1.rows; ++i)
        {
            for (int j = 0; j < matrix1.columns; ++j)
            {
                if (matrix1.matrix[i][j] != matrix2.matrix[i][j])
                {
                    return false;
                }
            }
        }
    
        return true;
    }    

    public static Result multiplySequential(MatrixInt matrix1, MatrixInt matrix2)
    {
        if (!MatrixInt.areMultipliable(matrix1, matrix2))
        {
            throw new IllegalArgumentException("Matrices are not multipliable.");
        }

        MatrixInt resultingMatrix = new MatrixInt(matrix1.rows, matrix2.columns);

        long timestepStart = System.currentTimeMillis();

        for (int rowIndex = 0; rowIndex < matrix1.rows; ++rowIndex)
        {
            for (int columnIndex = 0; columnIndex < matrix2.columns; ++columnIndex)
            {
                for (int dimensionIndex = 0; dimensionIndex < matrix1.columns; ++dimensionIndex)
                {
                    resultingMatrix.matrix[rowIndex][columnIndex] += matrix1.matrix[rowIndex][dimensionIndex] * matrix2.matrix[dimensionIndex][columnIndex];
                }
            }
        }

        long timestepEnd = System.currentTimeMillis();

        long executionTime = timestepEnd - timestepStart;

        Result result = new Result(resultingMatrix, executionTime);

        return result;
    }

    public static Result multiplyFox(MatrixInt matrix1, MatrixInt matrix2, int threadsCount)
    {
        if (!MatrixInt.areMultipliable(matrix1, matrix2))
        {
            throw new IllegalArgumentException("Matrices are not multipliable.");
        }

        int threadsPerBlock = (int) Math.sqrt(threadsCount);

        if (threadsPerBlock * threadsPerBlock != threadsCount)
        {
            throw new IllegalArgumentException("threadsCount is not power of 2.");
        }

        MatrixInt resultingMatrix = new MatrixInt(matrix1.rows, matrix2.columns);

        long timestepStart = System.currentTimeMillis();

        int threadPayload = (int) Math.ceil((double) matrix1.rows / threadsPerBlock);

        int threadIndex = 0;
        Thread[] threads = new Thread[threadsCount];
    
        for (int rowIndex = 0; rowIndex < matrix1.rows; rowIndex += threadPayload)
        {
            for (int columnIndex = 0; columnIndex < matrix2.columns; columnIndex += threadPayload)
            {
                final int ROW_INDEX = rowIndex;
                final int COLUMN_INDEX = columnIndex;

                threads[threadIndex++] = new Thread(() -> 
                {
                    int matrix1RowSize =  (ROW_INDEX + threadPayload) > matrix1.rows ? (matrix1.rows - ROW_INDEX) : threadPayload;
                    int matrix2ColumnSize = (COLUMN_INDEX + threadPayload) > matrix2.columns ? (matrix2.columns - COLUMN_INDEX) : threadPayload;

                    for (int i = 0; i < matrix1.rows; i += threadPayload)
                    {
                        int matrix2RowSize = (i + threadPayload) > matrix2.rows ? (matrix2.rows - i) : threadPayload;
                        int matrix1ColumnSize = (i + threadPayload) > matrix1.columns ? (matrix1.columns - i) : threadPayload;

                        MatrixInt block1 = copyMatrixIntBlock(matrix1, ROW_INDEX, ROW_INDEX + matrix1RowSize, i, i + matrix1ColumnSize);
                        MatrixInt block2 = copyMatrixIntBlock(matrix2, i, i + matrix2RowSize, COLUMN_INDEX, COLUMN_INDEX + matrix2ColumnSize);

                        MatrixInt resultingBlock = MatrixInt.multiplySequential(block1, block2).getMatrixInt();

                        for (int j = 0; j < resultingBlock.rows; ++j)
                        {
                            for (int k = 0; k < resultingBlock.columns; ++k)
                            {
                                resultingMatrix.set(j + ROW_INDEX, k + COLUMN_INDEX, resultingBlock.get(j, k) + resultingMatrix.get(j + ROW_INDEX, k + COLUMN_INDEX));
                            }
                        }
                    }
                });
            }
        }

        for (Thread thread : threads)
        {
            thread.start();
        }
        
        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        long timestepEnd = System.currentTimeMillis();

        long executionTime = timestepEnd - timestepStart;

        Result result = new Result(resultingMatrix, executionTime);

        return result;
    }

    private static MatrixInt copyMatrixIntBlock(MatrixInt source, int rowStart, int rowFinish, int columnStart, int columnFinish)
    {
        final int OFFSET_ROW = rowFinish - rowStart;
        final int OFFSET_COLUMN = columnFinish - columnStart;

        MatrixInt matrix = new MatrixInt(rowFinish - rowStart, columnFinish - columnStart);

        for (int i = 0; i < OFFSET_ROW; ++i)
        {
            for (int j = 0; j < OFFSET_COLUMN; ++j)
            {
                matrix.set(i, j, source.get(i + rowStart, j + columnStart));
            }
        }

        return matrix;
    }

    public static Result multiplyStripedWithActions(MatrixInt matrix1, MatrixInt matrix2, int threshold)
    {
        if (!MatrixInt.areMultipliable(matrix1, matrix2))
        {
            throw new IllegalArgumentException("Matrices are not multipliable.");
        }

        MatrixInt resultingMatrix = new MatrixInt(matrix1.rows, matrix2.columns);

        long timestepStart = System.currentTimeMillis();

        ForkJoinPool.commonPool().invoke(new StripedMultiplicationAction(matrix1, matrix2, resultingMatrix, 0, matrix1.rows * matrix2.columns, threshold));

        long timestepEnd = System.currentTimeMillis();
        long executionTime = timestepEnd - timestepStart;

        return new Result(resultingMatrix, executionTime);
    }

    public static Result multiplyStripedWithThreads(MatrixInt matrix1, MatrixInt matrix2, int threadsCount)
    {
        if (!MatrixInt.areMultipliable(matrix1, matrix2))
        {
            throw new IllegalArgumentException("Matrices are not multipliable.");
        }

        MatrixInt resultingMatrix = new MatrixInt(matrix1.rows, matrix2.columns);

        long timestepStart = System.currentTimeMillis();

        int totalTasks = matrix1.rows * matrix2.columns;
        int tasksPerThread = totalTasks / threadsCount;

        Thread[] threads = new Thread[threadsCount];

        for (int i = 0; i < threadsCount; ++i)
        {
            final int THREAD_ID = i;

            threads[i] = new Thread(() -> 
            {
                int startTaskIndex = THREAD_ID * tasksPerThread;
                int endTaskIndex = (THREAD_ID == threadsCount - 1) ? totalTasks : startTaskIndex + tasksPerThread;

                for (int j = startTaskIndex; j < endTaskIndex; ++j)
                {
                    int rowIndex = j / matrix1.columns;
                    int columnIndex = j % matrix2.columns;

                    int[] row = matrix1.getRow(rowIndex);
                    int[] column = matrix2.getColumn(columnIndex);

                    int result = 0;

                    for (int k = 0; k < row.length; ++k)
                    {
                        result += row[k] * column[k];
                    }

                    resultingMatrix.set(rowIndex, columnIndex, result);
                }
            });
        }

        for (Thread thread : threads) 
        {
            thread.start();
        }

        for (Thread thread : threads) 
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        long timestepEnd = System.currentTimeMillis();

        long executionTime = timestepEnd - timestepStart;

        Result result = new Result(resultingMatrix, executionTime);

        return result;
    }

    private static boolean areMultipliable(MatrixInt matrix1, MatrixInt matrix2)
    {
        return matrix1.getColumns() == matrix2.getRows();
    }
}