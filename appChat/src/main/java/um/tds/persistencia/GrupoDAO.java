package um.tds.persistencia;

import java.util.List;

import um.tds.clases.Grupo;

public interface GrupoDAO {
	public void registrarGrupo(Grupo grupo);
	public void borrarGrupo(Grupo grupo);
	public void modificarGrupo(Grupo grupo);
	public Grupo recuperarGrupo(int codigo);
	public List<Grupo> recuperarTodosGrupos();	
}	
