import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Viewer extends Application {
    private class PropositionView {
        private class AstView {
            private static final double PADDING_Y = 20.0, PADDING_X = 10.0,
                    FONT_SIZE = 20.0, BASELINE2BOTTOM = 5.0;

            private Font font = new Font(FONT_SIZE);
            private String str;
            private AstView parent = null, first = null, second = null;
            private double x, y, textWidth, width, height;

            AstView(Ast ast) {
                str = ast.toString();
                Text text = new Text(str);
                text.setFont(font);
                textWidth = text.getBoundsInLocal().getWidth();

                if (ast instanceof Not) {
                    first = new AstView(((Not) ast).getAst());
                    first.parent = this;
                } else if (ast instanceof CompoundProposition) {
                    CompoundProposition cp = (CompoundProposition) ast;
                    first = new AstView(cp.getFirst());
                    second = new AstView(cp.getSecond());
                    first.parent = second.parent = this;
                }
            }

            void calcSize() {
                height = FONT_SIZE + BASELINE2BOTTOM;
                width = textWidth;
                if (first != null) {
                    first.calcSize();
                    width = Math.max(first.width, width);
                    height = FONT_SIZE + BASELINE2BOTTOM + PADDING_Y + first.height;
                    if (second != null) {
                        second.calcSize();
                        width = Math.max(first.width + PADDING_X + second.width, width);
                        height = Math.max(height, FONT_SIZE + BASELINE2BOTTOM + PADDING_Y + second.height);
                    }
                }
            }

            void display(GraphicsContext g, double x, double y) {
                this.x = x;
                this.y = y;

                double cx = x + width / 2, cy = y;
                g.fillText(str, cx - textWidth / 2, cy);

                if (parent != null) {
                    double px = parent.x + parent.width / 2,
                            py = parent.y + FONT_SIZE + BASELINE2BOTTOM;
                    g.strokeLine(px, py, cx, cy);
                }
                if (first != null) {
                    y += FONT_SIZE + BASELINE2BOTTOM + PADDING_Y;
                    if (second != null) {
                        double space = textWidth - first.width - second.width;
                        if (space > PADDING_X) {
                            space /= 3;
                            first.display(g, x + space, y);
                            second.display(g, x + space + first.width + space, y);
                        } else {
                            first.display(g, x, y);
                            second.display(g, x + first.width + PADDING_X, y);
                        }
                    } else {
                        double space = textWidth - first.width;
                        if (space > 0) {
                            space /= 2;
                            first.display(g, x + space, y);
                        } else {
                            first.display(g, x, y);
                        }
                    }
                }
            }
        }

        private VBox vb = new VBox();
        private ScrollPane sp = new ScrollPane();

        PropositionView() {
            TextArea tf = new TextArea();
            tf.setPrefRowCount(3);
            tf.setPrefColumnCount(20);
            vb.getChildren().add(tf);

            Button ok = new Button("ok");
            ok.setPadding(new Insets(5));
            vb.getChildren().add(ok);

            ok.setOnAction(e -> {
                Ast ast = Main.parser.parse(tf.getText());
                if (ast == null) {
                    System.out.println("false");
                } else {
                    System.out.println("true");
                    display.draw(ast);
                }
            });

            vb.setPadding(new Insets(10.0));
            vb.setSpacing(20.0);

            sp.setContent(vb);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }

        void draw(Ast ast) {
            AstView vast = new AstView(ast);
            vast.calcSize();
            Canvas canvas = new Canvas(5.0 + vast.width, 5.0 + vast.height);
            GraphicsContext g = canvas.getGraphicsContext2D();
            g.setTextBaseline(VPos.TOP);
            g.setFont(vast.font);

            vast.display(g, 5.0, 5.0);
            vb.getChildren().add(canvas);
        }
    }

    private final PropositionView display = new PropositionView();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(display.sp, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Proposition Parser");
        primaryStage.show();

        PropositionParser parser = Main.parser;
        Main.test();
        try {
            Scanner scanner;
            if (args.length >= 1) {
                scanner = new Scanner(new FileInputStream(args[0]));
                while (scanner.hasNextLine()) {
                    Ast ast = parser.parse(scanner.nextLine());
                    if (ast == null) {
                        System.out.println("false");
                    } else {
                        System.out.println("true");
                    }
                    display.draw(ast);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String[] args;

    public static void main(String[] args) {
        Viewer.args = args;
        launch((String[])null);
    }
}
