
import java.awt.Image;
import java.util.List;
import java.io.File;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alisherka7
 * @github Alisherka7
 * @web alisherka.me
 */
public final class captcha extends javax.swing.JFrame {

    public int pos=0;
    public String Absolute_Path = null;//"C:\\Users\\PC\\Documents\\data_test";
    public String SingleImageName = null;
    public String SingleImageParent = null;
    public String IncorrectSingleImage = null;
    public String ImgPath = null;//Absolute_Path + File.separatorChar + "images";
    public String IncorrectPath = null;// Absolute_Path  + File.separatorChar +  "/incorrect";
    public String LblPath = null; //Absolute_Path + File.separatorChar + "/labeled";
    public List<String> imagesList = null; //getFilesList(ImgPath, ".jpg");
    public List<String> incorrectList = null;
    public List<String> labelsList = null; // getFilesList(LblPath, ".txt"); 
    public List<String> GlobalListName = null; //getFilesList(LblPath, "=");
    public List<String> Files_List = null;
    boolean OnOff = true;
    
    
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
    
    private List<String> getFilesList(String root, String type){
        File folder = new File(root);
        File[] listOfFiles = folder.listFiles(); 
        List<String> result = new ArrayList<>();
        for(int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                String ext = getFileExtension(listOfFiles[i]);
                if (ext.equals(type)){
                    result.add(filename);
                } 
                else if(type.equals("=")){
                    result.add(filename.substring(0,(filename.length()-4)));
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return result;
    }
   
    public captcha() {

        initComponents(); 
        jButton_openfile.setEnabled(false);
        setResizable(false);
    }
    
    public void start(){
        pos=0;
//        Absolute_Path = "C:\\data_test";//"C:\\Users\\PC\\Documents\\data_test";
        ImgPath = Absolute_Path + File.separatorChar + "images";
        IncorrectPath = Absolute_Path  + File.separatorChar +  "/incorrect";
        LblPath = Absolute_Path + File.separatorChar + "/labeled";
        
        imagesList = getFilesList(ImgPath, ".jpg"); //photo100.jpg
        incorrectList = getFilesList(IncorrectPath, ".jpg");
        labelsList = getFilesList(LblPath, ".txt"); //photo100.txt
        GlobalListName = getFilesList(LblPath, "=");//photo100
        Files_List = imagesList;
    }

   
    public void copyImage() throws IOException{        
//        String src_image = ImgPath + File.separatorChar + imagesList.get(pos);//GlobalListName[pos];
//        String dst_image = IncorrectPath + File.separatorChar + src_image;
        File f1 = new File(ImgPath, imagesList.get(pos));
        File f2 = new File(IncorrectPath, imagesList.get(pos));
        FileInputStream fis = new FileInputStream(f1);
        FileOutputStream fos = new FileOutputStream(f2);
        int c = fis.read();
        while(c!=-1){
            fos.write(c);
            c=fis.read();
        }
        System.out.println("Incorrect Image copied!");
    }
    public void getImagesList(List Files_List){
        int num = Files_List.size();
        jLabel_Photos.setText(num+""); 
        DefaultTableModel model = (DefaultTableModel)jTable_img.getModel();
        model.setRowCount(0);
        System.out.println(Files_List);
        model.setColumnIdentifiers(new String[]{"Images Names"});
        Object[] row = new Object[1];
        for (int i=0;i<Files_List.size();i++) {
            row[0] = Files_List.get(i);
            model.addRow(row);
        }  
        
    }
    
    private void showLabel(String name) {  
        String labelFilename = LblPath + File.separatorChar + name + ".txt";
        File f = new File(labelFilename);
        try
        {
            Scanner label_txt = new Scanner(f);
            String[] txt = label_txt.nextLine().split(" ");
            jLabel_result.setText(txt[2]);
        }
        catch(FileNotFoundException ex) {
            jLabel_result.setText("!!!!!!");
        }
        
    }
   
    public void showImage(int index) {
        String imagePath = ImgPath + File.separatorChar + imagesList.get(index);   
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(Paths.get(imagePath).toUri().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(captcha.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (icon != null)
        {
            Image image = icon.getImage().getScaledInstance(410, 147, Image.SCALE_DEFAULT);
            jLabel_Image.setIcon(new ImageIcon(image));
            String imageName = imagesList.get(index);
            String[] temp = imageName.split("\\."); // photo100_150.jpg -> photo100_150  jpg
            String p = (temp.length==2) ? temp[0] : null;
            
            if (p!=null) {
                showLabel(p);
      
            }
            else{
                System.out.println("P is null!!!!");
            }
        }
    }
    public void OneImageShow(){
        String ImagePath = SingleImageParent + File.separatorChar + SingleImageName; 
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(Paths.get(ImagePath).toUri().toURL());
        } catch (MalformedURLException ex) {
            System.out.println("Icon not found");
        }
        if (icon != null)
        {
            Image image = icon.getImage().getScaledInstance(410, 147, Image.SCALE_DEFAULT);
            jLabel_Image.setIcon(new ImageIcon(image));
        }
         
    }   
    
    public void copySingleImg() throws IOException{
//        String src_image = ImgPath + File.separatorChar + imagesList.get(pos);//GlobalListName[pos];
//        String dst_image = IncorrectPath + File.separatorChar + src_image;
        IncorrectSingleImage = SingleImageParent + File.separator + ".."+ File.separator + "incorrect";
        System.out.println(IncorrectSingleImage);
        File f1 = new File(SingleImageParent, SingleImageName);
        File f2 = new File(IncorrectSingleImage, SingleImageName);
        FileInputStream fis = new FileInputStream(f1);
        FileOutputStream fos = new FileOutputStream(f2);
        int c = fis.read();
        while(c!=-1){
            fos.write(c);
            c=fis.read();
        }
        System.out.println("Incorrect Image copied!");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jButton_previous = new javax.swing.JButton();
        jButton_next = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButtonCorrect = new javax.swing.JButton();
        jLabel_result = new javax.swing.JLabel();
        jLabel_Image = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel_Photos = new javax.swing.JLabel();
        jButton_openfile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_img = new javax.swing.JTable();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton_previous.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Pictures\\back.png")); // NOI18N
        jButton_previous.setText("PREV IMG");
        jButton_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_previousActionPerformed(evt);
            }
        });

        jButton_next.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Pictures\\next.png")); // NOI18N
        jButton_next.setText("NEXT IMG");
        jButton_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_nextActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 102, 102));
        jButton5.setText("INCORRECT");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButtonCorrect.setBackground(new java.awt.Color(39, 174, 96));
        jButtonCorrect.setText("CORRECT");
        jButtonCorrect.setBorder(null);
        jButtonCorrect.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCorrect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCorrectActionPerformed(evt);
            }
        });

        jLabel_result.setBackground(new java.awt.Color(0, 0, 51));
        jLabel_result.setFont(new java.awt.Font("Arial Black", 1, 100)); // NOI18N
        jLabel_result.setForeground(new java.awt.Color(102, 102, 102));
        jLabel_result.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_result.setText("!!!!!!");

        jLabel_Image.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_Image.setForeground(new java.awt.Color(102, 102, 102));
        jLabel_Image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Image.setText("IMAGE NOT FOUND!");

        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Pictures\\open-folder-with-document.png")); // NOI18N
        jButton1.setText("OPEN DIR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel_Photos.setFont(new java.awt.Font("굴림", 1, 36)); // NOI18N
        jLabel_Photos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Photos.setText("0");

        jButton_openfile.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Pictures\\back.png")); // NOI18N
        jButton_openfile.setText("OPEN FILE");
        jButton_openfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_openfileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton_next, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_previous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_openfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel_Photos, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_result, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCorrect, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel_Image, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_openfile, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_result, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_next, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_previous, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCorrect, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel_Photos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        jScrollPane1.setBackground(new java.awt.Color(22, 160, 133));

        jTable_img.setAutoCreateRowSorter(true);
        jTable_img.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable_img.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_imgMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_img);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = f.showOpenDialog(null); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = f.getSelectedFile();
            Absolute_Path = selectedFile.getAbsolutePath();
            start();
            OnOff = true;
            jButton_next.setEnabled(OnOff);
            jButton_previous.setEnabled(OnOff); 
            showImage(pos);
            getImagesList(Files_List);
            setTitle(Absolute_Path+"\\img\\");
            System.out.println(Absolute_Path);
            System.out.println("image length = " + imagesList.size());
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_nextActionPerformed
        pos = pos +1;
        if(pos >= imagesList.size()){
            pos = imagesList.size() -1;
        }
        showImage(pos);

    }//GEN-LAST:event_jButton_nextActionPerformed

    private void jButton_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_previousActionPerformed
        pos = pos -1;
        if(pos<0){
            pos = 0;
        }
        showImage(pos);
    }//GEN-LAST:event_jButton_previousActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            
            if(OnOff){
                System.out.println("OnOff is true");
                copyImage();
                pos = pos+1;
                if(pos >= imagesList.size()){
                    pos = imagesList.size() -1;
                }
                showImage(pos);
            }
            else{
                System.out.println("OnOff is false");
                copySingleImg();
            }
        } catch (IOException ex) {
            Logger.getLogger(captcha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonCorrectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCorrectActionPerformed
        pos = pos +1;
        if(pos >= imagesList.size()){
            pos = imagesList.size() -1;
        }
        showImage(pos);
    }//GEN-LAST:event_jButtonCorrectActionPerformed

    private void jButton_openfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_openfileActionPerformed

        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            SingleImageParent = selectedFile.getParent();
            SingleImageName = selectedFile.getName();
            System.out.println("File name is - " + SingleImageName);
            System.out.println("File parent path is - " + SingleImageParent);
            IncorrectSingleImage = SingleImageParent + "..\\" + "incorrect";
            System.out.println(IncorrectSingleImage);
            OnOff = false;
            jButton_next.setEnabled(OnOff);
            jButton_previous.setEnabled(OnOff); 
            OneImageShow();
        }
    }//GEN-LAST:event_jButton_openfileActionPerformed

    private void jTable_imgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_imgMouseClicked
        // TODO add your handling code here:
        int index = jTable_img.getSelectedRow();
        showImage(index);
    }//GEN-LAST:event_jTable_imgMouseClicked
    
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
            java.util.logging.Logger.getLogger(captcha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(captcha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(captcha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(captcha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new captcha().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonCorrect;
    private javax.swing.JButton jButton_next;
    private javax.swing.JButton jButton_openfile;
    private javax.swing.JButton jButton_previous;
    private javax.swing.JLabel jLabel_Image;
    private javax.swing.JLabel jLabel_Photos;
    private javax.swing.JLabel jLabel_result;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_img;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

}
