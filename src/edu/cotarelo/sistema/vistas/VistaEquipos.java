/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package edu.cotarelo.sistema.vistas;

import edu.cotarelo.dao.factories.MySQLFactory;
import java.awt.Color;
import edu.cotarelo.dao.objects.ClubDAO;
import edu.cotarelo.domain.Club;
import edu.cotarelo.sistema.Sistema;
import java.util.ArrayList;
import javax.naming.NamingException;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase VistaEquipos es una interfaz gráfica que permite gestionar la
 * información de equipos en una base de datos. Permite dar de alta, modificar,
 * y dar de baja equipos, así como cargar un listado de equipos desde la base de
 * datos.
 *
 * @author Dante Sarotti
 */
public class VistaEquipos extends javax.swing.JPanel {

    /**
     * Crea una nueva instancia de la clase VistaEquipos.
     */
    public VistaEquipos() {
        initComponents();

    }

    /**
     * Da de alta un nuevo equipo en la base de datos.
     */
    private void altaEquipo() {
        if (altaEquiposNombre.getText().isBlank() || altaEquiposCampo.getText().isBlank() || altaEquiposDescripcion.getText().isBlank()) {
            altaEquiposRespuesta.setForeground(Color.red);
            altaEquiposRespuesta.setText("Debe rellenar todos los campos");
        } else {
            Club nuevo = new Club(
                    altaEquiposNombre.getText(),
                    altaEquiposCampo.getText(),
                    altaEquiposDescripcion.getText()
            );
            MySQLFactory factoria = new MySQLFactory();
            ClubDAO clubDAO = factoria.getClubDAO();
            try {
                int salida = clubDAO.insertar(nuevo);
                if (salida < 0) {
                    altaEquiposRespuesta.setForeground(Color.red);
                    altaEquiposRespuesta.setText("No se ha podido insertar el equipo");
                } else {
                    altaEquiposRespuesta.setForeground(Color.blue);
                    altaEquiposRespuesta.setText("Se ha insertado el equipo");
                    altaEquiposNombre.setText("");
                    altaEquiposCampo.setText("");
                    altaEquiposDescripcion.setText("");
                }
            } catch (NamingException e) {
                System.out.println("Error");
            }
        }

    }

