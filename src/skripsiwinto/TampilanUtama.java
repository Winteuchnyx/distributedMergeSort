/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import skripsiwinto.probingip.ClientAnswerer;
import skripsiwinto.probingip.IpProbing;

import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.LinkedList;
import skripsiwinto.probingip.IpResponding;
/**
 *
 * @author Winto Junior Khosasi
 */
public class TampilanUtama extends javax.swing.JFrame {
    
    protected String networkAddress = null;
    protected String hostName = null;
    public static byte defaultCore = 2;
    
    private boolean client = false;
    
    private final LinkedList<String> hostList = new LinkedList<String>();
    /**
     * Creates new form TampilanUtama
     */
    public TampilanUtama() {
        initComponents();
        this.getContentPane().setBackground(new Color(0, 200, 0));
        jPanel1.setBackground(new Color(0,200,0));
        gambarLabel.setIcon(new StretchIcon(getClass().getResource("/Logo USU FASILKOMTI.png")));
        statusLabel.setText("Pilih komputer Host ini sebagai Client atau Server : ");
        cancelButton.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        gambarLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        clientButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        serverButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        buttonHelp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pilih Mode Module");
        setAlwaysOnTop(true);
        setName("MenuUtama"); // NOI18N
        setSize(new java.awt.Dimension(900, 900));
        setType(java.awt.Window.Type.UTILITY);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PEMBANGUNAN APLIKASI DESKTOP UNTUK MENGIMPLEMENTASI ");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" ALGORITMA MERGE SORT PADA SISTEM TERDISTRIBUSI");
        jLabel2.setAlignmentY(0.0F);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel3.setText("  DALAM PROSES PENGURUTAN ANGKA");

        gambarLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo USU FASILKOMTI.png"))); // NOI18N
        gambarLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gambarLabelMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setText("Oleh");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setText("Winto Junior Khosasih");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setText("161401093");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setText("Program Studi Ilmu Komputer");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setText("FAKULTAS ILMU KOMPUTER DAN TEKNOLOGI INFORMASI");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel10.setText("UNIVERSITAS SUMATERA UTARA");

        clientButton.setBackground(new java.awt.Color(0, 128, 0));
        clientButton.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        clientButton.setForeground(new java.awt.Color(204, 204, 204));
        clientButton.setText("CLIENT");
        clientButton.setFocusPainted(false);
        clientButton.setMaximumSize(new java.awt.Dimension(120, 50));
        clientButton.setMinimumSize(new java.awt.Dimension(100, 35));
        clientButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientButtonMouseClicked(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(0, 128, 0));
        cancelButton.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        cancelButton.setForeground(new java.awt.Color(204, 204, 204));
        cancelButton.setText("CANCEL");
        cancelButton.setMaximumSize(new java.awt.Dimension(120, 50));
        cancelButton.setMinimumSize(new java.awt.Dimension(100, 35));
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });

        serverButton.setBackground(new java.awt.Color(0, 128, 0));
        serverButton.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
        serverButton.setForeground(new java.awt.Color(204, 204, 204));
        serverButton.setText("SERVER");
        serverButton.setMaximumSize(new java.awt.Dimension(120, 50));
        serverButton.setMinimumSize(new java.awt.Dimension(100, 35));
        serverButton.setName(""); // NOI18N
        serverButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serverButtonMouseClicked(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        statusLabel.setText("Status : ");
        statusLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statusLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(clientButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(serverButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(statusLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(clientButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serverButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        buttonHelp.setIcon(new StretchIcon(getClass().getResource("/help-icon-17001.png")));
        buttonHelp.setBorderPainted(false);
        buttonHelp.setContentAreaFilled(false);
        buttonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHelpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(gambarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(gambarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serverButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serverButtonMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() ==1){
            /*TampilanClient form2 = new TampilanClient();
            form2.setVisible(true);
            this.setVisible(false);*/     
            
            client = false;
            serverButton.setVisible(false);
            clientButton.setVisible(false);
            cancelButton.setVisible(true);
            
            try {
                socketUdp = new DatagramSocket(9999);
                UIBackgroundProcess = new IpResponding(this, socketUdp);
                UIBackgroundProcess.execute();
            } catch (SocketException ex) {
                JOptionPane.showConfirmDialog(this, "Maaf terdapat aplikasi yang menggunakan port 9999, mohon matikan aplikasi terlebih dahulu", "Terdeteksi server lain", 
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
                Logger.getLogger(TampilanUtama.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_serverButtonMouseClicked

    private void statusLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusLabelMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2 && !evt.isConsumed()){
            evt.consume();
            JDialog util = new HiddenUtility(this,"First Hidden Utility");
            util.setVisible(true);
        }
    }//GEN-LAST:event_statusLabelMouseClicked
    
    SwingWorker<String, String> UIBackgroundProcess;
    DatagramSocket socketUdp;
    private void cancelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMouseClicked
        // TODO add your handling code here:
        if(client){
            UIBackgroundProcess.cancel(true);
        }else{
            UIBackgroundProcess.cancel(true);            
            socketUdp.close();
        }
        serverButton.setVisible(true);
        clientButton.setVisible(true);
        cancelButton.setVisible(false);
        statusLabel.setText("Pilih komputer Host ini sebagai Client atau Server : ");
    }//GEN-LAST:event_cancelButtonMouseClicked

    private void clientButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientButtonMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==1){
            client = true;
            serverButton.setVisible(false);
            clientButton.setVisible(false);
            cancelButton.setVisible(true);
            
            UIBackgroundProcess = new IpProbing(this,hostList,this.networkAddress,defaultCore);
            UIBackgroundProcess.execute();
        }
    }//GEN-LAST:event_clientButtonMouseClicked

    private void gambarLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gambarLabelMouseClicked
        if(evt.getClickCount()==2 && !evt.isConsumed()){
            evt.consume();
            Path path = Paths.get("README.txt");
            if(Files.exists(path)){
                try{
                    ProcessBuilder pb = new ProcessBuilder("notepad ",path.toString());
                    pb.start();
                } catch (IOException ex) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("open", "-t", path.toString());
                        pb.start();
                    } catch (IOException ex1) {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("xdg-open", "-t", path.toString());
                            pb.start();
                        } catch (IOException ex2) {
                            try {
                                ProcessBuilder pb = new ProcessBuilder("xdg-utils", "-t", path.toString());
                                pb.start();
                            } catch (IOException ex3) {
                                Logger.getLogger(TampilanClient.class.getName()).log(Level.SEVERE, null, ex1);
                                JOptionPane.showMessageDialog(this, "Maaf tidak terdapat text editor yang dapat dipakai aplikasi ini pada OS anda");
                            }
                        }
                    }
                }
            }else{
                try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path, StandardOpenOption.CREATE),true)){
                    pw.format("Aplikasi terdiri dari dua modul yakni Server dan Client : %n"+
                            "Client : %n"+"Client terdiri dari 4 tahap yakni : %n"+"1.Memilih Algoritma%n2.Konfigurasi parameter awal yang diperlukan sesuai algoritma terpilih%n"+
                            "3.Pengaturan pelacakkan history proses(Logging) sebelum memulai proses%n4.Memulai proses%n5.Menampilkan Hasil Proses%n"+
                            "Client dapat membangkitkan / membuat file random untuk disorting yang memiliki parameter / isian sebagai berikut : %n"+
                            "1.Jumlah Bilangan : menentukan banyak array, key atau angka yang mau diurutkan%n2.Jangkauan : menentukan variasi key atau angka antara 0 - nilai isian.%n"+
                            "3.Nama File : menentukan nama file hasil generate / pembangkitan untuk disimpan dalam Disk Drive.%n"+"Berikut petunjuk penggunaan aplikasi berdasarkan algoritma masing - masing : %n"+
                            "-.Algoritma Sequential MergeSort : %nParameter yang diperlukan / dapat diisi : %n"+"1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.%n"+
                            "2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).%n"+"-.Algoritma Parallel MergeSort : %nParameter yang diperlukan / dapat diisi : %n"+
                            "1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.%n"+
                            "2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).%n"+
                            "3.Core Client : Jumlah execution core / prosesor komputer yang dipakai dalam eksekusi Parallel oleh algoritma MergeSort%n"+"-.Algoritma Distributed MergeSort : %nParameter yang diperlukan / dapat diisi : %n"+
                            "1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.%n"+
                            "2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).%n"+
                            "3.Core Client : Jumlah execution Core / prosesor komputer yang dipakai oleh client dalam mengerjakan hasil proses dari berbagai server.%n"+
                            "4.ChunkSize : Jumlah maksimal array yang dikirim ke server.%n"+
                            "5.Server terdeteksi : dengan tombol scan akan merefresh / menyegarkan informasi tentang server yang masih terhubung dengan client dalam sistem.%n"+
                            "6.Tampilkan & set Server : menampilkan IP server - server yang terhubung dengan sistem dengan jumlah core yang terset masing - masing server, disini dapat juga mengubah jumlah core server - server tsb.%n"+
                            "%n%n Server : %n"+"Server hanya dapat menjalankan proses yang diperintahkan oleh client, pada server hanya dapat melihat / membaca parameter dan proses yang diperintahkan oleh client.%n"+
                            "Pada server hanya dapat buat log atau melakukan track log selama client belum melakukan lock atau kunci resource server (ditandai dengan tombol log terdisable).%n"+
                            "%n%nLogging / melakukan riwayat proses : %n"+"Tujuan logging tidak diatur sebagai pengaturan default / bawaan adalah pada sistem terdistribusi akan berefek menjadi lambat.%n"+
                            "Logging memerlukan resource I/O yang mana akses ke disk adalah akses paling lambat, supaya log dapat di tampilkan maka harus dilakukan bersamaan dengan proses maupun sebelum dan sesudah proses, %n"+
                            "yang mana elapsed time / waktu eksekusi akan terhitung bersamaan dengan Logging dan melencengkan keakuratan penelitian.%n Logging memiliki tiga kriteria : %n"+
                            "1.No log : tidak melakukan log sama sekali.(pengaturan khusus untuk Distributed MergeSort).%n"+
                            "2.Normal Log : melakukan logging minimum, dengan memberitahu kapan proses diterima , di jalankan dan dilepaskan (selesai).%n"+
                            "3.Debug Log : melakukan Logging seperti normal Log tetapi dengan tambahan menampilkan array atau key - key yang diterima, baik sebelum proses maupun sesudah proses.(dapat menyebabkan clutter).%n"+
                            "NB : Logging hanya mempengaruhi waktu eksekusi algoritma distributed MergeSort. Jadi saat penelitian disarankan untuk tidak melakukan LOG.%n"+
                            "Checkbox \"Auto Save Log\" : akan menyimpan log yang tertulis pada textArea saat sesi berlangsung kedalam folder : %n"+
                            "apabila untuk client : folder \"Log Client (autosave)\"%napabila untuk server : folder \"Log Server (autosave)\".%n"+
                            "%n%nHasil proses : %n"+"1.buka stat : Menampilkan waktu - waktu eksekusi (berdasarkan jumlah loop running test) perFile berdasarkan algoritma yang digunakan.%n"+
                            "2.buka hasil sort : memilih file hasil sortir proses pengurutan, format file adalah :  %n"+
                            "untuk sequential merge sort : [nama input file] [size],%n untuk parallel merge sort : [nama input file] [size] [core],%n untuk distributed merge sort : [nama input file] [size] [host].%n"+
                            "NB : dalam file, apabila terdapat nama yang sama maka akan diberi \"(counter)\" pada akhir nama file, nilai counter tertinggi adalah file paling update.%n"+
                            "%n%nFolder - folder yang akan dicreate Oleh aplikasi : %n"+
                            "1.outputfile : terdiri dari tiga sub folder masing - masing sesuai algoritma yang terpilih (sequential merge sort, parallel merge sort dan distributed merge sort),%n"+
                            "didalam sub folder akan berisi file hasil pengurutan masing - masing algoritma.%n"+
                            "2.Generated Input File : merupakan default folder yang dibuka ketika memilih file input dan juga merupakan folder tempat hasil generate / membangkitkan file bilangan acak disimpan.%n"+
                            "3.Statistik Log : merupakan folder tempat menyimpan data statistik waktu eksekusi hasil proses berdasarkan algoritma yang terpilih.%n"+
                            "4.Log Client (autosave) : folder tempat menyimpan file hasil capture logging pada text area oleh program client.%n"+
                            "5.Log Server (autosave) : folder tempat menyimpan file hasil capture logging pada text area oleh program server.%n"+
                            "%n%nHidden Folder : %n"+"1.UserSavedClientLog : folder tempat menyimpan file hasil capture bagian sesi text area di program client manual oleh user.%n"+
                            "1.UserSavedServerLog : folder tempat menyimpan file hasil capture bagian sesi text area di program Server manual oleh user.%n"+
                            "%n%nHidden Utility (pengaturan yang digunakan saat testing oleh programmer, tidak diperutukkan oleh user!!!) : %n"+
                            "main: %n"
                            + "double klik status label%n"
                            + "double klik logo USU%n"
                            + "%n"
                            + "server:%n"
                            + "double klik text field client terdeteksi%n"
                            + "double klik text field core terpakai%n"
                            + "double klik text field hostname RMI%n"
                            + "double klik label \"Buat Log\"%n"
                            + "%n"
                            + "client%n"
                            + "double klik status core%n"
                            + "double klik status running test%n"
                            + "double klik status chunksize%n"
                            + "double klik label hasil proses");
                } catch (IOException ex) {
                    Logger.getLogger(TampilanUtama.class.getName()).log(Level.SEVERE, null, ex);
                }
                try{
                    ProcessBuilder pb = new ProcessBuilder("notepad ",path.toString());
                    pb.start();
                } catch (IOException ex) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("xdg-utils", path.toString());
                        pb.start();
                    } catch (IOException ex1) {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("xdg-open", path.toString());
                            pb.start();
                        } catch (IOException ex2) {
                            try {
                                ProcessBuilder pb = new ProcessBuilder("open", "-t", path.toString());
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
    }//GEN-LAST:event_gambarLabelMouseClicked
    
    public TampilanPanduan panduan = null;
    private void buttonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHelpActionPerformed
        // TODO add your handling code here:
        if(panduan == null||!panduan.isDisplayable()){
            panduan = new TampilanPanduan(TampilanPanduan.panduan.utama);
            panduan.setVisible(true);
        }
    }//GEN-LAST:event_buttonHelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TampilanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TampilanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TampilanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TampilanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TampilanUtama().setVisible(true);
            }
        });
    }
    
    public void setNetworkAddress(String networkAddress){
        this.networkAddress = networkAddress;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonHelp;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clientButton;
    private javax.swing.JLabel gambarLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton serverButton;
    public javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}