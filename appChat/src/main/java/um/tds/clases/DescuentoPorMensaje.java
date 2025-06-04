package um.tds.clases;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DescuentoPorMensaje implements Descuento{
	
	private static final double DESCUENTO = 0.90; 
	private static final int NUMERO_MENSAJES = 10;
	
	@Override
	public double getDescuento(double precio) {
		return precio * DESCUENTO;
	}
	
	@Override
	public boolean esAplicable(Usuario usuario) {
		LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		
		int mensajesEnviadosUltimoMes = (int) usuario.getMensajesEnviados().stream().filter(m -> m.getHoraEnvio().isAfter(inicioMes)).count();
		return mensajesEnviadosUltimoMes >= NUMERO_MENSAJES;
	}
}
