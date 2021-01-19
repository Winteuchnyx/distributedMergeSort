/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import skripsiwinto.distributedmergesort.RMIServer;
import skripsiwinto.distributedmergesort.RMIServerLogged;
import skripsiwinto.distributedmergesort.RMIServerLogged.LOGOPTION;
import skripsiwinto.distributedmergesort.configuration;
import skripsiwinto.distributedmergesort.sorting;
import skripsiwinto.probingip.ServerAnswerer;
import skripsiwinto.probingip.ServerAsker;

/**
 *
 * @author Winto Junior Khosasi
 */
public class TampilanServer extends javax.swing.JFrame implements configuration{
    @Override
    public void aktivasiUlang(){
        lumpuhkanUI(true);
        aktifkanUtility();
        System.gc();
    }
    
    private static byte core;
    private String hostname;
    private LinkedList<String> listClient = new LinkedList<>();
    
    private RMIServerLogged.LOGOPTION option;
    private boolean autoSaveLog;
    private Path file;
    private JDialog dialogClient;
    
    private SwingWorker<Void,Void> workerListen;    
    private SwingWorker<Void,Void> workerScan;
    
    private DatagramSocket socketUdpListen;
    private SwingWorker<Void,Color> rgbing;

    public byte getCore() {
        return core;
    }

    public void setCore(byte core) {
        TampilanServer.core = core;
        textFieldCoreR.setText(Byte.toString(TampilanServer.core));
        setRmi();
    }
    
    public void lumpuhkanUI(boolean disabling){
        buttonSetCoreH.setEnabled(disabling);
        buttonSetHostnameH.setEnabled(disabling);
        buttonScanClient.setEnabled(disabling);
        buttonTampilkanClient.setEnabled(disabling);
        textFieldSetCoreH.setEnabled(disabling);
        textFieldsetHostnameH.setEnabled(disabling);
        
        radioButtonDebug.setEnabled(disabling);
        radioButtonNoLog.setEnabled(disabling);
        radioButtonNormal.setEnabled(disabling);
        checkBoxAutoSave.setEnabled(disabling);
        textFieldNamaLogFileH.setEditable(disabling);
        buttonSimpanLogFileH.setEnabled(disabling);
        
        jPanel1.setBackground(new Color(0, 71, 171,100));
        jPanel2.setBackground(new Color(0, 71, 171,100));
        
        if(!disabling){            
            rgbing = new RGBLighting(this.getContentPane(), new Color[][]{
                {this.getBackground(), this.getForeground()}, {Color.RED, Color.WHITE}
            }, 2000);
            rgbing.execute();
        }
    }
    
    public void setClient(String host){
        if(!listClient.contains(host)){
            listClient.add(host);
            textFieldClinetR.setText(Integer.toString(listClient.size()));
            if(dialogClient!=null)
            if (dialogClient.isVisible()) {
                ((TampilkanClient) dialogClient).refreshList(listClient.toArray(new String[0]));
            }
        }        
    }
    
    public void refreshScan(){
        textFieldClinetR.setText(Integer.toString(listClient.size()));
        if (dialogClient != null) {
            if (dialogClient.isVisible()) {
                ((TampilkanClient) dialogClient).refreshList(listClient.toArray(new String[0]));
            }
        }
    }
    
    public void matikanSocket(){
        if(rgbing!=null)
        rgbing.cancel(false);
        socketUdpListen.close();
    }
    
    /**
     * Creates new form TampilanServer
     */
    
