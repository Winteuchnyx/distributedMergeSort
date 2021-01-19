/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import skripsiwinto.distributedmergesort.RMIClient;
import skripsiwinto.distributedmergesort.RMIClientLogged;
import skripsiwinto.distributedmergesort.RMIClientLogged.LOGOPTION;
import skripsiwinto.distributedmergesort.UILoggerClient;
import skripsiwinto.distributedmergesort.configuration;
import skripsiwinto.iohandling.FileGenerator;
import skripsiwinto.iohandling.FileToArray;
import skripsiwinto.mergesort.ParallelMergeSort;
import skripsiwinto.mergesort.SequentialMergeSort;
import skripsiwinto.mergesort.Sorted;
import skripsiwinto.probingip.ClientAnswerer;
import skripsiwinto.probingip.ClientAsker;

/**
 *
 * @author Winto Junior Khosasi
 */
public class TampilanClient extends javax.swing.JFrame {
    
    public void refreshScan(){
        textFieldJmlClientR.setText(String.valueOf(hostList.size()));
        if(tampilkanServer!=null)
        if(tampilkanServer.isVisible()){
            ((TampilkanServer)tampilkanServer).refreshList(hostList.toArray(new containerHostConfig[0]));
        }
    }
    
    private JDialog tampilkanServer;
    
    private enum Algoritma{
        SEQUENTIAL_MERGESORT,PARALLEL_MERGESORT,DISTRIBUTED_MERGESORT
    }
    /**
     * Creates new form TampilanClient
     */
    
    private String networkAddress = null;
    private final LinkedList<containerHostConfig> hostList;
    private File[] userInputSource;
    
    private Algoritma algoritma;
    private Sorted sorted;
    
    private byte core;
    private int chunkSize;
    private int runningTest;
    
    private int maxCore = 4;
    private int maxChunkSize = 250;
    private int maxRunTest = 100;
    
    private LOGOPTION option; 
    
    private Path autosaveFile = null;
    private boolean autosave;
    
    private Path outputSortFile = null;
    private Path statFile = null;
    
    private SwingWorker<Void, String> workerScan;
    private SwingWorker<Void,Void> workerListen;
    private DatagramSocket socketUdpListen;
    private SwingWorker<Void,Color> rgbing;
    
    public TampilanClient(TampilanUtama form,String networkAddress,LinkedList<containerHostConfig> hostList) {
        this.networkAddress = networkAddress;
        this.hostList = hostList;
        this.core = 2;
        this.chunkSize = 0;
        this.runningTest = 10;
        this.algoritma = Algoritma.DISTRIBUTED_MERGESORT;
        this.sorted = Sorted.ASC;
        
        this.option = LOGOPTION.NO_LOG;
        this.autosave = false;
        
        initComponents();
        
        UIManager.put("Button.disabledForeground", Color.RED);
        this.getContentPane().setBackground(new Color(0, 200, 0));
        rgbing = new RGBLighting(ButtonPilihAlgoritma, new Color[][]{
            {ButtonPilihAlgoritma.getBackground(),ButtonPilihAlgoritma.getForeground()},{Color.RED,Color.WHITE},{ButtonPilihAlgoritma.getBackground(),ButtonPilihAlgoritma.getForeground()},
            {new Color(15,82,186),new Color(255,255,240)}
        }, 500);
        rgbing.execute();
        
        textFieldStatusAlgoritmaR.setText(comboBoxAlgoritma.getSelectedItem().toString());
        textFieldStatusUrutanR.setText(comboBoxUrutan.getSelectedItem().toString());
        textFieldJmlClientR.setText(String.valueOf(hostList.size()));
        textFieldCoreClientR.setText(String.valueOf(core));
        textFieldChunkR.setText(String.valueOf(chunkSize));
        textFieldJmlRunningR.setText(String.valueOf(runningTest));
        jProgressBar1.setVisible(false);
        labelProgressBar.setVisible(false);
        
        jLabel19.setVisible(false);
        textFieldNamaFileLog.setVisible(false);
        buttonSimpanFileLog.setVisible(false);
        
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel6.setVisible(false);
        
        aktivasiUtility();
    }
    
