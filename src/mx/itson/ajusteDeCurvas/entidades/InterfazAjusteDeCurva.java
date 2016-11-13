/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.itson.ajusteDeCurvas.entidades;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Fernando
 */
public class InterfazAjusteDeCurva extends javax.swing.JFrame {

    /**
     * Creates new form InterfazAjusteDeCurva
     */
    public InterfazAjusteDeCurva() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
    
    char[] letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
    //variables para la tabulacion
    int elementos;
    double[][] tabulacion;
    int tipoGrafica;
    
    //arreglos para las multiplicaciones
    double[][] xn, xnPf;
    int numeroDeX, numeroDeMulti;
    
    //arreglos para las sumatorias
    double[] sumX, sumXF;
    
    //matriz
    double[][] matriz;
    
    
    
    
    public void llenarTablaTabulacion(int e) {
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        tabla.addColumn("x");
        tabla.addColumn("y");
        tb_tabulacion.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
    }
    
    public void llenarTabulacion(int e) {
        TableModel tabla = tb_tabulacion.getModel();
        tabulacion = new double[e][2];
        
        for (int i = 0; i < e; i++) // ciclo para añadir datos
        {
            for (int j = 0; j < 2; j++) {
                double numero = Double.parseDouble((String) tabla.getValueAt(i, j));
                
                tabulacion[i][j] = numero;
                //System.out.println("a: " + tabulacion[i][j]);
                
            }
        }
        
        //guardar tipo de grafica
        tipoGrafica = cb_tipoGrafica.getSelectedIndex()+2;
        //System.out.println(tipoGrafica);
        
        //generar
        generarMultiplicaciones();
        generarSumatorias();
        generarMatriz();
        llenarTablaMatriz(tipoGrafica);
        
        aplicarMetodo(tipoGrafica);
        llenarTablaResultados(tipoGrafica);
    }
    
    public void llenarTablaMatriz(int e) {
        //llenar campos de la tabla
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        for (int i = 0; i < e; i++) {
            tabla.addColumn(letras[i]);
        }
        tabla.addColumn("R");
        tb_matriz.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
        //llenar datos de la tabla
        for (int i = 0; i < e; i++) {
            for (int j = 0; j <= e; j++) {
                tabla.setValueAt(matriz[i][j], i, j);
            }
        }
    }
    
    public void llenarTablaResultados(int e) {
        //llenar campos de la tabla
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        tabla.addColumn("Valores");
        tb_resultados.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
        //llenar datos de la tabla
        for (int j = e; j <= e; j++) {
            for (int i = 0; i < e; i++) {
                tabla.setValueAt(matriz[i][j], i, 0);
            }
        }
        
    }
    
    public void generarMultiplicaciones() {
        //xn
        numeroDeX = tipoGrafica + (tipoGrafica-1) - 1; //numero de x que se ocupan para el tipo de grafica
        //System.out.println(numeroDeX);
        xn = new double[elementos][numeroDeX]; //arreglo de x^n
        
        //llenar arreglo xn
        int exponente = 1;
        for (int j = 0; j < numeroDeX; j++) {
            for (int i = 0; i < elementos; i++) {
                //tabulacion
                double valor = Math.pow(tabulacion[i][0] , exponente);
                //System.out.println("valor:"+valor);
                //System.out.println("exponente:"+exponente);
                xn[i][j] = valor;
            }
            exponente++;
            
        }
        exponente = 1;
        
        //xnPf
        numeroDeMulti = tipoGrafica;
        xnPf = new double[elementos][numeroDeMulti];
        
        //llenar arreglo xnPf
        int columnaX = -1;
        for (int j = 0; j < numeroDeMulti; j++) {
            for (int i = 0; i < elementos; i++) {
                double valor;
                if (j == 0) {
                    valor = tabulacion[i][1];
                } else {
                    //se multiplica la columna segun la x que se ocupa por la segunda columna de la tabulacion
                    valor = xn[i][columnaX] * tabulacion[i][1];
                    
                    //System.out.println("columna:"+columnaX);
                }
                //System.out.println("valor:" + valor);
                xnPf[i][j] = valor;
            }
            columnaX++;
        }
        columnaX = -1;
    }
    
    public void generarSumatorias() {
        sumX = new double[numeroDeX]; //arreglo para guardar sumatorias de la matriz xn
        sumXF = new double[numeroDeMulti]; //para guardar sumatorias de la matriz xnPf
        
        //sumX
        for (int j = 0; j < numeroDeX; j++) {
            for (int i = 0; i < elementos; i++) {
                sumX[j] = sumX[j] + xn[i][j];
            }
        }
        
        //sumXF
        for (int j = 0; j < numeroDeMulti; j++) {
            for (int i = 0; i < elementos; i++) {
                sumXF[j] = sumXF[j] + xnPf[i][j];
            }
        }
    }
    
