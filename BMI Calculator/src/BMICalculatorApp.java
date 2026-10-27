// =====================================================================
// File: BMICalculatorApp.java
// Complete single-file Smart BMI Calculator & Wellness Advisor
// =====================================================================

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/* =====================================================================
 * Entry point + Main application shell
 * ===================================================================== */
public class BMICalculatorApp extends JFrame {

    private final User user;

    private CalculatorPanel  calculatorPanel;
    private TrackingPanel    trackingPanel;
    private ReportsPanel     reportsPanel;
    private ComparisonPanel  comparisonPanel;

    public BMICalculatorApp(User user) {
        super("Smart BMI Calculator & Wellness Advisor — " + user.getUsername());
        Theme.applyWindowIcons(this);
        this.user = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.HEADER_BG);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JLabel logoIconLabel = new JLabel(Theme.getAppLogoIcon(44));
        left.add(logoIconLabel);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Smart BMI Calculator & Wellness Advisor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Personalised BMI · Tracking · Reports · Comparison");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(0x94A3B8));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(title);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(sub);

        left.add(titleBlock);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JLabel chip = new JLabel("  " + user.getFullName() + "  ·  " + user.getUsername() + "  ");
        chip.setOpaque(true);
        chip.setBackground(new Color(0x1E293B));
        chip.setForeground(new Color(0x93C5FD));
        chip.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x334155), 1),
                new EmptyBorder(7, 10, 7, 10)));

        JButton logout = Theme.primaryButton("Sign Out", Theme.ROSE, new Color(0xBE123C));
        logout.addActionListener(e -> doLogout());

        right.add(chip);
        right.add(logout);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JComponent buildTabs() {
        calculatorPanel = new CalculatorPanel(user, this::refreshAll);
        trackingPanel   = new TrackingPanel(user);
        reportsPanel    = new ReportsPanel(user);
        comparisonPanel = new ComparisonPanel(user);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(Theme.BG);
        tabs.setBorder(new EmptyBorder(10, 14, 14, 14));

        tabs.addTab("  Calculator  ",   calculatorPanel);
        tabs.addTab("  Tracking  ",     trackingPanel);
        tabs.addTab("  Reports  ",      reportsPanel);
        tabs.addTab("  Comparison  ",   comparisonPanel);

        tabs.setForegroundAt(0, Theme.BLUE);
        tabs.setForegroundAt(1, Theme.GREEN);
        tabs.setForegroundAt(2, Theme.PURPLE);
        tabs.setForegroundAt(3, Theme.ORANGE);

        tabs.setBackgroundAt(0, Theme.BLUE_SOFT);
        tabs.setBackgroundAt(1, Theme.GREEN_SOFT);
        tabs.setBackgroundAt(2, Theme.PURPLE_SOFT);
        tabs.setBackgroundAt(3, Theme.ORANGE_SOFT);

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) trackingPanel.refresh();
            else if (idx == 2) reportsPanel.refresh();
            else if (idx == 3) comparisonPanel.refresh();
        });

        return tabs;
    }

    private void refreshAll() {
        trackingPanel.refresh();
        reportsPanel.refresh();
        comparisonPanel.refresh();
    }

    private void doLogout() {
        int ok = JOptionPane.showConfirmDialog(this,
                "Sign out and return to the login screen?",
                "Sign Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        UIManager.put("InternalFrame.activeTitleBackground",   new Color(0x0F172A));
        UIManager.put("InternalFrame.activeTitleForeground",   Color.WHITE);
        UIManager.put("InternalFrame.inactiveTitleBackground", new Color(0x94A3B8));
        UIManager.put("InternalFrame.inactiveTitleForeground", Color.WHITE);
        UIManager.put("InternalFrame.titleButtonHeight", 16);
        UIManager.put("InternalFrame.titleButtonWidth",  16);
        UIManager.put("InternalFrame.closeIcon",    null);
        UIManager.put("InternalFrame.iconifyIcon",  null);
        UIManager.put("InternalFrame.maximizeIcon", null);
        UIManager.put("InternalFrame.minimizeIcon", null);

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

/* =====================================================================
 * Central colour + font palette
 * ===================================================================== */
class Theme {

    private Theme() {}

    static final Color BG          = new Color(0xF4F6FB);
    static final Color CARD        = Color.WHITE;
    static final Color BORDER      = new Color(0xE2E8F0);
    static final Color HEADER_BG   = new Color(0x0F172A);

    static final Color TEXT_DARK   = new Color(0x0F172A);
    static final Color TEXT_BODY   = new Color(0x334155);
    static final Color TEXT_MUTED  = new Color(0x64748B);
    static final Color SLATE_100   = new Color(0xF1F5F9);

    static final Color BLUE        = new Color(0x2563EB);
    static final Color BLUE_DARK   = new Color(0x1D4ED8);
    static final Color BLUE_SOFT   = new Color(0xDBEAFE);

    static final Color GREEN       = new Color(0x16A34A);
    static final Color GREEN_DARK  = new Color(0x15803D);
    static final Color GREEN_SOFT  = new Color(0xDCFCE7);

    static final Color PURPLE      = new Color(0x7C3AED);
    static final Color PURPLE_DARK = new Color(0x6D28D9);
    static final Color PURPLE_SOFT = new Color(0xEDE9FE);

    static final Color ORANGE      = new Color(0xEA580C);
    static final Color ORANGE_DARK = new Color(0xC2410C);
    static final Color ORANGE_SOFT = new Color(0xFFEDD5);

    static final Color TEAL        = new Color(0x0D9488);
    static final Color TEAL_SOFT   = new Color(0xCCFBF1);

    static final Color ROSE        = new Color(0xE11D48);
    static final Color AMBER       = new Color(0xD97706);
    static final Color CYAN        = new Color(0x0284C7);

    static final Font H1        = new Font("Segoe UI", Font.BOLD, 22);
    static final Font H2        = new Font("Segoe UI", Font.BOLD, 17);
    static final Font H3        = new Font("Segoe UI", Font.BOLD, 13);
    static final Font BODY      = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font SMALL     = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font BIG_NUMBER= new Font("Segoe UI", Font.BOLD, 34);

    static JButton primaryButton(String text, Color base, Color hover) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(base);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(base);  }
        });
        return b;
    }

    static JButton outlineButton(String text, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(fg);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg, 1),
                new EmptyBorder(7, 13, 7, 13)));
        return b;
    }

    // --- Application Logo & Window Icons ---
    private static final Map<Integer, Image> LOGO_CACHE = new HashMap<>();

    static Image getAppLogoImage(int size) {
        if (LOGO_CACHE.containsKey(size)) {
            return LOGO_CACHE.get(size);
        }

        Image img = null;
        try {
            java.net.URL url = BMICalculatorApp.class.getResource("/logo.png");
            if (url == null) {
                url = BMICalculatorApp.class.getResource("logo.png");
            }
            if (url != null) {
                BufferedImage raw = ImageIO.read(url);
                if (raw != null) {
                    img = scaleImage(raw, size);
                }
            }
        } catch (Exception ignored) {}

        if (img == null) {
            String[] candidates = {
                "logo.png",
                "src/logo.png",
                "d:/AFTAB/BMI Calculator/src/logo.png",
                "d:/AFTAB/BMI Calculator/out/production/BMI Calculator/logo.png"
            };
            for (String p : candidates) {
                File f = new File(p);
                if (f.exists()) {
                    try {
                        BufferedImage raw = ImageIO.read(f);
                        if (raw != null) {
                            img = scaleImage(raw, size);
                            break;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        if (img == null) {
            img = drawVectorLogo(size);
        }

        LOGO_CACHE.put(size, img);
        return img;
    }

    private static Image scaleImage(BufferedImage src, int size) {
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, size, size, null);
        g2.dispose();
        return scaled;
    }

    private static Image drawVectorLogo(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        GradientPaint gp = new GradientPaint(0, 0, new Color(0x2563EB), size, size, new Color(0x0D9488));
        g2.setPaint(gp);
        int pad = Math.max(1, (int)(size * 0.04));
        int dia = size - (pad * 2);
        g2.fillOval(pad, pad, dia, dia);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.04f)));
        g2.drawOval(pad, pad, dia, dia);

        float scale = size / 100.0f;
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(Math.max(2.0f, 4.5f * scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        GeneralPath pulse = new GeneralPath();
        pulse.moveTo(22 * scale, 50 * scale);
        pulse.lineTo(38 * scale, 50 * scale);
        pulse.lineTo(44 * scale, 34 * scale);
        pulse.lineTo(50 * scale, 64 * scale);
        pulse.lineTo(56 * scale, 42 * scale);
        pulse.lineTo(62 * scale, 50 * scale);
        pulse.lineTo(78 * scale, 50 * scale);
        g2.draw(pulse);

        g2.setStroke(new BasicStroke(Math.max(1.5f, 2.5f * scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc((int)(34 * scale), (int)(56 * scale), (int)(32 * scale), (int)(24 * scale), 0, 180);
        g2.drawLine((int)(50 * scale), (int)(56 * scale), (int)(50 * scale), (int)(62 * scale));

        g2.dispose();
        return img;
    }

    static ImageIcon getAppLogoIcon(int size) {
        return new ImageIcon(getAppLogoImage(size));
    }

    static List<Image> getAppLogoImages() {
        int[] sizes = {16, 24, 32, 48, 64, 128, 256};
        List<Image> list = new ArrayList<>();
        for (int s : sizes) {
            list.add(getAppLogoImage(s));
        }
        return list;
    }

    static void applyWindowIcons(Window window) {
        if (window != null) {
            try {
                window.setIconImages(getAppLogoImages());
            } catch (Exception ignored) {}
        }
    }

    static JPanel sectionCard(String title, Color accent) {
        return sectionCard(title, accent, null);
    }

    static JPanel sectionCard(String title, Color accent, JComponent trailing) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(CARD);
        wrapper.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(accent);
        bar.setBorder(new EmptyBorder(7, 14, 7, 10));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        bar.add(lbl, BorderLayout.WEST);

        if (trailing != null) {
            bar.add(trailing, BorderLayout.EAST);
        }

        wrapper.add(bar, BorderLayout.NORTH);
        return wrapper;
    }

    static JPanel statCard(String label, Color accent, JLabel valueHolder) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(9, 12, 9, 12))));

        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);

        valueHolder.setFont(new Font("Segoe UI", Font.BOLD, 19));
        valueHolder.setForeground(accent);

        p.add(l, BorderLayout.NORTH);
        p.add(valueHolder, BorderLayout.CENTER);
        return p;
    }

    static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_BODY);
        return l;
    }

    static void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCBD5E1), 1),
                new EmptyBorder(6, 9, 6, 9)));
    }

    static JTextArea displayArea() {
        JTextArea a = new JTextArea();
        a.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBackground(new Color(0xFDFEFF));
        a.setForeground(TEXT_BODY);
        a.setBorder(new EmptyBorder(12, 14, 12, 14));
        return a;
    }
}

