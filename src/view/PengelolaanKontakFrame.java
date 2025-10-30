/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author rasyi
 */

import controller.KontakController; 
import java.io.*; 
import model.Kontak; 
 
import javax.swing.*; 
import javax.swing.table.DefaultTableModel; 
import java.sql.SQLException; 
import java.util.List; 
import java.util.logging.Level; 
import java.util.logging.Logger; 

public class PengelolaanKontakFrame extends javax.swing.JFrame {
    private DefaultTableModel model; 
    private KontakController controller; 
    /**
     * Creates new form PengelolaanKontakFrame
     */
    public PengelolaanKontakFrame() {
        initComponents();
        
        controller = new KontakController(); 
        model = new DefaultTableModel(new String[] 
                {"No", "Nama", "Nomor Telepon", "Kategori"}, 0); 
        tblKontak.setModel(model); 
        
        loadContacts(); 
    }


    private void loadContacts() { 
        try { 
            model.setRowCount(0); 
            List<Kontak> contacts = controller.getAllContacts(); 

            int rowNumber = 1; 
            for (Kontak contact : contacts) { 
                model.addRow(new Object[]{ 
                        rowNumber++, 
                        contact.getNama(), 
                        contact.getNomorTelepon(), 
                        contact.getKategori() 
                }); 
            } 
        } catch (SQLException e) { 
            showError(e.getMessage()); 
        } 
    } 

    private void showError(String message) { 
        JOptionPane.showMessageDialog(this, message, "Error", 
    JOptionPane.ERROR_MESSAGE); 
} 
    private void addContact() {       
    String nama = txtNama.getText().trim(); 
    String nomorTelepon = txtNomorTelepon.getText().trim(); 
    String kategori = (String) cmbKategori.getSelectedItem(); 
    
    if (!validatePhoneNumber(nomorTelepon)) { 
        return; // Validasi nomor telepon gagal 
    } 
    
    try { 
        if (controller.isDuplicatePhoneNumber(nomorTelepon, null)) { 
            JOptionPane.showMessageDialog(
                this, 
                "Kontak nomor telepon ini sudah ada.", 
                "Kesalahan", 
                JOptionPane.WARNING_MESSAGE
            );
            return; 
        } 
 
        controller.addContact(nama, nomorTelepon, kategori); 
        loadContacts(); 
        JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan!"); 
        clearInputFields(); 

    } catch (SQLException ex) { 
        showError("Gagal menambahkan kontak: " + ex.getMessage()); 
    } 
} 

    private boolean validatePhoneNumber(String phoneNumber) { 
        if (phoneNumber == null || phoneNumber.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Nomor telepon tidak boleh kosong."); 
            return false; 
        } 

        if (!phoneNumber.matches("\\d+")) { // Hanya angka 
            JOptionPane.showMessageDialog(this, "Nomor telepon hanya boleh berisi angka."); 
            return false; 
        } 

        if (phoneNumber.length() < 8 || phoneNumber.length() > 15) { // Panjang 8–15 
            JOptionPane.showMessageDialog(this, "Nomor telepon harus memiliki panjang antara 8 hingga 15 karakter."); 
            return false; 
        } 

        return true; 
    } 

