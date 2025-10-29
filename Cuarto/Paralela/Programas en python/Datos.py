import subprocess
import pandas as pd
import io
import os

# =========================================================================
# === 1. CONFIGURACIÓN: AJUSTA ESTAS TRES VARIABLES ========================
# =========================================================================

# 1. RUTA DEL EJECUTABLE:
# DEBES REEMPLAZAR ESTO con la ruta de tu archivo .exe generado por VS 2022.
# Usa una 'r' al inicio para manejar correctamente las barras invertidas de Windows.
# EJEMPLO TÍPICO DE VS 2022:
EJECUTABLE_PATH = r"GitHub\Pasen-Software\Cuarto\Paralela\Programas en C\Comptuacion SIMD\x64\Debug\Aplicacion SIMD 1M.exe"


# 2. Tamaños de 'Datos' (N) a medir
DATOS_VALORES = [1000000]

# 3. Número de muestras por cada tamaño de datos
N_MUESTRAS_POR_BLOQUE = 15 

# =========================================================================
# === 2. LÓGICA DE EJECUCIÓN Y PARSEO ======================================
# =========================================================================

datos_csv = []
columnas = ['Tiempo_Secuencial_s', 'Tiempo_SIMD_SSE_s', 'Aceleracion_SIMD_x', 'Datos']

print(f"Iniciando la generación de datos (Total: {len(DATOS_VALORES) * N_MUESTRAS_POR_BLOQUE} ejecuciones)...")

if not os.path.exists(EJECUTABLE_PATH):
    print(f"\nFATAL ERROR: El ejecutable NO fue encontrado en la ruta:\n{EJECUTABLE_PATH}")
    print("Por favor, verifica la ruta y la compilación en VS 2022 (Debug/Release).")
    exit()

# Bucle principal: Itera sobre cada tamaño de datos (N)
for n_datos in DATOS_VALORES:
    print(f"\nGenerando {N_MUESTRAS_POR_BLOQUE} muestras para N = {n_datos}...")
    
    # Bucle secundario: Repite la medición 15 veces
    for i in range(N_MUESTRAS_POR_BLOQUE):
        try:
            # 1. Ejecutar el programa C/C++ con el argumento N
            resultado = subprocess.run(
                [EJECUTABLE_PATH, str(n_datos)],
                capture_output=True, # Captura la salida (stdout y stderr)
                text=True,           # Captura como texto (string)
                check=True           # Lanza excepción si el código de retorno es distinto de 0
            )
            
            # 2. PARSEO CLAVE:
            # La salida es la línea de CSV (ej: 0.003456,0.001728,2.000000,1000000\n)
            salida_linea = resultado.stdout.strip()
            
            # Divide la línea por comas y convierte los valores
            valores = salida_linea.split(',')
            
            # Asegura que hay 4 valores y los convierte al tipo correcto
            if len(valores) == 4:
                datos_csv.append([float(valores[0]), float(valores[1]), float(valores[2]), int(valores[3])])
                print(f"  Muestra {i+1}/{N_MUESTRAS_POR_BLOQUE} - OK (T_seq={valores[0]})")
            else:
                 print(f"  ERROR DE FORMATO: Salida inesperada: '{salida_linea}'")
                 continue
            
        except subprocess.CalledProcessError as e:
            # Captura errores del ejecutable (ej. problemas de memoria)
            print(f"  ERROR DE EJECUCIÓN para N={n_datos}: {e.stderr.strip()}")
            continue
        except Exception as e:
             # Captura cualquier otro error (incluyendo el "could not convert string to float")
             print(f"  ERROR INESPERADO en la Muestra {i+1}/{N_MUESTRAS_POR_BLOQUE}: {e}")
             continue


# 3. Crear el DataFrame y el CSV
df_final = pd.DataFrame(datos_csv, columns=columnas)

# Guardar el DataFrame en un archivo CSV
nombre_archivo_csv = "resultados_rendimiento_generados20M.csv"
df_final.to_csv(nombre_archivo_csv, index=False)

print("\n========================================================")
print(f"✅ Proceso completado. Se generaron {len(df_final)} filas de datos.")
