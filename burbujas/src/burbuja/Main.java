package burbuja;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/** B2.4 — Bolas interactivas: eventos de ratón y de teclado. */
public class Main extends Application {

    private final Pane zona = new Pane();
    private final Label lblRaton = new Label("Ratón: —");
    private final Label lblBolas = new Label();

    @Override
    public void start(Stage stage) {
        Label instrucciones = new Label("Clic: crear bola · Clic en una bola: cambiar su color · "
                + "Clic derecho en una bola: eliminarla · Arrastrar: mover · C: cambiar colores · "
                + "Supr: borrar todo");
        instrucciones.getStyleClass().add("instrucciones");
        instrucciones.setWrapText(true);

        zona.setId("zona");
        zona.getStyleClass().add("zona");
        zona.setPrefSize(640, 420);

        Button btnBorrar = new Button("Borrar todo");
        btnBorrar.setId("btnBorrar");
        btnBorrar.setOnAction(e -> borrarTodo());
        lblRaton.setId("lblRaton");
        lblBolas.setId("lblBolas");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox barra = new HBox(20, lblRaton, lblBolas, espacio, btnBorrar);
        barra.getStyleClass().add("barra-estado");
        barra.setAlignment(Pos.CENTER_LEFT);

        BorderPane raiz = new BorderPane(zona);
        raiz.setTop(instrucciones);
        raiz.setBottom(barra);

        Scene scene = new Scene(raiz);
        scene.getStylesheets().add(Main.class.getResource("bolas.css").toExternalForm());

        // TODO T1: crear bolas con un clic y mostrar la posición del ratón
        zona.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                crearBola(e.getX(), e.getY());
            }
        });

        // TODO T4: teclas C y Supr
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.C) {
                for (Node c : zona.getChildren()) {
                    if (c instanceof Bola bola) {
                        bola.cambiarColor();
                    }
                }
            } else if (e.getCode() == KeyCode.DELETE) {
                borrarTodo();
            }
        });

        actualizar();
        stage.setTitle("B2.4 — Bolas interactivas");
        stage.setScene(scene);
        stage.show();
    }

    /** Crea una bola en (x, y), le asigna sus manejadores y la añade a la zona. */
    private void crearBola(double x, double y) {
        Bola bola = new Bola(x, y);

        // TODO T2: clic en una bola (cambiar color o eliminar)
        ((Node) bola).setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                bola.cambiarColor();
            } else if (e.getButton() == MouseButton.SECONDARY) {
                zona.getChildren().remove(bola);
                actualizar();
            }
            e.consume(); // Evita que el clic se propague a la zona
        });

        // TODO T3: arrastrar la bola
        ((Node) bola).setOnMouseDragged(e -> {
            bola.setCenterX(e.getX());
            bola.setCenterY(e.getY());
            e.consume(); // Evita que el arrastre se propague a la zona
        });

        zona.getChildren().add(bola);
        actualizar();
    }

    /** Elimina todas las bolas. */
    private void borrarTodo() {
        zona.getChildren().clear();
        actualizar();
    }

    /** Actualiza el contador de bolas de la barra de estado. */
    private void actualizar() {
        lblBolas.setText("Bolas: " + zona.getChildren().size());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
