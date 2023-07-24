package Entities;

import java.util.Objects;

public class Aplicacion {
    private String nombre;

    public Aplicacion(String nombre) {
        this.nombre = nombre;
    }

    public Aplicacion() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aplicacion that = (Aplicacion) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }


}
