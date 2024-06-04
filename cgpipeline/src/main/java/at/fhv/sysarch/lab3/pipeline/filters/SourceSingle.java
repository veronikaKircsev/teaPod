package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.List;

public class SourceSingle implements IFilterPush<Model, Model>, IFilterPull<Model,Face> {

    private Pipe<Model> pipeSuccessor;
    private Pipe<Model> predecessor;
    private List<Face> faces;
    private int count = 0;

    @Override
    public void setPipeSuccessor(Pipe<Model> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Model model) {
        pipeSuccessor.write(model);

    }

    @Override
    public Face read() {
        if (count < faces.size()) {
            return faces.get(count++);
        } else {
            count = 0;
            return null;
        }
    }

    @Override
    public void setPipePredecessor(Pipe<Model> predecessor) {
        this.predecessor = predecessor;

    }

    public void setModel(Model model) {
       faces = model.getFaces();
    }
}
