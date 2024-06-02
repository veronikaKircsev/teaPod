package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

import java.util.ArrayList;
import java.util.List;

public class BackfaceCulling implements IFilterPush<Face, Face>, IFilterPull<List<Face>, List<Face>> {

    private Vec3 camera;
    private Pipe<Face> pipeSuccessor;
    private Pipe<List<Face>> predecessor;

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    public BackfaceCulling(Vec3 camera) {
        this.camera = camera;
    }

    @Override
    public void write(Face input) {

        Vec4 cam = new Vec4(camera.getX(), camera.getY(), camera.getZ(), 0);
        Vec4 viewVector = input.getV1().subtract(cam);

        if (input.getN1().dot(viewVector) > 0) {
            pipeSuccessor.write(null);
        } else {
            pipeSuccessor.write(input);
        }

    }

    @Override
    public List<Face> read() {
        try {
        List<Face> faces = predecessor.read();
        List<Face> output = new ArrayList<>();
        for (Face input : faces) {
            Vec4 cam = new Vec4(camera.getX(), camera.getY(), camera.getZ(), 0);
            Vec4 viewVector = input.getV1().subtract(cam);

            if (input.getN1().dot(viewVector) < 0) {
                output.add(input);
            }

        }
        return output;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public void setPipePredecessor(Pipe<List<Face>> predecessor) {
        this.predecessor = predecessor;
    }
}
