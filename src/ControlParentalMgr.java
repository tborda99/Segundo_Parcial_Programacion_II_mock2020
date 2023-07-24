import Entities.*;
import Exceptions.AppNoCerrada;
import Exceptions.EntidadNoExiste;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.binarytree.MySearchBinaryTree;
import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;
import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.hash.MyHashImpl;
import uy.edu.um.adt.heap.MyHeap;
import uy.edu.um.adt.heap.MyHeapImpl;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import java.time.Duration;
import java.time.LocalDateTime;

public class ControlParentalMgr implements ControlParentalService{

    private MySearchBinaryTree<String, Usuario> usuarios = new MySearchBinaryTreeImpl<>();
    private MyHash<String,Aplicacion> aplicaciones = new MyHashImpl<>();

    @Override
    public void registrarUsuario(String mail, String nombre, boolean esMenor, String mailAdulto) throws InformacionInvalida, EntidadNoExiste {
        if(mail == null || nombre == null){
            throw new InformacionInvalida();
        }

        //Todos los strings en minuscula
        mail = mail.toLowerCase().trim();
        nombre = nombre.toLowerCase().trim();


        if(esMenor){
            //Caso Usuario Menor
            if (mailAdulto == null){
                throw new InformacionInvalida();
            }
            mailAdulto = mailAdulto.toLowerCase().trim();
            if(!usuarios.contains(mailAdulto)){
                throw new EntidadNoExiste();
            }

            Adulto usuarioAdulto = buscarOCrearUsuarioMayor(null,mailAdulto);
            Menor usuarioMenor =  buscarOCrearUsuarioMenor(nombre, mail, usuarioAdulto);


        }else{
            //Caso Usuario Mayor
            if (mailAdulto != null){
                throw new InformacionInvalida();
            }
            Usuario usuario = buscarOCrearUsuarioMayor(nombre, mail);

        }

    }

    @Override
    public void restringirAcceso(String mailAdulto, String mailMenor, String aplicacion, long tiempoPermitido) throws EntidadNoExiste, InformacionInvalida {
        if(mailAdulto == null || mailMenor == null || aplicacion == null){
            throw new EntidadNoExiste();
        }
        if (tiempoPermitido<0){
            throw new InformacionInvalida();
        }

        //Todos a minuscula
        mailAdulto = mailAdulto.toLowerCase().trim();
        mailMenor = mailMenor.toLowerCase().trim();
        aplicacion = aplicacion.toLowerCase().trim();

        if(!usuarios.contains(mailAdulto) || !usuarios.contains(mailMenor)){
            throw new EntidadNoExiste();
        }

        Adulto adulto = buscarOCrearUsuarioMayor(null,mailAdulto);
        Menor menor = buscarOCrearUsuarioMenor(null,mailMenor,null);
        Aplicacion app = buscarOCrearAplicacion(aplicacion);

        RestringirAcceso restringirAcceso = new RestringirAcceso(tiempoPermitido,app);

        menor.agregarRestriccion(restringirAcceso);



    }

