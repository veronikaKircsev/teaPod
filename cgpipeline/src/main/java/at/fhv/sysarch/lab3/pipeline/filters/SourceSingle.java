package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

public class SourceSingle implements IFilter<Model, Face>, IFilterOut<Face> {

    private IFilter<Face, ?> successor;
    private Pipe<Face> pipeSuccessor;

    @Override
    public void setSuccessor(IFilter<Face, ?> r) {
        this.successor = r;
    }

    @Override
    public void setPipeSuccessor(Pipe<Face> pipe) {
        this.pipeSuccessor = pipe;
    }

    public void write(Model model) {
        model.getFaces().forEach(f -> {
            pipeSuccessor.write(f);
        });
    }}
