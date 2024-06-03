package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

public class ResizeFilter implements IFilterPush<Face, Face>, IFilterPull<Face, Face> {

    private Pipe<Face> pipeSuccessor;
    private Pipe<Face> predecessor;

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
    public Face read() {
        Face input = predecessor.read();
        if (input == null) {
            return null;
        } else {
            return transform(input);
        }
    }

    private Face transform(Face face) {
        return new Face(face.getV1().multiply(100), face.getV2().multiply(100), face.getV3().multiply(100), face);
    }
    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }
}
