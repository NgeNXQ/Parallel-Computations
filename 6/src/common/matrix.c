#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "mpi.h"
#include "matrix.h"

#define MPI_ROOT_RANK 0
#define MPI_TAG_ROOT_MESSAGE 1
#define MPI_TAG_WORKER_MESSAGE 2

typedef struct InternalMatrixInt
{
    int rows;
    int columns;
    int** matrix;
} MatrixInt;

#pragma region init & free

void matrix_int_free(MatrixInt const *matrix)
{
    if (matrix == NULL)
    {
        perror("Argument matrix is null");
        return;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        free(matrix->matrix[i]);
    }

    free(matrix->matrix);
    free(matrix);
}

MatrixInt* matrix_int_init(const int rows, const int columns)
{
    if (rows < 0)
    {
        perror("Invalid rows argument");
        return NULL;
    }

    if (columns < 0)
    {
        perror("Invalid columns argument");
        return NULL;
    }

    MatrixInt* const matrix = (MatrixInt*)malloc(sizeof(MatrixInt));

    if (matrix == NULL)
    {
        perror("Failed to allocate memory for MatrixInt");
        return NULL;
    }

    matrix->rows = rows;
    matrix->columns = columns;
    matrix->matrix = (int**)malloc(rows * sizeof(int*));

    if (matrix->matrix == NULL)
    {
        free(matrix->matrix);
        free(matrix);
        perror("Failed to allocate memory for MatrixInt");
        return NULL;
    }

    for (int i = 0; i < rows; ++i)
    {
        matrix->matrix[i] = (int*)malloc(columns * sizeof(int));

        if (matrix->matrix[i] == NULL)
        {
            matrix_int_free(matrix);
            perror("Failed to allocate memory for MatrixInt");
            return NULL;
        }
    }

    return matrix;
}

#pragma endregion

#pragma region getters & setters

int matrix_int_get_rows_count(const MatrixInt const *matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return -1;
    }

    return matrix->rows;
}

int matrix_int_get_columns_count(const MatrixInt const *matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return -1;
    }
    
    return matrix->columns;
}

int** matrix_int_get_matrix(const MatrixInt const *matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return NULL;
    }

    return matrix->matrix;
}

void matrix_int_set_matrix(MatrixInt *matrix, const int const **data)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return;
    }

    if (data == NULL)
    {
        perror("Invalid data argument");
        return;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        for (int j = 0; j < matrix->columns; ++j)
        {
            matrix->matrix[i][j] = data[i][j];
        }
    }
}

int* matrix_int_get_row(const MatrixInt const *matrix, const int index)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return NULL;
    }

    if (index < 0 || index >= matrix->rows)
    {
        perror("Invalid index argument");
        return NULL;
    }

    return matrix->matrix[index];
}

int* matrix_int_get_column(const MatrixInt const *matrix, const int index)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return NULL;
    }

    if (index < 0 || index >= matrix->columns)
    {
        perror("Invalid index argument");
        return NULL;
    }

    int* const column = (int*)malloc(matrix->rows * sizeof(int));

    if (column == NULL)
    {
        perror("Memory allocation failed");
        return NULL;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        column[i] = matrix->matrix[i][index];
    }

    return column;
}

int matrix_int_get(const MatrixInt const *matrix, const int row_index, const int column_index)
{
    if (row_index < 0 || row_index >= matrix->rows)
    {
        perror("Invalid row_index argument");
        return -1;
    }

    if (column_index < 0 || column_index >= matrix->columns)
    {
        perror("Invalid column_index argument");
        return -1;
    }

    return matrix->matrix[row_index][column_index];
}

void matrix_int_set(const MatrixInt const *matrix, const int row_index, const int column_index, const int value)
{
    if (row_index < 0 || row_index >= matrix->rows)
    {
        perror("Invalid row_index argument");
        return;
    }

    if (column_index < 0 || column_index >= matrix->columns)
    {
        perror("Invalid column_index argument");
        return;
    }
    
    matrix->matrix[row_index][column_index] = value;
}

#pragma endregion

#pragma region general functions

void matrix_int_fill_random(const MatrixInt const *matrix, const int min, const int max)
{
    srand((unsigned int)time(NULL));

    for (int i = 0; i < matrix->rows; ++i)
    {
        for (int j = 0; j < matrix->columns; ++j)
        {
            matrix->matrix[i][j] = rand() % (max - min + 1) + min;
        }
    }
}

bool matrix_int_are_equal(const MatrixInt const *matrix1, const MatrixInt const *matrix2)
{
    if (matrix1 == NULL)
    {
        perror("Invalid matrix1 argument");
        return false;
    }

    if (matrix2 == NULL)
    {
        perror("Invalid matrix2 argument");
        return NULL;
    }

    if (matrix1->rows != matrix2->rows || matrix1->columns != matrix2->columns)
    {
        return false;
    }

    for (int i = 0; i < matrix1->rows; ++i)
    {
        for (int j = 0; j < matrix2->columns; ++j)
        {
            if (matrix1->matrix[i][j] != matrix2->matrix[i][j])
            {
                return false;
            }
        }
    }

    return true;
}

#pragma endregion

#pragma region multiplication functions

