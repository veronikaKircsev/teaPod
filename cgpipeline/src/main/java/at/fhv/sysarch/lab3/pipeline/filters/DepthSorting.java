package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;

import java.util.*;

public class DepthSorting implements IFilterPush<Face, Face>, IFilterPull<Face,Face> {

    private static Vec3 camera;
    private Pipe<Face> pipeSuccessor;
    private Pipe<Face> predecessor;

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
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Face input) {
        if (input != null) {
            faces.add(input);
            while (!faces.isEmpty()) {
                Face face = faces.poll();
                if (pipeSuccessor != null && face != null) {
                    pipeSuccessor.write(face);
                }
            }
        }

    }

    @Override
    public Face read() {
        Face input = predecessor.read() != null ? predecessor.read() : null;
        if (input != null) {
            faces.add(input);
            while (!faces.isEmpty()) {
                Face face = faces.poll();
                if (predecessor != null && face != null) {
                    return face;
                }
            }
        }
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }



}
