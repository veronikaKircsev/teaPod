package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

public class ResizeFilter implements IFilter<Face, Face> {

    private IFilter<Face, ?> successor;

    public void setSuccessor(IFilter<Face, ?> r) {
        this.successor = r;
    }

    public void write(Face face) {
        Face newFace = new Face(face.getV1().multiply(100), face.getV2().multiply(100), face.getV3().multiply(100), face);
        this.successor.write(newFace);
    }
}