    public void generarMatriz() {
        matriz = new double[tipoGrafica][tipoGrafica+1];
        
        int posicionX = 0, posicionXF = 0, iteracion = 0;
        for (int i = 0; i < tipoGrafica; i++) {
            for (int j = 0; j <= tipoGrafica ; j++) {
                if (i==0 && j==0) {
                    matriz[i][j] = elementos;
                }
                else if (j == (tipoGrafica)) {
                    matriz[i][j] = sumXF[posicionXF];
                    posicionXF++;
                }
                else {
                    matriz[i][j] = sumX[posicionX];
                    posicionX++;
                }
                //System.out.println(matriz[i][j]);
                
            }
            posicionX = 0 + iteracion;
            iteracion++;
        }
        posicionXF = 0;
    }
    
    //Método de gauss-jordan
    public void aplicarMetodo(int e) {
        int p = 0;
        for (int i = 0; i < e; i++) {
            pivote(matriz, p, e);
            hacerCeros(matriz, p, e);
            
            p++;
        }
    }
    
    public void pivote(double matriz[][], int p, int var) {
        double divicion = matriz[p][p]; //guardar sobre cuanto se va a dividir
        
        for (int y = 0; y < (var + 1); y++) { //recorrer la fila para ir dividiendo
            double resultado = matriz[p][y] / divicion;
            matriz[p][y] = resultado;
        }
    }
    
    public void hacerCeros(double matriz[][], int p, int var) {
        for (int x = 0; x < var; x++) { //recorrer filas
            
            if (x != p) { //si el pivote no concuerda con la fila en la que la esta
                double constante = matriz[x][p]; //guardar constante del valor de la fila que se va a restar
                
                for (int z = 0; z < (var + 1); z++) { //recorrer columnas
                    double resultado = ((-1 * constante) * matriz[p][z]) + matriz[x][z]; //la matriz se le va a restar tantas veces el pivote por su valor actual
                    matriz[x][z] = resultado;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_resultado = new javax.swing.JTextField();
        btn_graficar = new javax.swing.JButton();
        txt_elementos = new javax.swing.JTextField();
        btn_OK = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_tabulacion = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cb_tipoGrafica = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_matriz = new javax.swing.JTable();
        btn_calcular = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_resultados = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ajuste de curvas");

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Calculando una curva");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 620, 29);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Elementos de la tabulación:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(130, 90, 160, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Función resultante:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(80, 340, 120, 20);
        jPanel1.add(txt_resultado);
        txt_resultado.setBounds(210, 340, 260, 20);

        btn_graficar.setText("Graficar");
        jPanel1.add(btn_graficar);
        btn_graficar.setBounds(480, 330, 73, 40);
        jPanel1.add(txt_elementos);
        txt_elementos.setBounds(300, 90, 110, 20);

        btn_OK.setText("OK");
        btn_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OKActionPerformed(evt);
            }
        });
        jPanel1.add(btn_OK);
        btn_OK.setBounds(420, 70, 60, 30);

        tb_tabulacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tb_tabulacion);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 150, 140, 130);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de gráfica:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(180, 60, 110, 20);

        cb_tipoGrafica.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cb_tipoGrafica.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recta", "Parábola", "Cúbica", "Grado 4", "Grado 5", "Grado 6" }));
        jPanel1.add(cb_tipoGrafica);
        cb_tipoGrafica.setBounds(300, 60, 110, 21);

        tb_matriz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tb_matriz);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(180, 150, 320, 160);

        btn_calcular.setText("Calcular");
        btn_calcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_calcularActionPerformed(evt);
            }
        });
        jPanel1.add(btn_calcular);
        btn_calcular.setBounds(50, 293, 80, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tabular:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(60, 130, 60, 15);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Resultados:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(520, 130, 100, 15);

        tb_resultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tb_resultados);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(520, 150, 100, 160);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Resultado de la matriz:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(270, 130, 140, 15);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OKActionPerformed
        elementos = Integer.parseInt(txt_elementos.getText());
        llenarTablaTabulacion(elementos);
    }//GEN-LAST:event_btn_OKActionPerformed

    private void btn_calcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_calcularActionPerformed
        llenarTabulacion(elementos);
    }//GEN-LAST:event_btn_calcularActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazAjusteDeCurva().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_OK;
    private javax.swing.JButton btn_calcular;
    private javax.swing.JButton btn_graficar;
    private javax.swing.JComboBox<String> cb_tipoGrafica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tb_matriz;
    private javax.swing.JTable tb_resultados;
    private javax.swing.JTable tb_tabulacion;
    private javax.swing.JTextField txt_elementos;
    private javax.swing.JTextField txt_resultado;
    // End of variables declaration//GEN-END:variables
}
