// Aplicacion SIMD 1M.cpp : Este archivo contiene la función "main". La ejecución del programa comienza y termina ahí.
//

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <xmmintrin.h> // SSE
#include <climits>

#define N 20000000

float A[N], B[N], C[N];

void inicializar_vectores() {
    for (int i = 0; i < N; i++) {
        A[i] = (float)i;
        B[i] = (float)(2 * 1);
    }
}

void suma_secuencial() {
    for (int i = 0; i < N; i++) {
        C[i] = A[i] + B[i];
    }
}

void suma_simd_sse() {
    for (int i = 0; i < N; i += 4) {
        __m128 va = _mm_loadu_ps(&A[i]);
        __m128 vb = _mm_loadu_ps(&B[i]);
        __m128 vc = _mm_add_ps(va, vb);
        _mm_storeu_ps(&C[i], vc);
    }
}


int main()
{
    clock_t start, end;
    double tiempo_secuencial, tiempo_simd;
    //printf("Inicializando vectores...\n");
    inicializar_vectores();
    //printf("Iniciando suma secuencial...\n");
    start = clock();
        suma_secuencial();
    end = clock();
    tiempo_secuencial = (double)(end - start) / CLOCKS_PER_SEC;
    //printf("Tiempo suma secuencial: %.5f segundos\n", tiempo_secuencial);
    //printf("Iniciando suma SIMD con SSE...\n");
    start = clock();
        suma_simd_sse();
    end = clock();
    tiempo_simd = (double)(end - start) / CLOCKS_PER_SEC;
    //printf("Tiempo suma SIMD (SSE): %.5f segundos\n", tiempo_simd);
    //printf("Aceleracion SIMD: %.2fx\n", tiempo_secuencial / tiempo_simd);
    // ----------------------------------------------------------------------
// ESTA ES LA ÚNICA LÍNEA QUE DEBE IMPRIMIR DATOS EN EL main()
// ----------------------------------------------------------------------
printf("%.6f,%.6f,%.6f,%d\n", tiempo_secuencial, tiempo_simd, tiempo_secuencial / tiempo_simd, N);
// ----------------------------------------------------------------------

// ... (todas las otras líneas de printf como "Inicializando vectores...", etc. deben estar COMENTADAS)
    return 0;
}

// Ejecutar programa: Ctrl + F5 o menú Depurar > Iniciar sin depurar
// Depurar programa: F5 o menú Depurar > Iniciar depuración

// Sugerencias para primeros pasos: 1. Use la ventana del Explorador de soluciones para agregar y administrar archivos
//   2. Use la ventana de Team Explorer para conectar con el control de código fuente
//   3. Use la ventana de salida para ver la salida de compilación y otros mensajes
//   4. Use la ventana Lista de errores para ver los errores
//   5. Vaya a Proyecto > Agregar nuevo elemento para crear nuevos archivos de código, o a Proyecto > Agregar elemento existente para agregar archivos de código existentes al proyecto
//   6. En el futuro, para volver a abrir este proyecto, vaya a Archivo > Abrir > Proyecto y seleccione el archivo .sln
