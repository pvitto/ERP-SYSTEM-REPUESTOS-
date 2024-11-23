/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Soporte-Sistemas
 */
// Repuesto.java (Modelo)
// Repuesto.java (Modelo)
import java.math.BigDecimal;

public class Repuesto {
    private String id;
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private String clase;

    public Repuesto(String id, String nombre, BigDecimal precio, int cantidad, String clase) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.clase = clase;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getClase() {
        return clase;
    }
    
    public void setClase(String clase){
        this.clase = clase;
    }
}

  
