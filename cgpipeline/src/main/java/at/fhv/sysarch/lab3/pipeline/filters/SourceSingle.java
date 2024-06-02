package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.List;

public class SourceSingle implements IFilter<Model, Face>, IFilterOut<Face>, IFilterIn<Model,Face> {

    private Pipe<Face> pipeSuccessor;
    private Pipe<Model> predecessor;

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    public void write(Model model) {
        model.getFaces().forEach(f -> {
            pipeSuccessor.write(f);
        });
    }

    @Override
    public Face read() {
        Model model = predecessor.read();
        List<Face> faces = model.getFaces();
        for (Face f : faces) {
            return f;
        }
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Model> predecessor) {
        this.predecessor = predecessor;

    }
}
