package um.tds.clases;

public interface  Descuento {
	
	public double getDescuento(double precio);
	
	public boolean esAplicable(Usuario usuario);
}
