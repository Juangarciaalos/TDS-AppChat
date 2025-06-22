package um.tds.persistencia;

import java.io.File;
import java.time.LocalDate;
import java.util.LinkedList;

import um.tds.Utilidades.ConversorImagenes;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;

/**
 * Clase para cargar datos de prueba en la aplicacion.   
 */
public class InyectorAppChat {

	public static void main(String[] args) throws InterruptedException {
		
		AdaptadorContactoIndividualDAO adaptadorContactoIndividual = AdaptadorContactoIndividualDAO.getInstancia();
		AdaptadorGrupoDAO adaptadorGrupo = AdaptadorGrupoDAO.getInstancia();
		AdaptadorMensajeDAO adaptadorMensaje = AdaptadorMensajeDAO.getInstancia();
		AdaptadorUsuarioDAO adaptadorUsuario = AdaptadorUsuarioDAO.getInstancia();
		
		Usuario u1 = new Usuario("Luis", "García", 1111, "hola","123", "luis@gmail.com",LocalDate.of(2002, 4, 1), LocalDate.now());
		Usuario u2 = new Usuario("Pepe", "Martínez", 2222, "hola","123", "pepe@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u3 = new Usuario("Manuel", "Fernández", 3333, "hola","123", "manuel@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u4 = new Usuario("Juan", "Pérez", 4444, "hola","123", "juan@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u5 = new Usuario("Lucas", "García", 5555,"hola","123", "lucas@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u6 = new Usuario("Miguel", "García", 6666, "hola","123", "miguel@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u7 = new Usuario("Pepa", "García", 7777, "hola","123", "pepa@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u8 = new Usuario("Antonia", "García", 8888, "hola","123", "antonia@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		Usuario u9 = new Usuario("María", "Hernández", 9999, "hola","123", "maria@gmail.com",LocalDate.of(2003, 1, 8), LocalDate.now());
		
		Usuario[] usuarios = {u1, u2, u3, u4, u5, u6, u7, u8, u9 };
		for (Usuario u : usuarios) {
			adaptadorUsuario.registrarUsuario(u);
		}
			
		
		ContactoIndividual c1 = new ContactoIndividual("Pepe", u2);
		ContactoIndividual c2 = new ContactoIndividual("Manuel", u3);
		ContactoIndividual c3 = new ContactoIndividual("Juan", u4);
		ContactoIndividual c4 = new ContactoIndividual("Lucas primo", u5);
		ContactoIndividual c5 = new ContactoIndividual("Miguel primo", u6);
		ContactoIndividual c6 = new ContactoIndividual("Pepa prima", u7);
		ContactoIndividual c7 = new ContactoIndividual("Antonia prima", u8);
		ContactoIndividual c8 = new ContactoIndividual("Luis", u1);
		ContactoIndividual c9 = new ContactoIndividual("Luis clase", u1);


		
		ContactoIndividual[] contactos = {c1, c2, c3, c4, c5, c6, c7};
		for (ContactoIndividual c : contactos) {
			adaptadorContactoIndividual.registrarContactoIndividual(c);
			u1.addContacto(c);
		}

		adaptadorUsuario.modificarUsuario(u1);
		adaptadorContactoIndividual.registrarContactoIndividual(c8);
		u2.addContacto(c8);
		adaptadorUsuario.modificarUsuario(u2);
		adaptadorContactoIndividual.registrarContactoIndividual(c9);
		u9.addContacto(c9);
		adaptadorUsuario.modificarUsuario(u9);
		
		Mensaje m1 = new Mensaje("Hola",  u1, c1);
		Mensaje m2 = new Mensaje("Como estas?",  u1, c1);
		Mensaje m3 = new Mensaje("Holaaa", u2, c8);
		Mensaje m4 = new Mensaje("Que tal?",  u2, c8);
		Mensaje m5 = new Mensaje("Bien",  u1, c1);
		Mensaje m6 = new Mensaje(7, u1, c1);
		Mensaje m7 = new Mensaje("Soy Maria", u9, c9);
		Mensaje m8 = new Mensaje("Agrégame, tengo un nuevo número", u9, c9);
		adaptadorMensaje.registrarMensaje(m1);
		adaptadorMensaje.registrarMensaje(m2);
		adaptadorMensaje.registrarMensaje(m3);
		adaptadorMensaje.registrarMensaje(m4);
		adaptadorMensaje.registrarMensaje(m5);
		adaptadorMensaje.registrarMensaje(m6);
		adaptadorMensaje.registrarMensaje(m7);
		adaptadorMensaje.registrarMensaje(m8);
		
		u1.enviarMensaje(m1, c1);
		u1.enviarMensaje(m2, c1);
		u2.enviarMensaje(m3, c8);
		u2.enviarMensaje(m4, c8);
		u1.enviarMensaje(m5, c1);
		u2.enviarMensaje(m6, c8);
		u9.enviarMensaje(m7, c9);
		u9.enviarMensaje(m8, c9);
		
		adaptadorContactoIndividual.modificarContactoIndividual(c1);
		adaptadorContactoIndividual.modificarContactoIndividual(c8);
		adaptadorContactoIndividual.modificarContactoIndividual(c9);
		
		
		
		LinkedList<ContactoIndividual> participantes = new LinkedList<>();
		participantes.add(c4);
		participantes.add(c5);
		participantes.add(c6);
		participantes.add(c7);
			
		Grupo g1 = new Grupo("Familia", participantes, ConversorImagenes.imageToBase64(new File("src/main/resources/group.png")), "Familia García");
		adaptadorGrupo.registrarGrupo(g1);
		
		u1.addContacto(g1);
		adaptadorUsuario.modificarUsuario(u1);
		
		
		
		
		
		
	}

}
