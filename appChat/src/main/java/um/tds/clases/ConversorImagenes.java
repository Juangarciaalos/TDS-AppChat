package um.tds.clases;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.imageio.ImageIO;

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
	    
	    private static String getFormato(String nombreArchivo) {
	        String nombre = nombreArchivo.toLowerCase();
	        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) return "jpg";
	        if (nombre.endsWith(".gif")) return "gif";
	        if (nombre.endsWith(".bmp")) return "bmp";
	        return "png"; 
	    }
}
