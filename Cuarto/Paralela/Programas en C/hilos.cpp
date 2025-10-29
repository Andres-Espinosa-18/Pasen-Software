#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
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