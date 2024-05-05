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
    int **data;
} MatrixInt;

#pragma region init & free

void matrix_int_free(MatrixInt const *matrix)
{
    if (matrix == NULL)
    {
        return;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        free(matrix->data[i]);
    }

    free(matrix->data);
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

    MatrixInt *matrix = (MatrixInt*)malloc(sizeof(MatrixInt));

    if (matrix == NULL)
    {
        perror("Failed to allocate memory for MatrixInt");
        return NULL;
    }

    matrix->rows = rows;
    matrix->columns = columns;
    matrix->data = (int**)malloc(rows * sizeof(int*));

    if (matrix->data == NULL)
    {
        free(matrix->data);
        free(matrix);
        perror("Failed to allocate memory for MatrixInt");
        return NULL;
    }

    for (int i = 0; i < rows; ++i)
    {
        matrix->data[i] = (int*)malloc(columns * sizeof(int));

        if (matrix->data[i] == NULL)
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

int** matrix_int_get_matrix(const MatrixInt const* matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return NULL;
    }

    return matrix->data;
}

int matrix_int_get_rows_count(const MatrixInt const* matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return -1;
    }

    return matrix->rows;
}

int matrix_int_get_columns_count(const MatrixInt const* matrix)
{
    if (matrix == NULL)
    {
        perror("Invalid matrix argument");
        return -1;
    }

    return matrix->columns;
}

int* matrix_int_get_flatten_data(const MatrixInt const* matrix)
{
    int *flatten_data = malloc((matrix->rows * matrix->columns) * sizeof(int));

    if (flatten_data == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int index = 0;

    for (int i = 0; i < matrix->rows; ++i)
    {
        for (int j = 0; j < matrix->columns; ++j)
        {
            flatten_data[index++] = matrix->data[i][j];
        }
    }

    return flatten_data;
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

    return matrix->data[index];
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

    int *column = (int*)malloc(matrix->rows * sizeof(int));

    if (column == NULL)
    {
        perror("Memory allocation failed");
        return NULL;
    }

    for (int i = 0; i < matrix->rows; ++i)
    {
        column[i] = matrix->data[i][index];
    }

    return column;
}

void matrix_int_set_matrix_data(MatrixInt const *matrix, const int const **data)
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
            matrix->data[i][j] = data[i][j];
        }
    }
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

    return matrix->data[row_index][column_index];
}

void matrix_int_set(MatrixInt const *matrix, const int row_index, const int column_index, const int value)
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
    
    matrix->data[row_index][column_index] = value;
}

#pragma endregion

#pragma region general functions

void matrix_int_fill_random(MatrixInt const *matrix, const int min, const int max)
{
    srand((unsigned int)time(NULL));

    for (int i = 0; i < matrix->rows; ++i)
    {
        for (int j = 0; j < matrix->columns; ++j)
        {
            matrix->data[i][j] = rand() % (max - min + 1) + min;
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
            if (matrix1->data[i][j] != matrix2->data[i][j])
            {
                return false;
            }
        }
    }

    return true;
}

MatrixInt* matrix_int_unflatten_data(const int rows, const int columns, const int const *flatten_data)
{
    if (flatten_data == NULL)
    {
        perror("matrix_int_unflatten_matrix. Invalid flatten_data argument");
        return NULL;
    }

    MatrixInt *matrix = matrix_int_init(rows, columns);

    if (matrix == NULL)
    {
        perror("matrix_int_unflatten_matrix. Memory alloaction failed");
        return NULL;
    }

    int index = 0;

    for (int i = 0; i < rows; ++i)
    {
        for (int j = 0; j < columns; ++j)
        {
            matrix->data[i][j] = flatten_data[index++];
        }
    }

    return matrix;
}

#pragma endregion

#pragma region multiplication functions

