package MatrizInversa;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatrizInversa {

    public static double[][] leerMatriz(String nombreArchivo) {
        files fileManager = new files (); 
        int[] datos = fileManager.fileToIntArray(nombreArchivo);
        
        if (datos == null || datos.length < 2) return null;
        
        int n = datos[0];
        if (datos.length != (n * n) + 1) return null;
        
        double[][] matriz = new double[n][n];
        int k = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = datos[k++];
            }
        }
        return matriz;
    }

    // Determinante recursivo
    public static double determinante(double[][] matriz) {
        int n = matriz.length;
        if (n == 1) return matriz[0][0];
        if (n == 2) return (matriz[0][0] * matriz[1][1]) - (matriz[0][1] * matriz[1][0]);
        
        double det = 0;
        for (int j = 0; j < n; j++) {
            det += Math.pow(-1, j) * matriz[0][j] * determinante(subMatriz(matriz, 0, j));
        }
        return det;
    }

    public static double[][] subMatriz(double[][] matriz, int fila, int col) {
        int n = matriz.length;
        double[][] sub = new double[n - 1][n - 1];
        int r = 0, c = 0;
        for (int i = 0; i < n; i++) {
            if (i == fila) continue;
            c = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                sub[r][c++] = matriz[i][j];
            }
            r++;
        }
        return sub;
    }

    public static double[][] calcularInversa(double[][] matriz) {
        System.out.println("\n--- Paso 1: Calcular Determinante ---");
        double det = determinante(matriz);
        System.out.printf("Determinante = %.2f\n", det);

        if (Math.abs(det) < 1e-9) {
            System.out.println("Error: El determinante es 0, no tiene inversa.");
            return null;
        }

        int n = matriz.length;
        double[][] adjunta = new double[n][n];
        double[][] inversa = new double[n][n];

        System.out.println("\n--- Paso 2: Calcular Adjunta (Cofactores Transpuestos) ---");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double detSub = determinante(subMatriz(matriz, i, j));
                // Calculamos cofactor y transponemos (guardamos en [j][i])
                adjunta[j][i] = Math.pow(-1, i + j) * detSub; 
            }
        }
        imprimirMatriz(adjunta);

        System.out.println("\n--- Paso 3: Dividir Adjunta entre Determinante ---");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = adjunta[i][j] / det;
            }
        }

        return inversa;
    }

    public static void imprimirMatriz(double[][] matriz) {
        for (double[] fila : matriz) {
            System.out.print("| ");
            for (double val : fila) System.out.printf("%8.2f ", val);
            System.out.println("|");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Archivo de matriz: ");
        String archivo = br.readLine();
        
        double[][] matriz = leerMatriz(archivo);
        
        if (matriz != null) {
            System.out.println("\nMatriz Original:");
            imprimirMatriz(matriz);
            
            double[][] inversa = calcularInversa(matriz);
            
            if (inversa != null) {
                System.out.println("\n=== MATRIZ INVERSA RESULTANTE ===");
                imprimirMatriz(inversa);
            }
        } else {
            System.out.println("Error leyendo archivo.");
        }
    }
}
    
