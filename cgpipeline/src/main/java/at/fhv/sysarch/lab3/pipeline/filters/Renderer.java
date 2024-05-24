package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements IFilter<Face, Face>{

    private final GraphicsContext gpc;
    private final Color modelColor;
    private final RenderingMode renderingMode;

    public Renderer(GraphicsContext gpc, RenderingMode renderingMode, Color modelColor) {
        this.gpc = gpc;
        this.renderingMode = renderingMode;
        this.modelColor = modelColor;
    }

    @Override
    public void setSuccessor(IFilter<Face, ?> successor) {
        // NOT IMPLEMENTED
    }

    public void write(Face face) {
        gpc.setStroke(modelColor);
        if(this.renderingMode == RenderingMode.POINT) {
            gpc.setLineWidth(1);
            gpc.fillOval(50,50,150,150);
        }
        else {
            gpc.strokeLine(face.getV1().getX(), face.getV1().getY(), face.getV2().getX(), face.getV2().getY());
            gpc.strokeLine(face.getV2().getX(), face.getV2().getY(), face.getV3().getX(), face.getV3().getY());
            gpc.strokeLine(face.getV1().getX(), face.getV1().getY(), face.getV3().getX(), face.getV3().getY());
        }


    }
}
