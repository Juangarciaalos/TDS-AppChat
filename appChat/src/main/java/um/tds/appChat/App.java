package um.tds.appChat;

import java.awt.EventQueue;
import um.tds.ui.VentanaLogin;

/**
 *	Clase lanzadora de la aplicación.
 */
public class App 
{
    public static void main( String[] args )
    {
       EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin ventana = new VentanaLogin();
					ventana.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
