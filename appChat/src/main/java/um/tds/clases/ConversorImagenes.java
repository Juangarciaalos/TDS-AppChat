package um.tds.clases;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ConversorImagenes {
		public static String imageToBase64(File imagenFile) {
	        try {
	            BufferedImage buffered = ImageIO.read(imagenFile);
	
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            String formato = getFormato(imagenFile.getName());
	            ImageIO.write(buffered, formato, outputStream);
	            byte[] bytes = outputStream.toByteArray();
	
	            return Base64.getEncoder().encodeToString(bytes);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
		
		public static String imageToBase64(Image img) {
		    try {
		        BufferedImage buffered = new BufferedImage(
		            img.getWidth(null),
		            img.getHeight(null),
		            BufferedImage.TYPE_INT_ARGB
		        );
		        Graphics2D g2d = buffered.createGraphics();
		        g2d.drawImage(img, 0, 0, null);
		        g2d.dispose();
		        String formato = "png"; 
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        ImageIO.write(buffered, formato, outputStream);
		        byte[] bytes = outputStream.toByteArray();

		        return Base64.getEncoder().encodeToString(bytes);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
		}

	    public static Image base64ToImage(String base64) {
	        try {
	            byte[] bytes = Base64.getDecoder().decode(base64);
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
	            return ImageIO.read(inputStream);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public static Image base64ToImage(String base64, int ancho, int alto) {
	    	try {
	            byte[] bytes = Base64.getDecoder().decode(base64);
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
	            Image img = ImageIO.read(inputStream);
	            return img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public Image rutaToImage(String ruta) {
	    	try {
	            File file = new File(ruta);
	            if (file.exists()) {
	            	ImageIcon icon = new ImageIcon("src/main/resources/send.png");
	            	Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	            	return img;
	            } else {
	                System.err.println("El archivo no existe: " + ruta);
	                return null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    private static String getFormato(String nombreArchivo) {
	        String nombre = nombreArchivo.toLowerCase();
	        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) return "jpg";
	        if (nombre.endsWith(".gif")) return "gif";
	        if (nombre.endsWith(".bmp")) return "bmp";
	        return "png"; 
	    }
}