    @Override
    public boolean notificacionAperturaAplicacion(String mailUsuario, String aplicacion) throws InformacionInvalida, EntidadNoExiste, AppNoCerrada {
        LocalDateTime tiempo = LocalDateTime.now();
        if(mailUsuario == null || aplicacion == null){
            throw new InformacionInvalida();
        }

        mailUsuario = mailUsuario.toLowerCase().trim();
        aplicacion = aplicacion.toLowerCase().trim();

        if(!usuarios.contains(mailUsuario) || !aplicaciones.contains(aplicacion)){
            throw new EntidadNoExiste();
        }

        Usuario usuario = usuarios.find(mailUsuario);
        Aplicacion app = aplicaciones.get(aplicacion);


        //Si es adulto no tiene limitantes
        if(usuario instanceof Adulto){
            RegistroAcceso registroAcceso = new RegistroAcceso(tiempo,app);
            usuario.getRegistroAcceso().put(tiempo,registroAcceso);
            return true;
        }

        //Si es Menor hay que chequear
        Menor menor = buscarOCrearUsuarioMenor(null,mailUsuario,null);
        if(menor.tieneRestriccion(aplicacion)){
            //Tiene restriccion tengo que chequear

            //Obtengo el tiempo permitido
            RestringirAcceso restAcceso =  menor.getRestriccion(aplicacion);
            restAcceso.getTiempoPermitidoDiario();

            //Obtengo el tiempo que ya estuvo
            MyList<RegistroAcceso> registroUsuario = menor.getRegistroAcceso().values();
            long tiempoUtilizado = 0;
            for (int i = 0; i < registroUsuario.size(); i++) {
                RegistroAcceso aux =  registroUsuario.get(i);
                if(aux.getApplicacion().equals(app)){
                    if(aux.getFin() == null){
                        throw new AppNoCerrada();
                    }
                    tiempoUtilizado += Duration.between(aux.getInicio(), aux.getFin()).toMinutes();
                }

            }

            if (restAcceso.getTiempoPermitidoDiario() > tiempoUtilizado){
                //Puede entrar
                RegistroAcceso registroAcceso = new RegistroAcceso(tiempo,app);
                menor.getRegistroAcceso().put(tiempo,registroAcceso);
                return true;

            }else{
                //No puede entrar
                return false;

            }

        }else{
            RegistroAcceso registroAcceso = new RegistroAcceso(tiempo,app);
            menor.getRegistroAcceso().put(tiempo,registroAcceso);
            return true;
        }

    }

    @Override
    public void notificacionCierreAplicacion(String mailUsuario, String aplicacion) throws InformacionInvalida, EntidadNoExiste {
        LocalDateTime tiempo = LocalDateTime.now();
        if(mailUsuario == null || aplicacion == null){
            throw new InformacionInvalida();
        }

        mailUsuario = mailUsuario.toLowerCase().trim();
        aplicacion = aplicacion.toLowerCase().trim();

        if(!usuarios.contains(mailUsuario) || !aplicacion.contains(aplicacion)){
            throw new EntidadNoExiste();
        }

        Usuario usuario = usuarios.find(mailUsuario);
        Aplicacion app = aplicaciones.get(aplicacion);

        MyHash<LocalDateTime,RegistroAcceso> registroUsuario = usuario.getRegistroAcceso();

        MyList<RegistroAcceso> registroLista =  registroUsuario.values();
        boolean flag = true;
        for (int i = 0; i < registroLista.size(); i++) {
            RegistroAcceso aux = registroLista.get(i);
            if(aux.getApplicacion().equals(app) && aux.getFin() == null){
                //Encontre el registro cierro
                aux.setFin(tiempo);
                flag = false;
            }

        }

        if(flag){
            throw new EntidadNoExiste();
        }


    }

    @Override
    public MyList<Aplicacion> obtenerRankingAplicaciones(String mailUsuario) throws InformacionInvalida {
        if(mailUsuario == null){
            throw new InformacionInvalida();
        }
        Usuario usuario = usuarios.find(mailUsuario);
        MyList<RegistroAcceso> registro = usuario.getRegistroAcceso().values();

        MyHeap<Ranking> rankingHeap = new MyHeapImpl<>(false); //Si se busca de menor a mayor cambiar este valor por true.
        MyList<Ranking> rankingNoOrdenado = new MyLinkedListImpl<>();
        MyList<Ranking> rankingOrdenado = new MyLinkedListImpl<>();

        int sizeR = registro.size();
        for (int i = 0; i < sizeR; i++) {
            RegistroAcceso registroAux = registro.get(i);
            if(registroAux.fueCerrada()){
            Ranking rankingAux = new Ranking(registroAux.getApplicacion(),registroAux.tiempoAbierta());
                if(rankingNoOrdenado.contains(rankingAux)){
                    //Si existe, recorro el ranking hasta encontrarlo y le sumo los minutos
                    int sizeRNO = rankingNoOrdenado.size();
                    for (int j = 0; j < sizeRNO; j++) {
                        if(rankingNoOrdenado.get(j).getApp().equals(registroAux.getApplicacion())){
                            rankingNoOrdenado.get(j).addMinutos(registroAux.tiempoAbierta());
                        }
                    }
                }else{
                    //Si no esta lo agrego
                    rankingNoOrdenado.add(rankingAux);
                }
            }
        }

        for (int i = 0; i < rankingNoOrdenado.size(); i++) {
            rankingHeap.insert(rankingNoOrdenado.get(i));
        }

        int sizeH = rankingHeap.size();
        for (int i = 0; i < sizeH; i++) {
            rankingOrdenado.add(rankingHeap.delete());
        }


        MyList<Aplicacion> rankingListaApps = new MyLinkedListImpl<>();
        for (int i = rankingOrdenado.size() - 1; i >= 0; i--) {
            rankingListaApps.add(rankingOrdenado.get(i).getApp());
        }

    return rankingListaApps;


    }

