/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package edu.cotarelo.sistema.vistas;

import edu.cotarelo.dao.factories.MySQLFactory;
import edu.cotarelo.dao.objects.PartidoDAO;
import edu.cotarelo.dao.objects.ClubDAO;
import edu.cotarelo.domain.Club;
import edu.cotarelo.domain.Partido;
import edu.cotarelo.sistema.Sistema;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.swing.table.DefaultTableModel;

/**
 * La clase VistaPartidos es una interfaz gráfica que permite gestionar la
 * información de partidos en una base de datos. Permite dar de alta, modificar,
 * y dar de baja partidos, así como cargar un listado de partidos desde la base
 * de datos.
 *
 * @author Dante Sarotti
 */
public class VistaPartidos extends javax.swing.JPanel {

    private ArrayList<Partido> partidos;

    /**
     * Creates new form Partidos
     */
    public VistaPartidos() {
        initComponents();

        //Carga los equipos que deben ir en los dropdowns
        cargarDropdowns();
    }

    /**
     * Carga los equipos en los dropdowns de alta y baja partidos.
     */
    private void cargarDropdowns() {

        MySQLFactory factory = new MySQLFactory();
        ClubDAO equipoDAO = factory.getClubDAO();
        ArrayList<Club> listaEquipos = equipoDAO.getLista(1);
        for (Club equipo : listaEquipos) {
            altaPartidoEquipo1.addItem(equipo.getIdClub());
            altaPartidoEquipo2.addItem(equipo.getIdClub());

            bajaPartidoEquipo1.addItem(equipo.getIdClub());
            bajaPartidoEquipo2.addItem(equipo.getIdClub());

        }

        //deseleccionar los dropdowns de alta y baja
        altaPartidoEquipo1.setSelectedIndex(-1);
        altaPartidoEquipo2.setSelectedIndex(-1);
        bajaPartidoEquipo1.setSelectedIndex(-1);
        bajaPartidoEquipo2.setSelectedIndex(-1);
    }

