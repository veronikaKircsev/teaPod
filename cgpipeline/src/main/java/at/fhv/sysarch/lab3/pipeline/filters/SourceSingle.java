package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

public class SourceSingle implements IFilter<Model, Face> {

    private IFilter<Face, ?> successor;
    private Pipe<Face> pipe;

    @Override
    public void setSuccessor(IFilter<Face, ?> r) {
        this.successor = r;
    }

    public void setPipe(Pipe<Face> pipe) {
        this.pipe = pipe;
    }

    public void write(Model model) {
        model.getFaces().forEach(f -> {
            pipe.write(f);
        });
    }}
