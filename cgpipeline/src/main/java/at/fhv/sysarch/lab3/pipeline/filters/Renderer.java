package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Renderer implements IFilterPush<Pair<Face, Color>, Face>, IFilterPull<Pair<Face, Color>, Face> {

    //grafischen Kontexteinstellungen zuzugreifen und sie zu manipulieren
    //GraphicsContext ist eine Klasse aus JavaFX, die verwendet wird,
    // um grundlegende Zeichenoperationen durchzuführen.
    // Es bietet Methoden zum Zeichnen von Formen, Texten,
    // Bildern und anderen grafischen Objekten auf einer Canvas.

    private final GraphicsContext gpc;
    private final Color modelColor;
    private final RenderingMode renderingMode;
    private Pipe<Pair<Face, Color>> predecessor;

    public Renderer(GraphicsContext gpc, RenderingMode renderingMode, Color modelColor) {
        this.gpc = gpc;
        this.renderingMode = renderingMode;
        this.modelColor = modelColor;
    }

    @Override
    public void setPipeSuccessor(Pipe<Face> successor) {

    }

    public void write(Pair<Face, Color> input) {
        transform(input);
    }

    public void start() {
            Pair<Face, Color> input = predecessor.read();
            if (input != null) {
                transform(input);
            }
    }
    @Override
    public Face read() {
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    public void transform(Pair<Face, Color> input) {
        gpc.setStroke(input.snd());
        gpc.setFill(input.snd());
        if(this.renderingMode == RenderingMode.POINT) {
            gpc.setLineWidth(1);
            gpc.fillOval(input.fst().getV1().getX(),input.fst().getV1().getY(),1,1);
            gpc.fillOval(input.fst().getV2().getX(),input.fst().getV2().getY(),1,1);
            gpc.fillOval(input.fst().getV3().getX(),input.fst().getV3().getY(),1,1);
        } else if(this.renderingMode == RenderingMode.WIREFRAME) {
            gpc.strokeLine(input.fst().getV1().getX(), input.fst().getV1().getY(), input.fst().getV2().getX(), input.fst().getV2().getY());
            gpc.strokeLine(input.fst().getV2().getX(), input.fst().getV2().getY(), input.fst().getV3().getX(), input.fst().getV3().getY());
            gpc.strokeLine(input.fst().getV1().getX(), input.fst().getV1().getY(), input.fst().getV3().getX(), input.fst().getV3().getY());
        } else if (this.renderingMode == RenderingMode.FILLED){
            double[] cordX = new double[]{ input.fst().getV1().getX(), input.fst().getV2().getX(), input.fst().getV3().getX() };
            double[] cordY = new double[]{ input.fst().getV1().getY(), input.fst().getV2().getY(), input.fst().getV3().getY()};
            gpc.fillPolygon(cordX,
                    cordY,3);
            gpc.strokePolygon(cordX, cordY, 3);

        }
    }



}
