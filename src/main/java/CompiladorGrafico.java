import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CompiladorGrafico extends JFrame {
    private JTextArea codigoTextArea;
    private JTextArea recorridoTextArea;
    private int x = 300; // Coordenada x inicial
    private int y = 300; // Coordenada y inicial
    private double angulo = 0;
    private final int LIMITE_IZQUIERDA = 100;
    private final int LIMITE_DERECHA = 500;
    private final int LIMITE_SUPERIOR = 100;
    private final int LIMITE_INFERIOR = 500;

    public CompiladorGrafico() {
        setTitle("Compilador Gráfico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        panelIzquierdo.setBackground(new Color(240, 240, 240));
        add(panelIzquierdo, BorderLayout.WEST);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Arial", Font.BOLD, 16));
        panelIzquierdo.add(lblCodigo, BorderLayout.NORTH);

        codigoTextArea = new JTextArea(20, 30);
        codigoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panelIzquierdo.add(new JScrollPane(codigoTextArea), BorderLayout.CENTER);

        JButton ejecutarButton = new JButton("Ejecutar");
        ejecutarButton.setFont(new Font("Arial", Font.BOLD, 14));
        ejecutarButton.addActionListener(e -> interpretarCodigo());
        panelIzquierdo.add(ejecutarButton, BorderLayout.SOUTH);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        add(panelDerecho, BorderLayout.CENTER);

        JLabel lblRecorrido = new JLabel("Recorrido:");
        lblRecorrido.setFont(new Font("Arial", Font.BOLD, 16));
        panelDerecho.add(lblRecorrido, BorderLayout.NORTH);

        recorridoTextArea = new JTextArea(20, 30);
        recorridoTextArea.setEditable(false);
        recorridoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(recorridoTextArea);
        scrollPane.getViewport().setBackground(Color.WHITE); // Fondo blanco para el área de recorrido
        panelDerecho.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void interpretarCodigo() {
        String codigo = codigoTextArea.getText();
        recorridoTextArea.setText("");
        String[] instrucciones = codigo.split("\n");
        for (String instruccion : instrucciones) {
            interpretarInstruccion(instruccion.trim());
        }
        repaint();
    }

    private void interpretarInstruccion(String instruccion) {
        String[] partes = instruccion.split(" ");
        String comando = partes[0];
        switch (comando) {
            case "forw":
                int distancia = Integer.parseInt(partes[1]);
                avanzar(distancia);
                break;
            case "rotation":
                double grados = Double.parseDouble(partes[1]);
                girar(grados);
                break;
            case "q0":
                String[] coordenadas = partes[1].split(",");
                x = Integer.parseInt(coordenadas[0]);
                y = Integer.parseInt(coordenadas[1]);
                break;
        }
        recorridoTextArea.append(instruccion + "\n");
    }

    private void avanzar(int distancia) {
        int nuevoX = (int) (x + distancia * Math.cos(Math.toRadians(angulo)));
        int nuevoY = (int) (y + distancia * Math.sin(Math.toRadians(angulo)));

        // Verificar si la nueva posición está dentro de los límites
        if (nuevoX >= LIMITE_IZQUIERDA && nuevoX <= LIMITE_DERECHA && nuevoY >= LIMITE_SUPERIOR && nuevoY <= LIMITE_INFERIOR) {
            x = nuevoX;
            y = nuevoY;
        }
    }

    private void girar(double grados) {
        angulo += grados;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar el punto rojo dentro del área de recorrido
        int puntoX = (int) (x - LIMITE_IZQUIERDA) * recorridoTextArea.getWidth() / (LIMITE_DERECHA - LIMITE_IZQUIERDA);
        int puntoY = (int) (y - LIMITE_SUPERIOR) * recorridoTextArea.getHeight() / (LIMITE_INFERIOR - LIMITE_SUPERIOR);
        g2d.setColor(Color.RED);
        g2d.fillOval(puntoX - 5, puntoY - 5, 10, 10);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CompiladorGrafico::new);
    }
}
