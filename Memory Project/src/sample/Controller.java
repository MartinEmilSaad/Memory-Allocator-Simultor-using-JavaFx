package sample;

import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

//import java.awt.ScrollPane;
//import java.awt.ScrollPane;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

import java.awt.*;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;



import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import javax.swing.table.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

//import javafx.scene.control.ScrollPane;


public class Controller implements Initializable {

    enum me {best , first};

    me methodology;


    static Vector<memory>locations=new Vector<>();
    static Vector<process>processes=new Vector<>();
    protected static int id = 0 ;
    static int total = 0 ;
    ObservableList<memory>list1=FXCollections.observableArrayList();

    ObservableList<process>list2=FXCollections.observableArrayList();



    public void determine_total(){
        total = Integer.parseInt(total_size.getText());
    }

    public void add_memory(){
       memory m = new memory(Integer.parseInt(address.getText()),Double.parseDouble(memory_size.getText()));
       locations.add(m);
       System.out.println(locations.get(locations.size()-1).getSize());
       draw_memory_table();
    }

    public void delete_last_location(){
        if(locations.size()>0) {
            locations.remove(locations.size() - 1);
            draw_memory_table();
        }
    }

    public void arrange_memory(Vector<memory>a){
        for (int i = 0 ; i<a.size()-1;i++)
        {
            for (int j = 0;j<a.size()-i-1;j++ )
            {
                if(a.get(j).getAddress()>a.get(j+1).getAddress())
                {
                    int temp = a.get(j).getAddress();
                    double temp2 = a.get(j).getSize();
                    a.get(j).setAddress(a.get(j+1).getAddress());
                    a.get(j).setSize(a.get(j+1).getSize());
                    a.get(j+1).setAddress(temp);
                    a.get(j+1).setSize(temp2);
                }
            }
        }
        for(int i = 0 ; i<a.size() ; i++){
            System.out.println(a.get(i).getSize());
        }
    }


    void add_adjacent_holes(){
        int j = 0 ;
        while(j<locations.size()) {
            for (int i = 1; i < locations.size(); i++) {
                if (locations.get(i).address == (locations.get(i - 1).address + locations.get(i - 1).getSize())) {
                    locations.get(i - 1).size = (locations.get(i - 1).size + locations.get(i).size);
                    locations.get(i-1).can_be_divided = true;
                    locations.remove(i);
                }
            }
            j++;
        }
        draw_memory_table();
    }


    public void add_process(){
        process p = new process(id,Double.parseDouble(process_size.getText()));
        processes.add(p);
        id++;
        draw_process_table();
    }

    public void delete_last_process(){
        if(processes.size()>0){
            processes.remove(processes.size()-1);
            id--;
            draw_process_table();
        }
    }

    public void set_methodology() {

        String me = method.getSelectionModel().getSelectedItem().toString();
        if (me == "First Fit") methodology= Controller.me.first;
        if(me=="Best Fit") methodology= Controller.me.best;
        System.out.println(methodology.toString());

    }



    public void first_fit_method(){
        for(int i = 0 ; i<processes.size();i++){
            for(int j = 0 ; j<locations.size();j++){
                if(locations.get(j).getSize()>=processes.get(i).size&&locations.get(j).empty
                        &&processes.get(i).deallocated==false) {
                    locations.get(j).empty = false;
                    processes.get(i).assigned_memory=locations.get(j);
                    locations.get(j).contained =  processes.get(i);

                    if(locations.get(j).size>processes.get(i).size&&locations.get(j).can_be_divided){
                        double new_address;
                        if( processes.get(i).size/(int)(processes.get(i).size)==1 )
                        new_address = (locations.get(j).address+processes.get(i).size);
                        else
                            new_address = (locations.get(j).address+processes.get(i).size+1);
                       int nad=(int)new_address;
                       memory temp ;
                       if(processes.get(i).size/(int)(processes.get(i).size)==1)
                           temp = new memory(nad,locations.get(j).size-processes.get(i).size);
                       else
                           temp=new memory(nad , (int)(locations.get(j).size-
                                   (processes.get(i).size+1)));
                       locations.insertElementAt( temp,j+1);
                       locations.get(j+1).set_paarameters(temp);
                       locations.get(j+1).can_be_divided=true;
                        if( processes.get(i).size/(int)(processes.get(i).size)!=1 )
                       locations.get(j).size =(int)(processes.get(i).size+1);
                       else
                           locations.get(j).size=processes.get(i).size;
                    }

                    System.out.println(i + " ->  "+j);
                    break;
                }
                if(processes.get(i).deallocated==true) System.out.println("Catched!!");
            }
        }
        System.out.println("No of memories after allocation is : "+locations.size());
    }




