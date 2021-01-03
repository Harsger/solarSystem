import java.awt.Color; 
import java.awt.Graphics; 
import java.util.*;
import javax.swing.*;
import java.io.*;

public class testCreator{

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
    
    public static void main(String[] args) throws java.io.IOException{
    
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/sphere.txt"));
        
        for(int p=0; p<10000; p++){
        
            double[] position = sphere();
            
            for(int k=0; k<3; k++)
                writer.write( String.format( "%.4f;" , position[k] ) );
                
                 if( position[0] > 0. && position[1] > 0. ) 
                    writer.write( String.format( "%d" ,   Color.RED.getRGB() ) );
            else if( position[0] < 0. && position[1] > 0. ) 
                    writer.write( String.format( "%d" ,  Color.BLUE.getRGB() ) );
            else if( position[0] < 0. && position[1] < 0. ) 
                    writer.write( String.format( "%d" , Color.GREEN.getRGB() ) );
            else if( position[0] > 0. && position[1] < 0. ) 
                    writer.write( String.format( "%d" , Color.YELLOW.getRGB() ) );
            else    
                    writer.write( String.format( "%d" , Color.WHITE.getRGB() ) );
            
            writer.write("\n");
            
        }
        
        writer.close();
    
    }
    
}
