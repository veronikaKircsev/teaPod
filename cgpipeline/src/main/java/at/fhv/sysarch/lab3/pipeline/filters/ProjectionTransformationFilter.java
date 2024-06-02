package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionTransformationFilter implements IFilter<Pair<Face, Color>,Pair<Face,Color>>, IFilterOut<Pair<Face,Color>>,
IFilterIn<Pair<Face, Color>,Pair<Face,Color>>{

    private PipelineData pd;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<Pair<Face, Color>> predecessor;

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
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read()!=null ? predecessor.read() : null;
        if (input!=null) {
            return transform(input);
        }
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
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
