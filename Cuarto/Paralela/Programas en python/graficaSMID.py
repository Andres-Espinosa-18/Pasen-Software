import matplotlib.pyplot as plt
import pandas as pd
import os
# Muestra la carpeta actual donde Python está buscando.
print(os.getcwd())
contenido_actual = os.listdir() 
print(f"Contenido del directorio actual:\n{contenido_actual}\n")
# Datos obtenidos de la ejecución del programa 
# Tiempo suma secuencial: 0.00600 segundos
# Tiempo suma SIMD (SSE): 0.00300 segundos
# Aceleración SIMD: 2.00x

try:
    df = pd.read_csv('GitHub\Pasen-Software\Cuarto\Paralela\Programas en python\DATOS.csv') 
    
    # Opcional: Mostrar las primeras filas para verificar la carga
    print("Datos cargados:\n", df.head())
    
except FileNotFoundError:
    print("Error: El archivo 'Libro1.csv' no se encontró. Verifica la ruta.")
    exit()

Deivis_df = df.iloc[0:60, :] 
Andres_df = df.iloc[60:120, :]
David_df = df.iloc[120:180, :]
Yo_df = df.iloc[180:240, :]

print("--- Contenido de los Grupos en df_bloque_1 ---")
grupos = Yo_df.groupby('Datos')
for nombre_del_grupo, df_del_grupo in grupos:
    print(f"\n=====================================")
    print(f"GRUPO: Datos = {nombre_del_grupo}")
    print(f"Número de muestras: {len(df_del_grupo)}")

dff = grupos.get_group(20000000)
Tiempo_Secuencial_s = dff.iloc[:, 0].tolist()
Tiempo_SIMD_SSE_s = dff.iloc[:, 1].tolist()
aceleracion = dff.iloc[:, 2].tolist()

df = dff
valores_datos_unicos = df['Datos'].unique()

for valor in valores_datos_unicos:
    df_grupo = df[df['Datos'] == valor].reset_index(drop=True)
    df_grupo.index = range(1, len(df_grupo) + 1)
    muestras = df_grupo.index

    # --- Plot 1: Tiempo Comparación ---
    plt.figure(figsize=(10, 6))
    plt.plot(muestras, df_grupo['Tiempo_Secuencial_s'], label='Tiempo Secuencial (s)', marker='o', linestyle='-', color='blue', alpha=0.7)
    plt.plot(muestras, df_grupo['Tiempo_SIMD_SSE_s'], label='Tiempo SIMD SSE (s)', marker='x', linestyle='--', color='red', alpha=0.7)
    plt.title(f'Comparación de Tiempos vs Muestra | Datos con {valor} datos')
    plt.xlabel('Muestra (índice)')
    plt.ylabel('Tiempo (segundos)')
    plt.legend()
    plt.grid(True)
    plt.savefig(f"Yo_tiempo_comparacion_datos_{valor}.png")
    plt.close()

    # --- Plot 2: Aceleración ---
    plt.figure(figsize=(10, 6))
    plt.plot(muestras, df_grupo['Aceleracion_SIMD_x'], label='Aceleración SIMD (x)', marker='s', linestyle='-', color='green', linewidth=1.5)
    plt.axhline(1, color='gray', linestyle='--', label='Sin Aceleración (1x)')
    plt.title(f'Aceleración vs Muestra | Datos con {valor} datos')
    plt.xlabel('Muestra (índice)')
    plt.ylabel('Aceleración (Factor X)')
    plt.legend()
    plt.grid(True)
    plt.savefig(f"Yo_aceleracion_vs_muestra_datos_{valor}.png")
    plt.close()


plt.show()
