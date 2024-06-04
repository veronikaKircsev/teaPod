package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionTransformationFilter implements IFilterPush<Pair<Face,Color>, Pair<Face,Color>>,
        IFilterPull<Pair<Face, Color>, Pair<Face,Color>> {

    private Mat4 projTransform;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<Pair<Face, Color>> predecessor;

    public ProjectionTransformationFilter(Mat4 pd){
        this.projTransform = pd;
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
            Pair<Face, Color> input = predecessor.read();
            if (input == null) {
                return null;
            } else {
                return transform(input);
            }
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    private Pair<Face, Color> transform(Pair<Face, Color> input) {
        // Project the vertices of the Face using the projection transform matrix.
        Vec4 v1Proj = projTransform.multiply(input.fst().getV1());
        Vec4 v2Proj = projTransform.multiply(input.fst().getV2());
        Vec4 v3Proj = projTransform.multiply(input.fst().getV3());
        // Project the normals of the Face using the projection transform matrix.
        Vec4 v1ProjNorm = projTransform.multiply(input.fst().getN1());
        Vec4 v2ProjNorm = projTransform.multiply(input.fst().getN2());
        Vec4 v3ProjNorm = projTransform.multiply(input.fst().getN3());
        return new Pair<>(new Face(v1Proj, v2Proj, v3Proj, v1ProjNorm, v2ProjNorm, v3ProjNorm), input.snd());
    }
}
