package presentacion;

import domain.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface Auxiliar {

    public static final String POKEINFO = "resources/menu/pokedex/pokeinfo.png";
    public static final String POKEDEX = "resources/menu/pokedex/pokedex";
    public static final String TYPES = "resources/pokemones/Emerald/types/";
    public static final String POKEMONES = "resources/pokemones/Emerald/";
    public static final String GIF_EXT = ".gif";
    public static final String PNG_EXT = ".png";
    public static final String MENU = "resources/menu/";
    public static final String BACK_PATH = POKEMONES + "Back/";
    public static final String BACK_SHINY_PATH = POKEMONES + "BackShiny/";
    public static final String NORMAL_PATH = POKEMONES + "Normal/";
    public static final String SHINY_PATH = POKEMONES + "Shiny/";
    public static final String GIFNORMAL_PATH = POKEMONES + "GifNormal/";
    public static final String GIFSHINY_PATH = POKEMONES + "GifShiny/";
    public static final String SONGS =  "resources/songs/";
    public static final String SELECTION_PANEL = "resources/menu/selectionPanel/";
    public static final String ITEMS = "resources/Items/";
    public static final String BUTTONS = "resources/menu/buttons/";
    public static final String GALERIA_ITEMS =  "resources/menu/galeria_items.png";
    public static final String WINNER = "resources/menu/winner/";
    public static final String CHARACTER_ICONS = "resources/personaje/trainerIcons/";
    public static final String CHARACTER = "resources/personaje/trainers/";
    public static final String MAP = MENU+"map/";
    public static final String FRAME_ATTACK = MENU+"frameAttack/";
    public static final String FRAME = MENU+"frame/";
    public static final String FRONT_FLOOR = MENU+"frontFloor/";
    public static final String BACK_FLOOR = MENU+"backFloor/";
    public static final String STATUS = "resources/menu/status/";
    public static final  String ICONS = "resources/menu/icons/";

    default JButton crearBotonTransparente(String texto, Rectangle bounds, boolean alineado) {
        JButton boton = new JButton(texto);

        // Poner las coordenadas y tamaño
        boton.setBounds(bounds);

        // Hacer el botón transparente
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);

        // Color inicial del texto
        boton.setForeground(Color.BLACK);

        // Cambiar color al pasar el ratón
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(Color.BLACK);
            }
        });

        boton.setFont(cargarFuentePixel(18));
        if (!alineado) {
            boton.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return boton;
    }
     default Font cargarFuentePixel(int tamano) {
        try {
            Font fuenteBase = Font.createFont(Font.TRUETYPE_FONT,
                    new File("resources/fonts/themevck-text.ttf"));
            Font fuenteNegrita = fuenteBase.deriveFont(Font.BOLD, tamano);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuenteNegrita);
            return fuenteNegrita;

        } catch (FontFormatException | IOException e) {
            Log.record(e);
            return new Font("Monospaced", Font.BOLD, (int)tamano);
        }
    }
    default JButton crearBotonEstilizado(String texto, Rectangle bounds, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo opaco
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde negro
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2f)); // Grosor del borde (2px)
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        boton.setBounds(bounds);
        boton.setFont(cargarFuentePixel(18));
        boton.setForeground(Color.BLACK);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen interno
        boton.setFocusable(false);
        boton.setContentAreaFilled(false); // Importante mantenerlo para que funcione nuestro paintComponent
        boton.setOpaque(false); // Permitimos que se vea nuestro fondo personalizado



        return boton;
    }
    default ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    default void mostrarError(String titulo, String error) {
        String mensaje = titulo + ":\n"+ error;
        JOptionPane.showMessageDialog(null, mensaje,
                "Error", JOptionPane.ERROR_MESSAGE,new ImageIcon(ICONS+"snorlax.png"));
    }

    default int findAbsoluteLowestVisibleY(BufferedImage img) {
        int lowestY = 0;
        // Usamos un enfoque por columnas para mejor precisión
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = img.getHeight()-1; y >= lowestY; y--) {
                if ((img.getRGB(x, y) & 0xFF000000) != 0) {
                    if (y > lowestY) {
                        lowestY = y;
                    }
                    break; // Pasamos a la siguiente columna
                }
            }
        }
        return lowestY;
    }

    default BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bufferedImage;
    }
    default JButton createImageButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);

        try {
            // Cargar y escalar la imagen
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            Log.record(e);
            button.setText("No image");
        }

        // Hacer el botón transparente
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }
    default JButton createImageButton(String text, String imagePath, int x, int y, int width, int height, int fontSize, boolean alineado, boolean cubrirBoton) {
        JButton button = new JButton();

        // Soporte para saltos de línea usando HTML
        String formattedText = "<html>" + text.replace("\n", "<br>") + "</html>";
        button.setText(formattedText);

        // Si cubrirBoton es true, la imagen será del tamaño completo del botón
        int iconWidth = cubrirBoton ? width : 50;
        int iconHeight = cubrirBoton ? height : 50;

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        if (cubrirBoton) {
            // Imagen como fondo completo, texto encima
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);
        } else {
            // Imagen pequeña con texto al costado
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setVerticalTextPosition(SwingConstants.CENTER);
        }

        button.setFont(cargarFuentePixel(fontSize));
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBounds(x, y, width, height);

        if (alineado) {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMargin(new Insets(0, 20, 0, 0));
        }

        // Efecto hover para cambiar el color del texto
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }
    default JButton createfillImageButton(String text, String imagePath, int x, int y, int width, int height, int fontSize, boolean alineado, boolean cubrirBoton) {
        String formattedText = "<html>" + text.replace("\n", "<br>") + "</html>";

        JButton button = new JButton(formattedText) {
            Image image = new ImageIcon(imagePath).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                // 1. Fondo transparente
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.0f)); // Fondo 100% transparente
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();

                // 2. Dibujar imagen de fondo (solo si cubrirBoton = true)
                if (cubrirBoton && image != null) {
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
                super.paintComponent(g); // Dibuja el texto
            }
        };

        // 3. Configuración base del botón
        button.setFont(cargarFuentePixel(fontSize));
        button.setForeground(Color.BLACK);
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false); // Importante para transparencia

        // 4. Margen interno (para evitar texto pegado)
        int marginLeft = alineado ? 15 : 5; // 15px si está alineado, 5px si no
        button.setMargin(new Insets(5, marginLeft, 5, 5)); // Arriba, Izq, Abajo, Der

        // 5. Comportamiento según cubrirBoton
        if (!cubrirBoton) {
            ImageIcon icon = new ImageIcon(
                    new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
            );
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setVerticalTextPosition(SwingConstants.CENTER);
            button.setIconTextGap(10); // Espacio entre ícono y texto
        } else {
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);
        }

        // 6. Efecto hover (opcional)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }
}