/* =====================================================================
 * Gradient background panel
 * ===================================================================== */
class GradientPanel extends JPanel {
    private final Color c1;
    private final Color c2;
    private final boolean diagonal;

    GradientPanel(Color c1, Color c2) { this(c1, c2, true); }

    GradientPanel(Color c1, Color c2, boolean diagonal) {
        this.c1 = c1;
        this.c2 = c2;
        this.diagonal = diagonal;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = diagonal
                ? new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2)
                : new GradientPaint(0, 0, c1, 0, getHeight(), c2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}

/* =====================================================================
 * User account
 * ===================================================================== */
class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String passwordHash;
    private final String fullName;
    private final int age;
    private final String gender;
    private final long createdAt;

    User(String username, String passwordHash, String fullName, int age, String gender) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.createdAt = System.currentTimeMillis();
    }

    String getUsername()     { return username; }
    String getPasswordHash() { return passwordHash; }
    String getFullName()     { return fullName; }
    int    getAge()          { return age; }
    String getGender()       { return gender; }
    long   getCreatedAt()    { return createdAt; }
}

/* =====================================================================
 * One saved BMI measurement
 * ===================================================================== */
class BMIRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
    static final DateTimeFormatter SHORT_FMT = DateTimeFormatter.ofPattern("dd MMM");

    private final String username;
    private final LocalDateTime timestamp;
    private final int age;
    private final String gender;
    private final double heightCm;
    private final double weightKg;
    private final double bmi;
    private final String category;

    BMIRecord(String username, int age, String gender,
              double heightCm, double weightKg, double bmi, String category) {
        this.username = username;
        this.timestamp = LocalDateTime.now();
        this.age = age;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.bmi = bmi;
        this.category = category;
    }

    String        getUsername() { return username; }
    LocalDateTime getTimestamp(){ return timestamp; }
    int           getAge()      { return age; }
    String        getGender()   { return gender; }
    double        getHeightCm() { return heightCm; }
    double        getWeightKg() { return weightKg; }
    double        getBmi()      { return bmi; }
    String        getCategory() { return category; }

    String getFormattedTime() { return timestamp.format(FMT); }
    String getShortTime()     { return timestamp.format(SHORT_FMT); }

    @Override public String toString() {
        return getFormattedTime() + "  •  BMI " + String.format("%.1f", bmi) + "  (" + category + ")";
    }
}

/* =====================================================================
 * Tiny file-backed persistence layer
 * ===================================================================== */
class DataStore {

    private static final String FILE_NAME = "bmi_data.ser";
    private static DataStore instance;

    private final Map<String, User> users = new LinkedHashMap<>();
    private final List<BMIRecord> records = new ArrayList<>();

    private DataStore() { load(); }

    static synchronized DataStore get() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    synchronized boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    synchronized boolean register(User u) {
        String key = u.getUsername().toLowerCase();
        if (users.containsKey(key)) return false;
        users.put(key, u);
        save();
        return true;
    }

    synchronized User authenticate(String username, String password) {
        User u = users.get(username.toLowerCase());
        if (u == null) return null;
        return u.getPasswordHash().equals(sha256(password)) ? u : null;
    }

    synchronized void addRecord(BMIRecord r) {
        records.add(r);
        save();
    }

    synchronized void deleteRecord(BMIRecord r) {
        records.remove(r);
        save();
    }

    synchronized void clearRecords(String username) {
        records.removeIf(r -> r.getUsername().equalsIgnoreCase(username));
        save();
    }

    synchronized List<BMIRecord> recordsFor(String username) {
        List<BMIRecord> out = new ArrayList<>();
        for (BMIRecord r : records) {
            if (r.getUsername().equalsIgnoreCase(username)) out.add(r);
        }
        out.sort(Comparator.comparing(BMIRecord::getTimestamp));
        return out;
    }

    static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(s.hashCode());
        }
    }

    private static class Payload implements Serializable {
        private static final long serialVersionUID = 1L;
        Map<String, User> users = new LinkedHashMap<>();
        List<BMIRecord> records = new ArrayList<>();
    }

    private void save() {
        Payload p = new Payload();
        p.users = new LinkedHashMap<>(users);
        p.records = new ArrayList<>(records);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(p);
        } catch (IOException e) {
            System.err.println("Could not save data: " + e.getMessage());
        }
    }

    private void load() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Payload p = (Payload) in.readObject();
            users.putAll(p.users);
            records.addAll(p.records);
        } catch (Exception e) {
            System.err.println("Could not load data: " + e.getMessage());
        }
    }
}

/* =====================================================================
 * Login / registration screen
 * ===================================================================== */
class LoginFrame extends JFrame {

    private final JTextField     loginUser  = new JTextField();
    private final JPasswordField loginPass  = new JPasswordField();

    private final JTextField     regName    = new JTextField();
    private final JTextField     regUser    = new JTextField();
    private final JPasswordField regPass    = new JPasswordField();
    private final JPasswordField regConfirm = new JPasswordField();
    private final JSpinner       regAge     = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
    private final JComboBox<String> regGender = new JComboBox<>(new String[]{"Male", "Female"});

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardHost = new JPanel(cardLayout);

    private static final String CARD_LOGIN    = "LOGIN";
    private static final String CARD_REGISTER = "REGISTER";

