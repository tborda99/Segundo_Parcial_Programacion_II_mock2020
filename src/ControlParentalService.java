import Entities.Aplicacion;
import Entities.Usuario;
import Exceptions.AppNoCerrada;
import Exceptions.EntidadNoExiste;
import Exceptions.InformacionInvalida;
import uy.edu.um.adt.linkedlist.MyList;
public interface ControlParentalService {

    void registrarUsuario(String mail, String nombre, boolean esMenor, String mailAdulto) throws InformacionInvalida, EntidadNoExiste;

    void restringirAcceso(String mailAdulto, String mailMenor, String aplicacion, long tiempoPermitido) throws EntidadNoExiste, InformacionInvalida;

    boolean notificacionAperturaAplicacion(String mailUsuario, String aplicacion) throws InformacionInvalida, EntidadNoExiste, AppNoCerrada;

    void notificacionCierreAplicacion(String mailUsuario, String aplicacion) throws InformacionInvalida, EntidadNoExiste;

    MyList<Aplicacion> obtenerRankingAplicaciones(String mailUsuario) throws InformacionInvalida;

    MyList<Usuario> obtenerRankingTop5Usuarios() throws EntidadNoExiste;

}
