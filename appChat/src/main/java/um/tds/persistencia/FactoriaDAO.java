package um.tds.persistencia;

public abstract class FactoriaDAO {
	
	private static FactoriaDAO instancia;
	public static final String DAO_TDS = "um.tds.persistencia.TDSFactoriaDAO";
	
	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (instancia == null) {
			try {
				instancia = (FactoriaDAO) Class.forName(tipo).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		}
		return instancia;
	}
	
	public static FactoriaDAO getInstancia() throws DAOException {
			return getInstancia(FactoriaDAO.DAO_TDS);
	}
	
	protected FactoriaDAO() {
	}
	
	public abstract UsuarioDAO getUsuarioDAO();
	public abstract ContactoIndividualDAO getContactoIndividualDAO();
	public abstract GrupoDAO getGrupoDAO();
	public abstract MensajeDAO getMensajeDAO();
}