    LoginFrame() {
        super("Smart BMI Calculator — Sign In");
        Theme.applyWindowIcons(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 620);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 560));

        GradientPanel root = new GradientPanel(new Color(0x0F172A), new Color(0x1E3A8A));
        root.setLayout(new GridBagLayout());
        setContentPane(root);

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(0, 20, 0, 40));

        JLabel logo = new JLabel(Theme.getAppLogoIcon(72));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t1 = new JLabel("Smart BMI");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 34));
        t1.setForeground(Color.WHITE);
        t1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t2 = new JLabel("Health & Diet Advisor");
        t2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t2.setForeground(new Color(0x93C5FD));
        t2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t3 = new JLabel("<html><div style='width:250px;line-height:150%'>"
                + "Track your body mass index over time, receive gender-specific "
                + "wellness guidance and compare every report you have ever saved."
                + "</div></html>");
        t3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t3.setForeground(new Color(0x94A3B8));
        t3.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(Box.createVerticalGlue());
        brand.add(logo);
        brand.add(Box.createRigidArea(new Dimension(0, 10)));
        brand.add(t1);
        brand.add(t2);
        brand.add(Box.createRigidArea(new Dimension(0, 18)));
        brand.add(t3);
        brand.add(Box.createVerticalGlue());

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 0.45; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 50, 0, 0);
        root.add(brand, gc);

        cardHost.setOpaque(false);
        cardHost.add(buildLoginCard(),    CARD_LOGIN);
        cardHost.add(buildRegisterCard(), CARD_REGISTER);

        JPanel cardWrap = new JPanel(new GridBagLayout());
        cardWrap.setOpaque(false);
        cardWrap.add(cardHost);

        gc.gridx = 1;
        gc.weightx = 0.55;
        gc.insets = new Insets(30, 0, 30, 60);
        root.add(cardWrap, gc);

        cardLayout.show(cardHost, CARD_LOGIN);
    }

    private JPanel buildLoginCard() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Color.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2E8F0), 1),
                new EmptyBorder(30, 32, 28, 32)));
        outer.setPreferredSize(new Dimension(360, 400));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("Welcome back");
        h.setFont(new Font("Segoe UI", Font.BOLD, 24));
        h.setForeground(Theme.TEXT_DARK);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel("Sign in to access your health dashboard");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        s.setForeground(Theme.TEXT_MUTED);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        head.add(h);
        head.add(Box.createRigidArea(new Dimension(0, 4)));
        head.add(s);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;
        g.insets = new Insets(6, 0, 6, 0);

        g.gridy = 0; form.add(Theme.fieldLabel("Username"), g);
        g.gridy = 1; Theme.styleField(loginUser); form.add(loginUser, g);
        g.gridy = 2; form.add(Theme.fieldLabel("Password"), g);
        g.gridy = 3; Theme.styleField(loginPass); form.add(loginPass, g);

        JButton signIn = Theme.primaryButton("Sign In", Theme.BLUE, Theme.BLUE_DARK);
        signIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signIn.setBorder(new EmptyBorder(11, 16, 11, 16));
        signIn.addActionListener(e -> doLogin());

        JButton guest = Theme.outlineButton("Continue as Guest", Theme.TEAL);
        guest.addActionListener(e -> {
            String gUser = "guest";
            DataStore ds = DataStore.get();
            if (!ds.userExists(gUser)) {
                ds.register(new User(gUser, DataStore.sha256("guest"), "Guest User", 25, "Male"));
            }
            openApp(ds.authenticate(gUser, "guest"));
        });

        JButton toRegister = new JButton("<html><u>New here? Create an account</u></html>");
        toRegister.setBorderPainted(false);
        toRegister.setContentAreaFilled(false);
        toRegister.setFocusPainted(false);
        toRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toRegister.setForeground(Theme.BLUE);
        toRegister.addActionListener(e -> cardLayout.show(cardHost, CARD_REGISTER));

        g.gridy = 4; g.insets = new Insets(18, 0, 6, 0); form.add(signIn, g);
        g.gridy = 5; g.insets = new Insets(0, 0, 6, 0);  form.add(guest, g);
        g.gridy = 6; g.insets = new Insets(6, 0, 0, 0);  form.add(toRegister, g);

        outer.add(head, BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildRegisterCard() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Color.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2E8F0), 1),
                new EmptyBorder(24, 32, 22, 32)));
        outer.setPreferredSize(new Dimension(360, 470));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("Create your profile");
        h.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h.setForeground(Theme.PURPLE);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel("Your data stays on this device");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        s.setForeground(Theme.TEXT_MUTED);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        head.add(h);
        head.add(Box.createRigidArea(new Dimension(0, 4)));
        head.add(s);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;
        g.insets = new Insets(4, 0, 4, 0);

        int y = 0;
        g.gridy = y++; form.add(Theme.fieldLabel("Full Name"), g);
        g.gridy = y++; Theme.styleField(regName); form.add(regName, g);

        g.gridy = y++; form.add(Theme.fieldLabel("Username"), g);
        g.gridy = y++; Theme.styleField(regUser); form.add(regUser, g);

        g.gridy = y++; form.add(Theme.fieldLabel("Password"), g);
        g.gridy = y++; Theme.styleField(regPass); form.add(regPass, g);

        g.gridy = y++; form.add(Theme.fieldLabel("Confirm Password"), g);
        g.gridy = y++; Theme.styleField(regConfirm); form.add(regConfirm, g);

        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);

        JPanel ageBox = new JPanel(new BorderLayout(0, 4));
        ageBox.setOpaque(false);
        ageBox.add(Theme.fieldLabel("Age"), BorderLayout.NORTH);
        regAge.setFont(Theme.BODY);
        ageBox.add(regAge, BorderLayout.CENTER);

        JPanel genBox = new JPanel(new BorderLayout(0, 4));
        genBox.setOpaque(false);
        genBox.add(Theme.fieldLabel("Gender"), BorderLayout.NORTH);
        regGender.setFont(Theme.BODY);
        genBox.add(regGender, BorderLayout.CENTER);

        row.add(ageBox);
        row.add(genBox);

        g.gridy = y++; g.insets = new Insets(8, 0, 4, 0);
        form.add(row, g);

        JButton create = Theme.primaryButton("Create Account", Theme.PURPLE, Theme.PURPLE_DARK);
        create.setFont(new Font("Segoe UI", Font.BOLD, 14));
        create.setBorder(new EmptyBorder(11, 16, 11, 16));
        create.addActionListener(e -> doRegister());

        JButton back = new JButton("<html><u>Already registered? Sign in</u></html>");
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        back.setForeground(Theme.BLUE);
        back.addActionListener(e -> cardLayout.show(cardHost, CARD_LOGIN));

        g.gridy = y++; g.insets = new Insets(16, 0, 6, 0); form.add(create, g);
        g.gridy = y;   g.insets = new Insets(2, 0, 0, 0);  form.add(back, g);

        outer.add(head, BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    private void doLogin() {
        String u = loginUser.getText().trim();
        String p = new String(loginPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Missing Details", JOptionPane.WARNING_MESSAGE);
            return;
        }
        User user = DataStore.get().authenticate(u, p);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        openApp(user);
    }

    private void doRegister() {
        String name  = regName.getText().trim();
        String u     = regUser.getText().trim();
        String p     = new String(regPass.getPassword());
        String c     = new String(regConfirm.getPassword());
        int    age   = (Integer) regAge.getValue();
        String genre = (String) regGender.getSelectedItem();

        if (name.isEmpty() || u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (u.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters.",
                    "Invalid Username", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (p.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.",
                    "Weak Password", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!p.equals(c)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (DataStore.get().userExists(u)) {
            JOptionPane.showMessageDialog(this, "That username is already taken.",
                    "Taken", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = new User(u, DataStore.sha256(p), name, age, genre);
        DataStore.get().register(user);
        JOptionPane.showMessageDialog(this, "Account created! You are now signed in.",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
        openApp(user);
    }

    private void openApp(User user) {
        BMICalculatorApp app = new BMICalculatorApp(user);
        app.setVisible(true);
        dispose();
    }
}

/* =====================================================================
 * Tab 1 — Calculator
 * ===================================================================== */
class CalculatorPanel extends JPanel {

    private static final DecimalFormat DF = new DecimalFormat("0.0");

    private final User user;
    private final Runnable onDataChanged;

    private JTextField nameField, ageField, heightField, weightField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> unitCombo;
    private JLabel heightLbl, weightLbl;

    private JLabel bmiValueLabel;
    private JLabel categoryBadge;
    private JProgressBar bmiGauge;
    private JTextArea suggestionsArea, dietPlanArea, healthTipsArea;

    // Health & Recommendations minimize/maximize controls and states
    public enum ViewState { NORMAL, MAXIMIZED, MINIMIZED }
    private ViewState currentViewState = ViewState.NORMAL;

    private JComponent inputSide;
    private JPanel resultCard;
    private JPanel resultBody;
    private JPanel resultHeader;
    private JPanel mainContentPanel;
    private JLabel miniSummaryLabel;
    private JButton minBtn, maxBtn, popoutBtn;
    private JDialog popoutDialog;
    private JPanel popoutPlaceholder;

    CalculatorPanel(User user, Runnable onDataChanged) {
        this.user = user;
        this.onDataChanged = onDataChanged;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(16, 18, 16, 18));
        buildUI();
    }

    private void buildUI() {
        mainContentPanel = new JPanel();
        mainContentPanel.setOpaque(false);
        add(mainContentPanel, BorderLayout.CENTER);

        inputSide = buildInputSide();
        resultCard = (JPanel) buildResultSide();

        setViewState(ViewState.NORMAL);

        SwingUtilities.invokeLater(() -> performCalculation(false));
    }

    private JComponent buildInputSide() {
        JPanel card = Theme.sectionCard("  Personal Details & Measurements", Theme.BLUE);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(14, 16, 16, 16));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 6, 7, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0; g.weightx = 0.34;
        g.anchor = GridBagConstraints.WEST;

        body.add(Theme.fieldLabel("Full Name"), g);
        g.gridx = 1; g.weightx = 0.66;
        nameField = new JTextField(user.getFullName());
        Theme.styleField(nameField);
        nameField.setEditable(false);
        nameField.setBackground(new Color(0xF1F5F9));
        body.add(nameField, g);

        g.gridy++; g.gridx = 0; g.weightx = 0.34;
        body.add(Theme.fieldLabel("Age (years)"), g);
        g.gridx = 1; g.weightx = 0.66;
        ageField = new JTextField(String.valueOf(user.getAge()));
        Theme.styleField(ageField);
        body.add(ageField, g);

        g.gridy++; g.gridx = 0;
        body.add(Theme.fieldLabel("Gender"), g);
        g.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        genderPanel.setOpaque(false);
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        maleRadio.setOpaque(false);
        femaleRadio.setOpaque(false);
        maleRadio.setFont(Theme.BODY);
        femaleRadio.setFont(Theme.BODY);
        maleRadio.setForeground(Theme.BLUE_DARK);
        femaleRadio.setForeground(Theme.ROSE);
        if ("Female".equalsIgnoreCase(user.getGender())) femaleRadio.setSelected(true);
        else maleRadio.setSelected(true);
        maleRadio.setEnabled(false);
        femaleRadio.setEnabled(false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(maleRadio); bg.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        body.add(genderPanel, g);

        g.gridy++; g.gridx = 0;
        body.add(Theme.fieldLabel("Unit System"), g);
        g.gridx = 1;
        unitCombo = new JComboBox<>(new String[]{"Metric (cm / kg)", "Imperial (in / lbs)"});
        unitCombo.setFont(Theme.BODY);
        unitCombo.setBackground(Color.WHITE);
        body.add(unitCombo, g);

        g.gridy++; g.gridx = 0;
        heightLbl = Theme.fieldLabel("Height (cm)");
        body.add(heightLbl, g);
        g.gridx = 1;
        heightField = new JTextField("175");
        Theme.styleField(heightField);
        body.add(heightField, g);

        g.gridy++; g.gridx = 0;
        weightLbl = Theme.fieldLabel("Weight (kg)");
        body.add(weightLbl, g);
        g.gridx = 1;
        weightField = new JTextField("72");
        Theme.styleField(weightField);
        body.add(weightField, g);

        unitCombo.addActionListener(e -> {
            if (unitCombo.getSelectedIndex() == 0) {
                heightLbl.setText("Height (cm)");
                weightLbl.setText("Weight (kg)");
                if ("69".equals(heightField.getText().trim())) heightField.setText("175");
                if ("158".equals(weightField.getText().trim())) weightField.setText("72");
            } else {
                heightLbl.setText("Height (inches)");
                weightLbl.setText("Weight (lbs)");
                if ("175".equals(heightField.getText().trim())) heightField.setText("69");
                if ("72".equals(weightField.getText().trim())) weightField.setText("158");
            }
        });

        g.gridy++; g.gridx = 0; g.gridwidth = 2;
        g.insets = new Insets(18, 6, 8, 6);
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);

        JButton calcBtn = Theme.primaryButton("Calculate & Save", Theme.BLUE, Theme.BLUE_DARK);
        calcBtn.addActionListener(e -> performCalculation(true));

        JButton resetBtn = Theme.outlineButton("Reset", Theme.TEXT_MUTED);
        resetBtn.addActionListener(e -> resetForm());

        btnPanel.add(calcBtn);
        btnPanel.add(resetBtn);
        body.add(btnPanel, g);

        g.gridy++; g.insets = new Insets(10, 6, 4, 6);
        body.add(buildReferenceBox(), g);

        card.add(body, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private JPanel buildReferenceBox() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Theme.SLATE_100);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCBD5E1), 1),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel t = new JLabel("WHO BMI Reference Ranges");
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(Theme.TEXT_BODY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(t);
        box.add(Box.createRigidArea(new Dimension(0, 6)));

        box.add(refRow("Underweight",       "< 18.5",        Theme.CYAN));
        box.add(refRow("Normal weight",     "18.5 – 24.9",   Theme.GREEN));
        box.add(refRow("Overweight",        "25.0 – 29.9",   Theme.AMBER));
        box.add(refRow("Obese Class I–II",  "30.0 – 39.9",   Theme.ROSE));
        box.add(refRow("Extreme Obesity",   "≥ 40.0",        new Color(0x9F1239)));
        return box;
    }

    private JPanel refRow(String label, String range, Color col) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel tag = new JLabel("● " + label);
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(col);

        JLabel val = new JLabel(range);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        val.setForeground(Theme.TEXT_MUTED);

        row.add(tag, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private JComponent buildResultSide() {
        resultCard = new JPanel(new BorderLayout());
        resultCard.setBackground(Theme.CARD);
        resultCard.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));

        // Header bar
        resultHeader = new JPanel(new BorderLayout());
        resultHeader.setBackground(Theme.TEAL);
        resultHeader.setBorder(new EmptyBorder(6, 12, 6, 10));
        resultHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);

        JLabel logoIconLabel = new JLabel(Theme.getAppLogoIcon(20));
        JLabel titleLabel = new JLabel("Health Analysis & Recommendations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(Color.WHITE);

        miniSummaryLabel = new JLabel("  Awaiting Input  ");
        miniSummaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        miniSummaryLabel.setOpaque(true);
        miniSummaryLabel.setBackground(new Color(0x134E4A));
        miniSummaryLabel.setForeground(new Color(0xCCFBF1));
        miniSummaryLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x2DD4BF), 1),
                new EmptyBorder(2, 8, 2, 8)));
        miniSummaryLabel.setVisible(false);

        titlePanel.add(logoIconLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(miniSummaryLabel);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actionsPanel.setOpaque(false);

        minBtn = createHeaderActionButton(new WindowActionIcon(WindowActionIcon.Type.MINIMIZE, 12, Color.WHITE),
                "Minimize / Collapse Health Recommendations", e -> toggleMinimize());
        maxBtn = createHeaderActionButton(new WindowActionIcon(WindowActionIcon.Type.MAXIMIZE, 12, Color.WHITE),
                "Maximize to Full View", e -> toggleMaximize());
        popoutBtn = createHeaderActionButton(new WindowActionIcon(WindowActionIcon.Type.POPOUT, 12, Color.WHITE),
                "Open in Separate Resizable Window", e -> togglePopout());

        actionsPanel.add(minBtn);
        actionsPanel.add(maxBtn);
        actionsPanel.add(popoutBtn);

        resultHeader.add(titlePanel, BorderLayout.WEST);
        resultHeader.add(actionsPanel, BorderLayout.EAST);

        resultHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (currentViewState == ViewState.MAXIMIZED) {
                        setViewState(ViewState.NORMAL);
                    } else if (currentViewState == ViewState.NORMAL) {
                        setViewState(ViewState.MAXIMIZED);
                    } else if (currentViewState == ViewState.MINIMIZED) {
                        setViewState(ViewState.NORMAL);
                    }
                }
            }
        });

        resultBody = new JPanel(new BorderLayout(0, 12));
        resultBody.setBackground(Color.WHITE);
        resultBody.setBorder(new EmptyBorder(14, 16, 16, 16));

        resultBody.add(buildScorePanel(), BorderLayout.NORTH);
        resultBody.add(buildAdviceDesktop(), BorderLayout.CENTER);

        popoutPlaceholder = new JPanel(new GridBagLayout());
        popoutPlaceholder.setBackground(Color.WHITE);
        popoutPlaceholder.setBorder(new EmptyBorder(30, 20, 30, 20));

        JPanel phBox = new JPanel();
        phBox.setOpaque(false);
        phBox.setLayout(new BoxLayout(phBox, BoxLayout.Y_AXIS));

        JLabel phTitle = new JLabel("Health Analysis & Recommendations is active in a separate window.");
        phTitle.setFont(Theme.H3);
        phTitle.setForeground(Theme.TEXT_DARK);
        phTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel phSub = new JLabel("You can drag, resize, maximize, or minimize the window independently.");
        phSub.setFont(Theme.BODY);
        phSub.setForeground(Theme.TEXT_MUTED);
        phSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton redockBtn = Theme.primaryButton("Dock Back to Main Tab", Theme.TEAL, new Color(0x0F766E));
        redockBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        redockBtn.addActionListener(e -> dockBackFromPopout());

        phBox.add(phTitle);
        phBox.add(Box.createRigidArea(new Dimension(0, 8)));
        phBox.add(phSub);
        phBox.add(Box.createRigidArea(new Dimension(0, 16)));
        phBox.add(redockBtn);

        popoutPlaceholder.add(phBox);
        popoutPlaceholder.setVisible(false);

        resultCard.add(resultHeader, BorderLayout.NORTH);
        resultCard.add(resultBody, BorderLayout.CENTER);

        return resultCard;
    }

    private JPanel buildScorePanel() {
        JPanel scorePanel = new JPanel(new BorderLayout(15, 0));
        scorePanel.setBackground(new Color(0xF8FAFC));
        scorePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, Theme.TEAL),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER, 1),
                        new EmptyBorder(12, 14, 12, 14))));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CALCULATED BMI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 10));
        title.setForeground(Theme.TEXT_MUTED);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiValueLabel = new JLabel("--.-");
        bmiValueLabel.setFont(Theme.BIG_NUMBER);
        bmiValueLabel.setForeground(Theme.TEXT_DARK);
        bmiValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        categoryBadge = new JLabel("Awaiting Input");
        categoryBadge.setOpaque(true);
        categoryBadge.setBackground(new Color(0xE2E8F0));
        categoryBadge.setForeground(Theme.TEXT_MUTED);
        categoryBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        categoryBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
        categoryBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(title);
        left.add(bmiValueLabel);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(categoryBadge);

        scorePanel.add(left, BorderLayout.WEST);

        JPanel gaugePanel = new JPanel();
        gaugePanel.setOpaque(false);
        gaugePanel.setLayout(new BoxLayout(gaugePanel, BoxLayout.Y_AXIS));
        gaugePanel.setBorder(new EmptyBorder(14, 0, 0, 0));

        JLabel gaugeLbl = new JLabel("Scale Indicator (10 – 45)");
        gaugeLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        gaugeLbl.setForeground(Theme.TEXT_MUTED);
        gaugeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiGauge = new JProgressBar(10, 45);
        bmiGauge.setValue(22);
        bmiGauge.setStringPainted(true);
        bmiGauge.setString("Normal Range");
        bmiGauge.setPreferredSize(new Dimension(190, 24));
        bmiGauge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        bmiGauge.setForeground(Theme.GREEN);
        bmiGauge.setBackground(new Color(0xE2E8F0));
        bmiGauge.setFont(new Font("Segoe UI", Font.BOLD, 11));

        gaugePanel.add(gaugeLbl);
        gaugePanel.add(Box.createRigidArea(new Dimension(0, 6)));
        gaugePanel.add(bmiGauge);
        gaugePanel.add(Box.createVerticalGlue());

        scorePanel.add(gaugePanel, BorderLayout.CENTER);
        return scorePanel;
    }

    private JComponent buildAdviceDesktop() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        suggestionsArea = Theme.displayArea();
        dietPlanArea    = Theme.displayArea();
        healthTipsArea  = Theme.displayArea();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.setBackground(Color.WHITE);
        
        tabs.addTab("Health Suggestions", createScrollableArea(suggestionsArea));
        tabs.addTab("Custom Diet Plan", createScrollableArea(dietPlanArea));
        tabs.addTab("Gender Tips & Wellness", createScrollableArea(healthTipsArea));

        wrap.add(tabs, BorderLayout.CENTER);
        return wrap;
    }

    private JScrollPane createScrollableArea(JTextArea area) {
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    static class WindowActionIcon implements Icon {
        enum Type { MINIMIZE, MAXIMIZE, RESTORE, EXPAND, POPOUT, DOCK }
        private final Type type;
        private final int size;
        private final Color color;

        WindowActionIcon(Type type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }

        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            int s = size;

            switch (type) {
                case MINIMIZE:
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 2, y + s - 3, x + s - 2, y + s - 3);
                    break;
                case MAXIMIZE:
                    g2.setStroke(new BasicStroke(1.8f));
                    g2.drawRect(x + 2, y + 2, s - 4, s - 4);
                    break;
                case RESTORE:
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRect(x + 4, y + 1, s - 6, s - 6);
                    g2.setColor(c != null ? c.getBackground() : new Color(0x0F766E));
                    g2.fillRect(x + 1, y + 4, s - 5, s - 5);
                    g2.setColor(color);
                    g2.drawRect(x + 1, y + 4, s - 5, s - 5);
                    break;
                case EXPAND:
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 2, y + s/2, x + s - 2, y + s/2);
                    g2.drawLine(x + s/2, y + 2, x + s/2, y + s - 2);
                    break;
                case POPOUT:
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRect(x + 1, y + 4, s - 5, s - 5);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + s/2, y + 1, x + s - 1, y + 1);
                    g2.drawLine(x + s - 1, y + 1, x + s - 1, y + s/2);
                    g2.drawLine(x + 4, y + s - 4, x + s - 1, y + 1);
                    break;
                case DOCK:
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRect(x + 1, y + 1, s - 5, s - 5);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 3, y + s - 3, x + s - 3, y + 3);
                    break;
            }
            g2.dispose();
        }
    }

    private JButton createHeaderActionButton(Icon icon, String tooltip, ActionListener listener) {
        JButton btn = new JButton(icon);
        btn.setBackground(new Color(0x0F766E));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(28, 24));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0x115E59)); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(new Color(0x0F766E)); }
        });
        btn.addActionListener(listener);
        return btn;
    }

    private void toggleMinimize() {
        if (currentViewState == ViewState.MINIMIZED) {
            setViewState(ViewState.NORMAL);
        } else {
            setViewState(ViewState.MINIMIZED);
        }
    }

    private void toggleMaximize() {
        if (currentViewState == ViewState.MAXIMIZED) {
            setViewState(ViewState.NORMAL);
        } else {
            setViewState(ViewState.MAXIMIZED);
        }
    }

    void setViewState(ViewState state) {
        if (popoutDialog != null && popoutDialog.isVisible()) {
            dockBackFromPopout();
        }
        currentViewState = state;
        mainContentPanel.removeAll();

        switch (state) {
            case NORMAL:
                mainContentPanel.setLayout(new GridLayout(1, 2, 16, 0));
                mainContentPanel.add(inputSide);
                mainContentPanel.add(resultCard);
                resultBody.setVisible(true);
                miniSummaryLabel.setVisible(false);
                minBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.MINIMIZE, 12, Color.WHITE));
                minBtn.setToolTipText("Minimize / Collapse Health Recommendations");
                maxBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.MAXIMIZE, 12, Color.WHITE));
                maxBtn.setToolTipText("Maximize to Full View");
                break;

            case MAXIMIZED:
                mainContentPanel.setLayout(new BorderLayout());
                mainContentPanel.add(resultCard, BorderLayout.CENTER);
                resultBody.setVisible(true);
                miniSummaryLabel.setVisible(false);
                minBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.MINIMIZE, 12, Color.WHITE));
                minBtn.setToolTipText("Minimize / Collapse Health Recommendations");
                maxBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.RESTORE, 12, Color.WHITE));
                maxBtn.setToolTipText("Restore Normal Split View");
                break;

            case MINIMIZED:
                mainContentPanel.setLayout(new BorderLayout(0, 12));
                mainContentPanel.add(inputSide, BorderLayout.CENTER);
                mainContentPanel.add(resultCard, BorderLayout.SOUTH);
                resultBody.setVisible(false);
                miniSummaryLabel.setVisible(true);
                minBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.EXPAND, 12, Color.WHITE));
                minBtn.setToolTipText("Expand / Restore Health Recommendations");
                maxBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.MAXIMIZE, 12, Color.WHITE));
                maxBtn.setToolTipText("Maximize to Full View");
                break;
        }

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    void togglePopout() {
        if (popoutDialog != null && popoutDialog.isVisible()) {
            dockBackFromPopout();
            return;
        }
        openPopoutDialog();
    }

    void openPopoutDialog() {
        if (currentViewState == ViewState.MINIMIZED) {
            setViewState(ViewState.NORMAL);
        }
        Window parent = SwingUtilities.getWindowAncestor(this);
        popoutDialog = new JDialog(parent instanceof Frame ? (Frame) parent : null,
                "Health Analysis & Recommendations — " + user.getFullName(), false);
        Theme.applyWindowIcons(popoutDialog);
        popoutDialog.setSize(820, 620);
        popoutDialog.setMinimumSize(new Dimension(540, 420));
        popoutDialog.setLocationRelativeTo(this);
        popoutDialog.setLayout(new BorderLayout());

        resultCard.remove(resultBody);
        resultCard.add(popoutPlaceholder, BorderLayout.CENTER);
        popoutPlaceholder.setVisible(true);

        popoutDialog.add(resultBody, BorderLayout.CENTER);
        popoutBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.DOCK, 12, Color.WHITE));
        popoutBtn.setToolTipText("Dock Back into Main Tab");

        popoutDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dockBackFromPopout();
            }
        });

        resultCard.revalidate();
        resultCard.repaint();
        popoutDialog.setVisible(true);
    }

    void dockBackFromPopout() {
        if (popoutDialog != null) {
            popoutDialog.remove(resultBody);
            popoutDialog.dispose();
            popoutDialog = null;
        }
        resultCard.remove(popoutPlaceholder);
        popoutPlaceholder.setVisible(false);
        resultCard.add(resultBody, BorderLayout.CENTER);
        resultBody.setVisible(currentViewState != ViewState.MINIMIZED);
        popoutBtn.setIcon(new WindowActionIcon(WindowActionIcon.Type.POPOUT, 12, Color.WHITE));
        popoutBtn.setToolTipText("Open in Separate Resizable Window");
        resultCard.revalidate();
        resultCard.repaint();
    }

    private void performCalculation(boolean save) {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = user.getFullName();

            int    age    = Integer.parseInt(ageField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());

            if (height <= 0 || weight <= 0 || age <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Please enter positive values for Age, Height and Weight.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isMetric = unitCombo.getSelectedIndex() == 0;
            boolean isMale   = maleRadio.isSelected();

            double heightCm, weightKg, bmi;

            if (isMetric) {
                heightCm = height;
                weightKg = weight;
                double m = heightCm / 100.0;
                bmi = weightKg / (m * m);
            } else {
                heightCm = height * 2.54;
                weightKg = weight * 0.45359237;
                bmi = (weight / (height * height)) * 703.0;
            }

            updateResultsUI(name, age, isMale, bmi);

            if (save) {
                if (currentViewState == ViewState.MINIMIZED) {
                    setViewState(ViewState.NORMAL);
                }
                BMIRecord rec = new BMIRecord(user.getUsername(), age,
                        isMale ? "Male" : "Female", heightCm, weightKg, bmi, categoryOf(bmi));
                DataStore.get().addRecord(rec);
                if (onDataChanged != null) onDataChanged.run();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please verify that Age, Height and Weight are valid numbers.",
                    "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String categoryOf(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal Weight";
        if (bmi < 30.0) return "Overweight";
        if (bmi < 35.0) return "Obesity (Class I)";
        return "Severe Obesity (Class II/III)";
    }

    private void updateResultsUI(String name, int age, boolean isMale, double bmi) {
        bmiValueLabel.setText(DF.format(bmi));

        String category;
        Color badgeBg;

        if (bmi < 18.5) {
            category = "Underweight";
            badgeBg  = Theme.CYAN;
        } else if (bmi < 25.0) {
            category = "Normal Weight";
            badgeBg  = Theme.GREEN;
        } else if (bmi < 30.0) {
            category = "Overweight";
            badgeBg  = Theme.AMBER;
        } else if (bmi < 35.0) {
            category = "Obesity (Class I)";
            badgeBg  = Theme.ROSE;
        } else {
            category = "Severe Obesity (Class II/III)";
            badgeBg  = new Color(0x9F1239);
        }

        categoryBadge.setText(category);
        categoryBadge.setBackground(badgeBg);
        categoryBadge.setForeground(Color.WHITE);
        bmiGauge.setForeground(badgeBg);

        if (miniSummaryLabel != null) {
            miniSummaryLabel.setText("  BMI: " + DF.format(bmi) + " · " + category + "  ");
            miniSummaryLabel.setBackground(badgeBg);
            miniSummaryLabel.setForeground(Color.WHITE);
        }

        int progress = (int) Math.round(bmi);
        progress = Math.max(10, Math.min(45, progress));
        bmiGauge.setValue(progress);
        bmiGauge.setString(category + " (" + DF.format(bmi) + ")");

        suggestionsArea.setText(generateHealthSuggestions(name, age, bmi, category));
        dietPlanArea.setText(generateDietPlan(category, isMale));
        healthTipsArea.setText(generateGenderTips(name, age, isMale, bmi));

        suggestionsArea.setCaretPosition(0);
        dietPlanArea.setCaretPosition(0);
        healthTipsArea.setCaretPosition(0);
    }

    private void resetForm() {
        nameField.setText(user.getFullName());
        ageField.setText(String.valueOf(user.getAge()));
        heightField.setText("175");
        weightField.setText("72");
        unitCombo.setSelectedIndex(0);
        if ("Female".equalsIgnoreCase(user.getGender())) femaleRadio.setSelected(true);
        else maleRadio.setSelected(true);

        bmiValueLabel.setText("--.-");
        categoryBadge.setText("Reset");
        categoryBadge.setBackground(new Color(0xE2E8F0));
        categoryBadge.setForeground(Theme.TEXT_MUTED);
        bmiGauge.setValue(22);
        bmiGauge.setForeground(Theme.GREEN);
        bmiGauge.setString("Waiting for input");

        if (miniSummaryLabel != null) {
            miniSummaryLabel.setText("  Awaiting Input  ");
            miniSummaryLabel.setBackground(new Color(0x134E4A));
            miniSummaryLabel.setForeground(new Color(0xCCFBF1));
        }

        suggestionsArea.setText("");
        dietPlanArea.setText("");
        healthTipsArea.setText("");
    }

    private String generateHealthSuggestions(String name, int age, double bmi, String category) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ").append(name).append(", here is your clinical breakdown:\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append("Current classification : ").append(category)
                .append("   (BMI ").append(DF.format(bmi)).append(")\n");
        sb.append("Target healthy BMI     : 18.5 – 24.9\n\n");

        switch (category) {
            case "Underweight":
                sb.append("• Risk assessment: compromised immune response, nutrient deficiencies\n")
                        .append("  (iron, vitamin D, calcium) and lower bone mineral density.\n");
                sb.append("• Activity: progressive resistance training 3–4× weekly rather than\n")
                        .append("  extensive high-intensity cardio.\n");
                sb.append("• Caloric strategy: controlled surplus of +300 to +500 kcal/day using\n")
                        .append("  nutrient-dense whole foods.\n");
                sb.append("• Lifestyle: prioritise 7–8 hours of sleep to support muscle synthesis.\n");
                break;

            case "Normal Weight":
                sb.append("• Status: excellent. Your BMI sits in the range associated with\n")
                        .append("  optimal cardiovascular and metabolic vitality.\n");
                sb.append("• Maintenance: keep nutrition balanced; monitor waist-to-height ratio\n")
                        .append("  to avoid 'normal weight obesity'.\n");
                sb.append("• Activity: 150–300 min moderate aerobic exercise plus 2 strength\n")
                        .append("  sessions per week.\n");
                sb.append("• Check-ups: track resting blood pressure, fasting lipids and glucose\n")
                        .append("  annually.\n");
                break;

            case "Overweight":
                sb.append("• Risk assessment: elevated predisposition to high blood pressure,\n")
                        .append("  insulin resistance and joint stress.\n");
                sb.append("• Active goal: a gradual 5%–7% reduction in total body weight can\n")
                        .append("  dramatically improve metabolic biomarkers.\n");
                sb.append("• Exercise: 150–200 min moderate cardio (brisk walking, cycling,\n")
                        .append("  swimming) plus 2–3 resistance sessions weekly.\n");
                sb.append("• Habits: limit liquid sugars, alcohol and refined carbohydrates;\n")
                        .append("  replace late-night snacking with herbal tea or water.\n");
                break;

            default:
                sb.append("• Clinical attention: we strongly suggest partnering with a primary\n")
                        .append("  care physician and a registered dietitian.\n");
                sb.append("• Cardiovascular protection: regular non-impact movement (pool\n")
                        .append("  walking, stationary cycling) relieves joint tension.\n");
                sb.append("• Blood sugar & lipids: schedule HbA1c, lipid profile and liver enzymes.\n");
                sb.append("• Sustainable goal: steady loss of 0.5–1 kg (1–2 lbs) per week.\n")
                        .append("  Avoid extreme crash diets that deplete lean mass.\n");
                break;
        }

        sb.append("\nNote: BMI is a useful screening metric but does not differentiate\n")
                .append("between muscle mass, bone density and adipose tissue.");
        return sb.toString();
    }

    private String generateDietPlan(String category, boolean isMale) {
        StringBuilder sb = new StringBuilder();
        sb.append("CUSTOMISED NUTRITION TEMPLATE — ").append(category.toUpperCase()).append("\n");
        sb.append("Tailored for: ")
                .append(isMale ? "Male metabolism & caloric baseline"
                        : "Female metabolism & hormonal balance")
                .append("\n");
        sb.append("======================================================================\n\n");

        if (category.equals("Underweight")) {
            sb.append("Goal: healthy, nutrient-rich weight gain & lean mass accretion\n\n");
            sb.append("1. Breakfast\n   • Rolled oats with whole milk, chia seeds, banana, honey,\n     almond butter.\n   • 2–3 scrambled eggs with avocado and whole-grain toast.\n\n");
            sb.append("2. Mid-morning\n   • Greek yoghurt with walnuts, pumpkin seeds, dried cranberries.\n\n");
            sb.append("3. Lunch\n   • Grilled chicken or tofu bowl with quinoa, roasted sweet potato,\n     olive-oil dressing.\n   • Fresh vegetable salad with hummus.\n\n");
            sb.append("4. Afternoon boost\n   • Smoothie: peanut butter, banana, oats, protein, milk.\n\n");
            sb.append("5. Dinner\n   • Baked salmon or paneer/lentil curry with brown basmati rice,\n     steamed broccoli, olive oil.\n\n");
            sb.append("6. Hydration\n   • Avoid large glasses of water immediately before meals.\n");
        } else if (category.equals("Normal Weight")) {
            sb.append("Goal: metabolic optimisation, cellular repair, sustained energy\n\n");
            sb.append("1. Breakfast\n   • Overnight oats or a spinach & mushroom omelette with sourdough.\n   • One seasonal fruit (apple, berries or orange).\n\n");
            sb.append("2. Mid-morning\n   • Small handful of mixed nuts and green tea.\n\n");
            sb.append("3. Lunch\n   • Mediterranean grain bowl: brown rice, greens, chickpeas/tuna/\n     chicken, cucumber, lemon-tahini dressing.\n\n");
            sb.append("4. Evening snack\n   • Carrot and bell-pepper sticks with hummus or cottage cheese.\n\n");
            sb.append("5. Dinner\n   • Stir-fried vegetables with lean turkey, shrimp or edamame over\n     cauliflower rice or soba noodles.\n\n");
            sb.append("6. Hydration\n   • 2.5 – 3.5 litres of clean water daily.\n");
        } else {
            sb.append("Goal: satiety enhancement, insulin stabilisation, sustainable fat loss\n\n");
            sb.append("1. Breakfast\n   • 3 egg whites + 1 whole egg omelette with spinach, peppers,\n     tomatoes.\n   • Green tea or black coffee (no sugar or syrup).\n\n");
            sb.append("2. Mid-morning\n   • One green apple or cucumber slices with a pinch of Himalayan salt.\n\n");
            sb.append("3. Lunch\n   • Half a plate of raw/steamed leafy greens and cruciferous veg.\n   • One palm-sized portion of lean protein.\n   • ½ cup complex carbs (quinoa, sweet potato or brown lentils).\n\n");
            sb.append("4. Evening snack\n   • Roasted chickpeas or 15–20 raw almonds with chamomile tea.\n\n");
            sb.append("5. Dinner\n   • Vegetable & lentil soup or grilled cod/tofu with asparagus and\n     sautéed zucchini.\n   • Finish dinner 2.5 – 3 hours before bed.\n\n");
            sb.append("6. Golden rules\n   • Avoid processed foods, sweetened drinks and commercial sauces.\n   • Drink 500 ml water 20 minutes before each meal.\n");
        }
        return sb.toString();
    }

    private String generateGenderTips(String name, int age, boolean isMale, double bmi) {
        StringBuilder sb = new StringBuilder();

        if (isMale) {
            sb.append("MALE-SPECIFIC HEALTH & PHYSIOLOGY ADVISORY\n");
            sb.append("======================================================================\n\n");
            sb.append("• Visceral fat awareness\n  Men are predisposed to abdominal visceral fat (android distribution),\n  which directly impacts heart health and liver lipid storage. Keep waist\n  circumference below 94–102 cm (37–40 in).\n\n");
            sb.append("• Testosterone & metabolic health\n  Healthy testosterone depends on lean muscle percentage, dietary zinc,\n  magnesium and restorative deep sleep. High body fat triggers aromatase\n  conversion of testosterone into estrogens.\n\n");
            sb.append("• Cardiovascular vigilance\n  Cardiovascular risk escalates earlier in men (often around 35–40).\n  Prioritise aerobic endurance conditioning and regular BP checks.\n\n");
            sb.append("• Targeted nutrition\n  – 1.2–1.8 g protein per kg of ideal body weight.\n  – Cruciferous vegetables (broccoli, Brussels sprouts) for hormone clearance.\n\n");
            sb.append(bmi >= 25.0
                    ? "• Recommendation: add HIIT 1–2× weekly alongside compound resistance\n  lifts (squat, deadlift, press).\n"
                    : "• Recommendation: focus on progressive strength overload and functional\n  mobility.\n");
        } else {
            sb.append("FEMALE-SPECIFIC HEALTH & PHYSIOLOGY ADVISORY\n");
            sb.append("======================================================================\n\n");
            sb.append("• Bone density & calcium / vitamin D\n  Women experience higher lifetime rates of osteopenia and osteoporosis.\n  Weight-bearing activity stimulates osteoblast activity. Aim for\n  1,000–1,200 mg calcium daily.\n\n");
            sb.append("• Iron & micronutrient balance\n  Premenopausal women are at heightened risk of iron-deficiency anaemia.\n  Pair iron-rich foods with vitamin C to boost absorption.\n\n");
            sb.append("• Hormonal rhythm & fat distribution\n  Estrogen influences gynoid fat distribution (hips, thighs). Avoid severe\n  calorie restriction (<1200 kcal/day) — it can impair thyroid function and\n  disrupt menstrual regularity.\n\n");
            sb.append("• Targeted wellness\n  – Mind-body practice (Pilates, yoga) to regulate cortisol.\n  – Omega-3 fatty acids (flaxseed, chia, fatty fish) for cellular health.\n\n");
            sb.append(bmi >= 25.0
                    ? "• Recommendation: prioritise low-stress steady-state cardio and resistance\n  bands to promote fat loss without spiking cortisol.\n"
                    : "• Recommendation: maintain balanced strength conditioning to protect\n  posture, pelvic stability and joint longevity.\n");
        }

        sb.append("\nAge note (").append(age).append(" yrs): ");
        if (age < 30) {
            sb.append("peak window for building baseline bone density and\nfoundational muscle mass. Maximise these habits now.");
        } else if (age < 50) {
            sb.append("metabolism naturally slows 2–4% per decade without\nresistance training. Emphasise 8,000–10,000 daily steps and lean protein.");
        } else {
            sb.append("sarcopenia prevention, joint flexibility and cardiovascular\nelasticity are crucial. Prioritise balance work and daily walks.");
        }
        return sb.toString();
    }
}

