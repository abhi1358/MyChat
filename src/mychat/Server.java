/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author ABHICRUISER
 */


public class Server extends javax.swing.JFrame {

    /**
     * Creates new form Server
     */
    public Server() {
        initComponents();
    }
    ServerSocket server;
    Socket client;
    public static int clientID;
    Vector<ClientHandler> ar = new Vector<>();
    HashMap<String,ClientHandler> har = new HashMap<>();
    static Vector<String> online_userlist = new Vector<>();
    DefaultListModel model = new DefaultListModel();
    
    public synchronized void getDisplay(String s) {
        server_display.setText(server_display.getText() + s);
    }
    
    public synchronized void sendUserList() throws IOException {
        int n=har.size();
        for(HashMap.Entry<String, ClientHandler> hr : har.entrySet()) {
            DataOutputStream duser = new DataOutputStream(hr.getValue().socket.getOutputStream());
            duser.writeUTF("UL@" + har.size());
            for(HashMap.Entry<String, ClientHandler> usr_list : har.entrySet()) {
                duser.writeUTF(usr_list.getKey());
            }
        }
        
    }
    public synchronized void addUser(String name,ClientHandler ch) throws IOException {
        har.put(name,ch);
        ar.add(ch);
        online_userlist.add(name);
        sendUserList();
        onlineUsers.setModel(model);
        //String userName = online_userlist[clientID];
        model.addElement(online_userlist.get(clientID));
        clientID++;
        //server_display.setText(server_display.getText() + s);
    }
//    public synchronized void updateOnlineUsers() {
//        DefaultListModel model = new DefaultListModel();
//        onlineUsers.setModel(model);
//        //String userName = online_userlist[clientID];
//        model.addElement(online_userlist.get(clientID));
//        //onlineUsers.add(model);
//    }
    public void sendMsg(Socket st,String msg) throws IOException {
        DataOutputStream dout = new DataOutputStream(st.getOutputStream());
        dout.writeUTF(msg);
    }
    class Listener implements Runnable {

//        Vector<ClientHandler> ar = new Vector<>();
//        HashMap<String,ClientHandler> har = new HashMap<>();
        public void run() {
            try {
                server = new ServerSocket(13000);
                //int i=0;
                while(true) {
                    client = server.accept();
                    
                    InputStream is = client.getInputStream();
                    DataInputStream din = new DataInputStream(is);
                    OutputStream os = client.getOutputStream();
                    DataOutputStream dout = new DataOutputStream(os);
                    String username = din.readUTF();
                    getDisplay(username + " got Connected\n");
                    ClientHandler ch = new ClientHandler(client,username,din,dout);
                    addUser(username,ch);
                    //System.out.println(online_userlist.size());
                    //displayUsers();
                    //updateOnlineUsers();
                    Thread th = new Thread(ch);
                    th.start();
                    //clientID++;
                }
            } catch(Exception e) {
                System.out.println("error");
            }
        }
    }
    class ClientHandler implements Runnable {
        DataOutputStream ds;
        DataInputStream di;
        Socket socket;
        String name;
        ClientHandler(Socket socket,String name,DataInputStream di,DataOutputStream ds) {
            this.ds=ds;
            this.di=di;
            this.socket=socket;
            this.name =name;
        }
        public void run() {
            while(true) {
                try {
                    String s1=di.readUTF(),msg;
                    //JOptionPane.showMessageDialog(rootPane, s1);
                    int s1n=s1.length();
//                    int i=0;
                    String reciever = s1.substring(0,s1.indexOf('#')) ;
                    msg=name + ": " + s1.substring(s1.indexOf('#') + 1);
//                    while(s1[i]!='#') {
//                        reciever+=s1[i];
//                        i++;
//                    }
                    String ss="\n" + name + "-> " + s1;
                    getDisplay(ss);
                    //System.out.println(har.get(reciever).name);
                    if(har.containsKey(reciever))
                        sendMsg(har.get(reciever).socket,"MSG@" + msg);
                    else
                        sendMsg(socket,"Reciever Not Present at the Moment\n");
                    //server_display.setText(ss + );
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public void stop() throws IOException { 
            socket.close();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        server_display = new javax.swing.JTextArea();
        start_server = new javax.swing.JButton();
        stop_server = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        onlineUsers = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SERVER");

        server_display.setColumns(20);
        server_display.setRows(5);
        jScrollPane1.setViewportView(server_display);

        start_server.setText("Start Server");
        start_server.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                start_serverActionPerformed(evt);
            }
        });

        stop_server.setText("Stop Server");
        stop_server.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stop_serverActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(onlineUsers);

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel1.setText("Online Users : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(start_server)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stop_server))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(start_server)
                    .addComponent(stop_server))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void start_serverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_start_serverActionPerformed
        // TODO add your handling code here:
        Thread thl = new Thread(new Listener());
        thl.start();
    }//GEN-LAST:event_start_serverActionPerformed

    private void stop_serverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stop_serverActionPerformed
        try {
            // TODO add your handling code here:
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_stop_serverActionPerformed

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
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> onlineUsers;
    private javax.swing.JTextArea server_display;
    private javax.swing.JButton start_server;
    private javax.swing.JButton stop_server;
    // End of variables declaration//GEN-END:variables
}
