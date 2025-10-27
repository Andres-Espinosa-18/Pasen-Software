import matplotlib.pyplot as plt

# Datos obtenidos de la ejecución del programa 
# Tiempo suma secuencial: 0.00600 segundos
# Tiempo suma SIMD (SSE): 0.00300 segundos
# Aceleración SIMD: 2.00x

etiquetas = ['Suma Secuencial', 'Suma SIMD (SSE)']
tiempos = [0.00600, 0.00300]
aceleracion = tiempos[0] / tiempos[1]

fig, ax = plt.subplots()

barras = ax.bar(etiquetas, tiempos, color=['#1f77b4', '#ff7f0e'])

ax.set_title(f'Comparación de Tiempos de Ejecución (Aceleración: {aceleracion:.2f}x)', fontsize=14)
ax.set_ylabel('Tiempo de Ejecución (segundos)', fontsize=12)
ax.set_xlabel('Tipo de Ejecución', fontsize=12)

plt.show()