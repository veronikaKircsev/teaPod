package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;

import java.util.*;

public class DepthSorting implements IFilter<Face,Face> {

    private IFilter<Face, ?> successor;
    private static Vec3 camera;
    private Pipe<Face> pipe;

    public DepthSorting(Vec3 camera) {
        this.camera = camera;
    }

    private static Queue<Face> faces = new PriorityQueue<>((o1, o2) -> {
        float cam = camera.getZ() * 3;
        float v1 =  (o1.getV1().getZ() + o1.getV2().getZ() + o1.getV3().getZ() - cam) / 3;
        float v2 = (o2.getV1().getZ() + o2.getV2().getZ() + o2.getV3().getZ() - cam) / 3;
        return Float.compare(v2, v1);
    });

    @Override
    public void setSuccessor(IFilter<Face, ?> successor) {
        this.successor = successor;
    }

    public void setPipe(Pipe<Face> pipe) {
        this.pipe = pipe;
    }

    @Override
    public void write(Face input) {

        if (input != null) {
            faces.add(input);

            while (!faces.isEmpty()) {
                Face face = faces.poll();
                if (pipe != null && face != null) {
                    pipe.write(face);
                }
            }
        }

    }

}
