/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hansengel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import javax.swing.Timer;

public class JavaSucks {
    private static final String separator = System.getProperty("line.separator");

    public static String read(File file) {
        String ret = ""; String line = null;
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            
            while ( ( line = input.readLine() ) != null ) {
                ret += line; ret += separator;
            }

            input.close();
        } catch ( Exception e ) { e.printStackTrace(); }

        return ret;
    }

    public static String matchWithSeparator(String in, String separator) {
        String[] res = in.split(separator);

        if ( res.length >= 1 )
            return res[0];
        return null;
    }

    public static void callAfterDelay(String _method, Class<?> receiver, final Object receiverInstance, int delay) {
        final Method method;

        try {
            method = receiver.getMethod(_method);
        } catch ( SecurityException e ) { return;
        } catch ( NoSuchMethodException e ) { return; }

        ActionListener listener = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    method.invoke(receiverInstance);
                } catch ( Exception ex ) { ex.printStackTrace(); }
            }
        };

        Timer timer = new Timer(delay, listener);
        timer.setRepeats(false);
        timer.start();
    }
}