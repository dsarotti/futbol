/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package edu.cotarelo.sistema;

import edu.cotarelo.dao.factories.MySQLFactory;
import edu.cotarelo.dao.objects.UsuarioDAO;
import edu.cotarelo.domain.Usuario;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.swing.JOptionPane;

/**
 * Clase para presentar la pantalla de login
 *
 * @author desarotti
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
    }

    /**
     * Comprueba que la combinación de usuario y contraseña escrita en los
     * campos de texto sea válida y se corresponda con un usuario de la base de
     * datos.
     * @return Un usuario si la validación es correcta, null en caso contrario.
     */
    private Usuario validarUsuario() {
        Usuario usuario = null;
        if (!loginNombre.getText().isBlank()
                && !loginNombre.getText().equals("Introduzca aquí su nombre de usuario")
                && !String.valueOf(loginPass.getPassword()).isBlank()) {
            try {
                String usuarioNombre = loginNombre.getText();
                String pass = String.valueOf(loginPass.getPassword());
                MySQLFactory f = new MySQLFactory();
                UsuarioDAO usuarioDao = f.getUsuarioDAO();
                usuario = usuarioDao.getUsuarioByNombreContraseña(usuarioNombre, pass);
                if (usuario == null) {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario Autenticado!");
                }
            } catch (NamingException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe introducir el usuario y la contraseña");
        }
        return usuario;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginPass = new javax.swing.JPasswordField();
        loginTitulo = new javax.swing.JLabel();
        loginNombre = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        balonAzul = new javax.swing.JLabel();
        cesped = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(680, 570));
        setMinimumSize(new java.awt.Dimension(680, 570));
        setPreferredSize(new java.awt.Dimension(680, 570));
        setResizable(false);
        setSize(new java.awt.Dimension(680, 570));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginPass.setBackground(new java.awt.Color(0, 153, 51));
        loginPass.setForeground(new java.awt.Color(255, 255, 255));
        loginPass.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contraseña", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        loginPass.setCaretColor(new java.awt.Color(255, 255, 255));
        getContentPane().add(loginPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 390, 80));

        loginTitulo.setBackground(new java.awt.Color(107, 199, 0));
        loginTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        loginTitulo.setForeground(new java.awt.Color(255, 255, 255));
        loginTitulo.setText("Acceso para usuarios");
        loginTitulo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(loginTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        loginNombre.setBackground(new java.awt.Color(0, 153, 0));
        loginNombre.setForeground(new java.awt.Color(255, 255, 255));
        loginNombre.setText("Introduzca aquí su nombre de usuario");
        loginNombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        loginNombre.setCaretColor(new java.awt.Color(255, 255, 255));
        loginNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                loginNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                loginNombreFocusLost(evt);
            }
        });
        loginNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginNombreActionPerformed(evt);
            }
        });
        getContentPane().add(loginNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 390, 80));

        loginButton.setBackground(new java.awt.Color(51, 51, 255));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Iniciar");
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginButtonMouseClicked(evt);
            }
        });
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        getContentPane().add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 200, 70));

        balonAzul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/balon.jpg"))); // NOI18N
        getContentPane().add(balonAzul, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 350, 400));

        cesped.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cesped.jpeg"))); // NOI18N
        getContentPane().add(cesped, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 570));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginNombreActionPerformed

    private void loginNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_loginNombreFocusGained
        if (loginNombre.getText().equals("Introduzca aquí su nombre de usuario")) {
            loginNombre.setText("");
            loginNombre.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_loginNombreFocusGained

    private void loginNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_loginNombreFocusLost
        if (loginNombre.getText().equals("")) {
            loginNombre.setText("Introduzca aquí su nombre de usuario");
            loginNombre.setForeground(Color.WHITE);
        }
    }//GEN-LAST:event_loginNombreFocusLost

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:
        Usuario usuario = validarUsuario();
        if (usuario != null) {
            new Sistema(usuario).setVisible(true);
            setVisible(false);
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    private void loginButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseClicked
        
    }//GEN-LAST:event_loginButtonMouseClicked

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {                
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balonAzul;
    private javax.swing.JLabel cesped;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginNombre;
    private javax.swing.JPasswordField loginPass;
    private javax.swing.JLabel loginTitulo;
    // End of variables declaration//GEN-END:variables
}