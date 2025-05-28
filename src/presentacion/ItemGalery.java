
package presentacion;

import domain.POOBkemon;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.ArrayList;

public final class ItemGalery extends JPanel implements Auxiliar{

    private POOBkemon game;
    private JPanel mainPanel;
    private PokemonBattlePanel.BattleListener battleListener;
    public ItemGalery(POOBkemon game) {
        if (game == null) throw new IllegalArgumentException("Game cannot be null");
        this.game = game;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(showItemsGalery(), BorderLayout.CENTER);
    }

    private JPanel showItemsGalery() {
        JPanel itemsPanel = new ImagePanel(null, GALERIA_ITEMS);
        ArrayList<ArrayList<String>> items = this.game.getItemsInfo();
        final int[] currentIndex = {0}; // Para trackear el primer item visible

        // Panel para mostrar los items (4 máximo)
        JPanel itemsDisplayPanel = new JPanel(null);
        itemsDisplayPanel.setBounds(0, 0, 750, 550);
        itemsDisplayPanel.setOpaque(false);


        // Área de información (DERECHA de la imagen) - TRANSPARENTE
        JTextPane infoPanel = new JTextPane();  // Cambiamos a JTextPane para mejor control
        infoPanel.setBounds(300, 40, 390, 200);
        infoPanel.setEditable(false);
        infoPanel.setFont(cargarFuentePixel(18));
        infoPanel.setOpaque(false);  // Hacemos el fondo transparente

        //Aumenta el espacio entre lineas
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setLineSpacing(defaultStyle, 0.9f); // Aumentar el espaciado (0.5 = 50% más espacio)
        infoPanel.setDocument(doc);
        itemsPanel.add(infoPanel);

        JLabel messageLabel = new JLabel("ITEMS");
        messageLabel.setFont(cargarFuentePixel(32));
        messageLabel.setForeground(Color.white);
        messageLabel.setHorizontalAlignment(JLabel.LEFT);
        messageLabel.setBounds(30, 10, 400, 50);

        JLabel message1Label = new JLabel("INFORMACION SOBRE LOS ITEMS DEL JUEGO");
        message1Label.setFont(cargarFuentePixel(24));
        message1Label.setForeground(Color.white);
        message1Label.setHorizontalAlignment(JLabel.LEFT);
        message1Label.setBounds(120, 360, 600, 100);


        ImageIcon arrowNext = new ImageIcon(MENU + "flechaDerecha.png");
        ImageIcon arrowPrev = new ImageIcon(MENU + "flechaIzquierda.png");

        Image scaledArrowNext = arrowNext.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image scaledArrowPrev = arrowPrev.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JButton nextButton = new JButton();
        nextButton.setBounds(160, 150, 100, 100);
        nextButton.setIcon(new ImageIcon(scaledArrowNext));
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);

        JButton prevButton = new JButton();
        prevButton.setBounds(-15, 150, 100, 100);
        prevButton.setIcon(new ImageIcon(scaledArrowPrev));
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);


        JButton backButton = crearBotonTransparente("Volver", new Rectangle(0, 395, 130, 40),true);

        itemsPanel.add(nextButton);
        itemsPanel.add(prevButton);
        itemsPanel.add(messageLabel);
        itemsPanel.add(message1Label);
        itemsPanel.add(backButton);
        itemsPanel.add(itemsDisplayPanel);
        itemsPanel.add(infoPanel);


        Runnable updateItemsDisplay = () -> {
            itemsDisplayPanel.removeAll();

            if (!items.isEmpty()) {
                ArrayList<String> item = items.get(currentIndex[0]);
                JButton itemButton = createImageButton(ITEMS + item.get(0) + ".png", 65, 140, 100, 100);
                itemsDisplayPanel.add(itemButton);
                String texto = item.get(1).replace("\n", "\n\n");
                infoPanel.setText(texto);
            }
            itemsDisplayPanel.revalidate();
            itemsDisplayPanel.repaint();
        };

        nextButton.addActionListener(e -> {
            if (currentIndex[0] < items.size() - 1) {
                currentIndex[0]++;
                updateItemsDisplay.run();
            }
        });

        prevButton.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                updateItemsDisplay.run();
            }
        });

        // Acciones de navegación
        backButton.addActionListener(e -> {
            if (battleListener != null) {
                battleListener.onBattleEnd(false);
            }
        });

        // Mostrar los primeros items
        updateItemsDisplay.run();
        return  itemsPanel;
    }

    public void setBattleListener(PokemonBattlePanel.BattleListener listener) {
        this.battleListener = listener;
    }
}
