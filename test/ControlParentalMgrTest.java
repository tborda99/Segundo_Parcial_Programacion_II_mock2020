import Entities.Aplicacion;
import Entities.Usuario;
import Exceptions.AppNoCerrada;
import Exceptions.EntidadNoExiste;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;

import static org.junit.jupiter.api.Assertions.*;

class ControlParentalMgrTest {

    private ControlParentalMgr controlParental;

    @org.junit.jupiter.api.BeforeEach
    void setup(){
        controlParental = new ControlParentalMgr();
    }

    @org.junit.jupiter.api.Test
    void registrarUsuario() throws InformacionInvalida, EntidadNoExiste {

        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");

        assertEquals(controlParental.getUsuarios().find("tbordaberry").getNombre(),"pablo");
        assertEquals(controlParental.getUsuarios().find("bortom").getNombre(),"tomas");
        assertTrue(controlParental.getUsuarios().inOrder().size() == 2);

    }

    @org.junit.jupiter.api.Test
    void restringirAcceso() throws InformacionInvalida, EntidadNoExiste {
        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");

        controlParental.restringirAcceso("tbordaberry","bortom","Java",2);

        assertEquals(controlParental.getAplicaciones().get("java").getNombre(),"java");
        assertEquals(controlParental.buscarOCrearUsuarioMenor(null,"bortom",null).getNombre(),"tomas");
        assertEquals(controlParental.buscarOCrearUsuarioMayor(null,"tbordaberry").getNombre(),"pablo");
        assertEquals(controlParental.buscarOCrearUsuarioMayor(null,"tbordaberry").getMenores().size(),1);
        assertEquals(controlParental.buscarOCrearUsuarioMayor(null,"tbordaberry").getMenores().get(0).getNombre(),"tomas");
        assertEquals(controlParental.buscarOCrearUsuarioMayor(null,"tbordaberry").getMenores().get(0).getRestringirAccesso().get(0).getAplicacion().getNombre(),"java");

        assertEquals(controlParental.buscarOCrearUsuarioMenor(null,"bortom",null).getRestringirAccesso().size(),1);
        assertEquals(controlParental.buscarOCrearUsuarioMenor(null,"bortom",null).getRestringirAccesso().get(0).getAplicacion().getNombre(),"java");

    }

    @org.junit.jupiter.api.Test
    void notificacionAperturaAplicacion() throws InformacionInvalida, EntidadNoExiste, AppNoCerrada {
        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");
        controlParental.registrarUsuario("manuberch","manuel",true,"tbordaberry");

        controlParental.restringirAcceso("tbordaberry","bortom","Java",20);
        controlParental.restringirAcceso("tbordaberry","manuberch","Java",0);

        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        assertTrue(controlParental.notificacionAperturaAplicacion("bortom","Java"));

        assertFalse(controlParental.notificacionAperturaAplicacion("manuberch","Java"));


    }

    @org.junit.jupiter.api.Test
    void notificacionCierreAplicacion() throws InformacionInvalida, EntidadNoExiste, AppNoCerrada {
        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");
        controlParental.registrarUsuario("manuberch","manuel",true,"tbordaberry");

        controlParental.restringirAcceso("tbordaberry","bortom","Java",20);
        controlParental.restringirAcceso("tbordaberry","manuberch","Java",0);

        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        assertTrue(controlParental.notificacionAperturaAplicacion("bortom","Java"));

        controlParental.notificacionCierreAplicacion("bortom","java");


    }

    @org.junit.jupiter.api.Test
    void obtenerRankingAplicaciones() throws InformacionInvalida, EntidadNoExiste, AppNoCerrada, InterruptedException {
        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");
        controlParental.registrarUsuario("manuberch","manuel",true,"tbordaberry");

        controlParental.restringirAcceso("tbordaberry","bortom","Java",20);
        controlParental.restringirAcceso("tbordaberry","manuberch","Java",0);
        controlParental.restringirAcceso("tbordaberry","manuberch","Python",0);
        controlParental.restringirAcceso("tbordaberry","manuberch","SQL",0);

        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Python"));


        controlParental.notificacionCierreAplicacion("tbordaberry","java");
        controlParental.notificacionCierreAplicacion("tbordaberry","python");
        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        Thread.sleep(10000); //10 segundos, si se baja puede generar error ya que los tiempos son los mismos...
        controlParental.notificacionCierreAplicacion("tbordaberry","java");
        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","SQL"));

        //Deberia aparecer 1) JAVA, 2) Python, SQL no deberia aparecer

        MyList<Aplicacion> ranking = controlParental.obtenerRankingAplicaciones("tbordaberry");

        assertTrue(ranking.size() == 2);
        assertEquals(ranking.get(0).getNombre(),"java");
        assertEquals(ranking.get(1).getNombre(),"python");


    }

    @org.junit.jupiter.api.Test
    void obtenerRankingTop5Usuarios() throws InformacionInvalida, EntidadNoExiste, AppNoCerrada, InterruptedException {
        controlParental.registrarUsuario("tbordaberry","pablo",false,null);
        controlParental.registrarUsuario("bortom","tomas",true,"tbordaberry");
        controlParental.registrarUsuario("manuberch","manuel",true,"tbordaberry");
        controlParental.registrarUsuario("floren","florencia",true,"tbordaberry");
        controlParental.registrarUsuario("jpbord","Juanpi",true,"tbordaberry");
        controlParental.registrarUsuario("joedoe","joe",true,"tbordaberry");

        controlParental.restringirAcceso("tbordaberry","bortom","Java",20);
        controlParental.restringirAcceso("tbordaberry","manuberch","Java",0);
        controlParental.restringirAcceso("tbordaberry","manuberch","Python",0);
        controlParental.restringirAcceso("tbordaberry","manuberch","SQL",0);

        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        assertTrue(controlParental.notificacionAperturaAplicacion("bortom","Java"));
        Thread.sleep(3000);
        controlParental.notificacionCierreAplicacion("bortom","java");

        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Python"));

        assertTrue(controlParental.notificacionAperturaAplicacion("floren","Python"));
        assertTrue(controlParental.notificacionAperturaAplicacion("joedoe","Python"));
        assertTrue(controlParental.notificacionAperturaAplicacion("jpbord","Python"));
        controlParental.notificacionCierreAplicacion("floren","Python");
        controlParental.notificacionCierreAplicacion("joedoe","Python");
        controlParental.notificacionCierreAplicacion("jpbord","python");


        controlParental.notificacionCierreAplicacion("tbordaberry","java");
        controlParental.notificacionCierreAplicacion("tbordaberry","python");
        assertTrue(controlParental.notificacionAperturaAplicacion("tbordaberry","Java"));
        Thread.sleep(10000); //10 segundos
        controlParental.notificacionCierreAplicacion("tbordaberry","java");

        assertFalse(controlParental.notificacionAperturaAplicacion("manuberch","SQL"));


        MyList<Usuario> rankingUsuarios =  controlParental.obtenerRankingTop5Usuarios();

        assertTrue(rankingUsuarios.size() == 5);

        assertEquals(rankingUsuarios.get(0).getMail(),"tbordaberry");


    }
}