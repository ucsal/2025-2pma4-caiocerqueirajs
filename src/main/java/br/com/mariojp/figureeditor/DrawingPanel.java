package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

class DrawingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_SIZE = 60;
    private final List<Shape> shapes = new ArrayList<>();

    private Point startDrag;
    private Point endDrag;

    DrawingPanel() {
        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && startDrag == null) {
                    int size = DEFAULT_SIZE;
                    double x = e.getX() - size / 2.0;
                    double y = e.getY() - size / 2.0;
                    Shape circle = new Ellipse2D.Double(x, y, size, size);
                    shapes.add(circle);
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = e.getPoint();
                endDrag = startDrag;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                endDrag = e.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (startDrag != null && endDrag != null) {
                    int x = Math.min(startDrag.x, endDrag.x);
                    int y = Math.min(startDrag.y, endDrag.y);
                    int w = Math.abs(endDrag.x - startDrag.x);
                    int h = Math.abs(endDrag.y - startDrag.y);

                    Shape rect = new Rectangle2D.Double(x, y, w, h);
                    shapes.add(rect);
                }
                startDrag = null;
                endDrag = null;
                repaint();
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    void clear() {
        shapes.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(1.2f));
        for (Shape s : shapes) {
            g2.setColor(new Color(30, 144, 255));
            g2.fill(s);
            g2.setColor(new Color(0, 0, 0, 70));
            g2.draw(s);
        }

        if (startDrag != null && endDrag != null) {
            g2.setColor(Color.GRAY);
            float[] dash = {5f, 5f};
            g2.setStroke(new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10f, dash, 0));
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);
            int w = Math.abs(endDrag.x - startDrag.x);
            int h = Math.abs(endDrag.y - startDrag.y);
            g2.drawRect(x, y, w, h);
        }

        g2.dispose();
    }
}
