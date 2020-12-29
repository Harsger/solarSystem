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
        double bulgeRadius = 0.4 * radius ;
        double width = 10000.;
        double bulgeExtension = 1.4;
        double ratioCenterArms = 0.3;
        double centerRadius = 20000.;
        double[] massRange = new double[]{ 0.1 , 2000. };
        int stepsINspiralArm = 100;
        double numberDecrease = 0.05;
        double radiusDecrease = 0.1;
        double rotationVelocity = 1e-4;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/galaxy.txt"));
        
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
                    
                    double[] position = Useful.sphere() ;
                    double starClusterDistance = Useful.zufaellig( 0. , clusterRadius ) ;
                    
                    for(int k=0; k<3; k++)
                        position[k] = clusterPosition[k] + position[k] * starClusterDistance ;
                        
                    double[] velocity = new double[]{
                          position[1] * rotationVelocity ,
                        - position[0] * rotationVelocity ,
//                         - position[2] * rotationVelocity 
                        0.
                    };
                    
                    double starMass = Useful.massGenerator( massRange[0] , massRange[1] );
                    
                    for(int k=0; k<3; k++)
                        writer.write( String.format("%.1f\t",position[k] ) );
                    for(int k=0; k<3; k++)
                        writer.write( String.format("%.5f\t",velocity[k] ) );
                        
                    writer.write( String.format("%.2f",starMass) );
                    writer.write( "\n" );
                    
                }
                
            }
            
        }
        
        System.out.println(" # "+counter);
        
        if( counter < requiredStars ){
        
//             requiredStars -= counter ;
            
            for(int s=0; s<requiredStars; s++){
                    
                double[] position = Useful.sphere() ;
                double starDistance = Useful.zufaellig( 0. , bulgeRadius ) ;
                
                position[0] *= starDistance ;
                position[1] *= starDistance ;
                position[2] *= ( starDistance * width / bulgeRadius * bulgeExtension ) ;
                    
                double[] velocity = new double[]{
                      position[1] * rotationVelocity ,
                    - position[0] * rotationVelocity ,
//                     - position[2] * rotationVelocity 
                    0.
                };
                    
                double starMass = Useful.massGenerator( massRange[0] , massRange[1] );
                    
                for(int k=0; k<3; k++)
                    writer.write( String.format("%.1f\t",position[k] ) );
                for(int k=0; k<3; k++)
                    writer.write( String.format("%.5f\t",velocity[k] ) );
                    
                writer.write( String.format("%.2f",starMass) );
                writer.write( "\n" );
                
            }
        
        }
        
        writer.close();
    
    }
    
}