    public TampilanServer(TampilanUtama form,String hostname,String clientName, byte core) {
        this.hostname = hostname;
        TampilanServer.core = core;
        listClient.add(clientName);
        option = RMIServerLogged.LOGOPTION.NO_LOG;
        
        initComponents();
        
        this.autoSaveLog = checkBoxAutoSave.isSelected();
        
        labelSimpanLogFileH.setVisible(false);
        textFieldNamaLogFileH.setVisible(false);
        buttonSimpanLogFileH.setVisible(false);
        
        buttonListenH.setVisible(false);
        
        textFieldSetCoreH.setVisible(false);
        buttonSetCoreH.setVisible(false);
        
        textFieldsetHostnameH.setVisible(false);
        buttonSetHostnameH.setVisible(false);
        
        textFieldClinetR.setText(Integer.toString(listClient.size()));
        textFieldCoreR.setText(Byte.toString(TampilanServer.core));
        textFieldHostnameR.setText(this.hostname);
        this.getContentPane().setBackground(new Color(0, 200, 0));
        
        System.setProperty("java.rmi.server.hostname", hostname);
        System.setProperty("java.security.policy","file:///test.policy");
        try{
            RMIServer serv = new RMIServer(core);
            sorting stub = (sorting) UnicastRemoteObject.exportObject(serv, 0);
            configuration stub2 = (configuration) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("MERGESORT",stub);
            registry.bind("aktivasi",stub2);
            System.out.println("server ready");
        }catch(AlreadyBoundException | RemoteException e){
            System.err.println(e.toString());
        }
        
        aktifkanUtility();
    }
    
