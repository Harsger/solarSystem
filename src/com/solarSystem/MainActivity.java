package com.solarSystem;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Color;

class OfUse{
    public static double maximum = Math.pow(10,6);
    public static double minimum = Math.pow(10,-12);
}

class Dot{
    double[] koord;
    int col;
    public Dot(double a, double b, int c){
        koord = new double[2];
        koord[0] = a;
        koord[1] = b;
        col = c;
    }
    public String toString(){
        return " Dot ( "+this.koord[0]+" , "+this.koord[1]+" ) mit Farbe : "+col;
    }
} 

class Punkt{
    double[] koord;
    int col;
    public Punkt(double a, double b, double c, int d){
        koord = new double[3];
        koord[0] = a;
        koord[1] = b;
        koord[2] = c;
        col = d;
    }
    public Punkt(double[] ueb, int d){
        koord = new double[3];
        for(int i=0; i<3; i++){
            koord[i] = ueb[i];
        }
        col = d;
    }
    public Punkt(Punkt pu){
        this.koord = new double[3];
        for(int i=0; i<3; i++){
            this.koord[i] = pu.koord[i];
        }
        this.col = pu.col;
    }
    public void mult(double mal){
        for(int i=0; i<3; i++){
            this.koord[i] = mal*this.koord[i];
        }
    }
    public Punkt multNew(double mal){
        Punkt rueck = new Punkt( this.koord , this.col ) ;
        for(int i=0; i<3; i++){
            rueck.koord[i] = mal*this.koord[i];
        }
        return rueck;
    }
    public Punkt adi(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        for(int i=0; i<3; i++){
            rueck.koord[i] = this.koord[i] + pu.koord[i];
        }
        return rueck;
    }
    public Punkt subt(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        for(int i=0; i<3; i++){
            rueck.koord[i] = this.koord[i] - pu.koord[i];
        }
        return rueck;
    }
    public double prod(Punkt pu){
        double rueck=0.;
        for(int i=0; i<3; i++){
            rueck += this.koord[i]*pu.koord[i];
        }
        return rueck;
    }
    
    public Punkt cross(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        rueck.col = this.col * pu.col;
        rueck.koord[0] = this.koord[1]*pu.koord[2] - this.koord[2]*pu.koord[1];
        rueck.koord[1] = this.koord[2]*pu.koord[0] - this.koord[0]*pu.koord[2];
        rueck.koord[2] = this.koord[0]*pu.koord[1] - this.koord[1]*pu.koord[0];
        return rueck;
    }
    public double norm(){
        double rueck=Math.sqrt(this.prod(this));
        return rueck;
    }
    public Punkt normal(){
        Punkt rueck = new Punkt( this.koord , this.col ) ;
        double norm = this.norm();
        if(norm>OfUse.minimum){
            rueck.mult(1./norm);
        }
        return rueck;
    }
    public double dist(Punkt pu){
        Punkt arbeiter = this.subt(pu);
        double rueck = arbeiter.norm();
        return rueck;
    }
    public double winkel(Punkt pu){
        double rueck=0.;
        rueck = this.prod(pu)/this.norm()/pu.norm();
        return rueck;
    }
    public String toString(){
        return " Punkt ( "+this.koord[0]+" , "+this.koord[1]+" , "+this.koord[2]+" ) mit Farbe : "+col;
    }
}

