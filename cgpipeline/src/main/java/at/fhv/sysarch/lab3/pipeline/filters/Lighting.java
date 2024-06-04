package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting implements IFilterPush<Pair<Face,Color>, Pair<Face,Color>>,
        IFilterPull<Pair<Face,Color>, Pair<Face,Color>> {

    private final Vec3 unitVector;
    private Pipe<Pair<Face,Color>> pipeSuccessor;
    private Pipe<Pair<Face, Color>> predecessor;

    public Lighting(Vec3 unitVector) {
        this.unitVector = unitVector;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {
        if (input != null) {
            pipeSuccessor.write(transform(input));
        }
    }

    @Override
    public Pair<Face, Color> read() {
            Pair<Face, Color> input = predecessor.read();
            if (input == null) {
                return null;
            } else {
                return transform(input);
            }
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    private Pair<Face, Color> transform(Pair<Face, Color> input){
        Face face = input.fst();
        Color color = input.snd();

        // Calculate the shading value based on the dot product of the face normal and lightning
        float shading = face.getN1().toVec3().dot(unitVector.getUnitVector());

        // Clamp the shading value to the range [0, 1]
        shading = Math.max(0, Math.min(1, shading));

        // If the shading value is less than or equal to 0, return the face with black color
        if(shading <= 0){
            return new Pair<>(face, Color.BLACK);
        } else {
            // Otherwise, derive a new color based on the original color and the shading value
            Color shadedColor = color.deriveColor(0, 1, shading, 1);
            return new Pair<>(face, shadedColor);
        }
    }


}
