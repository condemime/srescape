import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SREscape extends JFrame {
    private int score = 0;
    private int currentScreen = 0;
    private JPanel mainPanel;
    private JLabel scoreLabel;
    
    // Configuration chargée depuis le fichier properties
    private int gameDurationMinutes = 10; // Valeur par défaut
    private String firstServiceName = "Gestion Carriere"; // Valeur par défaut
    private String secondServiceName = "Referentiel Individu"; // Valeur par défaut
    
    // Chronomètre
    private Timer timer;
    private int timeRemaining; // Sera initialisé depuis la config
    private long startTime;
    private long elapsedTime = 0;
    private JLabel timerLabel;
    private boolean timerStarted = false;
    
    // Couleur de fond identique à la présentation PowerPoint
    private static final Color BACKGROUND_COLOR = new Color(52, 73, 94);
    private static final Color DARKER_BACKGROUND = new Color(44, 62, 80);
    
    // Images pré-chargées
    private BufferedImage logoImage;
    private BufferedImage supportImage;
    private BufferedImage dashboardOkImage;
    private BufferedImage dashboardKoImage;
    
    public SREscape() {
        setTitle("SREscape - Simulation Incident SRE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        
        // Charger la configuration
        loadConfiguration();
        
        // Initialiser le temps restant
        timeRemaining = gameDurationMinutes * 60;
        
        // Charger les images
        loadImages();
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Panneau supérieur avec score et chrono
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Chronomètre à gauche
        timerLabel = new JLabel(String.format("Temps: %02d:00", gameDurationMinutes), SwingConstants.LEFT);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        topPanel.add(timerLabel, BorderLayout.WEST);
        
        // Score à droite
        scoreLabel = new JLabel("Score: 0", SwingConstants.RIGHT);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        topPanel.add(scoreLabel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        add(mainPanel);
        showScreen(0);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (timer != null) {
                        timer.stop();
                    }
                    System.exit(0);
                }
            }
        });
        
        setVisible(true);
    }
    
    private void loadConfiguration() {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();
            
            // Charger la durée du jeu
            String durationStr = props.getProperty("game.duration.minutes", "10");
            gameDurationMinutes = Integer.parseInt(durationStr);
            
            // Charger les noms des services
            firstServiceName = props.getProperty("service.first.name", "Gestion Carriere");
            secondServiceName = props.getProperty("service.second.name", "Referentiel Individu");
            
            System.out.println("Configuration chargee :");
            System.out.println("  Duree: " + gameDurationMinutes + " minutes");
            System.out.println("  Service 1: " + firstServiceName);
            System.out.println("  Service 2: " + secondServiceName);
            
        } catch (IOException e) {
            System.err.println("Fichier config.properties non trouve. Utilisation des valeurs par defaut.");
        } catch (NumberFormatException e) {
            System.err.println("Erreur dans le format de la duree. Utilisation de la valeur par defaut (10 minutes).");
        }
    }
    
    private void loadImages() {
        try {
            File logoFile = new File("images/logo_entreprise.png");
            if (logoFile.exists()) {
                logoImage = ImageIO.read(logoFile);
            }
            
            File supportFile = new File("images/slide_3_0.png");
            if (supportFile.exists()) {
                supportImage = ImageIO.read(supportFile);
            }
            
            File dashOkFile = new File("images/slide_4_1.png");
            if (dashOkFile.exists()) {
                dashboardOkImage = ImageIO.read(dashOkFile);
            }
            
            File dashKoFile = new File("images/slide_6_0.png");
            if (dashKoFile.exists()) {
                dashboardKoImage = ImageIO.read(dashKoFile);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des images: " + e.getMessage());
        }
    }
    
    private void updateScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }
    
    private void startTimer() {
        if (timerStarted) return;
        
        timerStarted = true;
        startTime = System.currentTimeMillis();
        
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                int remaining = timeRemaining - (int)elapsedTime;
                
                if (remaining <= 0) {
                    timerLabel.setText("Temps: 00:00");
                    timerLabel.setForeground(new Color(231, 76, 60)); // Rouge
                } else {
                    int minutes = remaining / 60;
                    int seconds = remaining % 60;
                    timerLabel.setText(String.format("Temps: %02d:%02d", minutes, seconds));
                    
                    // Changer la couleur selon le temps restant
                    if (remaining <= 60) {
                        timerLabel.setForeground(new Color(231, 76, 60)); // Rouge
                    } else if (remaining <= 120) {
                        timerLabel.setForeground(new Color(241, 196, 15)); // Jaune
                    } else {
                        timerLabel.setForeground(Color.WHITE);
                    }
                }
            }
        });
        timer.start();
    }
    
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
    
    private String getElapsedTimeString() {
        int totalSeconds = (int)elapsedTime;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    private void showScreen(int screenNumber) {
        currentScreen = screenNumber;
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));
        
        switch (screenNumber) {
            case 0:
                showTitleScreen(content);
                break;
            case 1:
                showInstructionsScreen(content);
                break;
            case 2:
                showIncidentScreen(content);
                break;
            case 3:
                showDashboardScreen(content);
                break;
            case 4:
                showLogsScreen(content);
                break;
            case 5:
                showReferentielScreen(content);
                break;
            case 6:
                showFinalChoiceScreen(content);
                break;
            case 7:
                showResultScreen(content);
                break;
        }
        
        if (mainPanel.getComponentCount() > 1) {
            mainPanel.remove(1);
        }
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showTitleScreen(JPanel content) {
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Logo
        if (logoImage != null) {
            ImageIcon scaledIcon = new ImageIcon(logoImage.getScaledInstance(150, -1, Image.SCALE_SMOOTH));
            JLabel logoLabel = new JLabel(scaledIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(logoLabel);
            content.add(Box.createRigidArea(new Dimension(0, 35)));
        }
        
        JLabel title = createTitle("SREscape", 70);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 74)));
        
        JButton startButton = createButton("Commencer", new Color(41, 128, 185));
        startButton.addActionListener(e -> showScreen(1));
        content.add(startButton);
        
    }
    
    private void showInstructionsScreen(JPanel content) {
        JLabel title = createTitle("Mission", 60);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        
        content.add(createText("Vous êtes une équipe d'ingénieur de fiabilité des services", 32));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createText("Votre mission : assurer le bon fonctionnement des applications", 32));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(createText("Un incident va se produire, vous devrez le résoudre", 32));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        content.add(createText("A chaque étape, vous devrez prendre une décision", 32));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(createText("[+] Les bonnes décisions donnent des points", 30));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(createText("[-] Les mauvaises décisions font perdre des points", 30));
        
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        JButton continueButton = createButton("Continuer", new Color(41, 128, 185));
        continueButton.addActionListener(e -> showScreen(2));
        content.add(continueButton);
    }
    
    private void showIncidentScreen(JPanel content) {
        // Démarrer le chronomètre au début de l'incident
        startTimer();
        
        JLabel title = createTitle("INCIDENT SIGNALÉ", 36);
        title.setForeground(new Color(231, 76, 60));
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Image du support client
        if (supportImage != null) {
            ImageIcon scaledIcon = new ImageIcon(supportImage.getScaledInstance(400, -1, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(imageLabel);
            content.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        content.add(createText("Un utilisateur a appelé, il rencontre une erreur sur l'application " + firstServiceName, 17));
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(createText("Tous ses collègues ont le même problème.", 17));
        
        JButton btn1 = createChoiceButton("[TEL] Appeler le developpeur de l'application");
        btn1.addActionListener(e -> {
            updateScore(-1);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Il demande plus d'informations.\n-1 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn1);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn2 = createChoiceButton("[OBSERVABILITE] Consulter la plate-forme d'observabilité pour le service concerné");
        btn2.addActionListener(e -> {
            updateScore(5);
            showScreen(3);
        });
        content.add(btn2);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn3 = createChoiceButton("[RESTART] Redémarrer le serveur d'application");
        btn3.addActionListener(e -> {
            updateScore(-2);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! L'erreur est toujours présente.\n-2 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn3);
    }
    
    private void showDashboardScreen(JPanel content) {
        JLabel title = createTitle("TABLEAU DE BORD", 36);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        content.add(createText("Service " + firstServiceName, 18));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Image du dashboard Serice Erreur
        if (dashboardOkImage != null) {
            ImageIcon scaledIcon = new ImageIcon(dashboardOkImage.getScaledInstance(700, -1, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 2));
            content.add(imageLabel);
            content.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        JButton btn1 = createChoiceButton("[TEL] Appeler le developpeur de l'application");
        btn1.addActionListener(e -> {
            updateScore(-1);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Il faut continuer l'analyse.\n-1 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn1);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn2 = createChoiceButton("[RESEAU] Appeler l'équipe réseau");
        btn2.addActionListener(e -> {
            updateScore(-2);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Ce n'est pas un probleme réseau.\n-2 points", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn2);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn3 = createChoiceButton("[LOGS] Regarder les journaux (logs)");
        btn3.addActionListener(e -> {
            updateScore(5);
            showScreen(4);
        });
        content.add(btn3);
    }
    
    private void showLogsScreen(JPanel content) {
        JLabel title = createTitle("JOURNAUX (LOGS)", 36);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        
        content.add(createText("Vous consultez les journaux de l'application :", 20));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JTextArea logsArea = new JTextArea(
            "14:20:05 ERROR [service] Pas de réponse\n" +
            "14:20:05 ERROR [service] Service " + secondServiceName + " injoignable\n" +
            "14:20:05 INFO  [service] Lancement Recherche individu\n" +
            "14:20:04 ERROR [service] Pas de réponse\n" +
            "14:20:04 ERROR [service] Service " + secondServiceName + " injoignable\n" +
            "14:20:03 ERROR [service] Pas de réponse\n" +
            "14:20:03 ERROR [service] Service " + secondServiceName + " injoignable"
        );
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        logsArea.setBackground(new Color(39, 55, 70));
        logsArea.setForeground(new Color(46, 204, 113));
        logsArea.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
        JScrollPane scrollPane = new JScrollPane(logsArea);
        scrollPane.setMaximumSize(new Dimension(800, 150));
        scrollPane.setPreferredSize(new Dimension(800, 150));
        content.add(scrollPane);
        
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton btn1 = createChoiceButton("[TEL] Appeler le developpeur de l'application");
        btn1.addActionListener(e -> {
            updateScore(-1);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Le développeur demande plus d'informations.\n-1 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn1);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn2 = createChoiceButton("[GDI] Appeler la gestion des incidents");
        btn2.addActionListener(e -> {
            updateScore(-2);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! La GDI demande si une analyse a été effectuée.\n-2 points", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn2);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn3 = createChoiceButton("[OBSERVABILITE] Consulter la plate-forme d'observabilité pour le service " + secondServiceName);
        btn3.addActionListener(e -> {
            updateScore(5);
            showScreen(5);
        });
        content.add(btn3);
    }
    
    private void showReferentielScreen(JPanel content) {
        JLabel title = createTitle(secondServiceName.toUpperCase(), 36);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        content.add(createText("Service " + secondServiceName, 18));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Image du dashboard KO
        if (dashboardKoImage != null) {
            ImageIcon scaledIcon = new ImageIcon(dashboardKoImage.getScaledInstance(700, -1, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 2));
            content.add(imageLabel);
            content.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton btn1 = createChoiceButton("[TEL] Appeler le developpeur de l'application");
        btn1.addActionListener(e -> {
            updateScore(-1);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Il demande plus d'informations.\n-1 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn1);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn2 = createChoiceButton("[RESPONSABLE] Appeler le responsable d'application");
        btn2.addActionListener(e -> {
            updateScore(-1);
            JOptionPane.showMessageDialog(this, "Mauvais choix ! Il demande dans quel état est le statut de l'application.\n-1 point", 
                "Erreur", JOptionPane.WARNING_MESSAGE);
        });
        content.add(btn2);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn3 = createChoiceButton("[RESTART] Redémarrer le serveur d'application");
        btn3.addActionListener(e -> {
            updateScore(5);
            showScreen(6);
        });
        content.add(btn3);
    }
    
    private void showFinalChoiceScreen(JPanel content) {
        JLabel title = createTitle("SERVICE " + secondServiceName.toUpperCase() + " RETABLI", 36);
        title.setForeground(new Color(46, 204, 113));
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Image du dashboard OK (service restauré)
        try {
            File dashOkFinalFile = new File("images/slide_7_0.png");
            if (dashOkFinalFile.exists()) {
                BufferedImage dashOkFinalImage = ImageIO.read(dashOkFinalFile);
                ImageIcon scaledIcon = new ImageIcon(dashOkFinalImage.getScaledInstance(700, -1, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(scaledIcon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2));
                content.add(imageLabel);
                content.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement image finale: " + e.getMessage());
        }
        
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createText("Le trafic revient. Et maintenant ?", 17));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JButton btn1 = createChoiceButton("[TERMINER] J'ai fini mon intervention");
        btn1.addActionListener(e -> {
            updateScore(1);
            showScreen(7);
        });
        content.add(btn1);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn2 = createChoiceButton("[TEL] Rappeler l'utilisateur");
        btn2.addActionListener(e -> {
            updateScore(3);
            showScreen(7);
        });
        content.add(btn2);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton btn3 = createChoiceButton("[OBSERVABILITE] Consulter la plate-forme d'observabilité pour le service " + firstServiceName);
        btn3.addActionListener(e -> {
            updateScore(5);
            showScreen(7);
        });
        content.add(btn3);
    }
    
    private void showResultScreen(JPanel content) {
        // Arrêter le chronomètre
        stopTimer();
        
        // Pénalité si dépassement du temps
        boolean overtime = elapsedTime > timeRemaining;
        if (overtime) {
            updateScore(-5);
        }
        else {
            updateScore(5);
        }
        
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JLabel title = createTitle("BRAVO !", 56);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        content.add(createText("Vous avez réussi a rétablir le service", 22));
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Affichage du temps
        JLabel timeLabel = new JLabel("Temps : " + getElapsedTimeString());
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(overtime ? new Color(231, 76, 60) : new Color(46, 204, 113));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        content.add(timeLabel);
        
        if (overtime) {
            content.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel penaltyLabel = createText("Dépassement du temps limite ! -5 points", 18);
            penaltyLabel.setForeground(new Color(231, 76, 60));
            content.add(penaltyLabel);
        }
        
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JLabel scoreResult = new JLabel("Score Final : " + score + " points");
        scoreResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreResult.setForeground(new Color(241, 196, 15));
        scoreResult.setFont(new Font("Arial", Font.BOLD, 50));
        content.add(scoreResult);
        
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        String evaluation;
        Color evalColor;
        if (score >= 30) {
            evaluation = "[EXPERT] Expert SRE ! Intervention parfaite !";
            evalColor = new Color(46, 204, 113);
        } else if (score >= 25) {
            evaluation = "[TRES BIEN] Bonne gestion de l'incident.";
            evalColor = new Color(52, 152, 219);
        } else if (score >= 20) {
            evaluation = "[BIEN] Quelques ameliorations possibles.";
            evalColor = new Color(241, 196, 15);
        } else if (score >= 15) {
            evaluation = "[MOYEN] Revoyez les bonnes pratiques SRE.";
            evalColor = new Color(230, 126, 34);
        } else {
            evaluation = "[A AMELIORER] Formez-vous davantage !";
            evalColor = new Color(231, 76, 60);
        }
        
        JLabel evalLabel = createText(evaluation, 20);
        evalLabel.setForeground(evalColor);
        content.add(evalLabel);
        
        content.add(Box.createRigidArea(new Dimension(0, 50)));
        
        JButton replayButton = createButton("Rejouer", new Color(41, 128, 185));
        replayButton.addActionListener(e -> {
            score = 0;
            timeRemaining = gameDurationMinutes * 60;
            elapsedTime = 0;
            timerStarted = false;
            updateScore(0);
            timerLabel.setText(String.format("Temps: %02d:00", gameDurationMinutes));
            timerLabel.setForeground(Color.WHITE);
            showScreen(0);
        });
        content.add(replayButton);
        
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton quitButton = createButton("Quitter", new Color(192, 57, 43));
        quitButton.addActionListener(e -> System.exit(0));
        content.add(quitButton);
    }
    
    private JLabel createTitle(String text, int size) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(236, 240, 241));
        label.setFont(new Font("Arial", Font.BOLD, size));
        return label;
    }
    
    private JLabel createText(String text, int size) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(236, 240, 241));
        label.setFont(new Font("Arial", Font.PLAIN, size));
        return label;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(350, 50));
        button.setMaximumSize(new Dimension(350, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JButton createChoiceButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(DARKER_BACKGROUND);
        button.setForeground(new Color(236, 240, 241));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
        button.setPreferredSize(new Dimension(600, 50));
        button.setMaximumSize(new Dimension(800, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMargin(new Insets(8, 15, 8, 15));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
                button.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 3));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARKER_BACKGROUND);
                button.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 2));
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SREscape());
    }
}
