package Entities;

import java.util.Objects;

public class RestringirAcceso {
    private long tiempoPermitidoDiario;
    private Aplicacion aplicacion;

    public RestringirAcceso(long tiempoPermitidoDiario, Aplicacion aplicacion) {
        this.tiempoPermitidoDiario = tiempoPermitidoDiario;
        this.aplicacion = aplicacion;
    }

    public RestringirAcceso() {
    }

    public long getTiempoPermitidoDiario() {
        return tiempoPermitidoDiario;
    }

    public void setTiempoPermitidoDiario(long tiempoPermitidoDiario) {
        this.tiempoPermitidoDiario = tiempoPermitidoDiario;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestringirAcceso that = (RestringirAcceso) o;
        return Objects.equals(aplicacion, that.aplicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aplicacion);
    }
}
