package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import at.fhv.sysarch.lab3.pipeline.filters.*;
import javafx.scene.paint.Color;

import java.util.List;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        Renderer r = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode(),pd.getModelColor());

        // feed into the sink (renderer)
        Pipe<Pair<Face, Color>> screenSpacePipe = new Pipe<>();
        screenSpacePipe.setSuccessor(r);
        // perform perspective division to screen coordinates
        ScreenSpaceFilter screenSpace = new ScreenSpaceFilter(pd.getViewportTransform());
        screenSpace.setPipeSuccessor(screenSpacePipe);

        Pipe<Pair<Face, Color>> projectionTransfomationPipe = new Pipe<>();
        projectionTransfomationPipe.setSuccessor(screenSpace);
        //perform projection transformation on VIEW SPACE coordinates
        ProjectionTransformationFilter projectionTransformationFilter = new ProjectionTransformationFilter(pd);
        projectionTransformationFilter.setPipeSuccessor(projectionTransfomationPipe);

        Pipe<Pair<Face, Color>> colorPipe = new Pipe<>();
        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            //perform lighting in VIEW SPACE
            Pipe<Pair<Face, Color>> lightingPipe = new Pipe<>();
            lightingPipe.setSuccessor(projectionTransformationFilter);
            Lighting lighting = new Lighting(pd.getLightPos());
            lighting.setPipeSuccessor(lightingPipe);
            colorPipe.setSuccessor(lighting);


        } else {
            colorPipe.setSuccessor(projectionTransformationFilter);
        }

        //add coloring (space unimportant)
        Coloring coloring = new Coloring(pd.getModelColor());
        coloring.setPipeSuccessor(colorPipe);

        Pipe<Face> depthSortingPipe = new Pipe<>();
        depthSortingPipe.setSuccessor(coloring);
        // perform depth sorting in VIEW SPACE
        DepthSorting depthSorting = new DepthSorting(pd.getViewingEye());
        depthSorting.setPipeSuccessor(depthSortingPipe);


        Pipe<List<Face>> backFacePipe = new Pipe<>();
        backFacePipe.setSuccessor(depthSorting);
        // perform backface culling in VIEW SPACE
        BackfaceCulling backfaceCulling = new BackfaceCulling(pd.getViewingEye());
        backfaceCulling.setPipeSuccessor(backFacePipe);

        Pipe<List<Face>> pipe = new Pipe<>();
        pipe.setSuccessor(backfaceCulling);

        ResizeFilter filter = new ResizeFilter();
        filter.setPipeSuccessor(pipe);

        Pipe<List<Face>> transFilterPipe = new Pipe<>();
        transFilterPipe.setSuccessor(filter);
        //perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationFilter transFilter = new ModelViewTransformationFilter();
        transFilter.setPipeSuccessor(transFilterPipe);

        Pipe<Model> sourcePipe = new Pipe<>();
        sourcePipe.setSuccessor(transFilter);
        // push from the source (model)
        SourceSingle source = new SourceSingle();
        source.setPipeSuccessor(sourcePipe);

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the fraction
        return new AnimationRenderer(pd) {

            //rotation variable goes in here
            private float rotation = 0;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer).
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render
             */
            @Override
            protected void render(float fraction, Model model) {


                //pd.getGraphicsContext().setStroke(Color.RED);
                //pd.getGraphicsContext().strokeLine(pos,pos,pos+100,pos+100);
                //pos++;


                // müssen wir drechung bestimmen
                //pd.getModelRotAxis();
                //multiplizieren
                //pd.getModelTranslation();
                // resultat dann multipl with faces
                //pd.getViewTransform();
                // erste filter, immer neu bestimmen und neu filtern

                // last two steps
                //pd.getProjTransform();
                //pd.getViewportTransform();

                /*
                Mat4 rotMat = Matrices.rotate(100, pd.getModelRotAxis()); // neue matrix kommt raus
                Mat4 transMat = pd.getModelTranslation().multiply(rotMat);
                Mat4 viewMat = pd.getViewTransform().multiply(transMat);
                model.getFaces().forEach(f -> {
                    Vec4 v1new =  viewMat.multiply(f.getV1());
                    Vec4 v2new = viewMat.multiply(f.getV2());
                    Vec4 v3new = viewMat.multiply(f.getV3());

                    Vec4 v1NormalNew = viewMat.multiply(f.getN1());
                    Vec4 v2NormalNew = viewMat.multiply(f.getN2());
                    Vec4 v3NormalNew = viewMat.multiply(f.getN3());
                    Face trans = new Face(v1new,v2new, v3new, v1NormalNew, v2NormalNew, v3NormalNew);

                    Vec4 v1Proj = pd.getProjTransform().multiply(trans.getV1());
                    Vec4 v2Proj = pd.getProjTransform().multiply(trans.getV2());
                    Vec4 v3Proj = pd.getProjTransform().multiply(trans.getV3());

                    pd.getViewTransform();

                });

                 */
                rotation +=  (fraction)%360;

                Mat4 rotMat = Matrices.rotate(rotation, pd.getModelRotAxis());
                Mat4 transMat = pd.getModelTranslation().multiply(rotMat);
                Mat4 viewMat = pd.getViewTransform().multiply(transMat);

                /*pd.getModelRotAxis();
                pd.getModelTranslation();
                pd.getViewTransform();


                pd.getProjTransform();
                pd.getViewportTransform();*/
                transFilter.setTransMatrix(viewMat);

                source.write(model);

                // vetktor kommt zurück
                //ersteFilter fertig
                 /*model.getFaces().forEach(face -> {
                    pd.getGraphicsContext().strokeLine(face.getV1().getX()*100, face.getV1().getY()*100, face.getV2().getX()*100, face.getV2().getY()*100);
                    pd.getGraphicsContext().strokeLine(face.getV2().getX()*100, face.getV2().getY()*100, face.getV3().getX()*100, face.getV3().getY()*100);
                    pd.getGraphicsContext().strokeLine(face.getV1().getX()*100, face.getV1().getY()*100, face.getV3().getX()*100, face.getV3().getY()*100);
                });*/

                // TODO compute rotation in radians

                // TODO create new model rotation matrix using pd.modelRotAxis

                // TODO compute updated model-view tranformation

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline

            }
        };
    }
}