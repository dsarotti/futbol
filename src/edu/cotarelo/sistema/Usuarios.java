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

        panelAlta = new javax.swing.JPanel();
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
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarioListado = new javax.swing.JTable();
        tituloTablaUsuarios = new javax.swing.JLabel();
        botonCargarTablaUsuarios = new javax.swing.JButton();
        tablaUsuariosRespuesta = new javax.swing.JLabel();
        panelBaja = new javax.swing.JPanel();
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
        botonesBajaModificacion = new javax.swing.JPanel();
        botonEliminarUsuario = new javax.swing.JButton();
        botonModificarUsuario = new javax.swing.JButton();

        tituloAltaUsuario.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloAltaUsuario.setText("Alta de usuario");

        nombreLabel.setText("Nombre");

        rolLabel.setText("Rol");

        apellidosLabel.setText("Apellidos");

        contraseñaLabel.setText("Contraseña");

        altaUsuarioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altaUsuarioNombreActionPerformed(evt);
            }
        });

        altaUsuarioRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Admin" }));

        botonAltaUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/insertarI.png"))); // NOI18N
        botonAltaUsuario.setText("Alta");
        botonAltaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonAltaUsuarioMouseClicked(evt);
            }
        });

        altaUsuarioRespuesta.setEditable(false);

        javax.swing.GroupLayout panelAltaLayout = new javax.swing.GroupLayout(panelAlta);
        panelAlta.setLayout(panelAltaLayout);
        panelAltaLayout.setHorizontalGroup(
            panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloAltaUsuario)
                    .addGroup(panelAltaLayout.createSequentialGroup()
                        .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(altaUsuarioNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombreLabel))
                        .addGap(6, 6, 6)
                        .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(altaUsuarioApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(apellidosLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contraseñaLabel)
                            .addComponent(altaUsuarioPass, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAltaLayout.createSequentialGroup()
                                .addComponent(altaUsuarioRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botonAltaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(rolLabel)))
                    .addComponent(altaUsuarioRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAltaLayout.setVerticalGroup(
            panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloAltaUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreLabel)
                    .addComponent(apellidosLabel)
                    .addComponent(contraseñaLabel)
                    .addComponent(rolLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(altaUsuarioApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(altaUsuarioNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(altaUsuarioPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(altaUsuarioRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAltaUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(altaUsuarioRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

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

        tituloTablaUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloTablaUsuarios.setText("Listado de usuarios");

        botonCargarTablaUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cargarI.png"))); // NOI18N
        botonCargarTablaUsuarios.setText("Cargar");
        botonCargarTablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonCargarTablaUsuariosMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloTablaUsuarios)
                .addGap(136, 136, 136)
                .addComponent(tablaUsuariosRespuesta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCargarTablaUsuarios)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(tituloTablaUsuarios)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonCargarTablaUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tablaUsuariosRespuesta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tituloBajaUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloBajaUsuarios.setText("Baja/Modificación");

        nombreBajaLabel.setText("Nombre");

        rolBajaLabel.setText("Rol");

        apellidosBajaLabel.setText("Apellidos");

        contraseñaBajaLabel.setText("Contraseña");

        bajaUsuarioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaUsuarioNombreActionPerformed(evt);
            }
        });

        bajaUsuarioRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Admin" }));
        bajaUsuarioRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaUsuarioRolActionPerformed(evt);
            }
        });

        bajaUsuarioId.setEditable(false);

        bajaUsuarioRespuesta.setEditable(false);

        contraseñaBajaLabel1.setText("id");

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

        javax.swing.GroupLayout botonesBajaModificacionLayout = new javax.swing.GroupLayout(botonesBajaModificacion);
        botonesBajaModificacion.setLayout(botonesBajaModificacionLayout);
        botonesBajaModificacionLayout.setHorizontalGroup(
            botonesBajaModificacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botonesBajaModificacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(botonesBajaModificacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonModificarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonEliminarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        botonesBajaModificacionLayout.setVerticalGroup(
            botonesBajaModificacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botonesBajaModificacionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonModificarUsuario)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelBajaLayout = new javax.swing.GroupLayout(panelBaja);
        panelBaja.setLayout(panelBajaLayout);
        panelBajaLayout.setHorizontalGroup(
            panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBajaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bajaUsuarioRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tituloBajaUsuarios)
                    .addGroup(panelBajaLayout.createSequentialGroup()
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bajaUsuarioNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombreBajaLabel))
                        .addGap(6, 6, 6)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bajaUsuarioApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(apellidosBajaLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contraseñaBajaLabel)
                            .addComponent(bajaUsuarioPass, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bajaUsuarioRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rolBajaLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bajaUsuarioId, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contraseñaBajaLabel1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonesBajaModificacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBajaLayout.setVerticalGroup(
            panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBajaLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonesBajaModificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBajaLayout.createSequentialGroup()
                        .addComponent(tituloBajaUsuarios)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombreBajaLabel)
                            .addComponent(apellidosBajaLabel)
                            .addComponent(contraseñaBajaLabel)
                            .addComponent(rolBajaLabel)
                            .addComponent(contraseñaBajaLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bajaUsuarioApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bajaUsuarioNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bajaUsuarioPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bajaUsuarioRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bajaUsuarioId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bajaUsuarioRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelBaja, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAlta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
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
    private javax.swing.JPanel botonesBajaModificacion;
    private javax.swing.JLabel contraseñaBajaLabel;
    private javax.swing.JLabel contraseñaBajaLabel1;
    private javax.swing.JLabel contraseñaLabel;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreBajaLabel;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JPanel panelAlta;
    private javax.swing.JPanel panelBaja;
    private javax.swing.JLabel rolBajaLabel;
    private javax.swing.JLabel rolLabel;
    private javax.swing.JTable tablaUsuarioListado;
    private javax.swing.JLabel tablaUsuariosRespuesta;
    private javax.swing.JLabel tituloAltaUsuario;
    private javax.swing.JLabel tituloBajaUsuarios;
    private javax.swing.JLabel tituloTablaUsuarios;
    // End of variables declaration//GEN-END:variables
}
