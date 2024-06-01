package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class Source implements IFilterOut<Face>, IFilterIn<Model> {

    private IFilterIn<Face> successor;
    private Mat4 transMatrix;

    public void setSuccessor(IFilterIn<Face> r) {
        this.successor = r;
    }

    public void setTransMatrix(Mat4 matrix) {
        this.transMatrix = matrix;
    }

    public void write(Model model) {
        model.getFaces().forEach(f -> {
            Vec4 v1new = transMatrix.multiply(f.getV1());
            Vec4 v2new = transMatrix.multiply(f.getV2());
            Vec4 v3new = transMatrix.multiply(f.getV3());

            Vec4 v1NormalNew = transMatrix.multiply(f.getN1());
            Vec4 v2NormalNew = transMatrix.multiply(f.getN2());
            Vec4 v3NormalNew = transMatrix.multiply(f.getN3());

            Face transFace = new Face(v1new, v2new, v3new, v1NormalNew, v2NormalNew, v3NormalNew);
            successor.write(transFace);
        });
    }

    @Override
    public void setPipePredecessor(Pipe<Model> successor) {

    }

    public void setSuccessor(IFilterOut<Face> successor) {

    }

    @Override
    public void setPipeSuccessor(Pipe<Face> predecessor) {

    }
}
