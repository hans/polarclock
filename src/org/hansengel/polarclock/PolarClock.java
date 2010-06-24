/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hansengel.polarclock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import javax.swing.JApplet;
import javax.swing.Timer;
import org.hansengel.JavaSucks;

public class PolarClock extends JApplet {
    private BufferedImage offscreen;
    private Graphics2D offscreenGraphics;

    private boolean debug = false;
    private boolean omgBBQApocalypse = false;

    private BasicStroke arcStroke;

    private Color bgColor = new Color(0x1a1a1a);
    private Color titleColor = new Color(0xffffff);

    private Color red = new Color(0xe31a14);
    private Color orange = new Color(0xf06b14);
    private Color green = new Color(0x46c22d);
    private Color blue = new Color(0x1693a5);
    private Color gold = new Color(0xfbb829);
    private Color vanilla = new Color(0xfcfbe3);

    private Font titleFont;
    private FontRenderContext titleFRC;
    private Rectangle2D titleBounds;

    private Font labelFont;
    private FontRenderContext labelFRC;
    private Rectangle2D labelBounds;

    private int secLblX;
    private int minLblX;
    private int hrLblX;
    private int dayLblX;
    private int monLblX;
    private int yrLblX;

    private int strokeWidth = 20;//11;

    private Arc2D secArc;
    private Arc2D minArc;
    private Arc2D hrArc;
    private Arc2D dayArc;
    private Arc2D monArc;
    private Arc2D yrArc;

    private boolean showClock = false;

    @Override public void init() {
        setSize(750, 750);

        offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        offscreenGraphics = (Graphics2D)offscreen.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        };
        Timer timer = new Timer(1000 / 30, listener);
        timer.start();

        arcStroke = new BasicStroke(strokeWidth + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        labelFont = new Font("Myriad Pro", Font.PLAIN, 10);
        labelFRC = new FontRenderContext(null, true, false);

        secArc = new Arc2D.Double(225, 190, 300.0, 300.0, 0.0, 0, Arc2D.OPEN);
        labelBounds = labelFont.getStringBounds("99", labelFRC);
        secLblX = (int)( secArc.getX() + secArc.getWidth() - labelBounds.getWidth() + 4 );

        minArc = new Arc2D.Double(secArc.getX() - strokeWidth, secArc.getY() - strokeWidth, secArc.getWidth() + 2 * strokeWidth, secArc.getHeight() + 2 * strokeWidth, 0.0, 0, Arc2D.OPEN);
        minLblX = (int)( minArc.getX() + minArc.getWidth() - labelBounds.getWidth() + 4 );

        hrArc = new Arc2D.Double(minArc.getX() - strokeWidth, minArc.getY() - strokeWidth, minArc.getWidth() + 2 * strokeWidth, minArc.getHeight() + 2 * strokeWidth, 0.0, 0, Arc2D.OPEN);
        hrLblX = (int)( hrArc.getX() + hrArc.getWidth() - labelBounds.getWidth() + 4 );

        dayArc = new Arc2D.Double(hrArc.getX() - strokeWidth, hrArc.getY() - strokeWidth, hrArc.getWidth() + 2 * strokeWidth, hrArc.getHeight() + 2 * strokeWidth, 0.0, 0, Arc2D.OPEN);
        dayLblX = (int)( dayArc.getX() + dayArc.getWidth() - labelBounds.getWidth() + 4 );
        
        monArc = new Arc2D.Double(dayArc.getX() - strokeWidth, dayArc.getY() - strokeWidth, dayArc.getWidth() + 2 * strokeWidth, dayArc.getHeight() + 2 * strokeWidth, 0.0, 0, Arc2D.OPEN);
        monLblX = (int)( monArc.getX() + monArc.getWidth() - labelBounds.getWidth() + 4 );

        yrArc = new Arc2D.Double(monArc.getX() - strokeWidth, monArc.getY() - strokeWidth, monArc.getWidth() + 2 * strokeWidth, monArc.getHeight() + 2 * strokeWidth, 0.0, 0, Arc2D.OPEN);
        yrLblX = (int)( yrArc.getX() + yrArc.getWidth() - labelBounds.getWidth() + 4 );

        titleFont = new Font("Myriad Pro", Font.PLAIN, 40);
        titleFRC = new FontRenderContext(null, true, false);
        titleBounds = titleFont.getStringBounds("polarclock", titleFRC);

        JavaSucks.callAfterDelay("startClock", this.getClass(), this, 1000);
    }

