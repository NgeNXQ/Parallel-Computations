#include <stdio.h>
#include <stdlib.h>

#include "mpi.h"
#include "matrix.h"

#define MPI_ROOT_RANK 0
#define MPI_TAG_ROOT_MESSAGE 1
#define MPI_TAG_WORKER_MESSAGE 2

int main(int argc, char** argv)
{
    MPI_Init(&argc, &argv);

    int mpi_comm_rank;
    int mpi_comm_size;

    MPI_Comm_rank(MPI_COMM_WORLD, &mpi_comm_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &mpi_comm_size);

    const MatrixInt const *matrix1 = matrix_int_init(500, 500);
    const MatrixInt const *matrix2 = matrix_int_init(500, 500);

    if (mpi_comm_rank == MPI_ROOT_RANK)
    {
        matrix_int_fill_random(matrix1, 0, 100);
        matrix_int_fill_random(matrix2, 0, 100);
    }

    for (int i = 0; i < matrix_int_get_rows_count(matrix1); ++i)
    {
        MPI_Bcast(matrix_int_get_row(matrix1, i), matrix_int_get_columns_count(matrix1), MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);
        MPI_Bcast(matrix_int_get_row(matrix2, i), matrix_int_get_columns_count(matrix2), MPI_INT, MPI_ROOT_RANK, MPI_COMM_WORLD);
    }

    const MatrixInt const *result1 = matrix_int_multiply_sequential(matrix1, matrix2);
    const MatrixInt const* result2 = matrix_int_multiply_mpi_blocking(matrix1, matrix2);

    printf("Rank: %2d | %d.", mpi_comm_rank, matrix_int_are_equal(result1, result2));

    MPI_Finalize();

    return EXIT_SUCCESS;
}
