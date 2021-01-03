import java.awt.Color; 
import java.awt.Graphics; 
import java.util.*;
import javax.swing.*;
import java.io.*;

class Planet{
    
    double mass;
    double radius;
    double orbit;
    double speed;
    int color;
    
    public Planet( double m , double o , double r , double s , int c ){
        mass = m;
        orbit = o;
        radius = r;
        speed = s;
        color = c;
    }
    
    public String to_string(){
        String rueck = "";
        double angle = Math.random() * 2. * Math.PI;
        rueck += ( orbit * Math.cos( angle ) ) ;
        rueck += "\t";
        rueck += ( orbit * Math.sin( angle ) ) ;
        rueck += "\t0.\t";
        rueck += ( - speed * Math.sin( angle ) ) ;
        rueck += "\t";
        rueck += (   speed * Math.cos( angle ) ) ;
        rueck += "\t0.\t";
        rueck += mass;
        rueck += "\t";
        rueck += radius;
        rueck += "\t";
        rueck += color;
        return rueck ;
    }
        
}

public class systemCreator{
    public static void main(String[] args) throws java.io.IOException{
        
        ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(
            new Planet(  1.9885e30 ,   0.      , 696342. ,  0.   , Color.YELLOW.getRGB() ) , // sun
            new Planet(  3.301e23  ,  57.909e6 ,   2439. , 47.36 ,              0x443C3C ) , // mercury
            new Planet(  4.875e24  , 108.16e6  ,   6051. , 35.02 ,              0x8B8B94 ) , // venus
            new Planet(  5.9724e24 , 149.6e6   ,   6378. , 29.78 ,   Color.BLUE.getRGB() ) , // earth
            new Planet(  6.417e23  ,  227.99e6 ,   3396. , 24.07 ,    Color.RED.getRGB() ) , // mars
            new Planet(  1.899e27  ,  778.51e6 ,  71492. , 13.06 ,              0xD1C180 ) , // jupiter
            new Planet(  5.683e26  , 1433.4e6  ,  60268. ,  9.68 ,              0xA49577 ) , // saturn
            new Planet(  8.681e25  , 2872.4e6  ,  25559. ,  6.81 ,              0xBADEEE ) , // uranus
            new Planet(  1.024e26  , 4495.e6   ,  24764. ,  5.43 ,              0x7786A4 )   // neptun
        ));
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/planets.txt"));
        
        for(int p=0; p<planets.size(); p++){
            
            writer.write( planets.get(p).to_string()+"\n" );
            
        }
        
        writer.close();
        
    }
}