    public void best_fit_method(){
        int index = 0 ;
        for(int i = 0 ; i<processes.size();i++) {
            System.out.println("i stopped at  i ,j :  "+i);
            boolean flag = true ;
            memory best = new memory();
            boolean chosen = false ;
            for(int j = 0 ; j<locations.size();j++) {
                if(locations.get(j).getSize()>=processes.get(i).size&&locations.get(j).empty
                        &&processes.get(i).deallocated==false){
                    if(flag){
                        chosen = true;
                        flag=false;
                        best=locations.get(j);
                        index=j+1;
                    }
                    else
                        if( (locations.get(j).getSize()<best.getSize()&&best.can_be_divided&&
                                locations.get(j).empty&&locations.get(j).size>=processes.get(i).size
                                &&processes.get(i).deallocated==false&&locations.get(j).can_be_divided)
                                ||
                                (best.can_be_divided==false&&locations.get(j).can_be_divided&&
                                        locations.get(j).getSize()>=processes.get(i).size
                                &&locations.get(j).empty&&processes.get(i).deallocated==false)
                                ||
                                (best.can_be_divided==false&&locations.get(j).can_be_divided==false
                                &&locations.get(j).empty==true&&processes.get(i).deallocated==false
                                &&locations.get(j).size<best.size)){
                            best=locations.get(j);
                            index=j+1;
                    }
                }
            }
            if(chosen) {
                processes.get(i).assigned_memory = locations.get(index - 1);
                locations.get(index - 1).contained = processes.get(i);
                //best.empty = false;
                locations.get(index - 1).empty = false;


                double new_address;
                if (locations.get(index - 1).size > processes.get(i).size && best.can_be_divided) {
                    if (processes.get(i).size / (int) (processes.get(i).size) == 1)
                        new_address = (best.address + processes.get(i).size);
                    else
                        new_address = (int) (best.address + processes.get(i).size + 1);
                    int nad = (int) new_address;
                    memory temp ;
                    if(processes.get(i).size/(int)(processes.get(i).size)==1)
                        temp = new memory(nad,locations.get(index-1).size-processes.get(i).size);
                    else
                        temp=new memory(nad , (int)(locations.get(index-1).size-
                                (processes.get(i).size+1)));
                    locations.insertElementAt(temp, index);
                    locations.get(index).set_paarameters(temp);
                    locations.get(index).can_be_divided = true;
                    if (processes.get(i).size / (int) (processes.get(i).size) == 1)
                        locations.get(index - 1).size = processes.get(i).size;
                    else locations.get(index-1).size=(int)(processes.get(i).size+1);


                }
            }
        }
    }


    public void deallocate(){
        System.out.println("Here i reached 1  ");
        if(id_deallocated.getText().equals(""))
            return;
        else
        {
            int id_of_the_deallocated = Integer.parseInt(id_deallocated.getText());
            System.out.println("deallocated   :      "+id_of_the_deallocated);
            if(id_of_the_deallocated>=0&&id_of_the_deallocated<processes.size())
                processes.get(id_of_the_deallocated).deallocated = true ;
        }
        for(int i =0 ;i<processes.size();i++){
            if(processes.get(i).deallocated)
                System.out.println(i+"  True");
            else
                System.out.println(i + "   False");
        }
    }


    //========================= Draw table of memory ================================//

