package um.tds.persistencia;

import java.util.List;

import um.tds.clases.ContactoIndividual;

public interface ContactoIndividualDAO {
	public void registrarContactoIndividual(ContactoIndividual contactoIndividual);
	public void borrarContactoIndividual(ContactoIndividual contactoIndividual);
	public void modificarContactoIndividual(ContactoIndividual contactoIndividual);
	public ContactoIndividual recuperarContactoIndividual(int codigo);
	public List<ContactoIndividual> recuperarTodosContactosIndividuales();
}