class Matrix{
    int zeile;
    int spalte;
    double[][] mat;
    public Matrix(int a, int b){
        this.zeile = a;
        this.spalte = b;
        this.mat = new double[a][b]; 
    }
    public Matrix(Punkt pu, double wi){
        this.zeile = 3;
        this.spalte = 3;
        double ca = Math.cos(wi);
        double sa = Math.sin(wi);
        Punkt npu = pu.normal();
        this.mat = new double[3][3];
        //
        this.mat[0][0] = pu.koord[0]*pu.koord[0]*(1.-ca)+ca;
        this.mat[0][1] = pu.koord[0]*pu.koord[1]*(1.-ca)-pu.koord[2]*sa;
        this.mat[0][2] = pu.koord[0]*pu.koord[2]*(1.-ca)+pu.koord[1]*sa;
        //
        this.mat[1][0] = pu.koord[1]*pu.koord[0]*(1.-ca)+pu.koord[2]*sa;
        this.mat[1][1] = pu.koord[1]*pu.koord[1]*(1.-ca)+ca;
        this.mat[1][2] = pu.koord[1]*pu.koord[2]*(1.-ca)-pu.koord[0]*sa;
        //
        this.mat[2][0] = pu.koord[2]*pu.koord[0]*(1.-ca)-pu.koord[1]*sa;
        this.mat[2][1] = pu.koord[2]*pu.koord[1]*(1.-ca)+pu.koord[0]*sa;
        this.mat[2][2] = pu.koord[2]*pu.koord[2]*(1.-ca)+ca;
    }
    public Matrix(double[][] emat){
        this.zeile = emat.length;
        this.spalte = emat[0].length;
        for(int i=1; i<this.spalte; i++){
            if(emat[i].length<this.spalte){this.spalte=emat[i].length;}
        }
        this.mat = new double[this.zeile][this.spalte];
        for(int i=0; i<this.zeile; i++){
            for(int j=0; j<this.spalte; j++){
                this.mat[i][j]=emat[i][j];
            }
        }
    }
    public void ein(int a, int b, double c){
        this.mat[a][b] = c;
    }
//     public void ausgabe(){
//         for(int i=0; i<this.mat.length; i++){
//             System.out.print(" [ ");
//             for(int j=0; j<this.mat[i].length; j++){
//                 System.out.printf("%s %8.4f %s"," ",mat[i][j]," ");
//             }
//             System.out.println(" ]");
//         }
//     }
    public Matrix mult(Matrix mal){
        Matrix rueck = new Matrix(this.zeile, mal.spalte);
        if(this.spalte==mal.zeile){
            for(int i=0; i<this.zeile; i++){
                for(int j=0; j<mal.spalte; j++){
                    double sum=0.;
                    for(int k=0; k<this.spalte; k++){
                        sum += this.mat[i][k] * mal.mat[k][j];
                    }
                    //System.out.print(sum+" ");
                    rueck.ein( i, j, sum);
                }
            }
            //System.out.println(" ");
        }
        else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
            for(int i=0; i<this.zeile; i++){
                for(int j=0; j<mal.spalte; j++){
                    if(i==j){
                        rueck.ein( i, j, 1.);
                    }
                    else{
                        rueck.ein( i, j, 0.);
                    }
                }
            }
        }
        return rueck;
    }
    public Dot mulDtoD(Dot doot){
        Dot rueck = new Dot(1., 1., 0);
        rueck.col = doot.col;
        if(this.zeile==2 && this.spalte==2){
            for(int i=0; i<2; i++){
                rueck.koord[i]=0;
                for(int j=0; j<2; j++){
                    rueck.koord[i] += this.mat[i][j] * doot.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public Punkt mulPtoP(Punkt pu){
        Punkt rueck = new Punkt( 1., 1., 1., 0);
        rueck.col = pu.col;
        if(this.zeile==3 && this.spalte==3){
            for(int i=0; i<3; i++){
                rueck.koord[i]=0;
                for(int j=0; j<3; j++){
                    rueck.koord[i] += this.mat[i][j] * pu.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public Dot mulPtoD(Punkt pu){
        Dot rueck = new Dot(1., 1., 0);
        rueck.col = pu.col;
        if(this.zeile==2 && this.spalte==3){
            for(int i=0; i<2; i++){
                rueck.koord[i]=0;
                for(int j=0; j<3; j++){
                    rueck.koord[i] += this.mat[i][j] * pu.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public String toString(){
        return " Zeilen : "+this.zeile+" ; Spalten : "+this.spalte;
    }
}

class RotMat extends Matrix{
    int achse;
    double winkel;
    //static super.zeile = 3;
    //static super.spalte = 3;
    public RotMat(int a, double p){
        super(3,3);
        achse = a%3;
        winkel = p%(2*Math.PI);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(i==achse){
                    if(j==achse){
                        super.mat[i][j]=1.;
                    }
                    else{
                        super.mat[i][j]=0.;
                    }
                }
                else if(j==achse){
                    super.mat[i][j]=0.;
                }
                else if(i==j){
                    super.mat[i][j]=Math.cos(winkel);
                }
                else if(i>j){
                    if(achse==1){super.mat[i][j]=Math.sin(winkel);}
                    else{super.mat[i][j]=-Math.sin(winkel);}
                }
                else{
                    if(achse==1){super.mat[i][j]=-Math.sin(winkel);}
                    else{super.mat[i][j]=Math.sin(winkel);}
                }
            }
        }
    }
    public String toString(){
        String ax;
        if(this.achse==0){
            ax = "x";
        }
        if(this.achse==1){
            ax = "y";
        }
        if(this.achse==2){
            ax = "z";
        }
        else{
            ax = "unbekannte";
        }
        double wingra = this.winkel*180/2/Math.PI;
        return " Rotationsmatrix um "+ax+"-Achse um "+wingra+" Grad ";
    }
}

class database{

    public static ArrayList< Punkt > orte = new ArrayList<Punkt>();
    public static ArrayList< Punkt > zBuffer = new ArrayList<Punkt>();
    public static ArrayList< Dot > zweiD = new ArrayList<Dot>();
    
    public static double[][] extrema ;
    public static double maxRange ;
    public static int[] lastTouch ;
    public static int[] currentTouch ;
    
    public static Punkt axis ;
    public static Punkt direction ;
    public static Punkt lastKnown ;
    
    public static int[] imageSize;
    public static int imageScale;
    public static Bitmap projection ;
    public static Bitmap emptyImage ;
    
}

class Projektor{
    double bereich ;
    Punkt beob ;
    Punkt forward ;
    Punkt upDir ;
    public Projektor(double ber){
        bereich = ber ;
        beob    = new Punkt(0., 0., ber, 0);
        forward = new Punkt(0., 0., -1, 0);
        upDir   = new Punkt(0., 1., 0., 0);
    }
    public Projektor(double ber, Punkt be, Punkt fow, Punkt up){
        bereich = ber ;
        beob = be;
        forward = fow;
        upDir = up;
    }
    public void projektion(){

        ///////////////////////////////define variables/////////////////////////////////////////////
        double rx, ry, rz;
        int rc;
        double near = bereich / 100;
        double far = 2. * bereich ;
        double oefwin = 120.*Math.PI/180./2.;
        double ow = 0.;
        double setver = 1.;
        double sv = 0.;
        Punkt zax = new Punkt(this.forward);
        zax = zax.multNew(-1.).normal();
        Punkt yax = new Punkt(this.upDir);
        yax = yax.normal();
        Punkt xax = yax.cross(zax).normal();
        Matrix camTrafo = new Matrix(4,4);
        Matrix projMat = new Matrix(4,4);
        double[] homCoord = new double[4];
        double[] projHom = new double[4];
        double win=0.;
        double distance=0.;
        double wver=Math.cos(oefwin);
        ///////////////////////////////define variables END/////////////////////////////////////////////

        if( Math.abs(Math.tan(oefwin)) > OfUse.minimum ){ow=1./Math.tan(oefwin);}
        else{ow=OfUse.maximum;}
        if( Math.abs(Math.tan(setver)) > OfUse.minimum ){sv=1./Math.tan(setver);}
        else{sv=OfUse.maximum;}

        ///////////////////////////////calculate trafomatrices////////////////////////////////
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                //Kameratransformationsmatrix
                if(i==0){
                    if(j<3){camTrafo.mat[i][j]=xax.koord[j];}
                    else{camTrafo.mat[i][j]=-xax.prod(beob);}
                }
                else if(i==1){
                    if(j<3){camTrafo.mat[i][j]=yax.koord[j];}
                    else{camTrafo.mat[i][j]=-yax.prod(beob);}
                }
                else if(i==2){
                    if(j<3){camTrafo.mat[i][j]=zax.koord[j];}
                    else{camTrafo.mat[i][j]=-zax.prod(beob);}
                }
                else{
                    if(j<3){camTrafo.mat[i][j]=0.;}
                    else{camTrafo.mat[i][j]=1.;}
                }
                //Projektionsmatrix
                if(i==0 && j==0){projMat.mat[i][j]=sv;}
                else if(i==1 && j==1){projMat.mat[i][j]=ow;}
                else if(i==2 && j==2){projMat.mat[i][j]=far/(near-far);}
                else if(i==2 && j==3){projMat.mat[i][j]=near*far/(near-far);}
                else if(i==3 && j==2){projMat.mat[i][j]=-1.;}
                else{projMat.mat[i][j]=0.;}
                /*
                if(i==j){projMat.mat[i][j]=1.;}
                else{projMat.mat[i][j]=0.;}
                */
            }
        }
        projMat = projMat.mult(camTrafo);
        ///////////////////////////////calculate trafomatrices END////////////////////////////////

        //////////////////////////////projekt orte to zBuffer///////////////////////////////
        database.zBuffer.clear();
        for(int k=0; k<database.orte.size(); k++){
            win = this.forward.winkel( database.orte.get(k).subt( this.beob ) );
            distance = database.orte.get(k).dist( this.beob );
            if( ( distance >= near && distance <= far ) && win >= wver ){
                //Kamtrafo und Projektion
                for(int i=0; i<4; i++){
                    if(i<3){homCoord[i]=database.orte.get(k).koord[i];}
                    else{homCoord[i]=1.;}
                }
                for(int i=0; i<projHom.length; i++){
                    projHom[i]=0.;
                    for(int j=0; j<projMat.mat[i].length; j++){
                        projHom[i] += projMat.mat[i][j] * homCoord[j];
                    }   
                }
                rc = database.orte.get(k).col;
                database.zBuffer.add(new Punkt(projHom, rc));
            }
        }
        //////////////////////////////projekt orte to zBuffer END///////////////////////////////
    }
    public void zBuffering(){
        database.zweiD.clear();
        double rx, ry, cz;
        int rc;
        int buffer;
        int num = database.zBuffer.size();
        int newnum = 0;
        int order[] = new int[num];
        double values[] = new double[num];
//         for(int i=1; i<num; i++){
//             order[i] = i;
//             values[i] = database.zBuffer.get(i).koord[2];
//         }
//         ////////////////////////////////bubble sort//////////////////////////////////
//         while(num!=0){
//             newnum = 0;
//             for(int i=1; i<num; i++){
//                 if( values[order[i]] > values[order[i-1]] ){
//                     buffer = order[i];
//                     order[i] = order[i-1];
//                     order[i-1] = buffer;
//                     newnum = i;
//                 }
//                 /*
//                 if(database.zBuffer.get(i-1).koord[2] > database.zBuffer.get(i).koord[2]){
//                     buffer = database.zBuffer.get(i);
//                     database.zBuffer.get(i) = database.zBuffer.get(i-1);
//                     database.zBuffer.get(i-1) = buffer;
//                     newnum = i;
//                 }
//                 */
//             }
//             num = newnum;
//         }
//         ////////////////////////////////bubble sort END//////////////////////////////////
        num = database.zBuffer.size();
        for(int i=num-1; i>=0; i--){
            order[i] = i ;
            cz = database.zBuffer.get(order[i]).koord[2];
            if(cz > OfUse.minimum){
                rx = database.zBuffer.get(order[i]).koord[0] / cz;
                ry = database.zBuffer.get(order[i]).koord[1] / cz;
            }
            else{
                rx = OfUse.maximum;
                ry = OfUse.maximum;
            }
            rc = database.zBuffer.get(order[i]).col;
            database.zweiD.add( new Dot(rx ,ry, rc));
        }
    }
    public void draw(){
        database.projection = database.emptyImage.copy( database.emptyImage.getConfig() , true ) ;
        double tx, ty;
        double ber = 1.;
        int gx, gy;
        for(int k=database.zweiD.size()-1; k>0; k--){
        
            tx = database.zweiD.get(k).koord[0];
            ty = database.zweiD.get(k).koord[1];    
//             if(Math.abs(tx)<ber && Math.abs(ty)<ber){
            gx = (int)( 
                        ( 
                            database.imageScale * ( ber - tx ) 
                            + database.imageSize[0] - database.imageScale 
                        ) * 0.5 
                    ); 
            gy = (int)( 
                        ( 
                            database.imageScale * ( ber - ty ) 
                            + database.imageSize[1] - database.imageScale 
                        ) * 0.5 
                    );
//                 if( gy < 0 || gy >= database.imageSize[1] ) continue ;
            if( 
                gx < 0 || gx >= database.imageSize[0]
                ||
                gy < 0 || gy >= database.imageSize[1]
            )
                continue ;
                
            database.projection.setPixel( gx , gy , database.zweiD.get(k).col );
//             }
        }
    }
    public void initialize(){
        this.projektion();
        this.zBuffering();
        this.draw();
    }
    public void rotateView(Punkt rotAx, double angle){
        Matrix rotMat = new Matrix(rotAx, angle);
        if( forward.cross( rotAx ).norm() > OfUse.minimum ){
            this.forward = rotMat.mulPtoP(forward);
            this.forward = this.forward.normal();
        }
        if( upDir.cross( rotAx ).norm() > OfUse.minimum ){
            this.upDir = rotMat.mulPtoP(upDir);
            this.upDir = this.upDir.normal();
        }
        this.initialize();
    }
    public void rotatePos(Punkt rotAx, double angle){
        Matrix rotMat = new Matrix(rotAx, angle);
        this.beob = rotMat.mulPtoP(beob);
        if( forward.cross( rotAx ).norm() > OfUse.minimum ){
            this.forward = rotMat.mulPtoP(forward);
            this.forward = this.forward.normal();
        }
//         if( upDir.cross( rotAx ).norm() > OfUse.minimum ){
        if( upDir.cross( rotAx ).norm() > 0.1 ){
            this.upDir = rotMat.mulPtoP(upDir);
            this.upDir = this.upDir.normal();
        }
        this.initialize();
    }
    public void movePos(Punkt direction, double stepsize){
        Punkt step = new Punkt(direction);
        step = step.normal().multNew(stepsize);
        this.beob = beob.adi(step);
        this.initialize();
    }
}

public class MainActivity extends Activity {

    Integer counter = 0 , index , coord ;
    static int x , y , z ;
    String line;
    Point dot = new Point();
    boolean changer;
    double[] scale ;
    double angleFactor = - Math.PI * 0.3 / Math.sqrt(2.) ;
    double ratio ;
    double pixelThreshold = 10. ;
    
    BufferedReader reader;
    
    String[] files;
    String[] columns;
    
    Projektor raster;

//     public static void loadDataFromView(View v) {
//         
//         database.imageSize = new int[]{
//             v.getLayoutParams().width , 
//             v.getLayoutParams().height
//         };
//     
//         database.projection = Bitmap.createBitmap( 
//                                             database.imageSize[0], 
//                                             database.imageSize[1], 
//                                             Bitmap.Config.ARGB_8888
//                                         );    
//                                         
//         database.emptyImage = database.projection ;
//         
//         for(x=0; x<database.imageSize[0]; x++){
//             database.emptyImage.setPixel( x , 0 , Color.BLUE );
//             database.emptyImage.setPixel( x , database.imageSize[1]-1 , Color.BLUE );
//         }
//         for(y=0; y<database.imageSize[1]; y++){
//             database.emptyImage.setPixel( 0 , y , Color.BLUE );
//             database.emptyImage.setPixel( database.imageSize[0]-1 , y , Color.BLUE );
//         }
//                                         
//         Canvas c = new Canvas( database.emptyImage );
//         v.layout( 0 , 0 , database.imageSize[0] , database.imageSize[1] );
//         v.draw(c);
//         
//     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Display display = getWindowManager().getDefaultDisplay();
        
        final ImageView picture = (ImageView)findViewById(R.id.picture);
        
        final TextView debugOutput = (TextView)findViewById(R.id.debugOutput);
        
        final Button resetButton = (Button)findViewById(R.id.reset);

        final AssetManager assetManager = getAssets();
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                raster.beob = new Punkt(0., 0., database.maxRange / 3., 0);
                raster.forward = new Punkt(0., 0., -1, 0);
                raster.upDir = new Punkt(0., 1., 0., 0);
                
                raster.initialize() ;
            
                picture.setImageBitmap( database.projection );
                
            }
        });
        
        picture.setOnTouchListener( new View.OnTouchListener(){
    
            @Override
            public boolean onTouch(View v, MotionEvent event){
                    
                database.currentTouch = new int[]{
                    (int)event.getX() ,
                    (int)event.getY()
                };
                
                switch( event.getAction() ){
                
                    case MotionEvent.ACTION_DOWN:
                    
                        database.lastTouch = database.currentTouch ;
                        
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                        
                        break;
                        
                    case MotionEvent.ACTION_UP:
                        
                        break;
                }

                scale = new double[]{ 
                                        database.currentTouch[0] - database.lastTouch[0] ,
                                        database.currentTouch[1] - database.lastTouch[1]
                                    } ;
                                    
                if( raster.upDir.norm() > OfUse.minimum ) database.lastKnown = raster.upDir ;

                if( Math.abs( scale[0] ) > pixelThreshold || Math.abs( scale[1] ) > pixelThreshold ){
                
                    if( Math.abs( scale[0] ) < pixelThreshold / 2 ) scale[0] = 0 ;
                    if( Math.abs( scale[1] ) < pixelThreshold / 2 ) scale[1] = 0 ;

                    database.axis = raster.forward.cross( raster.upDir );
                    database.axis.mult( Math.abs( scale[0] ) ) ;
                    database.direction = raster.upDir.multNew( Math.abs( scale[1] ) ) ;
//                     database.axis.mult( scale[0] ) ;
//                     database.direction = raster.upDir.multNew( scale[1] ) ;
                    database.direction = database.direction.adi( database.axis );
                    database.direction = database.direction.normal() ;
                    database.axis = raster.forward.cross( database.direction );
                    
                    ratio = Math.sqrt( scale[0] * scale[0] + scale[1] * scale[1] ) / database.imageScale ;
                    
                    if( scale[0] < 0. && scale[1] < 0. ) ratio *= -1. ;
                    if( scale[0] > 0. && scale[1] > 0. ) ratio *=  1. ;
                    
                    if( scale[0] < 0. && scale[1] > 0. ){ 
                        database.axis = database.axis.cross( raster.forward ).normal() ;
                        ratio *=  1. ;
                    }
                    if( scale[0] > 0. && scale[1] < 0. ){ 
                        database.axis = database.axis.cross( raster.forward ).normal() ;
                        ratio *= -1. ;
                    }
                    
                    if( scale[0] == 0 && scale[1] < 0 ) ratio *= -1. ;
                    if( scale[1] == 0 && scale[0] < 0 ) ratio *= -1. ;
                    
                    if( database.axis.norm() > OfUse.minimum ){
                    
                        raster.rotatePos( database.axis , ratio * angleFactor );
                        
                        if( raster.upDir.norm() < OfUse.minimum ){ 
                        
                            raster.upDir = database.lastKnown ;
                            raster.initialize() ;
                            
                        }
            
                        picture.setImageBitmap( database.projection );
                        
                    }
                
                    database.lastTouch = database.currentTouch ;
                    
                }
                    
                debugOutput.setText(
                    String.format(
                                    " beob ("+
                                                "%1$.0f"+"|"+
                                                "%2$.0f"+"|"+
                                                "%3$.0f"+
                                            ")"+
                                    "\n"+
                                    " forward ("+
                                                "%4$ 3.2f"+"|"+
                                                "%5$ 3.2f"+"|"+
                                                "%6$ 3.2f"+
                                            ")"+
                                    "\n"+
                                    " upDir ("+
                                                "%7$ 3.2f"+"|"+
                                                "%8$ 3.2f"+"|"+
                                                "%9$ 3.2f"+
                                            ")"+
                                    "\n"+
                                    " scale ("+
                                                "%10$.0f"+"|"+
                                                "%11$.0f"+
                                            ")"
                                    ,
                                    raster.beob.koord[0]   , raster.beob.koord[1]   , raster.beob.koord[2]   ,
                                    raster.forward.koord[0], raster.forward.koord[1], raster.forward.koord[2],
                                    raster.upDir.koord[0]  , raster.upDir.koord[1]  , raster.upDir.koord[2],
                                    scale[0]  , scale[1] 
                                )
                );
                    
                return true;
                    
            }
            
        } );
        
        database.orte = new ArrayList< Punkt >();
        database.extrema = new double[][] {
                        { -1. , 1. } ,
                        { -1. , 1. } ,
                        { -1. , 1. }
                    };
        
        try{
            files = assetManager.list("");
            counter = files.length;
        }
        catch(IOException e){
            debugOutput.setText(
                " can not read database "
            );
        }
        
        try{
        
            reader = new BufferedReader( new InputStreamReader( assetManager.open("stars.txt") ) );
            
            counter = 0;
            while( ( line = reader.readLine() ) != null ){
            
                columns = line.split("\t");
                if( columns.length < 4 ) continue;
                
                database.orte.add( new Punkt(
                        Double.parseDouble( columns[1] ) ,
                        Double.parseDouble( columns[2] ) ,
                        Double.parseDouble( columns[3] ) ,
                        Color.WHITE
                    ) 
                );
                
                if( counter < 1 ){
                    database.extrema = new double[][] {
                        { database.orte.get(counter).koord[0] , database.orte.get(counter).koord[0] } ,
                        { database.orte.get(counter).koord[1] , database.orte.get(counter).koord[1] } ,
                        { database.orte.get(counter).koord[2] , database.orte.get(counter).koord[2] }
                    };
                }
                
                for( coord=0 ; coord<3 ; coord++ ){
                
                    if( database.extrema[coord][0] > database.orte.get(counter).koord[coord] )
                        database.extrema[coord][0] = database.orte.get(counter).koord[coord] ;
                    
                    if( database.extrema[coord][1] < database.orte.get(counter).koord[coord] )
                        database.extrema[coord][1] = database.orte.get(counter).koord[coord] ;
                        
                }
                
                counter++;
                
            }
            
        }
        catch(IOException e){
            for(index=0; index<counter; index++){
                line += files[index];
                line += "\n";
            }
            debugOutput.setText(
                " can not read database "
                +
                " #files : "
                +
                counter.toString()
                +
                "\n"
                +
                line
            );
        }
        
        if( counter > 500 ){

            database.maxRange = database.extrema[0][1] - database.extrema[0][0] ;
            for( index=1; index<3; index++ ){
                if( database.extrema[index][1] - database.extrema[index][0] > database.maxRange )
                    database.maxRange = database.extrema[index][1] - database.extrema[index][0] ;
            }
                    
            display.getSize(dot);
            database.imageSize = new int[]{ (int)( (double)dot.x / 0.75 ) , dot.y };
            
//             database.imageSize = new int[]{
//                                                 picture.getMeasuredWidth() ,
//                                                 picture.getMeasuredHeight() 
//                                             };
                                            
            database.imageScale = database.imageSize[0] ;
            if( database.imageScale > database.imageSize[1] ) 
                database.imageScale = database.imageSize[1] ;            
                
            debugOutput.setText(
                                    " width "
                                    +
                                    database.imageSize[0]
                                    +
                                    " | "
                                    +
                                    " height "
                                    +
                                    database.imageSize[1]
                                );
    
            database.projection = Bitmap.createBitmap( 
                                                database.imageSize[0], 
                                                database.imageSize[1], 
                                                Bitmap.Config.ARGB_8888
                                            );    
                                            
            database.emptyImage = database.projection.copy( database.projection.getConfig() , true ) ;
            
            for(x=0; x<database.imageSize[0]; x++){
                database.emptyImage.setPixel( x , 0 , Color.BLUE );
                database.emptyImage.setPixel( x , database.imageSize[1]-1 , Color.BLUE );
            }
            for(y=0; y<database.imageSize[1]; y++){
                database.emptyImage.setPixel( 0 , y , Color.BLUE );
                database.emptyImage.setPixel( database.imageSize[0]-1 , y , Color.BLUE );
            }
        
            raster = new Projektor(
                database.maxRange / 3. ,
                new Punkt( 0. , 0. , database.maxRange / 3. , 0 ) ,
                new Punkt( 0. , 0. , -1. , 0 ) ,
                new Punkt( 0. , 1. , 0. , 0 ) 
            );
        
            raster.initialize();
            database.lastKnown = raster.upDir ;
            
            picture.setImageBitmap( database.projection );
        
        }
        
    }
}
