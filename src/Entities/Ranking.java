package Entities;

import java.util.Objects;

public class Ranking  implements Comparable<Ranking>{
    private Aplicacion app;
    private long minutos;

    public Ranking(Aplicacion app, long minutos) {
        this.app = app;
        this.minutos = minutos;
    }

    public Ranking() {
    }

    public Aplicacion getApp() {
        return app;
    }

    public void setApp(Aplicacion app) {
        this.app = app;
    }

    public long getMinutos() {
        return minutos;
    }

    public void setMinutos(long minutos) {
        this.minutos = minutos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranking ranking = (Ranking) o;
        return Objects.equals(app, ranking.app);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app);
    }


    @Override
    public int compareTo(Ranking o) {
        if (this.minutos < o.minutos) {
            return -1; //Menor
        } else if (this.minutos > o.minutos) {
            return 1; //Mayor
        } else {
            return 0; //Igual
        }
    }


    //Auxiliar
    public void addMinutos(long minutosExtra){
        this.minutos += minutosExtra;
    }
}
