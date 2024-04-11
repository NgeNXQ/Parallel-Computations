package common;

import java.util.Random;

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

        for (int i = 0; i < matrix1.rows; ++i)
        {
            for (int j = 0; j < matrix2.columns; ++j)
            {
                for (int k = 0; k < matrix1.columns; ++k)
                {
                    resultingMatrix.matrix[i][j] += matrix1.matrix[i][k] * matrix2.matrix[k][j];
                }
            }
        }

        long timestepEnd = System.currentTimeMillis();

        long executionTime = timestepEnd - timestepStart;

        Result result = new Result(resultingMatrix, executionTime);

        return result;
    }

    public static Result multiplyStriped(MatrixInt matrix1, MatrixInt matrix2, int threadsCount)
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

    public static Result multiplyFox(MatrixInt matrix1, MatrixInt matrix2, int threadsCount)
    {
        if (!MatrixInt.areMultipliable(matrix1, matrix2))
        {
            throw new IllegalArgumentException("Matrices are not multipliable.");
        }
    
        int size = matrix1.getRows();
        int blockSize = size / threadsCount;
        MatrixInt resultingMatrix = new MatrixInt(size, size);
    
        long timestepStart = System.currentTimeMillis();
    
        Thread[] threads = new Thread[threadsCount * threadsCount];
    
        for (int threadRow = 0; threadRow < threadsCount; ++threadRow)
        {
            for (int threadColumn = 0; threadColumn < threadsCount; ++threadColumn)
            {
                final int THREAD_ROW = threadRow;
                final int THREAD_COLUMN = threadColumn;
    
                threads[threadRow * threadsCount + threadColumn] = new Thread(() ->
                {
                    for (int k = 0; k < threadsCount; ++k)
                    {
                        int kk = (k + THREAD_ROW) % threadsCount;
    
                        for (int i = THREAD_ROW * blockSize; i < (THREAD_ROW + 1) * blockSize; ++i)
                        {
                            for (int j = THREAD_COLUMN * blockSize; j < (THREAD_COLUMN + 1) * blockSize; ++j)
                            {
                                for (int kkBlock = kk * blockSize; kkBlock < (kk + 1) * blockSize; ++kkBlock)
                                {
                                    resultingMatrix.matrix[i][j] += matrix1.matrix[i][kkBlock] * matrix2.matrix[kkBlock][j];
                                }
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

    private static boolean areMultipliable(MatrixInt matrix1, MatrixInt matrix2)
    {
        return matrix1.getColumns() == matrix2.getRows();
    }
}
