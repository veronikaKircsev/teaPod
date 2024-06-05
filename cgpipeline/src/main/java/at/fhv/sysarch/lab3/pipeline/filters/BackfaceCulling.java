package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

import java.util.ArrayList;
import java.util.List;

public class BackfaceCulling implements IFilterPush<List<Face>, List<Face>>, IFilterPull<Face, Face> {

    private Vec3 camera;
    private Pipe<List<Face>> pipeSuccessor;
    private Pipe<Face> predecessor;
    private boolean end;

    @Override
    public void setPipeSuccessor(Pipe<List<Face>> pipe) {
        this.pipeSuccessor = pipe;
    }

    public BackfaceCulling(Vec3 camera) {
        this.camera = camera;
    }

    @Override
    public void write(List<Face> input) {
        List<Face> output = new ArrayList<>();
        for (Face face : input){
            Face newFace = face;
            if (transform(newFace) <= 0) {
                output.add(face);
            }
        }

        pipeSuccessor.write(output);
    }

    @Override
    public Face read() {
        Face input;
            if (!end) {
                input = predecessor.read();
                if (input == null) {
                    end = true;
                    return null;
                } else {
                    if (transform(input) > 0) {
                        return read();
                    } else {
                        return input;
                    }
                }
            } else {
                end = false;
            }
            return read();
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }


    private float transform(Face input){
        // Camera position transform to Vec4
        Vec4 cam = new Vec4(camera.getX(), camera.getY(), camera.getZ(), 1);
        // View vector between camera and vertex
        Vec4 viewVector1 = input.getV1().subtract(cam);
        Vec4 viewVector2 = input.getV2().subtract(cam);
        Vec4 viewVector3 = input.getV3().subtract(cam);
        // create dot product from normal vector and view vector
        // create an average view distance
        float viewDistance = (input.getN1().dot(viewVector1) + input.getN2().dot(viewVector2)
                + input.getN3().dot(viewVector3)) / 3;
        return viewDistance;
    }


}
