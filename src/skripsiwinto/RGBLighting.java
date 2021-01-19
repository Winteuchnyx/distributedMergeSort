/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Winto Junior Khosasi
 */
public class RGBLighting extends SwingWorker<Void, Color>{
    private final Component component;
    private final Color oldBackground;
    private final Color oldForeground;
    private final Color[][] listOfColor;
    private final int gap;
    
    public RGBLighting(Component component, Color[][] color, int gap){
        this.component = component;
        oldBackground = component.getBackground();
        oldForeground = component.getForeground();
        this.listOfColor = color;
        this.gap = gap;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        int length = listOfColor.length;
        int counter = 0;
        while(!isCancelled()){
            publish(listOfColor[counter][0],listOfColor[counter][1]);
            counter++;
            if(counter>=length){
                counter = 0;
            }
            Thread.sleep(gap);
        }
        
        return null;
    }

    @Override
    protected void done() {
        component.setBackground(oldBackground);
        component.setForeground(oldForeground);
    }

    @Override
    protected void process(List<Color> chunks) {
        component.setBackground(chunks.get(0));
        component.setForeground(chunks.get(1));
    }
    
    
}
