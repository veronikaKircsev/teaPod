package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class BackfaceCulling implements IFilter<Face, Face> {

    private IFilter<Face, ?> successor;
    private Vec3 camera;
    private Pipe<Face> pipe;

    @Override
    public void setSuccessor(IFilter<Face, ?> successor) {
        this.successor = successor;
    }

    public void setPipe(Pipe<Face> pipe) {
        this.pipe = pipe;
    }

    public BackfaceCulling(Vec3 camera) {
        this.camera = camera;
    }

    @Override
    public void write(Face input) {

        Vec4 cam = new Vec4(camera.getX(), camera.getY(), camera.getZ(), 0);
        Vec4 viewVector = input.getV1().subtract(cam);

        if (input.getN1().dot(viewVector) > 0) {
            pipe.write(null);
        } else {
            pipe.write(input);
        }

    }
}
