package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

public class ResizeFilter implements IFilter<Face, Face>, IFilterOut<Face> {

    private Pipe<Face> pipeSuccessor;

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    public void write(Face face) {
        if (face != null) {
            Face newFace = new Face(face.getV1().multiply(100), face.getV2().multiply(100), face.getV3().multiply(100), face);
            pipeSuccessor.write(newFace);
        }
    }
}
