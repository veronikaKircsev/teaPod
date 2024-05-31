package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ScreenSpaceFilter implements IFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private IFilter<Pair<Face, Color>, ?> successor;
    private Mat4 portTrans;
    private Pipe<Pair<Face, Color>> pipe;

    public ScreenSpaceFilter(Mat4 portTrans) {
        this.portTrans = portTrans;
    }

    @Override
    public void setSuccessor(IFilter<Pair<Face, Color>, ?> successor) {

        this.successor = successor;
    }

    public void setPipe(Pipe<Pair<Face, Color>> pipe) {
        this.pipe = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {

        // Transformierte Face und Farbe an den nächsten Filter weiterleiten
        pipe.write(new Pair<>(new Face(portTrans.multiply(input.fst().getV1().multiply(1f / input.fst().getV1().getW()))
                , portTrans.multiply(input.fst().getV2().multiply(1f / input.fst().getV2().getW())),
                portTrans.multiply(input.fst().getV3().multiply(1f / input.fst().getV3().getW())), input.fst()), input.snd()));
    }

}