    private void clearInputFields() { 
        txtNama.setText(""); 
        txtNomorTelepon.setText(""); 
        cmbKategori.setSelectedIndex(0); 
    }
    
     
private void editContact() { 
    int selectedRow = tblKontak.getSelectedRow(); 
    if (selectedRow == -1) { 
        JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin diperbarui.", "Kesalahan", JOptionPane.WARNING_MESSAGE); 
        return; 
    } 
 
    int id = (int) model.getValueAt(selectedRow, 0); 
    String nama = txtNama.getText().trim(); 
    String nomorTelepon = txtNomorTelepon.getText().trim(); 
    String kategori = (String) cmbKategori.getSelectedItem(); 
 
    if (!validatePhoneNumber(nomorTelepon)) { 
        return; 
    } 
 
    try { 
        if (controller.isDuplicatePhoneNumber(nomorTelepon, id)) { 
            JOptionPane.showMessageDialog(this, "Kontak nomor telepon ini sudah ada.", "Kesalahan", JOptionPane.WARNING_MESSAGE); 
            return; 
        } 
 
        controller.updateContact(id, nama, nomorTelepon, kategori); 
        loadContacts(); 
        JOptionPane.showMessageDialog(this, "Kontak berhasil diperbarui!"); 
        clearInputFields(); 
    } catch (SQLException ex) { 
        showError("Gagal memperbarui kontak: " + ex.getMessage()); 
    } 
} 
 
private void populateInputFields(int selectedRow) { 
    // Ambil data dari JTable 
    String nama = model.getValueAt(selectedRow, 1).toString(); 
    String nomorTelepon = model.getValueAt(selectedRow, 2).toString(); 
    String kategori = model.getValueAt(selectedRow, 3).toString(); 
 
    // Set data ke komponen input 
    txtNama.setText(nama); 
    txtNomorTelepon.setText(nomorTelepon); 
    cmbKategori.setSelectedItem(kategori); 
} 

private void deleteContact() { 
    int selectedRow = tblKontak.getSelectedRow(); 
    if (selectedRow != -1) { 
        int id = (int) model.getValueAt(selectedRow, 0); 
        try { 
            controller.deleteContact(id); 
            loadContacts(); 
            JOptionPane.showMessageDialog(this, "Kontak berhasil dihapus!"); 
            clearInputFields(); 
        } catch (SQLException e) { 
            showError(e.getMessage()); 
        } 
    } 
}

private void searchContact() { 
    String keyword = txtPencarian.getText().trim(); 
    if (!keyword.isEmpty()) { 
        try { 
            List<Kontak> contacts = controller.searchContacts(keyword); 
            model.setRowCount(0); // Bersihkan tabel 
            for (Kontak contact : contacts) { 
                model.addRow(new Object[]{ 
                        contact.getId(), 
                        contact.getNama(), 
                        contact.getNomorTelepon(), 
                        contact.getKategori() 
                }); 
            } 
            if (contacts.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Tidak ada kontak ditemukan."); 
            } 
        } catch (SQLException ex) { 
            showError(ex.getMessage()); 
        } 
    } else { 
        loadContacts(); 
    } 
}
 
private void exportToCSV() { 
    JFileChooser fileChooser = new JFileChooser(); 
    fileChooser.setDialogTitle("Simpan File CSV"); 
    int userSelection = fileChooser.showSaveDialog(this); 

    if (userSelection == JFileChooser.APPROVE_OPTION) { 
        File fileToSave = fileChooser.getSelectedFile(); 

        // Tambahkan ekstensi .csv jika pengguna tidak menambahkannya 
        if (!fileToSave.getAbsolutePath().endsWith(".csv")) { 
            fileToSave = new File(fileToSave.getAbsolutePath() + ".csv"); 
        } 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) { 
            writer.write("ID,Nama,Nomor Telepon,Kategori\n"); // Header CSV 
            
            for (int i = 0; i < model.getRowCount(); i++) { 
                writer.write( 
                    model.getValueAt(i, 0) + "," + 
                    model.getValueAt(i, 1) + "," + 
                    model.getValueAt(i, 2) + "," + 
                    model.getValueAt(i, 3) + "\n"
                ); 
            } 

            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke " + fileToSave.getAbsolutePath()); 
        } catch (IOException ex) { 
            showError("Gagal menulis file: " + ex.getMessage()); 
        } 
    } 
} 

