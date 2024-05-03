#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "mpi.h"
#include "matrix.h"

#define MPI_ROOT_RANK 0
#define MPI_TAG_ROOT_MESSAGE 1
#define MPI_TAG_WORKER_MESSAGE 2

int main(const int argc, const char const **argv)
{
    MPI_Init(&argc, &argv);

    int mpi_comm_rank;
    int mpi_comm_size;

    MPI_Comm_rank(MPI_COMM_WORLD, &mpi_comm_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &mpi_comm_size);

    MatrixInt const *matrix1 = matrix_int_init(500, 500);
    MatrixInt const *matrix2 = matrix_int_init(500, 500);

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        matrix_int_fill_random(matrix1, 0, 100);
        matrix_int_fill_random(matrix2, 0, 100);
    }

    for (int j = 0; j < matrix_int_get_rows_count(matrix1); ++j)
    {
        MPI_Bcast(matrix_int_get_row(matrix1, j), matrix_int_get_columns_count(matrix1), MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);
        MPI_Bcast(matrix_int_get_row(matrix2, j), matrix_int_get_columns_count(matrix2), MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);
    }

    const MatrixInt const *RESULT_MPI_BLOCKING = matrix_int_multiply_mpi_blocking(matrix1, matrix2);

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        const MatrixInt const *RESULT_SEQUENTIAL = matrix_int_multiply_sequential(matrix1, matrix2);

        printf("MPI Blocking vs Sequential: %s\n", matrix_int_are_equal(RESULT_MPI_BLOCKING, RESULT_SEQUENTIAL) == 1 ? "Equal" : "Not equal");

        matrix_int_free(RESULT_SEQUENTIAL);
    }

    matrix_int_free(matrix1);
    matrix_int_free(matrix2);
    matrix_int_free(RESULT_MPI_BLOCKING);

    MPI_Finalize();

    return EXIT_SUCCESS;
}