/* =====================================================================
 * Tab 2 — Tracking
 * ===================================================================== */
class TrackingPanel extends JPanel {

    private static final DecimalFormat DF = new DecimalFormat("0.0");

    private final User user;
    private final RecordTableModel model = new RecordTableModel();
    private final JTable table = new JTable(model);
    private final List<BMIRecord> current = new ArrayList<>();

    private final JLabel countLbl    = new JLabel("0");
    private final JLabel latestLbl   = new JLabel("--.-");
    private final JLabel averageLbl  = new JLabel("--.-");
    private final JLabel bestLbl     = new JLabel("--.-");

    TrackingPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(0, 14));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(16, 18, 16, 18));

        add(buildSummaryStrip(), BorderLayout.NORTH);
        add(buildTableCard(),    BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildSummaryStrip() {
        JPanel strip = new JPanel(new GridLayout(1, 4, 12, 0));
        strip.setOpaque(false);

        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        latestLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        averageLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        bestLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        strip.add(Theme.statCard("Total Records", Theme.GREEN,  countLbl));
        strip.add(Theme.statCard("Latest BMI",    Theme.BLUE,   latestLbl));
        strip.add(Theme.statCard("Average BMI",   Theme.PURPLE, averageLbl));
        strip.add(Theme.statCard("Best BMI",      Theme.TEAL,   bestLbl));
        return strip;
    }

    private JPanel buildTableCard() {
        JPanel card = Theme.sectionCard("  Measurement History", Theme.GREEN);

        JPanel body = new JPanel(new BorderLayout(0, 10));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(12, 14, 14, 14));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);

        JButton refreshBtn = Theme.outlineButton("Refresh", Theme.GREEN);
        refreshBtn.addActionListener(e -> refresh());

        JButton deleteBtn = Theme.outlineButton("Delete Selected", Theme.ROSE);
        deleteBtn.addActionListener(e -> deleteSelected());

        JButton clearBtn = Theme.outlineButton("Clear All", Theme.ROSE);
        clearBtn.addActionListener(e -> clearAll());

        toolbar.add(refreshBtn);
        toolbar.add(deleteBtn);
        toolbar.add(clearBtn);

        table.setFont(Theme.BODY);
        table.setRowHeight(28);
        table.setGridColor(Theme.BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(Theme.GREEN_SOFT);
        table.setSelectionForeground(Theme.TEXT_DARK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(0xF0FDF4));
        header.setForeground(Theme.GREEN_DARK);
        header.setPreferredSize(new Dimension(0, 32));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.GREEN));

        table.getColumnModel().getColumn(7).setCellRenderer(new CategoryRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));

        body.add(toolbar, BorderLayout.NORTH);
        body.add(sp, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private static class CategoryRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                                                       boolean focus, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, value, sel, focus, row, col);
            String v = value == null ? "" : value.toString();
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            l.setOpaque(true);
            l.setForeground(Color.WHITE);
            l.setBackground(colorFor(v));
            return l;
        }

        private Color colorFor(String v) {
            switch (v) {
                case "Underweight":              return Theme.CYAN;
                case "Normal Weight":            return Theme.GREEN;
                case "Overweight":               return Theme.AMBER;
                case "Obesity (Class I)":        return Theme.ROSE;
                case "Severe Obesity (Class II/III)": return new Color(0x9F1239);
                default:                         return Theme.TEXT_MUTED;
            }
        }
    }

    void refresh() {
        current.clear();
        current.addAll(DataStore.get().recordsFor(user.getUsername()));
        Collections.reverse(current);
        model.setData(current);

        countLbl.setText(String.valueOf(current.size()));

        if (current.isEmpty()) {
            latestLbl.setText("--.-");
            averageLbl.setText("--.-");
            bestLbl.setText("--.-");
            return;
        }

        double sum = 0, best = Double.MAX_VALUE;
        for (BMIRecord r : current) {
            sum += r.getBmi();
            if (Math.abs(r.getBmi() - 22.0) < Math.abs(best - 22.0)) best = r.getBmi();
        }
        latestLbl.setText(DF.format(current.get(0).getBmi()));
        averageLbl.setText(DF.format(sum / current.size()));
        bestLbl.setText(DF.format(best));
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row first.",
                    "Nothing Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        BMIRecord rec = current.get(row);
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete the record from " + rec.getFormattedTime() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            DataStore.get().deleteRecord(rec);
            refresh();
        }
    }

    private void clearAll() {
        if (current.isEmpty()) return;
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete ALL " + current.size() + " records for " + user.getUsername() + "?\n"
                        + "This cannot be undone.",
                "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            DataStore.get().clearRecords(user.getUsername());
            refresh();
        }
    }

    private static class RecordTableModel extends AbstractTableModel {

        private final String[] COLS = {
                "#", "Date & Time", "Age", "Gender",
                "Height (cm)", "Weight (kg)", "BMI", "Category"
        };
        private List<BMIRecord> data = new ArrayList<>();

        void setData(List<BMIRecord> d) {
            data = d;
            fireTableDataChanged();
        }

        @Override public int getRowCount()    { return data.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }
        @Override public boolean isCellEditable(int r, int c) { return false; }

        @Override
        public Object getValueAt(int r, int c) {
            BMIRecord rec = data.get(r);
            int chronologicalIndex = data.size() - r;
            switch (c) {
                case 0: return chronologicalIndex;
                case 1: return rec.getFormattedTime();
                case 2: return rec.getAge();
                case 3: return rec.getGender();
                case 4: return DF.format(rec.getHeightCm());
                case 5: return DF.format(rec.getWeightKg());
                case 6: return DF.format(rec.getBmi());
                case 7: return rec.getCategory();
                default: return "";
            }
        }
    }
}