    private void aktifkanUtility(){
        try {
            jPanel1.setBackground(new Color(51,153,0));
            jPanel2.setBackground(new Color(51,153,0));
            socketUdpListen = new DatagramSocket(9999);
            workerListen = new ServerAnswerer(socketUdpListen, this, listClient);
            workerListen.execute();
        } catch (SocketException ex) {
            Logger.getLogger(TampilanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textFieldClinetR = new javax.swing.JTextField();
        textFieldCoreR = new javax.swing.JTextField();
        textFieldSetCoreH =  new javax.swing.JTextField(3);
        buttonScanClient = new javax.swing.JButton();
        buttonTampilkanClient = new javax.swing.JButton();
        buttonListenH = new javax.swing.JButton();
        buttonSetCoreH = new javax.swing.JButton();
        textFieldHostnameR = new javax.swing.JTextField();
        textFieldsetHostnameH = new javax.swing.JTextField();
        buttonSetHostnameH = new javax.swing.JButton();
        judul = new javax.swing.JLabel();
        buttonHelp = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        radioButtonNoLog = new javax.swing.JRadioButton();
        radioButtonNormal = new javax.swing.JRadioButton();
        radioButtonDebug = new javax.swing.JRadioButton();
        checkBoxAutoSave = new javax.swing.JCheckBox();
        labelBuatLog = new javax.swing.JLabel();
        labelSimpanLogFileH = new javax.swing.JLabel();
        textFieldNamaLogFileH = new javax.swing.JTextField();
        buttonSimpanLogFileH = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 240, 210));
        jLabel1.setText("Client Terdeteksi");

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 240, 210));
        jLabel2.setText("Thread / Core Terpakai");

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(210, 240, 210));
        jLabel3.setText("RMI Hostname");

        textFieldClinetR.setEditable(false);
        textFieldClinetR.setEnabled(false);
        textFieldClinetR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldClinetRMouseClicked(evt);
            }
        });

        textFieldCoreR.setEditable(false);
        textFieldCoreR.setEnabled(false);
        textFieldCoreR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldCoreRMouseClicked(evt);
            }
        });

        textFieldSetCoreH.setBackground(new java.awt.Color(0, 49, 83));
        textFieldSetCoreH.setForeground(new java.awt.Color(255, 255, 0));

        buttonScanClient.setBackground(new java.awt.Color(0, 128, 128));
        buttonScanClient.setForeground(new java.awt.Color(210, 210, 210));
        buttonScanClient.setText("SCAN");
        buttonScanClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonScanClientMouseClicked(evt);
            }
        });

        buttonTampilkanClient.setBackground(new java.awt.Color(0, 128, 128));
        buttonTampilkanClient.setForeground(new java.awt.Color(210, 210, 210));
        buttonTampilkanClient.setText("TAMPIL");
        buttonTampilkanClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonTampilkanClientMouseClicked(evt);
            }
        });

        buttonListenH.setBackground(new java.awt.Color(0, 128, 128));
        buttonListenH.setForeground(new java.awt.Color(210, 210, 210));
        buttonListenH.setText("LISTEN");
        buttonListenH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonListenHMouseClicked(evt);
            }
        });

        buttonSetCoreH.setBackground(new java.awt.Color(0, 128, 128));
        buttonSetCoreH.setForeground(new java.awt.Color(210, 210, 210));
        buttonSetCoreH.setText("SET CORE");
        buttonSetCoreH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetCoreHMouseClicked(evt);
            }
        });

        textFieldHostnameR.setEditable(false);
        textFieldHostnameR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldHostnameR.setEnabled(false);
        textFieldHostnameR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldHostnameRMouseClicked(evt);
            }
        });

        textFieldsetHostnameH.setBackground(new java.awt.Color(0, 49, 83));
        textFieldsetHostnameH.setForeground(new java.awt.Color(255, 255, 0));

        buttonSetHostnameH.setBackground(new java.awt.Color(0, 128, 128));
        buttonSetHostnameH.setForeground(new java.awt.Color(210, 210, 210));
        buttonSetHostnameH.setText("SET NAME");
        buttonSetHostnameH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetHostnameHMouseClicked(evt);
            }
        });

        judul.setBackground(new java.awt.Color(204, 204, 204));
        judul.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        judul.setForeground(new java.awt.Color(210, 240, 210));
        judul.setText("DashBoard");

        buttonHelp.setIcon(new StretchIcon(getClass().getResource("/help-icon-17010.png")));
        buttonHelp.setBorderPainted(false);
        buttonHelp.setContentAreaFilled(false);
        buttonHelp.setFocusPainted(false);
        buttonHelp.setFocusable(false);
        buttonHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonHelpMouseClicked(evt);
            }
        });

        class MyIntFilter extends DocumentFilter {
            @Override
            public void insertString(FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {

                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.insert(offset, string);

                if (test(sb.toString())) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    // warn the user and don't allow the insert
                }
            }

            private boolean test(String text) {
                try {
                    //Integer.parseInt(text);
                    Byte.parseByte(text);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                AttributeSet attrs) throws BadLocationException {

                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.replace(offset, offset + length, text);

                if(sb.toString().equals("")){
                    super.replace(fb,offset,length,"",attrs);
                }else{
                    if (test(sb.toString())) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        // warn the user and don't allow the insert
                    }
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.delete(offset, offset + length);

                if (test(sb.toString())) {
                    super.remove(fb, offset, length);
                } else {
                    // warn the user and don't allow the insert
                }

            }
        }
        PlainDocument doc = (PlainDocument) textFieldSetCoreH.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
        ((AbstractDocument)textFieldsetHostnameH.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("(\\d{0,3}[.]){0,3}\\d{0,3}");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(textFieldClinetR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(textFieldCoreR, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldHostnameR, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldSetCoreH, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textFieldsetHostnameH, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonSetHostnameH, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(buttonSetCoreH, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(buttonScanClient, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonTampilkanClient)
                                .addGap(18, 18, 18)
                                .addComponent(buttonListenH)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(judul)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(judul)
                    .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldClinetR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonScanClient)
                    .addComponent(buttonTampilkanClient)
                    .addComponent(buttonListenH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textFieldCoreR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldSetCoreH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSetCoreH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textFieldHostnameR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldsetHostnameH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSetHostnameH))
                .addContainerGap())
        );

        jPanel2.setBackground(new Color(0, 71, 171,100));

        buttonGroup1.add(radioButtonNoLog);
        radioButtonNoLog.setForeground(new java.awt.Color(204, 255, 204));
        radioButtonNoLog.setMnemonic('0');
        radioButtonNoLog.setSelected(true);
        radioButtonNoLog.setText("No Log");
        radioButtonNoLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonNoLogItemStateChanged(evt);
            }
        });

        buttonGroup1.add(radioButtonNormal);
        radioButtonNormal.setForeground(new java.awt.Color(204, 255, 204));
        radioButtonNormal.setMnemonic('1');
        radioButtonNormal.setText("Normal Log");
        radioButtonNormal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonNormalItemStateChanged(evt);
            }
        });

        buttonGroup1.add(radioButtonDebug);
        radioButtonDebug.setForeground(new java.awt.Color(204, 255, 204));
        radioButtonDebug.setMnemonic('2');
        radioButtonDebug.setText("Debug Log");
        radioButtonDebug.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonDebugItemStateChanged(evt);
            }
        });

        checkBoxAutoSave.setForeground(new java.awt.Color(204, 255, 204));
        checkBoxAutoSave.setText("Auto Save Log");
        checkBoxAutoSave.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxAutoSaveItemStateChanged(evt);
            }
        });

        labelBuatLog.setBackground(new java.awt.Color(204, 204, 204));
        labelBuatLog.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        labelBuatLog.setForeground(new java.awt.Color(210, 240, 210));
        labelBuatLog.setText("Buat Log");
        labelBuatLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelBuatLogMouseClicked(evt);
            }
        });

        labelSimpanLogFileH.setBackground(new java.awt.Color(204, 204, 204));
        labelSimpanLogFileH.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        labelSimpanLogFileH.setForeground(new java.awt.Color(210, 240, 210));
        labelSimpanLogFileH.setText("Simpan Log File Dengan Nama : ");

        buttonSimpanLogFileH.setBackground(new Color(0, 71, 171,100));
        buttonSimpanLogFileH.setForeground(new java.awt.Color(210, 210, 210));
        buttonSimpanLogFileH.setText("OK");
        buttonSimpanLogFileH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSimpanLogFileHMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(labelBuatLog)
                        .addGap(18, 18, 18)
                        .addComponent(radioButtonNoLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioButtonNormal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioButtonDebug)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxAutoSave))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(labelSimpanLogFileH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldNamaLogFileH, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSimpanLogFileH, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBuatLog)
                    .addComponent(radioButtonNoLog)
                    .addComponent(radioButtonNormal)
                    .addComponent(radioButtonDebug)
                    .addComponent(checkBoxAutoSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSimpanLogFileH)
                    .addComponent(textFieldNamaLogFileH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSimpanLogFileH)))
        );

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(0, 255, 0));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelBuatLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBuatLogMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2&&!evt.isConsumed()){
            evt.consume();
            labelSimpanLogFileH.setVisible(true);
            textFieldNamaLogFileH.setVisible(true);
            buttonSimpanLogFileH.setVisible(true);
        }
    }//GEN-LAST:event_labelBuatLogMouseClicked

    private void textFieldClinetRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldClinetRMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2&&!evt.isConsumed()){
            evt.consume();
            buttonListenH.setVisible(true);
        }
    }//GEN-LAST:event_textFieldClinetRMouseClicked

    private void textFieldCoreRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldCoreRMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2 &&!evt.isConsumed()){
            evt.consume();
            textFieldSetCoreH.setVisible(true);
            buttonSetCoreH.setVisible(true);
        }
    }//GEN-LAST:event_textFieldCoreRMouseClicked

    private void textFieldHostnameRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldHostnameRMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2 && !evt.isConsumed()){
            evt.consume();
            textFieldsetHostnameH.setVisible(true);
            buttonSetHostnameH.setVisible(true);
        }
    }//GEN-LAST:event_textFieldHostnameRMouseClicked

    private void radioButtonNoLogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonNoLogItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.NO_LOG;
            setRmi();
        }
    }//GEN-LAST:event_radioButtonNoLogItemStateChanged

    private void radioButtonNormalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonNormalItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.NORMAL_LOG;
            setRmi();
        }
    }//GEN-LAST:event_radioButtonNormalItemStateChanged

    private void radioButtonDebugItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonDebugItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.DEBUG_LOG;
            setRmi();
        }
    }//GEN-LAST:event_radioButtonDebugItemStateChanged

    private void checkBoxAutoSaveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxAutoSaveItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            autoSaveLog = true;
            if(file == null){
                file = Paths.get("Log Server (autosave)/autosave.txt");
                int counter = 1;
                while(Files.exists(file)){
                    file = Paths.get("Log Server (autosave)/autosave - "+counter+".txt");
                }
            }
            setRmi();
        }else if(evt.getStateChange() == ItemEvent.DESELECTED){
            autoSaveLog = false;
            setRmi();
        }
    }//GEN-LAST:event_checkBoxAutoSaveItemStateChanged

    private void buttonSetCoreHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetCoreHMouseClicked
        // TODO add your handling code here:
        try{
            byte temp = Byte.parseByte(textFieldSetCoreH.getText());
            if(temp < 0 || temp > 20){
                JOptionPane.showMessageDialog(this, "maaf input harus lebih besar dari 0 dan lebih kecil dari 20");
            }else{         
                core = temp;
                textFieldCoreR.setText(String.valueOf(temp));
                setRmi();
            }
        }catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Input tidak valid");
        }
    }//GEN-LAST:event_buttonSetCoreHMouseClicked

    private void buttonSetHostnameHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetHostnameHMouseClicked
        // TODO add your handling code here:
        System.setProperty("java.rmi.server.hostname", textFieldsetHostnameH.getText());
        textFieldHostnameR.setText(textFieldsetHostnameH.getText());
    }//GEN-LAST:event_buttonSetHostnameHMouseClicked

    private void buttonSimpanLogFileHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSimpanLogFileHMouseClicked
        // TODO add your handling code here:
        if(textFieldNamaLogFileH.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Maaf nama file tidak boleh kosong, usahakan sesuai dengan penulisan yang didukung OS anda");
        }else{
            Path pt = Paths.get("UserSavedServerLog/" + textFieldNamaLogFileH.getText() + ".txt");
            try {
                if (Files.notExists(pt.getParent())) {
                    Files.createDirectory(pt.getParent());
                }
                int counter = 1;
                while (Files.exists(pt)) {
                    pt = Paths.get("UserSavedServerLog/" + textFieldNamaLogFileH.getText() + "" + (counter++) + ".txt");
                }

                PrintWriter pw = new PrintWriter(Files.newOutputStream(pt), true);
                pw.println(jTextArea1.getText());
                pw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "tidak dapat buat file", JOptionPane.ERROR_MESSAGE);
            }
        }        
    }//GEN-LAST:event_buttonSimpanLogFileHMouseClicked

    private void buttonTampilkanClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonTampilkanClientMouseClicked
        // TODO add your handling code here:     
        if(dialogClient==null)
            dialogClient = new TampilkanClient(this, "Daftar Client terdeteksi");
        if(!dialogClient.isVisible()){
            dialogClient.setVisible(true);
            workerScan = new ServerAsker(this, listClient);
            workerScan.execute();
        }else{
            dialogClient.toFront();
        }
    }//GEN-LAST:event_buttonTampilkanClientMouseClicked

    private void buttonScanClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonScanClientMouseClicked
        // TODO add your handling code here:
        JButton button = (JButton)evt.getSource();
        if(button.getText().equals("SCAN")){
            workerScan = new ServerAsker(this,listClient);
            workerScan.execute();
            button.setText("CANCEL");
        }else if(button.getText().equals("CANCEL")){
            workerScan.cancel(true);
            button.setText("SCAN");
        }
    }//GEN-LAST:event_buttonScanClientMouseClicked

    private void buttonListenHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonListenHMouseClicked
        // TODO add your handling code here:
        JButton buttonTemp = (JButton) evt.getSource();
        lumpuhkanUI(true);
        if(buttonTemp.getText().equals("LISTEN")){
            matikanSocket();
            new Thread(
                    () -> {
                        try {
                            socketUdpListen = new DatagramSocket(9999);
                            DatagramPacket packet;
                            Boolean ready = false;

                            while (true) {
                                byte[] buf = new byte[256];
                                packet = new DatagramPacket(buf, buf.length);
                                socketUdpListen.receive(packet);
                                String listen = new String(packet.getData(), StandardCharsets.UTF_8);
                                if (listen.trim().matches("skripsi winto[?]")) {
                                    InetAddress addr = packet.getAddress();
                                    int port = packet.getPort();
                                    buf = "ya server skripsinya winto".getBytes();
                                    packet = new DatagramPacket(buf, buf.length, addr, port);
                                    socketUdpListen.send(packet);
                                } else if (listen.trim().matches("siap[?]")) {
                                    InetAddress addr = packet.getAddress();
                                    int port = packet.getPort();
                                    buf = "ya siap".getBytes(StandardCharsets.UTF_8);
                                    packet = new DatagramPacket(buf, buf.length, addr, port);
                                    socketUdpListen.send(packet);
                                } else if (listen.trim().matches("core.*[0-9]")) {
                                    core = Byte.parseByte(listen.trim().split("\\s")[2]);
                                    InetAddress addr = packet.getAddress();
                                    int port = packet.getPort();
                                    setClient(addr.getHostAddress());
                                    buf = "ya core diterima".getBytes();
                                    packet = new DatagramPacket(buf, buf.length, addr, port);
                                    socketUdpListen.send(packet);
                                    break;
                                }
                            }
                            socketUdpListen.close();
                            aktifkanUtility();
                            buttonListenH.setText("LISTEN");
                        } catch (SocketException ex) {
                            Logger.getLogger(TampilanServer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                    Logger.getLogger(TampilanServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
            ).start();
            buttonListenH.setText("CANCEL");
        }else if(buttonTemp.getText().equals("CANCEL")){
            socketUdpListen.close();
            aktifkanUtility();
            buttonListenH.setText("LISTEN");
        }
    }//GEN-LAST:event_buttonListenHMouseClicked
    TampilanPanduan panduan = null;
    private void buttonHelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonHelpMouseClicked
        // TODO add your handling code here:
        if (panduan == null || !panduan.isDisplayable()) {
            panduan = new TampilanPanduan(TampilanPanduan.panduan.server);
            panduan.setVisible(true);
        }
    }//GEN-LAST:event_buttonHelpMouseClicked

    private void setRmi(){
        if(option == LOGOPTION.NO_LOG){
            try {
                RMIServer serv = new RMIServer(core);
                sorting stub = (sorting) UnicastRemoteObject.exportObject(serv, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind("MERGESORT",stub);
                System.out.println("server ready");
            } catch (RemoteException ex) {
                Logger.getLogger(TampilanServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                RMIServerLogged serv = new RMIServerLogged(this, file, core, option, autoSaveLog);
                sorting stub = (sorting) UnicastRemoteObject.exportObject(serv, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind("MERGESORT",stub);
                System.out.println("server ready");
            } catch (RemoteException ex) {
                Logger.getLogger(TampilanServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonHelp;
    private javax.swing.JButton buttonListenH;
    public javax.swing.JButton buttonScanClient;
    private javax.swing.JButton buttonSetCoreH;
    private javax.swing.JButton buttonSetHostnameH;
    private javax.swing.JButton buttonSimpanLogFileH;
    private javax.swing.JButton buttonTampilkanClient;
    private javax.swing.JCheckBox checkBoxAutoSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel judul;
    private javax.swing.JLabel labelBuatLog;
    private javax.swing.JLabel labelSimpanLogFileH;
    private javax.swing.JRadioButton radioButtonDebug;
    private javax.swing.JRadioButton radioButtonNoLog;
    private javax.swing.JRadioButton radioButtonNormal;
    private javax.swing.JTextField textFieldClinetR;
    private javax.swing.JTextField textFieldCoreR;
    private javax.swing.JTextField textFieldHostnameR;
    private javax.swing.JTextField textFieldNamaLogFileH;
    private javax.swing.JTextField textFieldSetCoreH;
    private javax.swing.JTextField textFieldsetHostnameH;
    // End of variables declaration//GEN-END:variables
}
