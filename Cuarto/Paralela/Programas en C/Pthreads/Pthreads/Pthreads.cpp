// Pthreads.cpp : Este archivo contiene la función "main". La ejecución del programa comienza y termina ahí.
//

#include <stdio.h>
#include <stdlib.h>
#include <thread>
#include <math.h>

#define N 100
#define NUM_HILOS 4

float datos[N];

typedef struct {
    int inicio;
    int fin;
    int operacion;
} Tarea;

void* procesar(void* arg) {
    Tarea* t = (Tarea*)arg;
    for (int i = t->inicio; i <= t->fin; i++) {
        switch (t->operacion) {
        case 0: datos[i] = datos[i] * datos[i]; break;
        case 1: datos[i] = datos[i] * 2; break;
        case 2: datos[i] = datos[i] + 5; break;
        case 3: datos[i] = sqrt(datos[i]); break;
        }
    }
    pthread_exit(NULL);
}

int main() {
    pthread_t hilos[NUM_HILOS];
    Tarea tareas[NUM_HILOS];

    for (int i = 0; i < N; i++) {
        datos[i] = (float)(i + 1);
    }

    for (int i = 0; i < NUM_HILOS; i++) {
        tareas[i].inicio = i * 25;
        tareas[i].fin = tareas[i].inicio + 24;
        tareas[i].operacion = i;
        pthread_create(&hilos[i], NULL, procesar, &tareas[i]);
    }

    for (int i = 0; i < NUM_HILOS; i++) {
        pthread_join(hilos[i], NULL);
    }

    for (int i = 0; i < N; i++) {
        printf("datos[%d] = %.2f\n", i, datos[i]);
    }

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