    private void aktivasiUtility(){
        try {
            socketUdpListen = new DatagramSocket(9998);
            workerListen = new ClientAnswerer(socketUdpListen);
            workerListen.execute();
        } catch (SocketException ex) {
            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Tidak dapat memulai listener pada port 9998 , beberapa fitur scanner akan tidak tersedia", "Peringatan", JOptionPane.ERROR_MESSAGE);
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
        comboBoxAlgoritma = new javax.swing.JComboBox<>();
        comboBoxUrutan = new javax.swing.JComboBox<>();
        textFieldStatusAlgoritmaR = new javax.swing.JTextField();
        textFieldStatusUrutanR = new javax.swing.JTextField();
        ButtonPilihAlgoritma = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        buttonHelp = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        textFieldJmlClientR = new javax.swing.JTextField();
        buttonTampilkanServer = new javax.swing.JButton();
        buttonScanServer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        textFieldCoreClientR = new javax.swing.JTextField();
        textFieldSetCoreClient = new javax.swing.JTextField();
        buttonSetCoreClient = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        buttonPilihFileInput = new javax.swing.JButton();
        textFieldJmlFileInputR = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textFieldChunkR = new javax.swing.JTextField();
        buttonSetChunk = new javax.swing.JButton();
        textFieldSetChunk = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textFieldJmlRunningR = new javax.swing.JTextField();
        textFieldSetJmlRunning = new javax.swing.JTextField();
        buttonSetRunning = new javax.swing.JButton();
        buttonReady = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        radioButtonNoLog = new javax.swing.JRadioButton();
        radioButtonNormalLog = new javax.swing.JRadioButton();
        RadioButtonDebugLog = new javax.swing.JRadioButton();
        checkBoxAutoSave = new javax.swing.JCheckBox();
        buttonStart = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        textFieldArraySizeR = new javax.swing.JTextField();
        textFieldAntrianRunningR = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jProgressBar1 = new javax.swing.JProgressBar();
        labelProgressBar = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        textFieldJmlGenerator = new javax.swing.JTextField();
        textFieldRangeGenerator = new javax.swing.JTextField();
        textFieldOutputFileGenerator = new javax.swing.JTextField();
        buttonGenerator = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        buttonStatistik = new javax.swing.JButton();
        buttonHasilSorting = new javax.swing.JButton();
        buttonSavedLog = new javax.swing.JButton();
        buttonRestart = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        textFieldNamaFileLog = new javax.swing.JTextField();
        buttonSimpanFileLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 153, 0));
        jPanel1.setAutoscrolls(true);

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(210, 240, 210));
        jLabel1.setText("Status terpilih : ");

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 240, 210));
        jLabel2.setText("Pengaturan Algoritma :");

        comboBoxAlgoritma.setBackground(new java.awt.Color(0, 128, 0));
        comboBoxAlgoritma.setForeground(new java.awt.Color(204, 204, 204));
        comboBoxAlgoritma.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sequential Merge Sort", "Parallel Merge Sort", "Distributed Merge Sort" }));
        comboBoxAlgoritma.setSelectedIndex(2);
        comboBoxAlgoritma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAlgoritmaActionPerformed(evt);
            }
        });

        comboBoxUrutan.setBackground(new java.awt.Color(0, 128, 0));
        comboBoxUrutan.setForeground(new java.awt.Color(204, 204, 204));
        comboBoxUrutan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DESC" }));
        comboBoxUrutan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxUrutanActionPerformed(evt);
            }
        });

        textFieldStatusAlgoritmaR.setEditable(false);
        textFieldStatusAlgoritmaR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldStatusAlgoritmaR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldStatusAlgoritmaR.setForeground(new java.awt.Color(0, 255, 0));
        textFieldStatusAlgoritmaR.setEnabled(false);

        textFieldStatusUrutanR.setEditable(false);
        textFieldStatusUrutanR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldStatusUrutanR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldStatusUrutanR.setForeground(new java.awt.Color(0, 255, 0));
        textFieldStatusUrutanR.setEnabled(false);

        ButtonPilihAlgoritma.setBackground(new Color(0, 128, 128,255));
        ButtonPilihAlgoritma.setForeground(new java.awt.Color(210, 210, 210));
        ButtonPilihAlgoritma.setText("PILIH");
        ButtonPilihAlgoritma.setFocusable(false);
        ButtonPilihAlgoritma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPilihAlgoritmaActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(204, 204, 204));
        jLabel9.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(210, 240, 210));
        jLabel9.setText("Algoritma");

        jLabel10.setBackground(new java.awt.Color(204, 204, 204));
        jLabel10.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(210, 240, 210));
        jLabel10.setText("Urutan");

        buttonHelp.setIcon(new StretchIcon(getClass().getResource("/help-icon-17010.png")));
        buttonHelp.setBackground(new Color(0, 128, 128,255));
        buttonHelp.setForeground(new java.awt.Color(210, 210, 210));
        buttonHelp.setBorderPainted(false);
        buttonHelp.setContentAreaFilled(false);
        buttonHelp.setFocusPainted(false);
        buttonHelp.setFocusable(false);
        buttonHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonHelpMouseClicked(evt);
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
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxAlgoritma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxUrutan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonPilihAlgoritma)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(15, 15, 15)
                        .addComponent(textFieldStatusAlgoritmaR, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldStatusUrutanR, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(textFieldStatusAlgoritmaR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldStatusUrutanR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxAlgoritma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxUrutan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonPilihAlgoritma)))
        );

        jPanel2.setBackground(new Color(0, 71, 171,100));
        jPanel2.setAutoscrolls(true);
        jPanel2.setName("INPUTING"); // NOI18N

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(210, 240, 210));
        jLabel3.setText("Server terdeteksi");

        textFieldJmlClientR.setEditable(false);
        textFieldJmlClientR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldJmlClientR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldJmlClientR.setForeground(new java.awt.Color(0, 255, 0));
        textFieldJmlClientR.setEnabled(false);

        buttonTampilkanServer.setBackground(new Color(0, 128, 128,255));
        buttonTampilkanServer.setForeground(new java.awt.Color(210, 210, 210));
        buttonTampilkanServer.setText("TAMPILKAN & SET SERVER");
        buttonTampilkanServer.setFocusable(false);
        buttonTampilkanServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTampilkanServerActionPerformed(evt);
            }
        });

        buttonScanServer.setBackground(new Color(0, 128, 128,255));
        buttonScanServer.setForeground(new java.awt.Color(210, 210, 210));
        buttonScanServer.setText("SCAN");
        buttonScanServer.setFocusable(false);
        buttonScanServer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonScanServerMouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(210, 240, 210));
        jLabel4.setText("Core Client");

        textFieldCoreClientR.setEditable(false);
        textFieldCoreClientR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldCoreClientR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldCoreClientR.setForeground(new java.awt.Color(0, 255, 0));
        textFieldCoreClientR.setEnabled(false);
        textFieldCoreClientR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldCoreClientRMouseClicked(evt);
            }
        });

        textFieldSetCoreClient.setBackground(new java.awt.Color(0, 49, 83));
        textFieldSetCoreClient.setForeground(new java.awt.Color(255, 255, 0));

        buttonSetCoreClient.setBackground(new Color(0, 128, 128,255));
        buttonSetCoreClient.setForeground(new java.awt.Color(210, 210, 210));
        buttonSetCoreClient.setText("SET CORE");
        buttonSetCoreClient.setFocusable(false);
        buttonSetCoreClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetCoreClientMouseClicked(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(210, 240, 210));
        jLabel5.setText("Input File berisi angka acak");

        buttonPilihFileInput.setBackground(new Color(0, 128, 128,255));
        buttonPilihFileInput.setForeground(new java.awt.Color(210, 210, 210));
        buttonPilihFileInput.setText("PILIH FILE");
        buttonPilihFileInput.setFocusable(false);
        buttonPilihFileInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPilihFileInputActionPerformed(evt);
            }
        });

        textFieldJmlFileInputR.setEditable(false);
        textFieldJmlFileInputR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldJmlFileInputR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldJmlFileInputR.setForeground(new java.awt.Color(255, 0, 0));
        textFieldJmlFileInputR.setEnabled(false);

        jLabel6.setBackground(new java.awt.Color(204, 204, 204));
        jLabel6.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(210, 240, 210));
        jLabel6.setText("Jumlah File : ");

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(210, 240, 210));
        jLabel7.setText("Chunk Size ");

        textFieldChunkR.setEditable(false);
        textFieldChunkR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldChunkR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldChunkR.setForeground(new java.awt.Color(0, 255, 0));
        textFieldChunkR.setEnabled(false);
        textFieldChunkR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldChunkRMouseClicked(evt);
            }
        });

        buttonSetChunk.setBackground(new Color(0, 128, 128,255));
        buttonSetChunk.setForeground(new java.awt.Color(210, 210, 210));
        buttonSetChunk.setText("SET CHUNK");
        buttonSetChunk.setFocusable(false);
        buttonSetChunk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetChunkMouseClicked(evt);
            }
        });

        textFieldSetChunk.setBackground(new java.awt.Color(0, 49, 83));
        textFieldSetChunk.setForeground(new java.awt.Color(255, 255, 0));

        jLabel8.setBackground(new java.awt.Color(204, 204, 204));
        jLabel8.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(210, 240, 210));
        jLabel8.setText("Jumlah ulang / Running Test");

        textFieldJmlRunningR.setEditable(false);
        textFieldJmlRunningR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldJmlRunningR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldJmlRunningR.setForeground(new java.awt.Color(255, 0, 0));
        textFieldJmlRunningR.setEnabled(false);
        textFieldJmlRunningR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldJmlRunningRMouseClicked(evt);
            }
        });

        textFieldSetJmlRunning.setBackground(new java.awt.Color(0, 49, 83));
        textFieldSetJmlRunning.setForeground(new java.awt.Color(255, 255, 0));

        buttonSetRunning.setBackground(new Color(0, 128, 128,255));
        buttonSetRunning.setForeground(new java.awt.Color(210, 210, 210));
        buttonSetRunning.setText("SET RUN");
        buttonSetRunning.setFocusable(false);
        buttonSetRunning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetRunningMouseClicked(evt);
            }
        });

        buttonReady.setBackground(new Color(0, 128, 128,255));
        buttonReady.setForeground(new java.awt.Color(210, 210, 210));
        buttonReady.setText("READY");
        buttonReady.setFocusable(false);
        buttonReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReadyActionPerformed(evt);
            }
        });

        ((AbstractDocument)textFieldSetCoreClient.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        ((AbstractDocument)textFieldSetChunk.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        ((AbstractDocument)textFieldSetJmlRunning.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldJmlRunningR, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldSetJmlRunning)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSetRunning, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(buttonReady)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(133, 133, 133)
                                        .addComponent(textFieldJmlClientR, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonTampilkanServer)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(buttonPilihFileInput)
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel6)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(textFieldJmlFileInputR, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(buttonScanServer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldChunkR, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textFieldCoreClientR, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldSetCoreClient)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(textFieldSetChunk)
                                        .addGap(3, 3, 3)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonSetCoreClient)
                                    .addComponent(buttonSetChunk))))
                        .addGap(110, 110, 110))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textFieldJmlClientR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonTampilkanServer)
                    .addComponent(buttonScanServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textFieldCoreClientR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldSetCoreClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSetCoreClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(buttonPilihFileInput)
                    .addComponent(textFieldJmlFileInputR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textFieldChunkR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSetChunk)
                    .addComponent(textFieldSetChunk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(textFieldJmlRunningR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFieldSetJmlRunning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonSetRunning, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonReady))))
        );

        jPanel3.setBackground(new Color(0, 71, 171,100));

        jLabel11.setBackground(new java.awt.Color(204, 204, 204));
        jLabel11.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(210, 240, 210));
        jLabel11.setText("Buat log : ");

        radioButtonNoLog.setBackground(new java.awt.Color(0, 71, 171));
        buttonGroup1.add(radioButtonNoLog);
        radioButtonNoLog.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        radioButtonNoLog.setForeground(new java.awt.Color(204, 255, 204));
        radioButtonNoLog.setSelected(true);
        radioButtonNoLog.setText("No Log");
        radioButtonNoLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonNoLogItemStateChanged(evt);
            }
        });

        radioButtonNormalLog.setBackground(new java.awt.Color(0, 71, 171));
        buttonGroup1.add(radioButtonNormalLog);
        radioButtonNormalLog.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        radioButtonNormalLog.setForeground(new java.awt.Color(204, 255, 204));
        radioButtonNormalLog.setText("Normal Log");
        radioButtonNormalLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonNormalLogItemStateChanged(evt);
            }
        });

        RadioButtonDebugLog.setBackground(new java.awt.Color(0, 71, 171));
        buttonGroup1.add(RadioButtonDebugLog);
        RadioButtonDebugLog.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        RadioButtonDebugLog.setForeground(new java.awt.Color(204, 255, 204));
        RadioButtonDebugLog.setText("Debug Log");
        RadioButtonDebugLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RadioButtonDebugLogItemStateChanged(evt);
            }
        });

        checkBoxAutoSave.setBackground(new java.awt.Color(0, 71, 171));
        checkBoxAutoSave.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        checkBoxAutoSave.setForeground(new java.awt.Color(204, 255, 204));
        checkBoxAutoSave.setText("Auto Save Log");
        checkBoxAutoSave.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxAutoSaveItemStateChanged(evt);
            }
        });

        buttonStart.setBackground(new Color(0, 128, 128,255));
        buttonStart.setForeground(new java.awt.Color(210, 210, 210));
        buttonStart.setText("START");
        buttonStart.setFocusable(false);
        buttonStart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonStartMouseClicked(evt);
            }
        });
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(radioButtonNoLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonNormalLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RadioButtonDebugLog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkBoxAutoSave)
                .addGap(16, 16, 16)
                .addComponent(buttonStart)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(RadioButtonDebugLog)
                .addComponent(radioButtonNormalLog)
                .addComponent(radioButtonNoLog)
                .addComponent(checkBoxAutoSave)
                .addComponent(buttonStart))
        );

        jPanel4.setBackground(new java.awt.Color(51, 153, 0));

        jLabel12.setBackground(new java.awt.Color(204, 204, 204));
        jLabel12.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(210, 240, 210));
        jLabel12.setText("Ukuran Array");

        jLabel13.setBackground(new java.awt.Color(204, 204, 204));
        jLabel13.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(210, 240, 210));
        jLabel13.setText("Antrian Running Test ke : ");

        textFieldArraySizeR.setEditable(false);
        textFieldArraySizeR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldArraySizeR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldArraySizeR.setForeground(new java.awt.Color(255, 255, 0));
        textFieldArraySizeR.setEnabled(false);

        textFieldAntrianRunningR.setEditable(false);
        textFieldAntrianRunningR.setBackground(new java.awt.Color(0, 0, 0));
        textFieldAntrianRunningR.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        textFieldAntrianRunningR.setForeground(new java.awt.Color(255, 255, 0));
        textFieldAntrianRunningR.setEnabled(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(0, 255, 0));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jProgressBar1.setToolTipText("");

        labelProgressBar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        labelProgressBar.setForeground(new java.awt.Color(210, 230, 210));
        labelProgressBar.setText("Status Progress Bar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(30, 30, 30)
                        .addComponent(textFieldArraySizeR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldAntrianRunningR, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textFieldArraySizeR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(textFieldAntrianRunningR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProgressBar)
                .addGap(0, 0, 0)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new Color(0, 71, 171,100));

        jLabel16.setBackground(new java.awt.Color(204, 204, 204));
        jLabel16.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(210, 240, 210));
        jLabel16.setText("Jangkauan");

        jLabel15.setBackground(new java.awt.Color(204, 204, 204));
        jLabel15.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(210, 240, 210));
        jLabel15.setText("Jumlah Bilangan");

        jLabel14.setBackground(new java.awt.Color(204, 204, 204));
        jLabel14.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(210, 240, 210));
        jLabel14.setText("Random File Generator :");

        textFieldJmlGenerator.setBackground(new java.awt.Color(0, 49, 83));
        textFieldJmlGenerator.setForeground(new java.awt.Color(255, 255, 0));

        textFieldRangeGenerator.setBackground(new java.awt.Color(0, 49, 83));
        textFieldRangeGenerator.setForeground(new java.awt.Color(255, 255, 0));

        textFieldOutputFileGenerator.setBackground(new java.awt.Color(0, 49, 83));
        textFieldOutputFileGenerator.setForeground(new java.awt.Color(255, 255, 0));

        buttonGenerator.setBackground(new Color(0, 128, 128,255));
        buttonGenerator.setForeground(new java.awt.Color(210, 210, 210));
        buttonGenerator.setText("Generate");
        buttonGenerator.setFocusable(false);
        buttonGenerator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGeneratorActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(204, 204, 204));
        jLabel17.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(210, 240, 210));
        jLabel17.setText("Nama File");

        ((AbstractDocument)textFieldJmlGenerator.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        ((AbstractDocument)textFieldRangeGenerator.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldJmlGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldOutputFileGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textFieldRangeGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonGenerator))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(textFieldJmlGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(textFieldRangeGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(textFieldOutputFileGenerator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonGenerator))
                .addContainerGap())
        );

        jPanel6.setBackground(new Color(0, 71, 171,100));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(204, 204, 204));
        jLabel18.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(210, 240, 210));
        jLabel18.setText("Hasil Proses");
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        buttonStatistik.setBackground(new Color(0, 128, 128,255));
        buttonStatistik.setForeground(new java.awt.Color(210, 210, 210));
        buttonStatistik.setText("Buka Stat");
        buttonStatistik.setFocusable(false);
        buttonStatistik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonStatistikMouseClicked(evt);
            }
        });

        buttonHasilSorting.setBackground(new Color(0, 128, 128,255));
        buttonHasilSorting.setForeground(new java.awt.Color(210, 210, 210));
        buttonHasilSorting.setText("Buka Hasil Sort");
        buttonHasilSorting.setFocusable(false);
        buttonHasilSorting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonHasilSortingMouseClicked(evt);
            }
        });

        buttonSavedLog.setBackground(new Color(0, 128, 128,255));
        buttonSavedLog.setForeground(new java.awt.Color(210, 210, 210));
        buttonSavedLog.setText("Buka Log");
        buttonSavedLog.setFocusable(false);
        buttonSavedLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSavedLogMouseClicked(evt);
            }
        });

        buttonRestart.setBackground(new Color(0, 128, 128,255));
        buttonRestart.setForeground(new java.awt.Color(210, 210, 210));
        buttonRestart.setText("RESTART");
        buttonRestart.setFocusable(false);
        buttonRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRestartActionPerformed(evt);
            }
        });

        jLabel19.setBackground(new java.awt.Color(204, 204, 204));
        jLabel19.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(210, 240, 210));
        jLabel19.setText("Simpan Log File dengan Nama: ");

        textFieldNamaFileLog.setBackground(new java.awt.Color(0, 49, 83));
        textFieldNamaFileLog.setForeground(new java.awt.Color(255, 255, 0));

        buttonSimpanFileLog.setBackground(new Color(0, 128, 128,255));
        buttonSimpanFileLog.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        buttonSimpanFileLog.setForeground(new java.awt.Color(210, 210, 210));
        buttonSimpanFileLog.setText("OK");
        buttonSimpanFileLog.setFocusable(false);
        buttonSimpanFileLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimpanFileLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(buttonStatistik)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonHasilSorting)
                        .addGap(18, 18, 18)
                        .addComponent(buttonSavedLog)
                        .addGap(18, 18, 18)
                        .addComponent(buttonRestart)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSimpanFileLog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textFieldNamaFileLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(buttonSimpanFileLog, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonHasilSorting)
                        .addComponent(buttonSavedLog)
                        .addComponent(buttonRestart)
                        .addComponent(textFieldNamaFileLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonStatistik))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxAlgoritmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAlgoritmaActionPerformed
        // TODO add your handling code here:
        JComboBox obj = (JComboBox)evt.getSource();
        int index = obj.getSelectedIndex();
        switch(index){
            case 0 : 
                algoritma = Algoritma.SEQUENTIAL_MERGESORT;
                
                jLabel4.setVisible(false);
                textFieldCoreClientR.setVisible(false);
                textFieldSetCoreClient.setVisible(false);
                buttonSetCoreClient.setVisible(false);
                
                jLabel3.setVisible(false);
                textFieldJmlClientR.setVisible(false);
                buttonScanServer.setVisible(false);
                buttonTampilkanServer.setVisible(false);
                
                jLabel7.setVisible(false);
                textFieldChunkR.setVisible(false);
                textFieldSetChunk.setVisible(false);
                buttonSetChunk.setVisible(false);
            break;
            case 1 : 
                algoritma = Algoritma.PARALLEL_MERGESORT;
                jLabel3.setVisible(false);
                textFieldJmlClientR.setVisible(false);
                buttonScanServer.setVisible(false);
                buttonTampilkanServer.setVisible(false);
                
                jLabel7.setVisible(false);
                textFieldChunkR.setVisible(false);
                textFieldSetChunk.setVisible(false);
                buttonSetChunk.setVisible(false);
                
                jLabel4.setVisible(true);
                textFieldCoreClientR.setVisible(true);
                textFieldSetCoreClient.setVisible(true);
                buttonSetCoreClient.setVisible(true);
            break;
            default : 
                algoritma = Algoritma.DISTRIBUTED_MERGESORT;
                jLabel4.setVisible(true);
                textFieldCoreClientR.setVisible(true);
                textFieldSetCoreClient.setVisible(true);
                buttonSetCoreClient.setVisible(true);
                
                jLabel3.setVisible(true);
                textFieldJmlClientR.setVisible(true);
                buttonScanServer.setVisible(true);
                buttonTampilkanServer.setVisible(true);
                
                jLabel7.setVisible(true);
                textFieldChunkR.setVisible(true);
                textFieldSetChunk.setVisible(true);
                buttonSetChunk.setVisible(true);
                break;
        }
        textFieldStatusAlgoritmaR.setText(obj.getSelectedItem().toString());
    }//GEN-LAST:event_comboBoxAlgoritmaActionPerformed

    private void comboBoxUrutanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxUrutanActionPerformed
        // TODO add your handling code here:
        JComboBox obj = (JComboBox) evt.getSource();
        int index = obj.getSelectedIndex();
        switch(index){
            case 0 : sorted = Sorted.ASC;
            break;
            default : sorted = Sorted.DESC;
            break;
        }
        textFieldStatusUrutanR.setText(obj.getSelectedItem().toString());
    }//GEN-LAST:event_comboBoxUrutanActionPerformed

    private void ButtonPilihAlgoritmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPilihAlgoritmaActionPerformed
        // TODO add your handling code here:
        jPanel2.setVisible(true);
        comboBoxAlgoritma.setEnabled(false);
        comboBoxUrutan.setEnabled(false);
        ButtonPilihAlgoritma.setEnabled(false);
        
        rgbing.cancel(false);
        rgbing = new RGBLighting(buttonReady, new Color[][]{
            {buttonReady.getBackground(),buttonReady.getForeground()},{new Color(255,0,127),new Color(75,0,130)},{buttonReady.getBackground(),buttonReady.getForeground()},
            {new Color(0,127,255),new Color(255,255,0)}
        }, 1000);
        rgbing.execute();
    }//GEN-LAST:event_ButtonPilihAlgoritmaActionPerformed

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2&&!evt.isConsumed()){
            evt.consume();
            
            jLabel19.setVisible(true);
            textFieldNamaFileLog.setVisible(true);
            buttonSimpanFileLog.setVisible(true);
        }
    }//GEN-LAST:event_jLabel18MouseClicked

    private void buttonSetCoreClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetCoreClientMouseClicked
        // TODO add your handling code here:
        try{
            byte temp = Byte.parseByte(textFieldSetCoreClient.getText());
            if(temp < 0 ||temp > maxCore){
                JOptionPane.showMessageDialog(this, "Maaf Input tidak valid, nilai core harus diantara 0 sampai "+maxCore);
                textFieldSetCoreClient.setText(String.valueOf(core));
            }else{
                core = temp;
                textFieldCoreClientR.setText(String.valueOf(core));
            }            
        }catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_buttonSetCoreClientMouseClicked

    private void buttonPilihFileInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPilihFileInputActionPerformed
        // TODO add your handling code here:
        Path file = Paths.get("Generated Input File/");
        if(Files.notExists(file)){
            try {
                Files.createDirectory(file);
            } catch (IOException ex) {
                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JFileChooser chooseFile = new JFileChooser(file.toFile());
        chooseFile.setMultiSelectionEnabled(true);
        int retVal = chooseFile.showDialog(jPanel1, "Masukkan Semua File terpilih");
        if(retVal == JFileChooser.APPROVE_OPTION){
            userInputSource = chooseFile.getSelectedFiles();
            textFieldJmlFileInputR.setText(String.valueOf(userInputSource.length));
        }
    }//GEN-LAST:event_buttonPilihFileInputActionPerformed

    private void buttonSetChunkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetChunkMouseClicked
        // TODO add your handling code here:
        try{
            int temp = Integer.parseInt(textFieldSetChunk.getText());
            if(temp < 0 ||temp > maxChunkSize){
                JOptionPane.showMessageDialog(this, "Maaf Input tidak valid, nilai chunk harus diantara 0 sampai "+maxChunkSize);
                textFieldSetChunk.setText(String.valueOf(chunkSize));
            }else{
                chunkSize = temp;
                if(chunkSize == 0){
                    textFieldChunkR.setText("Otomatis");                    
                }else{
                    textFieldChunkR.setText(String.valueOf(String.format("%.4f",chunkSize * 4.0 / 1000000.0)) + "MB");                    
                }
            }            
        }catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_buttonSetChunkMouseClicked

    private void buttonSetRunningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetRunningMouseClicked
        // TODO add your handling code here:
        try{
            int temp = Integer.parseInt(textFieldSetJmlRunning.getText());
            if(temp < 1 ||temp > maxRunTest){
                JOptionPane.showMessageDialog(this, "Maaf Input tidak valid, nilai runTest harus diantara 1 atau "+maxRunTest);
                textFieldSetJmlRunning.setText(String.valueOf(runningTest));
            }else{
                runningTest = temp;
                textFieldJmlRunningR.setText(String.valueOf(runningTest));
            }            
        }catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_buttonSetRunningMouseClicked

    private void buttonReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReadyActionPerformed
        // TODO add your handling code here:
        if(userInputSource==null){
            JOptionPane.showMessageDialog(this, "Anda lupa memilih File untuk di urutkan");
        }else if(userInputSource.length ==0){
            JOptionPane.showMessageDialog(this, "File untuk diurutkan tidak tersedia ! mohon pilih ulang");
        }else{
            //code untuk disable server punya tampilan
            if (algoritma == Algoritma.DISTRIBUTED_MERGESORT) {
                EventQueue.invokeLater(
                        () -> {
                            try {
                                DatagramSocket socketUdp = new DatagramSocket();
                                byte[] buf = new byte[256];
                                buf = "lumpuhkan UI".getBytes(StandardCharsets.UTF_8);
                                DatagramPacket packet;
                                LinkedList<String> ipServer = new LinkedList<>();
                                hostList.forEach((item) -> {
                                    ipServer.add(item.getHostname());
                                });
                                byte backOff = 3;
                                while (!ipServer.isEmpty() && backOff > 0) {
                                    for (String item : ipServer) {
                                        packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(item), 9999);
                                        socketUdp.send(packet);
                                    }
                                    int max = ipServer.size();
                                    DatagramPacket packetRecv;
                                    socketUdp.setSoTimeout(300);
                                    while (max > 0) {
                                        byte[] bufRecv = new byte[256];
                                        packetRecv = new DatagramPacket(buf, buf.length);
                                        try {
                                            socketUdp.receive(packetRecv);
                                            String listenTemp = new String(packetRecv.getData(), StandardCharsets.UTF_8);
                                            if (listenTemp.trim().matches("ya UI dilumpuhkan")) {
                                                ipServer.remove(packetRecv.getAddress().getHostAddress());
                                            }
                                        } catch (SocketTimeoutException exx) {
                                            backOff--;
                                        }
                                        max--;
                                    }
                                }
                                socketUdp.close();
                            } catch (SocketException ex) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                );
            }
            rgbing.cancel(false);
            rgbing = new RGBLighting(buttonStart, new Color[][]{
                {buttonStart.getBackground(),buttonStart.getForeground()},{new Color(220,20,60),new Color(255,215,0)},{buttonStart.getBackground(),buttonStart.getForeground()},
                {new Color(0,149,182),new Color(212,212,212)}
            }, 1000);
            rgbing.execute();
            jPanel3.setVisible(true);            
        }        
    }//GEN-LAST:event_buttonReadyActionPerformed

    private void radioButtonNoLogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonNoLogItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.NO_LOG;
        }
    }//GEN-LAST:event_radioButtonNoLogItemStateChanged

    private void radioButtonNormalLogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonNormalLogItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.NORMAL_LOG;
        }
    }//GEN-LAST:event_radioButtonNormalLogItemStateChanged

    private void RadioButtonDebugLogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RadioButtonDebugLogItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            option = LOGOPTION.DEBUG_LOG;
        }
    }//GEN-LAST:event_RadioButtonDebugLogItemStateChanged

    private void checkBoxAutoSaveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxAutoSaveItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED){
            autosave = true;
            if(autosaveFile == null){
                autosaveFile = Paths.get("Log Client (autosave)/autosave.txt");
                int counter = 1;
                while(Files.exists(autosaveFile)){
                    autosaveFile = Paths.get("Log Client (autosave)/autosave - "+(counter++)+".txt");
                }
            }
        }else if(evt.getStateChange() == ItemEvent.DESELECTED){
            autosave = false;
        }
    }//GEN-LAST:event_checkBoxAutoSaveItemStateChanged

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        // TODO add your handling code here:     
        EventQueue.invokeLater(
                ()->{
                    if (userInputSource == null) {
                        JOptionPane.showMessageDialog(this, "File yang ingin disortir belum tersedia.");
                    } else if (userInputSource.length == 0) {
                        JOptionPane.showMessageDialog(this, "Tidak ada data terisi untuk diurutkan");
                    } else if (hostList.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tidak ada server terdeteksi mohon lakukan scan ulang!!!");
                    } else {
                        buttonTampilkanServer.setEnabled(false);
                        buttonScanServer.setEnabled(false);
                        textFieldSetCoreClient.setEnabled(false);
                        buttonSetCoreClient.setEnabled(false);
                        buttonPilihFileInput.setEnabled(false);
                        textFieldSetChunk.setEnabled(false);
                        buttonSetChunk.setEnabled(false);
                        textFieldSetJmlRunning.setEnabled(false);
                        buttonSetRunning.setEnabled(false);
                        buttonReady.setEnabled(false);
                        radioButtonNoLog.setEnabled(false);
                        radioButtonNormalLog.setEnabled(false);
                        RadioButtonDebugLog.setEnabled(false);
                        checkBoxAutoSave.setEnabled(false);
                    }   
                }
        );
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRestartActionPerformed
        // TODO add your handling code here:
        statFile = null;
        autosaveFile = null;
        outputSortFile = null;
        rgbing.cancel(false);
        jPanel2.setVisible(false);
        comboBoxAlgoritma.setEnabled(true);
        comboBoxUrutan.setEnabled(true);
        ButtonPilihAlgoritma.setEnabled(true);

        jPanel3.setVisible(false);

        buttonTampilkanServer.setEnabled(true);
        buttonScanServer.setEnabled(true);
        textFieldSetCoreClient.setEnabled(true);
        buttonSetCoreClient.setEnabled(true);
        buttonPilihFileInput.setEnabled(true);
        textFieldSetChunk.setEnabled(true);
        buttonSetChunk.setEnabled(true);
        textFieldSetJmlRunning.setEnabled(true);
        buttonSetRunning.setEnabled(true);
        buttonReady.setEnabled(true);
        radioButtonNoLog.setEnabled(true);
        radioButtonNormalLog.setEnabled(true);
        RadioButtonDebugLog.setEnabled(true);
        checkBoxAutoSave.setEnabled(true);
        buttonStart.setEnabled(true);
        textFieldAntrianRunningR.setText("");
        textFieldArraySizeR.setText("");
        jProgressBar1.setVisible(false);
        jProgressBar1.setValue(0);
        labelProgressBar.setVisible(false);

        jPanel6.setVisible(false);
        rgbing = new RGBLighting(ButtonPilihAlgoritma, new Color[][]{
            {ButtonPilihAlgoritma.getBackground(),ButtonPilihAlgoritma.getForeground()},{Color.RED,Color.WHITE},{ButtonPilihAlgoritma.getBackground(),ButtonPilihAlgoritma.getForeground()},
            {new Color(15,82,186),new Color(255,255,240)}
        }, 500);
        rgbing.execute();
    }//GEN-LAST:event_buttonRestartActionPerformed

    private void buttonStatistikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonStatistikMouseClicked
        // TODO add your handling code here:
        if(statFile != null)
        if(Files.exists(statFile)){
            try{
               ProcessBuilder pb = new ProcessBuilder("notepad",statFile.toString());
               pb.start();
            } catch (IOException ex) {
                try{
                    ProcessBuilder pb = new ProcessBuilder("xdg-utils", statFile.toString());
                    pb.start();
                } catch (IOException ex1) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("xdg-open", statFile.toString());
                        pb.start();
                    } catch (IOException ex2) {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("open", "-t", statFile.toString());
                            pb.start();
                        } catch (IOException ex3) {
                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex1);
                            JOptionPane.showMessageDialog(this, "Maaf tidak terdapat text editor yang dapat dipakai aplikasi ini pada OS anda");
                        }
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Maaf file tidak ditemukan, mohon lakukan proses pengurutan ulang agar file dapat dibuat kembali");
        }
        else
            JOptionPane.showMessageDialog(this, "Maaf file tidak tersedia karena proses gagal, mohon lakukan proses pengurutan ulang agar file dapat dibuat kembali");
    }//GEN-LAST:event_buttonStatistikMouseClicked

    private void buttonHasilSortingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonHasilSortingMouseClicked
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser(outputSortFile.getParent().toString());
        chooser.setMultiSelectionEnabled(true);
        int retVal = chooser.showOpenDialog(jPanel5);
        if(retVal == JFileChooser.APPROVE_OPTION){
            File[] file = chooser.getSelectedFiles();
            for(File item : file){
                try {
                    ProcessBuilder pb = new ProcessBuilder("notepad", item.getPath());
                    pb.start();
                } catch (IOException ex) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("xdg-utils", item.getPath());
                        pb.start();
                    } catch (IOException ex1) {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("xdg-open", item.getPath());
                            pb.start();
                        } catch (IOException ex2) {
                            try {
                                ProcessBuilder pb = new ProcessBuilder("open", "-t", item.getPath());
                                pb.start();
                            } catch (IOException ex3) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex1);
                                JOptionPane.showMessageDialog(this, "Maaf tidak terdapat text editor yang dapat dipakai aplikasi ini pada OS anda");
                            }
                        }
                    }
                }
            }            
        }
    }//GEN-LAST:event_buttonHasilSortingMouseClicked

    private void buttonSavedLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSavedLogMouseClicked
        // TODO add your handling code here:
        if(autosaveFile == null){
            JOptionPane.showMessageDialog(this, "Maaf file log autosave tidak tersedia karena sebelum proses \"Auto Save Log\" tidak dicentang atau terjadi pembatalan proses");
        }else{
            if (Files.exists(autosaveFile)) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("notepad", autosaveFile.toString());
                    pb.start();
                } catch (IOException ex) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("xdg-utils", autosaveFile.toString());
                        pb.start();
                    } catch (IOException ex1) {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("xdg-open", autosaveFile.toString());
                            pb.start();
                        } catch (IOException ex2) {
                            try {
                                ProcessBuilder pb = new ProcessBuilder("open", "-t", autosaveFile.toString());
                                pb.start();
                            } catch (IOException ex3) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex1);
                                JOptionPane.showMessageDialog(this, "Maaf tidak terdapat text editor yang dapat dipakai aplikasi ini pada OS anda");
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Maaf file tidak ditemukan, mohon lakukan proses pengurutan ulang agar file dapat dibuat kembali");
            }
        }
    }//GEN-LAST:event_buttonSavedLogMouseClicked

    private void buttonGeneratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGeneratorActionPerformed
        // TODO add your handling code here:
        if(textFieldOutputFileGenerator.getText().equals("")||textFieldJmlGenerator.getText().equals("")||textFieldRangeGenerator.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Maaf tidak boleh ada kolom yang kosong untuk nama file, jumlah dan jangkauan generator");
        }else{
            int jumlah = Integer.parseInt(textFieldJmlGenerator.getText());
            int jangkauan = Integer.parseInt(textFieldRangeGenerator.getText());
            if(jumlah <= 0){
                JOptionPane.showMessageDialog(this, "Maaf nilai jumlah tidak boleh 0");
            }else if(jangkauan <= 0){
                JOptionPane.showMessageDialog(this, "Maaf nilai jangkauan tidak boleh 0");
            }else{
                String temp = textFieldOutputFileGenerator.getText();
                Path fileTemp = Paths.get(temp);
                int counter = 1;
                while(Files.exists(fileTemp)){
                    String atemp = temp + "("+(counter++)+")";
                    fileTemp = Paths.get(atemp);
                }
                FileGenerator.randomGenerator("Generated Input File/"+temp, jumlah, jangkauan, 10);
                JOptionPane.showMessageDialog(this, "File Bilangan Acak Berhasil dibuat...");
            }
        }
    }//GEN-LAST:event_buttonGeneratorActionPerformed

    private void buttonStartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonStartMouseClicked
        buttonStart.setEnabled(false);rgbing.cancel(false);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(
                () -> {
                    if (userInputSource != null) {
                        if (!((userInputSource.length == 0) && hostList.isEmpty())) {
                            labelProgressBar.setVisible(true);
                            jProgressBar1.setVisible(true);
                            int inc = 0;
                            try{
                                if (algoritma == Algoritma.DISTRIBUTED_MERGESORT) {
                                    labelProgressBar.setText("Menyiapkan Server...");
                                    try {
                                        DatagramSocket socketUdp = new DatagramSocket();
                                        byte[] buf;
                                        DatagramPacket packet;
                                        LinkedList<String> serverList = new LinkedList<>();
                                        for (containerHostConfig item : hostList) {
                                            serverList.add(item.getHostname());
                                        }
                                        socketUdp.setSoTimeout(10000);
                                        while (serverList.size() > 0) {
                                            buf = "mulai".getBytes(StandardCharsets.UTF_8);
                                            for (String item : serverList) {
                                                packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(item), 9999);
                                                socketUdp.send(packet);
                                            }
                                            int max = serverList.size(), backOff = 3;
                                            while (max > 0) {
                                                try {
                                                    buf = new byte[256];
                                                    packet = new DatagramPacket(buf, buf.length);
                                                    socketUdp.receive(packet);
                                                    String listenTemp = new String(packet.getData(), StandardCharsets.UTF_8);
                                                    if (listenTemp.trim().matches("ya siap mulai")) {
                                                        InetAddress addr = packet.getAddress();
                                                        int port = packet.getPort();
                                                        serverList.remove(addr.getHostAddress());
                                                        buf = "baik".getBytes(StandardCharsets.UTF_8);
                                                        packet = new DatagramPacket(buf, buf.length, addr, port);
                                                        socketUdp.send(packet);
                                                    }
                                                    max--;
                                                    backOff = 3;
                                                } catch (SocketTimeoutException timeOutEx) {
                                                    backOff--;
                                                    if (backOff <= 0) {
                                                        JFrame frame = new JFrame();
                                                        int reply = JOptionPane.showConfirmDialog(frame, "Tidak ada respon dari salah satu server, Batalkan?", "Gagal menyiapkan sumber daya server", JOptionPane.YES_NO_OPTION);
                                                        frame.dispose();
                                                        if (reply == JOptionPane.YES_OPTION) {
                                                            buttonRestart.doClick();
                                                            return;
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        socketUdpListen.close();
                                    } catch (SocketException ex) {
                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    String temp = "Statistik Log/Log Distributed MergeSort(ChunkSize " + chunkSize + " ,host " + hostList.size() + " ,File " + userInputSource.length + " , loop " + runningTest + ")";
                                    statFile = Paths.get(temp + ".txt");
                                    if (Files.notExists(statFile.getParent())) {
                                        try {
                                            Files.createDirectory(statFile.getParent());
                                        } catch (IOException ex) {
                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        int counter = 1;
                                        while (Files.exists(statFile)) {
                                            String atemp = temp + " (" + (counter++) + ")";
                                            statFile = Paths.get(atemp + ".txt");
                                        }
                                    }
                                    int size, totalFile = userInputSource.length, progressValue;
                                    int[][] arr;
                                    for (inc = 0; inc < totalFile; inc++) {
                                        labelProgressBar.setText("Memproses file input : " + userInputSource[inc].getName() + "...");
                                        size = FileToArray.convertToMultiArr(userInputSource[inc].toPath(), hostList.size());
                                        if (size > 0) {
                                            textFieldArraySizeR.setText(String.valueOf(size));
                                            arr = FileToArray.getMultiArr();
                                            String[] serverList = new String[hostList.size()];
                                            int count = 0;
                                            for (containerHostConfig ips : hostList) {
                                                serverList[count++] = ips.getHostname();
                                            }
                                            double ElapsedTime = 0;
                                            try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                pw.println(userInputSource[inc].toPath().getFileName().toString() + " \"DISTRIBUTED MERGESORT\" Size: " + size + " host : " + serverList.length);
                                            } catch (IOException ex) {
                                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            int[] arrResult = null;
                                            if (option == LOGOPTION.NO_LOG) {
                                                RMIClient dmergesort;
                                                for (int i = 0; i < runningTest; i++) {
                                                    textFieldAntrianRunningR.setText(String.valueOf(i + 1));
                                                    dmergesort = new RMIClient(arr, chunkSize, serverList, core, sorted);
                                                    ElapsedTime = dmergesort.sort();
                                                    arrResult = dmergesort.getArr();
                                                    if (arrResult == null) {
                                                        JOptionPane.showMessageDialog(this, "Terdapat error dalam pendistribusian proses, proses dibatalkan !!!!");
                                                        arr = null;
                                                        break;
                                                    }
                                                    try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                        pw.println(ElapsedTime);
                                                        if (i == (runningTest - 1)) {
                                                            pw.println();
                                                        }
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    progressValue = (int) (((i + 1.0) / runningTest) * (((inc + 1.0) / totalFile) * 100.0));
                                                    jProgressBar1.setValue(progressValue);
                                                }
                                                if (arrResult != null) {
                                                    temp = "outputfile/Distributed MergeSort/" + userInputSource[inc].toPath().getFileName().toString() + " size " + size + " host " + serverList.length;
                                                    outputSortFile = Paths.get(temp + ".txt");
                                                    if (Files.notExists(outputSortFile.getParent())) {
                                                        try {
                                                            Files.createDirectories(outputSortFile.getParent());
                                                        } catch (IOException ex) {
                                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    } else {
                                                        int counter = 1;
                                                        while (Files.exists(outputSortFile)) {
                                                            String atemp = temp + "(" + (counter++) + ").txt";
                                                            outputSortFile = Paths.get(atemp);
                                                        }
                                                    }
                                                    try (PrintWriter pw = new PrintWriter(Files.newOutputStream(outputSortFile), true)) {
                                                        for (int isi : arrResult) {
                                                            pw.println(isi);
                                                        }
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            } else {
                                                RMIClientLogged dmergesort;
                                                for (int i = 0; i < runningTest; i++) {
                                                    textFieldAntrianRunningR.setText(String.valueOf(i + 1));
                                                    dmergesort = new RMIClientLogged(arr, chunkSize, serverList, core, sorted, this, autosaveFile, option, autosave);
                                                    ElapsedTime = dmergesort.sort();
                                                    arrResult = dmergesort.getArr();
                                                    if (arrResult == null) {
                                                        JOptionPane.showMessageDialog(this, "Terdapat error dalam pendistribusian proses, proses dibatalkan !!!!");
                                                        arr = null;
                                                        break;
                                                    }
                                                    try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                        pw.println(ElapsedTime);
                                                        if (i == (runningTest - 1)) {
                                                            pw.println();
                                                        }
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    progressValue = (int) (((i + 1.0) / runningTest) * (((inc + 1.0) / totalFile) * 100.0));
                                                    jProgressBar1.setValue(progressValue);
                                                }
                                                if (arrResult != null) {
                                                    temp = "outputfile/Distributed MergeSort/" + userInputSource[inc].toPath().getFileName().toString() + " size " + size + " host " + serverList.length;
                                                    outputSortFile = Paths.get(temp + ".txt");
                                                    if (Files.notExists(outputSortFile.getParent())) {
                                                        try {
                                                            Files.createDirectories(outputSortFile.getParent());
                                                        } catch (IOException ex) {
                                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    } else {
                                                        int counter = 1;
                                                        while (Files.exists(outputSortFile)) {
                                                            String atemp = temp + "(" + (counter++) + ").txt";
                                                            outputSortFile = Paths.get(atemp);
                                                        }
                                                    }
                                                    try (PrintWriter pw = new PrintWriter(Files.newOutputStream(outputSortFile), true)) {
                                                        for (int isi : arrResult) {
                                                            pw.println(isi);
                                                        }
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(this, "Maaf File input " + userInputSource[inc] + " tidak valid sehingga tidak dapat diparse nilainya");
                                        }
                                        progressValue = (int) (((inc + 1.0) / totalFile) * 100.0);
                                        jProgressBar1.setValue((progressValue));
                                    }
                                    labelProgressBar.setText("Mengaktifkan ulang server...");
                                    for (containerHostConfig item : hostList) {
                                        try {
                                            Registry registry = LocateRegistry.getRegistry(item.getHostname());
                                            configuration reaktivasi = (configuration) registry.lookup("aktivasi");
                                            reaktivasi.aktivasiUlang();
                                        } catch (NotBoundException | RemoteException ex) {
                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                } else if (algoritma == Algoritma.PARALLEL_MERGESORT) {
                                    socketUdpListen.close();
                                    String temp = "Statistik Log/Log Parallel MergeSort(core " + core + " ,File " + userInputSource.length + " , loop " + runningTest + ")";
                                    statFile = Paths.get(temp + ".txt");
                                    if (Files.notExists(statFile.getParent())) {
                                        try {
                                            Files.createDirectory(statFile.getParent());
                                        } catch (IOException ex) {
                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        int counter = 1;
                                        while (Files.exists(statFile)) {
                                            String atemp = temp + " (" + (counter++) + ")";
                                            statFile = Paths.get(atemp + ".txt");
                                        }
                                    }
                                    int size, totalFile = userInputSource.length, progressValue;
                                    int[] arr;
                                    for (File item : userInputSource) {
                                        labelProgressBar.setText("Memproses file input : " + item.getName() + "...");
                                        size = FileToArray.convertToArr(item.toPath());
                                        if (size > 0) {
                                            textFieldArraySizeR.setText(String.valueOf(size));
                                            arr = FileToArray.getArr();
                                            double ElapsedTime;
                                            try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                pw.println(item.toPath().getFileName().toString() + " \"PARALLEL MERGESORT\" Size: " + size + " CORE : " + core);
                                            } catch (IOException ex) {
                                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            for (int i = 0; i < runningTest; i++) {
                                                textFieldAntrianRunningR.setText(String.valueOf(i + 1));
                                                if (option != LOGOPTION.NO_LOG) {
                                                    SwingWorker gui = new UILoggerClient(this, "Array diterima... memulai pengurutan Parallel", autosaveFile, autosave);
                                                    gui.execute();
                                                    if (option == LOGOPTION.DEBUG_LOG) {
                                                        gui = new UILoggerClient(this, arr, autosaveFile, autosave);
                                                        gui.execute();
                                                    }
                                                }
                                                if (i == runningTest - 1) {
                                                    long startTime = System.nanoTime();
                                                    ParallelMergeSort.sort(arr, core, sorted);
                                                    long endTime = System.nanoTime();
                                                    ElapsedTime = (endTime - startTime) / 1_000_000;
                                                } else {
                                                    int[] arrTemp = arr.clone();
                                                    long startTime = System.nanoTime();
                                                    ParallelMergeSort.sort(arrTemp, core, sorted);
                                                    long endTime = System.nanoTime();
                                                    ElapsedTime = (endTime - startTime) / 1_000_000;
                                                }
                                                if (option != LOGOPTION.NO_LOG) {
                                                    SwingWorker gui = new UILoggerClient(this, "Pengurutan Parallel Selesai.", autosaveFile, autosave);
                                                    gui.execute();
                                                    if (option == LOGOPTION.DEBUG_LOG) {
                                                        gui = new UILoggerClient(this, arr, autosaveFile, autosave);
                                                        gui.execute();
                                                    }
                                                    String temps = String.format("Elapsed Time : %f %n", ElapsedTime);
                                                    gui = new UILoggerClient(this, temps, autosaveFile, autosave);
                                                    gui.execute();
                                                }

                                                try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                    pw.println(ElapsedTime);
                                                    if (i == (runningTest - 1)) {
                                                        pw.println();
                                                    }
                                                } catch (IOException ex) {
                                                    Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                progressValue = (int) (((i + 1.0) / runningTest) * (((inc * 1.0) / totalFile) * 100.0));
                                                jProgressBar1.setValue(progressValue);
                                            }
                                            temp = "outputfile/Parallel MergeSort/" + item.toPath().getFileName().toString() + " size " + size + " core " + core;
                                            outputSortFile = Paths.get(temp + ".txt");
                                            if (Files.notExists(outputSortFile.getParent())) {
                                                try {
                                                    Files.createDirectories(outputSortFile.getParent());
                                                } catch (IOException ex) {
                                                    Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            } else {
                                                int counter = 1;
                                                while (Files.exists(outputSortFile)) {
                                                    String atemp = temp + "(" + (counter++) + ").txt";
                                                    outputSortFile = Paths.get(atemp);
                                                }
                                            }
                                            try (PrintWriter pw = new PrintWriter(Files.newOutputStream(outputSortFile), true)) {
                                                for (int isi : arr) {
                                                    pw.println(isi);
                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(this, "Maaf File input " + item + " tidak valid sehingga tidak dapat diparse nilainya");
                                        }
                                        inc++;
                                        progressValue = (int) (((inc + 1.0) / totalFile) * 100.0);
                                        jProgressBar1.setValue((progressValue));
                                    }
                                } else if (algoritma == Algoritma.SEQUENTIAL_MERGESORT) {
                                    socketUdpListen.close();
                                    String temp = "Statistik Log/Log Sequential MergeSort(File " + userInputSource.length + " , loop " + runningTest + ")";
                                    statFile = Paths.get(temp + ".txt");
                                    if (Files.notExists(statFile.getParent())) {
                                        try {
                                            Files.createDirectory(statFile.getParent());
                                        } catch (IOException ex) {
                                            Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        int counter = 1;
                                        while (Files.exists(statFile)) {
                                            String atemp = temp + " (" + (counter++) + ")";
                                            statFile = Paths.get(atemp + ".txt");
                                        }
                                    }
                                    int size, progressValue, totalFile = userInputSource.length;
                                    int[] arr;
                                    for (File item : userInputSource) {
                                        labelProgressBar.setText("Memproses file input : " + item.getName() + "...");
                                        size = FileToArray.convertToArr(item.toPath());
                                        if (size > 0) {
                                            textFieldArraySizeR.setText(String.valueOf(size));
                                            arr = FileToArray.getArr();
                                            double ElapsedTime = 0;
                                            try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                pw.println(item.toPath().getFileName().toString() + " \"SEQUENTIAL MERGESORT\" Size: " + size);
                                            } catch (IOException ex) {
                                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            for (int i = 0; i < runningTest; i++) {
                                                textFieldAntrianRunningR.setText(String.valueOf(i + 1));
                                                if (option != LOGOPTION.NO_LOG) {
                                                    SwingWorker gui = new UILoggerClient(this, "Array diterima... memulai Sequential", autosaveFile, autosave);
                                                    gui.execute();
                                                    if (option == LOGOPTION.DEBUG_LOG) {
                                                        gui = new UILoggerClient(this, arr, autosaveFile, autosave);
                                                        gui.execute();
                                                    }
                                                }
                                                if (i == runningTest - 1) {
                                                    long startTime = System.nanoTime();
                                                    SequentialMergeSort.sort(arr, sorted);
                                                    long endTime = System.nanoTime();
                                                    ElapsedTime = (endTime - startTime) / 1_000_000;
                                                } else {
                                                    int[] arrTemp = arr.clone();
                                                    long startTime = System.nanoTime();
                                                    SequentialMergeSort.sort(arrTemp, sorted);
                                                    long endTime = System.nanoTime();
                                                    ElapsedTime = (endTime - startTime) / 1_000_000;
                                                }
                                                if (option != LOGOPTION.NO_LOG) {
                                                    SwingWorker gui = new UILoggerClient(this, "Pengurutan Sequential Selesai.", autosaveFile, autosave);
                                                    gui.execute();
                                                    if (option == LOGOPTION.DEBUG_LOG) {
                                                        gui = new UILoggerClient(this, arr, autosaveFile, autosave);
                                                        gui.execute();
                                                    }
                                                    String temps = String.format("Elapsed Time : %f %n", ElapsedTime);
                                                    gui = new UILoggerClient(this, temps, autosaveFile, autosave);
                                                    gui.execute();
                                                }
                                                try (PrintWriter pw = new PrintWriter(Files.newOutputStream(statFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE), true)) {
                                                    pw.println(ElapsedTime);
                                                    if (i == (runningTest - 1)) {
                                                        pw.println();
                                                    }
                                                } catch (IOException ex) {
                                                    Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                progressValue = (int) (((i + 1.0) / runningTest) * (((inc * 1.0) / totalFile) * 100.0));
                                                jProgressBar1.setValue(progressValue);
                                            }
                                            temp = "outputfile/Sequential MergeSort/" + item.toPath().getFileName().toString() + " size " + size;
                                            outputSortFile = Paths.get(temp + ".txt");
                                            if (Files.notExists(outputSortFile.getParent())) {
                                                try {
                                                    Files.createDirectories(outputSortFile.getParent());
                                                } catch (IOException ex) {
                                                    Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            } else {
                                                int counter = 1;
                                                while (Files.exists(outputSortFile)) {
                                                    String atemp = temp + "(" + (counter++) + ").txt";
                                                    outputSortFile = Paths.get(atemp);
                                                }
                                            }
                                            try (PrintWriter pw = new PrintWriter(Files.newOutputStream(outputSortFile), true)) {
                                                for (int isi : arr) {
                                                    pw.println(isi);
                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(this, "Maaf File input " + item + " tidak valid sehingga tidak dapat diparse nilainya");
                                        }
                                        inc++;
                                        progressValue = (int) (((inc + 1.0) / totalFile) * 100.0);
                                        jProgressBar1.setValue((progressValue));
                                    }
                                }
                            }catch(OutOfMemoryError|ArrayIndexOutOfBoundsException outofmemory){
                                JOptionPane.showMessageDialog(this, "Memory pada client tidak cukup untuk menjalankan proses pada file : "+userInputSource[inc].getName());
                                buttonRestart.doClick();
                                return;
                            }
                            labelProgressBar.setText("Mengaktifasi ulang client...");
                            aktivasiUtility();
                            rgbing = new RGBLighting(jPanel6, new Color[][]{
                                {jPanel6.getBackground(),jPanel6.getForeground()},{new Color(199,21,33,100),Color.WHITE},{new Color(255,247,0,100),Color.WHITE},
                                {new Color(0,123,167,100),Color.WHITE},{new Color(127,0,255,100),Color.WHITE},
                            }, 1000);
                            rgbing.execute();
                            jPanel6.setVisible(true);
                        }
                    }
                }
        );     
        exec.shutdown();
    }//GEN-LAST:event_buttonStartMouseClicked

    private void buttonSimpanFileLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimpanFileLogActionPerformed
        // TODO add your handling code here:
        if(textFieldNamaFileLog.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Maaf nama file tidak boleh kosong, usahakan sesuai dengan penulisan yang didukung OS anda");
        }else{
            Path pt = Paths.get("UserSavedClientLog/" + textFieldNamaFileLog.getText() + ".txt");
            try {
                if (Files.notExists(pt.getParent())) {
                    Files.createDirectory(pt.getParent());
                }
                int counter = 1;
                while (Files.exists(pt)) {
                    pt = Paths.get("UserSavedClientLog/" + textFieldNamaFileLog.getText() + "" + (counter++) + ".txt");
                }

                PrintWriter pw = new PrintWriter(Files.newOutputStream(pt), true);
                pw.println(jTextArea1.getText());
                pw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "tidak dapat buat file", JOptionPane.ERROR_MESSAGE);
            }
        }        
    }//GEN-LAST:event_buttonSimpanFileLogActionPerformed

    private void buttonTampilkanServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTampilkanServerActionPerformed
        if(tampilkanServer==null)            
            tampilkanServer = new TampilkanServer(this, "Daftar server terkoneksi dan pengaturan core masing - masing client", hostList);
        if(!tampilkanServer.isVisible()){
            tampilkanServer.setVisible(true);
            workerScan = new ClientAsker(this, networkAddress, hostList);
            workerScan.execute();
        }else{
            tampilkanServer.toFront();
        }
    }//GEN-LAST:event_buttonTampilkanServerActionPerformed
    
    private void buttonScanServerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonScanServerMouseClicked
        // TODO add your handling code here:
        JButton button = (JButton)evt.getSource();
        if(button.getText().equals("SCAN")){
            workerScan = new ClientAsker(this, networkAddress, hostList);
            workerScan.execute();
            button.setText("CANCEL");
        }else if(button.getText().equals("CANCEL")){
            workerScan.cancel(true);
            button.setText("SCAN");
        }
    }//GEN-LAST:event_buttonScanServerMouseClicked

    private void textFieldCoreClientRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldCoreClientRMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            maxCore = 250;
            JOptionPane.showMessageDialog(this, "Sekarang max core dapat diset sampai " + maxCore,"Hidden Utility first in Client",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_textFieldCoreClientRMouseClicked

    private void textFieldChunkRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldChunkRMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            maxChunkSize = Integer.MAX_VALUE;
            JOptionPane.showMessageDialog(this, "Sekarang max chunk dapat diset sampai " + maxChunkSize,"Hidden Utility second in Client",JOptionPane.INFORMATION_MESSAGE);            
        }
    }//GEN-LAST:event_textFieldChunkRMouseClicked

    private void textFieldJmlRunningRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldJmlRunningRMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            maxRunTest = 1000;
            JOptionPane.showMessageDialog(this, "Sekarang max running test dapat diset sampai " + maxRunTest, "Hidden Utility third in Client", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_textFieldJmlRunningRMouseClicked

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        rgbing.cancel(false);
        rgbing = new RGBLighting(buttonRestart, new Color[][]{
            {buttonRestart.getBackground(),buttonRestart.getForeground()},{Color.RED,Color.WHITE},{buttonRestart.getBackground(),buttonRestart.getForeground()},
            {Color.YELLOW,Color.BLACK}
        }, 1000);
        rgbing.execute();
        for(MouseListener item : jPanel6.getMouseListeners()){
            jPanel6.removeMouseListener(item);
        }
    }//GEN-LAST:event_jPanel6MouseEntered

    TampilanPanduan formPanduan = null;
    private void buttonHelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonHelpMouseClicked
        // TODO add your handling code here:
        if(formPanduan == null||!formPanduan.isDisplayable()){
            formPanduan = new TampilanPanduan(TampilanPanduan.panduan.client);
            formPanduan.setVisible(true);
        }        
    }//GEN-LAST:event_buttonHelpMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonPilihAlgoritma;
    private javax.swing.JRadioButton RadioButtonDebugLog;
    private javax.swing.JButton buttonGenerator;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonHasilSorting;
    public javax.swing.JButton buttonHelp;
    private javax.swing.JButton buttonPilihFileInput;
    private javax.swing.JButton buttonReady;
    private javax.swing.JButton buttonRestart;
    private javax.swing.JButton buttonSavedLog;
    public javax.swing.JButton buttonScanServer;
    private javax.swing.JButton buttonSetChunk;
    private javax.swing.JButton buttonSetCoreClient;
    private javax.swing.JButton buttonSetRunning;
    private javax.swing.JButton buttonSimpanFileLog;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonStatistik;
    private javax.swing.JButton buttonTampilkanServer;
    private javax.swing.JCheckBox checkBoxAutoSave;
    private javax.swing.JComboBox<String> comboBoxAlgoritma;
    private javax.swing.JComboBox<String> comboBoxUrutan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelProgressBar;
    private javax.swing.JRadioButton radioButtonNoLog;
    private javax.swing.JRadioButton radioButtonNormalLog;
    private javax.swing.JTextField textFieldAntrianRunningR;
    private javax.swing.JTextField textFieldArraySizeR;
    private javax.swing.JTextField textFieldChunkR;
    private javax.swing.JTextField textFieldCoreClientR;
    private javax.swing.JTextField textFieldJmlClientR;
    private javax.swing.JTextField textFieldJmlFileInputR;
    private javax.swing.JTextField textFieldJmlGenerator;
    private javax.swing.JTextField textFieldJmlRunningR;
    private javax.swing.JTextField textFieldNamaFileLog;
    private javax.swing.JTextField textFieldOutputFileGenerator;
    private javax.swing.JTextField textFieldRangeGenerator;
    private javax.swing.JTextField textFieldSetChunk;
    private javax.swing.JTextField textFieldSetCoreClient;
    private javax.swing.JTextField textFieldSetJmlRunning;
    private javax.swing.JTextField textFieldStatusAlgoritmaR;
    private javax.swing.JTextField textFieldStatusUrutanR;
    // End of variables declaration//GEN-END:variables
}