    @Override
    public MyList<Usuario> obtenerRankingTop5Usuarios() throws EntidadNoExiste {

        MyList<String> mailsUsuarios =  usuarios.inOrder(); //Lista de los nombres de los usuarios

        if(mailsUsuarios.size() == 0){ //Si la lista esta vacia no puedo hacer ranking.
            throw new EntidadNoExiste();
        }

        MyList<Usuario> usuariosNoOrdenados = new MyLinkedListImpl<>();
        for (int i = 0; i < mailsUsuarios.size(); i++) {
            Usuario usuarioAux = usuarios.find(mailsUsuarios.get(i));
            usuarioAux.calcularHoras();
            usuariosNoOrdenados.add(usuarioAux);
        }

        //Ya tengo la lista de usuarios, ahora solo los ordeno con heap sort
        MyHeap<Usuario> usuarioHeap = new MyHeapImpl<>(false); // False: Mayor a menor, True: Menor a Mayo
        MyList<Usuario> usuariosOrdenados = new MyLinkedListImpl<>();

        for (int i = 0; i < usuariosNoOrdenados.size(); i++) {
            usuarioHeap.insert(usuariosNoOrdenados.get(i));
        }

        int sizeHeap = usuarioHeap.size();
        int count = 1;
        for (int i = 0; i < sizeHeap; i++) {
            if(count <= 5) {
                usuariosOrdenados.add(usuarioHeap.delete());
                count++;
            }else{
                break;
            }
        }

        return usuariosOrdenados;
    }

    //GETTERS PARA TEST

    public MySearchBinaryTree<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public MyHash<String, Aplicacion> getAplicaciones() {
        return aplicaciones;
    }


    //AUXILIARES
    public Menor buscarOCrearUsuarioMenor(String nombre, String mail, Adulto adulto) throws EntidadNoExiste {

        mail = mail.toLowerCase().trim();

        if(adulto == null || nombre == null){
            //buscar
            if(usuarios.contains(mail)){
                return (Menor) usuarios.find(mail);
            }else{
                throw new EntidadNoExiste();
            }
        }else{
            //crear
            nombre = nombre.toLowerCase().trim();
            Menor menor = new Menor(mail, nombre);
            adulto.agregarMenor(menor);
            usuarios.add(mail,menor);
            return menor;
        }

    }

    public Adulto buscarOCrearUsuarioMayor(String nombre, String mail) throws EntidadNoExiste, InformacionInvalida {
        //Todos los strings en minuscula
        mail = mail.toLowerCase().trim();

        if(nombre == null){
            //buscar
            if(usuarios.contains(mail)) {
                if(usuarios.find(mail).getClass().equals(Adulto.class)){
                    return (Adulto) usuarios.find(mail);
                }else{
                    throw new InformacionInvalida();
                }
            }else{
                throw new EntidadNoExiste();
            }
        }else{
            nombre = nombre.toLowerCase().trim();
            //crear
            Adulto adulto = new Adulto(mail,nombre);
            usuarios.add(mail, adulto);
            return adulto;
        }
    }


    public Aplicacion buscarOCrearAplicacion(String nombre){

        //Todos los strings a minuscula
        nombre = nombre.toLowerCase().trim();

        if(aplicaciones.contains(nombre)){
            return aplicaciones.get(nombre);
        }else{
            Aplicacion app = new Aplicacion(nombre);
            aplicaciones.put(nombre, app);
            return app;
        }
    }
}