/* =====================================================================
 * Tab 3 — Reports
 * ===================================================================== */
class ReportsPanel extends JPanel {

    private static final DecimalFormat DF = new DecimalFormat("0.0");

    private final User user;

    private final JLabel totalLbl   = new JLabel("0");
    private final JLabel latestLbl  = new JLabel("--.-");
    private final JLabel averageLbl = new JLabel("--.-");
    private final JLabel minLbl     = new JLabel("--.-");
    private final JLabel maxLbl     = new JLabel("--.-");
    private final JLabel trendLbl   = new JLabel("—");

    private final BMIChart chart = new BMIChart();
    private final JPanel distributionBox = new JPanel();

    ReportsPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(0, 14));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(16, 18, 16, 18));

        add(buildStatStrip(), BorderLayout.NORTH);
        add(buildBody(),      BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildStatStrip() {
        JPanel strip = new JPanel(new GridLayout(1, 6, 10, 0));
        strip.setOpaque(false);

        strip.add(Theme.statCard("Records",  Theme.PURPLE, totalLbl));
        strip.add(Theme.statCard("Latest",   Theme.BLUE,   latestLbl));
        strip.add(Theme.statCard("Average",  Theme.TEAL,   averageLbl));
        strip.add(Theme.statCard("Lowest",   Theme.CYAN,   minLbl));
        strip.add(Theme.statCard("Highest",  Theme.ROSE,   maxLbl));
        strip.add(Theme.statCard("Trend",    Theme.ORANGE, trendLbl));
        return strip;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(1, 2, 14, 0));
        body.setOpaque(false);

        JPanel chartCard = Theme.sectionCard("  BMI Trend Over Time", Theme.PURPLE);
        JPanel chartWrap = new JPanel(new BorderLayout());
        chartWrap.setBackground(Color.WHITE);
        chartWrap.setBorder(new EmptyBorder(12, 14, 14, 14));
        chartWrap.add(chart, BorderLayout.CENTER);
        chartCard.add(chartWrap, BorderLayout.CENTER);

        JPanel distCard = Theme.sectionCard("  Category Distribution", Theme.TEAL);
        distributionBox.setLayout(new BoxLayout(distributionBox, BoxLayout.Y_AXIS));
        distributionBox.setBackground(Color.WHITE);

        JScrollPane distScroll = new JScrollPane(distributionBox);
        distScroll.setBorder(new EmptyBorder(12, 14, 14, 14));
        distScroll.getViewport().setBackground(Color.WHITE);

        distCard.add(distScroll, BorderLayout.CENTER);

        body.add(chartCard);
        body.add(distCard);
        return body;
    }

    void refresh() {
        List<BMIRecord> data = DataStore.get().recordsFor(user.getUsername());
        chart.setData(data);

        totalLbl.setText(String.valueOf(data.size()));

        if (data.isEmpty()) {
            latestLbl.setText("--.-");
            averageLbl.setText("--.-");
            minLbl.setText("--.-");
            maxLbl.setText("--.-");
            trendLbl.setText("—");
            trendLbl.setForeground(Theme.TEXT_MUTED);
            buildDistribution(data);
            return;
        }

        double sum = 0, min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (BMIRecord r : data) {
            sum += r.getBmi();
            min = Math.min(min, r.getBmi());
            max = Math.max(max, r.getBmi());
        }
        double avg = sum / data.size();
        double latest = data.get(data.size() - 1).getBmi();

        latestLbl.setText(DF.format(latest));
        averageLbl.setText(DF.format(avg));
        minLbl.setText(DF.format(min));
        maxLbl.setText(DF.format(max));

        if (data.size() >= 2) {
            double prev = data.get(data.size() - 2).getBmi();
            double delta = latest - prev;
            if (Math.abs(delta) < 0.05) {
                trendLbl.setText("Stable");
                trendLbl.setForeground(Theme.TEXT_MUTED);
            } else if (delta < 0) {
                trendLbl.setText("▼ " + DF.format(Math.abs(delta)));
                trendLbl.setForeground(Theme.GREEN);
            } else {
                trendLbl.setText("▲ " + DF.format(delta));
                trendLbl.setForeground(Theme.ROSE);
            }
        } else {
            trendLbl.setText("—");
            trendLbl.setForeground(Theme.TEXT_MUTED);
        }

        buildDistribution(data);
    }

    private void buildDistribution(List<BMIRecord> data) {
        distributionBox.removeAll();

        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("Underweight", 0);
        counts.put("Normal Weight", 0);
        counts.put("Overweight", 0);
        counts.put("Obesity (Class I)", 0);
        counts.put("Severe Obesity (Class II/III)", 0);

        for (BMIRecord r : data) {
            counts.merge(r.getCategory(), 1, Integer::sum);
        }

        int total = Math.max(1, data.size());

        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            distributionBox.add(buildDistRow(e.getKey(), e.getValue(), total));
            distributionBox.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        if (data.isEmpty()) {
            JLabel empty = new JLabel("No records yet.");
            empty.setFont(Theme.BODY);
            empty.setForeground(Theme.TEXT_MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            distributionBox.add(empty);
        }

        distributionBox.revalidate();
        distributionBox.repaint();
    }

    private JPanel buildDistRow(String label, int count, int total) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        Color col = colorFor(label);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel name = new JLabel(label);
        name.setFont(new Font("Segoe UI", Font.BOLD, 11));
        name.setForeground(col);

        JLabel num = new JLabel(count + "  (" + (count * 100 / total) + "%)");
        num.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        num.setForeground(Theme.TEXT_MUTED);

        top.add(name, BorderLayout.WEST);
        top.add(num, BorderLayout.EAST);

        JProgressBar bar = new JProgressBar(0, total);
        bar.setValue(count);
        bar.setForeground(col);
        bar.setBackground(new Color(0xEDF2F7));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(100, 10));

        row.add(top, BorderLayout.NORTH);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    private static Color colorFor(String cat) {
        switch (cat) {
            case "Underweight":   return Theme.CYAN;
            case "Normal Weight": return Theme.GREEN;
            case "Overweight":    return Theme.AMBER;
            case "Obesity (Class I)": return Theme.ROSE;
            default:              return new Color(0x9F1239);
        }
    }

    private static class BMIChart extends JPanel {

        private List<BMIRecord> data = new ArrayList<>();

        BMIChart() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(420, 240));
        }

        void setData(List<BMIRecord> d) {
            this.data = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int padL = 46, padR = 18, padT = 18, padB = 48;
            int cw = getWidth() - padL - padR;
            int ch = getHeight() - padT - padB;

            if (cw < 40 || ch < 40) { g2.dispose(); return; }

            if (data.isEmpty()) {
                g2.setColor(Theme.TEXT_MUTED);
                g2.setFont(Theme.BODY);
                String msg = "No records yet — calculate your BMI to start tracking.";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, Math.max(10, (getWidth() - fm.stringWidth(msg)) / 2),
                        getHeight() / 2);
                g2.dispose();
                return;
            }

            double min = 16, max = 38;
            for (BMIRecord r : data) {
                min = Math.min(min, r.getBmi());
                max = Math.max(max, r.getBmi());
            }
            min = Math.floor(min - 2);
            max = Math.ceil(max + 2);
            if (max - min < 8) max = min + 8;

            final double fMin = min, fMax = max;

            int bandTop = yFor(24.9, fMin, fMax, padT, ch);
            int bandBot = yFor(18.5, fMin, fMax, padT, ch);
            g2.setColor(new Color(0xDC, 0xFC, 0xE7, 170));
            g2.fillRect(padL, bandTop, cw, bandBot - bandTop);
            g2.setColor(new Color(0x16, 0xA3, 0x4A, 90));
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    1f, new float[]{4f, 4f}, 0f));
            g2.drawLine(padL, bandTop, padL + cw, bandTop);
            g2.drawLine(padL, bandBot, padL + cw, bandBot);
            g2.setStroke(new BasicStroke(1f));

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            int steps = 5;
            for (int i = 0; i <= steps; i++) {
                double v = fMin + (fMax - fMin) * i / steps;
                int y = yFor(v, fMin, fMax, padT, ch);
                g2.setColor(new Color(0xE2, 0xE8, 0xF0));
                g2.drawLine(padL, y, padL + cw, y);
                g2.setColor(Theme.TEXT_MUTED);
                String s = String.format("%.0f", v);
                g2.drawString(s, padL - 8 - g2.getFontMetrics().stringWidth(s), y + 4);
            }

            g2.setColor(new Color(0x94, 0xA3, 0xB8));
            g2.drawLine(padL, padT, padL, padT + ch);
            g2.drawLine(padL, padT + ch, padL + cw, padT + ch);

            int n = data.size();
            int[] xs = new int[n];
            int[] ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = (n == 1) ? padL + cw / 2
                        : padL + (int) Math.round((double) i * cw / (n - 1));
                ys[i] = yFor(data.get(i).getBmi(), fMin, fMax, padT, ch);
            }

            if (n > 1) {
                int[] ax = new int[n + 2];
                int[] ay = new int[n + 2];
                System.arraycopy(xs, 0, ax, 0, n);
                System.arraycopy(ys, 0, ay, 0, n);
                ax[n] = xs[n - 1];
                ay[n] = padT + ch;
                ax[n + 1] = xs[0];
                ay[n + 1] = padT + ch;
                g2.setColor(new Color(0x7C, 0x3A, 0xED, 40));
                g2.fillPolygon(ax, ay, n + 2);
            }

            g2.setColor(Theme.PURPLE);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 1; i < n; i++) g2.drawLine(xs[i - 1], ys[i - 1], xs[i], ys[i]);

            for (int i = 0; i < n; i++) {
                g2.setColor(Color.WHITE);
                g2.fillOval(xs[i] - 5, ys[i] - 5, 10, 10);
                g2.setColor(Theme.PURPLE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(xs[i] - 5, ys[i] - 5, 10, 10);
            }

            g2.setStroke(new BasicStroke(1f));
            g2.setColor(Theme.TEXT_MUTED);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            int labelStep = Math.max(1, n / 6);
            for (int i = 0; i < n; i += labelStep) {
                String s = data.get(i).getShortTime();
                int sw = g2.getFontMetrics().stringWidth(s);
                g2.drawString(s, xs[i] - sw / 2, padT + ch + 16);
            }
            if ((n - 1) % labelStep != 0 && n > 1) {
                String s = data.get(n - 1).getShortTime();
                int sw = g2.getFontMetrics().stringWidth(s);
                g2.drawString(s, xs[n - 1] - sw / 2, padT + ch + 30);
            }

            g2.dispose();
        }

        private int yFor(double v, double min, double max, int padT, int ch) {
            double ratio = (v - min) / (max - min);
            return padT + ch - (int) Math.round(ratio * ch);
        }
    }
}

