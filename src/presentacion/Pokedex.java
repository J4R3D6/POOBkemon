package presentacion;

import domain.Log;
import domain.POOBkemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public final class Pokedex extends JPanel implements Auxiliar {

    private static final int MAX_FONDOS = 4;
    private JPanel mainPanel;
    private PokemonBattlePanel.BattleListener battleListener;
    private POOBkemon game;
    private ImagePanel pokedexPanel;
    private int fondoActual = 0;

    public Pokedex(POOBkemon game) {
        if (game == null) throw new IllegalArgumentException("Game cannot be null");
        this.game = game;
        initializeUi();
    }

    private void initializeUi() {
        setLayout(new BorderLayout());
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(showPokedex(), BorderLayout.CENTER);
    }

    private JPanel showPokedex() {
        pokedexPanel = new ImagePanel(null, POKEDEX + fondoActual + PNG_EXT);
        ArrayList<String[]> pokemones = this.game.getPokemonsInfo();
        final int[] currentIndex = {0};

        // Componentes de la interfaz
        JLabel imagenLabel = new JLabel();
        imagenLabel.setBounds(215, 150, 150, 150);
        imagenLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Agregar un único MouseListener
        imagenLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDialogoPokemon(pokemones.get(currentIndex[0]));
            }
        });

        JLabel type1 = new JLabel();
        type1.setBounds(20, 120, 150, 150);
        JLabel type2 = new JLabel();
        type2.setBounds(20, 200, 150, 150);

        JLabel imagenArriba = new JLabel();
        imagenArriba.setBounds(215, 82, 150, 55);
        JLabel imagenAbajo = new JLabel();
        imagenAbajo.setBounds(215, 310, 150, 55);

        JTextPane infoPane = new JTextPane();
        infoPane.setBounds(440, 95, 280, 320);
        infoPane.setEditable(false);
        infoPane.setFont(cargarFuentePixel(20));
        infoPane.setOpaque(false);

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        JButton upButton = crearBotonEstilizado("Up", new Rectangle(250, 30, 80, 40), new Color(240, 240, 240, 200));
        JButton downButton = crearBotonEstilizado("Down", new Rectangle(250, 380, 80, 40), new Color(240, 240, 240, 200));
        JButton backButton = crearBotonTransparente("BACK", new Rectangle(30, 395, 130, 40), true);

        // Agregar componentes al panel
        pokedexPanel.add(imagenLabel);
        pokedexPanel.add(type1);
        pokedexPanel.add(type2);
        pokedexPanel.add(imagenArriba);
        pokedexPanel.add(imagenAbajo);
        pokedexPanel.add(infoPane);
        pokedexPanel.add(upButton);
        pokedexPanel.add(downButton);
        pokedexPanel.add(backButton);

        // Runnable para actualizar la vista
        Runnable actualizarVista = () -> {
            listaPanel.removeAll();
            fondoActual = (fondoActual + 1) % MAX_FONDOS;
            pokedexPanel.setBackgroundImage(POKEDEX + fondoActual + PNG_EXT);

            for (int i = 0; i < pokemones.size(); i++) {
                String[] p = pokemones.get(i);
                JLabel pokemonLabel = new JLabel((i + 1) + ". " + p[1]);
                pokemonLabel.setFont(cargarFuentePixel(20));

                if (i == currentIndex[0]) {
                    try {
                        // Actualizar imagen principal
                        ImageIcon icon = new ImageIcon(POKEMONES + "Normal/" + (i + 1) + PNG_EXT);
                        imagenLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

                        // Actualizar tipos
                        ImageIcon t1 = new ImageIcon(TYPES + p[2] + PNG_EXT);
                        ImageIcon t2 = new ImageIcon(TYPES + p[3] + PNG_EXT);
                        type1.setIcon(new ImageIcon(t1.getImage().getScaledInstance(128, 56, Image.SCALE_SMOOTH)));
                        type2.setIcon(new ImageIcon(t2.getImage().getScaledInstance(128, 56, Image.SCALE_SMOOTH)));

                        // Actualizar imágenes circundantes
                        if (currentIndex[0] > 0) {
                            ImageIcon iconAnterior = new ImageIcon(POKEMONES + "Normal/" + i + PNG_EXT);
                            imagenArriba.setIcon(new ImageIcon(iconAnterior.getImage().getScaledInstance(130, 55, Image.SCALE_SMOOTH)));
                        } else {
                            imagenArriba.setIcon(null);
                        }

                        if (currentIndex[0] < pokemones.size() - 1) {
                            ImageIcon iconSiguiente = new ImageIcon(POKEMONES + "Normal/" + (i + 2) + PNG_EXT);
                            imagenAbajo.setIcon(new ImageIcon(iconSiguiente.getImage().getScaledInstance(130, 55, Image.SCALE_SMOOTH)));
                        } else {
                            imagenAbajo.setIcon(null);
                        }

                        // Actualizar información
                        infoPane.setText("");
                        String pokemon = this.getListPokemones(i, pokemones);
                        infoPane.getStyledDocument().insertString(0, pokemon, null);
                    } catch (Exception e) {
                        Log.record(e);
                        imagenLabel.setIcon(null);
                    }
                }
                listaPanel.add(pokemonLabel);
            }
            listaPanel.revalidate();
            listaPanel.repaint();
        };

        // Listeners para los botones
        upButton.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                actualizarVista.run();
            }
        });

        downButton.addActionListener(e -> {
            if (currentIndex[0] < pokemones.size() - 1) {
                currentIndex[0]++;
                actualizarVista.run();
            }
        });

        backButton.addActionListener(e -> {
            if (battleListener != null) {
                battleListener.onBattleEnd(false);
            }
        });

        // Configuración de teclado
        InputMap inputMap = pokedexPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = pokedexPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "arriba");
        inputMap.put(KeyStroke.getKeyStroke("W"), "arriba");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "abajo");
        inputMap.put(KeyStroke.getKeyStroke("S"), "abajo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc");

        actionMap.put("arriba", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex[0] > 0) {
                    currentIndex[0]--;
                    actualizarVista.run();
                }
            }
        });
        actionMap.put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción al presionar ESC
                battleListener.onBattleEnd(false);
            }
        });

        actionMap.put("abajo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex[0] < pokemones.size() - 1) {
                    currentIndex[0]++;
                    actualizarVista.run();
                }
            }
        });

        actualizarVista.run();
        return pokedexPanel;
    }

    private void mostrarDialogoPokemon(String[] pokemon) {
        JPanel panel = new ImagePanel(new GridBagLayout(), POKEINFO);
        JDialog dialog = new JDialog();
        dialog.setIconImage(new ImageIcon(POKEMONES+"Icon/"+pokemon[0]+PNG_EXT).getImage());
        dialog.setTitle("Detalles de " + pokemon[1]);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();

        // Imagen del Pokémon
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.insets = new Insets(0, 0, 0, 20);
        try {
            ImageIcon icon = new ImageIcon(POKEMONES + "Normal/" + pokemon[0] + PNG_EXT);
            Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imagenGrande = new JLabel(new ImageIcon(scaledImage));
            panel.add(imagenGrande, gbc);
        } catch (Exception e) {
            Log.record(e);
        }

        // Nombre y número
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);

        JLabel nombreLabel = new JLabel(pokemon[1] + "  #" + pokemon[0]);
        nombreLabel.setFont(cargarFuentePixel(28));
        panel.add(nombreLabel, gbc);

        // Tipos
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);

        JPanel tiposPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tiposPanel.setOpaque(false);
        try {
            ImageIcon tipo1Icon = new ImageIcon(TYPES + pokemon[2] + PNG_EXT);
            tiposPanel.add(new JLabel(new ImageIcon(tipo1Icon.getImage().getScaledInstance(96, 42, Image.SCALE_SMOOTH))));

            if (!pokemon[3].isEmpty()) {
                ImageIcon tipo2Icon = new ImageIcon(TYPES + pokemon[3] + PNG_EXT);
                tiposPanel.add(new JLabel(new ImageIcon(tipo2Icon.getImage().getScaledInstance(96, 42, Image.SCALE_SMOOTH))));
            }
            ImageIcon icon = new ImageIcon(POKEMONES+"Icon/"+pokemon[0]+PNG_EXT);
            tiposPanel.add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))));
        } catch (Exception e) {
            Log.record(e);
        }
        panel.add(tiposPanel, gbc);

        // Estadísticas
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel statsPanel = new JPanel(new GridLayout(0, 2, 15, 5));
        statsPanel.setOpaque(false);
        //statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        addStat(statsPanel, "Total", pokemon[4]);
        addStat(statsPanel, "HP", pokemon[5]);
        addStat(statsPanel, "Ataque", pokemon[6]);
        addStat(statsPanel, "Defensa", pokemon[7]);
        addStat(statsPanel, "Sp. Atk", pokemon[8]);
        addStat(statsPanel, "Sp. Def", pokemon[9]);
        addStat(statsPanel, "Velocidad", pokemon[10]);

        panel.add(statsPanel, gbc);

        // Botón de cerrar
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 0, 0);

        JButton cerrarBtn = crearBotonEstilizado("Cerrar", new Rectangle(0, 0, 100, 30), Color.LIGHT_GRAY);
        cerrarBtn.addActionListener(e -> dialog.dispose());
        panel.add(cerrarBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addStat(JPanel panel, String nombre, String valor) {
        JLabel label = new JLabel(nombre + ": " + valor);
        label.setFont(cargarFuentePixel(16));
        panel.add(label);
    }

    private String getListPokemones(int a, ArrayList<String[]> pokemones) {
        String resultado = "";
        for (int i = a - 2; i <= a + 3; i++) {
            try {
                String[] pokemon = pokemones.get(i);
                resultado += "N°." + pokemon[0] + "  " + pokemon[1] + "\n\n";
            } catch (Exception e) {
                Log.record(e);
                resultado += "\n\n";
            }
        }
        return resultado;
    }

    public void setBattleListener(PokemonBattlePanel.BattleListener listener) {
        this.battleListener = listener;
    }
}