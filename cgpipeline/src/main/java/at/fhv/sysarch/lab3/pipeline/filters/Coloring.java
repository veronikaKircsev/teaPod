package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class Coloring implements IFilter<Face, Pair<Face, Color>>, IFilterOut<Pair<Face, Color>>,
IFilterIn<Face, Pair<Face, Color>>{

    private Color coloring;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<Face> predecessor;

    public Coloring(Color coloring) {
        this.coloring = coloring;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Face input) {
        Pair<Face, Color> data = new Pair<>(input, coloring);
       pipeSuccessor.write(data);
    }

    @Override
    public Pair<Face, Color> read() {
        Face face = predecessor.read() != null ? predecessor.read() : null;
        if (face != null) {
            return new Pair<>(face, coloring);
        }
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

}