MatrixInt* matrix_int_multiply_sequential(const MatrixInt* const matrix1, const MatrixInt* const matrix2)
{
    if (matrix1->columns != matrix2->rows)
    {
        perror("Matrices are not multipliable.");
        return NULL;
    }

    MatrixInt *result = matrix_int_init(matrix1->rows, matrix2->columns);
    
    if (result == NULL)
    {
        return NULL;
    }

    for (int i = 0; i < matrix1->rows; ++i)
    {
        for (int j = 0; j < matrix2->columns; ++j)
        {
            int sum = 0;

            for (int k = 0; k < matrix1->columns; ++k)
            {
                sum += matrix1->matrix[i][k] * matrix2->matrix[k][j];
            }

            result->matrix[i][j] = sum;
        }
    }

    return result;
}

MatrixInt* matrix_int_multiply_mpi_blocking(const MatrixInt* const matrix1, const MatrixInt* const matrix2)
{
    int mpi_comm_rank;
    int mpi_comm_size;

    MPI_Comm_rank(MPI_COMM_WORLD, &mpi_comm_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &mpi_comm_size);

    if (matrix1->columns != matrix2->rows)
    {
        perror("Matrices are not multipliable.");
        return NULL;
    }

    if (mpi_comm_size < 2)
    {
        printf("At least 2 MPI processors are required.\n");
        MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
        return NULL;
    }

    int mpi_index_start;
    int mpi_index_finish;
    MPI_Status mpi_status;

    int *mpi_stripe;
    int mpi_index_row;
    int mpi_index_column;

    const MatrixInt* const result = matrix_int_init(matrix1->rows, matrix2->columns);

    const clock_t CLOCK_START = clock();

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        const int TOTAL_TASKS = (matrix1->rows * matrix2->columns);
        const int WORKER_PAYLOAD = TOTAL_TASKS / mpi_comm_size;

        int index;

        for (int i = 1; i < mpi_comm_size; ++i)
        {
            mpi_index_start = (i - 1) * WORKER_PAYLOAD;
            mpi_index_finish = (i == (mpi_comm_size - 1)) ? TOTAL_TASKS : mpi_index_start + WORKER_PAYLOAD;

            MPI_Send(&mpi_index_start, 1, MPI_INT, i, MPI_TAG_ROOT_MESSAGE, MPI_COMM_WORLD);
            MPI_Send(&mpi_index_finish, 1, MPI_INT, i, MPI_TAG_ROOT_MESSAGE, MPI_COMM_WORLD);
        }

        for (int i = 1; i < mpi_comm_size; ++i)
        {
            index = 0;

            MPI_Recv(&mpi_index_finish, 1, MPI_INT, i, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD, &mpi_status);
            MPI_Recv(&mpi_index_start, 1, MPI_INT, i, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD, &mpi_status);

            mpi_stripe = (int*)malloc((mpi_index_finish - mpi_index_start) * sizeof(int));

            MPI_Recv(mpi_stripe, (mpi_index_finish - mpi_index_start), MPI_INT, i, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD, &mpi_status);

            for (int j = mpi_index_start; j < mpi_index_finish; ++j)
            {
                mpi_index_row = j / matrix1->columns;
                mpi_index_column = j % matrix2->columns;

                matrix_int_set(result, mpi_index_row, mpi_index_column, *(mpi_stripe + index++));
            }

            free(mpi_stripe);
        }
    }
    else
    {
        int sum;
        int index = 0;
        int* matrix_row;
        int* matrix_column;

        MPI_Recv(&mpi_index_start, 1, MPI_INT, MPI_ROOT_RANK, MPI_TAG_ROOT_MESSAGE, MPI_COMM_WORLD, &mpi_status);
        MPI_Recv(&mpi_index_finish, 1, MPI_INT, MPI_ROOT_RANK, MPI_TAG_ROOT_MESSAGE, MPI_COMM_WORLD, &mpi_status);

        mpi_stripe = (int*)malloc((mpi_index_finish - mpi_index_start) * sizeof(int));

        if (mpi_stripe == NULL)
        {
            perror("matrix_int_multiply_mpi_blocking. Memory allocation failed.\n");
            MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            return NULL;
        }

        for (int i = mpi_index_start; i < mpi_index_finish; ++i)
        {
            sum = 0;

            mpi_index_row = i / matrix1->columns;
            mpi_index_column = i % matrix2->columns;

            matrix_row = matrix_int_get_row(matrix1, mpi_index_row);
            matrix_column = matrix_int_get_column(matrix2, mpi_index_column);

            for (int j = 0; j < matrix1->columns; ++j)
            {
                sum += (*(matrix_row + j)) * (*(matrix_column + j));
            }

            *(mpi_stripe + index++) = sum;
        }

        MPI_Send(&mpi_index_finish, 1, MPI_INT, MPI_ROOT_RANK, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD);
        MPI_Send(&mpi_index_start, 1, MPI_INT, MPI_ROOT_RANK, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD);
        MPI_Send(mpi_stripe, index, MPI_INT, MPI_ROOT_RANK, MPI_TAG_WORKER_MESSAGE, MPI_COMM_WORLD);

        free(mpi_stripe);
    }

    for (int i = 0; i < result->rows; ++i)
    {
        MPI_Bcast(matrix_int_get_row(result, i), matrix_int_get_columns_count(result), MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);
    }

    const clock_t CLOCK_FINISH = clock();

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        printf("Execution time: %.3f seconds\n", (double)(CLOCK_FINISH - CLOCK_START) / CLOCKS_PER_SEC);
    }

    return result;
}

# pragma endregion

void matrix_int_print(const MatrixInt* const matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        for (int j = 0; j < matrix->columns; ++j)
        {
            printf("%d ", matrix->matrix[i][j]);
        }
        printf("\n");
    }
}