    /**
     * Da de alta un nuevo partido en la base de datos.
     */
    private void altaPartido() {
        MySQLFactory factory = new MySQLFactory();
        ClubDAO equipo = factory.getClubDAO();
        PartidoDAO partidoDAO = factory.getPartidoDAO();
        try {
            Partido nuevo = new Partido(
                    equipo.getClubById(altaPartidoEquipo1.getSelectedItem().toString()),
                    equipo.getClubById(altaPartidoEquipo2.getSelectedItem().toString())
            );
            if (altaPartidoEquipo1.getSelectedItem().toString().equals(altaPartidoEquipo2.getSelectedItem().toString())) {
                altaPartidoRespuesta.setForeground(Color.red);
                altaPartidoRespuesta.setText("Debe seleccionar dos equipos distintos");
            } else {
                int salida = partidoDAO.insertar(nuevo);
                if (salida < 0) {
                    altaPartidoRespuesta.setForeground(Color.red);
                    altaPartidoRespuesta.setText("No se ha podido dar de alta al partido");
                } else {
                    altaPartidoRespuesta.setForeground(Color.blue);
                    altaPartidoRespuesta.setText("El partido ha sido dado de alta correctamente");
                    partidos.add(nuevo);
                }
            }

        } catch (NamingException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los partidos desde la base de datos y los presenta en la tabla.
     */
    private void cargarTablaPartidos() {
        DefaultTableModel tm = (DefaultTableModel) tablaPartidosListado.getModel();
        tm.getDataVector().removeAllElements();
        tm.fireTableDataChanged();

        //pedimos los partidos
        MySQLFactory factoria = new MySQLFactory();
        PartidoDAO partidoDAO = factoria.getPartidoDAO();

        partidos = partidoDAO.getlistaPartidos();
        if (partidos == null || partidos.isEmpty()) {
            tablaPartidosRespuesta.setForeground(Color.red);
            tablaPartidosRespuesta.setText("No hay partidos en la bbdd");
        } else {
            for (int i = 0; i < partidos.size(); i++) {
                Object[] auxObject = new Object[4];
                auxObject[0] = partidos.get(i).getIdClub1().getIdClub();
                auxObject[1] = partidos.get(i).getIdClub2().getIdClub();
                auxObject[2] = partidos.get(i).getfecha() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(partidos.get(i).getfecha()) : "";

                tm.addRow(auxObject);
            }
            tablaPartidosRespuesta.setForeground(Color.blue);
            tablaPartidosRespuesta.setText("Se cargaron los partidos");
        }
    }

    /**
     * Da de baja un partido en la base de datos.
     */
    private void bajaPartido() {
        MySQLFactory factoria = new MySQLFactory();
        PartidoDAO partidoDAO = factoria.getPartidoDAO();
        try {
            Partido partido = null;
            if (!bajaPartidoFecha.getText().isBlank()) {
                try {
                    partido = partidoDAO.getPartidoById(bajaPartidoEquipo1.getSelectedItem().toString(), bajaPartidoEquipo2.getSelectedItem().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bajaPartidoFecha.getText()));
                } catch (ParseException ex) {
                    Logger.getLogger(VistaPartidos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (partido != null) {
                if (partidoDAO.borrar(partido) < 0) {
                    bajaPartidoRespuesta.setForeground(Color.red);
                    bajaPartidoRespuesta.setText("No se ha podido borrar el partido");
                } else {
                    bajaPartidoRespuesta.setForeground(Color.blue);
                    bajaPartidoRespuesta.setText("Se ha borrado el partido");
                }
            } else {
                bajaPartidoRespuesta.setForeground(Color.red);
                bajaPartidoRespuesta.setText("El partido seleccionado no existe en la base de datos, seleccione uno del listado.");
            }
        } catch (NamingException ex) {
            Logger.getLogger(VistaPartidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Modifica un partido en la base de datos.
     */
    private void modificarPartido() {
        if (bajaPartidoEquipo1.getSelectedItem() == null || bajaPartidoEquipo2.getSelectedItem() == null) {
            bajaPartidoRespuesta.setForeground(Color.red);
            bajaPartidoRespuesta.setText("Debe seleccionar un partido de la lista");
        } else {
            if (bajaPartidoEquipo1.getSelectedItem().toString().isBlank() || bajaPartidoEquipo2.getSelectedItem().toString().isBlank()) {
                bajaPartidoRespuesta.setForeground(Color.red);
                bajaPartidoRespuesta.setText("Debe seleccionar ambos equipos");
            } else if (bajaPartidoEquipo1.getSelectedItem().toString().equals(bajaPartidoEquipo2.getSelectedItem().toString())) {
                bajaPartidoRespuesta.setForeground(Color.red);
                bajaPartidoRespuesta.setText("Los equipos deben ser distintos");
            } else if (bajaPartidoEquipo1.getSelectedItem().toString().equals(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 0).toString())
                    && bajaPartidoEquipo2.getSelectedItem().toString().equals(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 1).toString())) {

                bajaPartidoRespuesta.setForeground(Color.red);
                bajaPartidoRespuesta.setText("No se ha modificado el partido");
            } else {
                MySQLFactory factoria = new MySQLFactory();
                PartidoDAO partidoDAO = factoria.getPartidoDAO();
                ClubDAO clubDAO = factoria.getClubDAO();
                try {
                    Partido viejoPartido = partidoDAO.getPartidoById(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 0).toString(), tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 1).toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bajaPartidoFecha.getText()));
                    Club equipoNuevoPartido1 = clubDAO.getClubById(bajaPartidoEquipo1.getSelectedItem().toString());
                    Club equipoNuevoPartido2 = clubDAO.getClubById(bajaPartidoEquipo2.getSelectedItem().toString());
                    Partido nuevoPartido = new Partido(equipoNuevoPartido1, equipoNuevoPartido2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bajaPartidoFecha.getText()));
                    if (partidoDAO.modificar(viejoPartido, nuevoPartido) < 1) {
                        bajaPartidoRespuesta.setForeground(Color.red);
                        bajaPartidoRespuesta.setText("No se ha podido modificar el partido");
                    } else {
                        //actualizar tabla
                        DefaultTableModel tabla = (DefaultTableModel) tablaPartidosListado.getModel();
                        for (int i = 0; i < tabla.getRowCount(); i++) {
                            if (tabla.getValueAt(i, 0).toString().equals(viejoPartido.getIdClub1().getIdClub()) && tabla.getValueAt(i, 1).toString().equals(viejoPartido.getIdClub2().getIdClub()) && tabla.getValueAt(i, 2).toString().equals(bajaPartidoFecha.getText())) {
                                tabla.setValueAt(equipoNuevoPartido1.getIdClub(), i, 0);
                                tabla.setValueAt(equipoNuevoPartido2.getIdClub(), i, 1);
                                break;
                            }
                        }

                        //actualizar mensaje
                        bajaPartidoRespuesta.setForeground(Color.blue);
                        bajaPartidoRespuesta.setText("El partido se ha modificado correctamente");
                    }
                } catch (ParseException | NamingException ex) {
                    Logger.getLogger(VistaPartidos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        jPanelAltaPartidos = new javax.swing.JPanel();
        tituloAltaPartido = new javax.swing.JLabel();
        nombrePartidoLabel = new javax.swing.JLabel();
        apellidosPartidoLabel = new javax.swing.JLabel();
        botonAltaPartido = new javax.swing.JButton();
        altaPartidoRespuesta = new javax.swing.JTextField();
        altaPartidoEquipo2 = new javax.swing.JComboBox<>();
        altaPartidoEquipo1 = new javax.swing.JComboBox<>();
        jPanelBajaPartidos = new javax.swing.JPanel();
        tituloBajaPartido = new javax.swing.JLabel();
        nombreBajaPartidoLabel = new javax.swing.JLabel();
        apellidosBajaPartidoLabel = new javax.swing.JLabel();
        bajaPartidoFecha = new javax.swing.JTextField();
        bajaPartidoRespuesta = new javax.swing.JTextField();
        idBajaPartidoLabel = new javax.swing.JLabel();
        botonEliminarPartido = new javax.swing.JButton();
        botonModificarPartido = new javax.swing.JButton();
        bajaPartidoEquipo1 = new javax.swing.JComboBox<>();
        bajaPartidoEquipo2 = new javax.swing.JComboBox<>();
        jPanelListadoPartido = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        tituloTablaPartidos = new javax.swing.JLabel();
        botonCargarPartidos = new javax.swing.JButton();
        tablaPartidosRespuesta = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPartidosListado = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout());

        jPaneles.setBackground(new java.awt.Color(0, 102, 51));
        jPaneles.setMinimumSize(new java.awt.Dimension(881, 333));
        jPaneles.setLayout(new java.awt.GridBagLayout());

        jPanelAltaPartidos.setBackground(new java.awt.Color(0, 153, 51));
        jPanelAltaPartidos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAltaPartidos.setMinimumSize(new java.awt.Dimension(750, 138));
        jPanelAltaPartidos.setPreferredSize(new java.awt.Dimension(920, 132));
        jPanelAltaPartidos.setLayout(new java.awt.GridBagLayout());

        tituloAltaPartido.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloAltaPartido.setText("Alta de partido");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaPartidos.add(tituloAltaPartido, gridBagConstraints);

        nombrePartidoLabel.setText("Equipo 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaPartidos.add(nombrePartidoLabel, gridBagConstraints);

        apellidosPartidoLabel.setText("Equipo 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaPartidos.add(apellidosPartidoLabel, gridBagConstraints);

        botonAltaPartido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/insertarI.png"))); // NOI18N
        botonAltaPartido.setText("Alta");
        botonAltaPartido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAltaPartidoActionPerformed(evt);
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
        jPanelAltaPartidos.add(botonAltaPartido, gridBagConstraints);

        altaPartidoRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelAltaPartidos.add(altaPartidoRespuesta, gridBagConstraints);

        altaPartidoEquipo2.setSelectedItem("Seleccione un equipo");
        altaPartidoEquipo2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaPartidos.add(altaPartidoEquipo2, gridBagConstraints);

        altaPartidoEquipo1.setSelectedItem("Seleccione un equipo");
        altaPartidoEquipo1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAltaPartidos.add(altaPartidoEquipo1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPaneles.add(jPanelAltaPartidos, gridBagConstraints);

        jPanelBajaPartidos.setBackground(new java.awt.Color(0, 153, 51));
        jPanelBajaPartidos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelBajaPartidos.setMinimumSize(new java.awt.Dimension(750, 161));
        jPanelBajaPartidos.setPreferredSize(new java.awt.Dimension(920, 161));
        jPanelBajaPartidos.setLayout(new java.awt.GridBagLayout());

        tituloBajaPartido.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloBajaPartido.setText("Baja/Modificación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(tituloBajaPartido, gridBagConstraints);

        nombreBajaPartidoLabel.setText("Equipo 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(nombreBajaPartidoLabel, gridBagConstraints);

        apellidosBajaPartidoLabel.setText("Equipo 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(apellidosBajaPartidoLabel, gridBagConstraints);

        bajaPartidoFecha.setEditable(false);
        bajaPartidoFecha.setMinimumSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(bajaPartidoFecha, gridBagConstraints);

        bajaPartidoRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelBajaPartidos.add(bajaPartidoRespuesta, gridBagConstraints);

        idBajaPartidoLabel.setText("Fecha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(idBajaPartidoLabel, gridBagConstraints);

        botonEliminarPartido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/eliminar.png"))); // NOI18N
        botonEliminarPartido.setText("Eliminar");
        botonEliminarPartido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarPartidoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(botonEliminarPartido, gridBagConstraints);

        botonModificarPartido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/actualizarI.png"))); // NOI18N
        botonModificarPartido.setText("Modificar");
        botonModificarPartido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarPartidoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(botonModificarPartido, gridBagConstraints);

        bajaPartidoEquipo1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(bajaPartidoEquipo1, gridBagConstraints);

        bajaPartidoEquipo2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBajaPartidos.add(bajaPartidoEquipo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPaneles.add(jPanelBajaPartidos, gridBagConstraints);

        add(jPaneles, java.awt.BorderLayout.NORTH);

        jPanelListadoPartido.setBackground(new java.awt.Color(0, 153, 51));
        jPanelListadoPartido.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelListadoPartido.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelListadoPartido.setPreferredSize(new java.awt.Dimension(1, 1));
        jPanelListadoPartido.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(450, 70));
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 70));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        tituloTablaPartidos.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tituloTablaPartidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloTablaPartidos.setText("Listado de partidos");
        tituloTablaPartidos.setMaximumSize(new java.awt.Dimension(360, 48));
        tituloTablaPartidos.setMinimumSize(new java.awt.Dimension(360, 48));
        tituloTablaPartidos.setPreferredSize(new java.awt.Dimension(360, 48));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(tituloTablaPartidos, gridBagConstraints);

        botonCargarPartidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/cotarelo/img/cargarI.png"))); // NOI18N
        botonCargarPartidos.setText("Cargar");
        botonCargarPartidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarPartidosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(botonCargarPartidos, gridBagConstraints);

        tablaPartidosRespuesta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(tablaPartidosRespuesta, gridBagConstraints);

        jPanelListadoPartido.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        tablaPartidosListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Equipo1", "Equipo2", "fecha"
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
        tablaPartidosListado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaPartidosListado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaPartidosListadoMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tablaPartidosListado);

        jPanelListadoPartido.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanelListadoPartido, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void botonEliminarPartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarPartidoActionPerformed
        bajaPartido();
    }//GEN-LAST:event_botonEliminarPartidoActionPerformed

    private void botonModificarPartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarPartidoActionPerformed
        modificarPartido();
    }//GEN-LAST:event_botonModificarPartidoActionPerformed

    private void botonAltaPartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAltaPartidoActionPerformed
        if (altaPartidoEquipo1.getSelectedItem() != null && altaPartidoEquipo2.getSelectedItem() != null) {
            altaPartido();
        } else {
            altaPartidoRespuesta.setForeground(Color.red);
            altaPartidoRespuesta.setText("Debe seleccionar ambos equipos");
        }
    }//GEN-LAST:event_botonAltaPartidoActionPerformed

    private void botonCargarPartidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarPartidosActionPerformed
        cargarTablaPartidos();
    }//GEN-LAST:event_botonCargarPartidosActionPerformed

    private void tablaPartidosListadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPartidosListadoMousePressed
        bajaPartidoEquipo1.setSelectedItem(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 0).toString());
        bajaPartidoEquipo2.setSelectedItem(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 1).toString());

        // Establecer la fecha solo si existen los equipos
        Object item1 = bajaPartidoEquipo1.getSelectedItem();
        Object item2 = bajaPartidoEquipo2.getSelectedItem();
        if (item1 != null && item2 != null) {
            if (bajaPartidoEquipo1.getSelectedItem().toString().equals(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 0).toString())
                    && bajaPartidoEquipo2.getSelectedItem().toString().equals(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 1).toString())) {
                bajaPartidoFecha.setText(tablaPartidosListado.getModel().getValueAt(tablaPartidosListado.getSelectedRow(), 2).toString());
            }
        }
    }//GEN-LAST:event_tablaPartidosListadoMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> altaPartidoEquipo1;
    private javax.swing.JComboBox<String> altaPartidoEquipo2;
    private javax.swing.JTextField altaPartidoRespuesta;
    private javax.swing.JLabel apellidosBajaPartidoLabel;
    private javax.swing.JLabel apellidosPartidoLabel;
    private javax.swing.JComboBox<String> bajaPartidoEquipo1;
    private javax.swing.JComboBox<String> bajaPartidoEquipo2;
    private javax.swing.JTextField bajaPartidoFecha;
    private javax.swing.JTextField bajaPartidoRespuesta;
    private javax.swing.JButton botonAltaPartido;
    private javax.swing.JButton botonCargarPartidos;
    private javax.swing.JButton botonEliminarPartido;
    private javax.swing.JButton botonModificarPartido;
    private javax.swing.JLabel idBajaPartidoLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAltaPartidos;
    private javax.swing.JPanel jPanelBajaPartidos;
    private javax.swing.JPanel jPanelListadoPartido;
    private javax.swing.JPanel jPaneles;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nombreBajaPartidoLabel;
    private javax.swing.JLabel nombrePartidoLabel;
    private javax.swing.JTable tablaPartidosListado;
    private javax.swing.JTextField tablaPartidosRespuesta;
    private javax.swing.JLabel tituloAltaPartido;
    private javax.swing.JLabel tituloBajaPartido;
    private javax.swing.JLabel tituloTablaPartidos;
    // End of variables declaration//GEN-END:variables
}
