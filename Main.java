import java.io.BufferedReader;
import java.io.BufferedWriter; 
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// Clase principal para generar reportes de ventas
public class Main {
    // Punto de entrada del programa
    public static void main(String[] args) {
        try {
            // 1. Calcula ventas totales por región
            Map<String, Double> ventasPorRegion = calcularVentasPorRegion(
                "ventas.csv", 
                "vendedores.csv", 
                "productos.csv"
            );
            generarReporte("reporte_ventas_region.csv", ventasPorRegion, "Región", "Ventas Totales");

            // 2. Calcula productos más vendidos
            Map<String, Integer> productosVendidos = calcularProductosVendidos("ventas.csv");
            generarReporte("reporte_productos_vendidos.csv", productosVendidos, "Producto", "Cantidad");

            System.out.println("✅ Reportes generados con éxito");
        } catch (IOException e) {
            System.err.println("❌ Error generando reportes: " + e.getMessage());
        }
    }

    // Calcula el total de ventas por región
    private static Map<String, Double> calcularVentasPorRegion(
        String archivoVentas, 
        String archivoVendedores, 
        String archivoProductos
    ) throws IOException {
        // Mapa para relacionar vendedores con su región
        Map<String, String> vendedorRegion = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoVendedores))) {
            lector.readLine();  // Saltamos la cabecera
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\|");
                vendedorRegion.put(partes[0], partes[2]);  // ID -> Región
            }
        }

        // Mapa para obtener precios de productos
        Map<String, Double> preciosProductos = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoProductos))) {
            lector.readLine();  // Saltamos cabecera
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\|");
                preciosProductos.put(partes[0], Double.parseDouble(partes[2]));
            }
        }

        // Acumulador para totales por región
        Map<String, Double> totales = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoVentas))) {
            lector.readLine();  // Saltamos cabecera
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\|");
                String region = vendedorRegion.get(partes[0]);  // Obtenemos región del vendedor
                double precioUnitario = preciosProductos.get(partes[1]);  // Precio del producto
                double totalVenta = precioUnitario * Integer.parseInt(partes[2]);  // Precio x Cantidad
                
                // Sumamos al total de la región
                totales.put(region, totales.getOrDefault(region, 0.0) + totalVenta);
            }
        }
        return totales;
    }

    // Cuenta cuántas unidades se vendieron de cada producto
    private static Map<String, Integer> calcularProductosVendidos(String archivoVentas) throws IOException {
        Map<String, Integer> acumulador = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoVentas))) {
            lector.readLine();  // Saltamos cabecera
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("\\|");
                String idProducto = partes[1];
                int cantidad = Integer.parseInt(partes[2]);
                acumulador.put(idProducto, acumulador.getOrDefault(idProducto, 0) + cantidad);
            }
        }
        return acumulador;
    }

    // Genera un archivo CSV a partir de un mapa de datos
    private static <T> void generarReporte(
        String nombreArchivo, 
        Map<String, T> datos, 
        String encabezado1, 
        String encabezado2
    ) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            escritor.write(String.format("%s|%s\n", encabezado1, encabezado2));  // Cabecera
            for (Map.Entry<String, T> entrada : datos.entrySet()) {
                escritor.write(String.format("%s|%s\n", entrada.getKey(), entrada.getValue()));
            }
        }
    }
}