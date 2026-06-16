package com.paradigmas.adventureworks.model;

import java.math.BigDecimal;

public class ProductoProveedor {
    private String proveedor;
    private String producto;
    private BigDecimal precioEstandar;
    private int tiempoPromedioEntrega;

    public ProductoProveedor(String proveedor, String producto, BigDecimal precioEstandar, int tiempoPromedioEntrega) {
        this.proveedor = proveedor;
        this.producto = producto;
        this.precioEstandar = precioEstandar;
        this.tiempoPromedioEntrega = tiempoPromedioEntrega;
    }

    public String getProveedor() { return proveedor; }
    public String getProducto() { return producto; }
    public BigDecimal getPrecioEstandar() { return precioEstandar; }
    public int getTiempoPromedioEntrega() { return tiempoPromedioEntrega; }
}