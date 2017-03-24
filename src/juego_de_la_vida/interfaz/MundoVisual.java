package juego_de_la_vida.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import javax.swing.*;
import juego_de_la_vida.Mundo;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class MundoVisual extends JFrame {

    boolean flagClick = false;

    public MundoVisual(CelulaVisual[][] matrizCelulas, final Mundo mundo, final PrincipalVisual padre) {
        this(matrizCelulas, mundo);
        //configura la forma en que se cerrara la ventana del juego y se pasara
        //nuevamente a la ventana inicial
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                padre.setVisible(true);
                mundo.terminar();
            }
        });
    }

    public MundoVisual(CelulaVisual[][] matrizCelulas, Mundo mundo) {
        int ancho = matrizCelulas[0].length;
        int alto = matrizCelulas.length;

        //setea las propiedades de la ventana
        this.setTitle("Juego de la vida");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(ancho * 50, alto * 50 + 100);
        this.setMinimumSize(new Dimension(550, 350));
        this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.toFront();

        //content es el contenedor de la botonera y la matrizCelulas.
        JPanel content = new JPanel(new BorderLayout());
        add(content);

        //botonera es el contenedor de los botones de random, reset y pausa.
        JPanel botonera = new JPanel(new GridLayout());
        botonera.setBackground(Color.darkGray);
        content.add(botonera, BorderLayout.NORTH);

        //matrizCelulas es el contenedor de las celulas visuales.
        JPanel matrizCelulasVisual = new JPanel(new GridLayout(alto, ancho));
        matrizCelulasVisual.setBackground(Color.BLACK);
        content.add(matrizCelulasVisual, BorderLayout.CENTER);

        //boton para poner todas las celulas muertas
        JButton botonReset = new JButton();
        botonReset.setToolTipText("RESET");
        botonReset.setIcon(new ImageIcon(getClass().getResource("../recursos/reset.png")));
        botonReset.setContentAreaFilled(false);
        botonReset.setBorderPainted(false);
        botonReset.setRolloverEnabled(true);
        botonReset.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/reset-rollover.gif")));
        botonReset.addActionListener(new ResetListener(mundo));

        //boton para setear las celulas en estados aleatorios
        JButton botonRandom = new JButton();
        botonRandom.setToolTipText("RANDOM");
        botonRandom.setIcon(new ImageIcon(getClass().getResource("../recursos/random.png")));
        botonRandom.setContentAreaFilled(false);
        botonRandom.setBorderPainted(false);
        botonRandom.setRolloverEnabled(true);
        botonRandom.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/random-rollover.png")));
        botonRandom.addActionListener(new RandomListener(mundo));

        //boton para pausar y reanudar el juego.
        JButton botonPause = new JButton();
        botonPause.setToolTipText("PLAY/PAUSA");
        botonPause.setContentAreaFilled(false);
        botonPause.setBorderPainted(false);
        botonPause.setRolloverEnabled(true);

        if (mundo.getPausa()) {
            botonPause.setIcon(new ImageIcon(getClass().getResource("../recursos/pause.png")));
            botonPause.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/pause-rollover.png")));
        } else {
            botonPause.setIcon(new ImageIcon(getClass().getResource("../recursos/play.gif")));
            botonPause.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/play-rollover.gif")));
        }
        botonPause.addActionListener(new PauseListener(mundo, botonPause));

        //botones de velocidad
        JPanel botoneraVelocidad = new JPanel(new BorderLayout());
        botoneraVelocidad.setBackground(Color.darkGray);
        JLabel labelVelocidad = new JLabel(110 - (((mundo.getVelocidad() + 1) * 100) / 10) + "%");
        labelVelocidad.setFont(new java.awt.Font("Tahoma", 0, 45));
        JPanel botoneraVelocidadMod = new JPanel(new GridLayout(1, 2));
        botoneraVelocidadMod.setBackground(Color.darkGray);
        JButton botonSubirVel = new JButton();
        botonSubirVel.setToolTipText("SUBIR VELOCIDAD");
        botonSubirVel.setIcon(new ImageIcon(getClass().getResource("../recursos/subir-velocidad.png")));
        botonSubirVel.setContentAreaFilled(false);
        botonSubirVel.setBorderPainted(false);
        botonSubirVel.setRolloverEnabled(true);
        botonSubirVel.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/subir-velocidad-rollover.png")));
        botonSubirVel.addActionListener(new SubirVelListener(mundo, labelVelocidad));
        JButton botonBajarVel = new JButton();
        botonBajarVel.setToolTipText("BAJAR VELOCIDAD");
        botonBajarVel.setIcon(new ImageIcon(getClass().getResource("../recursos/bajar-velocidad.png")));
        botonBajarVel.setContentAreaFilled(false);
        botonBajarVel.setBorderPainted(false);
        botonBajarVel.setRolloverEnabled(true);
        botonBajarVel.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/bajar-velocidad-rollover.png")));
        botonBajarVel.addActionListener(new BajarVelListener(mundo, labelVelocidad));

        //agregar los botones a la botonera.
        botonera.add(botonReset);
        botonera.add(botonRandom);
        botonera.add(botonPause);

        botonera.add(botoneraVelocidad);
        botoneraVelocidad.add(labelVelocidad, BorderLayout.NORTH);
        botoneraVelocidad.add(botoneraVelocidadMod, BorderLayout.CENTER);
        botoneraVelocidadMod.add(botonBajarVel);
        botoneraVelocidadMod.add(botonSubirVel);

        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                //agrega los botones que hacen de celulas visuales al contenedor de las celulas visuales.
                matrizCelulasVisual.add(matrizCelulas[i][j].getButton());
                //mundoCelulas[i][j].getButton().addActionListener(new CelulaListener(juego, i, j));
                matrizCelulas[i][j].getButton().addMouseListener(new CelulaListener(this, mundo, i, j));
            }
        }

    }

    /**
     * Setea un flag que indica cuando se esta presionando el click.
     *
     * @param flagClick
     */
    public void setFlagClick(boolean flagClick) {
        this.flagClick = flagClick;
    }

    /**
     *
     * @return flag que indica cuando se esta presionando el click.
     */
    public boolean getFlagClick() {
        return flagClick;
    }

    //Listener que realiza el cambio de estado en las celulas al clickearlas.
    class CelulaListener implements MouseListener {

        MundoVisual ventana;
        Mundo mundo;
        int celPosX;
        int celPosY;

        public CelulaListener(MundoVisual ventana, Mundo mundo, int celPosX, int celPosY) {
            this.ventana = ventana;
            this.mundo = mundo;
            this.celPosX = celPosX;
            this.celPosY = celPosY;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            mundo.getMatrizCelulas()[celPosX][celPosY].modEstadoAct();
            ventana.setFlagClick(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ventana.setFlagClick(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (ventana.getFlagClick()) {
                mundo.getMatrizCelulas()[celPosX][celPosY].modEstadoAct();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
    
    //Listener para el boton de resetear.
    class ResetListener implements ActionListener {

        Mundo mundo;

        public ResetListener(Mundo mundo) {
            this.mundo = mundo;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            mundo.reset();
        }

    }

    //Listener para el boton de random.
    class RandomListener implements ActionListener {

        Mundo mundo;

        public RandomListener(Mundo mundo) {
            this.mundo = mundo;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            mundo.random();
        }

    }

    //Listener para el boton de pausa.
    class PauseListener implements ActionListener {

        Mundo mundo;
        JButton boton;

        public PauseListener(Mundo mundo, JButton boton) {
            this.mundo = mundo;
            this.boton = boton;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (!mundo.getPausa()) {
                mundo.modPausa();
                boton.setIcon(new ImageIcon(getClass().getResource("../recursos/pause.png")));
                boton.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/pause-rollover.png")));
            } else {
                mundo.modPausa();
                mundo.despertar();
                boton.setIcon(new ImageIcon(getClass().getResource("../recursos/play.gif")));
                boton.setRolloverIcon(new ImageIcon(getClass().getResource("../recursos/play-rollover.gif")));
            }
        }

    }

    //Listener para el boton de subir volumen.
    class SubirVelListener implements ActionListener {

        Mundo mundo;
        JLabel labelVelocidad;

        public SubirVelListener(Mundo mundo, JLabel labelVelocidad) {
            this.mundo = mundo;
            this.labelVelocidad = labelVelocidad;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            mundo.subirVelocidad();
            labelVelocidad.setText(110 - (((mundo.getVelocidad() + 1) * 100) / 10) + "%");
        }

    }

    //Listener para el boton de bajar volumen.
    class BajarVelListener implements ActionListener {

        Mundo mundo;
        JLabel labelVelocidad;

        public BajarVelListener(Mundo mundo, JLabel labelVelocidad) {
            this.mundo = mundo;
            this.labelVelocidad = labelVelocidad;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            mundo.bajarVelocidad();
            labelVelocidad.setText(110 - (((mundo.getVelocidad() + 1) * 100) / 10) + "%");
        }

    }

}