    /**
     * Modifica un equipo en la base de datos.
     */
    private void modificarEquipo() {
        if (!bajaEquipoNombre.getText().isBlank()) {
            MySQLFactory factoria = new MySQLFactory();
            ClubDAO clubDao = factoria.getClubDAO();
            try {
                Club club = clubDao.getClubById(tablaEquiposListado.getModel().getValueAt(tablaEquiposListado.getSelectedRow(), 0).toString());
                if (club != null) {
                    Club clubAnterior = clubDao.getClubById(tablaEquiposListado.getModel().getValueAt(tablaEquiposListado.getSelectedRow(), 0).toString());

                    club.setNombre(bajaEquipoNombre.getText());
                    club.setCampo(bajaEquipoCampo.getText());
                    club.setDescripcion(bajaEquipoDescripcion.getText());
                    int modificado = clubDao.modificar(club, clubAnterior);
                    if (modificado == 1) {

                        bajaEquipoRespuesta.setForeground(Color.blue);
                        bajaEquipoRespuesta.setText("Se modificado el equipo " + bajaEquipoNombre.getText());
                        DefaultTableModel tabla = (DefaultTableModel) tablaEquiposListado.getModel();
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 0).toString().equals(clubAnterior.getNombre())) {
                                tabla.setValueAt(club.getNombre(), i, 0);
                                tabla.setValueAt(club.getCampo(), i, 1);
                                tabla.setValueAt(club.getDescripcion(), i, 2);
                                break;
                            }
                        }
                    } else {
                        bajaEquipoRespuesta.setForeground(Color.red);
                        bajaEquipoRespuesta.setText("No se ha podido modificar el equipo");
                    }
                } else {
                    bajaEquipoRespuesta.setForeground(Color.red);
                    bajaEquipoRespuesta.setText("No se ha encontrado el equipo en la base de datos");
                }
            } catch (NullPointerException | NamingException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                bajaEquipoRespuesta.setForeground(Color.red);
                bajaEquipoRespuesta.setText("Error al modificar el equipo");
            }
        } else {
            bajaEquipoRespuesta.setForeground(Color.red);
            bajaEquipoRespuesta.setText("Debe seleccionar un equipo de la lista");
        }
    }

    /**
     * Elimina el equipo seleccionado de la base de datos.
     */
    private void bajaEquipo() {
        if (!bajaEquipoNombre.getText().isBlank()) {
            MySQLFactory factoria = new MySQLFactory();
            ClubDAO clubDao = factoria.getClubDAO();
            try {
                Club jugador = clubDao.getClubById(bajaEquipoNombre.getText());
                if (jugador != null) {
                    int borrado = clubDao.borrar(jugador);
                    if (borrado == 1) {
                        bajaEquipoRespuesta.setText("Se borrado el equipo " + bajaEquipoNombre.getText());

                        //Busca el elemento que se ha borrado y lo quita de la tabla
                        DefaultTableModel tabla = (DefaultTableModel) tablaEquiposListado.getModel();
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 0).toString().equals(bajaEquipoNombre.getText())) {
                                tabla.removeRow(i);
                                break;
                            }
                        }
                    } else {
                        bajaEquipoRespuesta.setText("No se ha podido borrar el equipo");
                    }
                } else {
                    bajaEquipoRespuesta.setText("No se ha encontrado el equipo en la base de datos");
                }
            } catch (NullPointerException | NamingException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                bajaEquipoRespuesta.setText("Error al borrar el equipo");
            }
        } else {
            bajaEquipoRespuesta.setText("Debe seleccionar un equipo de la lista");
        }
    }

    /**
     * Carga los equipos desde la base de datos y los presenta en el listado de
     * equipos.
     */
    private void cargarTablaEquipos() {
        DefaultTableModel tm = (DefaultTableModel) tablaEquiposListado.getModel();
        tm.getDataVector().removeAllElements();
        tm.fireTableDataChanged();

        //pedimos los jugadores
        MySQLFactory factoria = new MySQLFactory();
        ClubDAO jugadorDAO = factoria.getClubDAO();

        ArrayList<Club> lista = jugadorDAO.getLista(1);
        if (lista == null || lista.isEmpty()) {
            tablaEquipoRespuesta.setForeground(Color.red);
            tablaEquipoRespuesta.setText("No hay equipos en la bbdd");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Object[] auxObject = new Object[4];
                auxObject[0] = lista.get(i).getNombre();
                auxObject[1] = lista.get(i).getCampo();
                auxObject[2] = lista.get(i).getDescripcion();
                tm.addRow(auxObject);
            }
            tablaEquipoRespuesta.setForeground(Color.blue);
            tablaEquipoRespuesta.setText("Se cargaron los equipos");
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
        tituloAltaEquipo = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JLabel();
        camposLabel = new javax.swing.JLabel();
        descripcionLabel = new javax.swing.JLabel();
        altaEquiposCampo = new javax.swing.JTextField();
        altaEquiposNombre = new javax.swing.JTextField();
        altaEquiposDescripcion = new javax.swing.JTextField();
        botonAltaEquipos = new javax.swing.JButton();
        altaEquiposRespuesta = new javax.swing.JTextField();
        panelBajaUsuarios = new javax.swing.JPanel();
        tituloBajaEquipos = new javax.swing.JLabel();
        nombreBajaLabel = new javax.swing.JLabel();
        campoBajaLabel = new javax.swing.JLabel();
        descripcionBajaLabel = new javax.swing.JLabel();
        bajaEquipoCampo = new javax.swing.JTextField();
        bajaEquipoNombre = new javax.swing.JTextField();
        bajaEquipoDescripcion = new javax.swing.JTextField();
        bajaEquipoRespuesta = new javax.swing.JTextField();
        botonEliminarEquipo = new javax.swing.JButton();
        botonModificarEquipo = new javax.swing.JButton();
        jPanelListadoUsuarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEquiposListado = new javax.swing.JTable();
        jPanelTituloListadoEquipo = new javax.swing.JPanel();
        tituloTablaEquipo = new javax.swing.JLabel();
        botonCargarTablaEquipo = new javax.swing.JButton();
        tablaEquipoRespuesta = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jPanelNorte.setBackground(new java.awt.Color(0, 102, 51));
        jPanelNorte.setLayout(new java.awt.GridBagLayout());

        panelAltaUsuarios.setBackground(new java.awt.Color(0, 153, 51));
        panelAltaUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelAltaUsuarios.setPreferredSize(new java.awt.Dimension(920, 132));
        panelAltaUsuarios.setLayout(new java.awt.GridBagLayout());

        tituloAltaEquipo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloAltaEquipo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloAltaEquipo.setText("Alta de equipo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(tituloAltaEquipo, gridBagConstraints);

        nombreLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(nombreLabel, gridBagConstraints);

        camposLabel.setText("Campo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(camposLabel, gridBagConstraints);

        descripcionLabel.setText("Descripción");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(descripcionLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaEquiposCampo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaEquiposNombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelAltaUsuarios.add(altaEquiposDescripcion, gridBagConstraints);

        botonAltaEquipos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/insertarI.png"))); // NOI18N
        botonAltaEquipos.setText("Alta");
        botonAltaEquipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAltaEquiposActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelAltaUsuarios.add(botonAltaEquipos, gridBagConstraints);

        altaEquiposRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 620;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelAltaUsuarios.add(altaEquiposRespuesta, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelNorte.add(panelAltaUsuarios, gridBagConstraints);

        panelBajaUsuarios.setBackground(new java.awt.Color(0, 153, 51));
        panelBajaUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelBajaUsuarios.setPreferredSize(new java.awt.Dimension(920, 161));
        panelBajaUsuarios.setLayout(new java.awt.GridBagLayout());

        tituloBajaEquipos.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloBajaEquipos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloBajaEquipos.setText("Baja/Modificación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(tituloBajaEquipos, gridBagConstraints);

        nombreBajaLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(nombreBajaLabel, gridBagConstraints);

        campoBajaLabel.setText("Campo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(campoBajaLabel, gridBagConstraints);

        descripcionBajaLabel.setText("Descripción");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(descripcionBajaLabel, gridBagConstraints);

        bajaEquipoCampo.setToolTipText("");
        bajaEquipoCampo.setName("campo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaEquipoCampo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaEquipoNombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 104;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(bajaEquipoDescripcion, gridBagConstraints);

        bajaEquipoRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 642;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelBajaUsuarios.add(bajaEquipoRespuesta, gridBagConstraints);

        botonEliminarEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/eliminar.png"))); // NOI18N
        botonEliminarEquipo.setText("Eliminar");
        botonEliminarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarEquipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(botonEliminarEquipo, gridBagConstraints);

        botonModificarEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/actualizarI.png"))); // NOI18N
        botonModificarEquipo.setText("Modificar");
        botonModificarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarEquipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelBajaUsuarios.add(botonModificarEquipo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelNorte.add(panelBajaUsuarios, gridBagConstraints);

        add(jPanelNorte, java.awt.BorderLayout.NORTH);

        jPanelListadoUsuarios.setBackground(new java.awt.Color(0, 153, 51));
        jPanelListadoUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelListadoUsuarios.setMinimumSize(new java.awt.Dimension(692, 110));
        jPanelListadoUsuarios.setLayout(new java.awt.BorderLayout());

        tablaEquiposListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Campo", "Descripcion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEquiposListado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaEquiposListadoMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEquiposListado);

        jPanelListadoUsuarios.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelTituloListadoEquipo.setBackground(new java.awt.Color(0, 153, 51));
        jPanelTituloListadoEquipo.setLayout(new java.awt.GridBagLayout());

        tituloTablaEquipo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloTablaEquipo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloTablaEquipo.setText("Listado de equipos");
        tituloTablaEquipo.setMaximumSize(new java.awt.Dimension(360, 48));
        tituloTablaEquipo.setMinimumSize(new java.awt.Dimension(360, 48));
        tituloTablaEquipo.setPreferredSize(new java.awt.Dimension(360, 48));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoEquipo.add(tituloTablaEquipo, gridBagConstraints);

        botonCargarTablaEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cargarI.png"))); // NOI18N
        botonCargarTablaEquipo.setText("Cargar");
        botonCargarTablaEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarTablaEquipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoEquipo.add(botonCargarTablaEquipo, gridBagConstraints);

        tablaEquipoRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTituloListadoEquipo.add(tablaEquipoRespuesta, gridBagConstraints);

        jPanelListadoUsuarios.add(jPanelTituloListadoEquipo, java.awt.BorderLayout.PAGE_START);

        add(jPanelListadoUsuarios, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void botonEliminarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarEquipoActionPerformed
        bajaEquipo();
    }//GEN-LAST:event_botonEliminarEquipoActionPerformed

    private void botonModificarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarEquipoActionPerformed
        modificarEquipo();
    }//GEN-LAST:event_botonModificarEquipoActionPerformed

    private void botonAltaEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAltaEquiposActionPerformed
        altaEquipo();
    }//GEN-LAST:event_botonAltaEquiposActionPerformed

    private void botonCargarTablaEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarTablaEquipoActionPerformed
        cargarTablaEquipos();
    }//GEN-LAST:event_botonCargarTablaEquipoActionPerformed

    /**
     * Maneja el evento de ratón para seleccionar un equipo de la tabla.
     */
    private void tablaEquiposListadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEquiposListadoMousePressed
        bajaEquipoNombre.setText(tablaEquiposListado.getModel().getValueAt(tablaEquiposListado.getSelectedRow(), 0).toString());
        bajaEquipoCampo.setText(tablaEquiposListado.getModel().getValueAt(tablaEquiposListado.getSelectedRow(), 1).toString());
        bajaEquipoDescripcion.setText(tablaEquiposListado.getModel().getValueAt(tablaEquiposListado.getSelectedRow(), 2).toString());
    }//GEN-LAST:event_tablaEquiposListadoMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField altaEquiposCampo;
    private javax.swing.JTextField altaEquiposDescripcion;
    private javax.swing.JTextField altaEquiposNombre;
    private javax.swing.JTextField altaEquiposRespuesta;
    private javax.swing.JTextField bajaEquipoCampo;
    private javax.swing.JTextField bajaEquipoDescripcion;
    private javax.swing.JTextField bajaEquipoNombre;
    private javax.swing.JTextField bajaEquipoRespuesta;
    private javax.swing.JButton botonAltaEquipos;
    private javax.swing.JButton botonCargarTablaEquipo;
    private javax.swing.JButton botonEliminarEquipo;
    private javax.swing.JButton botonModificarEquipo;
    private javax.swing.JLabel campoBajaLabel;
    private javax.swing.JLabel camposLabel;
    private javax.swing.JLabel descripcionBajaLabel;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JPanel jPanelListadoUsuarios;
    private javax.swing.JPanel jPanelNorte;
    private javax.swing.JPanel jPanelTituloListadoEquipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreBajaLabel;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JPanel panelAltaUsuarios;
    private javax.swing.JPanel panelBajaUsuarios;
    private javax.swing.JTextField tablaEquipoRespuesta;
    private javax.swing.JTable tablaEquiposListado;
    private javax.swing.JLabel tituloAltaEquipo;
    private javax.swing.JLabel tituloBajaEquipos;
    private javax.swing.JLabel tituloTablaEquipo;
    // End of variables declaration//GEN-END:variables
}
