package um.tds.Utilidades;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import um.tds.clases.Mensaje;

public class GeneradorPDF {
	
	
	public void generarPDF(String contacto, List<Mensaje> mensajes, String rutaSalida) throws IOException, DocumentException {
		Document documento = new Document();
		PdfWriter.getInstance(documento, new FileOutputStream(rutaSalida));
		
		documento.open();
		
		Paragraph titulo = new Paragraph("Chat con " + contacto);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);
		
		for (Mensaje mensaje : mensajes) {
			Paragraph p = new Paragraph();
			p.add(new Chunk(mensaje.getEmisor().getNombre() + ": "));
			p.add(new Chunk(mensaje.getTexto()));
			documento.add(p);
			
			Paragraph fecha = new Paragraph("Fecha: " + mensaje.getHoraEnvio().toString());
			fecha.setAlignment(Element.ALIGN_RIGHT);
			documento.add(fecha);
			
			documento.add(new Paragraph("\n")); 
		}
		
		documento.close();
	}
}