private void importFromCSV() { 
    showCSVGuide(); 

    int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Apakah Anda yakin file CSV yang dipilih sudah sesuai dengan format?", 
        "Konfirmasi Impor CSV", 
        JOptionPane.YES_NO_OPTION
    ); 

    if (confirm == JOptionPane.YES_OPTION) { 
        JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("Pilih File CSV"); 
        int userSelection = fileChooser.showOpenDialog(this); 

        if (userSelection == JFileChooser.APPROVE_OPTION) { 
            File fileToOpen = fileChooser.getSelectedFile(); 

            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) { 
                String line = reader.readLine(); // Baca header 

                if (!validateCSVHeader(line)) { 
                    JOptionPane.showMessageDialog(
                        this, 
                        "Format header CSV tidak valid. Pastikan header adalah: ID,Nama,Nomor Telepon,Kategori", 
                        "Kesalahan CSV", 
                        JOptionPane.ERROR_MESSAGE
                    ); 
                    return; 
                } 

                int rowCount = 0; 
                int errorCount = 0; 
                int duplicateCount = 0; 
                StringBuilder errorLog = new StringBuilder("Baris dengan kesalahan:\n"); 

                while ((line = reader.readLine()) != null) { 
                    rowCount++; 
                    String[] data = line.split(","); 

                    if (data.length != 4) { 
                        errorCount++; 
                        errorLog.append("Baris ").append(rowCount + 1).append(": Format kolom tidak sesuai.\n"); 
                        continue; 
                    } 

                    String nama = data[1].trim(); 
                    String nomorTelepon = data[2].trim(); 
                    String kategori = data[3].trim(); 

                    if (nama.isEmpty() || nomorTelepon.isEmpty()) { 
                        errorCount++; 
                        errorLog.append("Baris ").append(rowCount + 1).append(": Nama atau Nomor Telepon kosong.\n"); 
                        continue; 
                    } 

                    if (!validatePhoneNumber(nomorTelepon)) { 
                        errorCount++; 
                        errorLog.append("Baris ").append(rowCount + 1).append(": Nomor Telepon tidak valid.\n"); 
                        continue; 
                    } 

                    try { 
                        if (controller.isDuplicatePhoneNumber(nomorTelepon, null)) { 
                            duplicateCount++; 
                            errorLog.append("Baris ").append(rowCount + 1).append(": Kontak sudah ada.\n"); 
                            continue; 
                        } 
                    } catch (SQLException ex) { 
                        Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(Level.SEVERE, null, ex); 
                    } 

                    try { 
                        controller.addContact(nama, nomorTelepon, kategori); 
                    } catch (SQLException ex) { 
                        errorCount++; 
                        errorLog.append("Baris ").append(rowCount + 1)
                            .append(": Gagal menyimpan ke database - ")
                            .append(ex.getMessage()).append("\n"); 
                    } 
                } 

                loadContacts(); 

                if (errorCount > 0 || duplicateCount > 0) { 
                    errorLog.append("\nTotal baris dengan kesalahan: ").append(errorCount).append("\n"); 
                    errorLog.append("Total baris duplikat: ").append(duplicateCount).append("\n"); 
                    JOptionPane.showMessageDialog(this, errorLog.toString(), "Kesalahan Impor", JOptionPane.WARNING_MESSAGE); 
                } else { 
                    JOptionPane.showMessageDialog(this, "Semua data berhasil diimpor."); 
                } 
            } catch (IOException ex) { 
                showError("Gagal membaca file: " + ex.getMessage()); 
            } 
        } 
    } 
} 

private void showCSVGuide() { 
    String guideMessage = 
        "Format CSV untuk impor data:\n" + 
        "- Header wajib: ID, Nama, Nomor Telepon, Kategori\n" + 
        "- ID dapat kosong (akan diisi otomatis)\n" + 
        "- Nama dan Nomor Telepon wajib diisi\n" + 
        "- Contoh isi file CSV:\n" + 
        "  1, Andi, 08123456789, Teman\n" + 
        "  2, Budi Doremi, 08567890123, Keluarga\n\n" + 
        "Pastikan file CSV sesuai format sebelum melakukan impor."; 

    JOptionPane.showMessageDialog(this, guideMessage, "Panduan Format CSV", JOptionPane.INFORMATION_MESSAGE); 
} 

private boolean validateCSVHeader(String header) { 
    return header != null && header.trim().equalsIgnoreCase("ID,Nama,Nomor Telepon,Kategori"); 
} 



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtNomorTelepon = new javax.swing.JTextField();
        cmbKategori = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtPencarian = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKontak = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 19)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("APLIKASI PENGELOLAAN KONTAK");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(jLabel1)
                    .addContainerGap(88, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addComponent(jLabel1)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel2.setText("Nama Kontak");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel3.setText("No Telepon");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel4.setText("Kategori");

        txtNama.setToolTipText("Masukkan Nama ");

        txtNomorTelepon.setToolTipText("Masukkan No Telepon!!!");

        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keluarga", "Teman", "Kantor", " " }));

        jButton1.setBackground(new java.awt.Color(0, 204, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel5.setText("Pencarian");

        txtPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPencarianKeyTyped(evt);
            }
        });

        tblKontak.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblKontak.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKontakMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKontak);

        jButton4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton4.setForeground(java.awt.Color.black);
        jButton4.setText("Export");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton5.setForeground(java.awt.Color.black);
        jButton5.setText("Import");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(44, 44, 44))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNomorTelepon)
                            .addComponent(cmbKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtPencarian))))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addGap(55, 55, 55))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomorTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         addContact(); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        editContact(); 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblKontakMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKontakMouseClicked
        // TODO add your handling code here:
        int selectedRow = tblKontak.getSelectedRow(); 
        if (selectedRow != -1) { 
            populateInputFields(selectedRow); 
        } 
    }//GEN-LAST:event_tblKontakMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        deleteContact(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtPencarianKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPencarianKeyTyped
        // TODO add your handling code here:
        searchContact();
    }//GEN-LAST:event_txtPencarianKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        exportToCSV();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        importFromCSV(); 
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PengelolaanKontakFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PengelolaanKontakFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKontak;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNomorTelepon;
    private javax.swing.JTextField txtPencarian;
    // End of variables declaration//GEN-END:variables
}
