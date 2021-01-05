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
            new Planet(  1.9885e30 ,   0.      , 696342. ,  0.   , Color.YELLOW.getRGB()     ) , // sun
            new Planet(  3.301e23  ,  57.909e6 ,   2439. , 47.36 , Color.CYAN.getRGB()       ) , // mercury
            new Planet(  4.875e24  , 108.16e6  ,   6051. , 35.02 , Color.MAGENTA.getRGB()    ) , // venus
            new Planet(  5.9724e24 , 149.6e6   ,   6378. , 29.78 , Color.BLUE.getRGB()       ) , // earth
            new Planet(  6.417e23  ,  227.99e6 ,   3396. , 24.07 , Color.RED.getRGB()        ) , // mars
            new Planet(  1.899e27  ,  778.51e6 ,  71492. , 13.06 , Color.PINK.getRGB()       ) , // jupiter
            new Planet(  5.683e26  , 1433.4e6  ,  60268. ,  9.68 , Color.ORANGE.getRGB()     ) , // saturn
            new Planet(  8.681e25  , 2872.4e6  ,  25559. ,  6.81 , Color.GRAY.getRGB()       ) , // uranus
            new Planet(  1.024e26  , 4495.e6   ,  24764. ,  5.43 , Color.LIGHT_GRAY.getRGB() )   // neptun
        ));
        
//         ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(
//             new Planet(  1.e30 ,    0. , 100. , 0. , Color.YELLOW.getRGB()     ) , // sun
//             new Planet(  2.e29 ,  500. , 100. , 9. , Color.CYAN.getRGB()       ) , // mercury
//             new Planet(  3.e28 , 1000. , 100. , 8. , Color.MAGENTA.getRGB()    ) , // venus
//             new Planet(  4.e27 , 1500. , 100. , 7. , Color.BLUE.getRGB()       ) , // earth
//             new Planet(  5.e26 , 2000. , 100. , 6. , Color.RED.getRGB()        ) , // mars
//             new Planet(  6.e25 , 2500. , 100. , 5. , Color.PINK.getRGB()       ) , // jupiter
//             new Planet(  7.e24 , 3000. , 100. , 4. , Color.ORANGE.getRGB()     ) , // saturn
//             new Planet(  8.e23 , 3500. , 100. , 3. , Color.GRAY.getRGB()       ) , // uranus
//             new Planet(  9.e22 , 4000. , 100. , 2. , Color.LIGHT_GRAY.getRGB() )   // neptun
//         ));
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/planets.txt"));
        
        for(int p=0; p<planets.size(); p++){
            
            writer.write( planets.get(p).to_string()+"\n" );
            
        }
        
        writer.close();
        
    }
}
