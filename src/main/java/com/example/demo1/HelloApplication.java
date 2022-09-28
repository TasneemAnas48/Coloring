package com.example.demo1;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {

    Map<Pair<Integer, Integer>, Color> colored_area = new HashMap<>();
    List<Pair<Integer, Integer>> colored_area1 = new ArrayList<>();

    Map<Pair<Integer, Integer>, Color> undo_map = new HashMap<>();
    List<Pair<Integer, Integer>> undo_list = new ArrayList<>();


    List<Pair<Integer, Integer>> my_stack = new ArrayList<>();

    File filePath =  new File("C:\\Users\\Tasneem\\IdeaProjects\\demo1\\src\\main\\java\\com\\example\\demo1\\10.jpg");

    int width, height;
    PixelReader pixelReader;
    WritableImage wImage;
    PixelWriter writer;
    public HelloApplication() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Image image = new Image(new FileInputStream(filePath));
        Canvas canvas = new Canvas(700, 500);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //initDraw(graphicsContext);
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        //Creating a writable image
        wImage = new WritableImage(width, height);
        pixelReader = image.getPixelReader();

        writer = wImage.getPixelWriter();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                writer.setColor(x, y, color.darker());
            }
        }

        Group root = new Group();

        VBox vbox1 = new VBox();

        vbox1.setSpacing(20);
        vbox1.setAlignment(Pos.CENTER);
        Button red = new Button("Red");
        red.setPrefSize(150, 30);
        red.setStyle("-fx-background-color: #ff0000");
        Button green = new Button("Green");
        green.setPrefSize(150, 30);
        green.setStyle("-fx-background-color: #00ff00");
        Button blue = new Button("Blue");
        blue.setPrefSize(150, 30);
        blue.setStyle("-fx-background-color: #0000ff");
        Button yellow = new Button("Yellow");
        yellow.setPrefSize(150, 30);
        yellow.setStyle("-fx-background-color: #ffff00");
        Button orange = new Button("Orange");
        orange.setPrefSize(150, 30);
        orange.setStyle("-fx-background-color: #FFA500");
        Button pink = new Button("Pink");
        pink.setPrefSize(150, 30);
        pink.setStyle("-fx-background-color:#FFC0CB");

        Button choose = new Button("Change Image");
        choose.setPrefSize(150, 30);

        Button undo_Button = new Button("Undo");
        undo_Button.setPrefSize(150, 30);


        vbox1.getChildren().addAll(red, green, blue, yellow, orange, pink, undo_Button, choose);

        VBox vbox2 = new VBox();

        ImageView imageView = new ImageView(wImage);

        vbox2.getChildren().addAll(imageView);
        vbox2.setTranslateX(0);
        vbox2.setTranslateY(0);
        vbox1.setTranslateX(800);
        vbox1.setTranslateY(100);
        root.getChildren().addAll(vbox1, vbox2);


        Scene scene = new Scene(root, 1000, 500);

        primaryStage.setTitle("Writing pixels ");

        primaryStage.setScene(scene);

        final int[] color_draw = new int[1];
        //Color Action
        red.setOnAction(e -> {
            graphicsContext.setStroke(Color.RED.darker());
            graphicsContext.setFill(Color.RED.darker());
            color_draw[0] = 1;
        });

        green.setOnAction(e -> {
            graphicsContext.setStroke(Color.GREEN.darker());
            graphicsContext.setFill(Color.GREEN.darker());
            color_draw[0] = 2;
        });

        blue.setOnAction(e -> {
            graphicsContext.setStroke(Color.BLUE.darker());
            graphicsContext.setFill(Color.BLUE.darker());
            color_draw[0] = 3;
        });

        yellow.setOnAction(e -> {
            graphicsContext.setStroke(Color.YELLOW.darker());
            graphicsContext.setFill(Color.YELLOW.darker());
            color_draw[0] = 4;
        });

        orange.setOnAction(e -> {
            graphicsContext.setStroke(Color.ORANGE.darker());
            graphicsContext.setFill(Color.ORANGE.darker());
            color_draw[0] = 5;
        });

        pink.setOnAction(e -> {
            graphicsContext.setStroke(Color.PINK.darker());
            graphicsContext.setFill(Color.PINK.darker());
            color_draw[0] = 6;
        });


        undo_Button.setOnAction(e -> {
            for (int i = 0; i < colored_area1.size(); i++) {
                colored_area.remove(colored_area1.get(i));

            }
            drow();

        });

        //choose
        choose.setOnAction((e)-> {
            Stage stage2 = (Stage)((Node)e.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Change Image");
            File userDir = new File("C:\\Users\\Tasneem\\Desktop");
            fileChooser.setInitialDirectory(userDir);
            filePath = fileChooser.showOpenDialog(stage2);
            System.out.println(filePath);
            colored_area.clear();
            try {
                start(primaryStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                colored_area1.clear();
                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY());

            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                graphicsContext.lineTo(event.getX(), event.getY());
                Color color;
                if (colored_area.containsKey(new Pair<>((int) event.getX(), (int) event.getY()))) {
                    color = colored_area.get(new Pair<>((int) event.getX(), (int) event.getY()));

                }
                else{
                    color = pixelReader.getColor((int) event.getX(), (int) event.getY());
                }
                graphicsContext.stroke();
                my_stack.add(new Pair<>((int) event.getX(), (int) event.getY()));

                if (color_draw[0] == 1){
                    paint(color,Color.RED);
                }else if(color_draw[0] == 2) {
                    paint(color, Color.GREEN);
                }else if(color_draw[0] == 3) {
                    paint(color, Color.BLUE);
                }else if(color_draw[0] == 4) {
                    paint(color, Color.YELLOW);
                }else if(color_draw[0] == 5) {
                    paint(color, Color.ORANGE);
                }else if(color_draw[0] == 6) {
                    paint(color, Color.PINK);
                }

                undo_map.put(new Pair<>((int) event.getX(), (int) event.getY()),color);
                undo_list.add(new Pair<>((int) event.getX(), (int) event.getY()));
                drow();

            }

        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                WritableImage wim = new WritableImage(width, height);
            }
        });

        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void change_color(int x, int y, Color c1, Color my_color, int j) {
        Color color_right, color_left, color_up, color_down;

        if (j < 1000) {

            if (x + 1 < width) {
                color_right = pixelReader.getColor(x + 1, y);
                if ((my_color.getRed() >= color_right.getRed() - 0.1 && my_color.getRed() <= color_right.getRed() + 0.1) && (my_color.getBlue() >= color_right.getBlue() - 0.1 && my_color.getBlue() <= color_right.getBlue() + 0.1) && (my_color.getGreen() >= color_right.getGreen() - 0.1 && my_color.getGreen() <= color_right.getGreen() + 0.1) && !colored_area.containsKey(new Pair<>(x + 1, y))) {
                    colored_area.put(new Pair<>(x + 1, y), c1);
                    colored_area1.add( new Pair<>(x + 1, y));

                    change_color(x + 1, y, c1, my_color, j + 1);

                }
            }

            if (x - 1 > 0) {
                color_left = pixelReader.getColor(x - 1, y);
                if ((my_color.getRed() >= color_left.getRed() - 0.1 && my_color.getRed() <= color_left.getRed() + 0.1) && (my_color.getBlue() >= color_left.getBlue() - 0.1 && my_color.getBlue() <= color_left.getBlue() + 0.1) && (my_color.getGreen() >= color_left.getGreen() - 0.1 && my_color.getGreen() <= color_left.getGreen() + 0.1) && !colored_area.containsKey(new Pair<>(x - 1, y))) {
                    colored_area.put(new Pair<>(x - 1, y), c1);
                    colored_area1.add(new Pair<>(x - 1, y));

                    change_color(x - 1, y, c1, my_color, j + 1);

                }
            }

            if (y + 1 < height) {
                color_up = pixelReader.getColor(x, y + 1);
                if ((my_color.getRed() >= color_up.getRed() - 0.1 && my_color.getRed() <= color_up.getRed() + 0.1) && (my_color.getBlue() >= color_up.getBlue() - 0.1 && my_color.getBlue() <= color_up.getBlue() + 0.1) && (my_color.getGreen() >= color_up.getGreen() - 0.1 && my_color.getGreen() <= color_up.getGreen() + 0.1) && !colored_area.containsKey(new Pair<>(x, y + 1))) {
                    colored_area.put(new Pair<>(x, y + 1), c1);
                    colored_area1.add(new Pair<>(x , y+1));

                    change_color(x, y + 1, c1, my_color, j + 1);

                }
            }

            if (y - 1 > 0) {
                color_down = pixelReader.getColor(x, y - 1);
                if ((my_color.getRed() >= color_down.getRed() - 0.1 && my_color.getRed() <= color_down.getRed() + 0.1) && (my_color.getBlue() >= color_down.getBlue() - 0.1 && my_color.getBlue() <= color_down.getBlue() + 0.1) && (my_color.getGreen() >= color_down.getGreen() - 0.1 && my_color.getGreen() <= color_down.getGreen() + 0.1) && !colored_area.containsKey(new Pair<>(x, y - 1))) {
                    colored_area.put(new Pair<>(x, y - 1), c1);
                    colored_area1.add(new Pair<>(x, y-1));

                    change_color(x, y - 1, c1, my_color, j + 1);

                }
            }


        } else {
            my_stack.add(new Pair<>(x, y));
        }


    }

    void drow() {
        Pair p;
        Color c;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                p = new Pair(x, y);
                c = pixelReader.getColor(x, y);
                if (colored_area.containsKey(p)) {

                    c = colored_area.get(p);
                }
                writer.setColor(x, y, c.darker());
            }
        }
    }

    void paint(Color color, Color color_draw) {
        Pair pa;
        while (!my_stack.isEmpty()) {
            pa = my_stack.remove(0);
            change_color((int) pa.getKey(), (int) pa.getValue(), color_draw, color, 0);
        }
    }

}