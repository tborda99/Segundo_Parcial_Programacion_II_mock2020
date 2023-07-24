package Entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class RegistroAcceso {
    private LocalDateTime inicio;
    private LocalDateTime fin;

    private Aplicacion applicacion;

    public RegistroAcceso() {
    }

    public RegistroAcceso(LocalDateTime inicio, LocalDateTime fin, Aplicacion applicacion) {
        this.inicio = inicio;
        this.fin = fin;
        this.applicacion = applicacion;
    }

    public RegistroAcceso(LocalDateTime inicio, Aplicacion applicacion) {
        this.inicio = inicio;
        this.applicacion = applicacion;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public Aplicacion getApplicacion() {
        return applicacion;
    }

    public void setApplicacion(Aplicacion applicacion) {
        this.applicacion = applicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroAcceso that = (RegistroAcceso) o;
        return Objects.equals(inicio, that.inicio) && Objects.equals(applicacion, that.applicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, applicacion);
    }

    //Auxiliares
    public boolean fueCerrada(){
        return(this.fin!=null);
    }

    public long tiempoAbierta(){
        if(fueCerrada()){
            return(Duration.between(this.inicio, this.fin).toMinutes());
        }else{
            return 0;
        }
    }

}
