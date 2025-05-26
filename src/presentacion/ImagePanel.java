package presentacion;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.image.ImageObserver;

public final class ImagePanel extends JPanel {
    private ImageIcon imageIcon;

    public ImagePanel(LayoutManager layout, String rutaImagen) {
        super(layout);
        cargarImagen(rutaImagen);
        setOpaque(false);
    }

    private void cargarImagen(String ruta) {
        File file = new File(ruta);
        if (file.exists()) {
            imageIcon = new ImageIcon(ruta);
        } else {
            imageIcon = null;
        }
    }

    public void setBackgroundImage(String ruta) {
        cargarImagen(ruta);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageIcon != null) {
            g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this); // 'this' como ImageObserver
        }
    }
}