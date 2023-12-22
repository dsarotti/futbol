/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package edu.cotarelo.sistema;

import edu.cotarelo.dao.factories.MySQLFactory;
import edu.cotarelo.dao.objects.UsuarioDAO;
import edu.cotarelo.domain.Usuario;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SrSar
 */
public class Usuarios extends javax.swing.JPanel {

    /**
     * Creates new form Usuarios
     */
    public Usuarios() {
        initComponents();
        tablaUsuarioListado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bajaUsuarioNombre.setText(tablaUsuarioListado.getModel().getValueAt(tablaUsuarioListado.getSelectedRow(), 0).toString());
                bajaUsuarioApellidos.setText(tablaUsuarioListado.getModel().getValueAt(tablaUsuarioListado.getSelectedRow(), 1).toString());
                bajaUsuarioRol.setSelectedIndex(tablaUsuarioListado.getModel().getValueAt(tablaUsuarioListado.getSelectedRow(), 2).toString().equalsIgnoreCase("normal") ? 1 : 0);
                bajaUsuarioId.setText(tablaUsuarioListado.getModel().getValueAt(tablaUsuarioListado.getSelectedRow(), 3).toString());
            }
        });
    }
    /**
     * Da de alta un nuevo usuario en la base de datos
     */
    private void altaUsuario() {
        Usuario nuevo = new Usuario(
                altaUsuarioNombre.getText(),
                altaUsuarioApellidos.getText(),
                altaUsuarioPass.getText(),
                altaUsuarioRol.getSelectedItem().toString()
        );
        MySQLFactory factoria = new MySQLFactory();
        UsuarioDAO userDao = factoria.getUsuarioDAO();
        try {
            int salida = userDao.insertar(nuevo);
            if (salida < 0) {
                altaUsuarioRespuesta.setForeground(Color.red);
                altaUsuarioRespuesta.setText("No se ha podido insertar el usuario");
            } else {
                altaUsuarioRespuesta.setForeground(Color.blue);
                altaUsuarioRespuesta.setText("Se ha insertado el usuario");
                altaUsuarioNombre.setText("");
                altaUsuarioApellidos.setText("");
                altaUsuarioPass.setText("");
                altaUsuarioRol.setSelectedIndex(0);
            }
        } catch (NamingException e) {
            System.out.println("Error");
        }
    }
    
    private void modificarUsuario() {
        if (!bajaUsuarioId.getText().isBlank()) {
            MySQLFactory factoria = new MySQLFactory();
            UsuarioDAO userDao = factoria.getUsuarioDAO();
            try {
                Usuario user = userDao.getUsuarioById(Integer.parseInt(bajaUsuarioId.getText()));
                if (user != null) {
                    user.setNombre(bajaUsuarioNombre.getText());
                    user.setApellidos(bajaUsuarioApellidos.getText());
                    user.setClave(bajaUsuarioPass.getText());
                    user.setRol(bajaUsuarioRol.getSelectedItem().toString());
                    int modificado = userDao.modificar(user);
                    if (modificado == 1) {

                        bajaUsuarioRespuesta.setForeground(Color.blue);
                        bajaUsuarioRespuesta.setText("Se modificado el usuario con id " + bajaUsuarioId.getText());
                        DefaultTableModel tabla = (DefaultTableModel) tablaUsuarioListado.getModel();
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 3).toString().equals(bajaUsuarioId.getText())) {
                                tabla.setValueAt(user.getNombre(), i, 0);
                                tabla.setValueAt(user.getApellidos(), i, 1);
                                tabla.setValueAt(user.getRol(), i, 2);
                                break;
                            }
                        }
                    } else {
                        bajaUsuarioRespuesta.setForeground(Color.red);
                        bajaUsuarioRespuesta.setText("No se ha podido modificar el usuario");
                    }
                } else {
                    bajaUsuarioRespuesta.setForeground(Color.red);
                    bajaUsuarioRespuesta.setText("No se ha encontrado el usuario en la base de datos");
                }
            } catch (NullPointerException | NamingException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                bajaUsuarioRespuesta.setForeground(Color.red);
                bajaUsuarioRespuesta.setText("Error al modificar el usuario");
            }
        } else {
            bajaUsuarioRespuesta.setForeground(Color.red);
            bajaUsuarioRespuesta.setText("Debe seleccionar un usuario de la lista");
        }
    }
    
    private void bajaUsuario() {
        if (!bajaUsuarioId.getText().isBlank()) {
            MySQLFactory factoria = new MySQLFactory();
            UsuarioDAO userDao = factoria.getUsuarioDAO();
            try {
                Usuario user = userDao.getUsuarioById(Integer.parseInt(bajaUsuarioId.getText()));
                if (user != null) {
                    int borrado = userDao.borrar(user);
                    if (borrado == 1) {
                        bajaUsuarioRespuesta.setText("Se borrado el usuario con id " + bajaUsuarioId.getText());
                        DefaultTableModel tabla = (DefaultTableModel) tablaUsuarioListado.getModel();
                        
                        //Busca el elemento que se ha borrado y lo quita de la tabla
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 3).toString().equals(bajaUsuarioId.getText())) {
                                tabla.removeRow(i);
                                break;
                            }
                        }
                    } else {
                        bajaUsuarioRespuesta.setText("No se ha podido borrar el usuario");
                    }
                } else {
                    bajaUsuarioRespuesta.setText("No se ha encontrado el usuario en la base de datos");
                }
            } catch (NullPointerException | NamingException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                bajaUsuarioRespuesta.setText("Error al borrar el usuario");
            }
        } else {
            bajaUsuarioRespuesta.setText("Debe seleccionar un usuario de la lista");
        }
    }

    /**
     * Solicita los datos de los usuarios de la base de datos y los presenta en
     * el listado de usuarios
     */
    private void cargarTablaUsuarios() {
        DefaultTableModel tm = (DefaultTableModel) tablaUsuarioListado.getModel();
        tm.getDataVector().removeAllElements();
        tm.fireTableDataChanged();

        //pedimos los usuarios
        MySQLFactory factoria = new MySQLFactory();
        UsuarioDAO usuarioDao = factoria.getUsuarioDAO();
        try {
            ArrayList<Usuario> lista = usuarioDao.getListaUsuarios(1);
            if (lista == null || lista.isEmpty()) {
                tablaUsuariosRespuesta.setForeground(Color.blue);
                tablaUsuariosRespuesta.setText("No hay usuarios en la bbdd");
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    Object[] auxObject = new Object[4];
                    auxObject[0] = lista.get(i).getNombre();
                    auxObject[1] = lista.get(i).getApellidos();
                    auxObject[2] = lista.get(i).getRol();
                    auxObject[3] = lista.get(i).getIdUsuario();
                    tm.addRow(auxObject);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelNorte = new javax.swing.JPanel();
        panelAltaUsuarios = new javax.swing.JPanel();
        tituloAltaUsuario = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JLabel();
        rolLabel = new javax.swing.JLabel();
        apellidosLabel = new javax.swing.JLabel();
        contraseñaLabel = new javax.swing.JLabel();
        altaUsuarioApellidos = new javax.swing.JTextField();
        altaUsuarioNombre = new javax.swing.JTextField();
        altaUsuarioPass = new javax.swing.JTextField();
        altaUsuarioRol = new javax.swing.JComboBox<>();
        botonAltaUsuario = new javax.swing.JButton();
        altaUsuarioRespuesta = new javax.swing.JTextField();
        panelBajaUsuarios = new javax.swing.JPanel();
        tituloBajaUsuarios = new javax.swing.JLabel();
        nombreBajaLabel = new javax.swing.JLabel();
        rolBajaLabel = new javax.swing.JLabel();
        apellidosBajaLabel = new javax.swing.JLabel();
        contraseñaBajaLabel = new javax.swing.JLabel();
        bajaUsuarioApellidos = new javax.swing.JTextField();
        bajaUsuarioNombre = new javax.swing.JTextField();
        bajaUsuarioPass = new javax.swing.JTextField();
        bajaUsuarioRol = new javax.swing.JComboBox<>();
        bajaUsuarioId = new javax.swing.JTextField();
        bajaUsuarioRespuesta = new javax.swing.JTextField();
        contraseñaBajaLabel1 = new javax.swing.JLabel();
        botonEliminarUsuario = new javax.swing.JButton();
        botonModificarUsuario = new javax.swing.JButton();
        jPanelListadoUsuarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarioListado = new javax.swing.JTable();
        jPanelTituloListadoUsuarios = new javax.swing.JPanel();
        tituloTablaUsuarios = new javax.swing.JLabel();
        botonCargarTablaUsuarios = new javax.swing.JButton();
        tablaUsuariosRespuesta = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jPanelNorte.setLayout(new java.awt.GridBagLayout());

        panelAltaUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelAltaUsuarios.setLayout(new java.awt.GridBagLayout());

        tituloAltaUsuario.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloAltaUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloAltaUsuario.setText("Alta de usuario");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(tituloAltaUsuario, gridBagConstraints);

        nombreLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(nombreLabel, gridBagConstraints);

        rolLabel.setText("Rol");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(rolLabel, gridBagConstraints);

        apellidosLabel.setText("Apellidos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(apellidosLabel, gridBagConstraints);

        contraseñaLabel.setText("Contraseña");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(contraseñaLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaUsuarioApellidos, gridBagConstraints);

        altaUsuarioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altaUsuarioNombreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaUsuarioNombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaUsuarioPass, gridBagConstraints);

        altaUsuarioRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Admin" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaUsuarioRol, gridBagConstraints);

        botonAltaUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/insertarI.png"))); // NOI18N
        botonAltaUsuario.setText("Alta");
        botonAltaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonAltaUsuarioMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelAltaUsuarios.add(botonAltaUsuario, gridBagConstraints);

        altaUsuarioRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 620;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelAltaUsuarios.add(altaUsuarioRespuesta, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelNorte.add(panelAltaUsuarios, gridBagConstraints);

        panelBajaUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelBajaUsuarios.setLayout(new java.awt.GridBagLayout());

        tituloBajaUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloBajaUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloBajaUsuarios.setText("Baja/Modificación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(tituloBajaUsuarios, gridBagConstraints);

        nombreBajaLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(nombreBajaLabel, gridBagConstraints);

        rolBajaLabel.setText("Rol");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(rolBajaLabel, gridBagConstraints);

        apellidosBajaLabel.setText("Apellidos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(apellidosBajaLabel, gridBagConstraints);

        contraseñaBajaLabel.setText("Contraseña");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(contraseñaBajaLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaUsuarioApellidos, gridBagConstraints);

        bajaUsuarioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaUsuarioNombreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaUsuarioNombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaUsuarioPass, gridBagConstraints);

        bajaUsuarioRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Admin" }));
        bajaUsuarioRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaUsuarioRolActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaUsuarioRol, gridBagConstraints);

        bajaUsuarioId.setEditable(false);
        bajaUsuarioId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = -18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaUsuarioId, gridBagConstraints);

        bajaUsuarioRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 642;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelBajaUsuarios.add(bajaUsuarioRespuesta, gridBagConstraints);

        contraseñaBajaLabel1.setText("id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(contraseñaBajaLabel1, gridBagConstraints);

        botonEliminarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/eliminar.png"))); // NOI18N
        botonEliminarUsuario.setText("Eliminar");
        botonEliminarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonEliminarUsuarioMouseClicked(evt);
            }
        });
        botonEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(botonEliminarUsuario, gridBagConstraints);

        botonModificarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/actualizarI.png"))); // NOI18N
        botonModificarUsuario.setText("Modificar");
        botonModificarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonModificarUsuarioMouseClicked(evt);
            }
        });
        botonModificarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(botonModificarUsuario, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelNorte.add(panelBajaUsuarios, gridBagConstraints);

        add(jPanelNorte, java.awt.BorderLayout.NORTH);

        jPanelListadoUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelListadoUsuarios.setMinimumSize(new java.awt.Dimension(692, 110));
        jPanelListadoUsuarios.setLayout(new java.awt.BorderLayout());

        tablaUsuarioListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Apellidos", "Rol", "IdUsuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaUsuarioListado);

        jPanelListadoUsuarios.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelTituloListadoUsuarios.setLayout(new java.awt.GridBagLayout());

        tituloTablaUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloTablaUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloTablaUsuarios.setText("Listado de usuarios");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoUsuarios.add(tituloTablaUsuarios, gridBagConstraints);

        botonCargarTablaUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cargarI.png"))); // NOI18N
        botonCargarTablaUsuarios.setText("Cargar");
        botonCargarTablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonCargarTablaUsuariosMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoUsuarios.add(botonCargarTablaUsuarios, gridBagConstraints);

        tablaUsuariosRespuesta.setEditable(false);
        tablaUsuariosRespuesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tablaUsuariosRespuestaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoUsuarios.add(tablaUsuariosRespuesta, gridBagConstraints);

        jPanelListadoUsuarios.add(jPanelTituloListadoUsuarios, java.awt.BorderLayout.PAGE_START);

        add(jPanelListadoUsuarios, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void altaUsuarioNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altaUsuarioNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_altaUsuarioNombreActionPerformed

    private void botonAltaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAltaUsuarioMouseClicked
        altaUsuario();
    }//GEN-LAST:event_botonAltaUsuarioMouseClicked

    private void botonCargarTablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCargarTablaUsuariosMouseClicked
        cargarTablaUsuarios();
    }//GEN-LAST:event_botonCargarTablaUsuariosMouseClicked

    private void bajaUsuarioNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bajaUsuarioNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bajaUsuarioNombreActionPerformed

    private void bajaUsuarioRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bajaUsuarioRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bajaUsuarioRolActionPerformed

    private void botonEliminarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEliminarUsuarioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEliminarUsuarioMouseClicked

    private void botonEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarUsuarioActionPerformed
        bajaUsuario();
    }//GEN-LAST:event_botonEliminarUsuarioActionPerformed

    private void botonModificarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonModificarUsuarioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonModificarUsuarioMouseClicked

    private void botonModificarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarUsuarioActionPerformed
        modificarUsuario();
    }//GEN-LAST:event_botonModificarUsuarioActionPerformed

    private void tablaUsuariosRespuestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tablaUsuariosRespuestaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaUsuariosRespuestaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField altaUsuarioApellidos;
    private javax.swing.JTextField altaUsuarioNombre;
    private javax.swing.JTextField altaUsuarioPass;
    private javax.swing.JTextField altaUsuarioRespuesta;
    private javax.swing.JComboBox<String> altaUsuarioRol;
    private javax.swing.JLabel apellidosBajaLabel;
    private javax.swing.JLabel apellidosLabel;
    private javax.swing.JTextField bajaUsuarioApellidos;
    private javax.swing.JTextField bajaUsuarioId;
    private javax.swing.JTextField bajaUsuarioNombre;
    private javax.swing.JTextField bajaUsuarioPass;
    private javax.swing.JTextField bajaUsuarioRespuesta;
    private javax.swing.JComboBox<String> bajaUsuarioRol;
    private javax.swing.JButton botonAltaUsuario;
    private javax.swing.JButton botonCargarTablaUsuarios;
    private javax.swing.JButton botonEliminarUsuario;
    private javax.swing.JButton botonModificarUsuario;
    private javax.swing.JLabel contraseñaBajaLabel;
    private javax.swing.JLabel contraseñaBajaLabel1;
    private javax.swing.JLabel contraseñaLabel;
    private javax.swing.JPanel jPanelListadoUsuarios;
    private javax.swing.JPanel jPanelNorte;
    private javax.swing.JPanel jPanelTituloListadoUsuarios;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreBajaLabel;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JPanel panelAltaUsuarios;
    private javax.swing.JPanel panelBajaUsuarios;
    private javax.swing.JLabel rolBajaLabel;
    private javax.swing.JLabel rolLabel;
    private javax.swing.JTable tablaUsuarioListado;
    private javax.swing.JTextField tablaUsuariosRespuesta;
    private javax.swing.JLabel tituloAltaUsuario;
    private javax.swing.JLabel tituloBajaUsuarios;
    private javax.swing.JLabel tituloTablaUsuarios;
    // End of variables declaration//GEN-END:variables
}
