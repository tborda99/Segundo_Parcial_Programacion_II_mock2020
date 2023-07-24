package Entities;


import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.hash.MyHashImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario implements Comparable<Usuario>{
    private String mail;
    private String nombre;
    private MyHash<LocalDateTime,RegistroAcceso> registroAcceso;
    private long horasPantalla;

    public Usuario() {
        this.registroAcceso = new MyHashImpl<>();
    }

    public Usuario(String mail, String nombre) {
        this.mail = mail;
        this.nombre = nombre;
        this.registroAcceso = new MyHashImpl<>();
    }

    public Usuario(String mail, String nombre, MyHash<LocalDateTime, RegistroAcceso> registroAcceso) {
        this.mail = mail;
        this.nombre = nombre;
        this.registroAcceso = registroAcceso;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MyHash<LocalDateTime, RegistroAcceso> getRegistroAcceso() {
        return registroAcceso;
    }

    public void setRegistroAcceso(MyHash<LocalDateTime, RegistroAcceso> registroAcceso) {
        this.registroAcceso = registroAcceso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(mail, usuario.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail);
    }

    //Auxiliar
    public void calcularHoras(){
        MyList<RegistroAcceso> registroList = registroAcceso.values();

        long tiempo =0;
        for (int i = 0; i < registroList.size(); i++) {
            RegistroAcceso registroAux = registroList.get(i);
            if(registroAux.fueCerrada()){
                tiempo += registroAux.tiempoAbierta();
            }

        }
        this.horasPantalla += tiempo;

    }


    @Override
    public int compareTo(Usuario o) {
        if (this.horasPantalla < o.horasPantalla) {
            return -1; //Menor
        } else if (this.horasPantalla > o.horasPantalla) {
            return 1; //Mayor
        } else {
            return 0; //Igual
        }
    }
}
