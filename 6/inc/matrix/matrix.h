#pragma once

#include <stdbool.h>

typedef struct InternalMatrixInt MatrixInt;

#pragma region init & free

void matrix_int_free(MatrixInt const *matrix);
MatrixInt* matrix_int_init(const int rows, const int columns);

#pragma endregion

#pragma region getters & setters

int matrix_int_get_rows_count(const MatrixInt const *matrix);
int matrix_int_get_columns_count(const MatrixInt const *matrix);
int** matrix_int_get_matrix(const MatrixInt const *matrix);
void matrix_int_set_matrix(MatrixInt *matrix, const int const **data);
int* matrix_int_get_row(const MatrixInt const *matrix, const int index);
int* matrix_int_get_column(const MatrixInt const *matrix, const int index);
int matrix_int_get(const MatrixInt const *matrix, const int row_index, const int column_index);
void matrix_int_set(const MatrixInt const *matrix, const int row_index, const int column_index, const int value);

#pragma endregion

#pragma region general functions

void matrix_int_fill_random(const MatrixInt const *matrix, const int min, const int max);
bool matrix_int_are_equal(const MatrixInt const *matrix1, const MatrixInt const *matrix2);

#pragma endregion

#pragma region multiplication functions

MatrixInt* matrix_int_multiply_sequential(const MatrixInt const *matrix1, const MatrixInt const *matrix2);
MatrixInt* matrix_int_multiply_mpi_blocking(const MatrixInt const *matrix1, const MatrixInt const *matrix2);
//MatrixInt* matrix_int_multiply_mpi_non_blocking(const MatrixInt const* matrix_1, const MatrixInt const* matrix_2);

# pragma endregion