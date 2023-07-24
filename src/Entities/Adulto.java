package Entities;

import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.LocalDateTime;

public class Adulto extends Usuario{

    private MyList<Menor> menores;

    //CONSTRUCTORES
    public Adulto() {
        this.menores = new MyLinkedListImpl<>();
    }

    public Adulto(String mail, String nombre) {
        super(mail, nombre);
        this.menores = new MyLinkedListImpl<>();
    }
    public Adulto(String mail, String nombre, MyList<Menor> menores) {
        super(mail, nombre);
        this.menores = menores;
    }

    public Adulto(String mail, String nombre, MyHash<LocalDateTime, RegistroAcceso> registroAcceso, MyList<Menor> menores) {
        super(mail, nombre, registroAcceso);
        this.menores = menores;
    }

    public MyList<Menor> getMenores() {
        return menores;
    }

    public void setMenores(MyList<Menor> menores) {
        this.menores = menores;
    }

    public void agregarMenor(Menor menor){
        this.menores.add(menor);
    }

}