    public void draw_memory_table(){
        table.getItems().clear();
        for(int i = 0 ; i<locations.size();i++){
            list1.add(locations.get(i));
        }
        table.setItems(getlist1());
        c1.setCellValueFactory(new PropertyValueFactory<>("address")) ;
        c2.setCellValueFactory(new PropertyValueFactory<>("size"));
    }
    public ObservableList<memory> getlist1(){return list1;}

    //=================================================================================//

    //================= Draw table of processes ===============//

    public void draw_process_table(){
        table2.getItems().clear();
        for(int i = 0 ; i<processes.size();i++){
            list2.add(processes.get(i));
        }
        table2.setItems(getlist2());
        c3.setCellValueFactory(new PropertyValueFactory<>("id"));
        c4.setCellValueFactory(new PropertyValueFactory<>("size"));
    }
    public ObservableList<process> getlist2(){return list2;}

  //===============================================================//



    public void simulation(){

        // Return to the defaults
        for(int i = 0 ; i<locations.size() ; i++){
            locations.get(i).empty = true;
            locations.get(i).contained=new process(-1,-1);
        }
        for(int i = 0 ; i<processes.size();i++){
            processes.get(i).deallocated = false;
            processes.get(i).assigned_memory=new memory(-1,-1);
        }


        //Check if a process is deallocated
           deallocate();

        // arrange memories ascendingly
        arrange_memory(locations);

        //add holes
        add_adjacent_holes();

        System.out.println("no of memories is  "+locations.size());





       BorderPane second = new BorderPane();
       ScrollPane fe = new ScrollPane();
       fe.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
       fe.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

       Scene sec = new Scene(second,600,600);
       second.setCenter(fe);

       HBox h = new HBox();  // Hbox containing all drawings
        //h.setSpacing(300);
        h.setPadding(new Insets(50,50,50,50));


        // All locations without processes
        VBox ma = new VBox();
        /*
        for(int i = 0 ; i<locations.size();i++)
        {
            Label z = new Label();
            z.setStyle("-fx-border-color: #000000");
            z.setMinHeight(40);
            z.setMinWidth(100);
            ma.getChildren().add(z);
        }
        ma.setPadding(new Insets(50,50,50,50));
        h.getChildren().add(ma);
        */

        VBox main =new VBox();
        VBox address = new VBox();

        // Draw empty memory

        for(int i = 0 ; i<locations.size();i++)
        {
           // System.out.println("I stopped at i   "+i);
            if( (locations.get(i).address!=0&&i==0) || (i>0&&locations.get(i).address>
            locations.get(i-1).address+locations.get(i-1).size)){
                Label z = new Label();
                z.setStyle("-fx-border-color: #000000");
                z.setMinHeight(40);
                z.setMinWidth(100);
                z.setBackground
                        (new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY)));
                main.getChildren().add(z);

                Label z1 = new Label();
                if(i==0)z1.setMinHeight(35);
                z1.setText("");
                address.getChildren().add(z1);
            }
            Label adre = new Label();
            adre.setText(""+locations.get(i).address);
           if(i+1<locations.size()&&locations.get(i+1).address!=locations.get(i).address+locations.get(i).size)
               adre.setMinHeight(35);
          else if((i+1)==locations.size()) adre.setMinWidth(35);
          else adre.setMinWidth(20);
            address.getChildren().add(adre);

            Label adre2 = new Label(""+(locations.get(i).address+locations.get(i).getSize()));
            adre2.setMinHeight(0);
            address.getChildren().add(adre2);

