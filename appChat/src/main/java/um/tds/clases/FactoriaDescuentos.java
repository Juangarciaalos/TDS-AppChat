package um.tds.clases;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public enum FactoriaDescuentos {
	 INSTANCE;
	
	 public static List<Descuento> getDescuento(Usuario usuario) {
		 
		 List<Descuento> descuentos = new ArrayList<>(
				 Arrays.asList(
						 new DescuentoPorFecha(), 
						 new DescuentoPorMensaje(), 
						 	new DescuentoNulo()
				 ));
		 
		 return descuentos.stream()
				 .filter(d -> d.esAplicable(usuario))
				 .collect(Collectors.toList());
		 
	 }
}
