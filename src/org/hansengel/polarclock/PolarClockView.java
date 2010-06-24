/*
 * PolarClockView.java
 */

package org.hansengel.polarclock;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JWindow;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;

/**
 * The application's main frame.
 */
public class PolarClockView extends FrameView implements MouseListener, MouseMotionListener {
    public PolarClockView(SingleFrameApplication app) {
        super(app);

        window = new JWindow();
        window.setSize(750, 750);

        applet = new PolarClock();
        applet.init();
        applet.addMouseListener(this);
        applet.addMouseMotionListener(this);
        window.getContentPane().add(applet);

        window.setFocusable(true);
        window.setVisible(true);
        window.requestFocus();
    }

    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { dragPoint = e.getPoint(); }
    public void mouseReleased(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) {
        if ( skip > 0 ) {
            skip--;
            return;
        }

        double dX = e.getX() - dragPoint.getX();
        double dY = e.getY() - dragPoint.getY();

        window.setLocation(window.getX() + (int)dX, window.getY() + (int)dY);
        dragPoint = e.getPoint();
        skip = 5;
    }

    private Point dragPoint;
    private int skip = 0;

    private JWindow window;
    private PolarClock applet;
}