package Entities;

import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

public class Menor extends Usuario{

    private MyList<RestringirAcceso> restringirAccesso;

    public Menor() {
        this.restringirAccesso = new MyLinkedListImpl<>();
    }

    public Menor(String mail, String nombre) {
        super(mail, nombre);
        this.restringirAccesso = new MyLinkedListImpl<>();
    }

    public Menor(String mail, String nombre, MyList<RestringirAcceso> restringirAccesso) {
        super(mail, nombre);
        this.restringirAccesso = restringirAccesso;
    }

    public MyList<RestringirAcceso> getRestringirAccesso() {
        return restringirAccesso;
    }

    public void setRestringirAccesso(MyList<RestringirAcceso> restringirAccesso) {
        this.restringirAccesso = restringirAccesso;
    }


    //Auxiliar
    public void agregarRestriccion(RestringirAcceso restriccionNueva){
        this.restringirAccesso.add(restriccionNueva);
    }

    public boolean tieneRestriccion(String nombreApp){
        nombreApp = nombreApp.toLowerCase().trim();
        for (int i = 0; i < this.restringirAccesso.size(); i++) {
            if(this.restringirAccesso.get(i).getAplicacion().getNombre().equals(nombreApp)){
                return true;
            }
        }
        return false;

    }

    public RestringirAcceso getRestriccion(String nombreApp){
        nombreApp = nombreApp.toLowerCase().trim();
        for (int i = 0; i < this.restringirAccesso.size(); i++) {
            if(this.restringirAccesso.get(i).getAplicacion().getNombre().equals(nombreApp)){
                return this.restringirAccesso.get(i);
            }
        }
        return null;
    }

}