/* =====================================================================
 * Tab 4 — Comparison
 * ===================================================================== */
class ComparisonPanel extends JPanel {

    private static final DecimalFormat DF = new DecimalFormat("0.0");

    private final User user;

    private final JComboBox<BMIRecord> comboA = new JComboBox<>();
    private final JComboBox<BMIRecord> comboB = new JComboBox<>();

    private final JPanel resultBox = new JPanel();

    private List<BMIRecord> records = new ArrayList<>();
    private boolean suppressEvents = false;

    ComparisonPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(0, 14));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(16, 18, 16, 18));

        add(buildSelectorCard(), BorderLayout.NORTH);
        add(buildResultCard(),   BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildSelectorCard() {
        JPanel card = Theme.sectionCard("  Compare Two Reports", Theme.ORANGE);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(14, 16, 14, 16));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 8, 4, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        JLabel lA = Theme.fieldLabel("Report A (baseline)");
        lA.setForeground(Theme.BLUE);
        body.add(lA, g);

        g.gridx = 1; g.weightx = 1;
        comboA.setFont(Theme.BODY);
        comboA.setBackground(Color.WHITE);
        body.add(comboA, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        JLabel lB = Theme.fieldLabel("Report B (compare to)");
        lB.setForeground(Theme.PURPLE);
        body.add(lB, g);

        g.gridx = 1; g.weightx = 1;
        comboB.setFont(Theme.BODY);
        comboB.setBackground(Color.WHITE);
        body.add(comboB, g);

        g.gridx = 2; g.gridy = 0; g.gridheight = 2; g.weightx = 0;
        g.insets = new Insets(4, 16, 4, 4);
        JButton compare = Theme.primaryButton("Compare", Theme.ORANGE, Theme.ORANGE_DARK);
        compare.addActionListener(e -> renderComparison());
        body.add(compare, g);

        comboA.addActionListener(e -> { if (!suppressEvents) renderComparison(); });
        comboB.addActionListener(e -> { if (!suppressEvents) renderComparison(); });

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildResultCard() {
        JPanel card = Theme.sectionCard("  Comparison Result", Theme.ORANGE_DARK);

        resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.Y_AXIS));
        resultBox.setBackground(Color.WHITE);

        JScrollPane sp = new JScrollPane(resultBox);
        sp.setBorder(new EmptyBorder(14, 16, 16, 16));
        sp.getViewport().setBackground(Color.WHITE);

        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    void refresh() {
        records = DataStore.get().recordsFor(user.getUsername());

        suppressEvents = true;

        List<BMIRecord> descending = new ArrayList<>(records);
        Collections.reverse(descending);

        comboA.removeAllItems();
        comboB.removeAllItems();
        for (BMIRecord r : descending) {
            comboA.addItem(r);
            comboB.addItem(r);
        }

        if (descending.size() >= 2) {
            comboA.setSelectedIndex(1);
            comboB.setSelectedIndex(0);
        }

        suppressEvents = false;
        renderComparison();
    }

    private void renderComparison() {
        resultBox.removeAll();
        resultBox.setLayout(new GridBagLayout());

        BMIRecord a = (BMIRecord) comboA.getSelectedItem();
        BMIRecord b = (BMIRecord) comboB.getSelectedItem();

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 12, 8, 12);
        g.gridy = 0;
        
        if (a == null || b == null) {
            JLabel msg = new JLabel("Save at least two BMI records to unlock the comparison view.");
            msg.setFont(Theme.BODY);
            msg.setForeground(Theme.TEXT_MUTED);
            resultBox.add(msg, g);
            pushTop(resultBox, g);
            resultBox.revalidate();
            resultBox.repaint();
            return;
        }

        if (a == b) {
            JLabel msg = new JLabel("You selected the same report twice — pick two different dates.");
            msg.setFont(Theme.BODY);
            msg.setForeground(Theme.AMBER);
            resultBox.add(msg, g);
            pushTop(resultBox, g);
            resultBox.revalidate();
            resultBox.repaint();
            return;
        }

        // Header Row
        g.weightx = 0;
        g.gridx = 0; resultBox.add(headLabel("METRIC", Theme.TEXT_MUTED), g);
        g.weightx = 1.0;
        g.gridx = 1; resultBox.add(headLabel("REPORT A", Theme.BLUE), g);
        g.gridx = 2; resultBox.add(headLabel("REPORT B", Theme.PURPLE), g);
        g.gridx = 3; resultBox.add(headLabel("CHANGE", Theme.TEXT_MUTED), g);

        // Separator line
        g.gridy++;
        g.gridx = 0; g.gridwidth = 4;
        g.insets = new Insets(0, 12, 4, 12);
        JPanel sep = new JPanel();
        sep.setBackground(Theme.BORDER);
        sep.setPreferredSize(new Dimension(10, 1));
        resultBox.add(sep, g);

        // Metric Rows
        g.gridwidth = 1;
        g.insets = new Insets(6, 12, 6, 12);
        
        addMetricRow(resultBox, g, "Date & Time", a.getFormattedTime(), b.getFormattedTime(), null);
        addMetricRow(resultBox, g, "Age", String.valueOf(a.getAge()), String.valueOf(b.getAge()),
                deltaText(a.getAge(), b.getAge(), " yrs"));
        addMetricRow(resultBox, g, "Gender", a.getGender(), b.getGender(), null);
        addMetricRow(resultBox, g, "Height", DF.format(a.getHeightCm()) + " cm",
                DF.format(b.getHeightCm()) + " cm",
                deltaDouble(a.getHeightCm(), b.getHeightCm(), " cm"));
        addMetricRow(resultBox, g, "Weight", DF.format(a.getWeightKg()) + " kg",
                DF.format(b.getWeightKg()) + " kg",
                deltaDouble(a.getWeightKg(), b.getWeightKg(), " kg"));
        addMetricRow(resultBox, g, "BMI", DF.format(a.getBmi()), DF.format(b.getBmi()),
                deltaDouble(a.getBmi(), b.getBmi(), ""));
        addMetricRow(resultBox, g, "Category", a.getCategory(), b.getCategory(),
                verdict(a, b));

        // Banner
        g.gridy++;
        g.gridx = 0;
        g.gridwidth = 4;
        g.insets = new Insets(16, 12, 0, 12);
        resultBox.add(verdictBanner(a, b), g);

        pushTop(resultBox, g);
        
        resultBox.revalidate();
        resultBox.repaint();
    }

    private void pushTop(JPanel p, GridBagConstraints g) {
        g.gridy++;
        g.gridx = 0;
        g.gridwidth = GridBagConstraints.REMAINDER;
        g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        p.add(filler, g);
    }

    private JLabel headLabel(String t, Color c) {
        JLabel l = new JLabel(t, SwingConstants.LEFT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(c);
        return l;
    }

    private void addMetricRow(JPanel p, GridBagConstraints g, String metric, String valA, String valB, String delta) {
        g.gridy++;
        g.gridx = 0; g.weightx = 0;
        JLabel m = new JLabel(metric);
        m.setFont(new Font("Segoe UI", Font.BOLD, 12));
        m.setForeground(Theme.TEXT_BODY);
        p.add(m, g);

        g.gridx = 1; g.weightx = 1.0;
        JLabel la = new JLabel(valA);
        la.setFont(Theme.BODY);
        la.setForeground(Theme.BLUE_DARK);
        p.add(la, g);

        g.gridx = 2; g.weightx = 1.0;
        JLabel lb = new JLabel(valB);
        lb.setFont(Theme.BODY);
        lb.setForeground(Theme.PURPLE_DARK);
        p.add(lb, g);

        g.gridx = 3; g.weightx = 1.0;
        if (delta != null && !delta.isEmpty()) {
            JLabel d = new JLabel(delta);
            d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            d.setForeground(deltaColor(delta));
            p.add(d, g);
        } else {
            p.add(new JLabel(""), g);
        }
    }

    private JPanel verdictBanner(BMIRecord a, BMIRecord b) {
        double distA = Math.abs(a.getBmi() - 22.0);
        double distB = Math.abs(b.getBmi() - 22.0);
        double diff = b.getBmi() - a.getBmi();

        String text;
        Color bg;

        if (distB < distA - 0.05) {
            text = "IMPROVEMENT — Report B is closer to the healthy range than Report A "
                    + "(BMI changed by " + String.format("%+.1f", diff) + ").";
            bg = Theme.GREEN;
        } else if (distB > distA + 0.05) {
            text = "REGRESSION — Report B is further from the healthy range than Report A "
                    + "(BMI changed by " + String.format("%+.1f", diff) + ").";
            bg = Theme.ROSE;
        } else {
            text = "STABLE — Both reports are essentially at the same distance from the "
                    + "healthy range (BMI changed by " + String.format("%+.1f", diff) + ").";
            bg = Theme.AMBER;
        }

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(12, 16, 12, 16));

        // Use a JTextArea so text wraps automatically within the banner width
        JTextArea l = new JTextArea(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Color.WHITE);
        l.setOpaque(false);
        l.setLineWrap(true);
        l.setWrapStyleWord(true);
        l.setEditable(false);
        l.setFocusable(false);

        p.add(l, BorderLayout.CENTER);
        return p;
    }

    private static String deltaText(int a, int b, String unit) {
        int d = b - a;
        if (d == 0) return "no change";
        return (d > 0 ? "+" : "") + d + unit;
    }

    private static String deltaDouble(double a, double b, String unit) {
        double d = b - a;
        if (Math.abs(d) < 0.05) return "no change";
        return String.format("%+.1f%s", d, unit);
    }

    private static String verdict(BMIRecord a, BMIRecord b) {
        double distA = Math.abs(a.getBmi() - 22.0);
        double distB = Math.abs(b.getBmi() - 22.0);
        if (distB < distA - 0.05) return "improved";
        if (distB > distA + 0.05) return "worsened";
        return "unchanged";
    }

    private static Color deltaColor(String delta) {
        if (delta.contains("improved"))  return Theme.GREEN;
        if (delta.contains("worsened"))  return Theme.ROSE;
        if (delta.contains("unchanged") || delta.contains("no change")) return Theme.TEXT_MUTED;

        if (delta.startsWith("+")) return Theme.ORANGE;
        if (delta.startsWith("-")) return Theme.TEAL;
        return Theme.TEXT_MUTED;
    }
}