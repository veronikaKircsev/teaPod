package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filters.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.List;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        // pull from the source (model)
        SourceSingle sourceSingle = new SourceSingle();
        Pipe<Face> singleSourcePipe = new Pipe<>();
        singleSourcePipe.setPredecessor(sourceSingle);
        // perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationFilter modelViewTransformationFilter = new ModelViewTransformationFilter();
        modelViewTransformationFilter.setPipePredecessor(singleSourcePipe);
        Pipe<Face> viewPipe = new Pipe<>();
        viewPipe.setPredecessor(modelViewTransformationFilter);

        ResizeFilter resizeFilter = new ResizeFilter();
        resizeFilter.setPipePredecessor(viewPipe);
        Pipe<Face> resizePipe = new Pipe<>();
        resizePipe.setPredecessor(resizeFilter);

        // perform backface culling in VIEW SPACE
        BackfaceCulling backfaceCulling = new BackfaceCulling(pd.getViewingEye());
        backfaceCulling.setPipePredecessor(resizePipe);
        Pipe<Face> backFacePipe = new Pipe<>();
        backFacePipe.setPredecessor(backfaceCulling);

        // perform depth sorting in VIEW SPACE
        DepthSorting depthSorting = new DepthSorting(pd.getViewingEye());
        depthSorting.setPipePredecessor(backFacePipe);
        Pipe<Face> depthPipe = new Pipe<>();
        depthPipe.setPredecessor(depthSorting);

        //add coloring (space unimportant)
        Coloring color = new Coloring(pd.getModelColor());
        color.setPipePredecessor(depthPipe);
        Pipe<Pair<Face, Color>> colorPipe = new Pipe<>();
        colorPipe.setPredecessor(color);

        // lighting can be switched on/off
        ProjectionTransformationFilter projectionTransformationFilter = new ProjectionTransformationFilter(pd.getProjTransform());
        Pipe<Pair<Face,Color>> projectionPipe = new Pipe<>();
        projectionPipe.setPredecessor(projectionTransformationFilter);
        if (pd.isPerformLighting()) {
            // perform lighting in VIEW SPACE
            Lighting lighting = new Lighting(pd.getLightPos());
            lighting.setPipePredecessor(colorPipe);
            Pipe<Pair<Face, Color>> lightingPipe = new Pipe<>();
            lightingPipe.setPredecessor(lighting);
            
            // perform projection transformation on VIEW SPACE coordinates
            projectionTransformationFilter.setPipePredecessor(lightingPipe);

        } else {
            // perform projection transformation
            projectionTransformationFilter.setPipePredecessor(colorPipe);
        }

        // perform perspective division to screen coordinates
        ScreenSpaceFilter screenSpaceFilter = new ScreenSpaceFilter(pd.getViewportTransform());
        screenSpaceFilter.setPipePredecessor(projectionPipe);
        Pipe<Pair<Face, Color>> screenSpacePipe = new Pipe<>();
        screenSpacePipe.setPredecessor(screenSpaceFilter);

        // feed into the sink (renderer)
        Renderer renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode(),pd.getModelColor());
        renderer.setPipePredecessor(screenSpacePipe);


        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here

            private float rotation = 0;
            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {

                sourceSingle.setModel(model);

                System.out.println(model.getFaces().size());

                rotation +=  (fraction)%360;

                Mat4 rotMat = Matrices.rotate(rotation, pd.getModelRotAxis());
                Mat4 transMat = pd.getModelTranslation().multiply(rotMat);
                Mat4 viewMat = pd.getViewTransform().multiply(transMat);
                modelViewTransformationFilter.setTransMatrix(viewMat);
                renderer.read();

                // TODO compute rotation in radians

                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate

                // TODO compute updated model-view tranformation

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline
            }
        };
    }
}