package um.tds.clases;

import java.time.LocalDate;

public class DescuentoPorFecha implements Descuento{
	
	private static final double DESCUENTO = 0.75; 
	
	@Override
	public double getDescuento(double precio) {
		return precio * DESCUENTO;
	}
	
	@Override
	public boolean esAplicable(Usuario usuario) {
		if (usuario.getFechaAlta().plusMonths(6).isAfter(LocalDate.now())) {
			return true;
		} else {
			return false;
		}
	}
}
