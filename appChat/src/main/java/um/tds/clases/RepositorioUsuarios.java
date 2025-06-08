package um.tds.clases;

import java.util.List;
import java.util.Map;

import um.tds.persistencia.DAOException;
import um.tds.persistencia.FactoriaDAO;

public enum RepositorioUsuarios {
	INSTANCE;
	
	private Map<Integer, Usuario> usuarios;
	private Map<String, Usuario> usuariosPorNombre;
	private Map<Integer, Usuario> usuariosPorTelefono;
	
	private RepositorioUsuarios() {
		usuarios = new java.util.HashMap<Integer, Usuario>();
		usuariosPorNombre = new java.util.HashMap<String, Usuario>();
		usuariosPorTelefono = new java.util.HashMap<Integer, Usuario>();
		
		try {
			List<Usuario> listaUsuarios = FactoriaDAO.getInstancia().getUsuarioDAO().recuperarTodosUsuarios();
			for (Usuario usuario : listaUsuarios) {
				usuarios.put(usuario.getCodigo(), usuario);
				usuariosPorNombre.put(usuario.getNombre(), usuario);
				usuariosPorTelefono.put(usuario.getNumeroTelefono(), usuario);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUsuario(Usuario usuario) {
		if (usuario != null && !usuarios.containsKey(usuario.getCodigo())) {
			usuarios.put(usuario.getCodigo(), usuario);
			usuariosPorNombre.put(usuario.getNombre(), usuario);
			usuariosPorTelefono.put(usuario.getNumeroTelefono(), usuario);
		}
	}
	
	public Usuario getUsuario(int codigo) {
		return usuarios.get(codigo);
	}
	
	public Usuario getUsuarioPorNombre(String nombre) {
		return usuariosPorNombre.get(nombre);
	}
	
	public Usuario getUsuarioPorTelefono(int telefono) {
		return usuariosPorTelefono.get(telefono);
	}
	
	public boolean existeUsuario(Usuario usuario) {
		return usuarios.containsValue(usuario);
	}
	
	public boolean existeUsuarioPorNombre(String nombre) {
		return usuariosPorNombre.containsKey(nombre);
	}
	
	public boolean existeUsuarioPorTelefono(int telefono) {
		return usuariosPorTelefono.containsKey(telefono);
	}
}
