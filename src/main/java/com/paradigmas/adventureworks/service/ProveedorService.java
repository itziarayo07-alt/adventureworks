package com.paradigmas.adventureworks.service;

import com.paradigmas.adventureworks.model.ProductoProveedor;
import com.paradigmas.adventureworks.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ProveedorService {

    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> generarReporte(String proveedor) throws Exception {
        List<ProductoProveedor> productos = repository.buscarPorProveedor(proveedor);

        Map<String, List<ProductoProveedor>> productosPorProveedor = new HashMap<>();

        for (ProductoProveedor producto : productos) {
            productosPorProveedor
                    .computeIfAbsent(producto.getProveedor(), k -> new ArrayList<>())
                    .add(producto);
        }

        guardarReporte(productos);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<BigDecimal> precioPromedioFuture = executor.submit(() -> calcularPrecioPromedio(productos));
        Future<ProductoProveedor> mayorTiempoFuture = executor.submit(() -> encontrarMayorTiempoEntrega(productos));

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("productos", productos);
        resultado.put("productosPorProveedor", productosPorProveedor);
        resultado.put("precioPromedio", precioPromedioFuture.get());
        resultado.put("productoMayorTiempo", mayorTiempoFuture.get());

        executor.shutdown();

        return resultado;
    }

    private BigDecimal calcularPrecioPromedio(List<ProductoProveedor> productos) {
        if (productos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal suma = BigDecimal.ZERO;

        for (ProductoProveedor producto : productos) {
            suma = suma.add(producto.getPrecioEstandar());
        }

        return suma.divide(BigDecimal.valueOf(productos.size()), 2, RoundingMode.HALF_UP);
    }

    private ProductoProveedor encontrarMayorTiempoEntrega(List<ProductoProveedor> productos) {
        return productos.stream()
                .max(Comparator.comparingInt(ProductoProveedor::getTiempoPromedioEntrega))
                .orElse(null);
    }

    private void guardarReporte(List<ProductoProveedor> productos) throws Exception {
        FileWriter writer = new FileWriter("reporte_proveedores.txt");

        writer.write("Proveedor,Producto,PrecioEstandar,TiempoPromedioEntrega\n");

        for (ProductoProveedor p : productos) {
            writer.write(p.getProveedor() + "," +
                    p.getProducto() + "," +
                    p.getPrecioEstandar() + "," +
                    p.getTiempoPromedioEntrega() + "\n");
        }

        writer.close();
    }
}