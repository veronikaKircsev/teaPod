package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProjectionTransformationFilter implements IFilterPush<Pair<Face,Color>, Pair<Face,Color>>,
        IFilterPull<List<Pair<Face, Color>>, List<Pair<Face,Color>>> {

    private PipelineData pd;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<List<Pair<Face, Color>>> predecessor;

    public ProjectionTransformationFilter(PipelineData pd){
        this.pd = pd;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {
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

    @Override
    public void setPipePredecessor(Pipe<List<Pair<Face, Color>>> predecessor) {
        this.predecessor = predecessor;
    }

    private Pair<Face, Color> transform(Pair<Face, Color> input) {
        Vec4 v1Proj = pd.getProjTransform().multiply(input.fst().getV1());
        Vec4 v2Proj = pd.getProjTransform().multiply(input.fst().getV2());
        Vec4 v3Proj = pd.getProjTransform().multiply(input.fst().getV3());
        Vec4 v1ProjNorm = pd.getProjTransform().multiply(input.fst().getN1());
        Vec4 v2ProjNorm = pd.getProjTransform().multiply(input.fst().getN2());
        Vec4 v3ProjNorm = pd.getProjTransform().multiply(input.fst().getN3());
        return new Pair<>(new Face(v1Proj, v2Proj, v3Proj, v1ProjNorm, v2ProjNorm, v3ProjNorm), input.snd());
    }
}
