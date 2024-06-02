package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ScreenSpaceFilter implements IFilterPush<Pair<Face, Color>, Pair<Face, Color>>, IFilterPull<List<Pair<Face, Color>>, List<Pair<Face, Color>>> {

    private Mat4 portTrans;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<List<Pair<Face, Color>>> predecessor;

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
    public List<Pair<Face, Color>> read() {
        try {
            List<Pair<Face, Color>> input = predecessor.read();
            List<Pair<Face, Color>> output = new ArrayList<>();
            for (Pair<Face, Color> pair : input) {
                output.add(transform(pair));
            }
            return output;
        } catch (Exception e) {
            return null;
        }

    }

    private Pair<Face, Color> transform(Pair<Face, Color> input) {
        return new Pair<>(new Face(portTrans.multiply(input.fst().getV1().multiply(1f / input.fst().getV1().getW()))
                , portTrans.multiply(input.fst().getV2().multiply(1f / input.fst().getV2().getW())),
                portTrans.multiply(input.fst().getV3().multiply(1f / input.fst().getV3().getW())), input.fst()), input.snd());
    }

    @Override
    public void setPipePredecessor(Pipe<List<Pair<Face, Color>>> predecessor) {
        this.predecessor = predecessor;
    }

}
