package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.List;

public class ResizeFilter implements IFilterPush<List<Face>, List<Face>>, IFilterPull<Face, Face> {

    private Pipe<List<Face>> pipeSuccessor;
    private Pipe<Face> predecessor;

    @Override
    public void setPipeSuccessor(Pipe<List<Face>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(List<Face> input) {
        List<Face> output = new ArrayList<>();
        for (Face face : input){
            output.add(transform(face));
        }
        pipeSuccessor.write(output);
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
