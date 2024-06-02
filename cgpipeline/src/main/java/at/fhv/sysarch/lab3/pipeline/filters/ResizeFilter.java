package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.List;

public class ResizeFilter implements IFilterPush<Face, Face>, IFilterPull<List<Face>, List<Face>> {

    private Pipe<Face> pipeSuccessor;
    private Pipe<List<Face>> predecessor;

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Face face) {
        if (face != null) {
            pipeSuccessor.write(transform(face));
        }
    }

    @Override
    public List<Face> read() {
        try {
            List<Face> faceList = predecessor.read();
            List<Face> result = new ArrayList<>();
            for (Face face : faceList){
               result.add(transform(face));
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private Face transform(Face face) {
        return new Face(face.getV1().multiply(100), face.getV2().multiply(100), face.getV3().multiply(100), face);
    }
    @Override
    public void setPipePredecessor(Pipe<List<Face>> predecessor) {
        this.predecessor = predecessor;
    }
}
