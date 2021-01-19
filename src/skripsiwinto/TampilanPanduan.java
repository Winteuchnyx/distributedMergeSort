/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.HashMap;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

/**
 *
 * @author Winto Junior Khosasi
 */
public class TampilanPanduan extends javax.swing.JFrame {

    /**
     * Creates new form TampilanPanduan
     */
    public TampilanPanduan(panduan pandu) {
        try {
            this.pandu = pandu;   //gk usah buat variable
            initComponents();
            this.getContentPane().setBackground(new Color(0, 200, 0));
            switch(pandu){
                case utama:
                    jEditorPane1.setPage(getClass().getResource("/panduan_utama/utama.html"));
                    jLabel1.setText("<html><div style = \"border-bottom : 5px solid #3333CC; padding: 10px 100px\">PANDUAN MODUL UTAMA</div></html>");
                    break;
                case client:
                    jEditorPane1.setPage(getClass().getResource("/panduan_client/client.html"));
                    jLabel1.setText("<html><div style = \"border-bottom : 5px solid #3333CC; padding: 10px 100px\">PANDUAN MODUL CLIENT</div></html>");
                    break;
                case server:
                    jEditorPane1.setPage(getClass().getResource("/panduan_server/server.html"));
                    jLabel1.setText("<html><div style = \"border-bottom : 5px solid #3333CC; padding: 10px 100px\">PANDUAN MODUL SERVER</div></html>");
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(TampilanPanduan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public enum panduan{
        utama,client,server
    }
    
    panduan pandu;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setLocation(new java.awt.Point(800, 0));
        setPreferredSize(new java.awt.Dimension(700, 600));

        jEditorPane1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(jEditorPane1);

        jLabel1.setBackground(new java.awt.Color(51, 153, 0));
        jLabel1.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html><div style = \"border-bottom : 5px solid #3333CC; padding: 10px 100px\">PANDUAN</div></html>");

        Font font = jLabel1.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jLabel1.setFont(font.deriveFont(attributes));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}