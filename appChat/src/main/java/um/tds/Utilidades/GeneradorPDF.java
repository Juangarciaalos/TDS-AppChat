package um.tds.Utilidades;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import um.tds.clases.Contacto;
import um.tds.clases.Grupo;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;
import um.tds.controlador.Controlador;

public enum GeneradorPDF {
	
    INSTANCE;

    public void generarPDF(Contacto contacto, List<Mensaje> mensajes, String rutaSalida) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaSalida));
        documento.open();

        Paragraph titulo = new Paragraph("Chat con " + contacto.getNombre(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(Chunk.NEWLINE);

        if (contacto instanceof Grupo grupo) {
            Paragraph miembrosTitulo = new Paragraph("Miembros del grupo:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            documento.add(miembrosTitulo);
            for (Contacto c : grupo.getParticipantes()) {
                String linea = "- " + c.getNombre() + " (" + ((ContactoIndividual) c).getNumeroTelefono() + ")";
                Paragraph miembro = new Paragraph(linea, FontFactory.getFont(FontFactory.HELVETICA, 12));
                documento.add(miembro);
            }
            documento.add(Chunk.NEWLINE);
        } else if (contacto instanceof ContactoIndividual ci) {
        	Paragraph numero = new Paragraph("Número de teléfono: " + ci.getNumeroTelefono(), 
				FontFactory.getFont(FontFactory.HELVETICA, 12));
			documento.add(numero);
			documento.add(Chunk.NEWLINE);
        }

        Usuario actual = Controlador.getInstancia().getUsuarioActual();

        for (Mensaje mensaje : mensajes) {
            boolean enviadoPorUsuario = mensaje.getEmisor().getNumeroTelefono() == actual.getNumeroTelefono();
            String nombreEmisor = mensaje.getEmisor().getNombre();
            String contenido = mensaje.getTexto().isEmpty() ? "[Emoji]" : mensaje.getTexto();

            Paragraph mensajeParrafo = new Paragraph(nombreEmisor + ": " + contenido, 
                FontFactory.getFont(FontFactory.HELVETICA, 12));
            mensajeParrafo.setAlignment(enviadoPorUsuario ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
            documento.add(mensajeParrafo);

            Paragraph fecha = new Paragraph("Fecha: " + mensaje.getHoraEnvio(), 
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
            fecha.setAlignment(enviadoPorUsuario ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
            documento.add(fecha);

            documento.add(Chunk.NEWLINE);
        }

        documento.close();
    }
}
