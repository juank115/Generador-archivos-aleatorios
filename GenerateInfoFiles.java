import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

// Clase para generar archivos de prueba con datos aleatorios
public class GenerateInfoFiles {
    // Listas fijas para generar datos coherentes
    private static final String[] PRODUCTOS = {"Laptop", "Teléfono", "Tablet", "Monitor", "Teclado"};
    private static final String[] VENDEDORES = {"Ana López", "Carlos Ruiz", "María García", "Pedro Sánchez"};
    private static final String[] REGIONES = {"Norte", "Sur", "Este", "Oeste"};
    private static final Random generador = new Random();  // Para números aleatorios

    // Punto de entrada del programa
    public static void main(String[] args) {
        try {
            // Crear los tres archivos base
            crearArchivoProductos("productos.csv", 10);      // 10 productos
            crearArchivoVendedores("vendedores.csv", 5);      // 5 vendedores
            crearArchivoVentas("ventas.csv", 20);             // 20 ventas
            System.out.println("¡Archivos creados correctamente!");
        } catch (IOException e) {
            System.err.println("Oops, hubo un error: " + e.getMessage());
        }
    }

    // Crea archivo de productos con precios aleatorios
    private static void crearArchivoProductos(String nombreArchivo, int cantidad) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            escritor.write("ID_Producto|Nombre|Precio\n");  // Cabecera
            
            for (int i = 1; i <= cantidad; i++) {
                String id = String.format("PROD_%03d", i);  // Ej: PROD_001
                String nombre = PRODUCTOS[generador.nextInt(PRODUCTOS.length)];
                double precio = 100 + (generador.nextDouble() * 500);  // Entre 100 y 600
                escritor.write(String.format("%s|%s|%.2f\n", id, nombre, precio));
            }
        }
    }

    // Crea archivo de vendedores con región aleatoria
    private static void crearArchivoVendedores(String nombreArchivo, int cantidad) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            escritor.write("ID_Vendedor|Nombre|Región\n");
            
            for (int i = 1; i <= cantidad; i++) {
                String id = String.format("VEND_%03d", i);  // Ej: VEND_001
                String nombre = VENDEDORES[generador.nextInt(VENDEDORES.length)];
                String region = REGIONES[generador.nextInt(REGIONES.length)];
                escritor.write(String.format("%s|%s|%s\n", id, nombre, region));
            }
        }
    }

    // Crea registro de ventas aleatorias
    private static void crearArchivoVentas(String nombreArchivo, int cantidad) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            escritor.write("ID_Vendedor|ID_Producto|Cantidad|Fecha\n");
            
            for (int i = 0; i < cantidad; i++) {
                // Selecciones aleatorias de IDs existentes
                String idVendedor = String.format("VEND_%03d", 1 + generador.nextInt(5));  // VEND_001 a VEND_005
                String idProducto = String.format("PROD_%03d", 1 + generador.nextInt(10));  // PROD_001 a PROD_010
                int cantidadVendida = 1 + generador.nextInt(10);  // Entre 1 y 10 unidades
                String fecha = String.format("2024-%02d-%02d", 1 + generador.nextInt(12), 1 + generador.nextInt(28));
                
                escritor.write(String.format("%s|%s|%d|%s\n", idVendedor, idProducto, cantidadVendida, fecha));
            }
        }
    }
}