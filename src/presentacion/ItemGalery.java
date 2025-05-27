
package presentacion;

import domain.POOBkemon;

import javax.swing.*;
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
        itemsDisplayPanel.setBounds(330, 78, 370, 330);
        itemsDisplayPanel.setOpaque(false);


        // Área de información (DERECHA de la imagen) - TRANSPARENTE
        JTextPane infoPanel = new JTextPane();  // Cambiamos a JTextPane para mejor control
        infoPanel.setBounds(32, 100, 130, 200);
        infoPanel.setEditable(false);
        infoPanel.setFont(cargarFuentePixel(18));
        infoPanel.setOpaque(false);  // Hacemos el fondo transparente

        itemsPanel.add(infoPanel);

        //Botones con diseño mejorado
        JButton upButton = crearBotonEstilizado("Up", new Rectangle(490, 15, 50, 20),new Color(240, 240, 240, 200));
        JButton downButton = crearBotonEstilizado("Down", new Rectangle(490, 450, 50, 20),new Color(240, 240, 240, 200));
        JButton backButton = crearBotonTransparente("BACK", new Rectangle(30, 395, 130, 40),true);

        itemsPanel.add(upButton);
        itemsPanel.add(downButton);
        itemsPanel.add(backButton);
        itemsPanel.add(itemsDisplayPanel);
        itemsPanel.add(infoPanel);

        // Coordenadas personalizadas para cada item (puedes modificarlas)
        final int[][] itemPositions = {
                {10,30},   // Item 1 (x, y)
                {130,30},   // Item 2
                {250,30},   // Item 3
                {10,180},    // Item 4
                {130,180},   // Item 5
                {150,180} // Item 6
        };

        Runnable updateItemsDisplay = () -> {
            itemsDisplayPanel.removeAll();

            int itemsToShow = Math.min(6, items.size() - currentIndex[0]);

            for (int i = 0; i < itemsToShow; i++) {
                ArrayList<String> item = items.get(currentIndex[0] + i);

                // Crear botón con imagen del item
                JButton itemButton = createImageButton(ITEMS+item.get(0)+".png",
                        itemPositions[i][0],
                        itemPositions[i][1],
                        110, 110);

                // Acción al hacer clic en el item
                itemButton.addActionListener(e -> {
                    infoPanel.setText(item.get(1));
                });

                itemsDisplayPanel.add(itemButton);
            }

            itemsDisplayPanel.revalidate();
            itemsDisplayPanel.repaint();

            // Actualizar estado de los botones de navegación
            upButton.setEnabled(currentIndex[0] > 0);
            downButton.setEnabled(currentIndex[0] + 6 < items.size());
        };

        // Acciones de navegación
        upButton.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0] = Math.max(0, currentIndex[0] - 6);
                updateItemsDisplay.run();
            }
        });

        downButton.addActionListener(e -> {
            if (currentIndex[0] + 6 < items.size()) {
                currentIndex[0] += 6;
                updateItemsDisplay.run();
            }
        });

        backButton.addActionListener(e -> {
            if (battleListener != null) {
                battleListener.onBattleEnd(false);
            }
        });

        // Mostrar los primeros items
        updateItemsDisplay.run();
        return itemsPanel;
    }

    public void setBattleListener(PokemonBattlePanel.BattleListener listener) {
        this.battleListener = listener;
    }
}
