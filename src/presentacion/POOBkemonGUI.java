package presentacion;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import domain.*;

public final class POOBkemonGUI extends JFrame implements Auxiliar{
    //entradas del dominio
	private boolean random = false; // si los stats son random
	private ArrayList<String> players = new ArrayList<>(); //[trainer1, trainer2]
	private HashMap<String,ArrayList<Integer>> pokemones = new HashMap<>(); //<trainer, pokemones(int)>
	private HashMap<String,ArrayList<Integer>> moves = new HashMap<>(); //trianer, moves (en el orden de los pokemones)>
    private HashMap<String,String[][]> items = new HashMap<>();
    private ArrayList<Integer> order;
    private int[] trainersId;
    private String[] names;
	private POOBkemon game;
    private int shinyProbability = 10;
    private int criticalHitChance = 4;
	//
    private Clip clip;
    private FloatControl volumeControl;
    private int volumenInt = 70;
    //
	private JPanel introductionPanel;
	private JPanel menuPanel;
	private JPanel gameMode;
    //
    private JMenuBar menuBar;
    private JMenu menuArchivo;
    private JMenu menuOption;
    private JMenu fondos;
    private JMenu frames;
    private JMenu stats;
    private JMenu volumen;
    private JMenu probShiny ;
    private JMenu criticalHit;
    //
    private JMenuItem stastBase;
    private JMenuItem stastRamdom;
    private JMenuItem fondo1;
    private JMenuItem fondo2;
    private JMenuItem fondo3;
    private JMenuItem frame1,frame2,frame3,frame4,frame5,frame6;
    private JMenuItem itemNuevo;
    private JMenuItem itemAbrir;
    private JMenuItem itemSalvar;
    private JMenuItem itemSalir;
    private JSlider volumeSlider;
    private JSlider shinySlider;
    private JSlider criticalHitSlider;
    //
    private int fondo = 0,frame= 0, soundsMenu = 7, soundsBattle = 3;
    private JButton playButton;
    private JButton pokedexButton;
    private JButton itemsButton;
    private JButton exitButton;
    private JButton onePlayer;
    private JButton twoPlayers;
    private JButton machines;
    private JButton backButtonMenu;
    //

    
    private POOBkemonGUI() {
        this.game = POOBkemon.getInstance();
        setTitle("POOBkemon");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(750, 550);
        setMinimumSize(new Dimension(750, 550));
        setResizable(false);
        setLocationRelativeTo(null);
        prepareElements();
        prepareActions();   
    }
    private void prepareElements() {
    	prepareElementsMenu();
        prepareIntroductionPanel();
        prepareMenuPanel();
        prepareGameMode();
        add(introductionPanel);
        introductionPanel.setFocusable(true);
        introductionPanel.requestFocusInWindow();
        setIconImage(new ImageIcon(ICONS+"pokeball.png").getImage());
    }
    private void prepareActions(){
    	prepareActionsMenuBar();
    	prepareIntroductionAction();
    	prepareActionsMenuPanel();
    	prepareActionsGameMode();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }
    private void  refresh(JPanel panel) {
	    getContentPane().removeAll();
	    add(panel);
	    revalidate();
	    repaint();
	    panel.requestFocusInWindow();
	}
    private void prepareElementsMenu() {
		menuBar = new JMenuBar();
		//
		menuArchivo = new JMenu("File");
		menuOption = new JMenu("Opcions");
        fondos = new JMenu("Funds");
        frames = new JMenu("Frames");
        stats = new JMenu("Stats");
        volumen = new JMenu("Volumen");
        probShiny = new JMenu("Shiny");
        criticalHit = new JMenu("Prob.Golpe Critico");
        //
        criticalHitSlider = new JSlider(JSlider.HORIZONTAL, 4, 15, this.criticalHitChance);
        criticalHitSlider.setMajorTickSpacing(1);
        criticalHitSlider.setPaintLabels(true);
        criticalHitSlider.setSnapToTicks(true);
        shinySlider = new JSlider(JSlider.HORIZONTAL, 10, 100, this.shinyProbability);
        shinySlider.setMajorTickSpacing(10);
        shinySlider.setPaintLabels(true);
        shinySlider.setSnapToTicks(true);
        volumeSlider = new JSlider(0, 100, volumenInt);
        volumeSlider.setMajorTickSpacing(10);  // Marcas grandes cada 10 unidades
        volumeSlider.setMinorTickSpacing(5);  // Marcas pequeñas cada 5 (opcional)
        volumeSlider.setPaintTicks(true);     // Mostrar marcas
        volumeSlider.setPaintLabels(true);    // Mostrar números
        volumeSlider.setSnapToTicks(true);
        stastBase = new JMenuItem("Base");
        stastRamdom = new JMenuItem("Ramdom");
        frame1 = new JMenuItem("Dorado");
        frame2 = new JMenuItem("Undertale");
        frame3 = new JMenuItem("Clasico");
        frame4 = new JMenuItem("Esmeralda 1");
        frame5 = new JMenuItem("Esmeralda 2");
        frame6 = new JMenuItem("Esmeralda 3");
        fondo1 = new JMenuItem("Hierba alta");
        fondo2 = new JMenuItem("Alto Mando");
        fondo3 = new JMenuItem("Mar");
		itemNuevo = new JMenuItem("Nuevo Juego");
		itemAbrir = new JMenuItem("Abrir Partida");
		itemSalvar = new JMenuItem("Guardar Partida");
		itemSalir = new JMenuItem("Salir");
        //
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemSalvar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        probShiny.add(shinySlider);
        criticalHit.add(criticalHitSlider);
        volumen.add(volumeSlider);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);
        frames.add(frame5);
        frames.add(frame6);
        fondos.add(fondo1);
        fondos.add(fondo2);
        fondos.add(fondo3);
        stats.add(stastBase);
        stats.add(stastRamdom);
        menuOption.add(volumen);
        menuOption.addSeparator();
        menuOption.add(stats);
        menuOption.addSeparator();
        menuOption.add(probShiny);
        menuOption.addSeparator();
        menuOption.add(criticalHit);
        menuOption.addSeparator();
        menuOption.add(fondos);
        menuOption.add(frames);
        //
        menuBar.add(menuArchivo);
        menuBar.add(menuOption);
        //
        setJMenuBar(menuBar);
    }
    private void prepareActionsMenuBar() {
        itemNuevo.addActionListener(e -> refresh(gameMode));
        itemAbrir.addActionListener(e -> openGame());
        itemSalvar.addActionListener(e -> saveGame());
        itemSalir.addActionListener(e -> confirmExit());
        fondo1.addActionListener(e -> {fondo=0;});
        fondo2.addActionListener(e -> {fondo=1;});
        fondo3.addActionListener(e -> {fondo=2;});
        frame1.addActionListener(e -> {frame=0;});
        frame2.addActionListener(e -> {frame=2;});
        frame3.addActionListener(e -> {frame=1;});
        frame4.addActionListener(e -> {frame=3;});
        frame5.addActionListener(e -> {frame=4;});
        frame6.addActionListener(e -> {frame=5;});
        stastBase.addActionListener(e -> {random=false;});
        stastRamdom.addActionListener(e -> {random=true;});
        shinySlider.addChangeListener(e -> {this.game.setProbShiny(shinySlider.getValue());});
        criticalHitSlider.addChangeListener(e -> {this.game.setCriticalHitChance(criticalHitSlider.getValue());});
        volumeSlider.addChangeListener(e -> {
            int nuevoVolumen = volumeSlider.getValue();
            setVolumen(nuevoVolumen);
        });
    }
    private void prepareIntroductionPanel() {
    introductionPanel = new ImagePanel(null, MENU+"start.gif");
        introductionPanel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (introductionPanel.isShowing()) {
                    reproducirSonido("1-03TitleTheme.wav");
                } else {
                    detenerSonido();
                }
            }
        });
    }
    private void prepareIntroductionAction() {
        InputMap inputMap = introductionPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = introductionPanel.getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterAction");
        actionMap.put("enterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh(menuPanel);
                reproducirSonido("menu/"+getNumerRandom(soundsMenu)+".wav");
            }
        });
        
        introductionPanel.setFocusable(true);
        introductionPanel.requestFocusInWindow(); // Fuerza el foco
    }
    private void prepareMenuPanel() {
    	menuPanel = new ImagePanel(new BorderLayout(), MENU+"menuPrincipal/"+getNumerRandom(11)+".gif");
        prepareElementsMenuPanel();
    }
    private void prepareElementsMenuPanel() {
    	JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);
        //
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1,5,5));
        buttonPanel.setOpaque(false);
    	playButton = createImageButton("Play",BUTTONS+"button.png",0,0,200,60,18,false,true);
    	pokedexButton = createImageButton("Pokedex",BUTTONS+"button.png",0,0,200,60,18,false,true);
    	itemsButton = createImageButton("Items",BUTTONS+"button.png",0,0,200,60,18,false,true);
    	exitButton = createImageButton("Exit",BUTTONS+"button.png",0,0,200,60,18,false,true);
    	playButton.setPreferredSize(new Dimension(200, 60));
    	pokedexButton.setPreferredSize(new Dimension(200, 60));
    	itemsButton.setPreferredSize(new Dimension(200, 60));
    	exitButton.setPreferredSize(new Dimension(200, 60));
    	buttonPanel.add(playButton);
    	buttonPanel.add(pokedexButton);
    	buttonPanel.add(itemsButton);
    	buttonPanel.add(exitButton);
    	centerPanel.add(buttonPanel);
        menuPanel.add(centerPanel, BorderLayout.CENTER);
    }
    private void prepareActionsMenuPanel() {
    	playButton.addActionListener(e -> refresh(gameMode));
    	pokedexButton.addActionListener(e -> {
            showPokedex();
        });
    	itemsButton.addActionListener(e -> {
            showItemsGalery();
        });
        exitButton.addActionListener(e -> confirmExit());
        menuPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    detenerSonido();
                    refresh(introductionPanel);
                }
            }
        });
        menuPanel.setFocusable(true);
    }
    private void showPokedex() {
        Pokedex pokedexPanel = new Pokedex(game);
        pokedexPanel.setBattleListener(e -> {
            refresh(menuPanel);
        });
        refresh(pokedexPanel);
    }
    private void showItemsGalery() {
        ItemGalery itemsPanel = new ItemGalery(game);
        itemsPanel.setBattleListener(e -> {
            refresh(menuPanel);
        });
        refresh(itemsPanel);
    }
    private void prepareGameMode() {
    	gameMode = new ImagePanel(new BorderLayout(), MENU+"menuPrincipal/"+getNumerRandom(11)+".gif");
        prepareElementsGameMode();
    }
    private void prepareElementsGameMode() {
    	JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);
        //
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setOpaque(false);
        onePlayer = createImageButton(BUTTONS+"1Player.png",275, 100, 128, 128);
    	twoPlayers = createImageButton(BUTTONS+"vs.png",275, 170, 128, 128);
    	machines = createImageButton(BUTTONS+"mvsm.png",275, 240, 128, 128);
    	backButtonMenu = crearBotonEstilizado("Back",new Rectangle(275, 100, 20, 60),new Color(240, 240, 240, 200));

    	JPanel izqPrincipal = new JPanel(new BorderLayout());
    	izqPrincipal.setOpaque(false);
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);

        panelSur.add(new JLabel(" "), BorderLayout.SOUTH);
        panelSur.add(backButtonMenu, BorderLayout.CENTER);
        panelSur.add(new JLabel(" "), BorderLayout.WEST);

        izqPrincipal.add(panelSur, BorderLayout.SOUTH);
        
    	buttonPanel.add(onePlayer);
    	buttonPanel.add(twoPlayers);
    	buttonPanel.add(machines);
    	centerPanel.add(buttonPanel);
    	gameMode.add(centerPanel, BorderLayout.CENTER);
    	gameMode.add(izqPrincipal, BorderLayout.WEST);
    }
    private void prepareActionsGameMode() {
    	onePlayer.addActionListener(e -> {
    		String machine = chooseMachine("Escoge maquina","Por favor escoger una maquina");
            if(machine != null) {
                String name1 = askPlayerName("SetName","Ingrasa tu nombre Player");
                this.names = new String[]{name1, machine};
                createTrainers("Player1", machine);
                prepareItem();
                chooseCharacter();
            }
    		});
    	twoPlayers.addActionListener(e -> {
    		createTrainers("Player1","Player2");
            String modo = chooseMode("Escoge un modo", "Por favor escoger un modo de juego");
            if(modo != null) {
                String name1 = askPlayerName("SetName","Ingrasa tu nombre Player1");
                String name2 = askPlayerName("SetName","Ingrasa tu nombre Player2");
                this.names = new String[]{name1, name2};
                if (modo.equals("Normal")) {
                    prepareItem();
                    chooseCharacter();
                } else if (modo.equals("Survival")) {
                    createDataForGame();
                    showTimer("s");
                }
            }});
    	machines.addActionListener(e -> {
    		String machine1 = chooseMachine("Escoge maquina1","Por favor escoger una maquina");
            if(machine1 != null) {
                String machine2 = chooseMachine("Escoge maquina2", "Por favor escoger una maquina");
                if(machine1 != null) {
                    this.names = new String[]{machine1, machine2};
                    createTrainers(machine1 + "1", machine2 + "2");
                    prepareItem();
                    chooseCharacter();
                }
            }
    		});
    	backButtonMenu.addActionListener(e -> {
            refresh(menuPanel);
        });
        gameMode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                    refresh(menuPanel);
                }
            }
        });
        gameMode.setFocusable(true);
    }
    private void chooseCharacter() {
        this.trainersId = new int[2];
        JPanel chooseCharactelPanel = new ImagePanel(new BorderLayout(), SELECTION_PANEL +getNumerRandom(2)+".png");
        JButton backButtonGameMode = crearBotonEstilizado("Back",new Rectangle(275, 100, 20, 60),new Color(240, 240, 240, 200));
        JButton doneButton = crearBotonEstilizado("ok", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        doneButton.setVisible(false);
        JLabel turnLabel = new JLabel(names[0]+" choose Character", JLabel.CENTER);
        turnLabel.setOpaque(true);  // Esto es crucial para que el fondo sea visible
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setFont(cargarFuentePixel(18));
        turnLabel.setForeground(Color.blue);
        chooseCharactelPanel.add(turnLabel);

        ImagePanel imgTrainer1 = new ImagePanel(null,MENU+"null.png");
        ImagePanel imgTrainer2 = new ImagePanel(null,MENU+"null.png");
        ImagePanel gridPanel = new ImagePanel(new GridLayout(0, 4, 2, 2), MENU + "blue.png");
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        final int[] done = {0};
        final int[] trainerId = {0};
        for (int i = 0; i <= 19; i++) {
            final int newtrainer = i;
            JButton pokemonButton = createImageButton(CHARACTER_ICONS + i + ".png", 1, 1, 50, 50);
            pokemonButton.setOpaque(false);
            pokemonButton.setContentAreaFilled(false);
            pokemonButton.setFocusPainted(true);

            pokemonButton.addActionListener(e -> {
                trainerId[0] = newtrainer;
                if(done[0]==0){
                    imgTrainer1.setBackgroundImage("resources/personaje/trainers/" + newtrainer + ".png");
                }else if(done[0]==1){
                    imgTrainer2.setBackgroundImage("resources/personaje/trainers/" + newtrainer + ".png");
                }
                doneButton.setVisible(true);
            });

            gridPanel.add(pokemonButton);
        }

        backButtonGameMode.addActionListener(e -> refresh(gameMode));
        doneButton.addActionListener(ev -> {
            if(done[0]==0){
                done[0]++;
                this.trainersId[0] = trainerId[0];
                gridPanel.setBackgroundImage(MENU + "red.png");
                turnLabel.setText(names[1]+" choose Character");
                turnLabel.setForeground(new Color(255, 100, 100));
                doneButton.setVisible(false);
            }else if(done[0]==1 && trainerId[0]!= this.trainersId[0]){
                this.trainersId[1] = trainerId[0];
                choosePokemon();
            }else{
                mostrarError("Personaje", "Un jugador ya selecciono este personaje");
            }
        });
        JPanel nullPanel = new ImagePanel(null,MENU+"null.png");
        chooseCharactelPanel.add(backButtonGameMode);
        chooseCharactelPanel.add(doneButton);
        chooseCharactelPanel.add(turnLabel);
        chooseCharactelPanel.add(scrollPane);
        chooseCharactelPanel.add(imgTrainer2);
        chooseCharactelPanel.add(imgTrainer1);
        chooseCharactelPanel.add(nullPanel);
        chooseCharactelPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = chooseCharactelPanel.getWidth();
                int h = chooseCharactelPanel.getHeight();
                turnLabel.setBounds((int)(w*0),(int)(h*0),(int)(w*1),(int)(h*0.06));
                doneButton.setBounds((int)(w*0.87),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                backButtonGameMode.setBounds((int)(w*0.03),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                imgTrainer1.setBounds((int)(w*0.55),(int)(h*0.3),(int)(w*0.2),(int)(h*0.4));
                imgTrainer2.setBounds((int)(w*0.72),(int)(h*0.3),(int)(w*0.2),(int)(h*0.4));
                scrollPane.setBounds((int)(w*0.03),(int)(h*0.1),(int)(w*0.5),(int)(h*0.76));
            }
        });
        setContentPane(chooseCharactelPanel);
        revalidate();
        repaint();
    }
    private void choosePokemon() {
    	JPanel choosePokemonPanel = new ImagePanel(new BorderLayout(), SELECTION_PANEL +getNumerRandom(2)+".png");
    	choosePokemonPanel.setOpaque(false);
	    ArrayList<Integer> selectedPokemons1 = new ArrayList<>();
	    ArrayList<Integer> selectedPokemons2 = new ArrayList<>();
        JButton backButton = crearBotonEstilizado("Back",new Rectangle(275, 100, 20, 60),new Color(240, 240, 240, 200));
        JButton doneButton = crearBotonEstilizado("ok", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        JButton addButton = crearBotonEstilizado("add", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        addButton.setVisible(false);
        JLabel turnLabel = new JLabel(names[0]+" choose pokemons", JLabel.CENTER);
        turnLabel.setOpaque(true);  // Esto es crucial para que el fondo sea visible
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setFont(cargarFuentePixel(18));
        turnLabel.setForeground(Color.blue);
        choosePokemonPanel.add(turnLabel, BorderLayout.NORTH);
        JPanel pokemon = new JPanel(new BorderLayout());
        pokemon.setOpaque(false);

        ImageIcon originalball = new ImageIcon(MENU + "ball_display_" + selectedPokemons1.size() + ".png");
        ImageIcon scaledoriginalball = scaleIcon(originalball, 100, 16);
        JLabel counterImage = new JLabel(scaledoriginalball);
        counterImage.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon originalball2 = new ImageIcon(MENU + "ball_display_" + selectedPokemons2.size() + ".png");
        ImageIcon scaledoriginalball2 = scaleIcon(originalball2, 100, 16);
        JLabel counterImage2 = new JLabel(scaledoriginalball2);
        counterImage2.setHorizontalAlignment(JLabel.CENTER);

        JPanel imgTrainer1 = new ImagePanel(null,"resources/personaje/trainerIcons/" + this.trainersId[0] + ".png");
        JPanel imgTrainer2 = new ImagePanel(null,"resources/personaje/trainerIcons/" + this.trainersId[1] + ".png");
        ImagePanel gridPanel = new ImagePanel(new GridLayout(0, 4, 0, 0), MENU + "blue.png");
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(100, 400));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final int[] done = {0};
        for (int i = 1; i <= this.game.getNumPokemons(); i++) {
            final int pok = i;
            JButton pokemonButton = createImageButton(POKEMONES+"Icon/" + i + ".png", 1, 1, 50, 50);
            pokemonButton.setOpaque(false);
            pokemonButton.setContentAreaFilled(false);
            pokemonButton.setFocusPainted(true);

            pokemonButton.addActionListener(e -> {
                mostrarInfoPokemon(pokemon, this.game.getPokemonsInfo().get(pok-1));
                addButton.setVisible(true);
                for (ActionListener al : addButton.getActionListeners()) {
                    addButton.removeActionListener(al);
                }
                addButton.addActionListener(a -> {
                    if(selectedPokemons1.size() <6 && done[0]==0){
                        selectedPokemons1.add(pok);
                        ImageIcon originalMoreball = new ImageIcon(MENU + "ball_display_" + selectedPokemons1.size() + ".png");
                        Image scaledMoreball = originalMoreball.getImage().getScaledInstance(100, 16, Image.SCALE_SMOOTH);
                        counterImage.setIcon(new ImageIcon(scaledMoreball));
                        if(selectedPokemons1.size() == 6){
                            done[0]++;
                            gridPanel.setBackgroundImage(MENU + "red.png");
                            turnLabel.setText(names[1] + " choose pokemons");
                            turnLabel.setForeground(new Color(255, 100, 100));
                        }
                    }else if (selectedPokemons2.size() < 6) {
                        selectedPokemons2.add(pok);
                        ImageIcon originalMoreball = new ImageIcon(MENU + "ball_display_" + selectedPokemons2.size() + ".png");
                        Image scaledMoreball = originalMoreball.getImage().getScaledInstance(100, 16, Image.SCALE_SMOOTH);
                        counterImage2.setIcon(new ImageIcon(scaledMoreball));

                        if (selectedPokemons2.size() == 6) {
                            gridPanel.setBackgroundImage(MENU + "white.png");
                            turnLabel.setText("Presione Listo");
                            turnLabel.setForeground(Color.white);
                        }

                    }else if (selectedPokemons1.size() == 6 && selectedPokemons2.size() == 6 && doneButton.isVisible()) {
                        mostrarError("Pokemones completos","Por favor Dar en Listo");
                    }
                    addButton.setVisible(false);
                });
            });

            gridPanel.add(pokemonButton);
        }
        JPanel nullPanel = new ImagePanel(null, MENU+"nullpng");
        choosePokemonPanel.add(backButton);
        choosePokemonPanel.add(doneButton);
        choosePokemonPanel.add(turnLabel);
        choosePokemonPanel.add(scrollPane);
        choosePokemonPanel.add(imgTrainer2);
        choosePokemonPanel.add(counterImage);
        choosePokemonPanel.add(counterImage2);
        choosePokemonPanel.add(imgTrainer1);
        choosePokemonPanel.add(pokemon);
        choosePokemonPanel.add(addButton);
        choosePokemonPanel.add(nullPanel);
        choosePokemonPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = choosePokemonPanel.getWidth();
                int h = choosePokemonPanel.getHeight();
                turnLabel.setBounds((int)(w*0),(int)(h*0),(int)(w*1),(int)(h*0.06));
                doneButton.setBounds((int)(w*0.87),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                backButton.setBounds((int)(w*0.03),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                imgTrainer1.setBounds((int)(w*0.85),(int)(h*0.1),(int)(w*0.13),(int)(h*0.25));
                imgTrainer2.setBounds((int)(w*0.85),(int)(h*0.5),(int)(w*0.13),(int)(h*0.25));
                scrollPane.setBounds((int)(w*0.03),(int)(h*0.1),(int)(w*0.5),(int)(h*0.76));
                counterImage.setBounds((int)(w*0.82),(int)(h*0.28),(int)(w*0.2),(int)(h*0.2));
                counterImage2.setBounds((int)(w*0.82),(int)(h*0.68),(int)(w*0.2),(int)(h*0.2));
                pokemon.setBounds((int)(w*0.54),(int)(h*0.1),(int)(w*0.30),(int)(h*0.7));
                addButton.setBounds((int)(w*0.63),(int)(h*0.8),(int)(w*0.1),(int)(h*0.08));
            }
        });
        backButton.addActionListener(e -> chooseCharacter());
	    doneButton.addActionListener(ev -> {

            if(done[0]==0){
                if(selectedPokemons1.size()<=0){
                    mostrarError("Pokemon","Seleccione al menos un pokemon");
                }else {
                    done[0]++;
                    gridPanel.setBackgroundImage(MENU + "red.png");
                    turnLabel.setText(names[1] + " elige pokemones");
                    turnLabel.setForeground(new Color(255, 100, 100));
                }
            }else if(done[0]==1){
                if(selectedPokemons2.size()<=0){
                    mostrarError("Pokemon","Seleccione al menos un pokemon");
                }else {
                    assingPokemon(selectedPokemons1, selectedPokemons2);
                    chooseMoves();

                }
            }
	    	});
	    setContentPane(choosePokemonPanel);
	    revalidate();
	    repaint();
    }
    private void chooseMoves() {
        JPanel chooseMovesPanel = new ImagePanel(new BorderLayout(), SELECTION_PANEL +getNumerRandom(2)+".png");
        chooseMovesPanel.setOpaque(false);
        ArrayList<Integer> selectedMoves1 = new ArrayList<>();
        ArrayList<Integer> selectedMoves2 = new ArrayList<>();
        ArrayList<Integer> pokemones = new ArrayList<>();
        pokemones.addAll(this.pokemones.get(players.get(0)));
        pokemones.addAll(this.pokemones.get(players.get(1)));

        JButton backButton = crearBotonEstilizado("Back",new Rectangle(275, 100, 20, 60),new Color(240, 240, 240, 200));
        JButton doneButton = crearBotonEstilizado("ok", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        JButton addButton = crearBotonEstilizado("add", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        JButton randomPokemon = crearBotonEstilizado("? Pokemon", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        JButton randomTeam = crearBotonEstilizado("? Team", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        addButton.setVisible(false);
        doneButton.setVisible(false);
        JLabel turnLabel = new JLabel(names[0]+" choose moves", JLabel.CENTER);
        turnLabel.setOpaque(true);  // Esto es crucial para que el fondo sea visible
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setFont(cargarFuentePixel(18));
        turnLabel.setForeground(Color.blue);
        ImagePanel pokemon = new ImagePanel(null, POKEMONES+"GifNormal/"+pokemones.get(0)+".gif");
        pokemon.setOpaque(false);

        JPanel imgTrainer1 = new ImagePanel(null,"resources/personaje/trainerIcons/" + this.trainersId[0] + ".png");
        JPanel imgTrainer2 = new ImagePanel(null,"resources/personaje/trainerIcons/" + this.trainersId[1] + ".png");
        imgTrainer2.setVisible(false);

        ImagePanel gridPanel = new ImagePanel(new GridLayout(0, 2, 10, 10), MENU + "blue.png");
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margen interno

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen externo
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        final int[] pokemonActualIndex = {0};
        for (int i = 1; i <= this.game.getNumMovements(); i++) {
            final Integer pokemonId = i;
            JButton pokemonButton = createfillImageButton(this.game.getAttackForChoose(i), BUTTONS+this.game.getAttackType(i)+".png",1,1, 50, 50, 10, true,true);
            pokemonButton.setOpaque(false);
            pokemonButton.setContentAreaFilled(false);
            pokemonButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            pokemonButton.setFocusPainted(true);

            pokemonButton.addActionListener(e -> {
                addButton.setVisible(true);

                for (ActionListener al : addButton.getActionListeners()) {
                    addButton.removeActionListener(al);
                }

                addButton.addActionListener(ae -> {
                    if (selectedMoves1.size() < this.pokemones.get(players.get(0)).size()*4) {
                        selectedMoves1.add(pokemonId);

                        if (selectedMoves1.size() % 4 == 0) {
                            pokemonActualIndex[0]++;
                            if (pokemonActualIndex[0] < pokemones.size()) {
                                pokemon.setBackgroundImage(POKEMONES+"GifNormal/"+pokemones.get(pokemonActualIndex[0])+".gif");
                            }
                        }
                        if (selectedMoves1.size() == this.pokemones.get(players.get(0)).size()*4) {
                            imgTrainer1.setVisible(false);
                            imgTrainer2.setVisible(true);
                            gridPanel.setBackgroundImage(MENU + "red.png");
                            turnLabel.setText(names[1]+" choose moves");
                            turnLabel.setForeground(new Color(255, 100, 100));
                        }

                    } else if (selectedMoves1.size() == this.pokemones.get(players.get(0)).size()*4 && selectedMoves2.size() < this.pokemones.get(players.get(1)).size()*4) {
                        selectedMoves2.add(pokemonId);
                        if (selectedMoves2.size() % 4 == 0) {
                            pokemonActualIndex[0]++;
                            if (pokemonActualIndex[0] < pokemones.size()) {
                                pokemon.setBackgroundImage(POKEMONES+"GifNormal/"+pokemones.get(pokemonActualIndex[0])+".gif");
                            }
                        }
                        if (selectedMoves2.size() == this.pokemones.get(players.get(1)).size()*4 && !doneButton.isVisible()) {
                            gridPanel.setBackgroundImage(MENU + "white.png");
                            turnLabel.setText("Press Done");
                            turnLabel.setForeground(Color.white);
                            doneButton.setVisible(true);
                        }

                    } else if (selectedMoves1.size() == this.pokemones.get(players.get(0)).size()*4 && selectedMoves2.size() == this.pokemones.get(players.get(1)).size()*4 && doneButton.isVisible()) {
                        mostrarError("movimientos completos", "Porfavor Dar en Listo");
                    }
                    addButton.setVisible(false);
                });
            });

            gridPanel.add(pokemonButton);
        }
        JPanel nullPanel = new ImagePanel(null, MENU+"nullpng");
        chooseMovesPanel.add(backButton);
        chooseMovesPanel.add(doneButton);
        chooseMovesPanel.add(turnLabel);
        chooseMovesPanel.add(scrollPane);
        chooseMovesPanel.add(imgTrainer2);
        chooseMovesPanel.add(imgTrainer1);
        chooseMovesPanel.add(pokemon);
        chooseMovesPanel.add(addButton);
        chooseMovesPanel.add(nullPanel);
        chooseMovesPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = chooseMovesPanel.getWidth();
                int h = chooseMovesPanel.getHeight();
                turnLabel.setBounds((int)(w*0),(int)(h*0),(int)(w*1),(int)(h*0.06));
                doneButton.setBounds((int)(w*0.87),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                backButton.setBounds((int)(w*0.03),(int)(h*0.9),(int)(w*0.1),(int)(h*0.08));
                imgTrainer1.setBounds((int)(w*0.85),(int)(h*0.3),(int)(w*0.13),(int)(h*0.25));
                imgTrainer2.setBounds((int)(w*0.85),(int)(h*0.3),(int)(w*0.13),(int)(h*0.25));
                scrollPane.setBounds((int)(w*0.03),(int)(h*0.1),(int)(w*0.5),(int)(h*0.76));
                pokemon.setBounds((int)(w*0.54),(int)(h*0.2),(int)(w*0.30),(int)(w*0.30));
                addButton.setBounds((int)(w*0.63),(int)(h*0.8),(int)(w*0.1),(int)(h*0.08));
            }
        });
        backButton.addActionListener(e -> choosePokemon());
        doneButton.addActionListener(ev -> {
            assignMoves(selectedMoves1, selectedMoves2);
            chooseItems();
        });
        setContentPane(chooseMovesPanel);
        revalidate();
        repaint();
    }

    private void chooseItems() {
        JPanel chooseItemsPanel = new ImagePanel(new BorderLayout(), SELECTION_PANEL +getNumerRandom(2)+".png");
        chooseItemsPanel.setOpaque(false);
        //
        JLabel turnLabel = new JLabel("Jugador 1 elige", JLabel.CENTER);
        turnLabel.setOpaque(true);  // Esto es crucial para que el fondo sea visible
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setFont(cargarFuentePixel(18));
        turnLabel.setForeground(Color.blue);
        chooseItemsPanel.add(turnLabel, BorderLayout.NORTH);
        //
        JButton backButtonGameMode = crearBotonEstilizado("Back",new Rectangle(275, 100, 20, 60),new Color(240, 240, 240, 200));
        //
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.add(new JLabel(" "), BorderLayout.SOUTH);
        panelSur.add(backButtonGameMode, BorderLayout.CENTER);
        //panelSur.add(new JLabel(" "), BorderLayout.WEST);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension((int) (getWidth() * 0.25), getHeight()));
        leftPanel.add(panelSur, BorderLayout.SOUTH);
        //
        ImageIcon character = new ImageIcon(CHARACTER + "Bruno.png");
        ImageIcon scaledCharacter = scaleIcon(character, 192, 192);
        JLabel characterImage = new JLabel(scaledCharacter);
        characterImage.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon character2 = new ImageIcon(CHARACTER + "Aura.png");
        ImageIcon scaledCharacter2 = scaleIcon(character2, 192, 192);
        JLabel characterImage2 = new JLabel(scaledCharacter2);
        characterImage2.setHorizontalAlignment(JLabel.CENTER);

        JButton doneButton = crearBotonEstilizado("Listo", new Rectangle(275, 100, 100, 60), new Color(240, 240, 240, 200));
        doneButton.setBackground(new Color(200, 200, 200, 150));

        JPanel rightContent = new JPanel(new GridBagLayout());
        rightContent.setOpaque(false);
        JPanel rightContentPanel = new JPanel(new BorderLayout());
        rightContentPanel.setOpaque(false);
        rightContentPanel.add(characterImage,BorderLayout.NORTH);
        rightContentPanel.add(doneButton,BorderLayout.SOUTH);
        rightContent.add(rightContentPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension((int) (getWidth() * 0.25), getHeight()));
        rightPanel.add(rightContent,BorderLayout.CENTER);
        //
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        ImagePanel gridPanel = new ImagePanel(new GridLayout(0, 3, 0, 0), MENU + "blue.png");

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = scrollPane.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getValue() - vertical.getUnitIncrement());
            }
        });
        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getValue() + vertical.getUnitIncrement());
            }
        });

        JPanel scrollContainer = new JPanel();
        scrollContainer.setOpaque(false);
        scrollContainer.setLayout(new BoxLayout(scrollContainer, BoxLayout.Y_AXIS));
        scrollContainer.add(Box.createVerticalGlue());
        scrollContainer.add(scrollPane);
        scrollContainer.add(Box.createVerticalGlue());
        centerPanel.add(scrollContainer, BorderLayout.CENTER);
        //botones item
        final int[] contador = {0};
        JButton itemButton1 = createImageButton("x2", ITEMS+"potion.png",1,1,100,100,20,false,false);
        JButton itemButton2= createImageButton("x2", ITEMS+"superPotion.png",1,1,100,100,20,false,false);
        JButton itemButton3 = createImageButton("x2", ITEMS+"hyperPotion.png",1,1,100,100,20,false,false);
        JButton itemButton4 = createImageButton("x1", ITEMS+"revive.png",1,1,100,100,20,false,false);
        JButton itemButton5 = createImageButton("x1", ITEMS+"maxRevive.png",1,1,100,100,20,false,false);

        itemButton1.addActionListener(ev -> {
            String currentPlayer = players.get(contador[0]);
            if(Integer.parseInt(this.items.get(currentPlayer)[0][1]) < 2){
                assingItem(contador[0], 0);
                itemButton1.setText("x"+(2-Integer.parseInt(this.items.get(currentPlayer)[0][1])));
            } else {
                mostrarError("Maximo de item", "Ya tiene el maximo de este item");
            }
        });
        itemButton2.addActionListener(ev -> {
            String currentPlayer = players.get(contador[0]);
            if(Integer.parseInt(this.items.get(currentPlayer)[1][1]) < 2){
                assingItem(contador[0], 1);
                itemButton2.setText("x"+(2-Integer.parseInt(this.items.get(currentPlayer)[1][1])));
            } else {
                mostrarError("Maximo de item", "Ya tiene el maximo de este item");
            }
        });
        itemButton3.addActionListener(ev -> {
            String currentPlayer = players.get(contador[0]);
            if(Integer.parseInt(this.items.get(currentPlayer)[2][1]) < 2){
                assingItem(contador[0], 2);
                itemButton3.setText("x"+(2-Integer.parseInt(this.items.get(currentPlayer)[2][1])));
            } else {
                mostrarError("Maximo de item", "Ya tiene el maximo de este item");
            }
        });
        itemButton4.addActionListener(ev -> {
            String currentPlayer = players.get(contador[0]);
            if(Integer.parseInt(this.items.get(currentPlayer)[3][1]) < 1){
                assingItem(contador[0], 3);
                itemButton4.setText("x"+(1-Integer.parseInt(this.items.get(currentPlayer)[3][1])));
            } else {
                mostrarError("Maximo de item", "Ya tiene el maximo de este item");
            }
        });
        itemButton5.addActionListener(ev -> {
            String currentPlayer = players.get(contador[0]);
            if(Integer.parseInt(this.items.get(currentPlayer)[4][1]) < 1){
                assingItem(contador[0], 4);
                itemButton5.setText("x"+(1-Integer.parseInt(this.items.get(currentPlayer)[4][1])));
            } else {
                mostrarError("Maximo de item", "Ya tiene el maximo de este item");
            }
        });

        gridPanel.add(itemButton1);
        gridPanel.add(itemButton2);
        gridPanel.add(itemButton3);
        gridPanel.add(itemButton4);
        gridPanel.add(itemButton5);
        backButtonGameMode.addActionListener(e -> chooseMoves());
        doneButton.addActionListener(ev -> {
            if(contador[0] == 0){
                rightContentPanel.removeAll();
                itemButton1.setText("x2");
                itemButton2.setText("x2");
                itemButton3.setText("x2");
                itemButton4.setText("x1");
                itemButton5.setText("x1");
                gridPanel.repaint();
                rightContentPanel.add(characterImage2, BorderLayout.NORTH);
                rightContentPanel.add(doneButton, BorderLayout.SOUTH);
                rightContentPanel.revalidate();
                rightContentPanel.repaint();
                gridPanel.setBackgroundImage(MENU + "red.png");
                turnLabel.setText("Jugador 2 elige");
                turnLabel.setForeground(new Color(255, 100, 100));
                contador[0]++;
            }else{
                showCoin();
            }
        });
        chooseItemsPanel.add(leftPanel, BorderLayout.WEST);
        chooseItemsPanel.add(centerPanel, BorderLayout.CENTER);
        chooseItemsPanel.add(rightPanel, BorderLayout.EAST);

        setContentPane(chooseItemsPanel);
        revalidate();
        repaint();
    }
    private void showCoin(){
        int[] player1 = {0};
        ImagePanel coinPanel = new ImagePanel(null,MENU+"coin.gif");
        JButton cara =  new JButton("Cara");
        JButton sello = new JButton("Sello");
        cara.setBackground(new Color(240, 240, 240, 200));
        sello.setBackground(new Color(240, 240, 240, 200));
        coinPanel.add(cara);
        coinPanel.add(sello);
        cara.setVisible(true);
        sello.setVisible(true);

        Timer timer = new Timer(10000, e -> {
            if(player1[0]==0){
                this.order = new ArrayList<>();
                this.order.add(trainersId[0]);
                this.order.add(trainersId[1]);
                showTimer("p");
            }
        });
        Timer timer1 = new Timer(2000, e -> {
            this.order = new ArrayList<>();
            if(player1[0]==getNumerRandom(2)){
                this.order.add(trainersId[0]);
                this.order.add(trainersId[1]);
            }else{
                this.order.add(trainersId[1]);
                this.order.add(trainersId[0]);
            }
            showTimer("p");
        });
        cara.addActionListener(e -> {
            timer.stop();
            cara.setEnabled(false);
            sello.setEnabled(false);
            timer1.setRepeats(false);
            timer1.start();
            player1[0] = 1;
            cara.setBackground(Color.BLUE); // Seleccionado - azul
            sello.setBackground(Color.RED); // No seleccionado - rojo
        });
        sello.addActionListener(e -> {
            timer.stop();
            cara.setEnabled(false);
            sello.setEnabled(false);
            timer1.setRepeats(false);
            timer1.start();
            player1[0] = 2;
            sello.setBackground(Color.BLUE); // Seleccionado - azul
            cara.setBackground(Color.RED); // No seleccionado - rojo
        });

        coinPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = coinPanel.getWidth();
                int h = coinPanel.getHeight();
                cara.setBounds((int)(w*0.03),(int)(h*0.5),(int)(w*0.1),(int)(h*0.08));
                sello.setBounds((int)(w*0.87),(int)(h*0.5),(int)(w*0.1),(int)(h*0.08));
            }
        });
        timer.setRepeats(false);
        timer.start();
        refresh(coinPanel);
    }
    private void showTimer(String mode) {
        detenerSonido();
        JPanel panel = new ImagePanel(null, MENU+"pantallaDeCarga/"+getNumerRandom(7)+".gif");
        refresh(panel);
        Timer timer3 = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBattleStart(mode);
            }
        });
        timer3.setRepeats(false);
        timer3.start();
    }
    private void showBattleStart(String mode) {
        JPanel battleStartPanel = new ImagePanel(new BorderLayout(), MENU + "battleStart.png");
        battleStartPanel.setLayout(new BorderLayout());
        JPanel playerPanel = new ImagePanel(null,CHARACTER+this.trainersId[0]+PNG_EXT);
        JPanel enemyPanel = new ImagePanel(null,CHARACTER+this.trainersId[1]+PNG_EXT);
        playerPanel.setOpaque(false);
        enemyPanel.setOpaque(false);
        battleStartPanel.add(playerPanel);
        battleStartPanel.add(enemyPanel);

        // Panel para contener los GIFs (sin layout para posicionamiento manual)
        JPanel gifContainer = new JPanel(null);
        gifContainer.setOpaque(false);

        // Cargar el GIF original
        ImageIcon originalGif = new ImageIcon(MENU + "brillo.gif");
        JLabel gifLabel1 = new JLabel();
        JLabel gifLabel2 = new JLabel();

        // Ajustar el tamaño de los GIFs al cambiar el tamaño del contenedor
        gifContainer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = battleStartPanel.getWidth();
                int h = battleStartPanel.getHeight();
                playerPanel.setBounds((int)(w*0.12),(int)(h*0.541),(int)(w*0.23),(int)(h*0.32));
                enemyPanel.setBounds((int)(w*0.65),(int)(h*0.145),(int)(w*0.23),(int)(h*0.32));

                int containerHeight = gifContainer.getHeight();
                // Escalar el GIF proporcionalmente para que coincida con la altura del contenedor
                int newWidth = (int) (containerHeight * ((double) originalGif.getIconWidth() / originalGif.getIconHeight()));
                Image scaledImg = originalGif.getImage().getScaledInstance(newWidth, containerHeight, Image.SCALE_DEFAULT);
                ImageIcon scaledGif = new ImageIcon(scaledImg);

                gifLabel1.setIcon(scaledGif);
                gifLabel2.setIcon(scaledGif);
                gifLabel1.setSize(newWidth, containerHeight);
                gifLabel2.setSize(newWidth, containerHeight);

                // Posición inicial: dos GIFs uno al lado del otro
                gifLabel1.setLocation(0, 0);
                gifLabel2.setLocation(newWidth, 0);
            }
        });

        // Añadir los GIFs al contenedor
        gifContainer.add(gifLabel1);
        gifContainer.add(gifLabel2);

        // Temporizador para la animación de movimiento continuo
        Timer animationTimer = new Timer(16, e -> { // ~60 FPS (1000ms/60 ≈ 16ms)
            // Mover ambos GIFs hacia la izquierda
            gifLabel1.setLocation(gifLabel1.getX() - 2, 0); // Velocidad ajustable
            gifLabel2.setLocation(gifLabel2.getX() - 2, 0);

            // Si un GIF sale completamente por la izquierda, lo reposicionamos a la derecha del otro
            if (gifLabel1.getX() + gifLabel1.getWidth() <= 0) {
                gifLabel1.setLocation(gifLabel2.getX() + gifLabel2.getWidth(), 0);
            }
            if (gifLabel2.getX() + gifLabel2.getWidth() <= 0) {
                gifLabel2.setLocation(gifLabel1.getX() + gifLabel1.getWidth(), 0);
            }
        });

        // Iniciar/detener animación cuando el panel se muestra/oculta
        battleStartPanel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (battleStartPanel.isShowing()) {
                    animationTimer.start();
                    reproducirSonido("1-14.ReceivedBattlePoints_.wav");
                } else {
                    animationTimer.stop();
                    detenerSonido();
                }
            }
        });
        Timer timer1 = new Timer(4300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initMode(mode);
            }
        });
        timer1.setRepeats(false);
        timer1.start();
        battleStartPanel.add(gifContainer, BorderLayout.CENTER);
        refresh(battleStartPanel);
    }
    private void startBattle(POOBkemon game) {
        PokemonBattlePanel battlePanel = new PokemonBattlePanel(game, fondo, frame, order);
        battlePanel.setBattleListener(playerWon -> {
            showFinishPanel();
            this.game = POOBkemon.getInstance();
        });
        battlePanel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (battlePanel.isShowing()) {
                    reproducirSonido("batalla/"+getNumerRandom(soundsBattle)+".wav");
                } else {
                    detenerSonido();
                }
            }
        });

        refresh(battlePanel);
    }
    private void showFinishPanel(){
        String winnerId = "";
        try {
            winnerId = game.getWinner();
        } catch (POOBkemonException e) {
            Log.record(e);
        }
        JPanel fishPanel = new ImagePanel(new BorderLayout(), WINNER+getNumerRandom(2)+PNG_EXT);
        JLabel message = new JLabel("Jugador "+winnerId+" a ganado", SwingConstants.CENTER);
        message.setFont(cargarFuentePixel(30));
        message.setForeground(Color.WHITE);
        fishPanel.add(message, BorderLayout.CENTER);
        Timer timer = new Timer(5000, e -> {
            refresh(introductionPanel);
        });
        timer.setRepeats(false);
        timer.start();
        getContentPane().removeAll();
        add(fishPanel);
        revalidate();
        repaint();
    }
    // ========== Métodos auxiliares ========== //
    private void initMode(String mode){
        try {
            if (mode.equals("s")) {
                this.game.resetGame();
                this.game = Survive.getInstance();
                game.initGame(this.players, this.pokemones, this.items, this.moves, false,this.trainersId,this.names);
            } else if (mode.equals("p")) {
                this.game.resetGame();
                this.game = POOBkemon.getInstance();
                game.initGame(this.players, this.pokemones, this.items, this.moves, this.random,this.trainersId,this.names);
            }
            startBattle(game);
        }catch (POOBkemonException e){
            Log.record(e);
            refresh(introductionPanel);
            mostrarError("POOBkemon Error",e.getMessage());
        }
    }
    private void reproducirSonido(String sonido) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(SONGS + sonido));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Obtener control de volumen
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumen(volumenInt); // aplicar el volumen inicial

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void detenerSonido() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    public void setVolumen(int porcentaje) {
        this.volumenInt = porcentaje;

        if (volumeControl != null) {
            // Convertir porcentaje (0–100) al rango de volumen real
            float min = volumeControl.getMinimum(); // típicamente -80.0f
            float max = volumeControl.getMaximum(); // típicamente 0.0f
            float gain = min + (max - min) * (porcentaje / 100f);
            volumeControl.setValue(gain);
        }
    }
    //Metodos de los botones.
    private String chooseMachine(String tittle, String mensaje) {
        String[] opciones = {"Defensive", "Offensive", "Random", "Expert"};

        // Cargar la imagen de fondo
        ImageIcon fondoIcon = new ImageIcon("resources/menu/option.png"); // <- Cambia esta ruta
        Image fondo = fondoIcon.getImage();

        // Crear panel personalizado con fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setForeground(Color.WHITE); // Ajusta el color del texto según tu fondo
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 120));

        int respuesta = JOptionPane.showOptionDialog(
                null,
                panel,
                tittle,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (respuesta >= 0 && respuesta < opciones.length) {
            return opciones[respuesta];
        } else {
            return null; // default
        }
    }
    private String askPlayerName(String title, String mensaje) {
        // Cargar la imagen de fondo
        ImageIcon fondoIcon = new ImageIcon("resources/menu/option.png"); // Asegúrate que la ruta sea correcta
        Image fondo = fondoIcon.getImage();

        // Crear panel personalizado con fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 150));

        // Crear el campo de texto
        JTextField textField = new JTextField();
        textField.setFont(cargarFuentePixel(18));
        textField.setHorizontalAlignment(JTextField.CENTER);

        // Crear etiqueta
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(cargarFuentePixel(18));

        // Agregar al panel
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        // Mostrar el diálogo
        int resultado = JOptionPane.showConfirmDialog(
                null,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // Retornar el texto ingresado si presionaron OK
        if (resultado == JOptionPane.OK_OPTION && !textField.getText().trim().isEmpty()) {
            return textField.getText().trim();
        } else {
            return "N.N";
        }
    }

    private static String chooseMode(String tittle, String mensaje) {
        String[] opciones = {"Normal", "Survival"};

        // Cargar la imagen de fondo
        ImageIcon fondoIcon = new ImageIcon("resources/menu/option.png"); // <- Cambia esta ruta
        Image fondo = fondoIcon.getImage();

        // Crear panel personalizado con fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setForeground(Color.WHITE); // Ajusta el color del texto según tu fondo
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 120));

        int respuesta = JOptionPane.showOptionDialog(
                null,
                panel,
                tittle,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (respuesta >= 0 && respuesta < opciones.length) {
            return opciones[respuesta];
        } else {
            return null; // default
        }
    }
    private void openGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            try {
                game = game.open(selectedFile);
                startBattle(game);
            } catch (POOBkemonException e){
                Log.record(e);
            }
        }
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            try{
                game.save(selectedFile);
            } catch (POOBkemonException e){
                Log.record(e);
            }
        }
    }
    private void confirmExit(){
        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,new ImageIcon(ICONS+"snorlax.png"));
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    //
    private void mostrarInfoPokemon(JPanel contenedor, String[] pokemonData) {
        // Limpiar y configurar el contenedor
        contenedor.removeAll();
        contenedor.setLayout(new BorderLayout());
        contenedor.setOpaque(false);

        // Panel principal que centrará todo el contenido
        JPanel centerPanel = new ImagePanel(new GridBagLayout(), MENU+"tarjeta.png");
        centerPanel.setOpaque(false);

        // Panel de contenido con BoxLayout vertical
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 1. Nombre del Pokémon (centrado)
        JLabel nombreLabel = new JLabel(pokemonData[1], JLabel.CENTER);
        nombreLabel.setFont(cargarFuentePixel(24));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nombreLabel);
        contentPanel.add(Box.createVerticalStrut(15));

        // 2. Tipos (centrados) - MODIFICADO para usar ImagePanel
        JPanel tiposPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        tiposPanel.setOpaque(false);
        tiposPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tipo 1 con ImagePanel
        ImagePanel tipo1Panel = new ImagePanel(new BorderLayout(), TYPES + pokemonData[2] + PNG_EXT);
        tipo1Panel.setPreferredSize(new Dimension(contenedor.getWidth()/4, contenedor.getWidth()/8));
        tiposPanel.add(tipo1Panel);

        // Tipo 2 (si existe) con ImagePanel
        if(!pokemonData[3].isEmpty()) {
            ImagePanel tipo2Panel = new ImagePanel(new BorderLayout(), TYPES + pokemonData[3] + PNG_EXT);
            tipo2Panel.setPreferredSize(new Dimension(contenedor.getWidth()/4, contenedor.getWidth()/8));
            tiposPanel.add(tipo2Panel);
        }

        contentPanel.add(tiposPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // 3. Contenedor para el GIF/imagen con tamaño fijo
        JPanel gifContainer = new JPanel(new BorderLayout());
        gifContainer.setOpaque(false);
        gifContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Calcular tamaño para el GIF
        int gifSize = Math.min(contenedor.getWidth()/2, contenedor.getHeight()/3);
        gifContainer.setPreferredSize(new Dimension(gifSize, gifSize));
        gifContainer.setMaximumSize(new Dimension(gifSize, gifSize));

        // Usar ImagePanel para el GIF
        ImagePanel gifPanel = new ImagePanel(new BorderLayout(), POKEMONES + "GifNormal/" + pokemonData[0] + ".gif");
        gifPanel.setOpaque(false);

        gifContainer.add(gifPanel, BorderLayout.CENTER);
        contentPanel.add(gifContainer);
        contentPanel.add(Box.createVerticalStrut(20));

        // 4. Estadísticas (centradas)
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] stats = {"HP", "Ataque", "Defensa", "Atq. Esp.", "Def. Esp.", "Velocidad"};
        String[] statKeys = {"5", "6", "7", "8", "9", "10"};

        int fontSize = Math.max(12, contenedor.getWidth()/25);
        Font statFont = cargarFuentePixel(fontSize);

        for(int i = 0; i < stats.length; i++) {
            JPanel statRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            statRow.setOpaque(false);
            statRow.setMaximumSize(new Dimension(contenedor.getWidth()*2/3, Integer.MAX_VALUE));

            JLabel statName = new JLabel(stats[i] + ":");
            statName.setFont(statFont);

            JLabel statValue = new JLabel(pokemonData[Integer.parseInt(statKeys[i])]);
            statValue.setFont(statFont);

            statRow.add(statName);
            statRow.add(statValue);
            statsPanel.add(statRow);
        }

        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Centrar todo el contenido
        centerPanel.add(contentPanel);

        // Añadir scroll si es necesario
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contenedor.add(scrollPane, BorderLayout.CENTER);
        contenedor.revalidate();
        contenedor.repaint();
    }

    //
    private void createDataForGame(){
        createTrainers();
    	createPokemones();
    	createMoves();
        prepareItem();
    }

    private void createTrainers(){
        this.trainersId = new int[]{0,1};
        this.order = new ArrayList<>();
        this.order.add(trainersId[0]);
        this.order.add(trainersId[1]);
    }

    private void createPokemones(){
    	ArrayList<Integer> pokemones1 = new ArrayList<>();
    	ArrayList<Integer> pokemones2 = new ArrayList<>();
    	for(int i = 0; i<6; i++) {
    		pokemones1.add(getNumerRandom(386));
    		pokemones2.add(getNumerRandom(386));
    	}
    	assingPokemon(pokemones1, pokemones2);
    }
    private void createMoves(){
    	ArrayList<Integer> pokemons1moves = new ArrayList<>();
    	ArrayList<Integer> pokemons2moves = new ArrayList<>();
    	for(int i = 0; i<24; i++) {
    		pokemons1moves.add(getNumerRandom(354));
    		pokemons2moves.add(getNumerRandom(354));
    	}

        assignMoves(pokemons1moves, pokemons2moves);
    }
    private void prepareItem(){
        for(int i =0 ; i<2; i++) {
            String[][] items ={{"Potion", "0","25"},{"Potion", "0","50"},{"Potion", "0","100"},{"Revive", "0"},{"MaxRevive", "0"}};
            this.items.put(this.players.get(i), items);
        }
    }
    public static int getNumerRandom(int limit) {
		Random random = new Random();
        return random.nextInt(limit) + 1;
    }
    private void createTrainers(String trainer1, String trainer2){
        this.players.clear();
    	players.add(trainer1);
    	players.add(trainer2);
    }
    private void assingPokemon(ArrayList<Integer> trainer1, ArrayList<Integer> trainer2){
    	pokemones.put(players.get(0),trainer1);
    	pokemones.put(players.get(1),trainer2);
    }
    private void assignMoves(ArrayList<Integer> trainer1, ArrayList<Integer> trainer2){
    	moves.put(players.get(0),trainer1);
    	moves.put(players.get(1),trainer2);
    }
    private void assingItem(int player, int item){
        String[][] items = this.items.get(players.get(player));
        items[item][1] = String.valueOf(Integer.parseInt(items[item][1])+1);
    }
    //
    public static void main(String[] args) {
       POOBkemonGUI ventana = new POOBkemonGUI();
       ventana.setVisible(true);
    }
}