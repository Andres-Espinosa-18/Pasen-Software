import matplotlib.pyplot as plt
import numpy as np

# Datos de ejemplo para 4, 8, 14 y 20 núcleos
data_points = [100, 1000, 10000, 1000000]
core_counts = ['4 Hilos', '8 Hilos', '14 Hilos', '20 Hilos']

# Tiempos simulados (en ms, decreciendo con más núcleos)
time_4_cores = [0, 0, 0, 0]
time_8_cores = [15, 16, 15, 16]
time_14_cores = [31, 16, 16, 31]
time_20_cores = [15, 16, 15, 32]

all_times = [time_4_cores, time_8_cores, time_14_cores, time_20_cores]

# Configuración del gráfico
x = np.arange(len(data_points)) # Posiciones de las etiquetas de los datos
width = 0.20 # Ancho de cada barra

# Crear la figura y los ejes
fig, ax = plt.subplots(figsize=(12, 7))

# Definir las posiciones de las barras (4 grupos)
positions = [x - 1.5 * width, x - 0.5 * width, x + 0.5 * width, x + 1.5 * width]
colors = ['skyblue', 'lightcoral', 'lightgreen', 'gold']

# Dibujar las barras para cada configuración de núcleos
for i in range(len(core_counts)):
    ax.bar(positions[i], all_times[i], width, label=core_counts[i], color=colors[i])

# Configuración de etiquetas, título y leyenda
ax.set_ylabel('Tiempo (ms)', fontsize=12)
ax.set_xlabel('Punto de Dato', fontsize=12)
ax.set_title('Comparación de Tiempo de Procesamiento: 4, 8, 14 y 20 Hilos', fontsize=14)
ax.set_xticks(x)
ax.set_xticklabels(data_points, rotation=0)
ax.legend(loc='upper right', fontsize=10)
ax.grid(axis='y', linestyle='--', alpha=0.7)

# Ajustar diseño y guardar la figura
fig.tight_layout()
plt.savefig('comparacion_tiempos_hilos.png')