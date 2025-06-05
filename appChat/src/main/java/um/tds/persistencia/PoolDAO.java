package um.tds.persistencia;

import java.util.HashMap;

public enum PoolDAO {
	INSTANCE;
	
	private HashMap<Integer, Object> pool;
	
	private PoolDAO() {
		pool = new HashMap<Integer, Object>();
	}
	
	public void addObject(int id, Object obj) {
		pool.put(id, obj);
	}
	
	public void removeObject(int id) {
		pool.remove(id);
	}
	
	public Object getObject(int id) {
		return pool.get(id);
	}
	
	public boolean containsObject(int id) {
		return pool.containsKey(id);
	}
	
	
}
