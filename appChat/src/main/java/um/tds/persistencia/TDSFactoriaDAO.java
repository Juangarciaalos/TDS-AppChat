package um.tds.persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {


	public TDSFactoriaDAO() {
	}


	@Override
	public UsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getInstancia();
	}

	
	@Override
	public ContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividualDAO.getInstancia();
	}

	@Override
	public GrupoDAO getGrupoDAO() {
		return AdaptadorGrupoDAO.getInstancia();
	}
	
	@Override
	public MensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAO.getInstancia();
	}
	
}
