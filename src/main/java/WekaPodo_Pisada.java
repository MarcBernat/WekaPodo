import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by mbmarkus on 28/09/16.
 */



public class WekaPodo_Pisada extends JFrame implements ActionListener {

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

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JButton boton1;
    private JButton boton2;
    private JTextArea textarea1;


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==boton1) {

            try {
                createClass();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            bridgewidthdouble = Double.parseDouble(bridgewidthinput.getText());
            forefootwidthdouble = Double.parseDouble(forefootwidthinput.getText());
            heelwidthdouble = Double.parseDouble(heelwidthinput.getText());
            footareadouble = Double.parseDouble(footareainput.getText());

            try {
                Instance foot = ClassFoot(bridgewidthdouble, forefootwidthdouble,
                        heelwidthdouble, footareadouble);

                double result = clsNB.classifyInstance(foot);

                System.out.println(result);

                //Limpiamos el Panel
                textarea1.setText("");

                textarea1.append("Resultado:"+ '\n');
                switch ((int) result){
                    case 0:
                        textarea1.append("Pronadora"+ '\n');
                        break;
                    case 1:
                        textarea1.append("Estandard"+ '\n');
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
                            textarea1.append("Pronadora: ");
                            break;
                        case 1:
                            textarea1.append("Estandard: ");
                            break;
                    }
                    index++;
                    textarea1.append(String.valueOf(new DecimalFormat("#.##").format(dis*100)) +" %"+'\n');
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

    public void createClass() throws Exception{
        Instances train = ConverterUtils.DataSource.read("pieses_v5.arff");
        train.setClassIndex(train.numAttributes() - 1);

        //Creamos el clasificador
        clsNB = new NaiveBayes();
        clsNB.buildClassifier(train);
    }



    public WekaPodo_Pisada(){
        setLayout(null);
        setTitle("WekaPodo - 2016 - Pisada");

        label1=new JLabel("Autores: Marc Bernat, Marc Cabezas, Toni Mas");
        label1.setBounds(10,10,400,30);
        add(label1);

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

    public Instance ClassFoot(double bridgewidthi, double forefootwidthi, double heelwidthi,
                          double footareai) throws Exception {
        Instance inst_foot = null;

        try{
            ArrayList<Attribute> attributeList = new ArrayList<Attribute>(5);

            /**
             @attribute bridgewidth numeric
             @attribute forefootwidth numeric
             @attribute heelwidth numeric
             @attribute footarea numeric
             @attribute footprint {Pronadora,Estandard}
             */

            Attribute bridgewidth = new Attribute("bridgewidth");
            Attribute forefootwidth = new Attribute("forefootwidth");
            Attribute heelwidth = new Attribute("heelwidth");
            Attribute footarea = new Attribute("footarea");;

            FastVector fvClassVal = new FastVector(2);
            fvClassVal.addElement("Pronadora");
            fvClassVal.addElement("Supinadora");
            Attribute ClassAttribute = new Attribute("class", fvClassVal);


            attributeList.add(bridgewidth);
            attributeList.add(forefootwidth);
            attributeList.add(heelwidth);
            attributeList.add(footarea);
            attributeList.add(ClassAttribute);

            Instances data = new Instances("FootInstances",attributeList,0);
            data.setClassIndex(data.numAttributes() - 1);

            inst_foot = new DenseInstance(data.numAttributes());

            inst_foot.setValue(bridgewidth, bridgewidthi);
            inst_foot.setValue(forefootwidth, forefootwidthi);
            inst_foot.setValue(heelwidth, heelwidthi);
            inst_foot.setValue(footarea, footareai);
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
        WekaPodo_Pisada formulario1=new WekaPodo_Pisada();
        formulario1.setBounds(0,0,800,350);
        formulario1.setVisible(true);
    }
}
