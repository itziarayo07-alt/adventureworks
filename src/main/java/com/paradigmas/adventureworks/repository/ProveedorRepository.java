package com.paradigmas.adventureworks.repository;

import com.paradigmas.adventureworks.model.ProductoProveedor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProveedorRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProveedorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductoProveedor> buscarPorProveedor(String proveedor) {
        String sql = """
            SELECT
                v.Name AS proveedor,
                p.Name AS producto,
                p.StandardCost AS precioEstandar,
                pv.AverageLeadTime AS tiempoPromedioEntrega
            FROM Purchasing.ProductVendor pv
            INNER JOIN Purchasing.Vendor v
                ON pv.BusinessEntityID = v.BusinessEntityID
            INNER JOIN Production.Product p
                ON pv.ProductID = p.ProductID
            WHERE v.Name LIKE ?
            ORDER BY v.Name, p.Name
        """;

        return jdbcTemplate.query(sql, new Object[]{"%" + proveedor + "%"}, (rs, rowNum) ->
                new ProductoProveedor(
                        rs.getString("proveedor"),
                        rs.getString("producto"),
                        rs.getBigDecimal("precioEstandar"),
                        rs.getInt("tiempoPromedioEntrega")
                )
        );
    }
}