package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Coloring implements IFilterPush<Face, Pair<Face, Color>>, IFilterPull<List<Face>, List<Pair<Face, Color>>> {

    private Color coloring;
    private Pipe<Pair<Face, Color>> pipeSuccessor;
    private Pipe<List<Face>> predecessor;

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
    public List<Pair<Face, Color>> read() {
        try {
        List<Face> faceList = predecessor.read();
        List<Pair<Face, Color>> result = new ArrayList<>();
        for (Face face : faceList) {
            result.add(new Pair<>(face, coloring));
        }
        return result;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setPipePredecessor(Pipe<List<Face>> predecessor) {
        this.predecessor = predecessor;
    }

}
