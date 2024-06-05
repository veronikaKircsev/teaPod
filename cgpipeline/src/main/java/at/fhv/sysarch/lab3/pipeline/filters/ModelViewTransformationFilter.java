package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

import java.util.ArrayList;
import java.util.List;


public class ModelViewTransformationFilter implements IFilterPush<Model, List<Face>>, IFilterPull<Face, Face> {

    private Mat4 transMatrix;
    private Pipe<List<Face>> pipeSuccessor;
    private Pipe<Face> predecessor;

    public void setPipeSuccessor(Pipe<List<Face>> pipe) {
        this.pipeSuccessor = pipe;
    }

    public void setTransMatrix(Mat4 matrix) {
        this.transMatrix = matrix;
    }

    public void write(Model input) {
        List<Face> output = new ArrayList<>();
        for (Face face : input.getFaces()) {
            output.add(transform((face)));
        }
        pipeSuccessor.write(output);

    }

    @Override
    public Face read() {
            Face input = predecessor.read();
            if (input == null) {
                return null;
            } else {
                return transform(input);
            }
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    private Face transform(Face input){
        Vec4 v1new = transMatrix.multiply(input.getV1());
        Vec4 v2new = transMatrix.multiply(input.getV2());
        Vec4 v3new = transMatrix.multiply(input.getV3());
        Vec4 v1NormalNew = transMatrix.multiply(input.getN1());
        Vec4 v2NormalNew = transMatrix.multiply(input.getN2());
        Vec4 v3NormalNew = transMatrix.multiply(input.getN3());

        return new Face(v1new, v2new, v3new, v1NormalNew, v2NormalNew, v3NormalNew);
    }
}
