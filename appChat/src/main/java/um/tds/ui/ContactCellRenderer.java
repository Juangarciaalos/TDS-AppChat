package um.tds.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;

import um.tds.clases.Contacto;
import um.tds.clases.ConversorImagenes;

public class ContactCellRenderer extends JPanel implements ListCellRenderer<Contacto> {

    private JLabel nameLabel;
    private JLabel estadoLabel;
    private JLabel imageLabel;

    public ContactCellRenderer() {
    	setLayout(new GridBagLayout());
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(75, 75));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        add(imageLabel, gbc);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        estadoLabel = new JLabel();
        estadoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        estadoLabel.setForeground(Color.LIGHT_GRAY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(estadoLabel);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(textPanel, gbc);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(value.getNombre());
        estadoLabel.setText(value.getEstado().length() <= 30 ? value.getEstado() : value.getEstado().substring(0, 27) + "...");
        imageLabel.setIcon(new ImageIcon(value.getFoto()));

        if (isSelected) {
            setBackground(new Color(64, 64, 64));
        } else {
            setBackground(new Color(34, 34, 34));
        }

        return this;
    }
}
