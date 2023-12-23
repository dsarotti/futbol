/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package edu.cotarelo.sistema.vistas;

import edu.cotarelo.dao.factories.MySQLFactory;
import edu.cotarelo.dao.objects.JugadorDAO;
import edu.cotarelo.domain.Jugador;
import edu.cotarelo.sistema.Sistema;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SrSar
 */
public class VistaJugadores extends javax.swing.JPanel {

    /**
     * Creates new form Jugadores
     */
    public VistaJugadores() {
        initComponents();
        
        cargarDropdownsJugadores();
        
        tablaJugadoresListado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bajaJugadorNombre.setText(tablaJugadoresListado.getModel().getValueAt(tablaJugadoresListado.getSelectedRow(), 0).toString());
                bajaJugadorApellidos.setText(tablaJugadoresListado.getModel().getValueAt(tablaJugadoresListado.getSelectedRow(), 1).toString());
                bajaJugadorPosicion.setSelectedItem(tablaJugadoresListado.getModel().getValueAt(tablaJugadoresListado.getSelectedRow(), 2).toString());
                bajaJugadorId.setText(tablaJugadoresListado.getModel().getValueAt(tablaJugadoresListado.getSelectedRow(), 3).toString());
            }
        });
    }

    /**
     * Recibe de la base de datos los valores posibles de los menús desplegables de la pestaña Jugadores
     */
    private void cargarDropdownsJugadores(){
        MySQLFactory f = new MySQLFactory();
        JugadorDAO jd = f.getJugadorDAO();
        Collection<String> posiciones = jd.getListaPosiciones().values();
        for(String posicion : posiciones){
        altaJugadorPosicion.addItem(posicion);
        bajaJugadorPosicion.addItem(posicion);
        }
    }

    private void altaJugador() {
        Jugador nuevo = new Jugador(
                altaJugadorNombre.getText(),
                altaJugadorApellidos.getText(),
                altaJugadorPosicion.getSelectedItem().toString()
        );
        MySQLFactory factoria = new MySQLFactory();
        JugadorDAO jugadorDAO = factoria.getJugadorDAO();
        try {
            int salida = jugadorDAO.insertar(nuevo);
            if (salida < 0) {
                altaJugadorRespuesta.setForeground(Color.red);
                altaJugadorRespuesta.setText("No se ha podido dar de alta al jugador");
            } else {
                altaJugadorRespuesta.setForeground(Color.blue);
                altaJugadorRespuesta.setText("El jugador ha sido dado de alta correctamente");
            }
        } catch (NamingException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Solicita los datos de los jugadores de la base de datos y los presenta en
     * el listado de jugadores
     */
    private void cargarTablaJugadores() {
        DefaultTableModel tm = (DefaultTableModel) tablaJugadoresListado.getModel();
        tm.getDataVector().removeAllElements();
        tm.fireTableDataChanged();

        //pedimos los jugadores
        MySQLFactory factoria = new MySQLFactory();
        JugadorDAO jugadorDAO = factoria.getJugadorDAO();

        ArrayList<Jugador> lista = jugadorDAO.getLista(1);
        if (lista == null || lista.isEmpty()) {
            tablaJugadoresRespuesta.setForeground(Color.red);
            tablaJugadoresRespuesta.setText("No hay jugadores en la bbdd");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Object[] auxObject = new Object[4];
                auxObject[0] = lista.get(i).getNombre();
                auxObject[1] = lista.get(i).getApellidos();
                auxObject[2] = lista.get(i).getPosicion();
                auxObject[3] = lista.get(i).getIdJugador();
                tm.addRow(auxObject);
            }
            tablaJugadoresRespuesta.setForeground(Color.blue);
            tablaJugadoresRespuesta.setText("Se cargaron los jugadores");
        }
    }
    
    /**
     * Elimina el jugador seleccionado de la base de datos
     */
    private void bajaJugador(){
         if (!bajaJugadorId.getText().isBlank()) {
            MySQLFactory factoria = new MySQLFactory();
            JugadorDAO jugadorDao = factoria.getJugadorDAO();
            try {
                Jugador jugador = jugadorDao.getJugadorById(Integer.parseInt(bajaJugadorId.getText()));
                if (jugador != null) {
                    int borrado = jugadorDao.borrar(jugador);
                    if (borrado == 1) {
                        bajaJugadorRespuesta.setText("Se borrado el jugador con id " + bajaJugadorId.getText());

                        //Busca el elemento que se ha borrado y lo quita de la tabla
                        DefaultTableModel tabla = (DefaultTableModel) tablaJugadoresListado.getModel();
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 3).toString().equals(bajaJugadorId.getText())) {
                                tabla.removeRow(i);
                                break;
                            }
                        }
                    } else {
                        bajaJugadorRespuesta.setText("No se ha podido borrar el usuario");
                    }
                } else {
                    bajaJugadorRespuesta.setText("No se ha encontrado el jugador en la base de datos");
                }
            } catch (NullPointerException | NamingException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
                bajaJugadorRespuesta.setText("Error al borrar el jugador");
            }
        } else {
            bajaJugadorRespuesta.setText("Debe seleccionar un jugador de la lista");
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

        jPaneles = new javax.swing.JPanel();
        jPanelBajaJugadores = new javax.swing.JPanel();
        tituloBajaJugador = new javax.swing.JLabel();
        nombreBajaJugadorLabel = new javax.swing.JLabel();
        apellidosBajaJugadorLabel = new javax.swing.JLabel();
        posiciónBajaJugadorLabel = new javax.swing.JLabel();
        bajaJugadorApellidos = new javax.swing.JTextField();
        bajaJugadorNombre = new javax.swing.JTextField();
        bajaJugadorId = new javax.swing.JTextField();
        bajaJugadorRespuesta = new javax.swing.JTextField();
        idBajaJugadorLabel = new javax.swing.JLabel();
        botonEliminarJugador = new javax.swing.JButton();
        botonModificarJugador = new javax.swing.JButton();
        bajaJugadorPosicion = new javax.swing.JComboBox<>();
        jPanelAltaJugadores = new javax.swing.JPanel();
        tituloAltaJugador = new javax.swing.JLabel();
        nombreJugadorLabel = new javax.swing.JLabel();
        apellidosJugadorLabel = new javax.swing.JLabel();
        posicionJugadorLabel = new javax.swing.JLabel();
        altaJugadorApellidos = new javax.swing.JTextField();
        altaJugadorNombre = new javax.swing.JTextField();
        altaJugadorPosicion = new javax.swing.JComboBox<>();
        botonAltaJugador = new javax.swing.JButton();
        altaJugadorRespuesta = new javax.swing.JTextField();
        jPanelListadoJugadores = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaJugadoresListado = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        tituloTablaJugadores = new javax.swing.JLabel();
        botonCargarJugadores = new javax.swing.JButton();
        tablaJugadoresRespuesta = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(881, 443));
        setPreferredSize(new java.awt.Dimension(940, 809));
        setLayout(new java.awt.BorderLayout());

        jPaneles.setMinimumSize(new java.awt.Dimension(881, 333));
        jPaneles.setPreferredSize(new java.awt.Dimension(940, 333));
        jPaneles.setLayout(new java.awt.GridBagLayout());

        jPanelBajaJugadores.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelBajaJugadores.setMinimumSize(new java.awt.Dimension(750, 161));
        jPanelBajaJugadores.setPreferredSize(new java.awt.Dimension(920, 161));
        jPanelBajaJugadores.setLayout(new java.awt.GridBagLayout());

        tituloBajaJugador.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloBajaJugador.setText("Baja/Modificación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(tituloBajaJugador, gridBagConstraints);

        nombreBajaJugadorLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(nombreBajaJugadorLabel, gridBagConstraints);

        apellidosBajaJugadorLabel.setText("Apellidos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(apellidosBajaJugadorLabel, gridBagConstraints);

        posiciónBajaJugadorLabel.setText("Posición");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(posiciónBajaJugadorLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(bajaJugadorApellidos, gridBagConstraints);

        bajaJugadorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaJugadorNombreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 117;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(bajaJugadorNombre, gridBagConstraints);

        bajaJugadorId.setEditable(false);
        bajaJugadorId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bajaJugadorIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(bajaJugadorId, gridBagConstraints);

        bajaJugadorRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelBajaJugadores.add(bajaJugadorRespuesta, gridBagConstraints);

        idBajaJugadorLabel.setText("id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(idBajaJugadorLabel, gridBagConstraints);

        botonEliminarJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/eliminar.png"))); // NOI18N
        botonEliminarJugador.setText("Eliminar");
        botonEliminarJugador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonEliminarJugadorMouseClicked(evt);
            }
        });
        botonEliminarJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarJugadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(botonEliminarJugador, gridBagConstraints);

        botonModificarJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/actualizarI.png"))); // NOI18N
        botonModificarJugador.setText("Modificar");
        botonModificarJugador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonModificarJugadorMouseClicked(evt);
            }
        });
        botonModificarJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarJugadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(botonModificarJugador, gridBagConstraints);

        bajaJugadorPosicion.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaJugadores.add(bajaJugadorPosicion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPaneles.add(jPanelBajaJugadores, gridBagConstraints);

        jPanelAltaJugadores.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAltaJugadores.setMinimumSize(new java.awt.Dimension(750, 138));
        jPanelAltaJugadores.setPreferredSize(new java.awt.Dimension(920, 132));
        jPanelAltaJugadores.setLayout(new java.awt.GridBagLayout());

        tituloAltaJugador.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloAltaJugador.setText("Alta de jugador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(tituloAltaJugador, gridBagConstraints);

        nombreJugadorLabel.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(nombreJugadorLabel, gridBagConstraints);

        apellidosJugadorLabel.setText("Apellidos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(apellidosJugadorLabel, gridBagConstraints);

        posicionJugadorLabel.setText("Posición");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(posicionJugadorLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(altaJugadorApellidos, gridBagConstraints);

        altaJugadorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altaJugadorNombreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(altaJugadorNombre, gridBagConstraints);

        altaJugadorPosicion.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaJugadores.add(altaJugadorPosicion, gridBagConstraints);

        botonAltaJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/insertarI.png"))); // NOI18N
        botonAltaJugador.setText("Alta");
        botonAltaJugador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonAltaJugadorMouseClicked(evt);
            }
        });
        botonAltaJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAltaJugadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanelAltaJugadores.add(botonAltaJugador, gridBagConstraints);

        altaJugadorRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelAltaJugadores.add(altaJugadorRespuesta, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPaneles.add(jPanelAltaJugadores, gridBagConstraints);

        add(jPaneles, java.awt.BorderLayout.NORTH);

        jPanelListadoJugadores.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelListadoJugadores.setMinimumSize(new java.awt.Dimension(692, 110));
        jPanelListadoJugadores.setPreferredSize(new java.awt.Dimension(456, 476));
        jPanelListadoJugadores.setLayout(new java.awt.BorderLayout());

        tablaJugadoresListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Apellidos", "Posición", "IdJugador"
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
        tablaJugadoresListado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tablaJugadoresListado);

        jPanelListadoJugadores.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.setMinimumSize(new java.awt.Dimension(450, 70));
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 70));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        tituloTablaJugadores.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloTablaJugadores.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloTablaJugadores.setText("Listado de jugadores");
        tituloTablaJugadores.setMaximumSize(new java.awt.Dimension(360, 48));
        tituloTablaJugadores.setMinimumSize(new java.awt.Dimension(360, 48));
        tituloTablaJugadores.setPreferredSize(new java.awt.Dimension(360, 48));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(tituloTablaJugadores, gridBagConstraints);

        botonCargarJugadores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cargarI.png"))); // NOI18N
        botonCargarJugadores.setText("Cargar");
        botonCargarJugadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonCargarJugadoresMouseClicked(evt);
            }
        });
        botonCargarJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarJugadoresActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(botonCargarJugadores, gridBagConstraints);

        tablaJugadoresRespuesta.setEditable(false);
        tablaJugadoresRespuesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tablaJugadoresRespuestaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(tablaJugadoresRespuesta, gridBagConstraints);

        jPanelListadoJugadores.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        add(jPanelListadoJugadores, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void altaJugadorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altaJugadorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_altaJugadorNombreActionPerformed

    private void botonAltaJugadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAltaJugadorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonAltaJugadorMouseClicked

    private void botonAltaJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAltaJugadorActionPerformed
        altaJugador();
    }//GEN-LAST:event_botonAltaJugadorActionPerformed

    private void bajaJugadorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bajaJugadorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bajaJugadorNombreActionPerformed

    private void bajaJugadorIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bajaJugadorIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bajaJugadorIdActionPerformed

    private void botonEliminarJugadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEliminarJugadorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEliminarJugadorMouseClicked

    private void botonEliminarJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarJugadorActionPerformed
        bajaJugador();
    }//GEN-LAST:event_botonEliminarJugadorActionPerformed

    private void botonModificarJugadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonModificarJugadorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonModificarJugadorMouseClicked

    private void botonModificarJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarJugadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonModificarJugadorActionPerformed

    private void botonCargarJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarJugadoresActionPerformed
        cargarTablaJugadores();
    }//GEN-LAST:event_botonCargarJugadoresActionPerformed

    private void botonCargarJugadoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCargarJugadoresMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCargarJugadoresMouseClicked

    private void tablaJugadoresRespuestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tablaJugadoresRespuestaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaJugadoresRespuestaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField altaJugadorApellidos;
    private javax.swing.JTextField altaJugadorNombre;
    private javax.swing.JComboBox<String> altaJugadorPosicion;
    private javax.swing.JTextField altaJugadorRespuesta;
    private javax.swing.JLabel apellidosBajaJugadorLabel;
    private javax.swing.JLabel apellidosJugadorLabel;
    private javax.swing.JTextField bajaJugadorApellidos;
    private javax.swing.JTextField bajaJugadorId;
    private javax.swing.JTextField bajaJugadorNombre;
    private javax.swing.JComboBox<String> bajaJugadorPosicion;
    private javax.swing.JTextField bajaJugadorRespuesta;
    private javax.swing.JButton botonAltaJugador;
    private javax.swing.JButton botonCargarJugadores;
    private javax.swing.JButton botonEliminarJugador;
    private javax.swing.JButton botonModificarJugador;
    private javax.swing.JLabel idBajaJugadorLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAltaJugadores;
    private javax.swing.JPanel jPanelBajaJugadores;
    private javax.swing.JPanel jPanelListadoJugadores;
    private javax.swing.JPanel jPaneles;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nombreBajaJugadorLabel;
    private javax.swing.JLabel nombreJugadorLabel;
    private javax.swing.JLabel posicionJugadorLabel;
    private javax.swing.JLabel posiciónBajaJugadorLabel;
    private javax.swing.JTable tablaJugadoresListado;
    private javax.swing.JTextField tablaJugadoresRespuesta;
    private javax.swing.JLabel tituloAltaJugador;
    private javax.swing.JLabel tituloBajaJugador;
    private javax.swing.JLabel tituloTablaJugadores;
    // End of variables declaration//GEN-END:variables
}
