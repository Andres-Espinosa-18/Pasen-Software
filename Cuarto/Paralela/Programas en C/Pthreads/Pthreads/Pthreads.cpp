// Pthreads.cpp : Este archivo contiene la función "main". La ejecución del programa comienza y termina ahí.
//

#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <math.h>

#define N 1000000
#define NUM_HILOS 14

float datos[N];

typedef struct {
    int inicio;
    int fin;
    int operacion;
} Tarea;

DWORD WINAPI procesar(LPVOID arg) {
    Tarea* t = (Tarea*)arg;
    for (int i = t->inicio; i <= t->fin; i++) {
        switch (t->operacion) {
        case 0: datos[i] = log10f(datos[i]); break;
        case 1: datos[i] = log(datos[i]); break;
        case 2: datos[i] = cos(datos[i]); break;
        case 3: datos[i] = sin(datos[i]); break;
        case 4: datos[i] = sqrt(datos[i]); break;
        case 5: datos[i] = datos[i] * datos[i]; break;
        case 6: datos[i] = datos[i] - (N / NUM_HILOS); break;
        case 7: datos[i] = datos[i] + (N / NUM_HILOS); break;
        }
    }
    return 0;
}

int main() {
    HANDLE hilos[NUM_HILOS];
    Tarea tareas[NUM_HILOS];
    DWORD tiempoInicio, tiempoFin, tiempoTotal;

    // Inicializar datos
    for (int i = 0; i < N; i++) {
        datos[i] = (float)(i + 1);
    }

    int rep = 0;
    printf("Datos sin procesar: \n");
    while (rep < NUM_HILOS)
    {

        for (int i = 0; i < 10; i++)
            printf("Datos[%i] = %.4f\n", (i + 1) + (N / NUM_HILOS * rep), datos[i + (N / NUM_HILOS * rep)]);
        rep++;
    }

    // Medir tiempo inicial
    tiempoInicio = GetTickCount();

    // Crear hilos
    for (int i = 0; i < NUM_HILOS; i++) {
        tareas[i].inicio = i * N / NUM_HILOS;
        tareas[i].fin = (i == NUM_HILOS - 1) ? N - 1 : (tareas[i].inicio + N / NUM_HILOS - 1);
        tareas[i].operacion = i;
        hilos[i] = CreateThread(NULL, 0, procesar, &tareas[i], 0, NULL);
    }

    WaitForMultipleObjects(NUM_HILOS, hilos, TRUE, INFINITE);

    tiempoFin = GetTickCount();

    tiempoTotal = tiempoFin - tiempoInicio;

    for (int i = 0; i < NUM_HILOS; i++) {
        CloseHandle(hilos[i]);
    }

    rep = 0;
    printf("\nDatos Procesados: \n");
    while (rep < NUM_HILOS)
    {

        for (int i = 0; i < 10; i++)
            printf("Datos[%i] = %.4f\n", (i + 1) + (N / NUM_HILOS * rep), datos[i + (N / NUM_HILOS * rep)]);
        rep++;
    }

    printf("\nTiempo total de ejecucion: %lu ms\n", tiempoTotal);
    printf("Numero de datos: %i\n", N);
    printf("Numero de nucleos: %i", NUM_HILOS);

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
