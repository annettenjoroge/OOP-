import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.CubicCurve2D;
import java.io.File;

/**
 * RadioButtonDemo - Java Assignment 2
 * Displays a pet image based on the selected radio button.
 * Shows a JOptionPane message box on each selection.
 */
public class RadioButtonDemo extends JFrame implements ActionListener {

    // ── Radio buttons ──────────────────────────────────────────────────────────
    private final JRadioButton btnBird   = new JRadioButton("Bird");
    private final JRadioButton btnCat    = new JRadioButton("Cat");
    private final JRadioButton btnDog    = new JRadioButton("Dog");
    private final JRadioButton btnRabbit = new JRadioButton("Rabbit");
    private final JRadioButton btnPig    = new JRadioButton("Pig");

    // ── Image panel ────────────────────────────────────────────────────────────
    private final PetPanel petPanel = new PetPanel();

    // ══════════════════════════════════════════════════════════════════════════
    //  Constructor
    // ══════════════════════════════════════════════════════════════════════════
    public RadioButtonDemo() {
        super("RadioButtonDemo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        buildUI();

        // Default selection → Pig
        btnPig.setSelected(true);
        petPanel.setPet("Pig");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  UI construction
    // ══════════════════════════════════════════════════════════════════════════
    private void buildUI() {

        // ── Mnemonic keys (underlines visible on alt-press on some OS) ─────────
        btnBird  .setMnemonic(KeyEvent.VK_B);
        btnCat   .setMnemonic(KeyEvent.VK_C);
        btnDog   .setMnemonic(KeyEvent.VK_D);
        btnRabbit.setMnemonic(KeyEvent.VK_R);
        btnPig   .setMnemonic(KeyEvent.VK_P);

        // ── Button group (mutual exclusion) ────────────────────────────────────
        ButtonGroup group = new ButtonGroup();
        for (JRadioButton btn : new JRadioButton[]{btnBird, btnCat, btnDog, btnRabbit, btnPig}) {
            group.add(btn);
            btn.addActionListener(this);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // ── Left panel: radio buttons ──────────────────────────────────────────
        JPanel radioPanel = new JPanel(new GridLayout(5, 1, 4, 4));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        radioPanel.add(btnBird);
        radioPanel.add(btnCat);
        radioPanel.add(btnDog);
        radioPanel.add(btnRabbit);
        radioPanel.add(btnPig);

        // ── Right panel: pet image ─────────────────────────────────────────────
        petPanel.setPreferredSize(new Dimension(300, 250));
        petPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        // ── Content pane ──────────────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        content.add(radioPanel, BorderLayout.WEST);
        content.add(petPanel,   BorderLayout.CENTER);

        setContentPane(content);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ActionListener
    // ══════════════════════════════════════════════════════════════════════════
    @Override
    public void actionPerformed(ActionEvent e) {
        String pet = ((JRadioButton) e.getSource()).getText();
        petPanel.setPet(pet);

        // Show message box
        JOptionPane.showMessageDialog(
            this,
            "You selected: " + pet,
            "Pet Selection",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Entry point
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        // Run on the Event Dispatch Thread
        SwingUtilities.invokeLater(RadioButtonDemo::new);
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  Inner class: PetPanel  –  displays pet images
    // ══════════════════════════════════════════════════════════════════════════
    static class PetPanel extends JPanel {

        private String pet = "Pig";
        private ImageIcon imageIcon;

        PetPanel() {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
        }

        void setPet(String pet) {
            this.pet = pet;
            loadImage();
            repaint();
        }

        private void loadImage() {
            removeAll();
            String imagePath = getImagePath(pet);
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                add(imageLabel, BorderLayout.CENTER);
            } else {
                // Fallback to drawn images if files not found
                removeAll();
                add(new DrawnPetPanel(pet), BorderLayout.CENTER);
            }
            revalidate();
            repaint();
        }

        private String getImagePath(String petName) {
            return "images/" + petName.toLowerCase() + ".png";
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    static class DrawnPetPanel extends JPanel {
        private String pet = "Pig";

        DrawnPetPanel(String pet) {
            this.pet = pet;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g2.translate(w / 2, h / 2);

            switch (pet) {
                case "Bird"   -> drawBird(g2);
                case "Cat"    -> drawCat(g2);
                case "Dog"    -> drawDog(g2);
                case "Rabbit" -> drawRabbit(g2);
                default       -> drawPig(g2);
            }
            g2.dispose();
        }

        // ──────────────────────────────────────────────────────────────────────
        //  PIG
        // ──────────────────────────────────────────────────────────────────────
        private void drawPig(Graphics2D g) {
            Color pigPink   = new Color(255, 182, 193);
            Color pigDark   = new Color(220, 100, 130);
            Color pigSnout  = new Color(255, 150, 160);

            // Body
            g.setColor(pigPink);
            g.fillOval(-65, -35, 130, 90);
            g.setColor(pigDark);
            g.setStroke(new BasicStroke(2));
            g.drawOval(-65, -35, 130, 90);

            // Head
            g.setColor(pigPink);
            g.fillOval(-85, -60, 80, 70);
            g.setColor(pigDark);
            g.drawOval(-85, -60, 80, 70);

            // Ear left
            g.setColor(pigPink);
            int[] exL = {-85, -95, -75};
            int[] eyL = {-60, -85, -85};
            g.fillPolygon(exL, eyL, 3);
            g.setColor(pigDark);
            g.drawPolygon(exL, eyL, 3);

            // Ear right
            int[] exR = {-45, -35, -55};
            int[] eyR = {-60, -85, -85};
            g.setColor(pigPink);
            g.fillPolygon(exR, eyR, 3);
            g.setColor(pigDark);
            g.drawPolygon(exR, eyR, 3);

            // Snout
            g.setColor(pigSnout);
            g.fillOval(-60, -25, 50, 32);
            g.setColor(pigDark);
            g.drawOval(-60, -25, 50, 32);
            // Nostrils
            g.setColor(pigDark);
            g.fillOval(-53, -14, 10, 8);
            g.fillOval(-35, -14, 10, 8);

            // Eye
            g.setColor(Color.BLACK);
            g.fillOval(-72, -45, 12, 12);
            g.setColor(Color.WHITE);
            g.fillOval(-68, -43, 5, 5);

            // Tail
            g.setColor(pigDark);
            g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            CubicCurve2D tail = new CubicCurve2D.Float(65, -5, 85, -25, 95, 10, 75, 20);
            g.draw(tail);

            // Legs
            g.setColor(pigPink);
            g.setStroke(new BasicStroke(2));
            int[][] legX = {{-45,-35},{-15,-5},{15,25},{45,55}};
            int[]   legY = {50, 80};
            for (int[] lx : legX) {
                g.fillRoundRect(lx[0], legY[0], 16, 30, 8, 8);
                g.setColor(pigDark);
                g.drawRoundRect(lx[0], legY[0], 16, 30, 8, 8);
                g.setColor(pigPink);
            }

            // Label
            drawLabel(g, "Pig");
        }

        // ──────────────────────────────────────────────────────────────────────
        //  CAT
        // ──────────────────────────────────────────────────────────────────────
        private void drawCat(Graphics2D g) {
            Color fur    = new Color(180, 140, 100);
            Color furDark = new Color(130, 90, 55);
            Color inner  = new Color(255, 200, 200);

            // Body
            g.setColor(fur);
            g.fillOval(-50, 10, 100, 75);
            g.setColor(furDark);
            g.setStroke(new BasicStroke(2));
            g.drawOval(-50, 10, 100, 75);

            // Head
            g.setColor(fur);
            g.fillOval(-45, -60, 90, 80);
            g.setColor(furDark);
            g.drawOval(-45, -60, 90, 80);

            // Ears
            int[] exL = {-45, -60, -25};
            int[] eyL = {-60, -95, -85};
            g.setColor(fur);
            g.fillPolygon(exL, eyL, 3);
            g.setColor(furDark);
            g.drawPolygon(exL, eyL, 3);
            g.setColor(inner);
            int[] eixL = {-42, -55, -28};
            int[] eiyL = {-62, -88, -82};
            g.fillPolygon(eixL, eiyL, 3);

            int[] exR = {45, 60, 25};
            int[] eyR = {-60, -95, -85};
            g.setColor(fur);
            g.fillPolygon(exR, eyR, 3);
            g.setColor(furDark);
            g.drawPolygon(exR, eyR, 3);
            g.setColor(inner);
            int[] eixR = {42, 55, 28};
            int[] eiyR = {-62, -88, -82};
            g.fillPolygon(eixR, eiyR, 3);

            // Eyes
            g.setColor(new Color(60, 180, 60));
            g.fillOval(-30, -40, 20, 20);
            g.fillOval(10, -40, 20, 20);
            g.setColor(Color.BLACK);
            g.fillOval(-24, -36, 8, 14);
            g.fillOval(16, -36, 8, 14);
            g.setColor(Color.WHITE);
            g.fillOval(-22, -38, 4, 4);
            g.fillOval(18, -38, 4, 4);

            // Nose
            int[] nx = {-5, 5, 0};
            int[] ny = {-15, -15, -8};
            g.setColor(new Color(230, 100, 130));
            g.fillPolygon(nx, ny, 3);

            // Whiskers
            g.setColor(furDark);
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(-40, -10, -5, -10);
            g.drawLine(-40, -5, -5, -7);
            g.drawLine(40, -10, 5, -10);
            g.drawLine(40, -5, 5, -7);

            // Tail
            g.setColor(fur);
            g.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            CubicCurve2D tail = new CubicCurve2D.Float(50, 40, 90, 60, 100, 20, 70, 0);
            g.draw(tail);
            g.setColor(furDark);
            g.setStroke(new BasicStroke(1));
            g.draw(tail);

            drawLabel(g, "Cat");
        }

        // ──────────────────────────────────────────────────────────────────────
        //  DOG
        // ──────────────────────────────────────────────────────────────────────
        private void drawDog(Graphics2D g) {
            Color fur    = new Color(210, 170, 110);
            Color furDark = new Color(155, 110, 55);
            Color snout  = new Color(240, 210, 160);

            // Body
            g.setColor(fur);
            g.fillRoundRect(-55, 5, 110, 75, 30, 30);
            g.setColor(furDark);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(-55, 5, 110, 75, 30, 30);

            // Head
            g.setColor(fur);
            g.fillOval(-50, -65, 100, 85);
            g.setColor(furDark);
            g.drawOval(-50, -65, 100, 85);

            // Floppy ears
            g.setColor(furDark);
            g.fillOval(-65, -55, 30, 55);
            g.fillOval(35, -55, 30, 55);
            g.setColor(fur);
            g.fillOval(-60, -50, 20, 44);
            g.fillOval(40, -50, 20, 44);

            // Snout
            g.setColor(snout);
            g.fillRoundRect(-30, -20, 60, 35, 18, 18);
            g.setColor(furDark);
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(-30, -20, 60, 35, 18, 18);
            // Nose
            g.setColor(Color.BLACK);
            g.fillOval(-12, -20, 24, 14);
            g.setColor(Color.WHITE);
            g.fillOval(-6, -18, 8, 6);

            // Eyes
            g.setColor(Color.BLACK);
            g.fillOval(-32, -48, 16, 16);
            g.fillOval(16, -48, 16, 16);
            g.setColor(Color.WHITE);
            g.fillOval(-28, -46, 6, 6);
            g.fillOval(20, -46, 6, 6);

            // Mouth
            g.setColor(furDark);
            g.setStroke(new BasicStroke(2));
            g.drawArc(-20, -5, 18, 12, 200, 140);
            g.drawArc(2, -5, 18, 12, 200, 140);

            // Tail
            g.setColor(fur);
            g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            CubicCurve2D tail = new CubicCurve2D.Float(55, 10, 85, -10, 90, 30, 65, 40);
            g.draw(tail);

            // Legs
            g.setColor(fur);
            g.setStroke(new BasicStroke(2));
            int[][] legX = {{-45,-31},{-20,-6},{5,19},{30,44}};
            for (int[] lx : legX) {
                g.fillRoundRect(lx[0], 65, 14, 30, 7, 7);
                g.setColor(furDark);
                g.drawRoundRect(lx[0], 65, 14, 30, 7, 7);
                g.setColor(fur);
            }

            drawLabel(g, "Dog");
        }

        // ──────────────────────────────────────────────────────────────────────
        //  RABBIT
        // ──────────────────────────────────────────────────────────────────────
        private void drawRabbit(Graphics2D g) {
            Color white = new Color(230, 230, 235);
            Color grey  = new Color(160, 155, 165);
            Color pink  = new Color(255, 180, 190);

            // Body
            g.setColor(white);
            g.fillOval(-50, 5, 100, 80);
            g.setColor(grey);
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(-50, 5, 100, 80);

            // Tall ears
            g.setColor(white);
            g.fillRoundRect(-35, -100, 22, 80, 12, 12);
            g.fillRoundRect(13, -100, 22, 80, 12, 12);
            g.setColor(grey);
            g.drawRoundRect(-35, -100, 22, 80, 12, 12);
            g.drawRoundRect(13, -100, 22, 80, 12, 12);
            // Inner ears
            g.setColor(pink);
            g.fillRoundRect(-29, -94, 10, 65, 6, 6);
            g.fillRoundRect(19, -94, 10, 65, 6, 6);

            // Head
            g.setColor(white);
            g.fillOval(-42, -55, 84, 70);
            g.setColor(grey);
            g.drawOval(-42, -55, 84, 70);

            // Eyes
            g.setColor(new Color(210, 50, 80));
            g.fillOval(-28, -35, 14, 14);
            g.fillOval(14, -35, 14, 14);
            g.setColor(Color.WHITE);
            g.fillOval(-24, -33, 5, 5);
            g.fillOval(18, -33, 5, 5);

            // Nose
            int[] nx = {-5, 5, 0};
            int[] ny = {-14, -14, -8};
            g.setColor(pink);
            g.fillPolygon(nx, ny, 3);

            // Whiskers
            g.setColor(grey);
            g.setStroke(new BasicStroke(1));
            g.drawLine(-40, -12, -6, -12);
            g.drawLine(-40, -8,  -6, -9);
            g.drawLine(40, -12, 6, -12);
            g.drawLine(40, -8,  6, -9);

            // Fluffy tail
            g.setColor(white);
            g.fillOval(38, 40, 22, 22);
            g.setColor(grey);
            g.drawOval(38, 40, 22, 22);

            // Legs
            g.setColor(white);
            g.setStroke(new BasicStroke(1.5f));
            g.fillRoundRect(-40, 68, 18, 22, 9, 9);
            g.fillRoundRect(-10, 68, 18, 22, 9, 9);
            g.fillRoundRect(18,  68, 18, 22, 9, 9);
            g.setColor(grey);
            g.drawRoundRect(-40, 68, 18, 22, 9, 9);
            g.drawRoundRect(-10, 68, 18, 22, 9, 9);
            g.drawRoundRect(18,  68, 18, 22, 9, 9);

            drawLabel(g, "Rabbit");
        }

        // ──────────────────────────────────────────────────────────────────────
        //  BIRD
        // ──────────────────────────────────────────────────────────────────────
        private void drawBird(Graphics2D g) {
            Color feather = new Color(80, 160, 255);
            Color belly   = new Color(200, 230, 255);
            Color beak    = new Color(255, 200, 50);
            Color dark    = new Color(30, 80, 180);

            // Body
            g.setColor(feather);
            g.fillOval(-45, -20, 90, 70);
            g.setColor(dark);
            g.setStroke(new BasicStroke(2));
            g.drawOval(-45, -20, 90, 70);

            // Belly
            g.setColor(belly);
            g.fillOval(-25, 0, 50, 40);

            // Wing
            g.setColor(new Color(50, 130, 220));
            int[] wx = {-45, -80, -60, -45};
            int[] wy = {10, -10, 30, 30};
            g.fillPolygon(wx, wy, 4);
            g.setColor(dark);
            g.drawPolygon(wx, wy, 4);

            // Head
            g.setColor(feather);
            g.fillOval(-35, -70, 70, 60);
            g.setColor(dark);
            g.drawOval(-35, -70, 70, 60);

            // Crest feathers
            g.setColor(feather);
            g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(0, -70, -10, -95);
            g.drawLine(0, -70, 0, -98);
            g.drawLine(0, -70, 10, -95);

            // Eye
            g.setColor(Color.WHITE);
            g.fillOval(5, -58, 18, 18);
            g.setColor(Color.BLACK);
            g.fillOval(9, -54, 10, 10);
            g.setColor(Color.WHITE);
            g.fillOval(10, -55, 4, 4);

            // Beak
            int[] bx = {-35, -55, -35};
            int[] by = {-45, -38, -30};
            g.setColor(beak);
            g.fillPolygon(bx, by, 3);
            g.setColor(new Color(200, 140, 0));
            g.drawPolygon(bx, by, 3);

            // Feet
            g.setColor(beak);
            g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // Left foot
            g.drawLine(-15, 50, -15, 68);
            g.drawLine(-15, 68, -30, 75);
            g.drawLine(-15, 68, -10, 76);
            g.drawLine(-15, 68, 0, 73);
            // Right foot
            g.drawLine(15, 50, 15, 68);
            g.drawLine(15, 68, 0, 75);
            g.drawLine(15, 68, 20, 76);
            g.drawLine(15, 68, 30, 73);

            drawLabel(g, "Bird");
        }

        // ──────────────────────────────────────────────────────────────────────
        //  Label helper
        // ──────────────────────────────────────────────────────────────────────
        private void drawLabel(Graphics2D g, String text) {
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(text);
            g.setColor(new Color(60, 60, 60));
            g.drawString(text, -tw / 2, 95);
        }
    }
}
