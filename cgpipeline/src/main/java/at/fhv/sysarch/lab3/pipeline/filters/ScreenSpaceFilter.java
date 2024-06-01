package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class ScreenSpaceFilter implements IFilter<Pair<Face, Color>, Pair<Face, Color>>, IFilterOut<Pair<Face, Color>>{

    private IFilter<Pair<Face, Color>, ?> successor;
    private Mat4 portTrans;
    private Pipe<Pair<Face, Color>> pipeSuccessor;

    public ScreenSpaceFilter(Mat4 portTrans) {
        this.portTrans = portTrans;
    }

    @Override
    public void setSuccessor(IFilter<Pair<Face, Color>, ?> successor) {

        this.successor = successor;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {

        // Transformierte Face und Farbe an den nächsten Filter weiterleiten
        pipeSuccessor.write(new Pair<>(new Face(portTrans.multiply(input.fst().getV1().multiply(1f / input.fst().getV1().getW()))
                , portTrans.multiply(input.fst().getV2().multiply(1f / input.fst().getV2().getW())),
                portTrans.multiply(input.fst().getV3().multiply(1f / input.fst().getV3().getW())), input.fst()), input.snd()));
    }

}
