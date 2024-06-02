package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class ScreenSpaceFilter implements IFilterPush<Pair<Face, Color>, Pair<Face, Color>>, IFilterPull<Pair<Face, Color>, Pair<Face, Color>> {

    private Mat4 portTrans;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<Pair<Face, Color>> predecessor;

    public ScreenSpaceFilter(Mat4 portTrans) {
        this.portTrans = portTrans;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {
        // Transformierte Face und Farbe an den nächsten Filter weiterleiten
        pipeSuccessor.write(transform(input));
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read() != null ? predecessor.read(): null;
        if (input == null) {
            return null;
        } else {
            return transform(input);
        }
    }

    private Pair<Face, Color> transform(Pair<Face, Color> input) {
        return new Pair<>(new Face(portTrans.multiply(input.fst().getV1().multiply(1f / input.fst().getV1().getW()))
                , portTrans.multiply(input.fst().getV2().multiply(1f / input.fst().getV2().getW())),
                portTrans.multiply(input.fst().getV3().multiply(1f / input.fst().getV3().getW())), input.fst()), input.snd());
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

}
