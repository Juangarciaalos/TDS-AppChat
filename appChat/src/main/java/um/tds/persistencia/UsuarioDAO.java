package um.tds.persistencia;

import java.util.List;

import um.tds.clases.Usuario;

public interface UsuarioDAO {
	
	public void registrarUsuario(Usuario usuario);
	public void borrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int codigo);
	public List<Usuario> recuperarTodosUsuarios();
	
}