    public void paint(Graphics2D g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(titleFont);
        g.setColor(titleColor);

        g.drawString("polarclock",
                getWidth() / 2 - (int)titleBounds.getWidth() / 2,
                getHeight() / 2 - (int)titleBounds.getHeight() / 2);

        if ( showClock ) {
            Calendar cal = Calendar.getInstance();

            boolean pm = cal.get(Calendar.AM_PM) == Calendar.PM;
            int sec = cal.get(Calendar.SECOND);
            int min = cal.get(Calendar.MINUTE);
            int hr = cal.get(Calendar.HOUR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int mon = cal.get(Calendar.MONTH) + 1;
            int yr = cal.get(Calendar.YEAR);

            g.setStroke(arcStroke);

            double secDeg = -sec * 6.0;// - cal.get(Calendar.MILLISECOND) / 1000.0;
            secArc.setAngleExtent(secDeg);
            g.setColor(red);
            g.draw(secArc);

            g.setColor(vanilla); g.setFont(labelFont);
            g.drawString("" + sec, secLblX,
                    (int)(secArc.getY() + secArc.getHeight() / 2));

            double minDeg = -min * 6.0 + secDeg / 60.0;
            minArc.setAngleExtent(minDeg);
            g.setColor(orange);
            g.draw(minArc);

            g.setColor(vanilla); g.setFont(labelFont);
            g.drawString("" + min, minLblX,
                    (int)(minArc.getY() + minArc.getHeight() / 2));

            int modifier = ( pm ) ? 12 : 0;
            double hrDeg = -( ( hr + modifier ) * 15.0 ) + minDeg / 24.0;
            hrArc.setAngleExtent(hrDeg);
            g.setColor(green);
            g.draw(hrArc);

            g.setColor(vanilla); g.setFont(labelFont);
            g.drawString("" + ( hr + modifier ), hrLblX,
                    (int)(hrArc.getY() + hrArc.getHeight() / 2));

            double eachDayDeg = 360.0 / cal.getMaximum(Calendar.DAY_OF_MONTH);
            double dayDeg = -( day * eachDayDeg ) + hrDeg / ( 360.0 / eachDayDeg );
            dayArc.setAngleExtent(dayDeg);
            g.setColor(blue);
            g.draw(dayArc);

            g.setColor(vanilla); g.setFont(labelFont);
            g.drawString("" + day, dayLblX,
                    (int)(dayArc.getY() + dayArc.getHeight() / 2));

            double monDeg = -mon * 30.0 + dayDeg / 12.0;
            monArc.setAngleExtent(monDeg);
            g.setColor(gold);
            g.draw(monArc);

            g.setColor(vanilla); g.setFont(labelFont);
            g.drawString("" + mon, monLblX,
                    (int)(monArc.getY() + monArc.getHeight() / 2));

            double yrDeg = 0;
            if ( omgBBQApocalypse ) {
                // OMGBBQ APOKALIPZ!!!11!!!!!1!!! - progress from 2000 -> 2012
                yrDeg = -( yr - 2000 ) * 30.0 + monDeg / 12;
                yrArc.setAngleExtent(yrDeg);
                g.setColor(vanilla);
                g.draw(yrArc);

                g.setColor(vanilla); g.setFont(labelFont);
                g.drawString("" + yr, yrLblX,
                        (int)(yrArc.getY() + yrArc.getHeight() / 2));
            }

            if ( debug ) {
                g.setColor(Color.white);
                g.setFont(new Font("Monaco", Font.PLAIN, 13));

                g.drawString("mon:\t" + monDeg, 0, getHeight() - 2);
                g.drawString("day:\t" + dayDeg, 0, getHeight() - 15);
                g.drawString("hr:\t\t" + hrDeg, 0, getHeight() - 28);
                g.drawString("min:\t" + minDeg, 0, getHeight() - 41);
                g.drawString("sec:\t" + secDeg, 0, getHeight() - 54);
            }
        }
    }

    @Override public void paint(Graphics g) {
        paint(offscreenGraphics);
        g.drawImage(offscreen, 0, 0, null);
    }

    public void startClock() { showClock = true; }
}