MatrixInt* matrix_int_multiply_sequential(const MatrixInt const *matrix1, const MatrixInt const *matrix2)
{
    if (matrix1->columns != matrix2->rows)
    {
        perror("Matrices are not multipliable.");
        return NULL;
    }

    const MatrixInt const *RESULT = matrix_int_init(matrix1->rows, matrix2->columns);
    
    if (RESULT == NULL)
    {
        return NULL;
    }

    const clock_t TIMESTEP_START = clock();

    for (int i = 0; i < RESULT->rows; ++i)
    {
        for (int j = 0; j < RESULT->columns; ++j)
        {
            RESULT->data[i][j] = 0;

            for (int k = 0; k < matrix1->columns; ++k)
            {
                RESULT->data[i][j] += matrix1->data[i][k] * matrix2->data[k][j];
            }
        }
    }

    const clock_t TIMESTEP_FINISH = clock();

    printf("matrix_int_multiply_sequential. Execution time: %.3f seconds.\n", (double)(TIMESTEP_FINISH - TIMESTEP_START) / CLOCKS_PER_SEC);

    return RESULT;
}

MatrixInt* matrix_int_multiply_mpi_collective(const MatrixInt const* matrix1, const MatrixInt const* matrix2)
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
        perror("At least 2 MPI processors are required.");
        return NULL;
    }

    const clock_t TIMESTEP_START = clock();

    const int WORKER_PAYLOAD = matrix1->rows / mpi_comm_size * matrix1->columns;

    int *counts_buffer = (int*)malloc(mpi_comm_size * sizeof(int));

    if (counts_buffer == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int *displacements_buffer = (int*)malloc(mpi_comm_size * sizeof(int));

    if (displacements_buffer == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int index_start;
    int index_finish;
    int receiver_id = 0;

    for (int i = 0; i < mpi_comm_size; ++i)
    {
        index_start = i * WORKER_PAYLOAD;
        index_finish = (i == (mpi_comm_size - 1) ? (matrix1->rows * matrix1->columns) : (index_start + WORKER_PAYLOAD));

        *(displacements_buffer + receiver_id) = index_start;
        *(counts_buffer + receiver_id) = (index_finish - index_start);

        ++receiver_id;
    }

    int receive_count = counts_buffer[mpi_comm_rank];

    int *receive_buffer = (int*)malloc(receive_count * sizeof(int));

    if (receive_buffer == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int *matrix_row = (int*)malloc(matrix1->columns * sizeof(int));

    if (matrix_row == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int *local_results = (int*)malloc((receive_count) * sizeof(int));

    if (local_results == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int *global_results = (int*)malloc((matrix1->rows * matrix1->columns) * sizeof(int));

    if (global_results == NULL)
    {
        perror("matrix_int_multiply_mpi_collective. Memory allocation failed");
        return NULL;
    }

    int const *flatten_data = matrix_int_get_flatten_data(matrix1);

    MPI_Scatterv(flatten_data, counts_buffer, displacements_buffer, MPI_INT, receive_buffer, receive_count, MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);

    int local_index = 0;
    int local_index_row;
    int local_index_column;

    int *local_matrix_column;

    index_start = 0;
    index_finish = counts_buffer[mpi_comm_rank];

    for (int i = index_start; i < index_finish; i += matrix1->columns)
    {
        for (int  j = 0; j < matrix2->columns; ++j)
        {
            local_results[local_index] = 0;

            for (int k = 0; k < matrix1->columns; ++k)
            {
                matrix_row[k] = receive_buffer[i + k];
            }

            local_matrix_column = matrix_int_get_column(matrix2, j);

            for (int k = 0; k < matrix1->columns; ++k)
            {
                local_results[local_index] += matrix_row[k] * local_matrix_column[k];
            }

            ++local_index;

            free(local_matrix_column);
        }
    }

    MPI_Gatherv(local_results, receive_count, MPI_INT, global_results, counts_buffer, displacements_buffer, MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);

    const MatrixInt const *RESULT = matrix_int_unflatten_data(matrix1->rows, matrix2->columns, global_results);

    free(matrix_row);
    free(flatten_data);
    free(counts_buffer);
    free(local_results);
    free(global_results);
    free(receive_buffer);
    free(displacements_buffer);

    const clock_t TIMESTEP_FINISH = clock();

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        printf("matrix_int_multiply_mpi_collective. Execution time: %.3f seconds.\n", (double)(TIMESTEP_FINISH - TIMESTEP_START) / CLOCKS_PER_SEC);
    }

    return RESULT;
}

# pragma endregion