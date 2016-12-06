import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by mbmarkus on 28/09/16.
 */



public class WekaPodo_Tipo extends JFrame implements ActionListener, ChangeListener {

    private NaiveBayes clsNB;
    private JTextField footlengthinput;
    private JTextField bridgewidthinput;
    private JTextField forefootwidthinput;
    private JTextField heelwidthinput;
    private JTextField footareainput;
    public double footlengthdouble;
    public double bridgewidthdouble;
    public double forefootwidthdouble;
    public double heelwidthdouble;
    public double footareadouble;

    private JRadioButton radio1,radio2;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private ButtonGroup bg;
    private JButton boton1;
    private JButton boton2;
    private JTextArea textarea1;

    private String Pisada;


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==boton1) {

            try {
                createClass();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            footlengthdouble = Double.parseDouble(footlengthinput.getText());
            bridgewidthdouble = Double.parseDouble(bridgewidthinput.getText());
            forefootwidthdouble = Double.parseDouble(forefootwidthinput.getText());
            heelwidthdouble = Double.parseDouble(heelwidthinput.getText());
            footareadouble = Double.parseDouble(footareainput.getText());

            try {
                Instance foot = ClassFoot(footlengthdouble,bridgewidthdouble, forefootwidthdouble,
                        heelwidthdouble, footareadouble, Pisada);

                double result = clsNB.classifyInstance(foot);

                System.out.println(result);

                //Limpiamos el Panel
                textarea1.setText("");

                textarea1.append("Resultado:"+ '\n');
                switch ((int) result){
                    case 0:
                        textarea1.append("Plano"+ '\n');
                        break;
                    case 1:
                        textarea1.append("Cavo"+ '\n');
                        break;
                    case 2:
                        textarea1.append("Normal"+ '\n');
                        break;
                    default:
                        textarea1.append("Error en el proceso"+ '\n');
                        break;
                }

                textarea1.append("\n");

                double[] distribution = clsNB.distributionForInstance(foot);
                textarea1.append("Distribución por Instancias:"+ '\n');
                textarea1.append("\n");

                int index = 0;
                for(double dis : distribution){
                    switch (index){
                        case 0:
                            textarea1.append("Plano: ");
                            break;
                        case 1:
                            textarea1.append("Cavo: ");
                            break;
                        case 2:
                            textarea1.append("Normal: ");
                            break;
                    }
                    index++;
                    textarea1.append(String.valueOf(new DecimalFormat("#.####").format(dis*100)) +" %"+'\n');
                    System.out.println(dis);
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource()==boton2) {
            System.exit(0);
        }
    }
    public void stateChanged(ChangeEvent e) {
        if (radio1.isSelected()) {
            Pisada = "Pronadora";
        }
        if (radio2.isSelected()) {
            Pisada = "Supinadora";
        }
    }

    public void createClass() throws Exception{
        Instances train = ConverterUtils.DataSource.read("pieses_v4.arff");
        train.setClassIndex(train.numAttributes() - 1);

        //Creamos el clasificador
        clsNB = new NaiveBayes();
        clsNB.buildClassifier(train);
    }



    public WekaPodo_Tipo(){
        setLayout(null);
        setTitle("WekaPodo - 2016 - Tipo");

        label1=new JLabel("Autores: Marc Bernat, Marc Cabezas, Toni Mas");
        label1.setBounds(10,10,400,30);
        add(label1);

        label2=new JLabel("Longitud del Pie [cm]:");
        label2.setBounds(10,60,210,20);
        add(label2);
        footlengthinput=new JTextField();
        footlengthinput.setBounds(210,60,150,20);
        add(footlengthinput);

        label3=new JLabel("Amplitud del puente [cm]:");
        label3.setBounds(10,85,230,20);
        add(label3);
        bridgewidthinput=new JTextField();
        bridgewidthinput.setBounds(210,85,150,20);
        add(bridgewidthinput);

        label4=new JLabel("Ancho antepíe [cm]:");
        label4.setBounds(10,110,230,20);
        add(label4);
        forefootwidthinput=new JTextField();
        forefootwidthinput.setBounds(210,110,150,20);
        add(forefootwidthinput);

        label5=new JLabel("Ancho del talón [cm]:");
        label5.setBounds(10,135,230,20);
        add(label5);
        heelwidthinput=new JTextField();
        heelwidthinput.setBounds(210,135,150,20);
        add(heelwidthinput);

        label6=new JLabel("Area del pie [cm²]:");
        label6.setBounds(10,160,230,20);
        add(label6);
        footareainput=new JTextField();
        footareainput.setBounds(210,160,150,20);
        add(footareainput);

        label6=new JLabel("Pisada:");
        label6.setBounds(10,195,230,20);
        add(label6);

        //Botones
        bg=new ButtonGroup();
        radio1=new JRadioButton("Pronadora");
        radio1.setBounds(10,210,100,30);
        radio1.addChangeListener(this);
        add(radio1);
        bg.add(radio1);
        radio2=new JRadioButton("Estandard");
        radio2.setBounds(110,210,100,30);
        radio2.addChangeListener(this);
        add(radio2);
        bg.add(radio2);

        boton1=new JButton("Aplicar");
        boton1.setBounds(10,270,100,30);
        add(boton1);
        boton1.addActionListener(this);

        boton2=new JButton("Finalizar");
        boton2.setBounds(120,270,100,30);
        add(boton2);
        boton2.addActionListener(this);

        //Resultados
        textarea1=new JTextArea();
        textarea1.setBounds(430,50,300,200);
        textarea1.setMargin(new Insets(20,20,10,10));
        //textarea1.setBackground(Color.black);
        //textarea1.setForeground(Color.white);
        add(textarea1);

    }

    public Instance ClassFoot(double footlengthi, double bridgewidthi, double forefootwidthi, double heelwidthi,
                          double footareai, String footprinti) throws Exception {
        Instance inst_foot = null;

        try{
            ArrayList<Attribute> attributeList = new ArrayList<Attribute>(7);

            /**
             * @ATTRIBUTE footlenght	REAL
             @ATTRIBUTE bridgewidth 	REAL
             @ATTRIBUTE forefootwidth	REAL
             @ATTRIBUTE heelwidth	REAL
             @ATTRIBUTE footarea	REAL
             @ATTRIBUTE footprint {Pronadora, Estandard}
             */

            Attribute footlenght = new Attribute("footlenght");
            Attribute bridgewidth = new Attribute("bridgewidth");
            Attribute forefootwidth = new Attribute("forefootwidth");
            Attribute heelwidth = new Attribute("heelwidth");
            Attribute footarea = new Attribute("footarea");

            FastVector fvClassVal1 = new FastVector(2);
            fvClassVal1.addElement("Pronadora");
            fvClassVal1.addElement("Estandard");
            Attribute footprint = new Attribute("footprint", fvClassVal1);

            FastVector fvClassVal2 = new FastVector(2);
            fvClassVal2.addElement("Plano");
            fvClassVal2.addElement("Cavo");
            fvClassVal2.addElement("Normal");
            Attribute ClassAttribute = new Attribute("class", fvClassVal2);

            attributeList.add(footlenght);
            attributeList.add(bridgewidth);
            attributeList.add(forefootwidth);
            attributeList.add(heelwidth);
            attributeList.add(footarea);
            attributeList.add(footprint);
            attributeList.add(ClassAttribute);

            Instances data = new Instances("FootInstances",attributeList,0);
            data.setClassIndex(data.numAttributes() - 1);

            inst_foot = new DenseInstance(data.numAttributes());

            inst_foot.setValue(footlenght, footlengthi);
            inst_foot.setValue(bridgewidth, bridgewidthi);
            inst_foot.setValue(forefootwidth, forefootwidthi);
            inst_foot.setValue(heelwidth, heelwidthi);
            inst_foot.setValue(footarea, footareai);
            inst_foot.setValue(footprint, footprinti);
            inst_foot.setDataset(data);

            data.add(inst_foot);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return inst_foot;

    }

    public static void main(String[] args) throws Exception {
        // load data
        WekaPodo_Tipo formulario1=new WekaPodo_Tipo();
        formulario1.setBounds(0,0,800,350);
        formulario1.setVisible(true);
    }
}
