package um.tds.clases;

public class DescuentoNulo implements Descuento {

	@Override
	public double getDescuento(double precio) {
		return precio; 
	}

	@Override
	public boolean esAplicable(Usuario usuario) {
		return true; 
	}

}
