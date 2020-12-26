import java.awt.Color; 
import java.awt.Graphics; 
import java.util.*;
import javax.swing.*;
import java.io.*;

class Useful{

    public static double zufaellig(double unter, double ober){
        return zufaellig( unter, ober, 1 ) ;
    }

    public static double zufaellig(double unter, double ober, int vert){
        double rueck = 0.;
        if(vert<1) rueck=(ober-unter) * 0.5;
        else{
            for(int i=0; i<vert; i++){
                rueck += unter + Math.random() * (ober - unter);
            }
            rueck /= vert;
        }
        return rueck;
    }
    
    public static double[] sphere(){
        double[] rueck = new double[]{ zufaellig(-1.,1.) , zufaellig(-1.,1.) , zufaellig(-1.,1.) };
        double norm = Math.sqrt( rueck[0] * rueck[0] + rueck[1] * rueck[1] + rueck[2] * rueck[2] );
        rueck[0] /= norm ;
        rueck[1] /= norm ;
        rueck[2] /= norm ;
        return rueck ;
    }
    
    public static double massGenerator( double low , double high ){
        double rueck = zufaellig( low , high ) ;
        if( Math.random() < 0.5 ) rueck = zufaellig( low , high * 0.01 , 3 ) ;
        return rueck ;
    }

    public static double phiFunction( double t , double limit , double rounds ){
        return 2. * Math.PI * rounds * t / limit ;
    }

    public static double radiusFunction( double t , double limit , double end ){
        return radiusFunction( t , limit , end , 0. );
    }

    public static double radiusFunction( double t , double limit , double end , double offset ){
        double slope = ( end - offset ) / ( limit - 0. ) ;
        double intercept = end - slope * limit ;
        return intercept + slope * t ;
    }
}

public class galaxyCreator{
    public static void main(String[] args) throws java.io.IOException{
    
        int counter = 0;
        
        int requiredStars = 10000;
        int nSpiralArms = 6;
        double radius = 100000.;
        double width = 10000.;
        double ratioCenterArms = 0.3;
        double centerRadius = 20000.;
        double[] massRange = new double[]{ 0.1 , 2000. };
        int stepsINspiralArm = 100;
        double numberDecrease = 0.05;
        double radiusDecrease = 0.1;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/stars.txt"));
        
        int starsPerArm = (int)( 0.9 * requiredStars / (double)nSpiralArms ) ;
        
        for(int a=0; a<nSpiralArms; a++){
            
            for(int c=0; c<stepsINspiralArm; c++){
                
                double angle = 
                                Useful.phiFunction( 
                                                (double)c , 
                                                (double)(stepsINspiralArm-1) , 2./3. 
                                        ) 
                                + 2. * Math.PI 
                                * (double)a 
                                / (double)nSpiralArms ;
                                
                double distance = Useful.radiusFunction( (double)c , (double)(stepsINspiralArm-1) , radius );
                
                double[] clusterPosition = new double[]{
                                        distance * Math.cos( angle ) ,
                                        distance * Math.sin( angle ) ,
                                        0.
                                    };
                                    
                int starsINcluster = (int)(
                                            starsPerArm 
                                            / (double)stepsINspiralArm 
                                            / ( 0.5 + 0.5 * numberDecrease )
                                            *
                                            ( 
                                                1. 
                                                - 
                                                (double)c 
                                                / (double)stepsINspiralArm
                                                * ( 1. - numberDecrease )
                                            )
                                        );
                                    
                double clusterRadius = (double)(
                                                    width 
                                                    *
                                                    ( 
                                                        1.
                                                        -
                                                        (double)c 
                                                        / (double)stepsINspiralArm
                                                        * ( 1. - radiusDecrease )
                                                    )
                                                );
                                                
                counter += starsINcluster;
                                    
                for(int s=0; s<starsINcluster; s++){
                    
                    double[] positions = Useful.sphere() ;
                    double starClusterDistance = Useful.zufaellig( 0. , clusterRadius ) ;
                    
                    for(int k=0; k<3; k++)
                        positions[k] = clusterPosition[k] + positions[k] * starClusterDistance ;
                    
                    double starMass = Useful.massGenerator( massRange[0] , massRange[1] );
                    
                    writer.write( String.format("%.2f",starMass) );
                    for(int k=0; k<3; k++)
                        writer.write( String.format("\t%.1f",positions[k] ) );
                    writer.write( "\n" );
                    
                }
                
            }
            
        }
        
        writer.close();
        
        System.out.println(" # "+counter);
    
    }
    
}
