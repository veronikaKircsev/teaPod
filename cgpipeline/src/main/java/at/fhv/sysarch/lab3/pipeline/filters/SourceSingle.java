package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.List;
import java.util.Queue;

public class SourceSingle implements IFilterPush<Model, Face>, IFilterPull<Model,Face> {

    private Pipe<Face> pipeSuccessor;
    private Pipe<Model> predecessor;
    private List<Face> faces;
    private int count = 0;

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Model model) {
        model.getFaces().forEach(f -> {
            pipeSuccessor.write(f);
        });
    }

    @Override
    public Face read() {
        if (count < faces.size()){
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
