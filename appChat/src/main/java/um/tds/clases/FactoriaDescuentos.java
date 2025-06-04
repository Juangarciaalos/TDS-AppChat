package um.tds.clases;

import java.util.List;
import java.util.ArrayList;

public enum FactoriaDescuentos {
	 INSTANCE;
	
	 public static List<Descuento> getDescuento(Usuario usuario) {
		 
		 List<Descuento> descuentos = new ArrayList<>();
		 for (Descuento d : List.of(new DescuentoPorFecha(), new DescuentoPorMensaje(), new DescuentoNulo())) {
			    if (d.esAplicable(usuario)) descuentos.add(d);
		 }
		 
		 return descuentos;
	 }
}