            Label z = new Label();
            z.setStyle("-fx-border-color: #000000");
            z.setMinHeight(40);
            z.setMinWidth(100);
            main.getChildren().add(z);
        }
        if(locations.get(locations.size()-1).address+locations.get(locations.size()-1).size!=total) //last is hole
        {
            Label z = new Label();
            z.setStyle("-fx-border-color: #000000");
            z.setMinHeight(40);
            z.setMinWidth(100);
            z.setBackground
                    (new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY)));
            main.getChildren().add(z);
        }
        //address.setPadding(new Insets(50,47,50,50));
        //main.setPadding(new Insets(50,50,50,0));
        address.setPadding(new Insets(0,5,0,0));
        main.setPadding(new Insets(0,20,0,0));

        h.getChildren().addAll(address,main);



        //Allocate them
        if(methodology==me.first) first_fit_method();
        else best_fit_method();


        //Draw allocated memory


        for(int i = 0 ; i<processes.size();i++)
        {
            VBox ad = new VBox();
            VBox mem = new VBox();

            for(int j = 0 ; j<locations.size();j++)
            {
                if( (locations.get(j).address!=0&&j==0) || (j>0&&locations.get(j).address>
                        locations.get(j-1).address+locations.get(j-1).size)){
                    Label z = new Label();
                    z.setStyle("-fx-border-color: #000000");
                    z.setMinHeight(40);
                    z.setMinWidth(100);
                    z.setBackground
                            (new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY)));
                    mem.getChildren().add(z);

                    Label z1 = new Label();
                    z1.setMinHeight(40);
                    z1.setText("");
                    ad.getChildren().add(z1);
                }
                Label adre = new Label();
                adre.setText(""+locations.get(j).address);
                adre.setMinHeight(40);
                ad.getChildren().add(adre);

                Label z = new Label();
                System.out.println("About to check  " +  i  +   "    "+j);
                if( (locations.get(j).empty==false) && (locations.get(j).contained.id<=i) )
                    z.setText("Process "+locations.get(j).contained.id);
                z.setStyle("-fx-border-color: #000000");
                z.setMinHeight(40);
                z.setMinWidth(100);
                mem.getChildren().add(z);
            }
            ad.setPadding(new Insets(0,5,0,0));
            mem.setPadding(new Insets(0,20,0,0));
            h.getChildren().addAll(ad,mem);
        }


        draw_memory_table();

        /*

        for(int j = 0 ; j < processes.size() ; j++)
        {
            VBox me = new VBox();
            for(int i = 0 ; i<locations.size();i++)
            {
                Label z = new Label();
                if( (locations.get(i).empty==false) && (locations.get(i).contained.id<=j) )
                    z.setText("Process "+locations.get(i).contained.id);
                z.setStyle("-fx-border-color: #000000");
                z.setMinHeight(40);
                z.setMinWidth(100);
                me.getChildren().add(z);
            }
            me.setPadding(new Insets(50,50,50,50));
            h.getChildren().add(me);
        }
*/
       fe.setContent(h);


       Stage newone = new Stage();
        newone.setScene(sec);
        newone.show();
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Demo.fxml"));
    }

    strong f;


    @FXML private TextField total_size;
    @FXML private TextField address;
    @FXML private TextField memory_size;
    @FXML private Button add_memory;
    @FXML private Button delete_memory;

    @FXML private TextField process_size;

    @FXML private Button delete_process;

    @FXML private TableView<memory>table;
    @FXML private TableColumn<memory,Integer>c1;
    @FXML private TableColumn<memory,Integer>c2;

    @FXML private TableView<process>table2;
    @FXML private TableColumn<process,Integer>c3;
    @FXML private TableColumn<process,Integer>c4;

    @FXML private ComboBox method;

    @FXML private TextField id_deallocated;

    @FXML private Button simulate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        method.getItems().addAll("First Fit","Best Fit");
        method.setOnAction(e->set_methodology());

        /* Add total size */

        total_size.setOnAction(e->determine_total());

        /* Add new memory location */
        add_memory.setOnAction(e->add_memory());

        /* Delete last memory location*/
        delete_memory.setOnAction(e->delete_last_location());

        /* Add process size */
        process_size.setOnAction(e->add_process());

        /* Delete last process entered */
        delete_process.setOnAction(e->delete_last_process());


        simulate.setOnAction(e->simulation());
    }

    public class strong{
        int st;
        public  strong(){
            st=0;
        }
    }



}

